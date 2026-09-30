package com.example.similimumai.ui.viewmodel

import android.app.Application
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.similimumai.data.engine.CaseSheetPdfRenderer
import com.example.similimumai.data.engine.GeminiClinicalService
import com.example.similimumai.data.engine.HomeopathyKnowledgeEngine
import com.example.similimumai.data.local.ConsultationDatabase
import com.example.similimumai.data.local.entity.CaseRubricEntity
import com.example.similimumai.data.local.entity.DoctorEntity
import com.example.similimumai.data.local.entity.FollowUpEntity
import com.example.similimumai.data.local.entity.PatientEntity
import com.example.similimumai.data.local.entity.PrescriptionEntity
import com.example.similimumai.data.local.entity.SessionEntity
import com.example.similimumai.data.local.entity.SymptomRecordEntity
import com.example.similimumai.data.model.*
import com.example.similimumai.data.repository.ConsultationRepository
import com.example.similimumai.data.validation.ClinicalValidationRules
import com.example.similimumai.data.speech.AudioSpeechManager
import com.example.similimumai.data.speech.ClinicalSimulator
import com.example.similimumai.data.speech.SimulatedCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class ConsultationViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ConsultationRepository
    val audioSpeechManager: AudioSpeechManager = AudioSpeechManager(application)
    private val geminiService = GeminiClinicalService()

    private val _uiState = MutableStateFlow(ConsultationUiState())
    val uiState: StateFlow<ConsultationUiState> = _uiState.asStateFlow()

    private var simulationJob: Job? = null

    init {
        val database = ConsultationDatabase.getDatabase(application)
        repository = ConsultationRepository(
            database.patientDao(),
            database.sessionDao(),
            database.prescriptionDao(),
            database.symptomRecordDao(),
            database.followUpDao(),
            database.doctorDao(),
            database.caseRubricDao()
        )

        // Check Gemini status
        _uiState.update { it.copy(isGeminiAvailable = geminiService.isAvailable) }

        // Observe saved patients from Room
        viewModelScope.launch {
            repository.allPatients.collect { list ->
                _uiState.update { it.copy(savedPatients = list) }
            }
        }

        // Observe saved sessions from Room
        viewModelScope.launch {
            repository.allSessions.collect { list ->
                _uiState.update { it.copy(savedSessions = list) }
            }
        }

        // Observe saved prescriptions from Room
        viewModelScope.launch {
            repository.allPrescriptions.collect { list ->
                _uiState.update { it.copy(savedPrescriptions = list) }
            }
        }

        // Observe structured history for the saved-records review (schema.md tables 4/6/10)
        viewModelScope.launch {
            repository.allSymptomRecords.collect { list ->
                _uiState.update { it.copy(savedSymptomRecords = list) }
            }
        }
        viewModelScope.launch {
            repository.allFollowUps.collect { list ->
                _uiState.update { it.copy(savedFollowUps = list) }
            }
        }
        viewModelScope.launch {
            repository.allCaseRubrics.collect { list ->
                _uiState.update { it.copy(savedCaseRubrics = list) }
            }
        }

        // Load the local practitioner profile (schema.md table 1)
        viewModelScope.launch {
            repository.getDoctor().collect { doctor ->
                _uiState.update { it.copy(doctorProfile = doctor) }
            }
        }

        // Observe audio RMS level
        viewModelScope.launch {
            audioSpeechManager.liveRmsDb.collect { rms ->
                _uiState.update { it.copy(liveRmsDb = rms) }
            }
        }

        // Handle live recognized speech
        audioSpeechManager.onSpeechResultListener = { utterance ->
            processUtterance(utterance, SpeakerType.PATIENT)
        }

        // Load default initial case (Nat-m Case)
        loadInitialDefaultCase()

        // Pre-populate the LM calculator (LM1 standard) and Kent evaluator defaults
        _uiState.update {
            it.copy(
                lmProtocol = HomeopathyKnowledgeEngine.calculateLmProtocol(it.lmPotencyName),
                kentObservationResult = HomeopathyKnowledgeEngine.evaluateKentObservation(it.kentInput)
            )
        }
    }

    private fun loadInitialDefaultCase() {
        val defaultCase = ClinicalSimulator.availableCases[0]
        _uiState.update {
            it.copy(
                currentSimulatedCase = defaultCase,
                selectedRemedyDetail = HomeopathyKnowledgeEngine.polychrests.firstOrNull { r -> r.abbreviation == "Nat-m" },
                compareRemedyA = HomeopathyKnowledgeEngine.polychrests.firstOrNull { r -> r.abbreviation == "Nat-m" },
                compareRemedyB = HomeopathyKnowledgeEngine.polychrests.firstOrNull { r -> r.abbreviation == "Ign" }
            )
        }
        // Initialize with default rubrics
        addRubricById("r_mind_grief")
        addRubricById("r_head_sun_agg")
        addRubricById("r_stomach_salt_craving")
    }

    fun selectNavigationTab(tab: NavigationTab) {
        _uiState.update { it.copy(currentTab = tab) }
    }

    // Toggle live ambient microphone
    fun toggleMicrophone(enabled: Boolean) {
        if (enabled) {
            stopSimulation()
            audioSpeechManager.startListening()
            _uiState.update { it.copy(isMicListening = true) }
        } else {
            audioSpeechManager.stopListening()
            _uiState.update { it.copy(isMicListening = false, liveRmsDb = 0f) }
        }
    }

    // Stream a pre-configured clinical simulation case
    fun startSimulation(caseItem: SimulatedCase) {
        stopSimulation()
        audioSpeechManager.stopListening()

        // Clear previous consultation for fresh simulation
        _uiState.update {
            it.copy(
                isMicListening = false,
                isSimulationRunning = true,
                currentSimulatedCase = caseItem,
                simulationTurnIndex = 0,
                transcript = emptyList(),
                symptoms = emptyList(),
                activeRubrics = emptyList(),
                activeRedFlag = null,
                aiConstitutionalSynthesis = ""
            )
        }

        simulationJob = viewModelScope.launch {
            for ((index, turn) in caseItem.turns.withIndex()) {
                _uiState.update { it.copy(simulationTurnIndex = index) }
                val speaker = if (turn.speaker == "Doctor") SpeakerType.DOCTOR else SpeakerType.PATIENT
                processUtterance(turn.utterance, speaker)
                delay(turn.delayMs)
            }
            _uiState.update { it.copy(isSimulationRunning = false) }
        }
    }

    fun stopSimulation() {
        simulationJob?.cancel()
        simulationJob = null
        _uiState.update { it.copy(isSimulationRunning = false) }
    }

    // Core Utterance Processing
    fun processUtterance(text: String, speaker: SpeakerType) {
        if (text.isBlank()) return

        val entry = TranscriptEntry(
            id = UUID.randomUUID().toString(),
            speaker = speaker,
            text = text
        )

        _uiState.update { current ->
            current.copy(transcript = current.transcript + entry)
        }

        // Screen for emergency red flags
        val redFlag = HomeopathyKnowledgeEngine.screenForRedFlags(text)
        if (redFlag != null) {
            _uiState.update { it.copy(activeRedFlag = redFlag) }
        }

        if (speaker == SpeakerType.PATIENT) {
            val symptom = HomeopathyKnowledgeEngine.parseUtteranceToSymptom(text)
            addSymptom(symptom)
        }
    }

    fun addSymptom(symptom: Symptom) {
        _uiState.update { current ->
            val updatedList = current.symptoms + symptom
            val questions = HomeopathyKnowledgeEngine.generateHighYieldQuestions(updatedList)
            current.copy(
                symptoms = updatedList,
                highYieldQuestions = questions
            )
        }

        // If symptom matched a canonical rubric, add to active rubrics
        if (symptom.canonicalRubric.isNotBlank()) {
            val matchingRubric = HomeopathyKnowledgeEngine.rubrics.find { it.name == symptom.canonicalRubric }
            if (matchingRubric != null) {
                addRubric(matchingRubric)
            }
        }
    }

    fun addDoctorObservation(obs: DoctorObservationOption) {
        val entry = TranscriptEntry(
            id = UUID.randomUUID().toString(),
            speaker = SpeakerType.SYSTEM,
            text = "Doctor Physical Observation noted: [${obs.label}]"
        )

        val symptom = Symptom(
            id = UUID.randomUUID().toString(),
            location = obs.chapter,
            sensation = obs.label,
            modalities = "Physical Sign",
            concomitants = "",
            intensity = 3,
            isPqrs = obs.pqrs,
            rawUtterance = obs.label,
            canonicalRubric = obs.rubricName,
            completenessScore = 75
        )

        _uiState.update { it.copy(transcript = it.transcript + entry) }
        addSymptom(symptom)

        val rubric = HomeopathyKnowledgeEngine.rubrics.find { it.name.contains(obs.rubricName, ignoreCase = true) }
        if (rubric != null) {
            addRubric(rubric)
        }
    }

    fun addRubric(rubric: Rubric) {
        if (_uiState.value.activeRubrics.any { it.id == rubric.id }) return

        _uiState.update { current ->
            val updatedRubrics = current.activeRubrics + rubric
            val scores = HomeopathyKnowledgeEngine.calculateRepertorization(
                activeRubrics = updatedRubrics,
                school = current.selectedSchool,
                eliminateThermal = current.thermalEliminationFilter
            )

            // Auto-update top candidate in Rx if available
            val topRemedy = scores.firstOrNull()?.remedy?.abbreviation ?: current.rxRemedyName

            current.copy(
                activeRubrics = updatedRubrics,
                remedyScores = scores,
                rxRemedyName = topRemedy
            )
        }
    }

    fun addRubricById(id: String) {
        val r = HomeopathyKnowledgeEngine.rubrics.find { it.id == id } ?: return
        addRubric(r)
    }

    fun removeRubric(rubricId: String) {
        _uiState.update { current ->
            val updated = current.activeRubrics.filterNot { it.id == rubricId }
            val scores = HomeopathyKnowledgeEngine.calculateRepertorization(
                activeRubrics = updated,
                school = current.selectedSchool,
                eliminateThermal = current.thermalEliminationFilter
            )
            current.copy(
                activeRubrics = updated,
                remedyScores = scores
            )
        }
    }

    fun setRepertorySchool(school: RepertorySchool) {
        _uiState.update { current ->
            val scores = HomeopathyKnowledgeEngine.calculateRepertorization(
                activeRubrics = current.activeRubrics,
                school = school,
                eliminateThermal = current.thermalEliminationFilter
            )
            current.copy(
                selectedSchool = school,
                remedyScores = scores
            )
        }
    }

    fun setThermalEliminationFilter(filter: ThermalState?) {
        _uiState.update { current ->
            val scores = HomeopathyKnowledgeEngine.calculateRepertorization(
                activeRubrics = current.activeRubrics,
                school = current.selectedSchool,
                eliminateThermal = filter
            )
            current.copy(
                thermalEliminationFilter = filter,
                remedyScores = scores
            )
        }
    }

    fun dismissRedFlag() {
        _uiState.update { it.copy(activeRedFlag = null) }
    }

    fun selectRemedyDetail(remedy: Remedy) {
        _uiState.update { it.copy(selectedRemedyDetail = remedy) }
    }

    fun setComparisonRemedies(remedyA: Remedy, remedyB: Remedy) {
        val (isConflict, reason) = HomeopathyKnowledgeEngine.checkInimicalConflict(remedyA, remedyB)
        _uiState.update {
            it.copy(
                compareRemedyA = remedyA,
                compareRemedyB = remedyB,
                inimicalAlertMessage = if (isConflict) reason else null
            )
        }
    }

    fun updatePrescription(remedy: String, potency: String, scale: String, posology: String) {
        // §2.6 Inimical Check Rule: compare against the most recent saved prescription
        val lastPrescribed = _uiState.value.savedPrescriptions.firstOrNull()?.prescribedRemedy
        val conflict = ClinicalValidationRules.inimicalConflict(lastPrescribed, remedy)
        _uiState.update {
            it.copy(
                rxRemedyName = remedy,
                rxPotency = potency,
                rxScale = scale,
                rxPosology = posology,
                inimicalConflict = conflict,
                inimicalJustification = if (conflict == null) "" else it.inimicalJustification
            )
        }
    }

    fun setInimicalJustification(text: String) {
        _uiState.update { it.copy(inimicalJustification = text) }
    }

    fun setCaseMode(mode: CaseMode) {
        _uiState.update { it.copy(caseMode = mode) }
    }

    // Practitioner profile (schema.md table 1)
    fun updateDoctorProfile(
        fullName: String,
        registrationNumber: String,
        qualification: String,
        clinicName: String,
        clinicAddress: String,
        phoneNumber: String,
        email: String
    ) {
        // §2.1 Doctor registration validation — invalid profiles never reach Room
        val errors = ClinicalValidationRules.validateDoctorProfile(fullName, registrationNumber, qualification)
        if (errors.isNotEmpty()) {
            _uiState.update { it.copy(doctorProfileError = errors.joinToString(" • ")) }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(doctorProfileError = null) }
            repository.saveDoctor(
                DoctorEntity(
                    fullName = fullName,
                    registrationNumber = registrationNumber,
                    qualification = qualification,
                    clinicName = clinicName,
                    clinicAddress = clinicAddress,
                    phoneNumber = phoneNumber,
                    email = email,
                    updatedAt = System.currentTimeMillis()
                )
            )
        }
    }

    // Vision Lab: record a visual inspection finding into the consultation
    fun recordVisualFinding(finding: VisualFinding) {
        val rubric = if (finding.rubricId.isNotBlank()) {
            HomeopathyKnowledgeEngine.rubrics.find { it.id == finding.rubricId }
        } else null
        val symptom = Symptom(
            id = UUID.randomUUID().toString(),
            location = finding.panel.label,
            sensation = finding.label,
            modalities = "Doctor's silent physical observation",
            isPqrs = rubric?.isPqrs ?: true,
            rawUtterance = "Vision Lab finding: ${finding.label}",
            canonicalRubric = rubric?.name ?: "",
            completenessScore = 60
        )
        addSymptom(symptom)
        if (rubric != null) addRubric(rubric)
    }

    // LM 50-Millesimal dilution protocol (Organon §270-§272)
    fun updateLmParameters(potency: String, isHypersensitive: Boolean) {
        val protocol = HomeopathyKnowledgeEngine.calculateLmProtocol(potency, isHypersensitive)
        _uiState.update {
            it.copy(
                lmPotencyName = potency,
                lmHypersensitive = isHypersensitive,
                lmProtocol = protocol
            )
        }
    }

    // Kent's 12 Prognostic Observations evaluator
    fun evaluateKentObservation(input: KentReactionInput) {
        val result = HomeopathyKnowledgeEngine.evaluateKentObservation(input)
        _uiState.update { it.copy(kentInput = input, kentObservationResult = result) }
    }

    // Plain-text case sheet + prescription clipboard generator (MVP MVE criterion 4)
    fun copyCaseSheetToClipboard(): String {
        val sheet = buildCaseSheetText()
        val context = getApplication()
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboard.setPrimaryClip(ClipData.newPlainText("Similimum AI Case Sheet", sheet))
        return sheet
    }

    // PDF Case Record export (docs/05 Phase 4): returns absolute file path or null on failure
    fun exportCaseSheetPdf(): String? {
        return try {
            val app: Application = getApplication()
            val dir = app.getExternalFilesDir(null) ?: return null
            val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
            val file = File(dir, "case_sheet_$timestamp.pdf")
            file.writeBytes(CaseSheetPdfRenderer.renderPdf(buildCaseSheetText()))
            file.absolutePath
        } catch (e: Exception) {
            null
        }
    }

    private fun buildCaseSheetText(): String {
        val state = _uiState.value
        val generated = HomeopathyKnowledgeEngine.generateCaseSheet(
            patientName = state.patientName,
            patientAge = state.patientAge,
            patientSex = state.patientSex,
            thermal = state.patientThermal,
            miasm = state.patientMiasm,
            chiefComplaint = state.chiefComplaint,
            caseMode = state.caseMode.label,
            symptoms = state.symptoms,
            activeRubrics = state.activeRubrics,
            remedyScores = state.remedyScores,
            rxRemedy = state.rxRemedyName,
            rxPotency = state.rxPotency,
            rxScale = state.rxScale,
            rxPosology = state.rxPosology,
            dietaryRestrictions = state.rxDietaryRestrictions,
            hering = state.heringEvaluation,
            lmProtocol = state.lmProtocol
        )

        // Append the local practitioner signature (schema.md table 1) when known
        val doctor = state.doctorProfile
        if (doctor == null || doctor.fullName.isBlank()) return generated
        val practitionerLine = buildString {
            append("Prescribed By: Dr. ").append(doctor.fullName)
            if (doctor.registrationNumber.isNotBlank()) append(" (Reg. ").append(doctor.registrationNumber).append(")")
            if (doctor.clinicName.isNotBlank()) append(" — ").append(doctor.clinicName)
            if (doctor.phoneNumber.isNotBlank()) append(" — ").append(doctor.phoneNumber)
        }
        val lines = generated.lines()
        val titleIndex = lines.indexOfFirst { it.trimStart().startsWith("SIMILIMUM AI") }
        return if (titleIndex >= 0) {
            (lines.take(titleIndex + 1) + listOf(practitionerLine) + lines.drop(titleIndex + 1)).joinToString("\n")
        } else {
            generated + "\n\n$practitionerLine"
        }
    }

    fun evaluateHering(insideToOut: Boolean, aboveDown: Boolean, vitalToLess: Boolean, reverseTime: Boolean) {
        val evaluation = HomeopathyKnowledgeEngine.evaluateHeringProgression(
            insideToOutside = insideToOut,
            aboveDownwards = aboveDown,
            moreVitalToLess = vitalToLess,
            reverseOrder = reverseTime
        )
        _uiState.update { it.copy(heringEvaluation = evaluation) }
    }

    fun triggerGeminiDeepAnalysis() {
        val fullTranscript = _uiState.value.transcript.joinToString("\n") { "${it.speaker}: ${it.text}" }
        val symptoms = _uiState.value.symptoms

        _uiState.update { it.copy(isAiAnalyzing = true) }

        viewModelScope.launch {
            val result = geminiService.analyzeCaseTranscript(fullTranscript, symptoms)
            _uiState.update { current ->
                current.copy(
                    isAiAnalyzing = false,
                    aiConstitutionalSynthesis = "${result.source}\n\n${result.constitutionalSummary}\nDominant Miasm: ${result.dominantMiasm}\nThermal Profile: ${result.thermalVerdict}",
                    highYieldQuestions = result.questions
                )
            }
            // Apply AI-suggested rubrics (already matched to the canonical local database)
            result.suggestedRubrics.forEach { addRubric(it) }
            // Escalate AI-detected emergency red flags
            result.redFlags.firstOrNull()?.let { redFlag ->
                _uiState.update { it.copy(activeRedFlag = it.activeRedFlag ?: redFlag) }
            }
        }
    }

    // Save Consultation Session to Room Database
    fun saveSessionToDatabase() {
        viewModelScope.launch {
            // docs/data/validation-rules.md §2.2/§2.3 — gate persistence on data integrity
            val state0 = _uiState.value
            val integrityErrors = ClinicalValidationRules.validatePatient(
                state0.patientName, state0.patientAge, state0.patientSex
            ) + ClinicalValidationRules.validateChiefComplaint(state0.chiefComplaint)
            if (integrityErrors.isNotEmpty()) {
                _uiState.update { it.copy(saveError = integrityErrors.joinToString(" • ")) }
                return@launch
            }

            // §2.6 Inimical Check Rule — block unless a typed justification overrides
            val lastPrescribed = state0.savedPrescriptions.firstOrNull()?.prescribedRemedy
            val inimical = ClinicalValidationRules.inimicalConflict(lastPrescribed, state0.rxRemedyName)
            if (ClinicalValidationRules.inimicalJustificationRequired(inimical, state0.inimicalJustification)) {
                _uiState.update {
                    it.copy(saveError = "INIMICAL BLOCK — $inimical Type a clinical justification to override.")
                }
                return@launch
            }

            val patient = PatientEntity(
                mnr = "SIM-${System.currentTimeMillis() % 10000}",
                name = _uiState.value.patientName,
                age = _uiState.value.patientAge,
                sex = _uiState.value.patientSex,
                thermalState = _uiState.value.patientThermal.name,
                dominantMiasm = _uiState.value.patientMiasm.name,
                chiefComplaint = _uiState.value.chiefComplaint
            )
            val patientId = repository.savePatient(patient)

            val state = _uiState.value
            val session = SessionEntity(
                patientId = patientId,
                sessionDate = System.currentTimeMillis(),
                consultationType = state.caseMode.name,
                caseStatus = "ACTIVE",
                summaryNotes = state.symptoms.joinToString("; ") { "${it.location} - ${it.sensation} (${it.modalities})" },
                totalityScore = (state.symptoms.map { it.completenessScore }.average().takeIf { !it.isNaN() } ?: 60.0).toInt(),
                prescribedRemedy = state.rxRemedyName,
                potency = state.rxPotency,
                posology = state.rxPosology,
                heringStatus = state.heringEvaluation?.prognosisVerdict ?: "Not Evaluated",
                transcriptText = state.transcript.joinToString("\n") { "${it.speaker}: ${it.text}" },
                clinicalNotes = state.aiConstitutionalSynthesis,
                repertorySchool = state.selectedSchool.name
            )
            val sessionId = repository.saveSession(session)

            // Persist the active canonical rubric set (docs/data/schema.md table 6)
            repository.saveCaseRubrics(
                state.activeRubrics.map { r ->
                    CaseRubricEntity(
                        sessionId = sessionId,
                        rubricId = r.id,
                        weight = r.weight,
                        isEliminating = r.isEliminating
                    )
                }
            )

            // Persist the prescription record (docs/03 §5: PrescriptionEntity)
            val prescription = PrescriptionEntity(
                sessionId = sessionId,
                prescribedRemedy = _uiState.value.rxRemedyName,
                potencyScale = _uiState.value.rxScale,
                potency = _uiState.value.rxPotency,
                posology = _uiState.value.rxPosology,
                heringPrognosis = _uiState.value.heringEvaluation?.prognosisVerdict ?: "Not Evaluated"
            )
            repository.savePrescription(prescription)

            // Persist structured LSMC symptom records (docs/data/schema.md table 4).
            // §2.4: raw_utterance is capped at 1,000 characters at persistence time.
            repository.saveSymptomRecords(
                state.symptoms.map { s ->
                    SymptomRecordEntity(
                        id = s.id,
                        sessionId = sessionId,
                        rawUtterance = s.rawUtterance.take(ClinicalValidationRules.MAX_RAW_UTTERANCE_CHARS),
                        location = s.location,
                        sensation = s.sensation,
                        modalities = s.modalities,
                        concomitant = s.concomitants,
                        canonicalRubric = s.canonicalRubric,
                        isPqrs = s.isPqrs,
                        completenessScore = s.completenessScore
                    )
                }
            )

            // Persist the Hering/Kent follow-up outcome (docs/data/schema.md table 10)
            val hering = state.heringEvaluation
            val kent = state.kentObservationResult
            if (hering != null || kent != null) {
                repository.saveFollowUp(
                    FollowUpEntity(
                        sessionId = sessionId,
                        sessionDate = System.currentTimeMillis(),
                        insideOutward = hering?.insideToOutside ?: false,
                        aboveDownward = hering?.aboveDownwards ?: false,
                        vitalToLessVital = hering?.moreVitalToLess ?: false,
                        reverseOrderAppearance = hering?.reverseOrderOfTime ?: false,
                        kentObservationIndex = kent?.observation?.number ?: 0,
                        clinicalAssessment = hering?.prognosisVerdict
                            ?: kent?.summary
                            ?: "Not Evaluated",
                        recommendedAction = kent?.action?.name ?: ""
                    )
                )
            }

            _uiState.update {
                it.copy(
                    lastSavedSessionId = sessionId,
                    saveError = null,
                    inimicalJustification = ""
                )
            }
        }
    }

    fun updatePatientProfile(name: String, age: Int, sex: String, thermal: ThermalState, miasm: Miasm, complaint: String) {
        _uiState.update {
            it.copy(
                patientName = name,
                patientAge = age,
                patientSex = sex,
                patientThermal = thermal,
                patientMiasm = miasm,
                chiefComplaint = complaint
            )
        }
    }
}

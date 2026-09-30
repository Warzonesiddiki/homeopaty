package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.audio.AudioSpeechManager
import com.example.data.local.ConsultationDatabase
import com.example.data.local.entity.*
import com.example.data.repository.ConsultationRepository
import com.example.engine.GeminiClinicalService
import com.example.engine.HomeopathyKnowledgeEngine
import com.example.model.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

data class ConsultationUiState(
    val activePatient: Patient = Patient(
        id = "P-101",
        name = "Mrs. Kavita Sharma",
        age = 34,
        gender = "Female",
        mode = CaseMode.CHRONIC,
        chiefComplaintPreview = "Chronic Hammering Migraine after Grief",
        previousRemedyGiven = null,
        consentObtained = true
    ),
    val isRecording: Boolean = false,
    val recordingSeconds: Int = 0,
    val liveAudioWaveform: Float = 0f,
    val activeRedFlag: RedFlagAlert? = null,
    val activeInimicalWarning: InimicalWarning? = null,
    val selectedSchool: RepertorySchool = RepertorySchool.KENT_HIERARCHY,
    val selectedSpecialty: SpecialtyTree = SpecialtyTree.CONSTITUTIONAL,
    val selectedSimulatedCaseId: String = "CASE-1",
    val isSimulatingStream: Boolean = false,
    val currentSimulatedTurnIndex: Int = 0,
    val transcriptTurns: List<TranscriptTurn> = emptyList(),
    val extractedSymptoms: List<ExtractedSymptom> = emptyList(),
    val activeRubrics: List<Rubric> = emptyList(),
    val repertorizationResults: List<RepertorizationEntry> = emptyList(),
    val priorityFollowUpQuestions: List<FollowUpQuestion> = emptyList(),
    val pillarsStatus: List<PillarStatus> = HomeopathyKnowledgeEngine.all12Pillars,
    val keynoteVerifications: List<KeynoteVerification> = emptyList(),
    val miasmDistribution: Map<Miasm, Int> = emptyMap(),
    val heringsAssessment: HeringsLawAssessment = HeringsLawAssessment(),
    val prescription: Prescription = Prescription(
        patientId = "P-101",
        patientName = "Mrs. Kavita Sharma",
        selectedRemedyName = "Natrum Muriaticum",
        selectedRemedyCode = "Nat-m"
    ),
    val geminiApiKey: String = "",
    val isHybridMode: Boolean = true,
    val isDarkMode: Boolean = false,
    val geminiSummary: String? = null,
    val isGeminiProcessing: Boolean = false,
    val savedPatients: List<PatientEntity> = emptyList(),
    val isDatabaseSaving: Boolean = false,
    val databaseStatusMessage: String? = null,
    val showPatientRecordsDialog: Boolean = false
)

class ConsultationViewModel(application: Application) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(ConsultationUiState())
    val uiState: StateFlow<ConsultationUiState> = _uiState.asStateFlow()

    private val database = ConsultationDatabase.getDatabase(application)
    private val repository = ConsultationRepository(database)

    private val speechManager = AudioSpeechManager(application)
    private var recordingTimerJob: Job? = null
    private var simulationStreamJob: Job? = null

    init {
        speechManager.onFinalResult = { recognizedText ->
            processUtterance(Speaker.PATIENT, recognizedText)
        }

        // Preload default case (Case 1)
        loadSimulatedCase("CASE-1")

        // Watch audio amplitude
        viewModelScope.launch {
            speechManager.amplitude.collect { amp ->
                _uiState.update { it.copy(liveAudioWaveform = amp) }
            }
        }

        // Observe local database patients
        viewModelScope.launch {
            repository.allPatients.collect { patients ->
                _uiState.update { it.copy(savedPatients = patients) }
            }
        }

        // Initialize Gemini API Key from BuildConfig if available
        val defaultApiKey = getBuildConfigApiKey()
        if (defaultApiKey.isNotBlank()) {
            _uiState.update { it.copy(geminiApiKey = defaultApiKey) }
        }
    }

    private fun getBuildConfigApiKey(): String {
        return try {
            val clazz = Class.forName("com.example.BuildConfig")
            val field = clazz.getField("GEMINI_API_KEY")
            val key = field.get(null) as? String ?: ""
            if (key == "DEFAULT_API_KEY" || key == "MY_GEMINI_API_KEY") "" else key
        } catch (_: Throwable) {
            ""
        }
    }

    fun loadSimulatedCase(caseId: String) {
        stopSimulation()
        stopRecording()
        val simCase = HomeopathyKnowledgeEngine.preloadedCases.find { it.id == caseId } ?: return

        _uiState.update { current ->
            current.copy(
                activePatient = Patient(
                    id = "P-${simCase.id}",
                    name = simCase.patientName,
                    age = simCase.age,
                    gender = simCase.gender,
                    mode = simCase.mode,
                    chiefComplaintPreview = simCase.title,
                    previousRemedyGiven = if (caseId == "CASE-4") "Bry" else null, // Mr Deshmukh took Bryonia previously
                    consentObtained = true
                ),
                selectedSimulatedCaseId = caseId,
                currentSimulatedTurnIndex = 0,
                transcriptTurns = emptyList(),
                extractedSymptoms = emptyList(),
                activeRubrics = emptyList(),
                repertorizationResults = emptyList(),
                priorityFollowUpQuestions = emptyList(),
                activeRedFlag = null,
                activeInimicalWarning = null,
                recordingSeconds = 0,
                prescription = current.prescription.copy(
                    patientId = "P-${simCase.id}",
                    patientName = simCase.patientName,
                    selectedRemedyName = simCase.expectedRemedy.split(" ").firstOrNull() ?: "Natrum Muriaticum"
                )
            )
        }
    }

    fun toggleRecording() {
        val currentlyRecording = _uiState.value.isRecording
        if (currentlyRecording) {
            stopRecording()
        } else {
            startRecording()
        }
    }

    private fun startRecording() {
        _uiState.update { it.copy(isRecording = true) }
        speechManager.startListening()
        startTimer()
    }

    private fun stopRecording() {
        _uiState.update { it.copy(isRecording = false) }
        speechManager.stopListening()
        recordingTimerJob?.cancel()
    }

    private fun startTimer() {
        recordingTimerJob?.cancel()
        recordingTimerJob = viewModelScope.launch {
            while (true) {
                delay(1000L)
                _uiState.update { it.copy(recordingSeconds = it.recordingSeconds + 1) }
            }
        }
    }

    fun toggleSimulatedStream() {
        if (_uiState.value.isSimulatingStream) {
            stopSimulation()
        } else {
            startSimulationStream()
        }
    }

    private fun startSimulationStream() {
        val simCase = HomeopathyKnowledgeEngine.preloadedCases.find { it.id == _uiState.value.selectedSimulatedCaseId } ?: return
        _uiState.update { it.copy(isSimulatingStream = true, isRecording = true) }
        startTimer()

        simulationStreamJob?.cancel()
        simulationStreamJob = viewModelScope.launch {
            while (_uiState.value.currentSimulatedTurnIndex < simCase.turns.size && _uiState.value.isSimulatingStream) {
                val turn = simCase.turns[_uiState.value.currentSimulatedTurnIndex]
                speechManager.setSimulatedAmplitude(0.75f)
                processUtterance(turn.speaker, turn.text, turn.vocalTag)
                _uiState.update { it.copy(currentSimulatedTurnIndex = it.currentSimulatedTurnIndex + 1) }
                delay(1500L)
                speechManager.setSimulatedAmplitude(0.1f)
                delay(turn.delayMs)
            }
            _uiState.update { it.copy(isSimulatingStream = false) }
        }
    }

    fun stepNextSimulatedTurn() {
        val simCase = HomeopathyKnowledgeEngine.preloadedCases.find { it.id == _uiState.value.selectedSimulatedCaseId } ?: return
        val index = _uiState.value.currentSimulatedTurnIndex
        if (index < simCase.turns.size) {
            val turn = simCase.turns[index]
            processUtterance(turn.speaker, turn.text, turn.vocalTag)
            _uiState.update { it.copy(currentSimulatedTurnIndex = index + 1) }
        }
    }

    private fun stopSimulation() {
        _uiState.update { it.copy(isSimulatingStream = false) }
        simulationStreamJob?.cancel()
    }

    fun addManualUtterance(speaker: Speaker, text: String) {
        if (text.isBlank()) return
        processUtterance(speaker, text)
    }

    private fun processUtterance(speaker: Speaker, text: String, vocalTag: String? = null) {
        val redFlag = HomeopathyKnowledgeEngine.checkRedFlag(text)
        val newSymptoms = HomeopathyKnowledgeEngine.extractSymptomsAndRubrics(text)

        val currentRubrics = _uiState.value.activeRubrics.toMutableList()
        for (sx in newSymptoms) {
            val rubricPath = sx.linkedRubricPath
            if (rubricPath != null) {
                val matched = HomeopathyKnowledgeEngine.allRubrics.find { it.path == rubricPath }
                if (matched != null && currentRubrics.none { it.path == matched.path }) {
                    currentRubrics.add(matched)
                }
            }
        }

        val repertorization = HomeopathyKnowledgeEngine.repertorizeWithSchool(currentRubrics, _uiState.value.selectedSchool)
        val miasms = HomeopathyKnowledgeEngine.calculateMiasmDistribution(currentRubrics)

        val updatedPillars = _uiState.value.pillarsStatus.map { pillar ->
            val isNowExplored = when (pillar.id) {
                1 -> currentRubrics.any { it.path.contains("AILMENTS FROM") }
                2 -> currentRubrics.any { it.path.contains("MIND") && !it.path.contains("AILMENTS FROM") }
                3 -> currentRubrics.any { it.path.contains("FASTIDIOUS") || it.path.contains("FEAR") }
                4 -> currentRubrics.any { it.path.contains("AIR") || it.path.contains("SUN") }
                5 -> currentRubrics.any { it.path.contains("THIRST") }
                6 -> currentRubrics.any { it.path.contains("FOOD and DRINKS") }
                7 -> currentRubrics.any { it.path.contains("SLEEP") || it.path.contains("midnight") }
                9 -> currentRubrics.any { it.chapter in listOf("HEAD", "STOMACH", "ABDOMEN", "EXTREMITIES") }
                else -> pillar.isExplored
            }
            pillar.copy(isExplored = isNowExplored)
        }

        // Get questions based on active specialty or general
        val followUps = if (_uiState.value.selectedSpecialty == SpecialtyTree.CONSTITUTIONAL) {
            HomeopathyKnowledgeEngine.getFollowUpQuestions(
                activeRubrics = currentRubrics,
                topRemedies = repertorization,
                unexploredPillars = updatedPillars.filter { !it.isExplored }.map { it.id }
            )
        } else {
            HomeopathyKnowledgeEngine.getSpecialtyQuestions(_uiState.value.selectedSpecialty)
        }

        val keynotes = mutableListOf<KeynoteVerification>()
        topRemediesKeynotes(repertorization.take(3), keynotes)

        val newTurn = TranscriptTurn(
            id = "TURN-${UUID.randomUUID().toString().take(6)}",
            speaker = speaker,
            text = text,
            extractedSymptoms = newSymptoms,
            isRedFlagTrigger = redFlag != null,
            detectedVocalBiomarker = vocalTag
        )

        _uiState.update { current ->
            current.copy(
                activeRedFlag = redFlag ?: current.activeRedFlag,
                transcriptTurns = current.transcriptTurns + newTurn,
                extractedSymptoms = current.extractedSymptoms + newSymptoms,
                activeRubrics = currentRubrics,
                repertorizationResults = repertorization,
                priorityFollowUpQuestions = followUps,
                pillarsStatus = updatedPillars,
                keynoteVerifications = keynotes,
                miasmDistribution = miasms,
                prescription = if (repertorization.isNotEmpty()) {
                    current.prescription.copy(
                        selectedRemedyCode = repertorization.first().remedyCode,
                        selectedRemedyName = repertorization.first().remedyName
                    )
                } else current.prescription
            )
        }

        if (_uiState.value.isHybridMode && _uiState.value.geminiApiKey.isNotBlank()) {
            triggerGeminiReasoning()
        }
    }

    private fun topRemediesKeynotes(topRemedies: List<RepertorizationEntry>, targetList: MutableList<KeynoteVerification>) {
        for (entry in topRemedies) {
            val mm = HomeopathyKnowledgeEngine.allRemedies[entry.remedyCode] ?: continue
            for ((idx, kn) in mm.characteristicKeynotes.take(4).withIndex()) {
                targetList.add(
                    KeynoteVerification(
                        id = "KN-${entry.remedyCode}-$idx",
                        remedyCode = entry.remedyCode,
                        keynoteText = kn,
                        isVerified = false
                    )
                )
            }
        }
    }

    fun runGeminiSynthesisNow() {
        triggerGeminiReasoning()
    }

    private fun triggerGeminiReasoning() {
        val apiKey = _uiState.value.geminiApiKey.ifBlank { getBuildConfigApiKey() }
        if (apiKey.isBlank()) return

        val transcript = _uiState.value.transcriptTurns.joinToString("\n") { "[${it.speaker}]: ${it.text}" }
        val patient = _uiState.value.activePatient

        viewModelScope.launch {
            _uiState.update { it.copy(isGeminiProcessing = true) }
            val geminiResult = GeminiClinicalService.consultGemini(
                apiKey = apiKey,
                transcriptText = if (transcript.isNotBlank()) transcript else "Patient presents with chief complaint: ${patient.chiefComplaintPreview}",
                patientName = patient.name,
                patientAge = patient.age,
                patientGender = patient.gender,
                caseMode = patient.mode.name
            )

            if (geminiResult != null) {
                val newQuestions = geminiResult.followUpQuestions.mapIndexed { idx, qText ->
                    FollowUpQuestion(
                        id = "GEM-Q-$idx",
                        priority = QuestionPriority.HIGH,
                        questionType = QuestionType.COMPLETE_LSMC,
                        questionText = qText,
                        clinicalRationale = "Gemini 2.5 Flash Constitutional Synthesis",
                        aphorismCite = "Organon §153"
                    )
                }

                _uiState.update { current ->
                    current.copy(
                        isGeminiProcessing = false,
                        geminiSummary = geminiResult.reasoningSummary,
                        priorityFollowUpQuestions = newQuestions + current.priorityFollowUpQuestions.filterNot { it.id.startsWith("GEM-Q") },
                        activeRedFlag = if (geminiResult.redFlagNotice != null && current.activeRedFlag == null) {
                            RedFlagAlert(
                                id = "RF-GEM-${UUID.randomUUID().toString().take(4)}",
                                severity = RedFlagSeverity.EMERGENCY,
                                condition = "AI Emergency Detection",
                                immediateAction = geminiResult.redFlagNotice
                            )
                        } else current.activeRedFlag
                    )
                }
            } else {
                _uiState.update { it.copy(isGeminiProcessing = false) }
            }
        }
    }

    fun setRepertorySchool(school: RepertorySchool) {
        _uiState.update { it.copy(selectedSchool = school) }
        recalculateTotality(_uiState.value.activeRubrics)
    }

    fun setSpecialtyTree(specialty: SpecialtyTree) {
        _uiState.update { current ->
            current.copy(
                selectedSpecialty = specialty,
                priorityFollowUpQuestions = HomeopathyKnowledgeEngine.getSpecialtyQuestions(specialty)
            )
        }
    }

    fun addRubricByPath(path: String) {
        val matched = HomeopathyKnowledgeEngine.allRubrics.find { it.path == path }
        if (matched != null) {
            addRubric(matched)
        } else {
            // Dynamic rubric creation if not in seed
            val dynamicRubric = Rubric(
                id = "DRUB-${UUID.randomUUID().toString().take(4)}",
                path = path,
                chapter = path.split(" - ").firstOrNull() ?: "GENERALS",
                weight = 3,
                grades = mapOf("Nat-m" to 3, "Puls" to 2, "Ars" to 2, "Sulph" to 2)
            )
            addRubric(dynamicRubric)
        }
    }

    fun queueLabQuestion(testName: String, questionText: String) {
        val labQuestion = FollowUpQuestion(
            id = "Q-LAB-${UUID.randomUUID().toString().take(4)}",
            priority = QuestionPriority.HIGH,
            questionType = QuestionType.PHYSICAL_GENERAL,
            questionText = questionText,
            clinicalRationale = "Lab investigation probe: $testName",
            aphorismCite = "Pathological General"
        )
        _uiState.update { current ->
            current.copy(
                priorityFollowUpQuestions = listOf(labQuestion) + current.priorityFollowUpQuestions.filterNot { it.id == labQuestion.id }
            )
        }
    }

    fun injectVocalBiomarker(vb: VocalBiomarker) {
        addRubricByPath(vb.rubricPath)
        val turn = TranscriptTurn(
            id = "VB-${UUID.randomUUID().toString().take(6)}",
            speaker = Speaker.DOCTOR,
            text = "Vocal Prosody Detected: ${vb.name} -> Logged '${vb.rubricPath}'"
        )
        _uiState.update { it.copy(transcriptTurns = it.transcriptTurns + turn) }
    }

    fun onQuestionAnswered(questionId: String, isPositive: Boolean) {
        val question = _uiState.value.priorityFollowUpQuestions.find { it.id == questionId } ?: return
        val updatedQuestions = _uiState.value.priorityFollowUpQuestions.filterNot { it.id == questionId }

        if (isPositive && question.expectedRubricIfPositive != null) {
            addRubricByPath(question.expectedRubricIfPositive)
        }

        _uiState.update { it.copy(priorityFollowUpQuestions = updatedQuestions) }
    }

    fun togglePinQuestion(questionId: String) {
        _uiState.update { current ->
            current.copy(
                priorityFollowUpQuestions = current.priorityFollowUpQuestions.map {
                    if (it.id == questionId) it.copy(isPinned = !it.isPinned) else it
                }
            )
        }
    }

    fun injectSilentObservation(obs: SilentObservation) {
        addRubricByPath(obs.rubricPath)
        val turn = TranscriptTurn(
            id = "OBS-${UUID.randomUUID().toString().take(6)}",
            speaker = Speaker.DOCTOR,
            text = "Silent Observation logged: ${obs.label} (${obs.rubricPath})"
        )
        _uiState.update { it.copy(transcriptTurns = it.transcriptTurns + turn) }
    }

    fun addRubric(rubric: Rubric) {
        val currentRubrics = _uiState.value.activeRubrics.toMutableList()
        if (currentRubrics.none { it.path == rubric.path }) {
            currentRubrics.add(rubric)
            recalculateTotality(currentRubrics)
        }
    }

    fun removeRubric(rubricId: String) {
        val currentRubrics = _uiState.value.activeRubrics.filterNot { it.id == rubricId }
        recalculateTotality(currentRubrics)
    }

    fun setRubricWeight(rubricId: String, newWeight: Int) {
        val currentRubrics = _uiState.value.activeRubrics.map {
            if (it.id == rubricId) it.copy(weight = newWeight) else it
        }
        recalculateTotality(currentRubrics)
    }

    fun toggleEliminatingRubric(rubricId: String) {
        val currentRubrics = _uiState.value.activeRubrics.map {
            if (it.id == rubricId) it.copy(isEliminating = !it.isEliminating) else it
        }
        recalculateTotality(currentRubrics)
    }

    private fun recalculateTotality(rubrics: List<Rubric>) {
        val results = HomeopathyKnowledgeEngine.repertorizeWithSchool(rubrics, _uiState.value.selectedSchool)
        val miasms = HomeopathyKnowledgeEngine.calculateMiasmDistribution(rubrics)
        val keynotes = mutableListOf<KeynoteVerification>()
        topRemediesKeynotes(results.take(3), keynotes)

        val followUps = if (_uiState.value.selectedSpecialty == SpecialtyTree.CONSTITUTIONAL) {
            HomeopathyKnowledgeEngine.getFollowUpQuestions(
                activeRubrics = rubrics,
                topRemedies = results,
                unexploredPillars = _uiState.value.pillarsStatus.filter { !it.isExplored }.map { it.id }
            )
        } else {
            HomeopathyKnowledgeEngine.getSpecialtyQuestions(_uiState.value.selectedSpecialty)
        }

        _uiState.update { current ->
            current.copy(
                activeRubrics = rubrics,
                repertorizationResults = results,
                miasmDistribution = miasms,
                keynoteVerifications = keynotes,
                priorityFollowUpQuestions = followUps,
                prescription = if (results.isNotEmpty()) {
                    current.prescription.copy(
                        selectedRemedyCode = results.first().remedyCode,
                        selectedRemedyName = results.first().remedyName
                    )
                } else current.prescription
            )
        }
    }

    fun toggleKeynoteVerification(keynoteId: String) {
        _uiState.update { current ->
            current.copy(
                keynoteVerifications = current.keynoteVerifications.map {
                    if (it.id == keynoteId) it.copy(isVerified = !it.isVerified) else it
                }
            )
        }
    }

    fun explorePillar(pillarId: Int) {
        val pillar = _uiState.value.pillarsStatus.find { it.id == pillarId } ?: return
        val newQuestion = FollowUpQuestion(
            id = "Q-PIL-$pillarId",
            priority = QuestionPriority.HIGH,
            questionType = QuestionType.COMPLETE_LSMC,
            questionText = pillar.defaultQuestion,
            clinicalRationale = "Inquiry into unexplored pillar: ${pillar.name}",
            aphorismCite = "Organon §83-§104"
        )
        _uiState.update { current ->
            current.copy(
                priorityFollowUpQuestions = listOf(newQuestion) + current.priorityFollowUpQuestions.filterNot { it.id == newQuestion.id }
            )
        }
    }

    fun updateHeringsAssessment(
        energyMoodBetter: Boolean,
        directionAboveDownward: Boolean,
        directionWithinOutward: Boolean,
        reverseOrder: Boolean,
        oldSkinReturned: Boolean,
        warningInwardShift: Boolean
    ) {
        val recommendation = when {
            warningInwardShift -> "⚠️ WARNING: Possible suppression! Disease has shifted from exterior to deeper vital organ. Re-evaluate case or consider antidoting previous prescription."
            oldSkinReturned && energyMoodBetter -> "✅ IDEAL CURE (Hering's Law): Old skin eruption returned while vital energy/mood improved. DO NOT SUPPRESS! Prescribe Placebo (Sac Lac) and wait."
            energyMoodBetter && directionWithinOutward -> "✅ Favorable response. Vital force is moving from center to periphery. Wait & Watch."
            else -> "Continue monitoring. Vital response is active."
        }

        _uiState.update {
            it.copy(
                heringsAssessment = HeringsLawAssessment(
                    energyMoodSleepBetter = energyMoodBetter,
                    directionAboveDownward = directionAboveDownward,
                    directionWithinOutward = directionWithinOutward,
                    reverseOrderOfAppearance = reverseOrder,
                    oldSkinEruptionReturned = oldSkinReturned,
                    warningInwardSuppression = warningInwardShift,
                    recommendation = recommendation
                )
            )
        }
    }

    fun updatePrescription(
        remedyName: String,
        remedyCode: String,
        potency: String,
        posology: String,
        vehicle: String,
        clinicalNotes: String
    ) {
        // Run Inimical Compatibility check!
        val inimicalWarning = HomeopathyKnowledgeEngine.checkInimicalCompatibility(
            previousRemedyCode = _uiState.value.activePatient.previousRemedyGiven,
            selectedRemedyCode = remedyCode
        )

        _uiState.update {
            it.copy(
                activeInimicalWarning = inimicalWarning,
                prescription = it.prescription.copy(
                    selectedRemedyName = remedyName,
                    selectedRemedyCode = remedyCode,
                    potency = potency,
                    posology = posology,
                    vehicle = vehicle,
                    clinicalNotes = clinicalNotes
                )
            )
        }
    }

    fun dismissInimicalWarning() {
        _uiState.update { it.copy(activeInimicalWarning = null) }
    }

    fun setConsentObtained(obtained: Boolean) {
        _uiState.update {
            it.copy(activePatient = it.activePatient.copy(consentObtained = obtained))
        }
    }

    fun setCaseMode(mode: CaseMode) {
        _uiState.update {
            it.copy(activePatient = it.activePatient.copy(mode = mode))
        }
    }

    fun setGeminiApiKey(key: String) {
        _uiState.update { it.copy(geminiApiKey = key.trim()) }
    }

    fun toggleHybridMode(enabled: Boolean) {
        _uiState.update { it.copy(isHybridMode = enabled) }
    }

    fun toggleDarkMode() {
        _uiState.update { it.copy(isDarkMode = !it.isDarkMode) }
    }

    fun dismissRedFlag() {
        _uiState.update { it.copy(activeRedFlag = null) }
    }

    fun setPatientRecordsDialogVisible(visible: Boolean) {
        _uiState.update { it.copy(showPatientRecordsDialog = visible) }
    }

    fun clearDatabaseStatusMessage() {
        _uiState.update { it.copy(databaseStatusMessage = null) }
    }

    fun saveCurrentConsultation() {
        val current = _uiState.value
        viewModelScope.launch {
            _uiState.update { it.copy(isDatabaseSaving = true) }
            try {
                // 1. Save or find Patient
                val mrn = current.activePatient.id
                val existingPatient = repository.getPatientByMrn(mrn)
                val patientId = existingPatient?.id ?: repository.savePatient(
                    PatientEntity(
                        mrn = mrn,
                        fullName = current.activePatient.name,
                        age = current.activePatient.age,
                        gender = current.activePatient.gender,
                        thermalPreference = "HOT",
                        miasmaticTendency = current.miasmDistribution.maxByOrNull { it.value }?.key?.name ?: "PSORIC"
                    )
                )

                // 2. Save Session
                val combinedTranscript = current.transcriptTurns.joinToString("\n") { "[${it.speaker.name}] ${it.text}" }
                val dominantMiasm = current.miasmDistribution.maxByOrNull { it.value }?.key?.name ?: "PSORA"
                val lsmcScore = if (current.extractedSymptoms.isNotEmpty()) {
                    (current.extractedSymptoms.map { it.lsmcCompletenessPercent }.average()).toInt().coerceIn(0, 100)
                } else 0

                val sessionEntity = ConsultationSessionEntity(
                    patientId = patientId,
                    mode = current.activePatient.mode.name,
                    transcript = combinedTranscript,
                    clinicalNotes = current.prescription.clinicalNotes,
                    lsmcScore = lsmcScore,
                    miasmaticDominance = dominantMiasm
                )

                val symptomEntities = current.extractedSymptoms.map { sx ->
                    SymptomRecordEntity(
                        sessionId = 0L,
                        location = sx.location,
                        sensation = sx.sensation,
                        modalityAgg = sx.modalitiesAggravation.joinToString("; "),
                        modalityAmel = sx.modalitiesAmelioration.joinToString("; "),
                        concomitant = sx.concomitants.joinToString("; "),
                        miasm = dominantMiasm,
                        isCompleteLsmc = sx.lsmcCompletenessPercent >= 75
                    )
                }

                val rubricEntities = current.activeRubrics.map { r ->
                    SelectedRubricEntity(
                        sessionId = 0L,
                        rubricId = r.id,
                        rubricPath = r.path,
                        weight = r.weight,
                        chapter = r.chapter,
                        remediesCount = r.grades.size
                    )
                }

                val sessionId = repository.saveFullConsultation(sessionEntity, symptomEntities, rubricEntities)

                // 3. Save Prescription
                val rx = current.prescription
                repository.savePrescription(
                    PrescriptionRecordEntity(
                        sessionId = sessionId,
                        patientId = patientId,
                        remedyCode = rx.selectedRemedyCode,
                        remedyName = rx.selectedRemedyName,
                        potency = rx.potency,
                        posologyScale = if (rx.potency.startsWith("LM")) "FIFTY_MILLESIMAL" else "CENTESIMAL",
                        repetitionInterval = rx.posology,
                        specialInstructions = rx.clinicalNotes,
                        status = "DISPENSED"
                    )
                )

                // Refresh recent remedies for Inimical safety check
                val recentRemedies = repository.getRecentRemediesForPatient(patientId)
                val lastRemedy = recentRemedies.firstOrNull()

                _uiState.update {
                    it.copy(
                        isDatabaseSaving = false,
                        databaseStatusMessage = "Consultation & Prescription saved to Room DB! (MRN: $mrn)",
                        activePatient = it.activePatient.copy(previousRemedyGiven = lastRemedy)
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isDatabaseSaving = false,
                        databaseStatusMessage = "Failed to save: ${e.localizedMessage ?: "Unknown error"}"
                    )
                }
            }
        }
    }

    fun loadSavedPatient(patient: PatientEntity) {
        stopSimulation()
        stopRecording()
        viewModelScope.launch {
            val recentRemedies = repository.getRecentRemediesForPatient(patient.id)
            val lastRemedy = recentRemedies.firstOrNull()

            _uiState.update { current ->
                current.copy(
                    activePatient = Patient(
                        id = patient.mrn,
                        name = patient.fullName,
                        age = patient.age,
                        gender = patient.gender,
                        mode = CaseMode.CHRONIC,
                        chiefComplaintPreview = "Saved Patient Record (${patient.mrn})",
                        previousRemedyGiven = lastRemedy,
                        consentObtained = true
                    ),
                    selectedSimulatedCaseId = "CUSTOM",
                    currentSimulatedTurnIndex = 0,
                    transcriptTurns = listOf(
                        TranscriptTurn(
                            id = "RECORD-INIT",
                            speaker = Speaker.DOCTOR,
                            text = "Loaded saved patient record: ${patient.fullName}, MRN: ${patient.mrn}. Previous remedy on file: ${lastRemedy ?: "None"}."
                        )
                    ),
                    extractedSymptoms = emptyList(),
                    activeRubrics = emptyList(),
                    repertorizationResults = emptyList(),
                    priorityFollowUpQuestions = emptyList(),
                    activeRedFlag = null,
                    activeInimicalWarning = null,
                    showPatientRecordsDialog = false,
                    databaseStatusMessage = "Loaded ${patient.fullName} (MRN: ${patient.mrn})"
                )
            }
        }
    }

    fun deleteSavedPatient(patientId: Long) {
        viewModelScope.launch {
            repository.deletePatient(patientId)
            _uiState.update { it.copy(databaseStatusMessage = "Patient record deleted successfully") }
        }
    }

    fun createNewWalkInPatient(name: String, age: Int, gender: String, chiefComplaint: String) {
        stopSimulation()
        stopRecording()
        val newMrn = "SIM-${System.currentTimeMillis().toString().takeLast(6)}"
        _uiState.update { current ->
            current.copy(
                activePatient = Patient(
                    id = newMrn,
                    name = name.ifBlank { "New Walk-in Patient" },
                    age = age,
                    gender = gender,
                    mode = CaseMode.CHRONIC,
                    chiefComplaintPreview = chiefComplaint.ifBlank { "Acute/Chronic Case Taking" },
                    previousRemedyGiven = null,
                    consentObtained = true
                ),
                selectedSimulatedCaseId = "NEW",
                currentSimulatedTurnIndex = 0,
                transcriptTurns = emptyList(),
                extractedSymptoms = emptyList(),
                activeRubrics = emptyList(),
                repertorizationResults = emptyList(),
                priorityFollowUpQuestions = emptyList(),
                activeRedFlag = null,
                activeInimicalWarning = null,
                recordingSeconds = 0,
                showPatientRecordsDialog = false,
                databaseStatusMessage = "Initialized new case for $name ($newMrn)"
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        speechManager.stopListening()
        recordingTimerJob?.cancel()
        simulationStreamJob?.cancel()
    }
}

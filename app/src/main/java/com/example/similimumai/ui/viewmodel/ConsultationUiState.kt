package com.example.similimumai.ui.viewmodel

import com.example.similimumai.data.local.entity.CaseRubricEntity
import com.example.similimumai.data.local.entity.DoctorEntity
import com.example.similimumai.data.local.entity.FollowUpEntity
import com.example.similimumai.data.local.entity.PatientEntity
import com.example.similimumai.data.local.entity.PrescriptionEntity
import com.example.similimumai.data.local.entity.SessionEntity
import com.example.similimumai.data.local.entity.SymptomRecordEntity
import com.example.similimumai.data.model.*
import com.example.similimumai.data.speech.SimulatedCase

enum class NavigationTab(val label: String) {
    HUD("Co-Pilot HUD"),
    LSMC_RADAR("LSMC Radar"),
    REPERTORY("Repertory"),
    MATERIA_MEDICA("Materia Medica"),
    RX_HERING("Rx & Hering"),
    VISION_LAB("Vision Lab")
}

data class ConsultationUiState(
    val currentTab: NavigationTab = NavigationTab.HUD,

    // Patient Profile
    val patientName: String = "Priya Sharma",
    val patientAge: Int = 32,
    val patientSex: String = "Female",
    val patientThermal: ThermalState = ThermalState.HOT,
    val patientMiasm: Miasm = Miasm.PSORA,
    val chiefComplaint: String = "Severe throbbing right-sided headache & silent grief",

    // Live Consult Stream
    val transcript: List<TranscriptEntry> = emptyList(),
    val symptoms: List<Symptom> = emptyList(),
    val activeRubrics: List<Rubric> = emptyList(),
    val remedyScores: List<RemedyScore> = emptyList(),
    val highYieldQuestions: List<HighYieldQuestion> = emptyList(),
    val activeRedFlag: RedFlagAlert? = null,

    // Audio & Simulation
    val isMicListening: Boolean = false,
    val liveRmsDb: Float = 0f,
    val isSimulationRunning: Boolean = false,
    val currentSimulatedCase: SimulatedCase? = null,
    val simulationTurnIndex: Int = 0,

    // Repertory Options
    val selectedSchool: RepertorySchool = RepertorySchool.KENT,
    val thermalEliminationFilter: ThermalState? = null,

    // Materia Medica & Safety
    val selectedRemedyDetail: Remedy? = null,
    val compareRemedyA: Remedy? = null,
    val compareRemedyB: Remedy? = null,
    val inimicalAlertMessage: String? = null,

    // Prescription & Hering
    val rxRemedyName: String = "Nat-m",
    val rxPotency: String = "200C",
    val rxScale: String = "Centesimal",
    val rxPosology: String = "Single dose on tongue, followed by Placebo once daily for 14 days",
    val rxDietaryRestrictions: List<String> = listOf("Raw Onion", "Garlic", "Strong Coffee", "Camphor & Menthol"),
    val heringEvaluation: HeringEvaluation? = null,
    val lastSavedSessionId: Long? = null,

    // Case Mode (docs/data/enums.md)
    val caseMode: CaseMode = CaseMode.CHRONIC,

    // LM 50-Millesimal Dilution Calculator
    val lmPotencyName: String = "LM1",
    val lmHypersensitive: Boolean = false,
    val lmProtocol: LmProtocol? = null,

    // Kent's 12 Prognostic Observations
    val kentInput: KentReactionInput = KentReactionInput(),
    val kentObservationResult: KentObservationResult? = null,

    // AI & Intelligence
    val isGeminiAvailable: Boolean = false,
    val isAiAnalyzing: Boolean = false,
    val aiConstitutionalSynthesis: String = "",

    // Practitioner profile (schema.md table 1)
    val doctorProfile: DoctorEntity? = null,

    // Room Database Saved Records
    val savedPatients: List<PatientEntity> = emptyList(),
    val savedSessions: List<SessionEntity> = emptyList(),
    val savedPrescriptions: List<PrescriptionEntity> = emptyList(),
    val savedSymptomRecords: List<SymptomRecordEntity> = emptyList(),
    val savedFollowUps: List<FollowUpEntity> = emptyList(),
    val savedCaseRubrics: List<CaseRubricEntity> = emptyList()
)

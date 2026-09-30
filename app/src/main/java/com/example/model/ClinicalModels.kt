package com.example.model

enum class CaseMode(val label: String) {
    CHRONIC("CHRONIC CONSTITUTIONAL"),
    ACUTE("ACUTE OPD"),
    FOLLOW_UP("FOLLOW-UP (HERING)")
}

enum class Speaker(val label: String) {
    PATIENT("PATIENT"),
    DOCTOR("DOCTOR"),
    ATTENDANT("ATTENDANT / PARENT")
}

enum class SymptomCategory(val label: String, val defaultWeight: Int) {
    CAUSATION("Ailments From", 3),
    MENTAL_GENERAL("Mental General", 3),
    PHYSICAL_GENERAL("Physical General", 2),
    PARTICULAR("Particular", 1),
    PQRS("⚡ PQRS §153", 3)
}

enum class QuestionPriority {
    CRITICAL,
    HIGH,
    MEDIUM
}

enum class QuestionType(val badge: String) {
    RED_FLAG("🚨 RED FLAG CHECK"),
    COMPLETE_LSMC("🎯 COMPLETE LSMC"),
    DIFFERENTIATE_REMEDY("⚖️ DIFFERENTIATE REMEDY"),
    MENTAL_CAUSATION("🧠 MENTAL / CAUSATION"),
    PHYSICAL_GENERAL("🌡️ PHYSICAL GENERAL"),
    KINGDOM_MIASM("👑 KINGDOM / MIASM")
}

enum class RedFlagSeverity {
    EMERGENCY,
    WARNING
}

enum class Miasm(val label: String, val description: String) {
    PSORA("Psora", "Functional disturbance, sensitivity, itching, deficiency, irritability"),
    SYCOSIS("Sycosis", "Proliferation, overgrowth, warts, secretiveness, dampness agg."),
    SYPHILIS("Syphilis", "Destruction, ulceration, deep tissue changes, night agg., despair"),
    TUBERCULAR("Tubercular", "Cosmopolitan, changeable, respiratory, wasting, rapid succession")
}

enum class SpecialtyTree(val title: String, val icon: String, val summary: String) {
    CONSTITUTIONAL("Constitutional", "🧬", "Comprehensive Totality, Causation, Generals & Mentals"),
    PEDIATRIC("Pediatrics & Infant", "👶", "Carrying modalities, fontanelles, sleep position, maternal state"),
    DERMATOLOGY("Dermatology", "🧴", "Suppression history, discharge character, itching heat/bed <"),
    RHEUMATOLOGY("Rheumatology", "🦴", "Motion paradox (first vs continued), ascending vs descending direction"),
    GYNECOLOGY("Gynecology & PCOS", "🌸", "Menstrual flow relief, bearing-down, hormonal temperament"),
    PSYCHIATRY_DREAMS("Psychiatry & Dreams", "🌙", "Unconscious themes: Robbers, snakes, dead relatives, water, falling")
}

enum class RepertorySchool(val label: String, val author: String, val focusDescription: String) {
    KENT_HIERARCHY("Kentian Classical", "Dr. J.T. Kent", "Deductive: Mentals & PQRS ×3 -> Physical Generals ×2 -> Particulars ×1"),
    BOENNINGHAUSEN_TPB("Boenninghausen TPB", "C. von Boenninghausen", "Inductive: Grand Generalization of Modalities (< & >) and Concomitants"),
    BOGER_BBCR("Boger BBCR", "Dr. C.M. Boger", "Pathological Generals, Tissue Affinity (Mucous, Serous, Glands, Nerves)"),
    SANKARAN_KINGDOM("Sankaran Sensation", "Dr. Rajan Sankaran", "Vital Sensation: Plant (Sensitivity), Mineral (Structure), Animal (Survival)"),
    SEHGAL_MIND("Sehgal Mind Method", "Dr. M.L. Sehgal", "Bedside Present State: Fear, delusion, light desire, carried fast")
}

enum class SankaranKingdom(val label: String, val theme: String, val representativeRemedies: String) {
    PLANT("Plant Kingdom", "Sensitivity & Reactivity (Adaptable, soft, varied shifting physical sensations)", "Pulsatilla, Ignatia, Chamomilla, Rhus-t, Bryonia"),
    MINERAL("Mineral Kingdom", "Structure, Order, Role, Security & Performance (Periodic Table Rows 2-6)", "Natrum-m, Calcarea-c, Phosphorus, Silica, Kali-c, Arsenic"),
    ANIMAL("Animal Kingdom", "Survival, Competition, Hierarchy, Aggressor vs Victim, Loquacity & Jealousy", "Lachesis, Apis, Sepia (mollusc), Cantharis, Tarentula"),
    NOSODE("Nosode / Imponderable", "Miasmatic Taint, chronic block ('Never Well Since infection/vaccination')", "Medorrhinum, Psorinum, Tuberculinum, Syphilinum")
}

data class Patient(
    val id: String,
    val name: String,
    val age: Int,
    val gender: String,
    val mode: CaseMode = CaseMode.CHRONIC,
    val chiefComplaintPreview: String,
    val previousRemedyGiven: String? = null,
    val consentObtained: Boolean = true
)

data class TranscriptTurn(
    val id: String,
    val speaker: Speaker,
    val text: String,
    val timestamp: Long = System.currentTimeMillis(),
    val extractedSymptoms: List<ExtractedSymptom> = emptyList(),
    val isRedFlagTrigger: Boolean = false,
    val detectedVocalBiomarker: String? = null
)

data class ExtractedSymptom(
    val id: String,
    val category: SymptomCategory,
    val clinicalSummary: String,
    val verbatimQuote: String,
    val location: String = "",
    val sensation: String = "",
    val modalitiesAggravation: List<String> = emptyList(),
    val modalitiesAmelioration: List<String> = emptyList(),
    val concomitants: List<String> = emptyList(),
    val lsmcCompletenessPercent: Int = 50,
    val linkedRubricPath: String? = null
)

data class FollowUpQuestion(
    val id: String,
    val priority: QuestionPriority,
    val questionType: QuestionType,
    val questionText: String,
    val clinicalRationale: String,
    val targetRemedies: List<String> = emptyList(),
    val expectedRubricIfPositive: String? = null,
    val isPinned: Boolean = false,
    val isAnswered: Boolean = false,
    val aphorismCite: String = "Organon §86"
)

data class RedFlagAlert(
    val id: String,
    val severity: RedFlagSeverity,
    val condition: String,
    val immediateAction: String,
    val vitalsToCheck: String = "Pulse, BP, Respiratory Rate, SpO2, Temp"
)

data class SilentObservation(
    val id: String,
    val label: String,
    val rubricPath: String,
    val category: SymptomCategory = SymptomCategory.MENTAL_GENERAL
)

data class PillarStatus(
    val id: Int,
    val name: String,
    val isExplored: Boolean,
    val details: String,
    val defaultQuestion: String
)

data class SimulatedCase(
    val id: String,
    val patientName: String,
    val age: Int,
    val gender: String,
    val mode: CaseMode,
    val title: String,
    val chiefComplaint: String,
    val expectedRemedy: String,
    val differentialRemedies: String,
    val isEmergency: Boolean = false,
    val turns: List<SimulatedTurn>
)

data class SimulatedTurn(
    val speaker: Speaker,
    val text: String,
    val delayMs: Long = 3500L,
    val vocalTag: String? = null
)

// Multimodal Vision & Lab Models
data class TongueSign(
    val id: String,
    val name: String,
    val description: String,
    val rubricPath: String,
    val keyRemedies: List<String>,
    val iconEmoji: String = "👅"
)

data class ObjectivePhysicalSign(
    val id: String,
    val category: String, // SKIN, NAIL, FACE
    val name: String,
    val description: String,
    val rubricPath: String,
    val keyRemedies: List<String>,
    val iconEmoji: String = "🔬"
)

data class LabInvestigationSign(
    val id: String,
    val testName: String,
    val abnormality: String,
    val organAffinity: String,
    val supportiveRemedies: List<String>,
    val suggestedModalityQuestion: String
)

data class VocalBiomarker(
    val id: String,
    val name: String,
    val description: String,
    val rubricPath: String,
    val keyRemedies: List<String>
)

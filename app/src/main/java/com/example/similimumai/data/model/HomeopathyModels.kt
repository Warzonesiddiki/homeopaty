package com.example.similimumai.data.model

data class Symptom(
    val id: String,
    val location: String = "",
    val sensation: String = "",
    val modalities: String = "",
    val concomitants: String = "",
    val intensity: Int = 2, // 1: Mild, 2: Moderate, 3: Severe
    val isPqrs: Boolean = false, // Peculiar, Rare, Striking (§153)
    val rawUtterance: String = "",
    val canonicalRubric: String = "",
    val completenessScore: Int = 0 // 0 to 100%
)

data class Rubric(
    val id: String,
    val chapter: String, // MIND, HEAD, STOMACH, EXTREMITIES, GENERALITIES, etc.
    val name: String,
    val remedyGrades: Map<String, Int>, // Remedy Abbreviation -> Grade (1, 2, 3)
    val isPqrs: Boolean = false,
    val isEliminating: Boolean = false,
    val weight: Int = 1 // 1, 2, or 3
)

data class Remedy(
    val id: String,
    val abbreviation: String,
    val fullName: String,
    val commonName: String,
    val thermalState: ThermalState,
    val dominantMiasm: Miasm,
    val keynotes: List<String>,
    val inimicalRemedies: List<String>,
    val complementaryRemedies: List<String>,
    val antidoteRemedies: List<String>
)

enum class ThermalState {
    CHILLY, HOT, AMBITHERMAL
}

enum class Miasm {
    PSORA, SYCOSIS, SYPHILIS, TUBERCULAR
}

enum class RepertorySchool {
    KENT, BOENNINGHAUSEN, BOGER
}

data class TranscriptEntry(
    val id: String,
    val speaker: SpeakerType,
    val text: String,
    val timestamp: Long = System.currentTimeMillis()
)

enum class SpeakerType {
    DOCTOR, PATIENT, SYSTEM
}

data class RedFlagAlert(
    val id: String,
    val condition: String,
    val matchedSymptoms: List<String>,
    val urgencyLevel: UrgencyLevel,
    val immediateAction: String
)

enum class UrgencyLevel {
    EMERGENCY, WARNING, INFORMATIONAL
}

data class HighYieldQuestion(
    val id: String,
    val question: String,
    val targetDimension: String, // Modality, Concomitant, Causation, Constitutional
    val clinicalRationale: String
)

data class RemedyScore(
    val remedy: Remedy,
    val totalScore: Int,
    val rubricsCovered: Int,
    val totalRubrics: Int,
    val gradeSum: Int,
    val hasInimicalConflict: Boolean = false,
    val conflictReason: String = ""
)

data class DoctorObservationOption(
    val id: String,
    val label: String,
    val rubricName: String,
    val chapter: String,
    val pqrs: Boolean = false
)

data class HeringEvaluation(
    val insideToOutside: Boolean,
    val aboveDownwards: Boolean,
    val moreVitalToLess: Boolean,
    val reverseOrderOfTime: Boolean,
    val prognosisVerdict: String, // "Ideal Hahnemannian Cure in Progress", "Superficial Amelioration", "Suppression Alert"
    val clinicalGuidance: String
)

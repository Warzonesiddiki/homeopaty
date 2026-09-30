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
) {
    /** Canonical repertory path, e.g. "MIND - AILMENTS FROM - GRIEF" (docs/data, QA API surface). */
    val path: String
        get() = "$chapter - ${name.uppercase()}"
}

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
    val conflictReason: String = "",
    /** 0-100% share of the maximum theoretically attainable weighted score for the active rubric set. */
    val confidencePercent: Int = 0
) {
    /** QA-suite API surface (docs/engineering/testing-qa-strategy.md). */
    val remedyCode: String
        get() = remedy.abbreviation
}

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

// Consultation Case Mode (docs/data/enums.md #1)
enum class CaseMode(val label: String) {
    CHRONIC("CHRONIC CONSTITUTIONAL"),
    ACUTE("ACUTE OPD"),
    FOLLOW_UP("FOLLOW-UP (HERING)")
}

// Vision Lab: diagnostic visual inspection (docs/engineering/project-structure.md)
enum class VisionPanel(val label: String) {
    TONGUE("Tongue Inspection"),
    SKIN("Skin & Perspiration"),
    GENERAL("General Physical Signs")
}

data class VisualFinding(
    val id: String,
    val panel: VisionPanel,
    val label: String,
    val remedyCodes: List<String>,
    val rubricId: String = "", // canonical rubric activated when the finding is recorded
    val chapter: String = "MOUTH"
)

// LM 50-Millesimal Posology Protocol (Organon §270-§272, Hahnemann's Q Method)
data class LmProtocol(
    val potency: String, // e.g. "LM 1"
    val grainsOfMedicine: Int, // 4 grains of the previous level's medicinal solution
    val waterDrops: Int, // 10 drops distilled water
    val dilutionRatio: String, // 1 : 50,000 (1 grain to 50,000 grains of water)
    val succussions: Int, // 10 standard; 4 for hypersensitive patients
    val dilutionMethod: String, // "2nd Cup Method" description
    val doseInstructions: String,
    val splitDosing: String
)

// Kent's 12 Prognostic Observations (docs/data/enums.md #13, J. H. Kent "Prognosis of Homeopathic Treatment")
enum class KentObservationAction(val label: String) {
    WAIT_AND_WATCH_SAC_LAC("Sac Lac: Do not repeat or interfere with the vital reaction"),
    REPEAT_SAME_POTENCY("Repeat Same Potency: Reaction has ceased prematurely"),
    INCREASE_POTENCY("Increase Potency: Reaction favorable but patient requires deeper dynamization"),
    ANTIDOTE_IMMEDIATELY("Antidote Immediately: Violent prolonged aggravation with decline"),
    CHANGE_REMEDY("Change Remedy: Direction unfavorable, wrong similimum selected")
}

enum class KentReactionPattern(val label: String) {
    AGGRAVATION("Initial Aggravation"),
    AMELIORATION("Initial Amelioration"),
    NO_REACTION("No Reaction")
}

enum class KentSeverity(val label: String) {
    MILD("Mild"),
    MODERATE("Moderate"),
    VIOLENT("Violent / Prostrating")
}

enum class KentDuration(val label: String) {
    SHORT("Short (< 6 hours)"),
    PROLONGED("Prolonged (> 6 hours)"),
    UNKNOWN("Not Noted")
}

enum class KentOutcome(val label: String) {
    RAPID_IMPROVEMENT("Rapid Improvement After Reaction"),
    SLOW_IMPROVEMENT("Slow Steady Improvement"),
    RELAPSE("Too-Short Relief: Symptoms Returned"),
    NO_IMPROVEMENT("No Improvement After Reaction"),
    DEEPENING("Centripetal: Symptoms Moved Inward")
}

data class KentReactionInput(
    val pattern: KentReactionPattern = KentReactionPattern.AGGRAVATION,
    val severity: KentSeverity = KentSeverity.MILD,
    val duration: KentDuration = KentDuration.UNKNOWN,
    val outcome: KentOutcome = KentOutcome.SLOW_IMPROVEMENT,
    val oldSuppressedSymptomsReappeared: Boolean = false
)

data class KentObservation(
    val number: Int,
    val title: String,
    val description: String,
    val action: KentObservationAction,
    val clinicalGuidance: String
)

data class KentObservationResult(
    val observation: KentObservation,
    val action: KentObservationAction,
    val summary: String
)

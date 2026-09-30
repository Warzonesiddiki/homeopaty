package com.example.model

data class Rubric(
    val id: String,
    val path: String,
    val chapter: String,
    val weight: Int = 2, // 1 to 3
    val isEliminating: Boolean = false,
    val grades: Map<String, Int> = emptyMap(), // remedyCode -> grade (1..3)
    val bilingualKeywords: List<String> = emptyList(),
    val tissueTropism: String? = null // For Boger BBCR (e.g. "Serous Membranes", "Glands", "Fibrous")
)

data class RepertorizationEntry(
    val remedyCode: String,
    val remedyName: String,
    val totalWeightedScore: Int,
    val rubricsCovered: Int,
    val totalRubricsCount: Int,
    val confidencePercent: Int,
    val gradeInRubric: Map<String, Int> = emptyMap(),
    val kingdom: SankaranKingdom = SankaranKingdom.PLANT
)

data class RemedyMateriaMedica(
    val code: String,
    val name: String,
    val commonName: String,
    val essence: String,
    val causation: String,
    val mindGenerals: String,
    val thermal: String, // Hot / Chilly / Ambithermal
    val thirstAndFood: String,
    val keyAggravations: String,
    val keyAmeliorations: String,
    val characteristicKeynotes: List<String>,
    val complementary: String,
    val followsWell: String,
    val inimical: String,
    val antidotes: String,
    val primaryMiasm: Miasm,
    val kingdom: SankaranKingdom = SankaranKingdom.PLANT,
    val periodicTableRow: Int? = null,
    val periodicTableCol: Int? = null,
    val dominantTissue: String = "General"
)

data class KeynoteVerification(
    val id: String,
    val remedyCode: String,
    val keynoteText: String,
    val isVerified: Boolean = false
)

data class HeringsLawAssessment(
    val energyMoodSleepBetter: Boolean = true,
    val directionAboveDownward: Boolean = true,
    val directionWithinOutward: Boolean = true,
    val reverseOrderOfAppearance: Boolean = true,
    val oldSkinEruptionReturned: Boolean = false,
    val warningInwardSuppression: Boolean = false,
    val recommendation: String = "Wait and Watch. Do not interfere with placebo (Sac Lac). Vital force is curing from within outward."
)

data class KentObservation(
    val number: Int,
    val title: String,
    val observationText: String,
    val prognosis: String,
    val actionRequired: String
)

data class InimicalWarning(
    val remedyA: String,
    val remedyB: String,
    val severity: String = "CRITICAL",
    val warningText: String,
    val safeAlternative: String
)

data class LmPotencyCalculation(
    val potency: String,
    val bottleVolumeMl: Int = 100,
    val alcoholDrops: Int = 15,
    val succussions: Int = 8,
    val dilutionMethod: String = "Standard 1st Cup (1 tsp in 100ml water)",
    val patientDirections: String
)

data class Prescription(
    val id: String = "RX-${System.currentTimeMillis()}",
    val patientId: String,
    val patientName: String,
    val selectedRemedyName: String,
    val selectedRemedyCode: String,
    val potency: String = "200C",
    val posology: String = "Single Constitutional Dose (Stat 4 pills)",
    val vehicle: String = "Sugar of Milk (Sac Lac Globules #30)",
    val auxiliaryRegimen: List<String> = listOf("Avoid raw camphor and strong menthol", "No strong raw coffee during remedy action", "Maintain 30 min gap before/after dose"),
    val clinicalNotes: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

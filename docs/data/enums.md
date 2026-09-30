# Similimum AI — Comprehensive Clinical Enums Specification
`Location: /docs/data/enums.md`

---

## 1. Classical Homeopathic Enums

```kotlin
// 1. Consultation Case Mode
enum class CaseMode(val label: String) {
    CHRONIC("CHRONIC CONSTITUTIONAL"),
    ACUTE("ACUTE OPD"),
    FOLLOW_UP("FOLLOW-UP (HERING)")
}

// 2. Case Consultation Lifecycle Status
enum class CaseStatus {
    ACTIVE,
    COMPLETED,
    ARCHIVED
}

// 3. Speaker Diarization
enum class Speaker {
    PATIENT,
    DOCTOR
}

// 4. Symptom Totality Category
enum class SymptomCategory(val displayName: String) {
    MENTAL_GENERAL("Mental General"),
    PHYSICAL_GENERAL("Physical General"),
    MODALITY_AGG("Modality (< Aggravation)"),
    MODALITY_AMEL("Modality (> Amelioration)"),
    PARTICULAR("Particular Organ"),
    PQRS("PQRS (§153 Peculiar/Rare)")
}

// 5. Patient Thermal Temperament
enum class ThermalState(val label: String) {
    CHILLY("Chilly (Sensitive to Cold)"),
    HOT("Hot / Warm-Blooded (Sensitive to Heat)"),
    AMBITHERMAL("Ambithermal (Sensitive to both Extremes)")
}

// 6. Thirst Diathesis
enum class ThirstPattern(val label: String) {
    THIRSTLESS("Thirstless (Puls, Apis, Gels)"),
    LARGE_QUANTITY_LONG_INTERVAL("Large quantities, long intervals (Bryonia)"),
    SMALL_QUANTITY_FREQUENT("Small quantities frequently (Ars-alb, Phos)"),
    NORMAL("Normal Physiological Thirst")
}

// 7. Side Affinity
enum class SideAffinity(val label: String) {
    RIGHT("Right-Sided (Lyc, Bell, Bry)"),
    LEFT("Left-Sided (Lach, Phos, Sep)"),
    BILATERAL("Bilateral / Symmetrical"),
    DIAGONAL_RIGHT_TO_LEFT("Right to Left (Lyc)"),
    DIAGONAL_LEFT_TO_RIGHT("Left to Right (Lach)")
}

// 8. Chronic Miasm Classification
enum class Miasm(val label: String) {
    PSORA("Psora (Hypersensitivity & Deficiency)"),
    SYCOSIS("Sycosis (Excess, Proliferation & Coordination)"),
    SYPHILIS("Syphilis (Destruction & Degeneration)"),
    TUBERCULAR("Tubercular (Restlessness & Rapid Breakdown)")
}

// 9. Classical Repertory Schools
enum class RepertorySchool(val label: String, val mentalWeight: Float, val physicalWeight: Float, val particularWeight: Float) {
    KENT_HIERARCHY("Kent's Hierarchical Method", 3.0f, 2.0f, 1.0f),
    BOENNINGHAUSEN_TPB("Boenninghausen's TPB (Complete Modalities)", 2.0f, 2.5f, 1.5f),
    BOGER_SYNOPTIC("Boger's Generalities & Pathological Affinities", 2.0f, 2.0f, 2.0f)
}

// 10. Potency Scales & Dilution
enum class PotencyScale(val label: String) {
    CENTESIMAL("Centesimal Scale (C)"),
    MILLISIMAL_LM("50-Millesimal Scale (LM / Q)"),
    DECIMAL("Decimal Scale (X / D)"),
    MOTHER_TINCTURE("Mother Tincture (Q / Ø)")
}

// 11. Posology Repetition Method
enum class Posology(val label: String) {
    SINGLE_DOSE_STAT("Single Dose Statim (4 globules under tongue)"),
    SPLIT_DOSE_WATER("Split Dose in Distilled Water (Sip method)"),
    DAILY_LM_SUCCUSSED("Daily LM Dilution with 8-10 Succussions"),
    TDS_ACUTE("Three Times Daily for Acute Emergency"),
    SAC_LAC_PLACEBO("Sac Lac / Placebo (Wait and Watch)")
}

// 12. Hering's Law Actionable Outcome
enum class HeringsPrognosis(val label: String) {
    IDEAL_CURE_IN_PROGRESS("Ideal Direction of Cure: Inside-out & Above-downwards"),
    SUPPRESSION_WARNING("Warning: Superficial symptoms cleared, vital organ aggravated"),
    CENTRIPETAL_RETROGRESSION("Incurable or Deepening Pathology: Centripetal movement"),
    UNCERTAIN_MORE_DATA_NEEDED("Observation in progress: Re-evaluate in 7 days")
}

// 13. Kent's 12 Prognostic Clinical Actions
enum class KentObservationAction(val label: String) {
    WAIT_AND_WATCH_SAC_LAC("Sac Lac: Do not repeat or interfere with the vital reaction"),
    REPEAT_SAME_POTENCY("Repeat Same Potency: Reaction has ceased prematurely"),
    INCREASE_POTENCY("Increase Potency: Reaction favorable but patient requires deeper dynamization"),
    ANTIDOTE_IMMEDIATELY("Antidote Immediately: Violent prolonged aggravation with decline"),
    CHANGE_REMEDY("Change Remedy: Direction unfavorable, wrong similimum selected")
}
```

`[ASSUMPTION-ENUM-01]` Enum names and string representations are immutable across SQLite database migrations to ensure long-term schema backwards compatibility.

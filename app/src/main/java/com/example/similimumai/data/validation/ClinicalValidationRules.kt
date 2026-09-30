package com.example.similimumai.data.validation

import com.example.similimumai.data.engine.HomeopathyKnowledgeEngine

/**
 * Deterministic clinical data-integrity rules
 * (docs/data/validation-rules.md §2 — Entity-by-Entity Validation Rules).
 *
 * Pure Kotlin, no Android dependencies: the full surface is unit-testable
 * on the JVM, and the same rules gate both the ViewModel save paths
 * (Compose state checks) and everything persisted to Room.
 */
object ClinicalValidationRules {

    const val MAX_RAW_UTTERANCE_CHARS = 1_000
    const val MIN_CHIEF_COMPLAINT_CHARS = 3
    const val MAX_ELIMINATING_RUBRICS = 3

    private val REGISTRATION_FORMAT = Regex("^[A-Z0-9\\-/]{4,25}$")
    private val RECOGNIZED_DEGREES = listOf("BHMS", "MD", "LCEH", "DHMS")
    private val VALID_SEXES = listOf("MALE", "FEMALE", "OTHER")

    // ── §2.1 Doctor Registration ────────────────────────────────────────────

    /** @return human-readable errors; empty list = valid profile. */
    fun validateDoctorProfile(
        fullName: String,
        registrationNumber: String,
        qualification: String
    ): List<String> {
        val errors = mutableListOf<String>()
        val name = fullName.trim()
        if (name.length < 3 || name.length > 100) {
            errors += "Full name must be between 3 and 100 characters"
        }
        if (!registrationNumber.trim().uppercase().matches(REGISTRATION_FORMAT)) {
            errors += "Registration number must be 4-25 letters/digits with optional - or / (e.g. HO-1234)"
        }
        val qual = qualification.trim().uppercase()
        if (RECOGNIZED_DEGREES.none { qual.contains(it) }) {
            errors += "Qualification must include a recognized degree (${RECOGNIZED_DEGREES.joinToString(", ")})"
        }
        return errors
    }

    // ── §2.2 Patient Profile ────────────────────────────────────────────────

    fun validatePatient(name: String, age: Int, sex: String): List<String> {
        val errors = mutableListOf<String>()
        if (name.isBlank()) errors += "Patient name is required"
        if (age !in 0..125) errors += "Age must be between 0 and 125 years"
        if (sex.trim().uppercase() !in VALID_SEXES) {
            errors += "Biological sex must be one of MALE, FEMALE or OTHER"
        }
        return errors
    }

    // ── §2.3 Consultation Case ──────────────────────────────────────────────

    fun validateChiefComplaint(complaint: String): List<String> {
        return if (complaint.trim().length >= MIN_CHIEF_COMPLAINT_CHARS) {
            emptyList()
        } else {
            listOf("Chief complaint must be at least $MIN_CHIEF_COMPLAINT_CHARS characters")
        }
    }

    // ── §2.4 Symptom Record & Boenninghausen LSMC ───────────────────────────

    fun validateRawUtterance(utterance: String): List<String> {
        val errors = mutableListOf<String>()
        if (utterance.isBlank()) errors += "Utterance must not be blank"
        if (utterance.length > MAX_RAW_UTTERANCE_CHARS) {
            errors += "Utterance exceeds the $MAX_RAW_UTTERANCE_CHARS character limit"
        }
        return errors
    }

    /**
     * LSMC completeness, §2.4:
     * `(1_loc + 1_sens + 1_mod + 1_conc) / 4` — a float between 0.0 and 1.0.
     */
    fun computeLsmcCompleteness(
        location: String,
        sensation: String,
        modalities: String,
        concomitant: String
    ): Float =
        listOf(location, sensation, modalities, concomitant).count { it.isNotBlank() } / 4.0f

    // ── §2.5 Rubric Weights & Eliminator Filters ────────────────────────────

    fun validateWeight(weight: Int): List<String> {
        return if (weight in 1..3) {
            emptyList()
        } else {
            listOf("Rubric weight must be strictly between 1 and 3")
        }
    }

    /** Warning (not a block) when too many eliminators are stacked — §2.5. */
    fun eliminatingRubricWarning(eliminatingCount: Int): String? {
        return if (eliminatingCount > MAX_ELIMINATING_RUBRICS) {
            "WARNING: $eliminatingCount rubrics are marked as eliminating (max $MAX_ELIMINATING_RUBRICS) — " +
                "the candidate set may be emptied"
        } else {
            null
        }
    }

    // ── §2.6 Posology & Prescription Validation ─────────────────────────────

    /**
     * Inimical Check Rule, §2.6: returns the engine's conflict message when
     * [newRemedy] is strictly inimical to the previously prescribed remedy,
     * or null when the transition is safe (or unknown remedies).
     */
    fun inimicalConflict(previousRemedy: String?, newRemedy: String): String? {
        if (previousRemedy.isNullOrBlank() || newRemedy.isBlank()) return null
        return HomeopathyKnowledgeEngine.checkInimicalCompatibility(previousRemedy, newRemedy)
    }

    /** A strictly-inimical transition is blocked until a typed justification exists. */
    fun inimicalJustificationRequired(conflict: String?, justification: String): Boolean =
        conflict != null && justification.isBlank()
}

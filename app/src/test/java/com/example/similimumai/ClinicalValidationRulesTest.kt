package com.example.similimumai

import com.example.similimumai.data.validation.ClinicalValidationRules
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Tier 1: Pure JVM tests for docs/data/validation-rules.md §2.
 */
class ClinicalValidationRulesTest {

    // ── §2.1 Doctor Registration ────────────────────────────────────────────

    @Test
    fun `doctor profile - valid profile passes`() {
        val errors = ClinicalValidationRules.validateDoctorProfile(
            fullName = "Dr. Siddiki",
            registrationNumber = "HO-12345",
            qualification = "BHMS"
        )
        assertTrue("expected no errors, got $errors", errors.isEmpty())
    }

    @Test
    fun `doctor profile - name outside 3 to 100 characters fails`() {
        val tooShort = ClinicalValidationRules.validateDoctorProfile("Dr", "HO-1234", "BHMS")
        assertTrue(tooShort.any { it.contains("Full name") })

        val tooLong = ClinicalValidationRules.validateDoctorProfile("x".repeat(101), "HO-1", "BHMS")
        assertTrue(tooLong.any { it.contains("Full name") })

        val boundary = ClinicalValidationRules.validateDoctorProfile("x".repeat(3), "HO-1", "BHMS")
        assertTrue("3 chars is the allowed lower bound", boundary.isEmpty())
    }

    @Test
    fun `doctor profile - registration format enforced`() {
        assertTrue(
            ClinicalValidationRules.validateDoctorProfile("Dr. A", "HO-1234", "BHMS").isEmpty()
        )
        assertTrue(
            "too short",
            ClinicalValidationRules.validateDoctorProfile("Dr. A", "H1", "BHMS").isNotEmpty()
        )
        assertTrue(
            "too long",
            ClinicalValidationRules.validateDoctorProfile("Dr. A", "H".repeat(26), "BHMS").isNotEmpty()
        )
        assertTrue(
            "illegal characters",
            ClinicalValidationRules.validateDoctorProfile("Dr. A", "HO 1234", "BHMS").isNotEmpty()
        )
        // lowercase input is normalized before the match
        assertTrue(
            ClinicalValidationRules.validateDoctorProfile("Dr. A", "ho-1234", "BHMS").isEmpty()
        )
    }

    @Test
    fun `doctor profile - unrecognized degree fails`() {
        val errors = ClinicalValidationRules.validateDoctorProfile("Dr. A", "HO-1234", "BSc (Physics)")
        assertTrue(errors.any { it.contains("degree") })
        assertTrue(ClinicalRulesHaveAllDegrees())
    }

    private fun ClinicalRulesHaveAllDegrees(): Boolean {
        listOf("BHMS", "MBBS MD", "LCEH", "DHMS").forEach {
            assertTrue(
                "degree $it must be accepted",
                ClinicalValidationRules.validateDoctorProfile("Dr. A", "HO-1234", it).isEmpty()
            )
        }
        return true
    }

    // ── §2.2 Patient Profile ────────────────────────────────────────────────

    @Test
    fun `patient - valid profile passes`() {
        assertTrue(ClinicalValidationRules.validatePatient("John", 40, "MALE").isEmpty())
        assertTrue(ClinicalValidationRules.validatePatient("Jane", 0, "female").isEmpty())
    }

    @Test
    fun `patient - age bounds 0 to 125`() {
        assertTrue(ClinicalValidationRules.validatePatient("A", -1, "MALE").isNotEmpty())
        assertTrue(ClinicalValidationRules.validatePatient("A", 126, "MALE").isNotEmpty())
        assertTrue(ClinicalValidationRules.validatePatient("A", 125, "MALE").isEmpty())
    }

    @Test
    fun `patient - sex must be MALE FEMALE or OTHER`() {
        assertTrue(ClinicalValidationRules.validatePatient("A", 30, "OTHER").isEmpty())
        assertTrue(ClinicalValidationRules.validatePatient("A", 30, "unknown").isNotEmpty())
        assertTrue(ClinicalValidationRules.validatePatient("A", 30, "").isNotEmpty())
    }

    @Test
    fun `patient - blank name fails`() {
        assertTrue(ClinicalValidationRules.validatePatient("  ", 30, "MALE").isNotEmpty())
    }

    // ── §2.3 Consultation Case ──────────────────────────────────────────────

    @Test
    fun `chief complaint - minimum 3 characters`() {
        assertTrue(ClinicalValidationRules.validateChiefComplaint("head").isEmpty())
        assertTrue(ClinicalValidationRules.validateChiefComplaint("ai").isNotEmpty())
        assertTrue(ClinicalValidationRules.validateChiefComplaint("   ").isNotEmpty())
    }

    // ── §2.4 Symptom Record & LSMC ──────────────────────────────────────────

    @Test
    fun `raw utterance - capped at 1000 characters`() {
        assertTrue(ClinicalValidationRules.validateRawUtterance("x".repeat(1000)).isEmpty())
        assertTrue(ClinicalValidationRules.validateRawUtterance("x".repeat(1001)).isNotEmpty())
        assertTrue(ClinicalValidationRules.validateRawUtterance("").isNotEmpty())
    }

    @Test
    fun `lsmc completeness - fraction of four pillars present`() {
        assertEquals(
            1.0f,
            ClinicalValidationRules.computeLsmcCompleteness("head", "throbbing", "< motion", "nausea"),
            0.0001f
        )
        assertEquals(
            0.5f,
            ClinicalValidationRules.computeLsmcCompleteness("head", "throbbing", "", "nausea"),
            0.0001f
        )
        assertEquals(
            0.0f,
            ClinicalValidationRules.computeLsmcCompleteness("  ", "", "", ""),
            0.0001f
        )
    }

    // ── §2.5 Weights & Eliminators ──────────────────────────────────────────

    @Test
    fun `rubric weight - strictly 1 to 3`() {
        listOf(1, 2, 3).forEach { assertTrue(ClinicalValidationRules.validateWeight(it).isEmpty()) }
        listOf(0, 4).forEach { assertTrue(ClinicalValidationRules.validateWeight(it).isNotEmpty()) }
    }

    @Test
    fun `eliminator guard - warning above 3 rubrics`() {
        assertNull(ClinicalValidationRules.eliminatingRubricWarning(3))
        assertNotNull(ClinicalValidationRules.eliminatingRubricWarning(4))
        assertNull(ClinicalValidationRules.eliminatingRubricWarning(0))
    }

    // ── §2.6 Inimical Prescription Gate ─────────────────────────────────────

    @Test
    fun `inimical gate - apis and rhus tox are strictly inimical`() {
        val conflict = ClinicalValidationRules.inimicalConflict("Apis", "Rhus-t")
        assertNotNull(conflict)
        assertTrue(conflict!!.contains("inimical", ignoreCase = true))

        // reverse direction must also flag (symmetric rule)
        assertNotNull(ClinicalValidationRules.inimicalConflict("Rhus-t", "Apis"))

        // safe transitions and unknown remedies pass
        assertNull(ClinicalValidationRules.inimicalConflict("Apis", "Puls"))
        assertNull(ClinicalValidationRules.inimicalConflict(null, "Apis"))
        assertNull(ClinicalValidationRules.inimicalConflict("Apis", "   "))
        assertNull(ClinicalValidationRules.inimicalConflict("Unknown", "Apis"))
    }

    @Test
    fun `inimical gate - typed justification overrides the block`() {
        val conflict = ClinicalValidationRules.inimicalConflict("Apis", "Rhus-t")!!

        assertTrue(
            "blank justification must keep the block active",
            ClinicalValidationRules.inimicalJustificationRequired(conflict, "")
        )
        assertTrue(
            ClinicalValidationRules.inimicalJustificationRequired(conflict, "   ")
        )
        assertFalse(
            "typed justification clears the block",
            ClinicalValidationRules.inimicalJustificationRequired(conflict, "chronic case, sequential trial justified")
        )
        assertFalse(
            "no conflict never requires justification",
            ClinicalValidationRules.inimicalJustificationRequired(null, "")
        )
    }
}

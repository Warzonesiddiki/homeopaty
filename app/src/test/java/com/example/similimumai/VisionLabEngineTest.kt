package com.example.similimumai

import com.example.similimumai.data.engine.HomeopathyKnowledgeEngine
import com.example.similimumai.data.engine.RubricDatabase
import com.example.similimumai.data.model.VisionPanel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Tier 1: Pure JVM tests for the Vision Lab knowledge layer
 * (docs/engineering/testing-qa-strategy.md).
 */
class VisionLabEngineTest {

    @Test
    fun `rubric database contains the full 195 rubric corpus`() {
        assertEquals(195, RubricDatabase.rubrics.size)
    }

    @Test
    fun `visual findings cover all three inspection panels`() {
        val findings = HomeopathyKnowledgeEngine.visualFindings
        assertTrue("expected a rich finding catalogue", findings.size >= 20)

        val panels = findings.map { it.panel }.toSet()
        assertTrue(panels.contains(VisionPanel.TONGUE))
        assertTrue(panels.contains(VisionPanel.SKIN))
        assertTrue(panels.contains(VisionPanel.GENERAL))

        // finding ids are unique (they double as Compose testTag keys)
        assertEquals(findings.size, findings.map { it.id }.toSet().size)
    }

    @Test
    fun `every visual finding maps to a live rubric and chapter`() {
        val rubricsById = RubricDatabase.rubrics.associateBy { it.id }
        for (finding in HomeopathyKnowledgeEngine.visualFindings) {
            assertTrue("finding ${finding.id} must declare a rubricId", finding.rubricId.isNotBlank())
            val rubric = rubricsById[finding.rubricId]
            assertNotNull("rubric ${finding.rubricId} missing from RubricDatabase", rubric)
            assertEquals(
                "rubric chapter mismatch for ${finding.id}",
                rubric.chapter,
                finding.chapter
            )
        }
    }

    @Test
    fun `remedies for finding resolves classic polychrests`() {
        for (finding in HomeopathyKnowledgeEngine.visualFindings) {
            val remedies = HomeopathyKnowledgeEngine.remediesForFinding(finding)
            assertTrue(
                "finding ${finding.id} (${finding.remedyCodes}) must resolve to >=1 polychrest",
                remedies.isNotEmpty()
            )
            // every resolved remedy must have been declared for the finding
            for (r in remedies) {
                assertTrue(finding.remedyCodes.contains(r.abbreviation))
            }
        }

        // known association: red triangular tongue tip -> Rhus-t leads the list
        val redTip = HomeopathyKnowledgeEngine.visualFindings.first { it.id == "v_tongue_red_tip" }
        assertTrue(HomeopathyKnowledgeEngine.remediesForFinding(redTip).map { it.abbreviation }.contains("Rhus-t"))
    }

    @Test
    fun `the eleven vision lab rubrics exist with expected grades`() {
        val rubricsById = RubricDatabase.rubrics.associateBy { it.id }
        val expected = listOf(
            "r_mouth_tongue_red_tip", "r_mouth_tongue_imprints", "r_mouth_tongue_pale_swollen",
            "r_mouth_tongue_geographic", "r_mouth_tongue_yellow_coating", "r_mouth_tongue_white_coating",
            "r_mouth_tongue_dry_red", "r_mouth_tongue_cracked", "r_mouth_tongue_trembling",
            "r_skin_flexural_eczema", "r_skin_burning_beds"
        )
        for (id in expected) {
            val rubric = rubricsById[id]
            assertNotNull("missing vision lab rubric $id", rubric)
            assertTrue("rubric $id must carry remedy grades", rubric!!.remedyGrades.isNotEmpty())
            assertTrue(
                "rubric $id grades must be 1..3",
                rubric.remedyGrades.values.all { it in 1..3 }
            )
        }
    }
}

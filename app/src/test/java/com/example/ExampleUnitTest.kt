package com.example

import com.example.engine.HomeopathyKnowledgeEngine
import com.example.model.RepertorySchool
import org.junit.Assert.*
import org.junit.Test

class ExampleUnitTest {

    @Test
    fun testRedFlagCardiacDetection() {
        val alert = HomeopathyKnowledgeEngine.checkRedFlag("Doctor, I have crushing chest pain shooting down my left arm!")
        assertNotNull(alert)
        assertTrue(alert!!.condition.contains("Acute Coronary Syndrome"))
    }

    @Test
    fun testRedFlagNormalText() {
        val alert = HomeopathyKnowledgeEngine.checkRedFlag("I have a throbbing headache since 2 days.")
        assertNull(alert)
    }

    @Test
    fun testRepertorizationRankingNatrumMur() {
        val griefRubric = HomeopathyKnowledgeEngine.allRubrics.first { it.path.contains("grief") }
        val consolationAggRubric = HomeopathyKnowledgeEngine.allRubrics.first { it.path.contains("CONSOLATION - agg") }
        val sunAggRubric = HomeopathyKnowledgeEngine.allRubrics.first { it.path.contains("SUN") }
        val saltDesireRubric = HomeopathyKnowledgeEngine.allRubrics.first { it.path.contains("salt") }

        val results = HomeopathyKnowledgeEngine.repertorize(listOf(griefRubric, consolationAggRubric, sunAggRubric, saltDesireRubric))
        assertTrue(results.isNotEmpty())
        assertEquals("Nat-m", results.first().remedyCode)
        assertTrue(results.first().confidencePercent > 80)
    }

    @Test
    fun testInimicalSafetyBlocker() {
        // Apis and Rhus Tox are strictly inimical
        val warning = HomeopathyKnowledgeEngine.checkInimicalCompatibility("apis", "rhus-t")
        assertNotNull(warning)
        assertTrue(warning!!.warningText.contains("DANGEROUS INIMICAL COMBINATION"))
    }

    @Test
    fun testLmPotencyCalculator() {
        val protocol = HomeopathyKnowledgeEngine.calculateLmProtocol("LM1", isHypersensitive = true)
        assertEquals(4, protocol.succussions)
        assertTrue(protocol.dilutionMethod.contains("2nd Cup Method"))
    }

    @Test
    fun testMultiSchoolRepertorization() {
        val rubric = HomeopathyKnowledgeEngine.allRubrics.first { it.path.contains("grief") }
        val kentResults = HomeopathyKnowledgeEngine.repertorizeWithSchool(listOf(rubric), RepertorySchool.KENT_HIERARCHY)
        val boenninghausenResults = HomeopathyKnowledgeEngine.repertorizeWithSchool(listOf(rubric), RepertorySchool.BOENNINGHAUSEN_TPB)
        assertTrue(kentResults.isNotEmpty())
        assertTrue(boenninghausenResults.isNotEmpty())
    }

    @Test
    fun testTongueSignsAvailable() {
        assertEquals(10, HomeopathyKnowledgeEngine.tongueSigns.size)
        val mappedTongue = HomeopathyKnowledgeEngine.tongueSigns.first { it.name.contains("Mapped") }
        assertTrue(mappedTongue.keyRemedies.contains("Nat-m"))
    }
}

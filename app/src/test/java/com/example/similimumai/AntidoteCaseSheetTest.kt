package com.example.similimumai

import com.example.similimumai.data.engine.HomeopathyKnowledgeEngine
import com.example.similimumai.data.model.CaseMode
import com.example.similimumai.data.model.Miasm
import com.example.similimumai.data.model.ThermalState
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * MVP "antidotes & dietary prohibitions" (docs/product/mvp-scope.md):
 * classical antidote remedies are looked up from the polychrest KB and
 * printed on the generated case sheet.
 */
class AntidoteCaseSheetTest {

    private fun minimalSheet(rxRemedy: String, antidotes: List<String>): String =
        HomeopathyKnowledgeEngine.generateCaseSheet(
            patientName = "Test Patient",
            patientAge = 40,
            patientSex = "Male",
            thermal = ThermalState.CHILLY,
            miasm = Miasm.PSORA,
            chiefComplaint = "test",
            caseMode = CaseMode.ACUTE.label,
            symptoms = emptyList(),
            activeRubrics = emptyList(),
            remedyScores = emptyList(),
            rxRemedy = rxRemedy,
            rxPotency = "30C",
            rxScale = "Centesimal",
            rxPosology = "single dose",
            dietaryRestrictions = listOf("Garlic"),
            antidotes = antidotes
        )

    @Test
    fun `antidotesFor resolves case-insensitively from the polychrest KB`() {
        assertEquals(
            listOf("Camph", "Phos", "Spir-nit-d"),
            HomeopathyKnowledgeEngine.antidotesFor("nat-m")
        )
        assertEquals(
            listOf("Camph", "Phos", "Spir-nit-d"),
            HomeopathyKnowledgeEngine.antidotesFor("NAT-M")
        )
        assertTrue("unknown remedies have no documented antidotes",
            HomeopathyKnowledgeEngine.antidotesFor("Unk").isEmpty())
    }

    @Test
    fun `case sheet lists documented antidotes for the prescribed remedy`() {
        val sheet = minimalSheet(
            "Nat-m",
            HomeopathyKnowledgeEngine.antidotesFor("Nat-m")
        )
        assertTrue(sheet.contains("ANTIDOTES: Camph, Phos, Spir-nit-d"))
        assertTrue("dietary prohibitions stay on the sheet",
            sheet.contains("DIETARY PROHIBITIONS: Garlic"))
    }

    @Test
    fun `case sheet notes when no antidote is documented`() {
        val sheet = minimalSheet("Unk", emptyList())
        assertTrue(sheet.contains("ANTIDOTES: None documented for this remedy"))
    }
}

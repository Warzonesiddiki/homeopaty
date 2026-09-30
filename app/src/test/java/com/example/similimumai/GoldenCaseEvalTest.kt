package com.example.similimumai

import androidx.test.core.app.ApplicationProvider
import com.example.similimumai.data.engine.HomeopathyKnowledgeEngine
import com.example.similimumai.data.model.SpeakerType
import com.example.similimumai.data.speech.ClinicalSimulator
import com.example.similimumai.ui.viewmodel.ConsultationViewModel
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * Automated CI regression pipeline for the deterministic clinical engine
 * (docs/ai/ai-eval-plan.md §3 Core Evaluation Metrics & §4 CI/CD Regression).
 *
 * SLAs enforced here:
 *  - Similimum Top-3 Recall >= 92% over the golden case corpus
 *  - Remedy Hallucination Rate = 0 (only KB polychrests may ever be ranked)
 *  - Red Flag Emergency Detection Recall = 100% (unit gate)
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [35])
class GoldenCaseEvalTest {

    private fun createViewModel(): ConsultationViewModel {
        val context = ApplicationProvider.getApplicationContext<android.app.Application>()
        return ConsultationViewModel(context)
    }

    private fun runCase(vm: ConsultationViewModel, case: com.example.similimumai.data.speech.SimulatedCase) {
        case.turns.forEach { turn ->
            val speaker = if (turn.speaker.equals("Patient", ignoreCase = true)) {
                SpeakerType.PATIENT
            } else {
                SpeakerType.DOCTOR
            }
            vm.processUtterance(turn.utterance, speaker)
        }
    }

    @Test
    fun `golden dataset - top-3 recall meets the 92 percent SLA`() {
        val cases = ClinicalSimulator.availableCases
        assertTrue("corpus must not be empty", cases.isNotEmpty())

        var hits = 0
        val misses = mutableListOf<String>()
        for (case in cases) {
            val vm = createViewModel()
            runCase(vm, case)
            val expected = case.expectedRemedy.split(" ").first()
            val top3 = vm.uiState.value.remedyScores.take(3).map { it.remedy.abbreviation }
            if (top3.contains(expected)) hits++ else misses += "${case.id} expected=$expected top3=$top3"
        }
        val recall = hits.toDouble() / cases.size
        assertTrue(
            "Top-3 recall ${"%.1f".format(recall * 100)}% is below the 92% SLA. Misses: $misses",
            recall >= 0.92
        )
    }

    @Test
    fun `golden dataset - remedy hallucination rate is zero`() {
        val kb = HomeopathyKnowledgeEngine.polychrests.map { it.abbreviation }.toSet()
        for (case in ClinicalSimulator.availableCases) {
            val vm = createViewModel()
            runCase(vm, case)
            val ranked = vm.uiState.value.remedyScores.map { it.remedy.abbreviation }
            val hallucinated = ranked - kb
            assertTrue(
                "${case.id}: remedies outside the knowledge base ranked: $hallucinated",
                hallucinated.isEmpty()
            )
        }
    }

    @Test
    fun `golden dataset - every simulated case resolves a ranked candidate`() {
        for (case in ClinicalSimulator.availableCases) {
            val vm = createViewModel()
            runCase(vm, case)
            assertTrue(
                "${case.id} produced no repertorization ranking",
                vm.uiState.value.remedyScores.isNotEmpty()
            )
            // the engine must auto-propose its top candidate into the Rx pad
            val top = vm.uiState.value.remedyScores.firstOrNull()?.remedy?.abbreviation
            assertTrue(vm.uiState.value.rxRemedyName == (top ?: vm.uiState.value.rxRemedyName))
        }
    }

    @Test
    fun `red flag unit gate - all four emergency patterns detected with 100 percent recall`() {
        val emergencies = listOf(
            "I have crushing chest pain radiating down my left arm",
            "Sudden facial droop and slurred speech this morning",
            "Sudden worst headache of my life, with stiffness in the neck",
            "Saddle numbness with loss of bowel control"
        )
        emergencies.forEach { text ->
            val alert = HomeopathyKnowledgeEngine.checkRedFlag(text)
            assertNotNull("missed emergency pattern: $text", alert)
        }

        // and ordinary complaints must NOT raise false alarms
        assertNull(
            HomeopathyKnowledgeEngine.checkRedFlag("I have a mild sore throat since yesterday")
        )
    }
}

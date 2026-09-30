package com.example.similimumai

import com.example.similimumai.data.engine.HomeopathyKnowledgeEngine
import com.example.similimumai.data.model.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Tier 1: Domain & Clinical Invariants (docs/engineering/testing-qa-strategy.md).
 * Pure local-JVM tests (<500 ms) covering:
 *  - Red-Flag Emergency Rules
 *  - Kent/TPB Repertorization
 *  - Inimical Drug Blockers
 *  - LM Potency Dilution
 *  - Kent's 12 Prognostic Observations
 *  - Knowledge-base size guarantees (30+ polychrests, 150+ rubrics)
 */
class ExampleUnitTest {

    // ────────────────────────────────────────────────────────────────────────
    // Test 1: Emergency Red-Flag Detection
    // ────────────────────────────────────────────────────────────────────────

    @Test
    fun `red flag - crushing chest pain with left arm radiation raises ACS alert`() {
        val alert = HomeopathyKnowledgeEngine.checkRedFlag(
            "Doctor, I have crushing chest pain shooting down my left arm!"
        )
        assertNotNull(alert)
        assertTrue(alert!!.condition.contains("Acute Coronary Syndrome"))
        assertEquals(UrgencyLevel.EMERGENCY, alert.urgencyLevel)
    }

    @Test
    fun `red flag - facial droop and slurred speech raises stroke alert`() {
        val alert = HomeopathyKnowledgeEngine.checkRedFlag(
            "My father suddenly has a facial droop and slurred speech!"
        )
        assertNotNull(alert)
        assertTrue(alert!!.condition.contains("Stroke"))
        assertEquals(UrgencyLevel.EMERGENCY, alert.urgencyLevel)
    }

    @Test
    fun `red flag - ordinary complaint produces no alert`() {
        val alert = HomeopathyKnowledgeEngine.checkRedFlag(
            "I have a throbbing headache since 2 days."
        )
        assertNull(alert)
    }

    // ────────────────────────────────────────────────────────────────────────
    // Test 2: Classical Kentian Repertorization Ranking (Nat-m keynotes)
    // ────────────────────────────────────────────────────────────────────────

    private val natmKeynoteRubrics: List<Rubric> by lazy {
        HomeopathyKnowledgeEngine.allRubrics.filter { it.id in setOf("r_mind_grief", "r_mind_consolation_agg", "r_head_sun_agg", "r_stomach_salt_craving") }
    }

    @Test
    fun `repertorization - Nat-m ranks first with high confidence on its keynotes`() {
        val results = HomeopathyKnowledgeEngine.repertorize(natmKeynoteRubrics)
        assertTrue(results.isNotEmpty())
        assertEquals("Nat-m", results.first().remedyCode)
        assertTrue("Nat-m confidence should exceed 80%, was ${results.first().confidencePercent}%",
            results.first().confidencePercent > 80)
    }

    @Test
    fun `repertorization - scores are deterministically ordered descending`() {
        val results = HomeopathyKnowledgeEngine.repertorize(natmKeynoteRubrics)
        for (i in 0 until results.lastIndex) {
            assertTrue(results[i].totalScore >= results[i + 1].totalScore)
        }
    }

    @Test
    fun `repertorization - thermal elimination filter disqualifies opposing temperaments`() {
        val results = HomeopathyKnowledgeEngine.calculateRepertorization(
            activeRubrics = natmKeynoteRubrics,
            school = RepertorySchool.KENT,
            eliminateThermal = ThermalState.CHILLY
        )
        results.forEach { score ->
            if (score.totalScore > 0) {
                assertTrue(
                    "Remedy ${score.remedyCode} should pass CHILLY elimination",
                    score.remedy.thermalState == ThermalState.CHILLY ||
                        score.remedy.thermalState == ThermalState.AMBITHERMAL
                )
            }
        }
    }

    // ────────────────────────────────────────────────────────────────────────
    // Test 3: Inimical Drug Safety Interception
    // ────────────────────────────────────────────────────────────────────────

    @Test
    fun `inimical - Apis and Rhus Tox are blocked`() {
        val warning = HomeopathyKnowledgeEngine.checkInimicalCompatibility("apis", "rhus-t")
        assertNotNull(warning)
        assertTrue(warning!!.contains("DANGEROUS INIMICAL COMBINATION"))
    }

    @Test
    fun `inimical - Causticum and Phosphorus are blocked`() {
        val warning = HomeopathyKnowledgeEngine.checkInimicalCompatibility("caust", "phos")
        assertNotNull(warning)
        assertTrue(warning!!.contains("DANGEROUS INIMICAL COMBINATION"))
    }

    @Test
    fun `inimical - safe pair produces no warning`() {
        assertNull(HomeopathyKnowledgeEngine.checkInimicalCompatibility("nat-m", "lyc"))
    }

    // ────────────────────────────────────────────────────────────────────────
    // Test 4: LM 50-Millesimal Dilution Mathematics (Organon §270-§272)
    // ────────────────────────────────────────────────────────────────────────

    @Test
    fun `LM protocol - hypersensitive patient gets reduced succussions and 2nd cup method`() {
        val protocol = HomeopathyKnowledgeEngine.calculateLmProtocol("LM1", isHypersensitive = true)
        assertEquals(4, protocol.succussions)
        assertTrue(protocol.dilutionMethod.contains("2nd Cup Method"))
    }

    @Test
    fun `LM protocol - standard patient gets full succussions`() {
        val protocol = HomeopathyKnowledgeEngine.calculateLmProtocol("LM 12")
        assertEquals(10, protocol.succussions)
        assertEquals("LM 12", protocol.potency)
        assertEquals(4, protocol.grainsOfMedicine)
        assertEquals(10, protocol.waterDrops)
    }

    @Test
    fun `LM protocol - potency level is clamped to the LM1-LM30 scale`() {
        assertEquals("LM 30", HomeopathyKnowledgeEngine.calculateLmProtocol("LM99").potency)
        assertEquals("LM 1", HomeopathyKnowledgeEngine.calculateLmProtocol("garbage").potency)
    }

    // ────────────────────────────────────────────────────────────────────────
    // Test 5: Multi-School Repertory Divergence (Kent vs Boenninghausen TPB)
    // ────────────────────────────────────────────────────────────────────────

    @Test
    fun `multi-school - Kent and Boenninghausen TPB both produce rankings`() {
        val griefRubric = HomeopathyKnowledgeEngine.allRubrics.first { it.id == "r_mind_grief" }
        val kentResults = HomeopathyKnowledgeEngine.repertorizeWithSchool(
            listOf(griefRubric), RepertorySchool.KENT
        )
        val boenninghausenResults = HomeopathyKnowledgeEngine.repertorizeWithSchool(
            listOf(griefRubric), RepertorySchool.BOENNINGHAUSEN
        )
        assertTrue(kentResults.isNotEmpty())
        assertTrue(boenninghausenResults.isNotEmpty())
        assertEquals("Nat-m", kentResults.first().remedyCode)
    }

    // ────────────────────────────────────────────────────────────────────────
    // Test 6: Kent's 12 Prognostic Observations mapping
    // ────────────────────────────────────────────────────────────────────────

    @Test
    fun `kent - quick short aggravation with rapid recovery is Observation 3`() {
        val result = HomeopathyKnowledgeEngine.evaluateKentObservation(
            KentReactionInput(
                pattern = KentReactionPattern.AGGRAVATION,
                severity = KentSeverity.MILD,
                duration = KentDuration.SHORT,
                outcome = KentOutcome.RAPID_IMPROVEMENT
            )
        )
        assertEquals(3, result.observation.number)
        assertEquals(KentObservationAction.WAIT_AND_WATCH_SAC_LAC, result.action)
    }

    @Test
    fun `kent - no reaction maps to potency increase`() {
        val result = HomeopathyKnowledgeEngine.evaluateKentObservation(
            KentReactionInput(pattern = KentReactionPattern.NO_REACTION)
        )
        assertEquals(10, result.observation.number)
        assertEquals(KentObservationAction.INCREASE_POTENCY, result.action)
    }

    @Test
    fun `kent - violent prostrating aggravation demands antidote`() {
        val result = HomeopathyKnowledgeEngine.evaluateKentObservation(
            KentReactionInput(pattern = KentReactionPattern.AGGRAVATION, severity = KentSeverity.VIOLENT)
        )
        assertEquals(7, result.observation.number)
        assertEquals(KentObservationAction.ANTIDOTE_IMMEDIATELY, result.action)
    }

    @Test
    fun `kent - amelioration with return of old symptoms is Heringian cure`() {
        val result = HomeopathyKnowledgeEngine.evaluateKentObservation(
            KentReactionInput(
                pattern = KentReactionPattern.AMELIORATION,
                outcome = KentOutcome.SLOW_IMPROVEMENT,
                oldSuppressedSymptomsReappeared = true
            )
        )
        assertEquals(8, result.observation.number)
        assertEquals(KentObservationAction.WAIT_AND_WATCH_SAC_LAC, result.action)
    }

    // ────────────────────────────────────────────────────────────────────────
    // Test 7: Vernacular parser → canonical rubric mapping
    // ────────────────────────────────────────────────────────────────────────

    @Test
    fun `parser - grief utterance maps to the canonical Nat-m rubric`() {
        val symptom = HomeopathyKnowledgeEngine.parseUtteranceToSymptom(
            "I am in deep grief since my partner betrayed me, I want to weep alone."
        )
        assertEquals("Ailments from grief, sorrow, disappointed love", symptom.canonicalRubric)
        assertTrue(symptom.isPqrs)
    }

    @Test
    fun `parser - sun-aggravated headache maps to the PQRS sun rubric`() {
        val symptom = HomeopathyKnowledgeEngine.parseUtteranceToSymptom(
            "Doctor, dhoop mein sar fatne lagta hai - the sun aggravates my headache."
        )
        assertEquals("Headache aggravated by heat of sun", symptom.canonicalRubric)
        assertEquals("Head (Cephalic)", symptom.location)
    }

    @Test
    fun `parser - 4 to 8 pm time window maps to the Lyc bloating rubric`() {
        val symptom = HomeopathyKnowledgeEngine.parseUtteranceToSymptom(
            "My gas pain is strictly between 4:00 PM and 8:00 PM every day."
        )
        assertEquals("Abdomen distension, flatulence 4:00 PM to 8:00 PM", symptom.canonicalRubric)
        assertTrue(symptom.isPqrs)
    }

    @Test
    fun `parser - cool evening breeze maps to the open-air amel rubric`() {
        val symptom = HomeopathyKnowledgeEngine.parseUtteranceToSymptom(
            "In the open cool evening breeze he immediately calms down."
        )
        assertEquals("Generalities, open cool air ameliorates", symptom.canonicalRubric)
    }

    @Test
    fun `parser - fear of being alone maps to the fear rubric without touching grief`() {
        val fear = HomeopathyKnowledgeEngine.parseUtteranceToSymptom(
            "I get terrified when left alone in the dark."
        )
        assertEquals("Fear of being alone", fear.canonicalRubric)
        assertTrue(fear.isPqrs)
    }

    @Test
    fun `parser - ice-cold water craving maps to the ice-cold desire rubric`() {
        val symptom = HomeopathyKnowledgeEngine.parseUtteranceToSymptom(
            "I crave ice-cold water and ice cream like crazy."
        )
        assertEquals("Desire for ice-cold water, ice cream, cold drinks", symptom.canonicalRubric)
        assertTrue(symptom.isPqrs)
    }

    // ────────────────────────────────────────────────────────────────────────
    // Test 8: Case Sheet generation
    // ────────────────────────────────────────────────────────────────────────

    @Test
    fun `case sheet - contains patient, totality, repertory and prescription sections`() {
        val rubrics = natmKeynoteRubrics
        val scores = HomeopathyKnowledgeEngine.repertorize(rubrics)
        val sheet = HomeopathyKnowledgeEngine.generateCaseSheet(
            patientName = "Priya Sharma",
            patientAge = 32,
            patientSex = "Female",
            thermal = ThermalState.HOT,
            miasm = Miasm.PSORA,
            chiefComplaint = "Severe throbbing right-sided headache & silent grief",
            caseMode = CaseMode.CHRONIC.label,
            symptoms = listOf(
                Symptom(id = "s1", location = "Head (Cephalic)", sensation = "Throbbing",
                    modalities = "Aggravated by sun", canonicalRubric = "Headache aggravated by heat of sun",
                    isPqrs = true, intensity = 3, completenessScore = 75)
            ),
            activeRubrics = rubrics,
            remedyScores = scores,
            rxRemedy = "Nat-m",
            rxPotency = "200C",
            rxScale = "Centesimal",
            rxPosology = "Single dose on tongue",
            dietaryRestrictions = listOf("Raw Onion", "Garlic"),
            lmProtocol = HomeopathyKnowledgeEngine.calculateLmProtocol("LM1")
        )
        assertTrue(sheet.contains("SIMILIMUM AI"))
        assertTrue(sheet.contains("Priya Sharma"))
        assertTrue(sheet.contains("SYMPTOM TOTALITY"))
        assertTrue(sheet.contains("ACTIVE REPERTORY RUBRICS"))
        assertTrue(sheet.contains("PRESCRIPTION"))
        assertTrue(sheet.contains("Nat-m"))
        assertTrue(sheet.contains("2nd Cup Method"))
    }

    // ────────────────────────────────────────────────────────────────────────
    // Test 9: Documented knowledge-base size guarantees
    // ────────────────────────────────────────────────────────────────────────

    @Test
    fun `knowledge base - ships 30 or more classical polychrests`() {
        assertTrue(
            "Expected 30+ polychrests, found ${HomeopathyKnowledgeEngine.allPolychrests.size}",
            HomeopathyKnowledgeEngine.allPolychrests.size >= 30
        )
    }

    @Test
    fun `knowledge base - ships 150 or more canonical rubrics`() {
        assertTrue(
            "Expected 150+ rubrics, found ${HomeopathyKnowledgeEngine.allRubrics.size}",
            HomeopathyKnowledgeEngine.allRubrics.size >= 150
        )
    }

    @Test
    fun `knowledge base - rubric ids are unique`() {
        val ids = HomeopathyKnowledgeEngine.allRubrics.map { it.id }
        assertEquals(ids.size, ids.distinct().size)
    }
}

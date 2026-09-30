package com.example.similimumai

import android.os.Looper
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.similimumai.data.local.ConsultationDatabase
import com.example.similimumai.data.local.entity.FollowUpEntity
import com.example.similimumai.data.local.entity.PatientEntity
import com.example.similimumai.data.local.entity.PrescriptionEntity
import com.example.similimumai.data.local.entity.SessionEntity
import com.example.similimumai.data.local.entity.SymptomRecordEntity
import com.example.similimumai.data.speech.AudioSpeechManager
import com.example.similimumai.data.model.SpeakerType
import com.example.similimumai.ui.viewmodel.ConsultationViewModel
import com.example.similimumai.ui.viewmodel.NavigationTab
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config

/**
 * Tier 2: Components & Lifecycles (docs/engineering/testing-qa-strategy.md).
 * Robolectric local-JVM shadow engine covering:
 *  - Room DAO queries & FK cascades
 *  - ConsultationViewModel StateFlow behavior
 *  - AudioSpeechManager idle states
 *
 * Robolectric 4.16.1 max instrumented SDK is 35 (app targets 36),
 * hence the explicit @Config(sdk = [35]).
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [35])
class ExampleRobolectricTest {

    private fun inMemoryDatabase(): ConsultationDatabase {
        val context = ApplicationProvider.getApplicationContext<android.app.Application>()
        return Room.inMemoryDatabaseBuilder(context, ConsultationDatabase::class.java).build()
    }

    // ────────────────────────────────────────────────────────────────────────
    // Room DAO queries
    // ────────────────────────────────────────────────────────────────────────

    @Test
    fun `room - patient, session and prescription persist`() = runBlocking {
        val db = inMemoryDatabase()
        val patientId = db.patientDao().insertPatient(
            PatientEntity(
                mnr = "TEST-001",
                name = "Test Patient",
                age = 40,
                sex = "Male",
                thermalState = "CHILLY",
                dominantMiasm = "PSORA",
                chiefComplaint = "test chief complaint"
            )
        )
        val sessionId = db.sessionDao().insertSession(
            SessionEntity(
                patientId = patientId,
                consultationType = "CHRONIC",
                summaryNotes = "test totality",
                totalityScore = 70,
                prescribedRemedy = "Nat-m",
                potency = "200C",
                posology = "single dose",
                heringStatus = "Not Evaluated"
            )
        )
        db.prescriptionDao().insertPrescription(
            PrescriptionEntity(
                sessionId = sessionId,
                prescribedRemedy = "Nat-m",
                potencyScale = "Centesimal",
                potency = "200C",
                posology = "single dose",
                heringPrognosis = "Not Evaluated"
            )
        )

        val patients = db.patientDao().getAllPatients().first()
        val sessions = db.sessionDao().getAllSessions().first()
        val prescriptions = db.prescriptionDao().getAllPrescriptions().first()

        assertEquals(1, patients.size)
        assertEquals("Test Patient", patients.first().name)
        assertEquals(1, sessions.size)
        assertEquals(1, prescriptions.size)
        assertEquals("Nat-m", prescriptions.first().prescribedRemedy)
    }

    @Test
    fun `room - deleting a patient cascades to their sessions`() = runBlocking {
        val db = inMemoryDatabase()
        val patientId = db.patientDao().insertPatient(
            PatientEntity(
                mnr = "TEST-002",
                name = "Cascade Patient",
                age = 33,
                sex = "Female",
                thermalState = "HOT",
                dominantMiasm = "PSORA",
                chiefComplaint = "test chief complaint"
            )
        )
        val sessionId = db.sessionDao().insertSession(
            SessionEntity(
                patientId = patientId,
                consultationType = "ACUTE",
                summaryNotes = "test",
                totalityScore = 50,
                prescribedRemedy = "Ars",
                potency = "30C",
                posology = "single dose",
                heringStatus = "Not Evaluated"
            )
        )
        assertEquals(1, db.sessionDao().getSessionsForPatient(patientId).first().size)

        db.patientDao().deletePatient(db.patientDao().getPatientById(patientId)!!)

        assertEquals(0, db.sessionDao().getAllSessions().first().size)
        db.close()
    }

    @Test
    fun `room - symptom records and follow-ups persist per session`() = runBlocking {
        val db = inMemoryDatabase()
        val patientId = db.patientDao().insertPatient(
            PatientEntity(
                mnr = "TEST-003", name = "Record Patient", age = 28, sex = "Female",
                thermalState = "AMBITHERMAL", dominantMiasm = "SYPHILIS",
                chiefComplaint = "test"
            )
        )
        val sessionId = db.sessionDao().insertSession(
            SessionEntity(
                patientId = patientId,
                consultationType = "FOLLOW_UP",
                summaryNotes = "test",
                totalityScore = 60,
                prescribedRemedy = "Lyc",
                potency = "30C",
                posology = "single dose",
                heringStatus = "Cure in progress"
            )
        )

        db.symptomRecordDao().insertAll(
            listOf(
                SymptomRecordEntity(
                    id = "uuid-sym-1",
                    sessionId = sessionId,
                    rawUtterance = "my stomach bloats in the evening",
                    location = "Stomach & Abdomen",
                    sensation = "Distension",
                    modalities = "< evening",
                    concomitant = "flatulence",
                    canonicalRubric = "Abdomen distension, flatulence 4:00 PM to 8:00 PM",
                    isPqrs = true,
                    completenessScore = 75
                )
            )
        )
        db.followUpDao().insert(
            FollowUpEntity(
                sessionId = sessionId,
                insideOutward = true,
                aboveDownward = true,
                vitalToLessVital = true,
                reverseOrderAppearance = false,
                kentObservationIndex = 3,
                clinicalAssessment = "Quick short aggravation, rapid recovery",
                recommendedAction = "WAIT_AND_WATCH_SAC_LAC"
            )
        )

        val records = db.symptomRecordDao().getBySession(sessionId).first()
        assertEquals(1, records.size)
        assertEquals("uuid-sym-1", records.first().id)
        assertEquals("< evening", records.first().modalities)

        val followUps = db.followUpDao().getBySession(sessionId).first()
        assertEquals(1, followUps.size)
        assertEquals(3, followUps.first().kentObservationIndex)
        db.close()
    }

    // ────────────────────────────────────────────────────────────────────────
    // ViewModel StateFlow behavior
    // ────────────────────────────────────────────────────────────────────────

    private fun createViewModel(): ConsultationViewModel {
        val context = ApplicationProvider.getApplicationContext<android.app.Application>()
        return ConsultationViewModel(context)
    }

    @Test
    fun `viewmodel - navigation tab selection updates uiState`() {
        val vm = createViewModel()
        assertEquals(NavigationTab.HUD, vm.uiState.value.currentTab)

        vm.selectNavigationTab(NavigationTab.REPERTORY)
        assertEquals(NavigationTab.REPERTORY, vm.uiState.value.currentTab)

        vm.selectNavigationTab(NavigationTab.RX_HERING)
        assertEquals(NavigationTab.RX_HERING, vm.uiState.value.currentTab)
    }

    @Test
    fun `viewmodel - adding a rubric recomputes repertorization`() {
        val vm = createViewModel()
        val before = vm.uiState.value.activeRubrics.size

        vm.addRubricById("r_mind_weeping_easily")

        val state = vm.uiState.value
        assertEquals(before + 1, state.activeRubrics.size)
        assertTrue(state.remedyScores.isNotEmpty())
        // Grief + sun + salt + weeping keynotes still crown Nat-m
        assertEquals("Nat-m", state.remedyScores.first().remedyCode)
        assertTrue(state.remedyScores.first().confidencePercent > 0)
    }

    @Test
    fun `viewmodel - red-flag utterance surfaces emergency alert`() {
        val vm = createViewModel()
        vm.processUtterance(
            "Doctor, I have crushing chest pain and my left arm is numb, I am sweating a lot.",
            SpeakerType.PATIENT
        )

        val alert = vm.uiState.value.activeRedFlag
        assertNotNull(alert)
        assertTrue(alert!!.condition.contains("Acute Coronary Syndrome"))

        vm.dismissRedFlag()
        assertEquals(null, vm.uiState.value.activeRedFlag)
    }

    @Test
    fun `viewmodel - case mode, LM protocol and Kent evaluation propagate`() {
        val vm = createViewModel()

        vm.setCaseMode(com.example.similimumai.data.model.CaseMode.FOLLOW_UP)
        assertEquals("FOLLOW_UP", vm.uiState.value.caseMode.name)

        vm.updateLmParameters("LM2", isHypersensitive = true)
        val lm = vm.uiState.value.lmProtocol
        assertNotNull(lm)
        assertEquals("LM 2", lm!!.potency)
        assertEquals(4, lm.succussions)

        vm.evaluateHering(insideToOut = true, aboveDown = true, vitalToLess = true, reverseTime = true)
        assertTrue(vm.uiState.value.heringEvaluation!!.prognosisVerdict.contains("Cure in Progress"))
    }

    @Test
    fun `viewmodel - saving a session persists prescription, symptom records and follow-up`() {
        val vm = createViewModel()
        vm.processUtterance(
            "Doctor, I have been in deep grief for months and the sun makes my head split.",
            SpeakerType.PATIENT
        )
        vm.evaluateHering(insideToOut = true, aboveDown = true, vitalToLess = true, reverseTime = true)
        vm.saveSessionToDatabase()
        shadowOf(Looper.getMainLooper()).idle()

        val db = ConsultationDatabase.getDatabase(
            ApplicationProvider.getApplicationContext<android.app.Application>()
        )
        runBlocking {
            val sessions = db.sessionDao().getAllSessions().first()
            assertTrue(sessions.isNotEmpty())
            val last = sessions.first()
            assertEquals("Nat-m", last.prescribedRemedy)

            val prescriptions = db.prescriptionDao().getPrescriptionsForSession(last.id).first()
            assertEquals(1, prescriptions.size)
            assertEquals("Nat-m", prescriptions.first().prescribedRemedy)

            val records = db.symptomRecordDao().getBySession(last.id).first()
            assertTrue("symptom records persisted", records.isNotEmpty())
            assertTrue(records.any { it.location.contains("Head") })

            val followUps = db.followUpDao().getBySession(last.id).first()
            assertEquals(1, followUps.size)
            assertTrue(followUps.first().insideOutward)
        }
    }

    // ────────────────────────────────────────────────────────────────────────
    // AudioSpeechManager states
    // ────────────────────────────────────────────────────────────────────────

    @Test
    fun `audio manager - idle state is quiet and stopListening is safe no-op`() {
        val context = ApplicationProvider.getApplicationContext<android.app.Application>()
        val manager = AudioSpeechManager(context)

        assertEquals(false, manager.isListening.value)
        assertEquals(0f, manager.liveRmsDb.value, 0.0001f)

        // Stopping while idle must not throw and must keep the idle state
        manager.stopListening()
        assertEquals(false, manager.isListening.value)
        assertEquals(0f, manager.liveRmsDb.value, 0.0001f)
    }
}

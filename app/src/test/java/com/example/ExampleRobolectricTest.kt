package com.example

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.data.local.ConsultationDatabase
import com.example.data.local.entity.*
import com.example.data.repository.ConsultationRepository
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

    private lateinit var database: ConsultationDatabase
    private lateinit var repository: ConsultationRepository

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, ConsultationDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        repository = ConsultationRepository(database)
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("Similimum AI", appName)
    }

    @Test
    fun `save and retrieve patient and prescription in Room`() = runBlocking {
        val patient = PatientEntity(
            mrn = "SIM-TEST-001",
            fullName = "Ananya Roy",
            age = 29,
            gender = "Female",
            thermalPreference = "CHILLY",
            miasmaticTendency = "PSORIC"
        )
        val patientId = repository.savePatient(patient)
        assertTrue(patientId > 0)

        val retrieved = repository.getPatientByMrn("SIM-TEST-001")
        assertNotNull(retrieved)
        assertEquals("Ananya Roy", retrieved!!.fullName)

        // Save session
        val session = ConsultationSessionEntity(
            patientId = patientId,
            mode = "CHRONIC",
            transcript = "[PATIENT] Severe right-sided migraine.",
            clinicalNotes = "Prescribed Lycopodium",
            lsmcScore = 80,
            miasmaticDominance = "PSORA"
        )
        val symptoms = listOf(
            SymptomRecordEntity(
                sessionId = 0L,
                location = "Right temple",
                sensation = "Throbbing",
                modalityAgg = "4 PM to 8 PM",
                modalityAmel = "Warm drinks",
                concomitant = "Abdominal bloating",
                isCompleteLsmc = true
            )
        )
        val rubrics = listOf(
            SelectedRubricEntity(
                sessionId = 0L,
                rubricId = "HEAD-PAIN-RIGHT",
                rubricPath = "HEAD - PAIN - Right side",
                weight = 3,
                chapter = "HEAD"
            )
        )
        val sessionId = repository.saveFullConsultation(session, symptoms, rubrics)
        assertTrue(sessionId > 0)

        // Save Prescription
        val rx = PrescriptionRecordEntity(
            sessionId = sessionId,
            patientId = patientId,
            remedyCode = "Lyc",
            remedyName = "Lycopodium Clavatum",
            potency = "200C",
            posologyScale = "CENTESIMAL",
            status = "DISPENSED"
        )
        val rxId = repository.savePrescription(rx)
        assertTrue(rxId > 0)

        val recentRemedies = repository.getRecentRemediesForPatient(patientId)
        assertEquals(1, recentRemedies.size)
        assertEquals("Lyc", recentRemedies[0])
    }
}


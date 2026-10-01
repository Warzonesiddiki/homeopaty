package com.example.similimumai

import android.app.Application
import android.os.Looper
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.similimumai.data.local.ConsultationDatabase
import com.example.similimumai.data.local.entity.PatientEntity
import com.example.similimumai.ui.viewmodel.ConsultationViewModel
import com.example.similimumai.ui.viewmodel.NavigationTab
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config

/**
 * MVP "patient record search" (docs/product/mvp-scope.md): live Room search
 * by name or MNR plus loading a record as the active consultation patient.
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [35])
class PatientSearchTest {

    private fun inMemoryDatabase(): ConsultationDatabase {
        val context = ApplicationProvider.getApplicationContext<Application>()
        return Room.inMemoryDatabaseBuilder(context, ConsultationDatabase::class.java).build()
    }

    private suspend fun seedPatient(db: ConsultationDatabase, mnr: String, name: String): Long =
        db.patientDao().insertPatient(
            PatientEntity(
                mnr = mnr,
                name = name,
                age = 34,
                sex = "Female",
                thermalState = "CHILLY",
                dominantMiasm = "SYPHILIS",
                chiefComplaint = "migraine with silent grief"
            )
        )

    @Test
    fun `search matches name case-insensitively`() = runBlocking {
        val db = inMemoryDatabase()
        seedPatient(db, "MNR-101", "Priya Sharma")
        seedPatient(db, "MNR-102", "Amit Verma")

        assertEquals(1, db.patientDao().searchPatients("shar").first().size)
        assertEquals(1, db.patientDao().searchPatients("SHARMA").first().size)
        assertEquals(0, db.patientDao().searchPatients("zzz").first().size)
        db.close()
    }

    @Test
    fun `search matches the MNR identifier`() = runBlocking {
        val db = inMemoryDatabase()
        seedPatient(db, "MNR-101", "Priya Sharma")
        seedPatient(db, "MNR-102", "Amit Verma")

        val byMnr = db.patientDao().searchPatients("MNR-102").first()
        assertEquals(1, byMnr.size)
        assertEquals("Amit Verma", byMnr.first().name)

        // empty query returns the whole registry
        assertEquals(2, db.patientDao().searchPatients("").first().size)
        db.close()
    }

    @Test
    fun `selectPatient loads the record as the active consultation patient`() {
        val context = ApplicationProvider.getApplicationContext<Application>()
        val vm = ConsultationViewModel(context)
        val db = ConsultationDatabase.getDatabase(context)

        val patientId = runBlocking {
            seedPatient(db, "MNR-201", "Select Me")
        }
        val patient = runBlocking { db.patientDao().getPatientById(patientId) }!!

        vm.selectPatient(patient)
        shadowOf(Looper.getMainLooper()).idle()

        val state = vm.uiState.value
        assertEquals("Select Me", state.patientName)
        assertEquals(34, state.patientAge)
        assertEquals("Female", state.patientSex)
        assertEquals("migraine with silent grief", state.chiefComplaint)
        assertEquals(patientId, state.activePatientId)
        assertEquals(NavigationTab.HUD, state.currentTab)
    }
}

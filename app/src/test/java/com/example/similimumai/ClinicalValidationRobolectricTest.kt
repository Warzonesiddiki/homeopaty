package com.example.similimumai

import android.os.Looper
import androidx.test.core.app.ApplicationProvider
import com.example.similimumai.data.local.ConsultationDatabase
import com.example.similimumai.data.model.Miasm
import com.example.similimumai.data.model.ThermalState
import com.example.similimumai.ui.viewmodel.ConsultationViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config

/**
 * Tier 2: docs/data/validation-rules.md enforcement at the ViewModel boundary.
 * Robolectric local-JVM shadow engine (sdk 35 = Robolectric 4.16.1 max).
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [35])
class ClinicalValidationRobolectricTest {

    private fun createViewModel(): ConsultationViewModel {
        val context = ApplicationProvider.getApplicationContext<android.app.Application>()
        return ConsultationViewModel(context)
    }

    private fun database(): ConsultationDatabase =
        ConsultationDatabase.getDatabase(
            ApplicationProvider.getApplicationContext<android.app.Application>()
        )

    @Test
    fun `viewmodel - invalid patient profile blocks save to room`() = runBlocking {
        val vm = createViewModel()
        vm.updatePatientProfile("", 250, "unknown", ThermalState.HOT, Miasm.PSORA, "headache")

        vm.saveSessionToDatabase()
        shadowOf(Looper.getMainLooper()).idle()

        val error = vm.uiState.value.saveError
        assertNotNull("save must be blocked with an error", error)
        assertTrue("age error reported: $error", error!!.contains("Age"))
        assertTrue("sex error reported: $error", error.contains("sex"))

        assertTrue(
            "no session may reach Room when validation fails",
            database().sessionDao().getAllSessions().first().isEmpty()
        )
    }

    @Test
    fun `viewmodel - valid profile saves and clears the error`() = runBlocking {
        val vm = createViewModel()
        vm.updatePatientProfile("", 250, "unknown", ThermalState.HOT, Miasm.PSORA, "headache")
        vm.saveSessionToDatabase()
        shadowOf(Looper.getMainLooper()).idle()
        assertNotNull(vm.uiState.value.saveError)

        vm.updatePatientProfile("Asha Rao", 51, "Female", ThermalState.HOT, Miasm.PSORA, "Burning headache for a week")
        vm.saveSessionToDatabase()
        shadowOf(Looper.getMainLooper()).idle()

        assertNull("successful save clears the gate error", vm.uiState.value.saveError)
        assertEquals(1, database().sessionDao().getAllSessions().first().size)
    }

    @Test
    fun `viewmodel - inimical transition is blocked until a typed justification overrides`() = runBlocking {
        val vm = createViewModel()

        // 1) First prescription: Apis (valid default patient)
        vm.updatePrescription("Apis", "30C", "Centesimal (C)", "single dose")
        vm.saveSessionToDatabase()
        shadowOf(Looper.getMainLooper()).idle()
        assertNull(vm.uiState.value.saveError)
        assertEquals(1, database().sessionDao().getAllSessions().first().size)

        // 2) Strictly inimical successor: Rhus-tox flags the conflict in state
        vm.updatePrescription("Rhus-t", "30C", "Centesimal (C)", "single dose")
        assertNotNull(vm.uiState.value.inimicalConflict)

        // 3) Save without justification is blocked — no second session reaches Room
        vm.saveSessionToDatabase()
        shadowOf(Looper.getMainLooper()).idle()
        assertNotNull("inimical gate must block the save", vm.uiState.value.saveError)
        assertEquals(1, database().sessionDao().getAllSessions().first().size)

        // 4) Typed clinical justification overrides the block
        vm.setInimicalJustification("Sequential trial in a chronic case — override documented in chart")
        vm.saveSessionToDatabase()
        shadowOf(Looper.getMainLooper()).idle()
        assertNull("justification clears the gate", vm.uiState.value.saveError)
        assertEquals(2, database().sessionDao().getAllSessions().first().size)
        assertEquals("Rhus-t", database().sessionDao().getAllSessions().first().first().prescribedRemedy)
    }

    @Test
    fun `viewmodel - connectivity state defaults online and clears safely`() {
        val vm = createViewModel()
        assertEquals(true, vm.uiState.value.isOnline)

        // unregistering the network callback + stopping audio must not throw
        vm.onCleared()
    }

    @Test
    fun `viewmodel - invalid doctor profile never reaches room`() = runBlocking {
        val vm = createViewModel()
        vm.updateDoctorProfile(
            fullName = "Dr",
            registrationNumber = "bad registration!",
            qualification = "none",
            clinicName = "",
            clinicAddress = "",
            phoneNumber = "",
            email = ""
        )
        assertNotNull("validation errors must surface", vm.uiState.value.doctorProfileError)
        assertNull(
            "invalid profile must not be persisted",
            database().doctorDao().getDoctor("default").first()
        )

        vm.updateDoctorProfile(
            fullName = "Dr. Valid Practitioner",
            registrationNumber = "HO-777",
            qualification = "BHMS",
            clinicName = "",
            clinicAddress = "",
            phoneNumber = "",
            email = ""
        )
        shadowOf(Looper.getMainLooper()).idle()
        assertNull("valid save clears the error", vm.uiState.value.doctorProfileError)
        assertEquals(
            "Dr. Valid Practitioner",
            database().doctorDao().getDoctor("default").first()?.fullName
        )
    }
}

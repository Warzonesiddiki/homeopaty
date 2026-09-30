package com.example.similimumai

import android.app.Application
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.similimumai.data.local.ConsultationDatabase
import com.example.similimumai.data.local.entity.PatientEntity
import com.example.similimumai.data.local.entity.SessionEntity
import com.example.similimumai.data.sync.BackendSyncApi
import com.example.similimumai.data.sync.CaseSyncDto
import com.example.similimumai.data.sync.CaseSyncService
import com.example.similimumai.data.sync.StubBackendSyncApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * Tier 2: WorkManager sync pipeline logic (docs/ai/offline-strategy.md §3/§4).
 * CaseSyncService is exercised against an in-memory Room database with fake
 * backend APIs — the Worker itself is a thin glue over this service.
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [35])
class CaseSyncServiceTest {

    private class FakeApi(
        private val success: Boolean,
        val pushed: MutableList<CaseSyncDto> = mutableListOf()
    ) : BackendSyncApi {
        override val isConfigured: Boolean = true
        override suspend fun pushCase(dto: CaseSyncDto): Boolean {
            pushed.add(dto)
            return success
        }
    }

    private fun inMemoryDatabase(): ConsultationDatabase {
        val context = ApplicationProvider.getApplicationContext<Application>()
        return Room.inMemoryDatabaseBuilder(context, ConsultationDatabase::class.java).build()
    }

    @Test
    fun `pending sessions are pushed with idempotent payload and marked synced`() = runBlocking {
        val db = inMemoryDatabase()
        val patientDao = db.patientDao()
        val sessionDao = db.sessionDao()
        val patientId = patientDao.insertPatient(
            PatientEntity(
                mnr = "SYNC-001", name = "Sync Patient", age = 40, sex = "Male",
                thermalState = "CHILLY", dominantMiasm = "PSORA",
                chiefComplaint = "test"
            )
        )
        val session = SessionEntity(
            patientId = patientId,
            consultationType = "ACUTE",
            summaryNotes = "test",
            totalityScore = 60,
            prescribedRemedy = "Bell",
            potency = "30C",
            posology = "single dose",
            heringStatus = "Not Evaluated"
        )
        val sessionId = sessionDao.insertSession(session)
        assertEquals("exactly one PENDING session before the sweep", 1, sessionDao.getPendingSyncSessions().size)

        val api = FakeApi(success = true)
        val service = CaseSyncService(sessionDao, api)
        assertEquals(1, service.syncPending())

        assertTrue("no PENDING sessions may remain", sessionDao.getPendingSyncSessions().isEmpty())
        val stored = sessionDao.getAllSessions().first().first { it.id == sessionId }
        assertEquals("SYNCED", stored.syncStatus)

        // idempotency key + clock-drift-safe payload (§3/§4)
        val dto = api.pushed.single()
        assertEquals("case-$sessionId", dto.caseUuid)
        assertEquals(session.patientId, dto.patientId)
        assertEquals(session.sessionDate, dto.sessionDate)
        assertEquals(session.sessionDate, dto.revision)
        assertEquals("Bell", dto.remedy)
        db.close()
    }

    @Test
    fun `unconfigured backend leaves every session pending`() = runBlocking {
        val db = inMemoryDatabase()
        val patientId = db.patientDao().insertPatient(
            PatientEntity(
                mnr = "SYNC-002", name = "Pending Patient", age = 50, sex = "Female",
                thermalState = "HOT", dominantMiasm = "SYPHILIS",
                chiefComplaint = "test"
            )
        )
        db.sessionDao().insertSession(
            SessionEntity(
                patientId = patientId,
                consultationType = "ACUTE",
                summaryNotes = "test",
                totalityScore = 55,
                prescribedRemedy = "Puls",
                potency = "200C",
                posology = "single dose",
                heringStatus = "Not Evaluated"
            )
        )

        val service = CaseSyncService(db.sessionDao(), StubBackendSyncApi())
        assertEquals(0, service.syncPending())
        assertEquals(1, db.sessionDao().getPendingSyncSessions().size)
        db.close()
    }

    @Test
    fun `failed pushes keep the session pending and the next sweep retries`() = runBlocking {
        val db = inMemoryDatabase()
        val patientId = db.patientDao().insertPatient(
            PatientEntity(
                mnr = "SYNC-003", name = "Retry Patient", age = 30, sex = "Male",
                thermalState = "AMBITHERMAL", dominantMiasm = "SYPHILIS",
                chiefComplaint = "test"
            )
        )
        db.sessionDao().insertSession(
            SessionEntity(
                patientId = patientId,
                consultationType = "ACUTE",
                summaryNotes = "test",
                totalityScore = 60,
                prescribedRemedy = "Lyc",
                potency = "30C",
                posology = "single dose",
                heringStatus = "Not Evaluated"
            )
        )

        val failing = FakeApi(success = false)
        assertEquals(0, CaseSyncService(db.sessionDao(), failing).syncPending())
        assertEquals("case still PENDING after failed push", 1, db.sessionDao().getPendingSyncSessions().size)

        val healthy = FakeApi(success = true)
        assertEquals(1, CaseSyncService(db.sessionDao(), healthy).syncPending())
        assertEquals(0, db.sessionDao().getPendingSyncSessions().size)
        db.close()
    }

    @Test
    fun `already synced sessions are never re-pushed`() = runBlocking {
        val db = inMemoryDatabase()
        val patientId = db.patientDao().insertPatient(
            PatientEntity(
                mnr = "SYNC-004", name = "Once Patient", age = 25, sex = "Female",
                thermalState = "CHILLY", dominantMiasm = "PSORA",
                chiefComplaint = "test"
            )
        )
        db.sessionDao().insertSession(
            SessionEntity(
                patientId = patientId,
                consultationType = "ACUTE",
                summaryNotes = "test",
                totalityScore = 60,
                prescribedRemedy = "Ars",
                potency = "30C",
                posology = "single dose",
                heringStatus = "Not Evaluated"
            )
        )

        val first = FakeApi(success = true)
        assertEquals(1, CaseSyncService(db.sessionDao(), first).syncPending())

        val second = FakeApi(success = true)
        assertEquals(0, CaseSyncService(db.sessionDao(), second).syncPending())
        assertTrue("no re-push of synced cases", second.pushed.isEmpty())
        db.close()
    }
}

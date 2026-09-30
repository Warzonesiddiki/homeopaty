package com.example.similimumai.data.sync

import com.example.similimumai.data.local.dao.SessionDao
import com.example.similimumai.data.model.SyncStatus

/**
 * Sync pipeline logic, kept out of the Worker so it is directly testable
 * with an in-memory Room database and a fake [BackendSyncApi]
 * (docs/ai/offline-strategy.md §3; §4 client-wins conflict resolution).
 */
class CaseSyncService(
    private val sessionDao: SessionDao,
    private val syncApi: BackendSyncApi
) {

    /**
     * Pushes every PENDING session to the backend and marks acknowledgements
     * as SYNCED. @return the number of cases newly marked SYNCED.
     */
    suspend fun syncPending(): Int {
        if (!syncApi.isConfigured) return 0

        var synced = 0
        for (session in sessionDao.getPendingSyncSessions()) {
            val acknowledged = runCatching {
                syncApi.pushCase(
                    CaseSyncDto(
                        caseUuid = "case-${session.id}",
                        patientId = session.patientId,
                        sessionDate = session.sessionDate,
                        consultationType = session.consultationType,
                        remedy = session.prescribedRemedy,
                        potency = session.potency,
                        posology = session.posology,
                        revision = session.sessionDate
                    )
                )
            }.getOrDefault(false)

            if (acknowledged) {
                sessionDao.updateSyncStatus(session.id, SyncStatus.SYNCED.name)
                synced++
            }
            // failures keep the session PENDING for the next sweep
        }
        return synced
    }
}

package com.example.similimumai.data.sync

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.similimumai.data.local.ConsultationDatabase
import java.io.IOException

/**
 * WorkManager sync pipeline (docs/ai/offline-strategy.md §3).
 *
 * Periodic sweep every 15 minutes plus a one-shot sweep on network
 * reconnection. Transient (IO) failures schedule a retry via WorkManager's
 * backoff; business-level push failures leave cases PENDING for the next sweep.
 */
class CaseSyncWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    private val syncApi: BackendSyncApi = StubBackendSyncApi()

    override suspend fun doWork(): Result {
        val sessionDao = ConsultationDatabase.getDatabase(applicationContext).sessionDao()
        val service = CaseSyncService(sessionDao, syncApi)

        return try {
            service.syncPending()
            Result.success()
        } catch (e: IOException) {
            Result.retry()
        } catch (e: Exception) {
            // Non-transient failure: report done so WorkManager does not retry
            // forever; the cases remain PENDING for the next scheduled sweep.
            Result.success()
        }
    }
}

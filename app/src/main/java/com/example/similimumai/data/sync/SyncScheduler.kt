package com.example.similimumai.data.sync

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

/**
 * Schedules the case-sync WorkManager requests
 * (docs/ai/offline-strategy.md §3: PeriodicWorkRequest every 15 minutes and a
 * one-shot OneTimeWorkRequest on network reconnection).
 */
object SyncScheduler {

    const val PERIODIC_WORK_NAME = "similimum_case_sync_periodic"
    const val ONESHOT_WORK_NAME = "similimum_case_sync_oneshot"

    /** Enqueue (or keep) the 15-minute periodic sweep. Idempotent. */
    fun schedulePeriodicSync(context: Context) {
        val workManager = WorkManager.getInstance(context)
        val request = PeriodicWorkRequestBuilder<CaseSyncWorker>(15, TimeUnit.MINUTES).build()
        workManager.enqueueUniquePeriodicWork(
            PERIODIC_WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            request
        )
    }

    /** Enqueue a one-shot sweep, e.g. immediately after network reconnect. */
    fun scheduleOneShotSync(context: Context) {
        val workManager = WorkManager.getInstance(context)
        val request = OneTimeWorkRequestBuilder<CaseSyncWorker>().build()
        workManager.enqueueUniqueWork(
            ONESHOT_WORK_NAME,
            ExistingWorkPolicy.REPLACE,
            request
        )
    }
}

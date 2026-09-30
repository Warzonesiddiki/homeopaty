# Similimum AI — Offline Architecture & Synchronization Strategy
`Location: /docs/ai/offline-strategy.md`

---

## 1. Zero-Downtime Consulting Room Philosophy

In Indian clinical environments (tier-2/tier-3 cities, rural clinics, power outages), mobile internet is frequently intermittent. A medical app that freezes or disables core functionality during an outage is dangerous and unacceptable. 

Similimum AI follows an **Offline-First, Cloud-Enhanced** paradigm:

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                          FEATURE MATRIX BY CONNECTIVITY                     │
├──────────────────────────────────────────┬──────────────────────────────────┤
│ 100% OFFLINE (Local SQLite / Room Engine)│ CLOUD-ENHANCED (Requires Network)│
├──────────────────────────────────────────┼──────────────────────────────────┤
│ • Full Patient Records & Case Histories  │ • Gemini 2.5 Flash Deep Synthes. │
│ • 30+ Polychrest Materia Medica Profiles │ • Ambient Multi-turn Cloud STT   │
│ • Kent's Primary Rubrics & FTS5 Search   │ • Full 65k Kent Cloud pgvector   │
│ • Boenninghausen LSMC Manual Grid        │ • Kent's 12 Obs AI Follow-up Eval│
│ • Case Prescription & PDF Export         │ • Remote Cloud Database Backup   │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## 2. On-Device Local Fallback Engine

When network connectivity drops (monitored via Android `ConnectivityManager` and `NetworkCapabilities`):
1. **Status Bar Alert**: The green "AI Connected" pill switches instantly to an amber "Offline Mode (Local Knowledge Base Active)" indicator.
2. **Repertory Search**: The app automatically routes searches to SQLite `repertory_rubrics_fts` table, querying on-device pre-seeded rubrics with zero latency.
3. **Materia Medica**: Doctor can browse complete Boericke profiles for 30+ polychrests locally.
4. **Prescription Recording**: Prescriptions are written to the local SQLite database and flagged with `sync_status = 'PENDING'`.

---

## 3. WorkManager Synchronization Pipeline

Syncing is handled by Android Jetpack `WorkManager`, utilizing `PeriodicWorkRequestBuilder` (every 15 minutes) and one-shot `OneTimeWorkRequestBuilder` triggered immediately upon network reconnection:

```kotlin
class CaseSyncWorker(
    context: Context,
    workerParams: WorkerParameters,
    private val caseDao: CaseDao,
    private val syncApi: BackendSyncApi
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val pendingCases = caseDao.getPendingSyncCases()
        
        for (case in pendingCases) {
            try {
                // Idempotent sync using client-generated UUID
                val response = syncApi.pushCase(case.toDto())
                if (response.isSuccessful) {
                    caseDao.updateSyncStatus(case.id, SyncStatus.SYNCED)
                }
            } catch (e: IOException) {
                return Result.retry()
            }
        }
        return Result.success()
    }
}
```

---

## 4. Conflict Resolution Protocol

Because Similimum AI MVP is single-practitioner scoped:
- **Default Resolution**: **Client-Wins (Doctor Device Authority)**. The local clinic tablet/phone is the single source of clinical truth.
- **Clock Drift Protection**: All timestamps are stored in UTC milliseconds (`System.currentTimeMillis()`) accompanied by monotonically increasing transaction revision counters.

---

## 5. Offline Strategy Assumptions

- **[ASSUMPTION-OFFLINE-01]** Core clinical case-taking, repertory lookup, and prescription writing function completely without network connectivity.
- **[ASSUMPTION-OFFLINE-02]** WorkManager handles retry backoff exponentially (initial backoff 10 seconds, max 1 hour) to preserve device battery during extended outages.

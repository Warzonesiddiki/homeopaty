package com.example.similimumai.data.sync

/**
 * Cloud push endpoint contract for case synchronization
 * (docs/ai/offline-strategy.md §3).
 *
 * The single-practitioner MVP ships with no provisioned backend, so the
 * default implementation reports "not synced"; swap in a network-backed
 * implementation (e.g. Retrofit) when the cloud endpoint lands — the
 * worker, scheduler and sync_status flow all stay unchanged.
 */
interface BackendSyncApi {

    /** False while no endpoint is configured — pending cases are left untouched. */
    val isConfigured: Boolean

    /** @return true when the backend acknowledged the case. */
    suspend fun pushCase(dto: CaseSyncDto): Boolean
}

class StubBackendSyncApi : BackendSyncApi {
    override val isConfigured: Boolean = false
    override suspend fun pushCase(dto: CaseSyncDto): Boolean = false
}

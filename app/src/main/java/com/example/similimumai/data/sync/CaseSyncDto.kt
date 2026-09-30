package com.example.similimumai.data.sync

/**
 * Idempotent push payload for a locally saved consultation
 * (docs/ai/offline-strategy.md §3: "idempotent sync using client-generated UUID";
 * §4: UTC-millisecond timestamps + monotonically increasing revision counter,
 * client-wins conflict resolution).
 */
data class CaseSyncDto(
    val caseUuid: String,       // client-generated idempotency key
    val patientId: Long,
    val sessionDate: Long,      // UTC milliseconds (System.currentTimeMillis)
    val consultationType: String,
    val remedy: String,
    val potency: String,
    val posology: String,
    val revision: Long          // monotonically increasing transaction revision
)

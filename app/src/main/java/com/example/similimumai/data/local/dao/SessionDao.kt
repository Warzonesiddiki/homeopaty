package com.example.similimumai.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.similimumai.data.local.entity.SessionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SessionDao {
    @Query("SELECT * FROM consultation_sessions WHERE patientId = :patientId ORDER BY sessionDate DESC")
    fun getSessionsForPatient(patientId: Long): Flow<List<SessionEntity>>

    @Query("SELECT * FROM consultation_sessions ORDER BY sessionDate DESC")
    fun getAllSessions(): Flow<List<SessionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    // WorkManager sync pipeline (docs/ai/offline-strategy.md §3)
    @Query("SELECT * FROM consultation_sessions WHERE syncStatus = 'PENDING' ORDER BY sessionDate ASC")
    suspend fun getPendingSyncSessions(): List<SessionEntity>

    @Query("UPDATE consultation_sessions SET syncStatus = :syncStatus WHERE id = :id")
    suspend fun updateSyncStatus(id: Long, syncStatus: String)

    suspend fun insertSession(session: SessionEntity): Long

    @Delete
    suspend fun deleteSession(session: SessionEntity)
}

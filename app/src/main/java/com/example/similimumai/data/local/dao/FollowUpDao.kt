package com.example.similimumai.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.similimumai.data.local.entity.FollowUpEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FollowUpDao {
    @Query("SELECT * FROM follow_ups WHERE sessionId = :sessionId ORDER BY sessionDate ASC")
    fun getBySession(sessionId: Long): Flow<List<FollowUpEntity>>

    @Query("SELECT * FROM follow_ups ORDER BY sessionDate DESC")
    fun getAll(): Flow<List<FollowUpEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(followUp: FollowUpEntity): Long
}

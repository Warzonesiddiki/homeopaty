package com.example.similimumai.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.similimumai.data.local.entity.SymptomRecordEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SymptomRecordDao {
    @Query("SELECT * FROM symptom_records WHERE sessionId = :sessionId ORDER BY createdAt ASC")
    fun getBySession(sessionId: Long): Flow<List<SymptomRecordEntity>>

    @Query("SELECT * FROM symptom_records ORDER BY createdAt DESC")
    fun getAll(): Flow<List<SymptomRecordEntity>>

    @Insert
    suspend fun insertAll(records: List<SymptomRecordEntity>)
}

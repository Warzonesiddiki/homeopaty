package com.example.similimumai.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.similimumai.data.local.entity.CaseRubricEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CaseRubricDao {
    @Query("SELECT * FROM case_rubrics WHERE sessionId = :sessionId ORDER BY createdAt ASC")
    fun getBySession(sessionId: Long): Flow<List<CaseRubricEntity>>

    @Query("SELECT * FROM case_rubrics ORDER BY createdAt DESC")
    fun getAll(): Flow<List<CaseRubricEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(rubrics: List<CaseRubricEntity>)
}

package com.example.data.local.dao

import androidx.room.*
import com.example.data.local.entity.ConsultationSessionEntity
import com.example.data.local.entity.SelectedRubricEntity
import com.example.data.local.entity.SymptomRecordEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ConsultationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: ConsultationSessionEntity): Long

    @Query("SELECT * FROM consultation_sessions WHERE patient_id = :patientId ORDER BY timestamp DESC")
    fun getSessionsForPatientFlow(patientId: Long): Flow<List<ConsultationSessionEntity>>

    @Query("SELECT * FROM consultation_sessions WHERE id = :sessionId LIMIT 1")
    suspend fun getSessionById(sessionId: Long): ConsultationSessionEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSymptoms(symptoms: List<SymptomRecordEntity>)

    @Query("SELECT * FROM symptom_records WHERE session_id = :sessionId")
    suspend fun getSymptomsForSession(sessionId: Long): List<SymptomRecordEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSelectedRubrics(rubrics: List<SelectedRubricEntity>)

    @Query("SELECT * FROM selected_rubrics WHERE session_id = :sessionId")
    suspend fun getRubricsForSession(sessionId: Long): List<SelectedRubricEntity>

    @Query("DELETE FROM consultation_sessions WHERE id = :sessionId")
    suspend fun deleteSessionById(sessionId: Long)
}

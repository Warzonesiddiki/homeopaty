package com.example.data.local.dao

import androidx.room.*
import com.example.data.local.entity.PrescriptionRecordEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PrescriptionDao {
    @Query("SELECT * FROM prescription_records WHERE patient_id = :patientId ORDER BY created_at DESC")
    fun getPrescriptionsForPatientFlow(patientId: Long): Flow<List<PrescriptionRecordEntity>>

    @Query("SELECT remedy_code FROM prescription_records WHERE patient_id = :patientId AND status = 'DISPENSED' ORDER BY created_at DESC LIMIT 5")
    suspend fun getRecentRemediesForPatient(patientId: Long): List<String>

    @Query("SELECT * FROM prescription_records ORDER BY created_at DESC LIMIT 50")
    fun getAllRecentPrescriptionsFlow(): Flow<List<PrescriptionRecordEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPrescription(prescription: PrescriptionRecordEntity): Long

    @Query("UPDATE prescription_records SET status = :status WHERE id = :id")
    suspend fun updatePrescriptionStatus(id: Long, status: String)
}

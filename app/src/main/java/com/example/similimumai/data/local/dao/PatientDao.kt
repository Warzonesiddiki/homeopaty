package com.example.similimumai.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.similimumai.data.local.entity.PatientEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PatientDao {
    @Query("SELECT * FROM patients ORDER BY createdAt DESC")
    fun getAllPatients(): Flow<List<PatientEntity>>

    /**
     * Patient record search (docs/product/mvp-scope.md) — case-insensitive
     * match on patient name or MNR, newest first.
     */
    @Query(
        "SELECT * FROM patients WHERE name LIKE '%' || :query || '%' " +
            "OR mnr LIKE '%' || :query || '%' " +
            "ORDER BY name COLLATE NOCASE LIMIT 25"
    )
    fun searchPatients(query: String): Flow<List<PatientEntity>>

    @Query("SELECT * FROM patients WHERE id = :patientId LIMIT 1")
    suspend fun getPatientById(patientId: Long): PatientEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPatient(patient: PatientEntity): Long

    @Update
    suspend fun updatePatient(patient: PatientEntity)

    @Delete
    suspend fun deletePatient(patient: PatientEntity)
}

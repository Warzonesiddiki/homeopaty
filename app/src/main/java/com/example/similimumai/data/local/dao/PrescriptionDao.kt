package com.example.similimumai.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.similimumai.data.local.entity.PrescriptionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PrescriptionDao {
    @Query("SELECT * FROM prescriptions WHERE sessionId = :sessionId ORDER BY prescribedDate ASC")
    fun getPrescriptionsForSession(sessionId: Long): Flow<List<PrescriptionEntity>>

    @Query("SELECT * FROM prescriptions ORDER BY prescribedDate DESC")
    fun getAllPrescriptions(): Flow<List<PrescriptionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPrescription(prescription: PrescriptionEntity): Long
}

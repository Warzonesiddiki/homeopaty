package com.example.similimumai.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.similimumai.data.local.entity.DoctorEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DoctorDao {
    @Query("SELECT * FROM doctors WHERE id = :id LIMIT 1")
    fun getDoctor(id: String): Flow<DoctorEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertDoctor(doctor: DoctorEntity): Long
}

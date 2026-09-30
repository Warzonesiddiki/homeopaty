package com.example.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "patients",
    indices = [
        Index(value = ["mrn"], unique = true),
        Index(value = ["full_name"])
    ]
)
data class PatientEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "mrn")
    val mrn: String,
    @ColumnInfo(name = "full_name")
    val fullName: String,
    @ColumnInfo(name = "age")
    val age: Int,
    @ColumnInfo(name = "gender")
    val gender: String,
    @ColumnInfo(name = "thermal_preference")
    val thermalPreference: String = "AMBITHERMAL",
    @ColumnInfo(name = "miasmatic_tendency")
    val miasmaticTendency: String = "PSORIC",
    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "updated_at")
    val updatedAt: Long = System.currentTimeMillis()
)

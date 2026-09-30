package com.example.similimumai.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "patients")
data class PatientEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val mnr: String,
    val name: String,
    val age: Int,
    val sex: String,
    val thermalState: String, // HOT, CHILLY, AMBITHERMAL
    val dominantMiasm: String, // PSORA, SYCOSIS, SYPHILIS, TUBERCULAR
    val chiefComplaint: String,
    val createdAt: Long = System.currentTimeMillis()
)

package com.example.similimumai.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Local practitioner profile (docs/data/schema.md table 1: doctors).
 * Single-row local store keyed by id "default" for the consulting doctor.
 */
@Entity(tableName = "doctors")
data class DoctorEntity(
    @PrimaryKey
    val id: String = "default",
    val fullName: String = "",
    val registrationNumber: String = "",
    val qualification: String = "",
    val clinicName: String = "",
    val clinicAddress: String = "",
    val phoneNumber: String = "",
    val email: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

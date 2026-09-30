package com.example.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "prescription_records",
    foreignKeys = [
        ForeignKey(
            entity = ConsultationSessionEntity::class,
            parentColumns = ["id"],
            childColumns = ["session_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = PatientEntity::class,
            parentColumns = ["id"],
            childColumns = ["patient_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["session_id"]),
        Index(value = ["patient_id"]),
        Index(value = ["remedy_code"])
    ]
)
data class PrescriptionRecordEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "session_id")
    val sessionId: Long,
    @ColumnInfo(name = "patient_id")
    val patientId: Long,
    @ColumnInfo(name = "remedy_code")
    val remedyCode: String,
    @ColumnInfo(name = "remedy_name")
    val remedyName: String,
    @ColumnInfo(name = "potency")
    val potency: String,
    @ColumnInfo(name = "posology_scale")
    val posologyScale: String = "CENTESIMAL",
    @ColumnInfo(name = "repetition_interval")
    val repetitionInterval: String = "Single dose",
    @ColumnInfo(name = "special_instructions")
    val specialInstructions: String = "",
    @ColumnInfo(name = "status")
    val status: String = "DISPENSED",
    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis()
)

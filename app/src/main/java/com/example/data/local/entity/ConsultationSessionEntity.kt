package com.example.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "consultation_sessions",
    foreignKeys = [
        ForeignKey(
            entity = PatientEntity::class,
            parentColumns = ["id"],
            childColumns = ["patient_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["patient_id"]),
        Index(value = ["timestamp"])
    ]
)
data class ConsultationSessionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "patient_id")
    val patientId: Long,
    @ColumnInfo(name = "timestamp")
    val timestamp: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "mode")
    val mode: String = "CHRONIC",
    @ColumnInfo(name = "transcript")
    val transcript: String = "",
    @ColumnInfo(name = "clinical_notes")
    val clinicalNotes: String = "",
    @ColumnInfo(name = "lsmc_score")
    val lsmcScore: Int = 0,
    @ColumnInfo(name = "miasmatic_dominance")
    val miasmaticDominance: String = "PSORA"
)

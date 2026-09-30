package com.example.similimumai.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Prescription record for a consultation session
 * (docs/03_TECHNICAL_ARCHITECTURE_AISTUDIO.md §5: "PrescriptionEntity:
 * prescribed remedy, potency scale, posology, date, and Hering's law prognosis").
 */
@Entity(
    tableName = "prescriptions",
    foreignKeys = [
        ForeignKey(
            entity = SessionEntity::class,
            parentColumns = ["id"],
            childColumns = ["sessionId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["sessionId"])]
)
data class PrescriptionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val sessionId: Long,
    val prescribedRemedy: String,
    val potencyScale: String, // Centesimal (C), LM/50-Millesimal, Decimal (X), Mother Tincture (Q)
    val potency: String, // 30C, 200C, 1M, LM1 ...
    val posology: String,
    val prescribedDate: Long = System.currentTimeMillis(),
    val heringPrognosis: String // Hering's law verdict at time of prescription
)

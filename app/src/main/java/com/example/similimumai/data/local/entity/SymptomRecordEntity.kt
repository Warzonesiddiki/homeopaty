package com.example.similimumai.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Boenninghausen LSMC structured symptom record for a session
 * (docs/data/schema.md table 4: symptom_records).
 */
@Entity(
    tableName = "symptom_records",
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
data class SymptomRecordEntity(
    @PrimaryKey
    val id: String, // Symptom UUID — one row per extracted symptom
    val sessionId: Long,
    val rawUtterance: String,
    val location: String,
    val sensation: String,
    val modalities: String, // Aggravations and Ameliorations
    val concomitant: String,
    val canonicalRubric: String,
    val isPqrs: Boolean, // §153 Peculiar / Rare / Striking
    val completenessScore: Int, // 0-100 LSMC completeness
    val createdAt: Long = System.currentTimeMillis()
)

package com.example.similimumai.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Hering's Law + Kent's 12 Prognostic Observations follow-up outcome
 * (docs/data/schema.md table 10: follow_ups).
 */
@Entity(
    tableName = "follow_ups",
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
data class FollowUpEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val sessionId: Long,
    val sessionDate: Long = System.currentTimeMillis(),
    val insideOutward: Boolean,
    val aboveDownward: Boolean,
    val vitalToLessVital: Boolean,
    val reverseOrderAppearance: Boolean,
    val kentObservationIndex: Int, // 1 to 12 (0 = not evaluated)
    val clinicalAssessment: String, // Hering verdict / Kent summary
    val recommendedAction: String, // KentObservationAction name or ""
    val createdAt: Long = System.currentTimeMillis()
)

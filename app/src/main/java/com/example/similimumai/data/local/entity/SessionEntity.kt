package com.example.similimumai.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Consultation session (docs/data/schema.md table 3: cases).
 * v4 adds the documented case fields: case_status, transcript_text,
 * clinical_notes, repertory_school.
 */
@Entity(
    tableName = "consultation_sessions",
    foreignKeys = [
        ForeignKey(
            entity = PatientEntity::class,
            parentColumns = ["id"],
            childColumns = ["patientId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["patientId"]), Index(value = ["sessionDate"])]
)
data class SessionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val patientId: Long,
    val sessionDate: Long = System.currentTimeMillis(),
    val consultationType: String, // CHRONIC, ACUTE, FOLLOW_UP
    val caseStatus: String = "ACTIVE", // ACTIVE, COMPLETED, ARCHIVED
    val summaryNotes: String,
    val totalityScore: Int,
    val prescribedRemedy: String,
    val potency: String, // 30C, 200C, 1M, LM1
    val posology: String,
    val heringStatus: String,
    val transcriptText: String = "",
    val clinicalNotes: String = "",
    val repertorySchool: String = "KENT"
)

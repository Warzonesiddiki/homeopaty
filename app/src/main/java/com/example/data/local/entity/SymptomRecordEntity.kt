package com.example.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "symptom_records",
    foreignKeys = [
        ForeignKey(
            entity = ConsultationSessionEntity::class,
            parentColumns = ["id"],
            childColumns = ["session_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["session_id"])
    ]
)
data class SymptomRecordEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "session_id")
    val sessionId: Long,
    @ColumnInfo(name = "location")
    val location: String = "",
    @ColumnInfo(name = "sensation")
    val sensation: String = "",
    @ColumnInfo(name = "modality_agg")
    val modalityAgg: String = "",
    @ColumnInfo(name = "modality_amel")
    val modalityAmel: String = "",
    @ColumnInfo(name = "concomitant")
    val concomitant: String = "",
    @ColumnInfo(name = "miasm")
    val miasm: String = "PSORA",
    @ColumnInfo(name = "is_complete_lsmc")
    val isCompleteLsmc: Boolean = false
)

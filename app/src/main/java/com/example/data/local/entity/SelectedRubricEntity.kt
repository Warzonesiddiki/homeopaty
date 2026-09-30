package com.example.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "selected_rubrics",
    foreignKeys = [
        ForeignKey(
            entity = ConsultationSessionEntity::class,
            parentColumns = ["id"],
            childColumns = ["session_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["session_id"]),
        Index(value = ["rubric_id"])
    ]
)
data class SelectedRubricEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "session_id")
    val sessionId: Long,
    @ColumnInfo(name = "rubric_id")
    val rubricId: String,
    @ColumnInfo(name = "rubric_path")
    val rubricPath: String,
    @ColumnInfo(name = "weight")
    val weight: Int = 3,
    @ColumnInfo(name = "chapter")
    val chapter: String = "",
    @ColumnInfo(name = "remedies_count")
    val remediesCount: Int = 0
)

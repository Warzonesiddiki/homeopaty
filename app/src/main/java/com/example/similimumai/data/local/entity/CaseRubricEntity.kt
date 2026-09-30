package com.example.similimumai.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Active canonical rubric captured in a consultation
 * (docs/data/schema.md table 6: case_rubrics).
 * Composite primary key (sessionId, rubricId) — a rubric appears once per case.
 */
@Entity(
    tableName = "case_rubrics",
    primaryKeys = ["sessionId", "rubricId"],
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
data class CaseRubricEntity(
    @PrimaryKey val sessionId: Long,
    @PrimaryKey val rubricId: String,
    val weight: Int = 2, // 1 to 3
    val isEliminating: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)

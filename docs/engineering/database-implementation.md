# Similimum AI — Local Persistence & Room Database Architecture
`Location: /docs/engineering/database-implementation.md`

---

## 1. Overview & Offline-First Strategy

Similimum AI persists all patient records, consultation sessions, extracted symptoms, selected rubrics, and prescribed remedies locally on the physician's Android device using **AndroidX Room (SQLite ORM)**.

The persistence architecture guarantees:
- **100% Offline Availability**: The doctor can take cases, search historical records, review previous remedies, and formulate prescriptions without active internet connectivity.
- **Relational Integrity**: Foreign key constraints with cascading deletes ensure orphan-free case records while maintaining immutable master pharmacopoeia tables.
- **Reactive Queries**: DAOs expose Kotlin Coroutine `Flow<T>` streams that automatically notify UI components when database records change.
- **DPDP Act 2023 Compliance**: Support for SQLCipher encryption ensures all Protected Health Information (PHI) is encrypted at rest using AES-256.

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                          ROOM RELATIONAL SCHEMA                             │
├─────────────────────┬───────────────────────────┬───────────────────────────┤
│    patients         │   consultation_sessions   │   prescription_records    │
│  - id (PK)          │  - id (PK)                │  - id (PK)                │
│  - mrn (UNIQUE)     │  - patient_id (FK) ───────┼──► patient_id (FK)        │
│  - full_name        │  - timestamp              │  - session_id (FK)        │
│  - thermal_pref     │  - mode                   │  - remedy_code            │
│  - miasm_tendency   │  - lsmc_score             │  - potency & scale        │
│  - created_at       │  - transcript             │  - status (DISPENSED)     │
└──────────┬──────────┴─────────────┬─────────────┴───────────────────────────┘
           │                        │
           ▼                        ▼
┌─────────────────────┐  ┌─────────────────────┐
│   symptom_records   │  │   selected_rubrics  │
│  - id (PK)          │  │  - id (PK)          │
│  - session_id (FK)  │  │  - session_id (FK)  │
│  - location         │  │  - rubric_id        │
│  - sensation        │  │  - rubric_path      │
│  - modality_agg     │  │  - weight (1..5)    │
│  - concomitant      │  │  - chapter          │
└─────────────────────┘  └─────────────────────┘
```

---

## 2. Entity Specifications & Kotlin Schemas

### 2.1 `PatientEntity`
```kotlin
package com.example.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "patients",
    indices = [
        Index(value = ["mrn"], unique = true),
        Index(value = ["full_name"])
    ]
)
data class PatientEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "mrn")
    val mrn: String, // e.g. "SIM-2026-0841"
    @ColumnInfo(name = "full_name")
    val fullName: String,
    @ColumnInfo(name = "age")
    val age: Int,
    @ColumnInfo(name = "gender")
    val gender: String, // "MALE", "FEMALE", "OTHER"
    @ColumnInfo(name = "thermal_preference")
    val thermalPreference: String, // "CHILLY", "HOT", "AMBITHERMAL"
    @ColumnInfo(name = "miasmatic_tendency")
    val miasmaticTendency: String, // "PSORIC", "SYCOTIC", "SYPHILITIC", "TUBERCULAR"
    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "updated_at")
    val updatedAt: Long = System.currentTimeMillis()
)
```

### 2.2 `ConsultationSessionEntity`
```kotlin
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
    val mode: String, // "CHRONIC", "ACUTE", "FOLLOW_UP"
    @ColumnInfo(name = "transcript")
    val transcript: String,
    @ColumnInfo(name = "clinical_notes")
    val clinicalNotes: String,
    @ColumnInfo(name = "lsmc_score")
    val lsmcScore: Int, // 0 to 100% completeness
    @ColumnInfo(name = "miasmatic_dominance")
    val miasmaticDominance: String
)
```

### 2.3 `PrescriptionRecordEntity`
```kotlin
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
    val remedyCode: String, // e.g. "Nat-m"
    @ColumnInfo(name = "remedy_name")
    val remedyName: String, // e.g. "Natrum Muriaticum"
    @ColumnInfo(name = "potency")
    val potency: String, // "30C", "200C", "1M", "LM1"
    @ColumnInfo(name = "posology_scale")
    val posologyScale: String, // "CENTESIMAL", "FIFTY_MILLESIMAL"
    @ColumnInfo(name = "repetition_interval")
    val repetitionInterval: String, // "Single dose", "TDS in water", "Daily morning"
    @ColumnInfo(name = "special_instructions")
    val specialInstructions: String,
    @ColumnInfo(name = "status")
    val status: String, // "DRAFT", "DISPENSED", "CANCELLED"
    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis()
)
```

---

## 3. Data Access Objects (DAOs)

```kotlin
package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.data.local.entity.PatientEntity
import com.example.data.local.entity.ConsultationSessionEntity
import com.example.data.local.entity.PrescriptionRecordEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PatientDao {
    @Query("SELECT * FROM patients ORDER BY updated_at DESC")
    fun getAllPatientsFlow(): Flow<List<PatientEntity>>

    @Query("SELECT * FROM patients WHERE mrn = :mrn LIMIT 1")
    suspend fun getPatientByMrn(mrn: String): PatientEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPatient(patient: PatientEntity): Long
}

@Dao
interface PrescriptionDao {
    @Query("SELECT * FROM prescription_records WHERE patient_id = :patientId ORDER BY created_at DESC")
    fun getPrescriptionsForPatientFlow(patientId: Long): Flow<List<PrescriptionRecordEntity>>

    @Query("SELECT remedy_code FROM prescription_records WHERE patient_id = :patientId AND status = 'DISPENSED' ORDER BY created_at DESC LIMIT 5")
    suspend fun getRecentRemediesForPatient(patientId: Long): List<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPrescription(prescription: PrescriptionRecordEntity): Long
}
```

---

## 4. Encryption & Security Architecture (DPDP Act 2023)

To safeguard sensitive clinical records:
1. **Passphrase Derivation**: A 256-bit AES master key is generated and stored in the hardware-backed **Android Keystore System** (`KeyGenParameterSpec` with `PURPOSE_ENCRYPT or PURPOSE_DECRYPT`).
2. **SQLCipher Integration**: The Room database instance is opened using SQLCipher's `SupportFactory`:
   ```kotlin
   val factory = SupportFactory(keystoreMasterKeyBytes)
   Room.databaseBuilder(context, ConsultationDatabase::class.java, "similimum_secure.db")
       .openHelperFactory(factory)
       .build()
   ```
3. **No Unencrypted Backups**: `android:allowBackup="false"` is enforced in `AndroidManifest.xml` to prevent ADB backup leakage of patient records.

---

## 5. Key Architectural Assumptions

- **[ASSUMPTION-ENG-03]** Local database: Room with SQLite is used for patient management, consultation transcripts, rubric selections, and prescription history.
- **[ASSUMPTION-DATA-01]** SQLite with Room persistence enforces cascading deletes from Patient -> Case -> SymptomRecords while keeping standard canonical RepertoryRubric and Remedy tables immutable.
- **[ASSUMPTION-DB-01]** Prior remedy lookup executes via `getRecentRemediesForPatient()` to supply historical remedies to `HomeopathyKnowledgeEngine.checkInimicalCompatibility()` before a new prescription can be finalized.

package com.example.similimumai.data.local

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.similimumai.data.local.dao.CaseRubricDao
import com.example.similimumai.data.local.dao.DoctorDao
import com.example.similimumai.data.local.dao.FollowUpDao
import com.example.similimumai.data.local.dao.PatientDao
import com.example.similimumai.data.local.dao.PrescriptionDao
import com.example.similimumai.data.local.dao.SessionDao
import com.example.similimumai.data.local.dao.SymptomRecordDao
import com.example.similimumai.data.local.entity.CaseRubricEntity
import com.example.similimumai.data.local.entity.DoctorEntity
import com.example.similimumai.data.local.entity.FollowUpEntity
import com.example.similimumai.data.local.entity.PatientEntity
import com.example.similimumai.data.local.entity.PrescriptionEntity
import com.example.similimumai.data.local.entity.SessionEntity
import com.example.similimumai.data.local.entity.SymptomRecordEntity
import net.zetetic.database.sqlcipher.SupportOpenHelperFactory

@Database(
    entities = [
        PatientEntity::class,
        SessionEntity::class,
        PrescriptionEntity::class,
        SymptomRecordEntity::class,
        FollowUpEntity::class,
        DoctorEntity::class,
        CaseRubricEntity::class
    ],
    version = 6,
    exportSchema = false,
    autoMigrations = [
        AutoMigration(from = 1, to = 2),
        AutoMigration(from = 2, to = 3),
        AutoMigration(from = 3, to = 4),
        AutoMigration(from = 4, to = 5),
        AutoMigration(from = 5, to = 6)
    ]
)
abstract class ConsultationDatabase : RoomDatabase() {
    abstract fun patientDao(): PatientDao
    abstract fun sessionDao(): SessionDao
    abstract fun prescriptionDao(): PrescriptionDao
    abstract fun symptomRecordDao(): SymptomRecordDao
    abstract fun followUpDao(): FollowUpDao
    abstract fun doctorDao(): DoctorDao
    abstract fun caseRubricDao(): CaseRubricDao

    companion object {
        @Volatile
        private var INSTANCE: ConsultationDatabase? = null

        fun getDatabase(context: Context): ConsultationDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ConsultationDatabase::class.java,
                    "similimum_ai_db"
                )
                    // Encrypted on-device storage (mvp-scope.md): SQLCipher 256-bit AES
                    // with a Keystore-wrapped passphrase (SecureDbKey).
                    .openHelperFactory(
                        SupportOpenHelperFactory(
                            SecureDbKey.getOrCreatePassphrase(context.applicationContext).toByteArray()
                        )
                    )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

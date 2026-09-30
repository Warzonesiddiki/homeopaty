package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.local.dao.ConsultationDao
import com.example.data.local.dao.PatientDao
import com.example.data.local.dao.PrescriptionDao
import com.example.data.local.entity.*

@Database(
    entities = [
        PatientEntity::class,
        ConsultationSessionEntity::class,
        SymptomRecordEntity::class,
        SelectedRubricEntity::class,
        PrescriptionRecordEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class ConsultationDatabase : RoomDatabase() {

    abstract fun patientDao(): PatientDao
    abstract fun consultationDao(): ConsultationDao
    abstract fun prescriptionDao(): PrescriptionDao

    companion object {
        @Volatile
        private var INSTANCE: ConsultationDatabase? = null

        fun getDatabase(context: Context): ConsultationDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ConsultationDatabase::class.java,
                    "similimum_consultation.db"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

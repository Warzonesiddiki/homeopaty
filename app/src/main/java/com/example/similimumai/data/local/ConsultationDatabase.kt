package com.example.similimumai.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.similimumai.data.local.dao.PatientDao
import com.example.similimumai.data.local.dao.SessionDao
import com.example.similimumai.data.local.entity.PatientEntity
import com.example.similimumai.data.local.entity.SessionEntity

@Database(
    entities = [PatientEntity::class, SessionEntity::class],
    version = 1,
    exportSchema = false
)
abstract class ConsultationDatabase : RoomDatabase() {
    abstract fun patientDao(): PatientDao
    abstract fun sessionDao(): SessionDao

    companion object {
        @Volatile
        private var INSTANCE: ConsultationDatabase? = null

        fun getDatabase(context: Context): ConsultationDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ConsultationDatabase::class.java,
                    "similimum_ai_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

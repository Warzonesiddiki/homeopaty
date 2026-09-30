package com.example.similimumai.data.repository

import com.example.similimumai.data.local.dao.PatientDao
import com.example.similimumai.data.local.dao.SessionDao
import com.example.similimumai.data.local.entity.PatientEntity
import com.example.similimumai.data.local.entity.SessionEntity
import kotlinx.coroutines.flow.Flow

class ConsultationRepository(
    private val patientDao: PatientDao,
    private val sessionDao: SessionDao
) {
    val allPatients: Flow<List<PatientEntity>> = patientDao.getAllPatients()
    val allSessions: Flow<List<SessionEntity>> = sessionDao.getAllSessions()

    fun getSessionsForPatient(patientId: Long): Flow<List<SessionEntity>> {
        return sessionDao.getSessionsForPatient(patientId)
    }

    suspend fun getPatientById(patientId: Long): PatientEntity? {
        return patientDao.getPatientById(patientId)
    }

    suspend fun savePatient(patient: PatientEntity): Long {
        return patientDao.insertPatient(patient)
    }

    suspend fun updatePatient(patient: PatientEntity) {
        patientDao.updatePatient(patient)
    }

    suspend fun deletePatient(patient: PatientEntity) {
        patientDao.deletePatient(patient)
    }

    suspend fun saveSession(session: SessionEntity): Long {
        return sessionDao.insertSession(session)
    }

    suspend fun deleteSession(session: SessionEntity) {
        sessionDao.deleteSession(session)
    }
}

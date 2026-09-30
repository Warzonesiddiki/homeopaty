package com.example.similimumai.data.repository

import com.example.similimumai.data.local.dao.FollowUpDao
import com.example.similimumai.data.local.dao.PatientDao
import com.example.similimumai.data.local.dao.PrescriptionDao
import com.example.similimumai.data.local.dao.SessionDao
import com.example.similimumai.data.local.dao.SymptomRecordDao
import com.example.similimumai.data.local.entity.FollowUpEntity
import com.example.similimumai.data.local.entity.PatientEntity
import com.example.similimumai.data.local.entity.PrescriptionEntity
import com.example.similimumai.data.local.entity.SessionEntity
import com.example.similimumai.data.local.entity.SymptomRecordEntity
import kotlinx.coroutines.flow.Flow

class ConsultationRepository(
    private val patientDao: PatientDao,
    private val sessionDao: SessionDao,
    private val prescriptionDao: PrescriptionDao,
    private val symptomRecordDao: SymptomRecordDao,
    private val followUpDao: FollowUpDao
) {
    val allPatients: Flow<List<PatientEntity>> = patientDao.getAllPatients()
    val allSessions: Flow<List<SessionEntity>> = sessionDao.getAllSessions()
    val allPrescriptions: Flow<List<PrescriptionEntity>> = prescriptionDao.getAllPrescriptions()

    fun getSessionsForPatient(patientId: Long): Flow<List<SessionEntity>> {
        return sessionDao.getSessionsForPatient(patientId)
    }

    fun getPrescriptionsForSession(sessionId: Long): Flow<List<PrescriptionEntity>> {
        return prescriptionDao.getPrescriptionsForSession(sessionId)
    }

    fun getSymptomRecordsForSession(sessionId: Long): Flow<List<SymptomRecordEntity>> {
        return symptomRecordDao.getBySession(sessionId)
    }

    fun getFollowUpsForSession(sessionId: Long): Flow<List<FollowUpEntity>> {
        return followUpDao.getBySession(sessionId)
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

    suspend fun savePrescription(prescription: PrescriptionEntity): Long {
        return prescriptionDao.insertPrescription(prescription)
    }

    suspend fun saveSymptomRecords(records: List<SymptomRecordEntity>) {
        if (records.isNotEmpty()) symptomRecordDao.insertAll(records)
    }

    suspend fun saveFollowUp(followUp: FollowUpEntity): Long {
        return followUpDao.insert(followUp)
    }
}

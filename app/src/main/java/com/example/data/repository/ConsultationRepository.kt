package com.example.data.repository

import com.example.data.local.ConsultationDatabase
import com.example.data.local.entity.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class ConsultationRepository(private val database: ConsultationDatabase) {

    private val patientDao = database.patientDao()
    private val consultationDao = database.consultationDao()
    private val prescriptionDao = database.prescriptionDao()

    val allPatients: Flow<List<PatientEntity>> = patientDao.getAllPatientsFlow()
    val recentPrescriptions: Flow<List<PrescriptionRecordEntity>> = prescriptionDao.getAllRecentPrescriptionsFlow()

    suspend fun savePatient(patient: PatientEntity): Long = withContext(Dispatchers.IO) {
        patientDao.insertPatient(patient)
    }

    suspend fun getPatientById(id: Long): PatientEntity? = withContext(Dispatchers.IO) {
        patientDao.getPatientById(id)
    }

    suspend fun getPatientByMrn(mrn: String): PatientEntity? = withContext(Dispatchers.IO) {
        patientDao.getPatientByMrn(mrn)
    }

    suspend fun deletePatient(patientId: Long) = withContext(Dispatchers.IO) {
        patientDao.deletePatientById(patientId)
    }

    suspend fun saveFullConsultation(
        session: ConsultationSessionEntity,
        symptoms: List<SymptomRecordEntity>,
        rubrics: List<SelectedRubricEntity>
    ): Long = withContext(Dispatchers.IO) {
        val sessionId = consultationDao.insertSession(session)
        if (symptoms.isNotEmpty()) {
            val mappedSymptoms = symptoms.map { it.copy(sessionId = sessionId) }
            consultationDao.insertSymptoms(mappedSymptoms)
        }
        if (rubrics.isNotEmpty()) {
            val mappedRubrics = rubrics.map { it.copy(sessionId = sessionId) }
            consultationDao.insertSelectedRubrics(mappedRubrics)
        }
        sessionId
    }

    fun getSessionsForPatient(patientId: Long): Flow<List<ConsultationSessionEntity>> {
        return consultationDao.getSessionsForPatientFlow(patientId)
    }

    suspend fun savePrescription(prescription: PrescriptionRecordEntity): Long = withContext(Dispatchers.IO) {
        prescriptionDao.insertPrescription(prescription)
    }

    fun getPrescriptionsForPatient(patientId: Long): Flow<List<PrescriptionRecordEntity>> {
        return prescriptionDao.getPrescriptionsForPatientFlow(patientId)
    }

    suspend fun getRecentRemediesForPatient(patientId: Long): List<String> = withContext(Dispatchers.IO) {
        prescriptionDao.getRecentRemediesForPatient(patientId)
    }
}

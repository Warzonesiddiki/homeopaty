package com.example.similimumai.data.engine

import android.util.Log
import com.example.similimumai.BuildConfig
import com.example.similimumai.data.model.HighYieldQuestion
import com.example.similimumai.data.model.Symptom
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

class GeminiClinicalService {

    private val apiKey: String = try {
        BuildConfig.GEMINI_API_KEY
    } catch (e: Exception) {
        ""
    }

    val isAvailable: Boolean
        get() = apiKey.isNotBlank() && apiKey != "null" && apiKey != "MY_NEW_API_KEY_DEFAULT_VALUE"

    suspend fun analyzeCaseTranscript(
        patientTranscript: String,
        currentSymptoms: List<Symptom>
    ): GeminiCaseAnalysisResult = withContext(Dispatchers.IO) {
        if (!isAvailable) {
            // Fallback to offline deterministic intelligence
            return@withContext fallbackAnalysis(patientTranscript, currentSymptoms)
        }

        try {
            val url = URL("https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=$apiKey")
            val conn = url.openConnection() as HttpURLConnection
            conn.requestMethod = "POST"
            conn.setRequestProperty("Content-Type", "application/json")
            conn.connectTimeout = 8000
            conn.readTimeout = 12000
            conn.doOutput = true

            val systemInstruction = """
                You are Similimum AI, an expert Classical Homeopathy Clinical Reasoning Engine adhering strictly to Hahnemann's Organon of Medicine (§83-§104, §153 PQRS) and Boenninghausen's Complete Symptom Doctrine (LSMC).
                Analyze the patient-doctor consultation transcript and output a strict JSON object with:
                {
                   "constitutionalSummary": "Brief clinical synthesis of the patient's thermal, miasmatic, and emotional state",
                   "dominantMiasm": "PSORA / SYCOSIS / SYPHILIS / TUBERCULAR",
                   "thermalVerdict": "HOT / CHILLY / AMBITHERMAL",
                   "pqrsCharacteristics": ["list of striking uncommon symptoms"],
                   "highYieldQuestions": [
                      {"question": "clinical inquiry", "targetDimension": "Modality / Concomitant / Causation", "rationale": "clinical why"}
                   ],
                   "topSimilimumCandidates": ["Remedy 1", "Remedy 2", "Remedy 3"]
                }
            """.trimIndent()

            val requestBody = JSONObject().apply {
                put("contents", JSONArray().apply {
                    put(JSONObject().apply {
                        put("role", "user")
                        put("parts", JSONArray().apply {
                            put(JSONObject().apply {
                                put("text", "$systemInstruction\n\nPATIENT TRANSCRIPT:\n$patientTranscript")
                            })
                        })
                    })
                })
                put("generationConfig", JSONObject().apply {
                    put("responseMimeType", "application/json")
                    put("temperature", 0.2)
                })
            }

            OutputStreamWriter(conn.outputStream).use { writer ->
                writer.write(requestBody.toString())
                writer.flush()
            }

            val responseCode = conn.responseCode
            if (responseCode == 200) {
                val responseText = conn.inputStream.bufferedReader().use(BufferedReader::readText)
                val jsonResponse = JSONObject(responseText)
                val candidates = jsonResponse.optJSONArray("candidates")
                if (candidates != null && candidates.length() > 0) {
                    val contentObj = candidates.getJSONObject(0).optJSONObject("content")
                    val parts = contentObj?.optJSONArray("parts")
                    val rawAiText = parts?.optJSONObject(0)?.optString("text") ?: ""

                    val parsedData = JSONObject(rawAiText)
                    val constitutional = parsedData.optString("constitutionalSummary", "Classical totality synthesis")
                    val dominantMiasm = parsedData.optString("dominantMiasm", "PSORA")
                    val thermal = parsedData.optString("thermalVerdict", "AMBITHERMAL")

                    val questionsArray = parsedData.optJSONArray("highYieldQuestions")
                    val questions = mutableListOf<HighYieldQuestion>()
                    if (questionsArray != null) {
                        for (i in 0 until questionsArray.length()) {
                            val qObj = questionsArray.getJSONObject(i)
                            questions.add(
                                HighYieldQuestion(
                                    id = "gemini_q_$i",
                                    question = qObj.optString("question"),
                                    targetDimension = qObj.optString("targetDimension"),
                                    clinicalRationale = qObj.optString("rationale")
                                )
                            )
                        }
                    }

                    return@withContext GeminiCaseAnalysisResult(
                        success = true,
                        source = "Gemini 2.5 Flash Cloud AI",
                        constitutionalSummary = constitutional,
                        dominantMiasm = dominantMiasm,
                        thermalVerdict = thermal,
                        questions = if (questions.isNotEmpty()) questions else HomeopathyKnowledgeEngine.generateHighYieldQuestions(currentSymptoms)
                    )
                }
            }
            conn.disconnect()
        } catch (e: Exception) {
            Log.w("GeminiClinicalService", "Cloud call error, using local engine: ${e.message}")
        }

        fallbackAnalysis(patientTranscript, currentSymptoms)
    }

    private fun fallbackAnalysis(
        transcript: String,
        symptoms: List<Symptom>
    ): GeminiCaseAnalysisResult {
        val questions = HomeopathyKnowledgeEngine.generateHighYieldQuestions(symptoms)
        val hasPqrs = symptoms.any { it.isPqrs }
        val summary = if (hasPqrs) {
            "Deterministic Organon Analysis: Striking §153 characteristic symptoms detected. Totality points toward prominent classical polychrest."
        } else {
            "Deterministic Organon Analysis: Common physical symptoms prominent. Additional modalities and thermal verification recommended."
        }

        return GeminiCaseAnalysisResult(
            success = true,
            source = "Local Deterministic Knowledge Engine (Offline Mode)",
            constitutionalSummary = summary,
            dominantMiasm = "PSORA (Functional Diathesis)",
            thermalVerdict = "Determined via physical generals",
            questions = questions
        )
    }
}

data class GeminiCaseAnalysisResult(
    val success: Boolean,
    val source: String,
    val constitutionalSummary: String,
    val dominantMiasm: String,
    val thermalVerdict: String,
    val questions: List<HighYieldQuestion>
)

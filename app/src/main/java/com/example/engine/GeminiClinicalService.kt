package com.example.engine

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

data class GeminiAnalysisResult(
    val reasoningSummary: String,
    val suggestedRubrics: List<String>,
    val followUpQuestions: List<String>,
    val topRemediesMentioned: List<String>,
    val redFlagNotice: String?
)

object GeminiClinicalService {

    private val client = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(25, TimeUnit.SECONDS)
        .build()

    private val jsonMediaType = "application/json; charset=utf-8".toMediaType()

    suspend fun consultGemini(
        apiKey: String,
        transcriptText: String,
        patientName: String,
        patientAge: Int,
        patientGender: String,
        caseMode: String
    ): GeminiAnalysisResult? = withContext(Dispatchers.IO) {
        if (apiKey.isBlank()) return@withContext null

        try {
            val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=$apiKey"

            val systemInstruction = """
                You are Similimum AI, an expert Homeopathic Clinical Co-Pilot assisting a qualified doctor.
                Analyze the patient's transcript according to Hahnemann's Organon (§83-§104, §153) and Boenninghausen's LSMC.
                Respond with valid JSON with keys:
                "reasoningSummary": brief clinical evaluation,
                "suggestedRubrics": list of Kent repertory rubrics (e.g. "MIND - CONSOLATION - agg."),
                "followUpQuestions": top 3 non-leading follow-up questions to ask next,
                "topRemediesMentioned": list of top 3 candidate remedies,
                "redFlagNotice": string if medical emergency is detected, or null.
            """.trimIndent()

            val prompt = """
                Patient: $patientName, $patientAge $patientGender. Mode: $caseMode.
                Transcript:
                $transcriptText
            """.trimIndent()

            val requestBodyJson = JSONObject().apply {
                put("contents", JSONArray().apply {
                    put(JSONObject().apply {
                        put("role", "user")
                        put("parts", JSONArray().apply {
                            put(JSONObject().apply {
                                put("text", "$systemInstruction\n\n$prompt")
                            })
                        })
                    })
                })
                put("generationConfig", JSONObject().apply {
                    put("responseMimeType", "application/json")
                    put("temperature", 0.2)
                })
            }

            val request = Request.Builder()
                .url(url)
                .post(requestBodyJson.toString().toRequestBody(jsonMediaType))
                .build()

            val response = client.newCall(request).execute()
            if (!response.isSuccessful) {
                return@withContext null
            }

            val responseBody = response.body?.string() ?: return@withContext null
            val root = JSONObject(responseBody)
            val candidates = root.optJSONArray("candidates") ?: return@withContext null
            if (candidates.length() == 0) return@withContext null
            val firstCandidate = candidates.getJSONObject(0)
            val content = firstCandidate.optJSONObject("content") ?: return@withContext null
            val parts = content.optJSONArray("parts") ?: return@withContext null
            if (parts.length() == 0) return@withContext null
            val textOutput = parts.getJSONObject(0).optString("text")

            val parsedJson = JSONObject(textOutput)
            val rubricsArray = parsedJson.optJSONArray("suggestedRubrics") ?: JSONArray()
            val questionsArray = parsedJson.optJSONArray("followUpQuestions") ?: JSONArray()
            val remediesArray = parsedJson.optJSONArray("topRemediesMentioned") ?: JSONArray()

            val rubrics = mutableListOf<String>()
            for (i in 0 until rubricsArray.length()) {
                rubrics.add(rubricsArray.getString(i))
            }

            val questions = mutableListOf<String>()
            for (i in 0 until questionsArray.length()) {
                questions.add(questionsArray.getString(i))
            }

            val remedies = mutableListOf<String>()
            for (i in 0 until remediesArray.length()) {
                remedies.add(remediesArray.getString(i))
            }

            GeminiAnalysisResult(
                reasoningSummary = parsedJson.optString("reasoningSummary", "Gemini clinical synthesis complete."),
                suggestedRubrics = rubrics,
                followUpQuestions = questions,
                topRemediesMentioned = remedies,
                redFlagNotice = if (parsedJson.has("redFlagNotice") && !parsedJson.isNull("redFlagNotice")) parsedJson.getString("redFlagNotice") else null
            )
        } catch (e: Exception) {
            null
        }
    }
}

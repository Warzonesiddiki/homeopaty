# Similimum AI — Cloud AI Client & Network Integration
`Location: /docs/engineering/api-client-implementation.md`

---

## 1. Cloud Hybrid AI Overview

Similimum AI employs a **Dual-Intelligence Hybrid Architecture**:
1. **Local Deterministic Core (`HomeopathyKnowledgeEngine`)**: Instantly parses symptoms, scores Kentian rubrics, and evaluates safety invariants in < 15ms with 100% offline reliability.
2. **Cloud Reasoning Engine (`GeminiClinicalService`)**: Connects to Google's **Gemini 2.5 Flash** to provide deep miasmatic synthesis, multi-symptom deconvolution, and subtle cross-repertory literature citations when an API key and internet connectivity are available.

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                         DUAL-INTELLIGENCE ROUTING                           │
└─────────────────────────────────────────────────────────────────────────────┘

                             Doctor / Patient Utterance
                                         │
                                         ▼
                             ConsultationViewModel
                                         │
                 ┌───────────────────────┴───────────────────────┐
                 │                                               │
                 ▼ (Instant < 15ms)                              ▼ (Async Cloud)
      HomeopathyKnowledgeEngine                        GeminiClinicalService
      - Red-Flag Keyword Match                         - Check API Key Present
      - 150+ Canonical Rubrics                         - Check Internet Active
      - Exact LSMC Scoring                             - POST /v1beta/models/gemini-2.5-flash
                 │                                               │
                 ▼                                               ▼
         Immediate UI Update                           Cloud Reasoning Return
       (Transcript, Top-3 Bar)                     (Deep Miasm, Subtle Keynotes)
                 ▲                                               │
                 │                                               │
                 └─────────── On Timeout / Error ────────────────┘
                              Transparent Failover
```

---

## 2. Gemini 2.5 Flash REST Client Implementation

The network integration communicates directly with the Google Gemini REST API using `OkHttpClient` and `Moshi`:

### 2.1 Request Payload & Strict JSON Schema
```json
{
  "contents": [
    {
      "role": "user",
      "parts": [
        {
          "text": "Analyze the following homeopathic case consultation turn: 'Doctor, my migraine is throbbing on the right temple, much worse from 10 AM to 3 PM in the sun. I find myself craving extra salt on food, and I cannot stand people consoling me.'"
        }
      ]
    }
  ],
  "generationConfig": {
    "temperature": 0.1,
    "topP": 0.95,
    "responseMimeType": "application/json",
    "responseSchema": {
      "type": "OBJECT",
      "properties": {
        "detectedSymptoms": {
          "type": "ARRAY",
          "items": { "type": "STRING" }
        },
        "suggestedRubricIds": {
          "type": "ARRAY",
          "items": { "type": "STRING" }
        },
        "miasmaticDominance": {
          "type": "STRING",
          "enum": ["PSORA", "SYCOSIS", "SYPHILIS", "TUBERCULAR"]
        },
        "differentialRemedies": {
          "type": "ARRAY",
          "items": {
            "type": "OBJECT",
            "properties": {
              "remedyCode": { "type": "STRING" },
              "confidence": { "type": "INTEGER" },
              "keynoteRationale": { "type": "STRING" }
            },
            "required": ["remedyCode", "confidence", "keynoteRationale"]
          }
        },
        "highYieldQuestions": {
          "type": "ARRAY",
          "items": { "type": "STRING" }
        }
      },
      "required": ["detectedSymptoms", "suggestedRubricIds", "miasmaticDominance", "differentialRemedies", "highYieldQuestions"]
    }
  }
}
```

---

## 3. Resilience, Timeouts & Transparent Offline Failover

Clinical consultations must never stutter or display blocking error dialogues if network connectivity drops.

### 3.1 Network Configuration
- **Connect Timeout**: 3,000 ms
- **Read / Inference Timeout**: 5,000 ms
- **Retry Backoff**: 1 retry with 1,000 ms exponential delay before switching completely to the local engine.

### 3.2 Error Classification & Recovery

| HTTP Status / Exception | Diagnosis | Automated Recovery Action |
|---|---|---|
| `SocketTimeoutException` | Slow 3G / Hospital dead-zone | Instantly drop cloud request; continue with `HomeopathyKnowledgeEngine` results. |
| `UnknownHostException` | Device offline / Flight mode | Mark `isCloudAiActive = false`; serve 100% on-device responses. |
| `HTTP 429 Too Many Requests`| API quota exceeded | Log warning to telemetry; silent fallback to local knowledge engine. |
| `HTTP 401 Unauthorized` | Invalid or expired API key | Prompt physician in Settings panel; suppress cloud AI requests. |
| `JsonDataException` | Malformed response payload | Fallback to deterministic regex-based rubric matcher. |

---

## 4. API Key Security & Privacy Hygiene

1. **Zero Hardcoded Keys**: API keys are never stored in version control or plain Kotlin source code.
2. **Secrets Gradle Plugin**: Keys are managed in `.env` / AI Studio Secrets and injected at compile time via `BuildConfig.GEMINI_API_KEY`:
   ```kotlin
   val apiKey = BuildConfig.GEMINI_API_KEY
   if (apiKey.isNullOrBlank()) {
       Log.i("GeminiClient", "Operating in standalone offline mode.")
   }
   ```
3. **Client-Side PII Scrubbing**: Before any consultation transcript fragment is submitted to Google Gemini, patient names, mobile numbers, and municipal addresses are scrubbed using regex redaction (e.g. replacing patient name with `[PATIENT]`), ensuring full compliance with the Indian DPDP Act 2023.

---

## 5. Key Architectural Assumptions

- **[ASSUMPTION-AI-01]** Gemini 2.5 Flash serves as the primary cloud inference engine via secure backend proxy; direct client-side API key embedding is prohibited.
- **[ASSUMPTION-API-01]** If the external Gemini API call times out (>5000ms) or encounters HTTP 429/500, the app instantly and transparently switches to the local `HomeopathyKnowledgeEngine` without showing any user error alert.
- **[ASSUMPTION-API-02]** Client-side PII sanitization removes all identifiable demographic data prior to payload transmission, keeping medical text completely anonymized.

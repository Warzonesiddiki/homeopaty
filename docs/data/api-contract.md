# Similimum AI — API Contracts & External Interface Specifications
`Location: /docs/data/api-contract.md`

---

## 1. Cloud AI Reasoning Endpoint (Gemini 2.5 Flash)

For deep miasmatic synthesis, differential diagnostic expansion, and complex consultation analysis, the client interfaces with Google's Gemini 2.5 Flash model:

- **Protocol**: HTTPS REST (`POST`)
- **Base URL**: `https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent`
- **Authentication**: `?key={API_KEY}` query parameter or `x-goog-api-key` header.

### 1.1 Request Payload Format

```json
{
  "contents": [
    {
      "role": "user",
      "parts": [
        {
          "text": "ACT AS A MASTER HOMEOPATHIC CLINICAL CO-PILOT...\nPatient: Age 34, Female, Chilly...\nUtterance: 'Sar me fatne jaisa dard dhoop me badhta hai...'"
        }
      ]
    }
  ],
  "generationConfig": {
    "temperature": 0.2,
    "topK": 40,
    "topP": 0.95,
    "maxOutputTokens": 2048,
    "responseMimeType": "application/json"
  }
}
```

### 1.2 Strict JSON Response Schema (`application/json`)

```json
{
  "detectedSymptoms": [
    {
      "text": "sar me fatne jaisa dard dhoop me badhta hai",
      "category": "MODALITY_AGG",
      "location": "Head / Forehead",
      "sensation": "Bursting, throbbing",
      "modality": "Aggravated by exposure to sun and heat",
      "concomitant": "Nausea, photophobia",
      "canonicalRubric": "HEAD - PAIN - sun, from exposure to",
      "isPqrs": true,
      "pqrsExplanation": "Sun aggravation with bursting sensation points directly to Nat-m / Glonoine."
    }
  ],
  "lsmcCompletenessGaps": [
    {
      "symptom": "Headache worse in sun",
      "missingElements": ["CONCOMITANT"]
    }
  ],
  "highYieldFollowUpQuestions": [
    {
      "question": "Does tight pressure or wrapping a cloth around your head give any relief?",
      "clinicalRationale": "Differentiates Natrum Mur (relief from tight pressure) from Belladonna (cannot bear touch or pressure).",
      "targetDomain": "MODALITY"
    }
  ],
  "topRemedies": [
    {
      "remedyCode": "Nat-m",
      "name": "Natrum Muriaticum",
      "confidence": 94,
      "keynoteMatch": "Headache from sun, silent grief, craving for salt",
      "miasm": "PSORA_SYCOTIC"
    }
  ],
  "redFlagAlert": {
    "isEmergency": false,
    "condition": null,
    "recommendedUrgentAction": null
  }
}
```

---

## 2. Cloud Backup Sync Endpoint (Future v2.0 Sync Architecture)

- **Endpoint**: `POST /api/v1/sync/consultation`
- **Auth**: Bearer JWT Token issued via Indian Mobile Number OTP.
- **Payload**: Encrypted AES-256 payload blob containing de-identified case totality.

`[ASSUMPTION-API-01]` If the external Gemini API call times out (>5000ms) or encounters HTTP 429/500, the app instantly and transparently switches to the local `HomeopathyKnowledgeEngine` without showing any user error alert.

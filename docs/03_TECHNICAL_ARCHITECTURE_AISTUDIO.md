# Similimum AI — Homeopathic Clinical Co-Pilot
## 03. Technical Architecture Specification

---

### 1. High-Level Architectural Diagram

```
┌────────────────────────────────────────────────────────────────────────┐
│                        ANDROID CLIENT RUNTIME                          │
│                                                                        │
│   ┌────────────────────────────────────────────────────────────────┐   │
│   │                      PRESENTATION LAYER                        │   │
│   │  MainActivity (Single Activity, enableEdgeToEdge)              │   │
│   │  ├─ TopClinicalStatusBar (Patient, Mode, Mic Waves, Sync)      │   │
│   │  ├─ RedFlagBanner (Emergency Life-Threat Triage)               │   │
│   │  ├─ MiniRemedyLeaderboard (Sticky Top-3 Floating Tracker)      │   │
│   │  └─ Navigation Bar: HUD | LSMC Radar | Repertory | Materia | Rx│   │
│   └───────────────────────────────▲────────────────────────────────┘   │
│                                   │ StateFlow / Actions                │
│   ┌───────────────────────────────▼────────────────────────────────┐   │
│   │                       VIEWMODEL LAYER                          │   │
│   │  ConsultationViewModel (AndroidViewModel)                      │   │
│   │  ├─ UiState: Patient, Symptoms, Rubrics, ActiveRemedies        │   │
│   │  ├─ Audio Stream Handler & Simulation Orchestrator             │   │
│   │  └─ Coroutine Scopes (Dispatchers.Default & Main)              │   │
│   └───────────────▲───────────────────────────────▲────────────────┘   │
│                   │                               │                    │
│   ┌───────────────▼──────────────┐ ┌──────────────▼────────────────┐   │
│   │       INPUT SUBSYSTEM        │ │       INTELLIGENCE CORE        │   │
│   │  AudioSpeechManager          │ │  HomeopathyKnowledgeEngine    │   │
│   │  (Android SpeechRecognizer   │ │  (Local Deterministic Engine) │   │
│   │   + Continuous Loop + Audio  │ │  ├─ 30+ Classical Polychrests │   │
│   │   Waveform RMS Tracker)      │ │  ├─ 150+ Standard Rubrics     │   │
│   │                              │ │  ├─ LSMC Gap Analyzer         │   │
│   │  ClinicalSimulator           │ │  ├─ Inimical Safety Checker   │   │
│   │  (5 Multi-turn Real Cases)   │ │  └─ Red-Flag Evaluator        │   │
│   └──────────────────────────────┘ └──────────────┬────────────────┘   │
│                                                   │ Fallback           │
│                                    ┌──────────────▼────────────────┐   │
│                                    │  GeminiClinicalService        │   │
│                                    │  (Gemini 2.5 Flash via REST)  │   │
│                                    │  - Structured JSON Extraction │   │
│                                    │  - Deep Miasmatic Reasoning   │   │
│                                    └───────────────────────────────┘   │
└────────────────────────────────────────────────────────────────────────┘
```

---

### 2. State Management & Concurrency
- **Reactive StateFlow**: The single source of truth is maintained within `ConsultationViewModel.uiState: StateFlow<ConsultationUiState>`.
- **Atomic Updates**: All symptom additions, rubric insertions, and parameter tweaks dispatch immutable copy transformations.
- **Background Processing**: Speech recognition callbacks and repertorization matrix calculations run on `Dispatchers.Default`, never blocking the main UI thread.

---

### 3. Speech Recognition & Continuous Audio Loop
To overcome the native Android `SpeechRecognizer` timeout limitation on mobile devices:
1. `AudioSpeechManager` registers a `RecognitionListener`.
2. When `onResults` or `onError` (with `ERROR_NO_MATCH` or `ERROR_SPEECH_TIMEOUT`) occurs while `isListening == true`, the recognizer gracefully tears down and instantly schedules a new `SpeechRecognizer.startListening()` session.
3. Raw RMS dB audio changes (`onRmsChanged`) update a dynamic normalized float buffer (0.0f – 1.0f) consumed by the Jetpack Compose `Canvas` waveform visualizer.

---

### 4. Cloud Gemini Integration with Graceful Offline Fallback
- Calls model: `gemini-2.5-flash` endpoint via OkHttp / HttpURLConnection.
- Strict response MIME type `application/json` with response schema defining:
  ```json
  {
    "detectedSymptoms": [...],
    "highYieldQuestions": [...],
    "suggestedRubrics": [...],
    "redFlags": [...],
    "miasmaticDominance": "..."
  }
  ```
- **Fallback Guarantee**: If no API key is configured or the device is offline, `HomeopathyKnowledgeEngine` transparently handles 100% of extraction, questions, and repertorization with zero dropped frames or user error dialogues.

---

### 5. Local Database & Persistence Layer
For long-term storage across clinic days, the app incorporates a Room Database architecture (`ConsultationDatabase`):
- `PatientEntity`: Medical record number, name, age, biological sex, thermal temperament, miasmatic tendency.
- `ConsultationSessionEntity`: Session timestamp, mode (Chronic/Acute/Follow-up), audio transcript, clinical notes.
- `PrescriptionEntity`: Prescribed remedy, potency scale, posology, date, and Hering's law prognosis.

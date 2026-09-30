# Similimum AI — System Architecture Specification
`Location: /docs/engineering/system-architecture.md`

---

## 1. Architectural Philosophy & Clean Architecture Layers

Similimum AI employs a **Single-Activity Clean Architecture** pattern combined with **MVVM (Model-View-ViewModel)** and **Unidirectional Data Flow (UDF)**. 

Separation of concerns ensures that clinical homeopathy algorithms (Hahnemannian Totality, LSMC parsing, Kent/TPB repertorization) remain 100% deterministic, pure Kotlin, and isolated from Android framework lifecycles or cloud network interruptions.

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                            PRESENTATION LAYER                               │
│  MainActivity (Edge-to-Edge Single Activity Container)                      │
│  ├─ TopClinicalStatusBar (Patient MRN, Mode, Audio Waveform, Sync Status)    │
│  ├─ RedFlagBanner (Emergency ACS, Stroke, Meningeal Interception)           │
│  ├─ Workspaces:                                                             │
│  │   [1] CoPilotHudScreen (Ambient Transcript, Ask-Next Deck, Observations) │
│  │   [2] LsmcRadarScreen (Boenninghausen 12-Pillar Completeness Radar)       │
│  │   [3] RepertoryMatrixScreen (Cross-School Kent/TPB Weighted Grid)        │
│  │   [4] MateriaMedicaDiffScreen (Head-to-Head Keynote & Modality Diff)      │
│  │   [5] RxHeringsScreen (Posology, LM Calculator, Hering Follow-Up)         │
│  └─ MiniRemedyLeaderboard (Sticky Top-3 Live Similimum Bar)                 │
└─────────────────────────────────────▲───────────────────────────────────────┘
                                      │ StateFlow<ConsultationUiState>
                                      │ User Events (Click, Voice, Toggle)
┌─────────────────────────────────────▼───────────────────────────────────────┐
│                              VIEWMODEL LAYER                                │
│  ConsultationViewModel (AndroidViewModel)                                   │
│  - Single Source of Truth: MutableStateFlow<ConsultationUiState>            │
│  - Coroutine Scopes: viewModelScope (Dispatchers.Main & Dispatchers.Default)│
│  - SpeechRecognizer & Simulation Orchestration                              │
│  - Event Handlers: onUtteranceReceived, onRubricToggled, onSimilimumSelect  │
└───────────────────────▲─────────────────────────────▲───────────────────────┘
                        │                             │
┌───────────────────────▼──────────────┐ ┌────────────▼───────────────────────┐
│            DOMAIN LAYER              │ │           DATA LAYER                │
│  HomeopathyKnowledgeEngine           │ │  ConsultationDatabase (Room)        │
│  - Boenninghausen LSMC Parser        │ │  ├─ PatientDao & PatientEntity      │
│  - Multi-School Repertorizer         │ │  ├─ ConsultationDao & SessionEntity │
│  - Inimical Drug Safety Matrix       │ │  └─ PrescriptionDao & RxEntity      │
│  - Red-Flag Keyword Rule Engine      │ │                                     │
│  - LM 50-Millesimal Calculator       │ │  GeminiClinicalService (Hybrid AI)  │
│  - Hering's Direction of Cure Engine │ │  ├─ OkHttpClient REST Gateway       │
│                                      │ │  └─ Cloud-to-Local Failover Router  │
└──────────────────────────────────────┘ └─────────────────────────────────────┘
```

---

## 2. Layer Responsibilities & Isolation Boundaries

### 2.1 Presentation Layer (`com.example.ui`)
- **Immutability**: Receives read-only `ConsultationUiState` via `collectAsStateWithLifecycle()`.
- **Zero Business Logic**: Composables never calculate rubric sums, evaluate miasms, or inspect red-flag conditions directly. They render state and dispatch user intents.
- **Hardware Integration**: Renders raw 32-bin normalized RMS audio levels into a dynamic canvas waveform at 60 frames per second.
- **Adaptive Ergonomics**: Uses `BoxWithConstraints` to scale between portrait phone views (single pane with bottom bar) and tablet consulting desk displays (dual-pane master-detail).

### 2.2 ViewModel Layer (`com.example.viewmodel`)
- **State Custodian**: Exposes `uiState: StateFlow<ConsultationUiState>`.
- **Concurrency Orchestration**:
  - Speech transcription events are received on main thread listeners and immediately dispatched to `Dispatchers.Default` for parsing.
  - Heavy repertorization matrix recalculations execute on `Dispatchers.Default` to prevent UI thread frame drops.
  - State updates are applied atomically via `MutableStateFlow.update { copy(...) }`.
- **Lifecycle Safety**: Cancels active audio recording and simulation loops upon `onCleared()`.

### 2.3 Domain Layer (`com.example.engine`)
- **Framework Agnostic**: Pure Kotlin logic without Android SDK dependencies (allowing rapid JVM unit testing under 100ms).
- **Clinical Determinism**: Given the identical set of symptoms and rubrics, the repertorization mathematical score is 100% reproducible across all devices.
- **Safety Guardians**: Intercepts contradictory or dangerous remedy sequences (e.g. *Apis* + *Rhus Tox*) before the prescription reaches the database.

### 2.4 Data Layer (`com.example.data` & `com.example.engine`)
- **Single Source of Truth for Archive**: Room SQLite database maintains relational integrity across patients, sessions, rubrics, and prescriptions.
- **Transparent Fallback Service**: If `GeminiClinicalService` encounters network timeout (>5000ms), 429 rate limit, or absent API key, the engine completes symptom extraction and rubric matching using local knowledge tables with zero user-facing error dialogs.

---

## 3. End-to-End Sequence Workflows

### 3.1 Ambient Consultation Turn Lifecycle

```
User (Doctor/Patient)       AudioSpeechManager         ConsultationViewModel         HomeopathyKnowledgeEngine      Compose UI (HUD)
        │                           │                            │                               │                     │
        │── Spoken Utterance ──────►│                            │                               │                     │
        │   ("severe right head-    │                            │                               │                     │
        │    ache after sun")       │── onResults(transcript) ──►│                               │                     │
        │                           │                            │── checkRedFlag(text) ────────►│                     │
        │                           │                            │◄── RedFlagAlert? (null) ──────│                     │
        │                           │                            │                               │                     │
        │                           │                            │── parseSymptomsAndRubrics() ─►│                     │
        │                           │                            │◄── Symptoms, Rubrics, Qs ─────│                     │
        │                           │                            │                               │                     │
        │                           │                            │── repertorize(activeRubrics) ─►│                    │
        │                           │                            │◄── Ranked Remedy Scores ──────│                     │
        │                           │                            │                                                     │
        │                           │                            │── uiState.update { ... } ──────────────────────────►│
        │                           │                            │                                                     │ Recomposes:
        │                           │                            │                                                     │ - Transcript appends
        │                           │                            │                                                     │ - Rubrics highlight
        │                           │                            │                                                     │ - Top-3 Bar updates
        │                           │                            │                                                     │ - Question deck ready
```

### 3.2 Red-Flag Emergency Interception Workflow

```
Patient Utterance ──► AudioSpeechManager ──► ViewModel ──► checkRedFlag()
                                                                 │
                                                       [Match Detected!]
                                            "crushing chest pain down left arm"
                                                                 │
                                                                 ▼
                                                  EmergencyAlert(
                                                    condition = "Acute Coronary Syndrome",
                                                    urgency = CRITICAL,
                                                    action = "Transfer to ER"
                                                  )
                                                                 │
                                                                 ▼
                                                uiState.update { copy(activeAlert = alert) }
                                                                 │
                                                                 ▼
                                                RedFlagBanner displayed at top of screen
                                                in High-Contrast Dark Crimson (Container: #F8D7DA)
                                                Physician alerted with 1-tap ER action
```

---

## 4. Key Architectural Assumptions

- **[ASSUMPTION-ENG-04]** Architecture pattern: Single-Activity Clean MVVM with reactive Kotlin Coroutines, StateFlow, and UDF (Unidirectional Data Flow).
- **[ASSUMPTION-ARCH-01]** All domain calculations (Repertorization, LSMC scoring, Miasmatic dominance, and Inimical safety) execute synchronously or on `Dispatchers.Default` with sub-15ms latency, ensuring zero stutter in 60fps Compose UI transitions.

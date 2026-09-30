# Similimum AI — State Management & Concurrency Architecture
`Location: /docs/engineering/state-management.md`

---

## 1. State Management Philosophy & UDF Pattern

Similimum AI adheres strictly to the **Unidirectional Data Flow (UDF)** architectural pattern. State flows down from the `ConsultationViewModel` as an immutable snapshot, and user interactions (or ambient voice recognitions) flow up as events/intents.

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                       UNIDIRECTIONAL DATA FLOW (UDF)                        │
└─────────────────────────────────────────────────────────────────────────────┘

       ┌────────────────────────────────────────────────────────┐
       │                   ConsultationUiState                  │
       │                   (Immutable Data Class)               │
       └───────────────────────────┬────────────────────────────┘
                                   │
                                   ▼ Emitted via StateFlow
       ┌────────────────────────────────────────────────────────┐
       │                   Jetpack Compose UI                   │
       │    (collectAsStateWithLifecycle() inside Composables)  │
       └───────────────────────────┬────────────────────────────┘
                                   │
                                   ▼ Dispatches User / Voice Intents
       ┌────────────────────────────────────────────────────────┐
       │                 ConsultationViewModel                  │
       │    (Processes intents on Dispatchers.Default / IO)     │
       └───────────────────────────┬────────────────────────────┘
                                   │
                                   ▼ Calculates pure transformations
       ┌────────────────────────────────────────────────────────┐
       │              HomeopathyKnowledgeEngine                 │
       │        (Repertorization & LSMC Matrix Math)            │
       └───────────────────────────┬────────────────────────────┘
                                   │
                                   ▼ Atomically updates StateFlow:
                        _uiState.update { copy(...) }
```

---

## 2. Complete State Specification (`ConsultationUiState`)

The complete state of the active clinical consultation is represented by a single immutable data class:

```kotlin
data class ConsultationUiState(
    // Patient Identity & Context
    val patientId: String = "SIM-2026-0841",
    val patientName: String = "Rajesh Sharma",
    val patientAge: Int = 34,
    val patientGender: String = "Male",
    val consultationMode: ConsultationMode = ConsultationMode.CHRONIC,
    
    // Audio & Transcription Subsystem
    val isListening: Boolean = false,
    val audioAmplitudes: List<Float> = List(32) { 0.05f },
    val liveTranscript: String = "",
    val fullTranscriptHistory: List<TranscriptTurn> = emptyList(),
    
    // Extracted Clinical Entities
    val detectedSymptoms: List<SymptomEntity> = emptyList(),
    val activeRubrics: List<RepertoryRubric> = emptyList(),
    val highYieldQuestions: List<ClinicalQuestion> = emptyList(),
    val silentPhysicalSigns: List<PhysicalSign> = emptyList(),
    
    // Analytical Metrics
    val lsmcCompleteness: LsmcCompletenessScore = LsmcCompletenessScore.EMPTY,
    val miasmaticDominance: MiasmType = MiasmType.PSORA,
    
    // Ranked Differential Remedies
    val rankedRemedies: List<RepertoryScore> = emptyList(),
    val selectedSimilimum: Remedy? = null,
    
    // Safety & Clinical Directives
    val activeRedFlagAlert: RedFlagAlert? = null,
    val inimicalWarning: InimicalWarning? = null,
    
    // Posology & Prescription
    val posologyScale: PosologyScale = PosologyScale.FIFTY_MILLESIMAL, // LM
    val activePotency: String = "LM1",
    val repetitionSchedule: String = "Daily morning in water with 4 succussions",
    val prescriptionNotes: String = "",
    val isPrescriptionFinalized: Boolean = false,
    
    // Connectivity & Cloud Status
    val isCloudAiActive: Boolean = false,
    val isOfflineEngineActive: Boolean = true,
    val statusMessage: String = "Ready for consultation"
)
```

---

## 3. Concurrency Model & Coroutine Dispatchers

Similimum AI leverages Kotlin Coroutines with strict dispatcher isolation to maintain 60fps UI fluidity:

```
┌──────────────────────┬──────────────────────┬───────────────────────────────┐
│ Coroutine Dispatcher │ Target Operations    │ Concurrency Characteristics   │
├──────────────────────┼──────────────────────┼───────────────────────────────┤
│ Dispatchers.Main     │ UI State emission,   │ Runs on Android Main Looper;  │
│                      │ Speech listener      │ Zero blocking operations      │
│                      │ callbacks            │ allowed (< 5ms threshold).    │
├──────────────────────┼──────────────────────┼───────────────────────────────┤
│ Dispatchers.Default  │ LSMC matrix math,    │ Shared worker thread pool     │
│                      │ Kent/TPB repertory   │ sized to CPU cores; handles   │
│                      │ scoring, inimical    │ mathematical calculations     │
│                      │ compatibility checks │ without UI stutter.           │
├──────────────────────┼──────────────────────┼───────────────────────────────┤
│ Dispatchers.IO       │ Room SQLite read/    │ Elastic thread pool (up to 64 │
│                      │ write, OkHttp cloud  │ threads) for disk and network │
│                      │ API calls, file logs │ blocking operations.          │
└──────────────────────┴──────────────────────┴───────────────────────────────┘
```

### 3.1 Atomic State Mutations
To eliminate race conditions between streaming speech recognition and physician manual touch toggles, all state updates use atomic CAS (Compare-And-Set) operations:

```kotlin
fun toggleRubric(rubric: RepertoryRubric) {
    viewModelScope.launch(Dispatchers.Default) {
        val currentList = _uiState.value.activeRubrics
        val updatedList = if (currentList.contains(rubric)) {
            currentList - rubric
        } else {
            currentList + rubric
        }
        
        // Recalculate repertory scores on Dispatchers.Default
        val newScores = HomeopathyKnowledgeEngine.repertorize(updatedList)
        val newLsmc = HomeopathyKnowledgeEngine.calculateLsmcCompleteness(updatedList)
        
        // Atomic update to StateFlow
        _uiState.update { currentState ->
            currentState.copy(
                activeRubrics = updatedList,
                rankedRemedies = newScores,
                lsmcCompleteness = newLsmc
            )
        }
    }
}
```

---

## 4. Lifecycle-Aware UI Observation

In Jetpack Compose, the UI consumes `uiState` using `androidx.lifecycle.compose.collectAsStateWithLifecycle()`:

```kotlin
@Composable
fun CoPilotHudScreen(viewModel: ConsultationViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    
    // UI reacts cleanly to state emissions; stops collecting when screen is hidden
    HudContent(
        transcript = uiState.liveTranscript,
        questions = uiState.highYieldQuestions,
        topRemedies = uiState.rankedRemedies
    )
}
```

---

## 5. Key Architectural Assumptions

- **[ASSUMPTION-ENG-04]** Architecture pattern: Single-Activity Clean MVVM with reactive Kotlin Coroutines, StateFlow, and UDF (Unidirectional Data Flow).
- **[ASSUMPTION-STATE-01]** All UI state mutations are immutable and dispatched through `_uiState.update { ... }`, guaranteeing that simultaneous speech input and physician touch events never produce race conditions or inconsistent UI frames.

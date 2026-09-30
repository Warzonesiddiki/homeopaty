# Similimum AI — Project Structure & Codebase Organization
`Location: /docs/engineering/project-structure.md`

---

## 1. Directory Tree & Architecture Mapping

Similimum AI enforces a modular, cohesive package structure under the base namespace `com.example`. Source files are organized by functional domain and architectural responsibility, keeping individual files under 450 lines to prevent cognitive overhead and ensure seamless maintenance.

```
app/src/
├── main/
│   ├── AndroidManifest.xml                  # Permissions, Application, MainActivity declaration
│   ├── java/com/example/
│   │   ├── MainActivity.kt                  # Single Activity host with edge-to-edge scaffolding
│   │   ├── audio/
│   │   │   └── AudioSpeechManager.kt        # Continuous SpeechRecognizer loop & RMS audio visualizer
│   │   ├── engine/
│   │   │   ├── HomeopathyKnowledgeEngine.kt # 30+ Polychrests, 150+ Rubrics, LSMC, Inimicals, Red-Flags
│   │   │   └── GeminiClinicalService.kt     # Cloud Gemini 2.5 Flash client with offline fallback
│   │   ├── model/
│   │   │   ├── ClinicalModels.kt            # Patient, ConsultationSession, Simulation, RedFlagAlert models
│   │   │   └── RepertoryModels.kt           # Rubric, Remedy, RepertoryScore, InimicalPair, LMProtocol
│   │   ├── ui/
│   │   │   ├── components/
│   │   │   │   ├── TopClinicalStatusBar.kt  # MRN, patient badge, mode chips, audio waveform, sync indicator
│   │   │   │   ├── RedFlagBanner.kt         # Triage emergency alert banner with dismiss / ER action
│   │   │   │   └── MiniRemedyLeaderboard.kt # Sticky bottom floating pill displaying Top-3 remedy scores
│   │   │   ├── screens/
│   │   │   │   ├── CoPilotHudScreen.kt      # Workspace 1: Live transcript, Question Deck, Silent notes
│   │   │   │   ├── LsmcRadarScreen.kt       # Workspace 2: Boenninghausen LSMC 12-pillar radar & completeness
│   │   │   │   ├── RepertoryMatrixScreen.kt # Workspace 3: Cross-school repertory grid & weight sliders
│   │   │   │   ├── MateriaMedicaDiffScreen.kt # Workspace 4: Head-to-head remedy keynote & modality diff
│   │   │   │   ├── RxHeringsScreen.kt       # Workspace 5: Posology, LM dilution calculator & Hering follow-up
│   │   │   │   └── VisionLabScreen.kt       # Diagnostic imaging & visual symptom inspection lab
│   │   │   └── theme/
│   │   │       ├── Color.kt                 # Botanical Emerald, Crimson Alert, Miasmatic palette tokens
│   │   │       ├── Theme.kt                 # Material 3 Dynamic / Custom Dark & Light color schemes
│   │   │       └── Type.kt                  # Typography scale optimized for clinical data readability
│   │   └── viewmodel/
│   │       └── ConsultationViewModel.kt     # Reactive StateFlow manager, audio bridge, and simulation coordinator
│   └── res/
│       ├── values/
│       │   ├── strings.xml                  # App name, clinical strings, error copy, accessibility descriptions
│       │   └── colors.xml                   # Theme color fallbacks
│       ├── drawable/
│       │   ├── ic_launcher_background.xml   # Adaptive launcher icon background
│       │   └── ic_launcher_foreground.xml   # Adaptive launcher icon botanical caduceus
│       └── mipmap-*/                        # Adaptive icon resources
└── test/
    └── java/com/example/
        ├── ExampleUnitTest.kt               # Local JVM tests for Repertorization, Red-Flags, Inimicals, LM Posology
        └── ExampleRobolectricTest.kt        # Robolectric UI component and lifecycle tests
```

---

## 2. Package Descriptions & Responsibilities

### 2.1 `com.example.audio`
- **`AudioSpeechManager.kt`**: Encapsulates the native Android `SpeechRecognizer` API. Implements an automatic self-healing restart loop to bypass Android's 5-second silence timeout during ambient clinical consultations. Exposes a live 32-bin normalized RMS amplitude buffer for visual waveform feedback.

### 2.2 `com.example.engine`
- **`HomeopathyKnowledgeEngine.kt`**: The deterministic heart of the application. Contains pre-compiled, public-domain classical homeopathic knowledge:
  - 30+ Classical Polychrest Remedies (Latin names, thermal temperaments, miasmatic weights, affinities).
  - 150+ Canonical Rubrics categorized by Kentian chapters (Mind, Head, Throat, Stomach, Abdomen, Stool, Respiratory, Extremities, Sleep, Generalities).
  - Boenninghausen LSMC completeness evaluator.
  - Multi-school repertorization algorithms (Kent Hierarchical, Boenninghausen TPB).
  - Inimical drug interaction rules.
  - Red-flag life-threat keyword triggers.
  - LM 50-Millesimal posology dilution calculators.
- **`GeminiClinicalService.kt`**: Manages HTTP communications with Google Gemini 2.5 Flash via REST endpoints, enforcing strict JSON response schemas and seamlessly falling back to `HomeopathyKnowledgeEngine` during network loss.

### 2.3 `com.example.model`
- **`ClinicalModels.kt`**: Encapsulates patient profiles, consultation state (`ConsultationUiState`), clinical simulation cases, emergency triage alerts, and Hering's law progression records.
- **`RepertoryModels.kt`**: Defines immutable data structures for rubrics, remedy scores, miasmatic classifications (`PSORA`, `SYCOSIS`, `SYPHILIS`, `TUBERCULAR`), and posology protocols.

### 2.4 `com.example.ui.components`
- Modular, self-contained widgets shared across workspaces:
  - `TopClinicalStatusBar`: Renders patient identity, consultation mode badges, live speech waveform visualizer, and connection telemetry.
  - `RedFlagBanner`: Animated emergency banner presenting critical clinical directives with high contrast.
  - `MiniRemedyLeaderboard`: Floating bottom docked sheet showing top-ranking remedies with 1-tap expansion.

### 2.5 `com.example.ui.screens`
- The 5 primary clinical workspaces plus visual analysis screen:
  - `CoPilotHudScreen`: Primary listening screen displaying streaming conversation transcript, high-yield Ask-Next question deck, and silent physical signs.
  - `LsmcRadarScreen`: Visual radar chart depicting coverage across Location, Sensation, Modality, and Concomitant pillars.
  - `RepertoryMatrixScreen`: Interactive grid listing rubrics as rows and remedies as columns, with weights and elimination toggles.
  - `MateriaMedicaDiffScreen`: Side-by-side comparative table highlighting differentiating keynotes, thermals, and side affinities.
  - `RxHeringsScreen`: Prescription formulation, LM potency succussion instructions, and Hering's law follow-up prognosis.
  - `VisionLabScreen`: Photographic and visual inspection interface for dermatological, tongue, and physical manifestation analysis.

### 2.6 `com.example.viewmodel`
- **`ConsultationViewModel.kt`**: Single ViewModel orchestrating the consultation lifecycle, holding the single source of truth `uiState: StateFlow<ConsultationUiState>`, and exposing high-level user actions (`toggleRecording()`, `selectSimulatedCase()`, `toggleRubric()`, `updatePosology()`).

---

## 3. Modularity & File Size Guidelines

1. **Maximum File Length**: No source file shall exceed 500 lines of code. Files exceeding 400 lines must be evaluated for refactoring and decomposition.
2. **Strict TestTag Convention**: All interactive composables must define explicit `Modifier.testTag("tag_name")` in snake_case format for automated test targeting (e.g., `mic_toggle_button`, `rubric_checkbox_nat_m`, `prescribe_fab`).
3. **Pure Functions in Domain Logic**: Algorithms in `HomeopathyKnowledgeEngine` must remain pure functions without mutable global state, ensuring thread safety and deterministic testing.

---

## 4. Key Architectural Assumptions

- **[ASSUMPTION-DEV-01]** Modular, decoupled codebase architecture with small files (<450 lines) and strict type safety is required to prevent AI context overflow during maintenance turns.
- **[ASSUMPTION-PROJ-01]** The `com.example` namespace is preserved to maintain compatibility with existing generated `R` classes, while the `applicationId` uniquely identifies the app in build configurations.

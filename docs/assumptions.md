# Similimum AI — Comprehensive Master Assumption Register
`Location: /docs/assumptions.md`

This document tracks every assumption made during the technical and product architecture of Similimum AI. Each assumption is tagged with a unique ID and mapped to affected documentation phases.

---

## 1. Clinical & Domain Assumptions

- **[ASSUMPTION-CLIN-01]** The primary target user is a qualified, registered Homeopathic Medical Practitioner (BHMS / MD-Hom in India, or equivalent national licensing authority). They possess full knowledge of Hahnemannian principles, Materia Medica, and Repertory conventions.
- **[ASSUMPTION-CLIN-02]** Clinical decision support framing is mandatory across all screens and outputs. The AI acts as an advisory co-pilot; it never prescribes, never generates autonomous treatment plans, and always requires physician validation.
- **[ASSUMPTION-CLIN-03]** Boenninghausen’s LSMC (Location, Sensation, Modality < agg / > amel, Concomitant) and Kent's Hierarchical repertorization (Mental Generals > Physical Generals > Particulars) form the twin algorithmic foundations of symptom completeness and rubric grading.
- **[ASSUMPTION-CLIN-04]** Classical Materia Medica and Repertories utilized as the reference baseline (Kent, Boericke, Allen, Boger, Boenninghausen, Hering) are in the public domain, permitting royalty-free ingestion and vectorization.
- **[ASSUMPTION-CLIN-05]** The system must accommodate mixed colloquial terminology, including vernacular Hindi and Hinglish idioms commonly spoken in Indian clinical OPDs (e.g., *"dhoop mein sar fatna"*, *"pyaas bilkul nahi lagna"*).

---

## 2. Platform & Engineering Architecture Assumptions

- **[ASSUMPTION-ENG-01]** Platform choice: **Native Android using Kotlin and Jetpack Compose** (Material 3). Native Android provides direct low-level hardware access to on-device SpeechRecognizer, zero-latency audio buffering, offline Room database encryption (SQLCipher), and full adherence to Android OS background task management.
- **[ASSUMPTION-ENG-02]** Offline-first architecture: The entire clinical database (remedies, rubrics, patient records, local repertorization) must function 100% offline without active internet. Cloud AI calls (Gemini 2.5 Flash) provide augmented reasoning when connectivity is available, falling back gracefully to the deterministic engine when offline.
- **[ASSUMPTION-ENG-03]** Local database: **Room with SQLite** is used for patient management, consultation transcripts, rubric selections, and prescription history.
- **[ASSUMPTION-ENG-04]** Architecture pattern: Single-Activity Clean MVVM with reactive Kotlin Coroutines, StateFlow, and UDF (Unidirectional Data Flow).

---

## 3. Data Privacy & Regulatory Compliance Assumptions

- **[ASSUMPTION-SEC-01]** Target geography launch is **India (AYUSH)**, governed by the Digital Personal Data Protection Act (DPDP Act 2023) and the National Telemedicine Practice Guidelines.
- **[ASSUMPTION-SEC-02]** Patient health data stored on the Android device must be protected with AES-256 hardware-backed encryption or SQLCipher passphrase protection.
- **[ASSUMPTION-SEC-03]** Audio recordings during ambient consultations are transcribed in real-time in memory and discarded or securely retained solely based on explicit patient and doctor consent toggles. Raw audio files are never transmitted to third parties without encryption.
- **[ASSUMPTION-SEC-04]** The application is classified as a Clinical Decision Support System (CDSS) / Wellness & Informational software under Indian CDSCO rules and does NOT constitute a Software as a Medical Device (SaMD) Class C/D diagnostic tool, provided clear disclaimers and non-prescriptive framing are maintained.

---

## 4. Monetization & Business Assumptions

- **[ASSUMPTION-BIZ-01]** Target model is B2B SaaS Freemium for individual practitioners and small homeopathic clinics:
  - *Free Tier*: Unlimited offline case management, manual repertory lookups, 5 AI-assisted consultations per month.
  - *Pro Tier (₹799/month or ₹7,999/year in India)*: Unlimited ambient listening, Gemini 2.5 Flash deep miasmatic synthesis, PDF case sheet export, multi-device backup.
- **[ASSUMPTION-BIZ-02]** In-App Purchases / Subscriptions are processed via Google Play Billing for Android.

---

## 5. Solo-Developer & AI-Assisted "Vibe Coding" Assumptions

- **[ASSUMPTION-DEV-01]** Development is conducted by a solo engineer using AI coding assistants (AI Studio / Cursor / Claude). Modular, decoupled codebase architecture with small files (<400 lines) and strict type safety is required to prevent AI context overflow.
- **[ASSUMPTION-DEV-02]** Every task will be sized for atomic execution in a single prompt session, with automated local JVM unit tests (`ExampleUnitTest.kt`) verifying mathematical accuracy and safety rules before deployment.

---

## 6. Phase 1 Product Foundation Assumptions

- **[ASSUMPTION-PROD-01]** Target doctors hold valid homeopathic medical qualifications (BHMS, MD-Hom, or international equivalent) and assume full statutory responsibility for prescriptions.
- **[ASSUMPTION-PRD-01]** Patient clinical records are stored locally with AES-256 encryption on the doctor's Android device.
- **[ASSUMPTION-SCOPE-01]** Multi-user receptionist/compounder roles are deferred to v2; v1 is exclusively operated by the consulting doctor.
- **[ASSUMPTION-MOSCOW-01]** Solo developer capacity is focused entirely on P0 and P1 items to achieve a production-ready, medical-grade v1.0 release.
- **[ASSUMPTION-PERSONA-01]** English and Hindi cover over 85% of clinical consultations across urban and semi-urban Indian homeopathic clinics.
- **[ASSUMPTION-STORY-01]** Acceptance criteria are verified via automated JVM unit tests (`ExampleUnitTest.kt`) and manual clinical testing.
- **[ASSUMPTION-FLOW-01]** Average consultation duration is 30–45 minutes for new chronic cases and 10–15 minutes for acute/follow-up visits.
- **[ASSUMPTION-GLOSS-01]** Terminology conforms to Central Council for Research in Homoeopathy (CCRH, India) standards and Liga Medicorum Homoeopathica Internationalis (LMHI).
- **[ASSUMPTION-COMP-01]** Legacy desktop software pricing ($1,000+) creates a massive untapped market of younger and rural practitioners seeking mobile-first subscriptions (under ₹1,000/mo).
- **[ASSUMPTION-METRICS-01]** Privacy-safe event tracking will be implemented via Firebase Analytics with all PII filtering enabled at the client boundary.
- **[ASSUMPTION-MONETIZE-01]** Google Play's 15% service fee for the first $1M USD annual revenue is factored into unit economics, yielding an 85% net margin on digital subscriptions.
- **[ASSUMPTION-ROADMAP-01]** v1.0 MVP establishes the rock-solid core clinical foundation; each subsequent release builds upon an immutable, tested architecture.

---

## 7. Phase 2 UX & Design Assumptions

- **[ASSUMPTION-IA-01]** Flat 5-tab workspace switching preserves internal state (scroll positions, unsaved notes, audio recording status) across all tab transitions.
- **[ASSUMPTION-WIRE-01]** Text-described wireframes accurately reflect responsive Composable component hierarchies implemented in `com.example.ui.screens`.
- **[ASSUMPTION-DESIGN-01]** Material 3 Dynamic Theming on Android 12+ is supported with fallback to custom Botanical Emerald tokens for clinical branding consistency.
- **[ASSUMPTION-UX-01]** Zero-data states provide actionable 1-tap demo actions (e.g., loading sample cases) so first-time users can immediately explore all screens.
- **[ASSUMPTION-RESP-01]** Tablet landscape orientation (1280x800dp or 1920x1200dp) represents the primary consulting desk hardware configuration for private practitioners.
- **[ASSUMPTION-COPY-01]** Mandatory onboarding disclaimer acceptance is stored in encrypted SharedPreferences and must be re-accepted upon major version upgrades.
- **[ASSUMPTION-I18N-01]** Classical remedy Latin names (e.g. *Nux Vomica*, *Arsenicum Album*) remain in Latin across all locales to preserve global pharmacological standardization.
- **[ASSUMPTION-A11Y-01]** All clinical status changes and question deck updates provide semantic TalkBack feedback without triggering audio interference with the active speech recognizer.

---

## 8. Phase 3 Domain Data Model Assumptions

- **[ASSUMPTION-DATA-01]** SQLite with Room persistence enforces cascading deletes from Patient -> Case -> SymptomRecords while keeping standard canonical RepertoryRubric and Remedy tables immutable.
- **[ASSUMPTION-SCHEMA-01]** SQLite foreign keys (`PRAGMA foreign_keys = ON;`) are strictly enforced by the Room Database configuration.
- **[ASSUMPTION-ENUM-01]** Enum names and string representations are immutable across SQLite database migrations to ensure long-term schema backwards compatibility.
- **[ASSUMPTION-DICT-01]** All timestamps (`created_at`, `updated_at`) are stored as 64-bit Unix Epoch milliseconds (`INTEGER`).
- **[ASSUMPTION-VALID-01]** Inimical safety validation executes synchronously before any prescription can transition from `DRAFT` to `DISPENSED`.
- **[ASSUMPTION-API-01]** If the external Gemini API call times out (>5000ms) or encounters HTTP 429/500, the app instantly and transparently switches to the local `HomeopathyKnowledgeEngine` without showing any user error alert.
- **[ASSUMPTION-KB-01]** All ingested texts are curated from verified scholarly digitizations (e.g., National Center for Homeopathy archives and CCRH publications) ensuring zero OCR transcription corruption.
- **[ASSUMPTION-SEED-01]** Pre-seeded clinical cases function seamlessly both online and offline to enable immediate clinician orientation and automated JVM testing.

---

## 9. Phase 4 AI System Assumptions

- **[ASSUMPTION-AI-01]** Gemini 2.5 Flash serves as the primary inference engine via secure backend proxy; direct client-side API key embedding is prohibited.
- **[ASSUMPTION-AI-02]** On-device local SQLite FTS5 handles zero-connectivity fallback for rubric lookups, ensuring the doctor is never blocked during an internet outage.
- **[ASSUMPTION-RAG-01]** Hybrid search uses Reciprocal Rank Fusion (RRF) with vector weight 0.65 and BM25 weight 0.35 to prevent semantic drift away from specific homeopathic modalities.
- **[ASSUMPTION-RAG-02]** Embedding vectors are pre-computed and bundled into the Cloud pgvector database; client devices never compute high-dimensional dense embeddings locally.
- **[ASSUMPTION-PROMPT-01]** All prompts mandate JSON schema outputs to prevent unstructured prose from leaking into native Android Compose UI components.
- **[ASSUMPTION-PROMPT-02]** Every prompt injects Kent's 12 observations and Hering's law as explicit evaluative criteria for follow-ups.
- **[ASSUMPTION-GUARD-01]** Red-flag screening executes locally via regex keyword matching for zero-latency emergency detection before cloud LLM transmission.
- **[ASSUMPTION-GUARD-02]** Any LLM response containing a remedy not found in the local SQLite pharmacopoeia database is silently stripped at the ViewModel layer.
- **[ASSUMPTION-EVAL-01]** The Golden 50 benchmark cases are derived exclusively from public domain classical literature (Kent, Nash, Boericke).
- **[ASSUMPTION-EVAL-02]** Automated clinical testing is executed using mock Gemini responses in local JVM tests and real LLM calls in scheduled nightly CI runs.
- **[ASSUMPTION-OFFLINE-01]** Core clinical case-taking, repertory lookup, and prescription writing function completely without network connectivity.
- **[ASSUMPTION-OFFLINE-02]** WorkManager handles retry backoff exponentially (initial backoff 10 seconds, max 1 hour) to preserve device battery during extended outages.

---

## 10. Phase 5 Engineering Assumptions

- **[ASSUMPTION-TECH-01]** All dependency versions specified in `/gradle/libs.versions.toml` are tested as an integrated, compatible manifest and must remain version-locked to avoid binary incompatibility across the Compose/Kotlin/KSP compiler toolchain.
- **[ASSUMPTION-ARCH-01]** All domain calculations (Repertorization, LSMC scoring, Miasmatic dominance, and Inimical safety) execute synchronously or on `Dispatchers.Default` with sub-15ms latency, ensuring zero stutter in 60fps Compose UI transitions.
- **[ASSUMPTION-PROJ-01]** The `com.example` namespace is preserved to maintain compatibility with existing generated `R` classes, while the `applicationId` uniquely identifies the app in build configurations.
- **[ASSUMPTION-DB-01]** Prior remedy lookup executes via `getRecentRemediesForPatient()` to supply historical remedies to `HomeopathyKnowledgeEngine.checkInimicalCompatibility()` before a new prescription can be finalized.
- **[ASSUMPTION-AUDIO-01]** The self-healing loop in `AudioSpeechManager` maintains continuous acoustic capture across consultation sessions up to 60 minutes with < 0.5% CPU baseline impact during periods of silence.
- **[ASSUMPTION-STATE-01]** All UI state mutations are immutable and dispatched through `_uiState.update { ... }`, guaranteeing that simultaneous speech input and physician touch events never produce race conditions or inconsistent UI frames.
- **[ASSUMPTION-API-02]** Client-side PII sanitization removes all identifiable demographic data prior to payload transmission, keeping medical text completely anonymized.
- **[ASSUMPTION-TEST-01]** Testing execution relies 100% on JVM-based local testing (JUnit + Robolectric); instrumented emulator testing is avoided to maintain sub-10 second feedback loops.

---

## 11. Phase 6 Operations & Deployment Assumptions

- **[ASSUMPTION-OPS-01]** Build pipeline executes automated unit tests on local JVMs in under 60 seconds without requiring an Android emulator.
- **[ASSUMPTION-OPS-02]** API keys are injected at build time via BuildConfig sourced from environment variables; the app functions 100% offline with zero degradation if no key is present.
- **[ASSUMPTION-OPS-03]** All telemetry data transmitted to remote analytics services is strictly de-identified, non-PII, and limited to operational stability metrics.
- **[ASSUMPTION-OPS-04]** Patient case databases are excluded from unencrypted Android Auto-Backup; doctor-controlled encrypted export/import via Storage Access Framework ensures DPDP Act compliance.
- **[ASSUMPTION-OPS-05]** Any cloud AI outage or network latency spike results in instantaneous, zero-latency fallback to the local deterministic engine, guaranteeing zero consultation downtime.
- **[ASSUMPTION-OPS-06]** Zero raw audio recordings are written to device disk or uploaded to external servers; speech data is processed purely in memory and immediately discarded.
- **[ASSUMPTION-OPS-07]** On-device processing of speech recognition and repertorization keeps backend cloud costs below ₹5.00 per active doctor per month, enabling an exceptionally high gross margin (>98%).
- **[ASSUMPTION-OPS-08]** All Google Play health policy requirements, permission justifications, and clinical decision support disclaimers are validated prior to production release promotion.




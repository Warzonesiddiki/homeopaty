# Similimum AI — Incident Response & Operational Runbooks
`Location: /docs/operations/incident-response-runbook.md`

---

## 1. Incident Severity Classification

Similimum AI establishes a 4-tier incident severity taxonomy prioritizing patient safety, diagnostic integrity, and continuous clinic availability:

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                       INCIDENT SEVERITY MATRIX                              │
├──────┬──────────────────────┬────────────────────────┬──────────────────────┤
│ Tier │ Severity             │ Clinical / System Impact│ Target MTTR & Action │
├──────┼──────────────────────┼────────────────────────┼──────────────────────┤
│ **SEV-1**│ **Critical Clinical**│ Failure to trigger an  │ Immediate hotfix     │
│      │ **Safety Hazard**    │ allopathic red flag or │ within 4 hours;      │
│      │                      │ incorrect posology math│ Play Store halt      │
├──────┼──────────────────────┼────────────────────────┼──────────────────────┤
│ **SEV-2**│ **App Crash Loop /** │ App crashes on launch; │ Hotfix release       │
│      │ **Database Failure** │ database corrupted     │ within 12 hours      │
├──────┼──────────────────────┼────────────────────────┼──────────────────────┤
│ **SEV-3**│ **Cloud AI Outage /**│ Gemini 2.5 Flash 429   │ Automatic failover   │
│      │ **API Exhaustion**   │ or server downtime     │ to local engine (0s) │
├──────┼──────────────────────┼────────────────────────┼──────────────────────┤
│ **SEV-4**│ **Cosmetic / Minor** │ Text truncation, minor │ Next scheduled       │
│      │ **UI Glitch**        │ UI alignment issue     │ sprint release       │
└──────┴──────────────────────┴────────────────────────┴──────────────────────┘
```

---

## 2. Operational Runbooks

### 2.1 Runbook 1: Red-Flag Emergency Triage Failure (SEV-1)
- **Trigger**: Clinical advisor or practitioner reports that an acute medical emergency phrase (e.g., *"thunderclap headache with vomiting"*) did not trigger the red banner.
- **Immediate Action**:
  1. Reproduce phrase in local JVM test suite (`ExampleUnitTest.kt`).
  2. Inspect regex patterns in `HomeopathyKnowledgeEngine.checkRedFlag()`.
  3. Append the missing vernacular synonym and regex pattern to the red-flag engine.
  4. Run `gradle :app:testDebugUnitTest`.
  5. Deploy emergency hotfix build with incremented `versionCode`.

### 2.2 Runbook 2: Gemini 2.5 Flash Quota Exhaustion / HTTP 429 (SEV-3)
- **Trigger**: Cloud AI queries return HTTP 429 (Rate Limit Exceeded) or HTTP 503.
- **System Behavior**:
  1. The OkHttp interceptor catches the non-200 HTTP status code.
  2. System logs sanitized diagnostic tag `GEMINI_HTTP_429_FAILOVER`.
  3. `ConsultationViewModel` automatically falls back to `HomeopathyKnowledgeEngine.generateConstitutionalSynthesis()`.
  4. A subtle info badge indicates *"Deterministic Offline Engine Active"* without interrupting the consultation.
- **Remediation**:
  - Request quota increase in Google AI Studio / Google Cloud Console.

### 2.3 Runbook 3: Audio SpeechRecognizer Continuous Silence Hang (SEV-3)
- **Trigger**: Device microphone stops streaming speech after incoming phone call or OS interruption.
- **Automated Self-Healing**:
  1. `AudioSpeechManager` monitors the RMS audio buffer.
  2. If zero callbacks are received for > 8,000 ms while `isListening == true`, the watchdog triggers `restartListening()`.
  3. The audio recognizer unbinds and reinitializes cleanly without user intervention.
- **Manual Doctor Remediation**:
  - Tap the prominent **"STOP MIC"** button, wait 1 second, and tap **"START AMBIENT MIC"**.

### 2.4 Runbook 4: Database Migration Failure (SEV-2)
- **Trigger**: Room database throws `IllegalStateException: A migration from X to Y was required but not found`.
- **System Behavior**:
  1. App catches migration exception inside `RoomDatabase.Builder`.
  2. Safe fallback mode preserves existing SQLite file without dropping tables (`fallbackToDestructiveMigration` is disabled in production).
  3. App surfaces a dedicated "Database Recovery" prompt guiding the doctor to back up before updating.

---

## 3. Post-Incident Review (PIR) Protocol

For any SEV-1 or SEV-2 incident, the engineering team conducts a blameless retrospective within 48 hours:
1. **Root Cause Analysis (5 Whys)**.
2. **Clinical Risk Assessment**: Were any patient consultations directly affected?
3. **Automated Test Gap**: Why did existing unit tests not catch this condition?
4. **Corrective Action Items**: Addition of mandatory test cases to `ExampleUnitTest.kt`.

`[ASSUMPTION-OPS-05]` Any cloud AI outage or network latency spike results in instantaneous, zero-latency fallback to the local deterministic engine, guaranteeing zero consultation downtime.

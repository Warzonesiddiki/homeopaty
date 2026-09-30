# Similimum AI — Monitoring, Observability & Telemetry
`Location: /docs/operations/monitoring-observability.md`

---

## 1. Observability Philosophy in Clinical Systems

In medical AI software, observability serves a dual purpose: ensuring five-nines software reliability while maintaining absolute clinical patient privacy. Traditional mobile telemetry that records raw user inputs, full screen snapshots, or unvetted text payloads is strictly impermissible in a medical consultation context.

Similimum AI enforces a **Zero-PII / Zero-PHI Telemetry Policy**:
- **Permitted Telemetry**: Performance timings, error codes, fallback rates, anonymized rubric frequencies, and crash stack traces.
- **Prohibited Telemetry**: Patient names, ages, phone numbers, addresses, audio recordings, raw spoken transcripts, and clinical case summaries.

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                          TELEMETRY & PRIVACY FILTER                         │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                             │
│  [Consultation Event] ──► [Local PII Sanitizer]                            │
│                                   │                                         │
│               ┌───────────────────┴───────────────────┐                     │
│               ▼                                       ▼                     │
│      [Clinical Sensitive Data]             [Anonymized Operational Metrics] │
│      - Patient Name, MRN                   - Latency (ms)                   │
│      - Spoken Transcripts                  - Engine Mode (Local/Cloud)      │
│      - Doctor Notes                        - Error Type (HTTP 429, Timeout) │
│               │                            - Audio Recognizer Restart Count │
│               ▼                                       │                     │
│      [Stored ONLY locally in                          ▼                     │
│       Encrypted SQLite DB]                 [Firebase Crashlytics / Metrics] │
│                                                                             │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## 2. Crash Reporting & Non-Fatal Logging (Firebase Crashlytics)

Crashlytics captures unhandled exceptions with sanitized metadata:

### 2.1 Sanitized Crash Context
```kotlin
object ClinicalTelemetry {
    fun logNonFatal(tag: String, message: String, throwable: Throwable? = null) {
        if (BuildConfig.ENABLE_DEBUG_LOGGING) {
            android.util.Log.e(tag, message, throwable)
        }
        
        // Sanitize string to remove any unintended phone numbers or emails
        val sanitizedMsg = message
            .replace(Regex("\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}\\b"), "[EMAIL]")
            .replace(Regex("\\b\\d{10}\\b"), "[PHONE]")
            
        FirebaseCrashlytics.getInstance().apply {
            setCustomKey("event_tag", tag)
            log("[$tag] $sanitizedMsg")
            if (throwable != null) {
                recordException(throwable)
            }
        }
    }
}
```

---

## 3. Clinical Reliability & Performance Metrics

The application continuously samples critical health signals across the consultation lifecycle:

| Metric Name | Target Threshold | Critical Trigger | Remediation Action |
|---|---|---|---|
| **Repertorization Latency** | < 15 ms | > 100 ms | Offload matrix calculation to background coroutine thread |
| **Gemini 2.5 Flash Latency** | < 2,500 ms | > 5,000 ms | Trigger auto-timeout and activate local deterministic engine |
| **Speech Recognizer Restart Rate**| < 2 restarts/min | > 6 restarts/min | Release audio session, reinitialize `SpeechRecognizer` |
| **Main Thread Frame Drops** | 0 frames (>16.6ms) | > 5 consecutive | Audit Compose recomposition scopes and list keying |
| **Offline Fallback Ratio** | Track baseline | Spike > 40% | Inspect external API key quota or cloud network reachability |

---

## 4. ANR (Application Not Responding) Prevention

Under Android OS guidelines, any main thread stall exceeding 5,000 ms triggers an ANR dialog. Similimum AI isolates all heavyweight workloads:
1. **Audio Capture**: Managed on dedicated OS audio thread; event callbacks dispatch to coroutine channels.
2. **Repertorization & Scoring**: Computations execute on `Dispatchers.Default`.
3. **Database Transactions**: Room queries execute on `Dispatchers.IO` with indexed SQLite tables.
4. **Cloud Network Requests**: OkHttp calls execute asynchronously with strict 5-second socket timeouts.

---

## 5. Audit Logging for Statutory Clinical Invariants

To verify compliance with clinical safety directives, the local database maintains an encrypted, immutable audit log:
- **Red-Flag Triggers**: Timestamp, matched keyword category (e.g., `ACS_CHEST_PAIN`), and doctor acknowledgment timestamp.
- **Inimical Override Events**: If a physician deliberately dispenses an inimical pair (e.g., *Apis* following *Rhus Tox*), the system logs the override flag alongside the doctor's explicit confirmation.

`[ASSUMPTION-OPS-03]` All telemetry data transmitted to remote analytics services is strictly de-identified, non-PII, and limited to operational stability metrics.

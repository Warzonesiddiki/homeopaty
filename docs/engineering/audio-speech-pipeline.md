# Similimum AI — Ambient Speech Recognition & Audio Pipeline
`Location: /docs/engineering/audio-speech-pipeline.md`

---

## 1. Overview & Acoustic Design Goals

In classical homeopathy, patient consultation is an intimate dialogue. The physician must maintain uninterrupted empathetic eye contact rather than typing on a keyboard. 

The **Ambient Speech Recognition Pipeline** listens passively to the room audio, handles noisy clinical OPD environments in India, tolerates code-switching between English, Hindi, and Hinglish, and visualizes live acoustic activity on a 32-bin waveform visualizer.

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                         AMBIENT AUDIO SUBSYSTEM                             │
├─────────────────────┬───────────────────────────┬───────────────────────────┤
│ Audio Hardware      │ Continuous Loop Engine    │ Compose Presentation      │
│ (Microphone)        │ (AudioSpeechManager)      │ (TopClinicalStatusBar)    │
│                     │                           │                           │
│  Mic Audio Stream   │  SpeechRecognizer         │  Canvas Waveform          │
│  16kHz / Mono PCM   │  Listener Loop            │  32-Bin Floating Bars     │
│        │            │        │                  │        ▲                  │
│        ▼            │        ▼                  │        │                  │
│  onRmsChanged() ───┼─► Normalization Buffer ───┼────────┘                  │
│  (dB level -2..10)  │  (0.0f .. 1.0f array)     │                           │
│                     │        │                  │                           │
│  onResults() ───────┼─► Text Accumulation ──────┼─► Real-Time Transcript    │
│  (Final text)       │        │                  │   & LSMC Tokenization     │
│                     │        ▼                  │                           │
│  onError() ─────────┼─► Auto-Restart Loop ──────┘                           │
│  (Timeout / NoMatch)│   (Zero dropped frames)                               │
└─────────────────────┴───────────────────────────┴───────────────────────────┘
```

---

## 2. Solving the Android Silence Timeout (The Continuous Loop State Machine)

Native Android `SpeechRecognizer` automatically stops and throws `ERROR_SPEECH_TIMEOUT` (Error code 6) or `ERROR_NO_MATCH` (Error code 7) after 3 to 5 seconds of silence. In a clinical consultation, long pauses occur while the patient contemplates their symptoms or while the doctor reflects.

### 2.1 The Auto-Restart State Machine

`AudioSpeechManager` manages an internal state machine that intercepts non-fatal termination codes and gracefully re-instantiates the recognizer:

```kotlin
override fun onError(error: Int) {
    when (error) {
        SpeechRecognizer.ERROR_SPEECH_TIMEOUT,
        SpeechRecognizer.ERROR_NO_MATCH -> {
            // Expected acoustic silence: Gracefully restart recognition
            if (isListeningActive) {
                scheduleRecognizerRestart(delayMs = 150)
            }
        }
        SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> {
            // Teardown stale instance and rebind
            teardownRecognizer()
            scheduleRecognizerRestart(delayMs = 400)
        }
        SpeechRecognizer.ERROR_AUDIO,
        SpeechRecognizer.ERROR_SERVER -> {
            // Temporary hardware or offline transition
            scheduleRecognizerRestart(delayMs = 800)
        }
        SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> {
            isListeningActive = false
            notifyPermissionDenied()
        }
        else -> {
            if (isListeningActive) {
                scheduleRecognizerRestart(delayMs = 500)
            }
        }
    }
}
```

### 2.2 Re-Instantiation Cleanup
To prevent audio track memory leaks, each restart explicitly invokes:
```kotlin
private fun teardownRecognizer() {
    try {
        speechRecognizer?.stopListening()
        speechRecognizer?.cancel()
        speechRecognizer?.destroy()
    } catch (e: Exception) {
        Log.w("AudioSpeechManager", "Safe teardown exception: ${e.message}")
    } finally {
        speechRecognizer = null
    }
}
```

---

## 3. Real-Time RMS Audio Amplitude Normalization

`RecognitionListener.onRmsChanged(rmsdB: Float)` provides decibel power levels ranging from `-2.0 dB` (ambient clinic room silence) to `+10.0 dB` (close-mic loud speech).

### 3.1 Normalization Algorithm
```kotlin
fun normalizeRms(rmsdB: Float): Float {
    val clamped = rmsdB.coerceIn(-2.0f, 10.0f)
    // Scale linearly from [-2.0, 10.0] to [0.0, 1.0]
    val normalized = (clamped + 2.0f) / 12.0f
    // Apply slight non-linear power curve for visual punch in low vocal registers
    return normalized.pow(1.2f)
}
```

### 3.2 32-Bin Circular Visualization Buffer
The normalized float updates a fixed-size circular array of 32 float amplitudes. When the doctor speaks, the array updates at up to 60Hz. Jetpack Compose reads this state inside `Canvas` without triggering full screen recomposition.

---

## 4. Multi-Turn Clinical Simulator Subsystem

For testing and environments without physical speech access (such as headless containers, emulators, or clinical training workshops), `AudioSpeechManager` integrates a high-fidelity **Clinical Simulator Engine**:

| Case Index | Persona & Chief Complaint | Target Similimum | Diagnostic Keynotes Streamed |
|---|---|---|---|
| **Case 1** | 29F, Chronic Migraine | *Natrum Muriaticum* | Bursting right-sided headache, < 10 AM to 3 PM sun, craving salt, silent grief. |
| **Case 2** | 52M, Chronic Dyspepsia | *Lycopodium Clavatum* | Bloating 4–8 PM, flatulence, right-to-left liver pain, craving warm drinks. |
| **Case 3** | 38M, Acute Gastroenteritis | *Arsenicum Album* | Midnight 1 AM diarrhea, burning relieved by hot tea, extreme prostration. |
| **Case 4** | 22F, Post-Viral Dry Cough | *Phosphorus* | Pain behind sternum, craving ice-cold water, < lying on left side. |
| **Case 5** | 7M, Pediatric Otitis Media | *Pulsatilla Pratensis* | Mild weeping child, desires carrying, thirstless, relief in open fresh air. |

The simulator injects realistic pauses (2.5 seconds between conversational turns) and simulates RMS audio amplitude jitter, perfectly exercising the downstream parsing and repertorization pipeline.

---

## 5. Runtime Permissions & Multilingual Localization

- **Permissions**: `android.permission.RECORD_AUDIO` is declared in `AndroidManifest.xml` and requested at runtime via Jetpack Compose's `rememberLauncherForActivityResult(RequestPermission())`.
- **Locale Setting**: The speech recognizer intent defaults to `en-IN` (Indian English) with automatic fallback to `hi-IN` (Hindi) via `RecognizerIntent.EXTRA_LANGUAGE`.

---

## 6. Key Architectural Assumptions

- **[ASSUMPTION-SEC-03]** Audio recordings during ambient consultations are transcribed in real-time in memory and discarded; raw audio files are never saved to disk or transmitted to third parties without explicit physician consent.
- **[ASSUMPTION-AUDIO-01]** The self-healing loop in `AudioSpeechManager` maintains continuous acoustic capture across consultation sessions up to 60 minutes with < 0.5% CPU baseline impact during periods of silence.

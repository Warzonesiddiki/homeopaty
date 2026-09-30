# Similimum AI — Technology Stack & Dependency Matrix
`Location: /docs/engineering/tech-stack.md`

---

## 1. Executive Summary & Stack Rationale

Similimum AI is engineered as an **offline-first, medical-grade Native Android application**. The core design imperative is zero-latency responsiveness in the clinical consultation room, unwavering data privacy compliance under the Indian DPDP Act 2023, and resilient hybrid intelligence that operates seamlessly whether the physician is in a high-bandwidth metropolitan hospital or a zero-connectivity rural dispensary.

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                           TECHNOLOGY STACK SUMMARY                          │
├──────────────────────┬──────────────────────────────────────────────────────┤
│ Language & Runtime   │ Kotlin 2.2.10 / JVM 11 / Android SDK 24 to 36 (ext 1) │
│ UI Framework         │ Jetpack Compose BOM 2024.09.00 (Material Design 3)   │
│ Architecture         │ Single-Activity Clean MVVM + Unidirectional Data Flow│
│ Local Persistence    │ SQLite 3 via AndroidX Room 2.7.0 (with KSP 2.3.5)    │
│ Concurrency          │ Kotlinx Coroutines 1.10.2 + Reactive StateFlow       │
│ Ambient Speech       │ Android Native SpeechRecognizer + RMS Audio Buffer   │
│ Hybrid Cloud AI      │ Google Gemini 2.5 Flash via REST + Schema Enforcement│
│ Networking & JSON    │ Square OkHttp 4.10.0 + Moshi 1.15.2                  │
│ Build Toolchain      │ Gradle 9.1.1 + Secrets Gradle Plugin 2.0.1           │
│ Test Harness         │ JUnit 4.13.2 + Robolectric 4.16.1 + Coroutines Test  │
└──────────────────────┴──────────────────────────────────────────────────────┘
```

---

## 2. Comprehensive Dependency Matrix

Every dependency and plugin version in Similimum AI is centrally pinned in `/gradle/libs.versions.toml` to prevent transitive dependency drift and build non-determinism.

### 2.1 Core Platform & Android Toolchain

| Component / Artifact | Group & Name | Exact Version | Rationale & Clinical Purpose |
|---|---|---|---|
| **Android Gradle Plugin** | `com.android.application` | `9.1.1` | Modern build pipeline, AAPT2 incremental compilation, optimized R8 minification. |
| **Kotlin Toolchain** | `org.jetbrains.kotlin.android` | `2.2.10` | High-performance Kotlin compiler with K2 compiler frontend for rapid compilation. |
| **Compose Compiler Plugin**| `org.jetbrains.kotlin.plugin.compose` | `2.2.10` | Bundled Kotlin Compose plugin aligning directly with Kotlin 2.2.10 language version. |
| **Android SDK Compile** | `compileSdk` | `36 (minorApiLevel = 1)` | Access to Android 16 / SDK 36 platform APIs and modern window management. |
| **Android SDK Min** | `minSdk` | `24` (Android 7.0 Nougat) | Covers >96% of active Indian Android devices in clinical circulation. |
| **Android SDK Target** | `targetSdk` | `36` | Strict adherence to modern background permissions, edge-to-edge layout, and audio policies. |
| **Java Compatibility** | `JavaVersion.VERSION_11` | `11` | Optimized bytecode execution for Android runtime (ART). |

### 2.2 Jetpack Compose & UI Design System

| Component / Artifact | Version Catalog Ref | Exact Version | Clinical Usage & Purpose |
|---|---|---|---|
| **Compose BOM** | `androidx-compose-bom` | `2024.09.00` | Centrally coordinates transitive Compose library compatibility. |
| **Compose UI** | `androidx.compose.ui:ui` | BOM managed | Canvas rendering for Audio RMS Waveform and LSMC Radar charts. |
| **Compose Material 3** | `androidx.compose.material3:material3` | BOM managed | Material 3 components: NavigationBar, FilterChip, Card, Scaffold, OutlinedTextField. |
| **Material Icons Core** | `androidx-compose-material-icons-core` | BOM managed | Baseline clinical navigation and utility icons. |
| **Material Icons Extended**| `androidx-compose-material-icons-extended` | BOM managed | Rich icons: `Mic`, `Psychology`, `LocalPharmacy`, `Shield`, `Warning`, `Analytics`. |
| **Activity Compose** | `androidx-activity-compose` | `1.10.1` | Edge-to-edge window insets (`enableEdgeToEdge()`) and back-handler integration. |
| **Lifecycle Compose** | `androidx-lifecycle-runtime-compose` | `2.8.7` | `collectAsStateWithLifecycle()` for lifecycle-aware state observation without leaks. |
| **Lifecycle ViewModel** | `androidx-lifecycle-viewmodel-compose` | `2.8.7` | Scoped ViewModel factory instantiation inside composable hierarchies. |

### 2.3 Persistence & Local Database

| Component / Artifact | Version Catalog Ref | Exact Version | Clinical Usage & Purpose |
|---|---|---|---|
| **Room Runtime** | `androidx-room-runtime` | `2.7.0` | Local SQLite ORM managing patients, consults, rubrics, and prescriptions. |
| **Room KTX** | `androidx-room-ktx` | `2.7.0` | Kotlin Coroutines Flow query streams and transaction extensions (`@Transaction`). |
| **Room Compiler** | `androidx-room-compiler` | `2.7.0` | Compile-time SQL query syntax and type validation. |
| **Google KSP** | `com.google.devtools.ksp` | `2.3.5` | Fast Kotlin Symbol Processing replacing legacy KAPT for Room code generation. |

### 2.4 Asynchronous Processing & Reactive Streams

| Component / Artifact | Version Catalog Ref | Exact Version | Clinical Usage & Purpose |
|---|---|---|---|
| **Coroutines Core** | `kotlinx-coroutines-core` | `1.10.2` | Structured concurrency, `Flow`, `StateFlow`, `SharedFlow`, and channel primitives. |
| **Coroutines Android**| `kotlinx-coroutines-android`| `1.10.2` | Android `Dispatchers.Main` and UI thread dispatchers. |
| **Coroutines Test** | `kotlinx-coroutines-test` | `1.10.2` | Deterministic `StandardTestDispatcher` and `runTest` for unit tests. |

### 2.5 Networking, Serialization & Secrets

| Component / Artifact | Version Catalog Ref | Exact Version | Clinical Usage & Purpose |
|---|---|---|---|
| **OkHttp Client** | `okhttp` | `4.10.0` | Connection pooling, HTTP/2 streaming, and TLS 1.3 socket management for Cloud AI. |
| **Logging Interceptor** | `logging-interceptor` | `4.10.0` | Debug network payload logging (disabled in release builds). |
| **Moshi Kotlin** | `moshi-kotlin` | `1.15.2` | High-speed reflection/codegen JSON parser for Gemini schema responses. |
| **Secrets Gradle Plugin**| `secretsGradlePlugin` | `2.0.1` | Safely injects `.env` configuration keys (`GEMINI_API_KEY`) into `BuildConfig`. |

### 2.6 Quality Assurance & Automated Testing

| Component / Artifact | Version Catalog Ref | Exact Version | Clinical Usage & Purpose |
|---|---|---|---|
| **JUnit** | `junit` | `4.13.2` | Standard JVM test execution harness. |
| **Robolectric** | `robolectric` | `4.16.1` | Shadow Android runtime for local JVM testing of SpeechRecognizer and UI without emulator. |
| **AndroidX Test Core** | `androidx-core` | `1.6.1` | Application context provider for local tests. |
| **AndroidX Test Runner**| `androidx-runner` | `1.6.2` | Instrumentation test runner harness. |

---

## 3. Platform Configuration & Optimization

### 3.1 Android Build Configuration (`app/build.gradle.kts`)
- **Application ID**: `com.aistudio.similimumai.kpmxrq` (Unique Play-compliant namespace).
- **Compilation Target**: Java 11 bytecode compatibility.
- **R8 / ProGuard Optimization**:
  ```proguard
  # Keep Room DAOs and Entities
  -keep class * extends androidx.room.RoomDatabase
  -keep @androidx.room.Entity class *
  -dontwarn androidx.room.paging.**

  # Keep Moshi JSON data models
  -keepattributes *Annotation*, Signature, InnerClasses, EnclosingMethod
  -keep @com.squareup.moshi.JsonQualifier interface *
  -keepclassmembers class * {
      @com.squareup.moshi.Json *;
  }
  ```

### 3.2 Hardware & System Constraints

- **Minimum RAM**: 2.0 GB (Recommended: 4.0 GB+ for running ambient speech recognition alongside Jetpack Compose 60fps rendering).
- **Storage Footprint**: < 25 MB initial APK footprint; local SQLite database scales at ~1.5 KB per consultation.
- **Battery Optimization**: Audio speech processing uses hardware-accelerated acoustic DSP where available; audio capture pauses immediately when consultation is placed in background or paused.

---

## 4. Key Architectural Assumptions

- **[ASSUMPTION-ENG-01]** Platform choice: Native Android using Kotlin and Jetpack Compose (Material 3). Native Android provides direct low-level hardware access to on-device SpeechRecognizer, zero-latency audio buffering, offline Room database encryption, and full adherence to Android OS background task management.
- **[ASSUMPTION-TECH-01]** All dependency versions specified in `/gradle/libs.versions.toml` are tested as an integrated, compatible manifest and must remain version-locked to avoid binary incompatibility across the Compose/Kotlin/KSP compiler toolchain.

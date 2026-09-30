# Similimum AI — CI/CD Pipeline & Build Automation
`Location: /docs/operations/cicd-pipeline.md`

---

## 1. CI/CD Architecture Overview

Similimum AI employs an automated, deterministic Continuous Integration and Continuous Deployment (CI/CD) workflow tailored for native Android with Kotlin and Jetpack Compose. Given the safety-critical nature of clinical homeopathic decision support, every pull request and release build undergoes strict static analysis, compile verification, and local JVM clinical rule testing before packaging.

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                          SIMILIMUM AI CI/CD PIPELINE                        │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                             │
│  [Developer Commit / PR]                                                    │
│         │                                                                   │
│         ▼                                                                   │
│  ┌───────────────────────────────────────────────────────────────────────┐  │
│  │ 1. Static Verification & Linting                                      │  │
│  │    - Gradle Kotlin DSL check                                          │  │
│  │    - Android Lint (Safety, Accessibility, Resource Hygiene)           │  │
│  │    - Detekt / Ktlint formatting checks                                │  │
│  └──────────────────────────────────┬────────────────────────────────────┘  │
│                                     │                                       │
│                                     ▼                                       │
│  ┌───────────────────────────────────────────────────────────────────────┐  │
│  │ 2. Automated Test Execution (< 60s)                                   │  │
│  │    - `gradle :app:testDebugUnitTest`                                  │  │
│  │    - 100% Red-Flag Triage Verification                                │  │
│  │    - Kentian & Boenninghausen Repertorization Accuracy Tests          │  │
│  │    - Inimical Drug Relationship Blocking Verification                 │  │
│  │    - LM Potency & Dilution Mathematics Validation                     │  │
│  │    - Robolectric UI Component & ViewModel Tests                       │  │
│  └──────────────────────────────────┬────────────────────────────────────┘  │
│                                     │                                       │
│                                     ▼                                       │
│  ┌───────────────────────────────────────────────────────────────────────┐  │
│  │ 3. Artifact Compilation & Shrinking                                   │  │
│  │    - `gradle :app:bundleRelease` (AAB) & `assembleRelease` (APK)      │  │
│  │    - R8 Code & Resource Shrinking / ProGuard Rules Enforcement        │  │
│  │    - Dependency Locking verification from `libs.versions.toml`        │  │
│  └──────────────────────────────────┬────────────────────────────────────┘  │
│                                     │                                       │
│                                     ▼                                       │
│  ┌───────────────────────────────────────────────────────────────────────┐  │
│  │ 4. Cryptographic Signing & Distribution                               │  │
│  │    - Release Keystore Signing via v2/v3 Android Signing Scheme         │  │
│  │    - Fastlane deployment to Google Play Internal App Sharing / Track   │  │
│  │    - GitHub Release Tagging & Signed Checksum (SHA-256) Generation    │  │
│  └───────────────────────────────────────────────────────────────────────┘  │
│                                                                             │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## 2. GitHub Actions CI/CD Workflow (`.github/workflows/android-build.yml`)

```yaml
name: Similimum AI Build & Verification Pipeline

on:
  push:
    branches: [ main, develop ]
    tags: [ 'v*.*.*' ]
  pull_request:
    branches: [ main ]

concurrency:
  group: ${{ github.workflow }}-${{ github.ref }}
  cancel-in-progress: true

jobs:
  validate-and-test:
    name: Lint, Unit Test & Clinical Validation
    runs-on: ubuntu-latest
    timeout-minutes: 15

    steps:
      - name: Checkout Code Repository
        uses: actions/checkout@v4
        with:
          fetch-depth: 0

      - name: Set up JDK 17 (Temurin)
        uses: actions/setup-java@v4
        with:
          distribution: 'temurin'
          java-version: '17'
          cache: 'gradle'

      - name: Validate Gradle Wrapper & Dependencies
        uses: gradle/actions/setup-gradle@v3
        with:
          gradle-version: wrapper

      - name: Run Clinical Verification Unit Tests
        run: gradle :app:testDebugUnitTest --no-daemon --stacktrace

      - name: Publish Test Results
        uses: mikepenz/action-junit-report@v4
        if: always()
        with:
          report_paths: 'app/build/test-results/testDebugUnitTest/*.xml'
          check_name: 'JUnit & Clinical Rule Test Results'

  build-release:
    name: Build Signed Android App Bundle (AAB)
    needs: validate-and-test
    if: startsWith(github.ref, 'refs/tags/v')
    runs-on: ubuntu-latest
    timeout-minutes: 20

    steps:
      - name: Checkout Code Repository
        uses: actions/checkout@v4

      - name: Set up JDK 17 (Temurin)
        uses: actions/setup-java@v4
        with:
          distribution: 'temurin'
          java-version: '17'
          cache: 'gradle'

      - name: Configure Production Environment Variables
        env:
          GEMINI_API_KEY: ${{ secrets.PROD_GEMINI_API_KEY }}
        run: |
          echo "GEMINI_API_KEY=$GEMINI_API_KEY" > .env

      - name: Decode Android Release Keystore
        env:
          RELEASE_KEYSTORE_BASE64: ${{ secrets.ANDROID_RELEASE_KEYSTORE_BASE64 }}
        run: |
          echo "$RELEASE_KEYSTORE_BASE64" | base64 --decode > app/release.keystore

      - name: Build Release Android App Bundle (AAB)
        env:
          SIGNING_KEY_ALIAS: ${{ secrets.SIGNING_KEY_ALIAS }}
          SIGNING_KEY_PASSWORD: ${{ secrets.SIGNING_KEY_PASSWORD }}
          SIGNING_STORE_PASSWORD: ${{ secrets.SIGNING_STORE_PASSWORD }}
        run: |
          gradle :app:bundleRelease --no-daemon -Pandroid.injected.signing.store.file=$(pwd)/app/release.keystore \
            -Pandroid.injected.signing.store.password=$SIGNING_STORE_PASSWORD \
            -Pandroid.injected.signing.key.alias=$SIGNING_KEY_ALIAS \
            -Pandroid.injected.signing.key.password=$SIGNING_KEY_PASSWORD

      - name: Upload Release AAB Artifact
        uses: actions/upload-artifact@v4
        with:
          name: similimum-release-bundle
          path: app/build/outputs/bundle/release/*.aab
          retention-days: 14
```

---

## 3. R8 Code Shrinking & ProGuard Configuration (`app/proguard-rules.pro`)

To minimize APK download size (< 12MB target), optimize cold startup times, and prevent runtime reflection issues across Room and JSON parsing:

```proguard
# Similimum AI R8 / ProGuard Optimization Rules

# 1. Jetpack Compose
-keepclassmembers class * extends androidx.compose.runtime.State { *; }
-dontwarn androidx.compose.**

# 2. Room Database & SQLite
-keep class * extends androidx.room.RoomDatabase
-dontwarn androidx.room.paging.**
-keep @androidx.room.Entity class * { *; }
-keep @androidx.room.Dao interface * { *; }

# 3. Kotlinx Coroutines & Serialization
-keepattributes *Annotation*, InnerClasses, Signature
-keepclassmembers class kotlinx.coroutines.internal.MainDispatcherFactory {
    java.lang.String getLoadPriority();
    kotlinx.coroutines.MainCoroutineDispatcher createDispatcher(java.util.List);
}

# 4. Moshi / Retrofit Models for Gemini 2.5 Flash API
-keepattributes Signature
-keep @com.squareup.moshi.JsonClass class * { *; }
-dontwarn okio.**
-dontwarn retrofit2.**

# 5. Clinical Knowledge Engine Data Classes (Immunity from Obfuscation)
-keep class com.example.model.** { *; }
-keep class com.example.engine.** { *; }
```

---

## 4. Release Channel Staging Matrix

| Track | Target Audience | Trigger Mechanism | Retention / Gate |
|---|---|---|---|
| **Internal Testing** | Core engineering & clinical advisors (5 doctors) | Auto on every tagged git push (`v*`) | 24-hour soak; 0 clinical crashes |
| **Closed Beta (AYUSH Council)** | 25 pilot BHMS/MD-Hom clinicians in India | Manual promotion from Internal | 7-day trial; 100+ simulated & live cases |
| **Open Production** | Registered practitioners via Google Play Store | Staged rollout (10% -> 25% -> 50% -> 100%) | Crash-free sessions > 99.8% |

`[ASSUMPTION-OPS-01]` Build pipeline executes automated unit tests on local JVMs in under 60 seconds without requiring an Android emulator.

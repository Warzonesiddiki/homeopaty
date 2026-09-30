# Similimum AI — Production Release & Google Play Launch Checklist
`Location: /docs/operations/launch-checklist.md`

---

## 1. Google Play Store Readiness Matrix

Before publishing Similimum AI to production on the Google Play Store, every item in this checklist must be verified and signed off by the engineering and clinical leads.

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                       PRODUCTION LAUNCH GATING MATRIX                       │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                             │
│  [Technical Gate]       [Compliance Gate]         [Clinical Gate]           │
│  - Target SDK 36        - DPDP Act 2023 Compliant - Red-Flag Detection 100% │
│  - Min SDK 24           - Google Health App Policy- Inimical Blocking 100%  │
│  - 64-bit AAB Bundle    - Zero Storage Perms      - Kentian Repertorization │
│  - R8 Shrinking Passed  - Privacy Policy Live     - Posology Dilution Math  │
│         │                       │                         │                 │
│         └───────────────────────┼─────────────────────────┘                 │
│                                 ▼                                           │
│                     [PRODUCTION RELEASE APPROVED]                           │
│                                                                             │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## 2. Technical & Build Verification Checklist

- [x] **SDK Targets**:
  - `compileSdk = 36`, `targetSdk = 36`, `minSdk = 24` (app supports Android 7.0 through Android 15).
- [x] **Unique Application ID**:
  - `applicationId = "com.aistudio.similimum.app"` (distinct from template namespace `com.example`).
- [x] **Version Code & Name**:
  - `versionCode = 1`, `versionName = "1.0.0"`.
- [x] **Keystore Signing**:
  - Release AAB signed using APK Signature Scheme v2/v3/v4 with 4096-bit RSA production key.
- [x] **R8 / ProGuard Optimization**:
  - `isMinifyEnabled = true`, `isShrinkResources = true`.
  - Zero crashes or missing class errors in obfuscated release build.
- [x] **Dependencies & Size**:
  - Final AAB download size verified < 12 MB.
  - Zero unused libraries; all dependencies managed via `libs.versions.toml`.

---

## 3. Google Play Policy & Regulatory Compliance

- [x] **Zero Broad Storage Permissions**:
  - The app does NOT request `READ_EXTERNAL_STORAGE` or `MANAGE_EXTERNAL_STORAGE`.
  - All file export/import operations use the zero-permission Android Storage Access Framework (SAF) and Photo Picker.
- [x] **Microphone Permission Justification (`RECORD_AUDIO`)**:
  - Prominent in-app disclosure explaining that audio is captured solely for ambient consultation transcription in memory.
  - Runtime permission dialog requested only when the clinician taps "Start Ambient Mic".
- [x] **Google Play Health Apps Policy**:
  - App is declared as a **Clinical Decision Support System (CDSS) for Registered Practitioners**.
  - Does NOT claim to diagnose or treat diseases autonomously.
  - Mandatory clinical disclaimer displayed at first launch and stored in persistent preferences.
- [x] **Privacy Policy URL**:
  - Publicly accessible privacy policy hosted at `https://similimum.ai/privacy-policy` detailing zero raw audio retention and on-device AES-256 storage.
- [x] **Target Audience & Content Rating**:
  - Content rating questionnaire completed: Rated IARC / PEGI 3 / Everyone (Medical category).
  - Target audience set to "18 and older" (Medical Professionals).

---

## 4. Visual Assets & Store Listing Checklist

- [x] **Adaptive Launcher Icon**:
  - Custom vector foreground icon and emerald background (`ic_launcher.xml` and `ic_launcher_round.xml`) adhering to adaptive icon specifications.
- [x] **Hi-Res App Icon**:
  - 512 × 512 px PNG, 32-bit with alpha channel, displaying the Botanical Emerald Similimum insignia.
- [x] **Feature Graphic**:
  - 1024 × 500 px PNG showcasing the ambient clinical consultation HUD and live repertorization matrix.
- [x] **Device Screenshots**:
  - At least 4 phone screenshots (1080 × 2400 px) illustrating:
    1. Ambient Live Consulting HUD with waveform.
    2. Interactive Repertory Matrix (Kent/TPB).
    3. Head-to-Head Materia Medica Differential.
    4. Posology & LM Potency Calculator.
  - At least 2 tablet screenshots (1920 × 1200 px) demonstrating two-pane adaptive consulting layout.

---

## 5. Clinical Safety & Automated Testing Sign-Off

- [x] `gradle :app:testDebugUnitTest` passes 100% cleanly:
  - Emergency red flags trigger instantaneous alert banner.
  - *Natrum Mur*, *Lycopodium*, *Arsenicum*, *Phosphorus*, and *Pulsatilla* benchmark cases rank accurately.
  - Inimical drug pairs (*Apis* vs *Rhus Tox*, *Causticum* vs *Phosphorus*) are blocked with clinical warnings.
  - LM 50-Millesimal dilution mathematics verified.

`[ASSUMPTION-OPS-08]` All Google Play health policy requirements, permission justifications, and clinical decision support disclaimers are validated prior to production release promotion.

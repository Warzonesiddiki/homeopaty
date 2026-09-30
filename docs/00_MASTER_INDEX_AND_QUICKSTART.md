# Similimum AI — Homeopathic Clinical Co-Pilot
## 00. Master Index & Quickstart Guide

---

### Executive Summary
**Similimum AI** is an ambient, real-time clinical AI co-pilot engineered specifically for classical and clinical Homeopathic practitioners. During live consultations, the application quietly listens to the patient-doctor dialogue (or processes clinical input), dynamically extracts symptoms structured by **Boenninghausen’s LSMC** (Location, Sensation, Modality, Concomitant) and **Hahnemannian Totality** (Organon §83–§104, §153 PQRS), suggests the highest-yield homeopathic follow-up questions to uncover the deep constitutional layer, translates colloquial speech (English, Hindi, Hinglish) into canonical rubrics (Kent, Boericke, TPB), runs live weighted repertorization, screens for allopathic red flags, and evaluates case progression according to **Hering's Law of Cure**.

---

### Document Suite Index

| Doc # | Document Title | Purpose |
|---|---|---|
| **00** | [Master Index & Quickstart](00_MASTER_INDEX_AND_QUICKSTART.md) | High-level system overview, setup, and navigation guide. |
| **01** | [Product Requirements Document (PRD)](01_PRD_PRODUCT_REQUIREMENTS.md) | Functional requirements, clinical personas, user stories, and zero-compromise criteria. |
| **02** | [Clinical Homeopathy AI Engine Spec](02_CLINICAL_HOMEOPATHY_AI_ENGINE_SPEC.md) | Organon compliance, LSMC parser, Kent/Boericke rubrics, Miasmatic diagnosis, Inimical drug relationships, and Hering's Law logic. |
| **03** | [Technical Architecture](03_TECHNICAL_ARCHITECTURE_AISTUDIO.md) | Jetpack Compose M3, Coroutines, StateFlow, Room local DB, SpeechRecognizer, and Gemini 2.5 Flash hybrid integration. |
| **04** | [UI/UX Design System](04_UI_UX_DESIGN_SYSTEM.md) | Botanical Clinical Emerald design tokens, typography, HUD ergonomics, silent observation chips, and accessibility. |
| **05** | [Roadmap & Execution Plan](05_ROADMAP_AND_EXECUTION_PLAN.md) | Milestones, phased implementation from offline engine to live ambient capture, and validation. |
| **06** | [Master Prompts for AI Reasoning](06_MASTER_PROMPTS_FOR_GOOGLE_AISTUDIO.md) | Production system instructions, JSON schemas, few-shot clinical homeopathic case transcripts. |
| **Blueprint**| [Complete Master Blueprint](SIMILIMUM_AI_COMPLETE_MASTER_BLUEPRINT.md) | Complete end-to-end specification combining clinical theory, engineering design, and operational guardrails. |

---

### Key Architectural Highlights
1. **Zero Distraction Clinical HUD**: Designed for the consulting desk. Doctors maintain continuous empathetic eye contact with patients while peripheral glance cues highlight missing modalities and high-yield questions.
2. **Dual Intelligence Engine**:
   - *Local Deterministic Engine*: Responds in <10ms without internet. Loaded with 30+ classical polychrests, 150+ canonical rubrics, LSMC gap analysis, red-flag screening, and inimical safety validation.
   - *Cloud Gemini 2.5 Flash Reasoning Engine*: Delivers deep constitutional synthesis, complex miasmatic deconvolution, and subtle miasmatic/organopathic analysis when an API key is present.
3. **Dual Audio Input**:
   - *Real Device Mode*: Continuous Android `SpeechRecognizer` loop with live speech amplitude waveform animation.
   - *Clinical Simulator Mode*: 5 pre-configured, multi-turn clinical cases (Nat-m Chronic Migraine, Lyc Chronic GERD, Ars-alb Acute Gastroenteritis, Phos Post-Viral Cough, Puls Pediatric Otitis Media) with automatic turn streaming and custom utterance entry.
4. **Safety & Ethics First**:
   - Immediate red-flag detection (Acute Coronary Syndrome, Meningitis, Stroke, Cauda Equina) with prominent clinical urgency warnings.
   - Inimical drug interaction blocking (e.g., *Apis* vs *Rhus Tox*, *Causticum* vs *Phosphorus*).
   - Strict adherence to Hering’s Law of Cure and Kent's 12 Prognostic Observations for follow-up evaluation.

---

### Quickstart Guide for Developers & Clinicians

#### 1. Requirements
- Android SDK 24 (Android 7.0) minimum, Target SDK 36.
- Kotlin 2.0+ with Jetpack Compose (Material 3).
- Optional: Gemini API Key configured in the app's Settings panel or `.env` (`GEMINI_API_KEY`).

#### 2. Running the App
1. Build and install the APK via Android Studio or AI Studio preview container.
2. Grant the `RECORD_AUDIO` permission when prompted on launch.
3. Launch live speech recognition with the **"START AMBIENT MIC"** button, or test simulated clinical flows using the **"▶ STREAM SIMULATED PATIENT"** control.
4. Observe real-time symptom extraction, LSMC completeness updates, dynamic Repertory ranking, and prescription formulation.

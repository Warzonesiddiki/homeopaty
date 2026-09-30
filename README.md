# Similimum AI — Homeopathic Clinical Co-Pilot

Similimum AI is an ambient clinical homeopathy co-pilot designed for classical and clinical Homeopathic practitioners running on Android with Jetpack Compose.

## Key Features

1. **Ambient Consulting Co-Pilot HUD**:
   - Continuous audio capture with live RMS amplitude waveform visualizer.
   - Clinical simulation player with 5 multi-turn clinical cases (Nat-m, Lyc, Ars-alb, Phos, Puls).
   - Real-time verbatim transcript streaming and automatic §83 Hahnemannian symptom extraction.
   - Ask-Next high-yield question recommendation engine to complete missing modalities and identify constitutional etiologies.
   - Doctor's 1-tap silent physical observation chips.
   - Immediate red-flag emergency allopathic screening (ACS, stroke, meningitis, cauda equina).

2. **Boenninghausen LSMC Radar & Totality Gauge**:
   - Location, Sensation, Modality (< / >), and Concomitant completeness score.
   - 12-Pillar Constitutional Assessment (Thermal state, Miasm, Cravings, Sleep, Perspiration).
   - Organon §83–§104 case totality quality rating.

3. **Dynamic Repertory Matrix**:
   - Canonical rubrics database mapped to classical polychrests.
   - Multi-school repertorization engine (Kent Hierarchical, Boenninghausen TPB, Boger).
   - Thermal elimination filters.
   - Live ranked candidate similimums with score breakdowns.

4. **Classical Materia Medica & Drug Safety**:
   - Comprehensive polychrest monographs with §153 PQRS keynotes.
   - Side-by-side comparative polychrest analyzer.
   - Inimical & incompatible drug safety matrix (preventing harmful sequential prescriptions like Apis vs Rhus Tox, Causticum vs Phosphorus, Silicea vs Mercurius).

5. **Rx Pad & Hering's Law Follow-Up**:
   - Hahnemannian prescription pad with centesimal and LM/50-millesimal scales.
   - Strict antidote and dietary restriction guidance.
   - 4-Vector Hering's Law of Cure evaluator (direction of cure analysis).
   - Local database persistence via Android Room SQLite database for saving and reviewing patient consultations.

6. **Gemini 2.5 Flash Cloud AI Integration**:
   - Deep constitutional synthesis and miasmatic deconvolution when API key is configured.
   - 100% functional offline fallback powered by local deterministic homeopathy knowledge engine.

7. **Follow-Up & Prognosis Toolkit**:
   - LM 50-Millesimal (LM1–LM30) dilution protocol calculator (2nd Cup Method, succussion count, split dosing).
   - Kent's 12 Prognostic Observations evaluator with actionable clinical next steps (Sac Lac / Repeat / Increase / Antidote / Change).
   - Hering's Law 4-vector directional cure tracker.
   - One-tap plain-text Case Sheet + prescription clipboard export.

## Build & Test

Requires JDK 21, Android SDK 36 (compileSdk) — build via Android Studio or Gradle 8.7+/9.x:

```bash
# Debug APK
gradle :app:assembleDebug

# Tier 1 local JVM clinical invariant tests (docs/engineering/testing-qa-strategy.md):
# red-flag triage, Kent/TPB repertorization ranking, inimical safety blockers,
# LM dilution mathematics, Kent's 12 observations, knowledge-base size guarantees.
gradle :app:testDebugUnitTest
```

The knowledge base ships with **30 classical polychrests** and **184 canonical rubrics**
across Kentian chapters (docs/data/seed-data.md), fully offline.

The optional Gemini cloud reasoning layer is enabled by setting `GEMINI_API_KEY`
in `.env` (see `.env.example`); without a key the deterministic local engine
provides 100% of the clinical functionality.

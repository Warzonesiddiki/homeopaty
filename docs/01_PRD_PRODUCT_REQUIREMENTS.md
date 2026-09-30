# Similimum AI — Homeopathic Clinical Co-Pilot
## 01. Product Requirements Document (PRD)

---

### 1. Product Vision & Mission
**Vision**: To deliver the world's most sophisticated and respectful ambient AI clinical diagnostic companion for Homeopathic Doctors, preserving the sacred doctor-patient connection while automating repertorization, case totality compilation, and differential analysis.

**Mission**: Empower homeopaths to achieve consistent clinical cures by:
1. Eliminating consultation note-taking friction so the physician remains 100% focused on empathetic observation.
2. Uncovering the true *Simillimum* through real-time LSMC gap detection and prompt suggestions.
3. Enforcing Hahnemannian principles (§83–§104 Organon) and preventing dangerous posology errors.

---

### 2. The Four Pillars of Zero Compromise

| Pillar | Principle | Clinical Implementation |
|---|---|---|
| **1. Doctor-Patient Bond** | No intrusive screens or robotic interruptions. | Silent HUD, high-legibility ambient cues, peripheral question chips, and non-disruptive audio capture. |
| **2. Homeopathic Purity** | Individualized totality over disease labels. | Avoids conventional diagnostic groupings; groups by constitutional modalities, sensations, causations, and PQRS. |
| **3. Absolute Data Privacy** | Sensitive emotional traumas remain confidential. | Local-first architecture; all core logic runs fully on-device without internet; cloud Gemini reasoning is opt-in. |
| **4. Medical-Grade Precision** | No hallucinations or pseudo-science. | Deterministic cross-referencing against standard classical literature (Kent, Boericke, Boger, Boenninghausen). |

---

### 3. User Personas & Clinical Scenarios

#### Persona A: Dr. Anand Mehta, Senior Classical Homeopath
- **Setting**: Busy private clinic in Mumbai, seeing 25–35 patients daily.
- **Pain Point**: Spends 30 minutes per patient handwriting case notes and cross-referencing synthetic repertories; fatigue leads to missing subtle thermal and side-affinity modalities.
- **Needs**: Ambient listener that captures vernacular Hindi/English statements (e.g., "khana khate hi pet phool jata hai") and converts them into Boericke/Kent rubrics instantly.

#### Persona B: Dr. Sarah Jenkins, Integrative & Pediatric Homeopath
- **Setting**: Holistic health center in London.
- **Pain Point**: Differentiating between closely matched polychrests (e.g., Pulsatilla vs Chamomilla in pediatric otitis).
- **Needs**: Instant head-to-head Materia Medica differentials with Keynote verifications and Hering’s Law tracking during follow-ups.

---

### 4. Functional Requirements

#### 4.1 Ambient Audio & Consultation Capture
- **FR-1.1**: Real device audio recording via Android `SpeechRecognizer` with continuous loop restart and partial result streaming.
- **FR-1.2**: Live waveform visualizer reflecting microphone amplitude.
- **FR-1.3**: Integrated Clinical Simulator providing 5 realistic clinical cases for testing, demonstrations, and offline verification.
- **FR-1.4**: Manual multi-lingual text/speech utterance entry supporting colloquial English and Hinglish expressions.

#### 4.2 Homeopathic Totality & LSMC Gap Analysis
- **FR-2.1**: Boenninghausen LSMC Breakdown: Automatically categorize incoming complaints into **Location**, **Sensation**, **Modalities (< agg / > amel)**, and **Concomitants**.
- **FR-2.2**: 12-Pillar Constitutional Coverage Radar: Track coverage of Causation, Mind, Generals, Thermals, Thirst, Cravings, Aversions, Sleep, Dreams, Miasm, Past History, and Physical Particulars.
- **FR-2.3**: Ask-Next Question Generator: Compute missing elements in real-time and provide the physician with the top 3 highest-yield clinical clarifying questions.

#### 4.3 Multi-School Repertorization Matrix
- **FR-3.1**: Support for Kent's Hierarchical Method, Boenninghausen's TPB, and Boger-Boenninghausen approaches.
- **FR-3.2**: Dynamic rubric grading (Grade 1, 2, 3), weight adjustments (×1, ×2, ×3), and rubric elimination toggles.
- **FR-3.3**: Dynamic confidence scoring and real-time remedy rank re-sorting in <10ms.

#### 4.4 Differential Materia Medica & Keynotes
- **FR-4.1**: Side-by-side comparison of top 3 ranked remedies detailing Ailments From, Mental Generals, Physical Generals, Modalities, and Miasmatic dominance.
- **FR-4.2**: 1-Tap Keynote Verification Checklist for clinical confirmation.

#### 4.5 Safety, Red Flags & Drug Relationships
- **FR-5.1**: Instant triage of allopathic life-threatening emergencies (Cardiac, Stroke, Meningeal, Respiratory, Acute Abdomen).
- **FR-5.2**: Automatic validation of drug relationships (Inimical, Complementary, Follows-Well, Antidote). Explicit warning preventing prescription of incompatible remedies.

#### 4.6 Case Sheet, Prescription & Hering's Law Follow-Up
- **FR-6.1**: One-tap comprehensive case sheet generation with clinical summary, symptom totality, and full rubric mapping.
- **FR-6.2**: Prescription builder supporting Centesimal (30C, 200C, 1M, 10M), 50-Millesimal (LM/Q), and Mother Tinctures with vehicle and posology instructions.
- **FR-6.3**: Hering's Law follow-up evaluator assessing symptom direction of cure (Inside Out, Above Downwards, Most Vital to Least Vital, Reverse Order of Appearance).

---

### 5. Non-Functional Requirements
- **NFR-1**: Latency: On-device repertorization and symptom classification must complete in <15ms.
- **NFR-2**: Zero Internet Dependency: The entire clinical engine must function completely offline without network connectivity.
- **NFR-3**: Accessibility: Meets WCAG AAA contrast guidelines with minimum touch target sizes of 48dp × 48dp.

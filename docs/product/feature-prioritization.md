# Similimum AI — Feature Prioritization (MoSCoW Matrix)
`Location: /docs/product/feature-prioritization.md`

---

## 1. Prioritization Framework Overview

Features are ranked using the **MoSCoW** methodology (Must have, Should have, Could have, Won't have for v1) calibrated against the "Zero-Compromise on Homeopathic Purity and Doctor-Patient Connection" directive.

---

## 2. MoSCoW Feature Categorization

### 1. MUST HAVE (P0 — Critical for Launch)
- **F-P0-01: Continuous Speech Capture & Visualizer**: Ambient Android `SpeechRecognizer` loop with live RMS audio waveform indicator.
- **F-P0-02: Vernacular to Canonical Rubric Translation**: Real-time conversion of colloquial Hindi/English expressions (e.g., *"sar mein phatne jaisa dard"*) into standard Kent/Boericke rubrics.
- **F-P0-03: Boenninghausen LSMC Symptom Completeness Analyzer**: Decomposition into Location, Sensation, Modality, Concomitant with missing-gap flags.
- **F-P0-04: High-Yield Ask-Next Question Generator**: Top 3 clinically prioritized non-leading inquiries to uncover missing modalities and causations.
- **F-P0-05: Real-Time Multi-School Weighted Repertorization**: <15ms repertory calculation supporting Kent Hierarchy and Boenninghausen TPB weighting.
- **F-P0-06: Allopathic Red-Flag Emergency Triage**: Automatic screening for acute coronary syndrome, stroke, acute abdomen, and meningitis.
- **F-P0-07: Inimical & Incompatible Drug Safety Blocker**: Explicit warning preventing concurrent or immediate prescription of inimical pairs (e.g., *Apis* vs *Rhus Tox*).
- **F-P0-08: Prescription Builder & LM Dilution Calculator**: Potency selection (30C, 200C, 1M, LM1) and step-by-step dilution instructions.
- **F-P0-09: Offline-First Operation**: 100% of core repertorization, remedy lookup, and local case storage working without internet.
- **F-P0-10: Decision-Support Legal Safeguards**: Persistent disclaimers and physician verification flows on all screens.

### 2. SHOULD HAVE (P1 — Target for v1.1)
- **F-P1-01: 5 Multi-Turn Clinical Simulation Engine**: Pre-loaded complete consultation cases for doctor training, testing, and cloud emulator use.
- **F-P1-02: Head-to-Head Materia Medica Differential Screen**: Side-by-side comparison of top 3 remedies with keynote verification checklists.
- **F-P1-03: Hering’s Law & Kent’s 12 Observations Follow-Up Evaluator**: Directional cure and prognostic tracking across patient visits.
- **F-P1-04: Physical Tongue & Objective Sign Analyzer**: Mapped tongue, strawberry tongue, and trembling tongue sign correlation.
- **F-P1-05: Case Sheet Export & Sharing**: Formatted plain text and clipboard export of complete consultation totality.

### 3. COULD HAVE (P2 — Planned for v2.0)
- **F-P2-01: Cloud Encrypted Backup & Sync**: End-to-end encrypted backup of patient records to Indian data residency servers.
- **F-P2-02: Print-Ready PDF Case Sheet Generation**: Branded clinic letterhead PDF export with doctor registration details.
- **F-P2-03: Custom Clinic Rubric & Materia Medica Additions**: Doctor ability to save personal clinical pearls and empirical rubrics.
- **F-P2-04: Voice Note Summarization for Past Physical Files**: Summarizing historical paper case files using camera OCR and audio summary.

### 4. WON'T HAVE (P3 — Strictly Deferred to v3.0+)
- **F-P3-01: Direct Patient-Facing Mobile App**: Patient portal for remote consultations.
- **F-P3-02: Integrated E-Commerce Pharmacy Dispensing**: Direct medicine courier ordering.
- **F-P3-03: Multi-Doctor Clinic Chain Administration**: Enterprise ERP billing and multi-branch management.

`[ASSUMPTION-MOSCOW-01]` Solo developer capacity is focused entirely on P0 and P1 items to achieve a production-ready, medical-grade v1.0 release.

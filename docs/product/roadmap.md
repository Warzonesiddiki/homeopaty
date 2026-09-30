# Similimum AI — Product Roadmap (v1.0 MVP to v3.0 Scale)
`Location: /docs/product/roadmap.md`

---

## 1. Product Evolution Phases

```
┌────────────────────────────────────────────────────────────────────────┐
│                        SIMILIMUM AI ROADMAP                            │
├────────────────────────────────────────────────────────────────────────┤
│  v1.0 (MVP)            v2.0 (Clinical Scale)   v3.0 (Ecosystem)        │
│  - Ambient Native Mic  - Encrypted Cloud Sync  - Multi-Doctor Clinics  │
│  - Offline Engine      - PDF Case Reports      - AYUSH Hospital EHR    │
│  - Boenninghausen LSMC - Expand 100+ Remedies  - Regional Languages    │
│  - Kent / TPB Matrix   - Custom Clinic Rubrics - Hardware Mic Array    │
│  - LM Potency Calc     - In-App Google Billing - Research Clinical Trial│
└────────────────────────────────────────────────────────────────────────┘
```

---

## 2. Phase 1: MVP Release (v1.0 — Current)

- **Target Audience**: Individual homeopathic practitioners and medical interns in India.
- **Core Deliverables**:
  - Single-Activity Jetpack Compose Material 3 Native Android app.
  - Continuous on-device Android `SpeechRecognizer` (`en-IN`, `hi-IN`) with live RMS audio waveform visualizer.
  - 5 rich multi-turn consultation simulations (*Natrum Mur*, *Lycopodium*, *Arsenicum*, *Phosphorus*, *Pulsatilla*).
  - Deterministic `HomeopathyKnowledgeEngine` with 30+ classical polychrests, 150+ rubrics, and tongue diagnostics.
  - Multi-School Repertorization (Kent Hierarchical, Boenninghausen TPB).
  - High-yield non-leading Ask-Next question deck based on LSMC gap detection.
  - Inimical drug relationship checker (*Apis* vs *Rhus Tox*, *Causticum* vs *Phosphorus*).
  - Allopathic red-flag emergency screening banner.
  - LM 50-Millesimal potency dilution protocol calculator.
  - Hering’s Law of Cure follow-up evaluator and Kent's 12 Prognostic Observations.
  - Formatted plain-text case sheet and prescription clipboard generator.
  - Direct Gemini 2.5 Flash hybrid API fallback.

---

## 3. Phase 2: Enhanced Clinical Practice (v2.0 — Months 3 to 6)

- **Encrypted Cloud Backup & Sync**: End-to-end encrypted backup to Indian data residency servers complying with the DPDP Act 2023.
- **Materia Medica Expansion**: Ingest complete Boericke and Allen’s Keynotes (120+ remedies, 1,000+ rubrics).
- **Print-Ready PDF Case Sheet**: Export beautiful, branded A4 clinic case records featuring doctor's registration number, clinic logo, and patient instructions.
- **Custom Doctor Clinic Rubrics**: Allow practitioners to save clinical observations and empirical keynotes to their personal private repository.
- **Google Play Billing Integration**: Commercial launch of Pro subscription tier (₹799/month).
- **Additional Regional Indian Languages**: Speech parsing support for Marathi, Bengali, and Gujarati.

---

## 4. Phase 3: Connected Clinical Ecosystem (v3.0 — Months 7 to 12)

- **Multi-Room Clinic & Receptionist Portal**: Assistant tablet for queuing patient vitals, thermal baseline, and chief complaint prior to the doctor's consultation.
- **AYUSH Teaching Hospital & Academic Edition**: Anonymized case totality review tool for homeopathic medical college faculties.
- **Longitudinal Clinical Research Module**: Aggregate de-identified clinical outcome data tracking remedy efficacy for homeopathic research papers.
- **Dedicated Bluetooth Consulting Desk Microphone**: Firmware optimization for external multi-directional desk microphones.

`[ASSUMPTION-ROADMAP-01]` v1.0 MVP establishes the rock-solid core clinical foundation; each subsequent release builds upon an immutable, tested architecture.

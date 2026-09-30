# Similimum AI — MVP Scope Specification (v1.0)
`Location: /docs/product/mvp-scope.md`

---

## 1. Scope Boundary Definition

The MVP (v1.0) targets single-doctor private clinics and OPD practices in India. It prioritizes core clinical decision support, zero-lag offline repertorization, and real-time speech capture while excluding complex multi-user clinic management.

---

## 2. Explicit Feature Matrix: IN vs OUT of MVP v1

| Feature Area | Explicitly IN MVP v1 | Explicitly OUT of MVP v1 (Deferred to v2/v3) |
|---|---|---|
| **Platform & OS** | Native Android (Kotlin + Jetpack Compose), minSdk 24 (Android 7.0+), phones & tablets. | iOS app, Web browser dashboard, desktop native builds. |
| **Audio & Input** | On-device Android `SpeechRecognizer` (`en-IN`, `hi-IN`), Live RMS waveform, 5 rich simulated multi-turn consultation cases, manual multilingual text bar. | Multi-speaker diarization using external cloud acoustic models; continuous multi-microphone array beamforming. |
| **Homeopathic Knowledge Base** | 30+ classical polychrest remedies with comprehensive Materia Medica, 150+ canonical rubrics (Kent & Boericke), physical tongue diagnostic signs, inimical drug relationship matrix. | Full 3,000+ rare remedy synthetic repertory library; proprietary modern non-classical proving collections. |
| **Repertorization Engine** | Instant (<15ms) weighted repertorization, Kent hierarchical weighting, Boenninghausen TPB weighting, rubric elimination filters, dynamic weight adjustments (x1, x2, x3). | Automated computerized statistical grid re-sorting based on proprietary machine-learning historical patient outcome datasets. |
| **AI Cloud Integration** | Gemini 2.5 Flash structured JSON reasoning via REST API for deep miasmatic synthesis with graceful offline fallback to local deterministic engine. | On-premise self-hosted fine-tuned open-source LLM cluster. |
| **Safety & Red Flags** | Immediate allopathic red-flag emergency screener (cardiac, stroke, meningitis, acute abdomen, anaphylaxis) with prominent clinical alert banners. | Direct automated telemetry dispatch to emergency medical services (108/911 integration). |
| **Prescription & Posology** | Centesimal (6C–10M), LM 50-Millesimal (LM1–LM30), Mother Tinctures (Q), posology rules, LM dilution instructions, antidotes & dietary prohibitions. | Integrated digital pharmacy medicine order dispatch & inventory barcode dispensing. |
| **Follow-Up & Prognosis** | Hering’s Law directional tracker, Kent's 12 Prognostic Observations assessment, follow-up comparison view. | Automated patient automated SMS/WhatsApp bot for daily self-reporting. |
| **Data Storage & Privacy** | Local Room SQLite database, encrypted on-device storage, patient record search, exportable formatted case sheet. | Multi-clinic cloud synchronization, multi-branch multi-doctor role-based administration. |
| **Billing & Payments** | Free tier with core offline features; Google Play In-App Billing for Pro tier. | Custom GST invoicing, payment gateway collection from patients for clinic consultation fees. |

---

## 3. Minimum Viable Experience (MVE) Criteria
A consultation is considered successfully completed in MVP v1 when:
1. The doctor can conduct a 15-minute consultation with ambient or simulated speech.
2. At least 4 distinct LSMC symptoms are structured into canonical rubrics.
3. The top 3 differentiated remedies update dynamically with confidence percentages.
4. The doctor can tap 1 remedy (e.g., *Nux Vomica 30C* or *Lycopodium LM1*), generate a complete Case Sheet, and copy/export it.

`[ASSUMPTION-SCOPE-01]` Multi-user receptionist/compounder roles are deferred to v2; v1 is exclusively operated by the consulting doctor.

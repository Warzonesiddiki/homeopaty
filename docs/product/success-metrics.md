# Similimum AI — Product Success Metrics & KPIs
`Location: /docs/product/success-metrics.md`

---

## 1. Product Metric Framework (AARRR)

To monitor value delivery and clinical adoption among homeopathic doctors without compromising patient privacy (no patient health data is ever transmitted to analytics), Similimum AI tracks anonymized product engagement and clinical efficiency metrics.

---

## 2. Core Key Performance Indicators (KPIs)

### 2.1 Activation & First-Day Value
- **Time to First Completed Case (TTFC)**: Target: **< 5 minutes** from initial launch. Measured when a new user tests a simulated consultation or runs their first live mic consultation and generates a draft case sheet.
- **Microphone Permission Grant Rate**: Target: **> 85%** of onboarded doctors granting `RECORD_AUDIO` on first launch after viewing the clinical privacy explanation.
- **Tutorial & Simulation Completion Rate**: Target: **> 70%** of new doctors running at least 1 full simulated case (e.g., *Natrum Mur* migraine) to experience the HUD.

### 2.2 Engagement & Consultation Efficiency
- **Daily Active Cases per Doctor (DAC)**:
  - Part-time / Visiting Doctor: 3–5 cases/day.
  - Full-time Private Clinic Doctor: 15–25 cases/day.
- **Average Time Saved per Chronic Consultation**: Target: **12–18 minutes reduction** in case-taking and note-taking time compared to manual paper/desktop repertory methods.
- **Question Acceptance Rate**: Percentage of suggested high-yield follow-up questions tapped or acknowledged by the doctor. Target: **> 45%**.
- **LSMC Completeness Improvement**: Percentage of recorded symptoms that reach "Complete" status (Location + Sensation + Modality + Concomitant). Target: **> 75%** of final recorded rubrics fully completed.

### 2.3 Retention & Stickiness
- **Day 7 Retention**: Target: **> 40%**.
- **Day 30 Retention**: Target: **> 28%**.
- **Follow-Up Consultation Rate**: Percentage of returning patients who have a subsequent visit logged under *Follow-Up (Hering)* mode. Target: **> 35%** within 45 days of initial chronic consultation.

### 2.4 Safety & Decision-Support Compliance
- **Red-Flag Acknowledgment Rate**: Target: **100%** of emergency allopathic alerts acknowledged by the doctor before proceeding.
- **Inimical Blocker Intervention Rate**: Track frequency where an inimical warning prevented sequential prescribing of incompatible remedies (*Apis* vs *Rhus Tox*).

---

## 3. Privacy-Safe Analytics Instrumentation Rules

> **STRICT PRIVACY DIRECTIVE**: Analytics telemetry (Firebase Analytics / Mixpanel) must NEVER log patient names, medical histories, transcripts, or specific prescribed remedies. Only anonymous event tags are permitted (e.g., `event_consultation_completed { mode: "CHRONIC", duration_sec: 1420, rubrics_count: 7, offline: true }`).

`[ASSUMPTION-METRICS-01]` Privacy-safe event tracking will be implemented via Firebase Analytics with all PII filtering enabled at the client boundary.

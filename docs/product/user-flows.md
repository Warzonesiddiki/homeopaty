# Similimum AI — Clinical User Flows
`Location: /docs/product/user-flows.md`

---

## 1. Master Clinical Consultation Flow

```
┌────────────────────────────────────────────────────────────────────────┐
│                        CONSULTING ROOM USER FLOW                       │
└────────────────────────────────────────────────────────────────────────┘

 [1] PATIENT ARRIVAL & SELECTION
      │
      ├─► Select Existing Patient OR Enter New Patient Details (Name, Age, Sex, Thermal)
      │
      ▼
 [2] CONSULTATION SETUP
      │
      ├─► Set Mode: [CHRONIC CONSTITUTIONAL] | [ACUTE OPD] | [FOLLOW-UP (HERING)]
      ├─► Verify Verbal Consent: Toggle "Patient Consent Verified"
      ├─► Tap "START AMBIENT MIC" (or "STREAM SIMULATED PATIENT" for training)
      │
      ▼
 [3] ACTIVE CASE-TAKING (HUD SCREEN)
      │
      ├─► Doctor listens empathically; app transcribes ambient speech
      ├─► Symptoms extracted automatically into symptom cards
      ├─► Doctor taps "Silent Observation Chips" (e.g. Restless, Weeping, Dry Lips)
      ├─► High-Yield Question Deck shows missing modalities (e.g., thermal / time agg)
      ├─► Doctor asks suggested question; patient response clarifies rubric
      │
      ▼
 [4] REPERTORIZATION & TOTALITY (REPERTORY MATRIX SCREEN)
      │
      ├─► Review Active Rubrics (Kent / Boenninghausen / Boger)
      ├─► Adjust weights (x1, x2, x3) or toggle "Eliminating Rubric"
      ├─► Observe Top-3 Remedy Leaderboard recalculating live
      │
      ▼
 [5] MATERIA MEDICA DIFFERENTIAL (DIFFERENTIAL SCREEN)
      │
      ├─► Head-to-head comparison of top 2-3 remedies (e.g. Nat-m vs Ignatia vs Sepia)
      ├─► Review Keynotes, Miasmatic dominance, and Ailments From
      ├─► Check Inimical & Complementary safety rules
      │
      ▼
 [6] PRESCRIPTION & POSOLOGY (RX & HERING'S SCREEN)
      │
      ├─► Select Remedy (e.g., Natrum Muriaticum)
      ├─► Select Potency Scale: Centesimal (30C, 200C, 1M) or LM (LM1–LM3)
      ├─► Select Posology (Single dose, Split dose, Water solution)
      ├─► Review Dietary Prohibitions (No raw camphor, coffee, eucalyptus)
      ├─► Generate Final Case Sheet & Prescription Summary
      ├─► Doctor signs off / validates decision
```

---

## 2. Follow-Up Consultation Flow (Hering's Law Evaluation)

```
 [1] Open Patient Record ──► Switch Mode to [FOLLOW-UP (HERING)]
      │
      ▼
 [2] Review Baseline Totality from Previous Visit
      │
      ▼
 [3] Patient reports new and modified symptoms
      │
      ▼
 [4] System evaluates directional vectors:
      ├─ Inside Out? (Mental/Emotional improved, skin eruption returned?)
      ├─ Above Downwards? (Headache resolved, leg joints aching?)
      ├─ Reverse Order of Appearance? (Old childhood asthma symptoms reappeared?)
      │
      ▼
 [5] Co-Pilot computes Kent's Observation (e.g., Observation 3: Short sharp agg, then long cure)
      │
      ▼
 [6] Clinical Recommendation:
      ├─ [WAIT AND WATCH / SAC LAC] (True cure in progress - do not interfere)
      ├─ [REPEAT POTENCY] (Relief too short; vital force paused)
      └─ [CHANGE REMEDY / ANTIDOTE] (Direction unfavorable; wrong remedy given)
```

---

## 3. Daily Clinic Workflow (Doctor Operational Loop)

1. **Morning Clinic Setup (8:30 AM)**: Doctor opens Similimum AI on Android tablet, verifies offline database readiness, and reviews pending follow-up cases.
2. **Consultation Rounds (9:00 AM – 2:00 PM)**:
   - 1-tap patient selection.
   - Ambient capture active on desk stand.
   - Peripheral glance at question deck to ensure complete LSMC modalities.
   - 1-tap prescription generation per patient in under 45 seconds post-case.
3. **End of Day Review (2:30 PM)**:
   - Review total cases taken, remedy distribution summary, and export daily clinic register.

`[ASSUMPTION-FLOW-01]` Average consultation duration is 30–45 minutes for new chronic cases and 10–15 minutes for acute/follow-up visits.

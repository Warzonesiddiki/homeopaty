# Similimum AI — Text-Described Screen Wireframes
`Location: /docs/design/wireframes.md`

---

## 1. Top Clinical Status Bar (Persistent Across All Screens)

```
┌────────────────────────────────────────────────────────────────────────┐
│ [● LIVE] Kavita Sharma (34F • Chilly)  [MODE: CHRONIC ▼]  [SETTINGS ⚙] │
│ ────────────────────────────────────────────────────────────────────── │
│ [❚❚ PAUSE MIC]  |||||||||||||||||||| (RMS Waveform)  [CONSENT: VERIFIED]│
└────────────────────────────────────────────────────────────────────────┘
```
- **Elements**: Patient avatar/initials, name, age, biological sex, thermal temperament (`Chilly` / `Warm-Blooded`). Consultation Mode dropdown badge. Live audio amplitude visualizer. Consent verification switch.

---

## 2. Red-Flag Emergency Banner (Conditional)

```
┌────────────────────────────────────────────────────────────────────────┐
│ ⚠️ CRITICAL ALLOPATHIC RED FLAG: Acute Coronary Syndrome Suspected      │
│ Crushing chest pain with left arm radiation detected. Urgent medical   │
│ emergency referral advised before homeopathic repertorization.         │
│ [ACKNOWLEDGE & DISMISS]                                                │
└────────────────────────────────────────────────────────────────────────┘
```
- **Visuals**: High-contrast Crimson background (`#DC2626`), white text, bold warning icon, required physician acknowledgement button.

---

## 3. Screen 1: Co-Pilot HUD (`SCREEN_HUD`)

```
┌────────────────────────────────────────────────────────────────────────┐
│ [▶ STREAM SIMULATED PATIENT: Mrs. Sharma (Nat-m Migraine)] [RESET]     │
│ [Input utterance bar: "e.g. thoda thoda paani baar baar..."]   [PARSE] │
├────────────────────────────────────────────────────────────────────────┤
│ 💡 HIGH-YIELD "ASK NEXT" QUESTION DECK                                 │
│ ┌────────────────────────────────────────────────────────────────────┐ │
│ │ Q1: "Is your headache relieved by tight pressure or cold cloths?"   │ │
│ │ Rationale: Differentiates Nat-m (tight bandage amel) from Belladonna│ │
│ │ [ASKED / COPY]                                                     │ │
│ └────────────────────────────────────────────────────────────────────┘ │
├────────────────────────────────────────────────────────────────────────┤
│ 👁️ SILENT CLINICAL OBSERVATIONS (Tap to inject into Totality)         │
│ [Restless in chair] [Weeping during narrative] [Dry chapped lips]      │
├────────────────────────────────────────────────────────────────────────┤
│ 📝 LIVE ANNOTATED TRANSCRIPT FEED                                      │
│ • PATIENT: "Doctor, dhoop mein jaate hi sar fatne lagta hai..."        │
│   🏷️ Rubric: HEAD - PAIN - sun, from exposure to (Nat-m, Glon)       │
│ • DOCTOR: "Pyaas kitni lagti hai?"                                     │
│ • PATIENT: "Pyaas bilkul nahi lagti, gale me sookha lagne par bhi."    │
│   🏷️ Rubric: STOMACH - THIRSTLESSNESS (Puls, Apis)                   │
└────────────────────────────────────────────────────────────────────────┘
```

---

## 4. Screen 2: LSMC & 12-Pillar Radar (`SCREEN_LSMC`)

```
┌────────────────────────────────────────────────────────────────────────┐
│ 🎯 BOENNINGHAUSEN LSMC SYMPTOM COMPLETENESS BREAKDOWN                  │
│ Symptom: "Bursting temporal headache" (Completeness: 75%)              │
│ ┌──────────────┬──────────────┬──────────────┬──────────────┐          │
│ │ LOCATION     │ SENSATION    │ MODALITIES   │ CONCOMITANTS │          │
│ │ Right temple │ Bursting /   │ < Sun heat   │ Nausea with  │          │
│ │ & forehead   │ Throbbing    │ > Bandage [?]│ vomiting [?] │          │
│ └──────────────┴──────────────┴──────────────┴──────────────┘          │
├────────────────────────────────────────────────────────────────────────┤
│ 🌐 12-PILLAR CONSTITUTIONAL COVERAGE RADAR                             │
│ [✓] 1. Causation (Grief)          [✓] 7. Cravings (Salt)              │
│ [✓] 2. Mind & Emotions (Closed)   [ ] 8. Aversions (Fat, bread)       │
│ [✓] 3. Thermal State (Chilly)     [✓] 9. Sleep & Dreams (Robbers)     │
│ [✓] 4. Thirst (Thirstless)        [✓] 10. Miasm (Psora-Sycotic)       │
│ [✓] 5. Time Modalities (10 AM)    [✓] 11. Past Medical History        │
│ [✓] 6. Side Affinity (Right)      [ ] 12. Physical Particulars        │
├────────────────────────────────────────────────────────────────────────┤
│ 👅 PHYSICAL SIGNS: TONGUE & OBJECTIVE FINDINGS                         │
│ [Mapped Tongue (Nat-m, Tarax)] [Strawberry Tongue (Bell)] [Trembling] │
└────────────────────────────────────────────────────────────────────────┘
```

---

## 5. Screen 3: Repertory Matrix (`SCREEN_REPERTORY`)

```
┌────────────────────────────────────────────────────────────────────────┐
│ School: [● KENT HIERARCHY]  [BOENNINGHAUSEN TPB]  [BOGER SYNOPTIC]     │
├────────────────────────────────────────────────────────────────────────┤
│ ACTIVE CANONICAL RUBRICS (4 active)             [+ ADD RUBRIC FROM DB] │
│ 1. MIND - GRIEF - silent sorrow           [W: 3x] [ELIMINATE: OFF] [✕] │
│ 2. MIND - CONSOLATION - agg.              [W: 3x] [ELIMINATE: ON]  [✕] │
│ 3. HEAD - PAIN - sun exposure, from       [W: 2x] [ELIMINATE: OFF] [✕] │
│ 4. STOMACH - DESIRES - salt               [W: 2x] [ELIMINATE: OFF] [✕] │
├────────────────────────────────────────────────────────────────────────┤
│ REPERTORIZATION RESULTS MATRIX                                         │
│ REMEDY        | COV | TOTAL SCORE | CONFIDENCE | GRADES ACROSS RUBRICS │
│ 1. Nat-m (★)  | 4/4 | 32 pts      | 94%        | [3] [3] [3] [3]       │
│ 2. Ignatia    | 3/4 | 21 pts      | 68%        | [3] [2] [0] [1]       │
│ 3. Sepia      | 3/4 | 18 pts      | 58%        | [1] [2] [1] [2]       │
│ 4. Pulsatilla | 2/4 | 12 pts      | Disqualified (Eliminated on Cons.) │
└────────────────────────────────────────────────────────────────────────┘
```

---

## 6. Screen 4: Materia Medica Differential (`SCREEN_MATERIA`)

```
┌────────────────────────────────────────────────────────────────────────┐
│ HEAD-TO-HEAD DIFFERENTIAL: Top 3 Competing Polychrests                 │
│ ┌───────────────────┬───────────────────┬───────────────────┐          │
│ │ Natrum Mur (94%)  │ Ignatia (68%)     │ Sepia (58%)       │          │
│ ├───────────────────┼───────────────────┼───────────────────┤          │
│ │ Silent grief;     │ Acute recent      │ Worn-out,         │          │
│ │ introverted;      │ emotional shock;  │ indifferent to    │          │
│ │ dwells on past.   │ sighing, sobbing. │ loved ones.       │          │
│ ├───────────────────┼───────────────────┼───────────────────┤          │
│ │ < Sun, 10-11 AM   │ < Tobacco, coffee │ < Cold, morning   │          │
│ │ > Tight pressure  │ > Swallowing solids│ > Vigorous motion │          │
│ └───────────────────┴───────────────────┴───────────────────┘          │
├────────────────────────────────────────────────────────────────────────┤
│ 🛡️ DRUG RELATIONSHIPS & INIMICAL SAFETY STATUS                         │
│ • Nat-m Follows Well: Sepia, Sulphur, Calcarea Carb                    │
│ • No Inimical Contraindications detected with patient history.         │
└────────────────────────────────────────────────────────────────────────┘
```

---

## 7. Screen 5: Prescription & Hering's Law (`SCREEN_PRESCRIPTION`)

```
┌────────────────────────────────────────────────────────────────────────┐
│ 💊 PRESCRIPTION BUILDER                                                │
│ Selected Remedy: Natrum Muriaticum                                     │
│ Potency: [6C] [30C] [● 200C] [1M] [10M] [LM1] [LM2] [Q Tincture]       │
│ Posology: [● Single Dose (4 pills)] [Split Dose in Water]              │
│ Vehicle: Sugar of Milk / No. 30 Globules                               │
├────────────────────────────────────────────────────────────────────────┤
│ ⚖️ HERING'S LAW FOLLOW-UP EVALUATOR                                    │
│ [✓] 1. Above Downward: Headache improved before knee pain emerged      │
│ [✓] 2. Inside Out: Mental depression lifted; mild skin eruption appeared│
│ [✓] 3. Vital to Less Vital: Heart palpitations stopped                 │
│ [✓] 4. Reverse Chronological Order: Old childhood eczema reappeared    │
│ EVALUATION: ✅ IDEAL PROGRESSION ACCORDING TO HERING'S LAW OF CURE     │
│ Recommendation: Wait & Watch (Sac Lac / Placebo)                       │
├────────────────────────────────────────────────────────────────────────┤
│ [📋 COPY FORMATTED CASE SHEET]          [📤 EXPORT PRESCRIPTION PAD]   │
└────────────────────────────────────────────────────────────────────────┘
```

---

## 8. Sticky Mini-Remedy Leaderboard & Bottom Navigation

```
┌────────────────────────────────────────────────────────────────────────┐
│ 🏆 TOP SIMILIMUM: 1. Nat-m (94%) • 2. Ign (68%) • 3. Sep (58%)   [VIEW]│
├────────────────────────────────────────────────────────────────────────┤
│ [ ⚡ HUD ]  [ 🎯 LSMC ]  [ 📊 REPERTORY ]  [ 📖 MATERIA ]  [ 💊 RX ]   │
└────────────────────────────────────────────────────────────────────────┘
```

`[ASSUMPTION-WIRE-01]` Text-described wireframes accurately reflect responsive Composable component hierarchies implemented in `com.example.ui.screens`.

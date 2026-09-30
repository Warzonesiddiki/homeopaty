# Similimum AI — Homeopathic Clinical Co-Pilot
## 02. Clinical Homeopathy AI Engine Specification

---

### 1. Theoretical Grounding & Organon of Medicine
Similimum AI strictly adheres to the classical principles formulated by Dr. Samuel Hahnemann:

- **§83–§104 (The Individualizing Examination of the Patient)**:
  - The physician observes silently, hears the patient's narrative without interruption, and writes down expressions in the patient’s own words.
  - The AI records colloquial, verbatim speech without early reduction to diagnostic labels.
- **§153 (Characteristic Symptoms - PQRS)**:
  - "The more striking, singular, uncommon and peculiar (characteristic) signs and symptoms of the case of disease are chiefly and most solely to be kept in view..."
  - General, common symptoms (such as plain headache or loss of appetite) receive low diagnostic weight unless qualified by striking modalities or causations.
- **§245–§285 (Posology & Drug Administration)**:
  - Strict posology guidelines: Single simple medicinal substance at one time, minimum dose, dynamic dilution, and assessment of susceptibility.

---

### 2. Boenninghausen’s Complete Symptom Doctrine (LSMC)

Every symptom processed by the parsing pipeline is decomposed into four discrete dimensions:

```
┌─────────────────────────────────────────────────────────────┐
│                    COMPLETE SYMPTOM                         │
├──────────────┬──────────────┬────────────────┬──────────────┤
│   Location   │  Sensation   │   Modalities   │ Concomitants │
│  (Anatomical │  (Character, │ (Aggravations/ │ (Associated  │
│  & Pathways) │ Pain Nature) │ Ameliorations) │  Phenomena)  │
└──────────────┴──────────────┴────────────────┴──────────────┘
```

#### Gap Identification Heuristics:
1. **Missing Modality Alert**: If Location and Sensation exist without Time, Thermal, or Motion modalities, the question engine triggers:
   - *"Is the symptom aggravated or relieved by cold, warmth, movement, or resting?"*
2. **Missing Concomitant Alert**: If high-intensity mental or physical symptoms occur without correlating bodily functions:
   - *"What happens to your stomach, perspiration, or energy when this headache strikes?"*

---

### 3. Vernacular Translation Engine (Colloquial to Canonical Rubrics)

The engine features a comprehensive semantic map translating everyday expressions across English, Hindi, and Hinglish into standard Kent, Boericke, and Synthetic Repertory Rubrics:

| Patient Utterance (Colloquial / Hindi) | Clinical Meaning | Canonical Repertory Rubric | Primary Remedies |
|---|---|---|---|
| *"Gusse ke baad se beemar ho gaya"* | Ailments from suppressed anger | `MIND - AILMENTS FROM - anger - suppressed` | Staph, Ign, Coloc, Cham |
| *"Pyaas bilkul nahi lagti, gala sukhne par bhi"* | Thirstlessness with dry mouth | `STOMACH - THIRSTLESSNESS - extreme dryness, with` | Puls, Apis, Gels, Nux-m |
| *"Thoda thoda paani baar baar peeta hoon"* | Thirst for small quantities frequently | `STOMACH - THIRST - small quantities, for - frequently` | Ars, Bell, Phos, Hyos |
| *"Dhoop mein sar fatne lagta hai"* | Headache aggravated by sun exposure | `HEAD - PAIN - sun, from exposure to` | Nat-m, Glon, Bell, Gels |
| *"Thandi hawa mein aate hi jadoo jaisa aaram"* | Amelioration in open cool air | `GENERALITIES - OPEN AIR - amel.` | Puls, Kali-bi, Sabin |
| *"Pani me bheeghne ke baad se jodo me dard"* | Arthritis agg by getting wet | `EXTREMITIES - PAIN - wetting, from getting` | Rhus-t, Dulc, Calc |
| *"Akelapan bilkul bardaasht nahi hota"* | Fear of being alone, seeks company | `MIND - FEAR - alone, of being` | Ars, Phos, Kali-c, Lyc |
| *"Chhoti si baat par rona aa jata hai"* | Weeping mood, ameliorated by consolation or worse | `MIND - WEEPING - tearful mood - easily` | Puls, Nat-m, Sep, Plat |

---

### 4. Dynamic Weighted Repertorization Formula

The total repertorial score for a candidate remedy $R$ given a set of active rubrics $S$ is computed as:

$$\text{Score}(R) = \sum_{r \in S} \left( \text{Grade}(R, r) \times \text{Weight}(r) \times \text{SchoolMultiplier}(r) \right) - \text{InimicalPenalty}$$

Where:
- $\text{Grade}(R, r) \in \{0, 1, 2, 3\}$ (0 = absent, 1 = plain, 2 = italics, 3 = bold in classical repertory).
- $\text{Weight}(r) \in \{1, 2, 3\}$ (User assigned or auto-boosted for PQRS / Causation).
- $\text{SchoolMultiplier}(r)$:
  - **Kent Hierarchical**: Mental Generals ($3\times$) $\rightarrow$ Physical Generals ($2\times$) $\rightarrow$ Particulars ($1\times$).
  - **Boenninghausen TPB**: Complete LSMC structure emphasized equally with Modalities prioritized ($2.5\times$).
- **Elimination Filter**: If a rubric is flagged as an *Eliminating Rubric* (e.g., rigid Thermal state "Chilly vs Warm-blooded"), any remedy with $\text{Grade}(R, r) = 0$ is disqualified immediately.

---

### 5. Inimical & Incompatible Drug Safety Matrix

Prescribing inimical remedies in sequence or together causes severe homeopathic aggravations and suppresses vitality:

| Remedy | Strictly Inimical / Incompatible | Safe Complementary Follow-Up |
|---|---|---|
| **Apis Mellifica** | **Rhus Toxicodendron** (Strictly Inimical) | Nat-m, Puls, Bar-c |
| **Causticum** | **Phosphorus**, **Coffea** | Carbo-v, Petros, Staph |
| **Ignatia Amara** | **Coffea Cruda**, **Tabacum**, **Nux Vomica** | Nat-m, Sepia, Zinc |
| **Lachesis Mutus** | **Dulcamara**, **Sepia**, **Psorinum** | Lyc, Hep, Nit-ac |
| **Silicea Terra** | **Mercurius Solubilis** (Causes deep suppurative ulceration) | Thuja, Sanic, Flu-ac |

---

### 6. Hering’s Law of Cure Assessment Matrix

During follow-up consultations, the engine maps changes across 4 directional vectors:

1. **Inside to Outside**: Center of vitality (mind/emotions) improves first; superficial symptoms (skin eruptions, discharges) appear. $\rightarrow$ **Favorable prognosis**.
2. **Above Downwards**: Headaches improve before leg pains. $\rightarrow$ **Favorable prognosis**.
3. **More Vital to Less Vital Organs**: Heart/lung asthma improves; eczema or rhinitis returns. $\rightarrow$ **True Cure in progress**.
4. **Reverse Order of Appearance**: Symptoms disappear in reverse chronological order of their historical emergence. $\rightarrow$ **Hering's Ideal Cure**.

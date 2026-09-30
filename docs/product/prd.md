# Similimum AI — Product Requirements Document (PRD)
`Location: /docs/product/prd.md`

---

## 1. Product Overview

Similimum AI is an ambient, native Android clinical companion designed for licensed homeopathic doctors. The application operates in the background during patient consultations, transcribing dialogue, structuring symptoms using Boenninghausen’s LSMC (Location, Sensation, Modality, Concomitant), highlighting PQRS (Peculiar, Queer, Rare, Strange) symptoms under Organon §153, generating non-leading follow-up questions, performing real-time weighted repertorization, and validating drug relationships (inimical/antidotal) and Hering’s Law of Cure during follow-ups.

---

## 2. Core Functional Modules & Feature Specifications

### Module 1: Ambient Consultation Listener & Dialogue Parser
- **Voice Stream Input**: Continuous on-device audio streaming via Android `SpeechRecognizer` (`en-IN`, `hi-IN`) with visual RMS amplitude waveform.
- **Simulation Mode**: Built-in 5 multi-turn clinical consultation simulations (*Natrum Mur* chronic migraine, *Lycopodium* GERD, *Arsenicum Album* acute gastroenteritis, *Phosphorus* cough, *Pulsatilla* otitis) for training and zero-mic testing.
- **Manual Input**: Multi-lingual text input bar supporting colloquial English and Hinglish phrases.
- **Silent Clinical Observation Chips**: 1-tap physical signs observed by the doctor (e.g., *"Restless in seat"*, *"Weeps during narrative"*, *"Dry peeling lips"*, *"Aversion to eye contact"*).

### Module 2: Hahnemannian Totality & LSMC Gap Engine
- **Boenninghausen Decomposition**: Every complaint is extracted into Location, Sensation, Modalities (< agg / > amel), and Concomitants.
- **LSMC Completeness Score**: Percentage meter tracking whether a symptom possesses all 4 components.
- **12-Pillar Constitutional Coverage Radar**: Visual tracker across Mind, Causation, Thermal State, Thirst, Desires/Aversions, Sleep, Dreams, Miasm, Time Modalities, Side Affinity, Generals, and Particulars.
- **High-Yield Question Deck**: Real-time card deck proposing top 3 non-leading inquiries to clarify missing modalities (e.g., *"Does the pain feel better in a cool breeze or with warm wraps?"*).

### Module 3: Multi-School Repertorization Matrix
- **Supported Repertory Traditions**:
  - *Kent’s Hierarchical System* (Mental Generals weighted 3x > Physical Generals 2x > Particulars 1x).
  - *Boenninghausen’s Therapeutic Pocket Book (TPB)* (LSMC modularity, Modalities emphasized 2.5x).
  - *Boger’s Synoptic & Pathological Generalities*.
- **Interactive Grid Controls**: Dynamic weight toggles (x1, x2, x3), Eliminating Rubric filter (hard disqualification of non-covering remedies), and custom clinic rubric injection.
- **Confidence Ranking**: Live percentage confidence score calculated using covered rubrics and grade sums.

### Module 4: Materia Medica Differential & Safety Check
- **Comparative Remedy View**: Side-by-side comparison of top 3 ranked remedies (e.g., *Bryonia* vs *Rhus Tox* vs *Arnica*).
- **Keynote Verification Checklist**: 1-tap verification of hallmark guiding symptoms.
- **Inimical & Incompatible Drug Blocker**: Automatic critical warning if doctor considers remedies known to be strictly inimical (e.g., *Apis Mellifica* followed by *Rhus Toxicodendron*, or *Causticum* with *Phosphorus*).

### Module 5: Clinical Prescription Pad & Posology Calculator
- **Potency Selection**: Centesimal (6C, 30C, 200C, 1M, 10M), 50-Millesimal (LM1 to LM30), and Mother Tinctures (Q).
- **LM Potency Dilution Calculator**: Step-by-step preparation protocol (medicinal solution, succussion count, second-cup dilution for hypersensitive patients).
- **Dietary & Lifestyle Prohibitions**: Contextual warnings against antidoting agents (e.g., raw camphor, eucalyptus, excessive strong coffee, mint).

### Module 6: Hering’s Law Follow-Up Tracker
- **Direction of Cure Evaluation**: Checklist verifying whether symptoms proceed:
  1. From above downwards.
  2. From inside out (vital to less vital organs).
  3. In reverse order of original appearance.
- **Kent’s 12 Prognostic Observations**: Classification of post-remedy reactions (e.g., Observation 3: Short aggravation followed by rapid cure; Observation 5: Amelioration comes first, aggravation follows later).

---

## 3. Mandatory Clinical Decision-Support Framing
- Every screen displays a persistent footer or badge: `"Clinical Decision Support Only • Requires Licensed Physician Validation"`.
- Prescription generation explicitly states: `"Draft Prescription prepared for Dr. [Name]. Verified by physician."`

`[ASSUMPTION-PRD-01]` Patient clinical records are stored locally with AES-256 encryption on the doctor's Android device.

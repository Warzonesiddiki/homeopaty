# Similimum AI — Comprehensive Clinical & Technical Glossary
`Location: /docs/product/glossary.md`

---

## 1. Classical Homeopathic Domain Terminology

> **CRITICAL DIRECTIVE**: The AI and developers must NEVER confuse or conflate the terms defined below. Misusing these terms invalidates clinical reasoning.

- **Simillimum (or Similimum)**: The single remedy whose medicinal pathogenesis most closely corresponds to the totality of the patient’s characteristic symptoms.
- **Totality of Symptoms**: The organized synthesis of characteristic mental generals, physical generals, modalities, and particular symptoms representing the disturbance of the vital force, not a mere numerical aggregation of complaints.
- **Symptom vs. Rubric**:
  - *Symptom*: The patient's subjective or objective experience in their own words (e.g., *"My head throbs whenever I step into bright sunshine"*).
  - *Rubric*: The standardized, canonical entry in a Homeopathic Repertory that catalogues remedies producing that specific symptom during provings (e.g., `HEAD - PAIN - sun, from exposure to`).
- **Repertory vs. Materia Medica**:
  - *Repertory*: An index or catalog of symptoms arranged systematically (by anatomical chapter or hierarchy) listing the proven remedies under each rubric with numerical grades.
  - *Materia Medica*: The comprehensive narrative encyclopedia of drug pictures detailing the toxicological, proving, and clinical symptoms of each individual medicinal substance.
- **Boenninghausen’s LSMC Doctrine**:
  - *Location*: Anatomical site, organ, tissue, or direction/pathway of pain.
  - *Sensation*: The subjective character of the suffering (e.g., burning, stitching, bruised, bursting, cramping).
  - *Modalities*: Factors that aggravate ($< \text{agg}$) or ameliorate ($> \text{amel}$) the complaint (time, weather, thermal, posture, motion, food).
  - *Concomitants*: Synchronous symptoms occurring alongside the main complaint that have no direct pathological relationship (e.g., nausea whenever a migraine begins).
- **PQRS (§153 Organon)**: Peculiar, Queer, Rare, and Strange symptoms. Highly individualized, characteristic symptoms that carry the highest repertorial weight (e.g., thirstlessness during high fever, or weeping when hearing music).
- **Potency vs. Dosage**:
  - *Potency*: The degree of dilution and succussion (dynamization) of the remedy:
    - *Decimal Scale (X/D)*: 1:10 dilution.
    - *Centesimal Scale (C)*: 1:100 dilution (e.g., 30C, 200C, 1M, 10M).
    - *50-Millesimal Scale (LM / Q)*: 1:50,000 dilution (LM1 to LM30), designed for gentle, continuous daily repetition without aggravation.
  - *Dosage / Posology*: The quantity and repetition frequency of the remedy (e.g., 4 globules of 30C dissolved in water, single dose vs. split dose).
- **Miasm**: The fundamental underlying chronic disease diathesis:
  - *Psora*: Lack, hypersensitivity, itching, functional disturbance.
  - *Sycosis*: Excess, overgrowth, proliferation, warty excrescences, fixed ideas.
  - *Syphilis*: Destruction, ulceration, deep tissue degeneration, suicidal depression.
  - *Tubercular*: Restlessness, rapid wasting, respiratory vulnerability, desire to travel.
- **Hering’s Law of Cure**: The natural law of healing formulated by Constantine Hering. True cure proceeds:
  1. From above downward (head to feet).
  2. From within outward (internal vital organs to skin/mucous membranes).
  3. From more vital to less vital organs (heart/lungs to joints).
  4. In the reverse order of the symptoms' historical appearance.
- **Kent’s 12 Prognostic Observations**: Observations of the patient's reaction post-remedy indicating whether the prescription initiated cure, suppression, or incurable exhaustion.
- **Drug Relationships**:
  - *Inimical*: Remedies that act destructively if given in close succession or together (e.g., *Apis* and *Rhus Tox*; *Causticum* and *Phosphorus*).
  - *Complementary*: Remedies that complete the cure started by another (e.g., *Belladonna* followed by *Calcarea Carb*).
  - *Antidote*: A substance that neutralizes the excessive action or aggravation of a remedy (e.g., *Camphor* or *Nux Vomica*).
  - *Follows Well*: Remedies proven by clinical experience to act favorably after a given remedy.

---

## 2. Technical & Architecture Terminology

- **Clinical Decision Support System (CDSS)**: Software that analyzes medical data and presents evidence-based recommendations to a human clinician who makes the final medical decision.
- **Ambient Listening**: Passive, low-latency audio capture that transcribes background dialogue without requiring push-to-talk buttons.
- **Local-First Software**: Architecture where all core functionality (storage, search, computation) executes locally on the user's hardware without needing remote server roundtrips.
- **Single-Activity Architecture**: Modern Android pattern where one `MainActivity` hosts Jetpack Compose composable screens using type-safe state transitions.

`[ASSUMPTION-GLOSS-01]` Terminology conforms to Central Council for Research in Homoeopathy (CCRH, India) standards and Liga Medicorum Homoeopathica Internationalis (LMHI).

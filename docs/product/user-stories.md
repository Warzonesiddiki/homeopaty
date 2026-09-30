# Similimum AI — User Stories & Acceptance Criteria
`Location: /docs/product/user-stories.md`

---

## 1. Consultation Capture & Ambient Audio

### US-01: Continuous Speech-to-Text Capture
- **As a** consulting homeopathic doctor,
- **I want** the app to listen continuously to the patient's narrative via my Android device microphone,
- **So that** I can maintain deep eye contact and rapport without typing or handwriting notes.
- **Acceptance Criteria**:
  - GIVEN the doctor grants `RECORD_AUDIO` permission, WHEN "Start Ambient Mic" is tapped, THEN the microphone captures dialogue with continuous loop restart upon silence.
  - An animated visual audio waveform confirms active microphone capture.
  - Doctor can pause or stop audio capture at any moment with a single tap.

### US-02: Vernacular & Idiomatic Symptom Extraction
- **As a** doctor practicing in India,
- **I want** the system to parse colloquial vernacular Hindi and English statements,
- **So that** expressions like *"Dhoop mein sar fatne lagta hai"* are mapped directly to canonical rubrics (`HEAD - PAIN - sun, from exposure to`).
- **Acceptance Criteria**:
  - WHEN a patient utters a recognized vernacular phrase, THEN the app extracts a symptom card within 500ms tagged with category and canonical rubric.
  - Extracted symptoms display an inline indicator showing whether the symptom is a General, Particular, or PQRS (§153).

---

## 2. Hahnemannian Totality & LSMC Completeness

### US-03: Boenninghausen LSMC Gap Detection
- **As a** classical homeopath,
- **I want** the app to evaluate whether a reported symptom has all 4 LSMC components (Location, Sensation, Modality, Concomitant),
- **So that** I know exactly which aspects are missing before selecting a rubric.
- **Acceptance Criteria**:
  - GIVEN an extracted symptom lacking modalities, WHEN viewed on the LSMC screen, THEN missing components are highlighted in amber.
  - The High-Yield Question Deck surfaces at least 2 non-leading clarifying questions targeting the missing elements.

### US-04: High-Yield Ask-Next Question Cards
- **As a** busy practitioner,
- **I want** the co-pilot to present the top 3 highest-yield homeopathic questions on the HUD,
- **So that** I can quickly ask the patient without interrupting the flow of case-taking.
- **Acceptance Criteria**:
  - Questions are non-leading and adhere to Organon §84–§90 rules.
  - Tapping a question copies it or marks it as asked, dismissing it from the deck.

---

## 3. Dynamic Repertorization & Safety

### US-05: Real-Time Weighted Repertorization
- **As a** doctor evaluating multiple symptom rubrics,
- **I want** candidate remedies to re-rank in real time as rubrics are added, weighted, or eliminated,
- **So that** I can observe the emerging Similimum dynamically.
- **Acceptance Criteria**:
  - Repertorization recalculation completes in <15ms locally.
  - Supports Kent Hierarchical weighting (Mental 3x, Physical Generals 2x, Particulars 1x) and Boenninghausen TPB weighting.
  - Eliminating rubrics strictly disqualify non-covering remedies from the top positions.

### US-06: Inimical Drug Safety Warning
- **As a** prescribing physician,
- **I want** the app to warn me if I select a remedy that is inimical to a recently prescribed remedy,
- **So that** I never cause severe drug aggravations or suppressions.
- **Acceptance Criteria**:
  - GIVEN a patient history with *Apis Mellifica*, WHEN *Rhus Toxicodendron* is considered, THEN a prominent safety warning banner is displayed: `"DANGEROUS INIMICAL COMBINATION: Apis is inimical to Rhus Tox"`.

---

## 4. Prescription & Follow-Up

### US-07: LM Potency Dilution Instructions
- **As a** prescriber using 50-Millesimal (LM) potencies,
- **I want** step-by-step preparation instructions generated for the selected LM potency,
- **So that** the patient or compounder prepares the medicinal solution accurately.
- **Acceptance Criteria**:
  - Displays succussion counts (8–10 succussions) and 2nd-cup dilution instructions for hypersensitive patients.

### US-08: Hering’s Law Follow-Up Evaluation
- **As a** doctor conducting a follow-up consultation,
- **I want** the app to evaluate symptom changes against Hering's 4 directional vectors,
- **So that** I can confirm whether the case is curing or suppressing.
- **Acceptance Criteria**:
  - Shows directional evaluation (Above downwards, Inside out, Reverse order of appearance).
  - Categorizes follow-up under Kent’s 12 Prognostic Observations with clinical next-step guidance (Wait & Watch / Placebo / Repeat).

`[ASSUMPTION-STORY-01]` Acceptance criteria are verified via automated JVM unit tests (`ExampleUnitTest.kt`) and manual clinical testing.

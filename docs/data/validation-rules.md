# Similimum AI — Client & Server Validation Rules
`Location: /docs/data/validation-rules.md`

---

## 1. Clinical Data Integrity Rules

To protect patient safety and prevent corrupted clinical histories, the application enforces strict deterministic validation rules across both the UI layer (Compose state checks) and the database persistence layer (Room entities).

---

## 2. Entity-by-Entity Validation Rules

### 2.1 Doctor Registration
- `full_name`: Non-blank string, length between 3 and 100 characters.
- `registration_number`: Non-blank, matches official state or national council format (e.g., regex `^[A-Z0-9\-\/]{4,25}$`).
- `qualification`: Must include recognized degree abbreviation (`BHMS`, `MD`, `LCEH`, `DHMS`).

### 2.2 Patient Profile
- `medical_record_number`: Non-blank, strictly unique across the doctor's database.
- `age`: Integer between 0 and 125.
- `biological_sex`: Must match one of `MALE`, `FEMALE`, or `OTHER`.
- `thermal_state`: Mandatory selection; cannot be null before initiating repertorization.

### 2.3 Consultation Case & Ambient Speech
- `chief_complaint`: Non-blank, minimum 3 characters.
- `case_mode`: Must be valid enum string (`CHRONIC`, `ACUTE`, `FOLLOW_UP`).
- `duration_seconds`: Non-negative integer.

### 2.4 Symptom Record & Boenninghausen LSMC
- `raw_utterance`: Non-blank string, max 1,000 characters.
- `category`: Must match valid `SymptomCategory` enum string.
- `completeness_score`: Validated float between `0.0f` and `1.0f` calculated as:
  $$\text{Score} = \frac{\mathbf{1}_{\text{loc}} + \mathbf{1}_{\text{sens}} + \mathbf{1}_{\text{mod}} + \mathbf{1}_{\text{conc}}}{4}$$

### 2.5 Repertory Rubric Weights & Eliminator Filters
- `weight`: Integer strictly between 1 and 3 (1 = low, 2 = standard, 3 = characteristic/PQRS).
- `is_eliminating`: Boolean (0 or 1). Warning issued if more than 3 rubrics are set as eliminating simultaneously to prevent empty candidate sets.

### 2.6 Posology & Prescription Validation
- `remedy_code`: Must exist as a valid primary key in the `remedies` table.
- `potency`: Mandatory selection; cannot be empty.
- **Inimical Check Rule**: The prescription validator executes `checkInimicalCompatibility(previousRemedy, newRemedy)`. If strictly inimical (e.g., *Apis Mellifica* and *Rhus Toxicodendron*), prescription submission is blocked until the doctor explicitly overrides with typed clinical justification.

`[ASSUMPTION-VALID-01]` Inimical safety validation executes synchronously before any prescription can transition from `DRAFT` to `DISPENSED`.

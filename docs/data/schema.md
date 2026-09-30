# Similimum AI — Database Schema Specification (SQLite / Room DDL)
`Location: /docs/data/schema.md`

---

## 1. Table Definitions & DDL Specifications

```sql
-- 1. DOCTORS TABLE (Local Practitioner Profile)
CREATE TABLE IF NOT EXISTS doctors (
    id TEXT PRIMARY KEY NOT NULL,
    full_name TEXT NOT NULL,
    registration_number TEXT NOT NULL,
    qualification TEXT NOT NULL, -- e.g. "BHMS, MD (Hom)"
    phone_number TEXT NOT NULL,
    email TEXT,
    clinic_name TEXT NOT NULL,
    clinic_address TEXT,
    created_at INTEGER NOT NULL,
    updated_at INTEGER NOT NULL
);

-- 2. PATIENTS TABLE (Demographics & Thermal Baseline)
CREATE TABLE IF NOT EXISTS patients (
    id TEXT PRIMARY KEY NOT NULL,
    doctor_id TEXT NOT NULL,
    medical_record_number TEXT NOT NULL UNIQUE,
    full_name TEXT NOT NULL,
    age INTEGER NOT NULL,
    biological_sex TEXT NOT NULL, -- 'MALE', 'FEMALE', 'OTHER'
    phone_number TEXT,
    thermal_state TEXT NOT NULL, -- 'CHILLY', 'HOT', 'AMBITHERMAL'
    thirst_tendency TEXT NOT NULL, -- 'THIRSTLESS', 'THIRST_LARGE_INTERVALS', 'THIRST_SMALL_FREQUENT'
    side_affinity TEXT NOT NULL, -- 'RIGHT', 'LEFT', 'BILATERAL', 'CROSSWISE'
    dominant_miasm TEXT NOT NULL, -- 'PSORA', 'SYCOSIS', 'SYPHILIS', 'TUBERCULAR'
    created_at INTEGER NOT NULL,
    updated_at INTEGER NOT NULL,
    FOREIGN KEY (doctor_id) REFERENCES doctors(id) ON DELETE CASCADE
);
CREATE INDEX idx_patients_doctor ON patients(doctor_id);
CREATE INDEX idx_patients_mrn ON patients(medical_record_number);

-- 3. CASES TABLE (Consultation Sessions)
CREATE TABLE IF NOT EXISTS cases (
    id TEXT PRIMARY KEY NOT NULL,
    patient_id TEXT NOT NULL,
    case_mode TEXT NOT NULL, -- 'CHRONIC', 'ACUTE', 'FOLLOW_UP'
    case_status TEXT NOT NULL, -- 'ACTIVE', 'COMPLETED', 'ARCHIVED'
    chief_complaint TEXT NOT NULL,
    duration_seconds INTEGER NOT NULL DEFAULT 0,
    transcript_text TEXT,
    clinical_notes TEXT,
    repertory_school TEXT NOT NULL DEFAULT 'KENT_HIERARCHY', -- 'KENT_HIERARCHY', 'BOENNINGHAUSEN_TPB', 'BOGER_SYNOPTIC'
    created_at INTEGER NOT NULL,
    updated_at INTEGER NOT NULL,
    FOREIGN KEY (patient_id) REFERENCES patients(id) ON DELETE CASCADE
);
CREATE INDEX idx_cases_patient ON cases(patient_id);
CREATE INDEX idx_cases_created ON cases(created_at DESC);

-- 4. SYMPTOM_RECORDS TABLE (Boenninghausen LSMC Structure)
CREATE TABLE IF NOT EXISTS symptom_records (
    id TEXT PRIMARY KEY NOT NULL,
    case_id TEXT NOT NULL,
    raw_utterance TEXT NOT NULL,
    location TEXT,
    sensation TEXT,
    modalities TEXT, -- Aggravations and Ameliorations
    concomitant TEXT,
    category TEXT NOT NULL, -- 'MENTAL_GENERAL', 'PHYSICAL_GENERAL', 'MODALITY_AGG', 'MODALITY_AMEL', 'PARTICULAR', 'PQRS'
    is_pqrs INTEGER NOT NULL DEFAULT 0,
    pqrs_rationale TEXT,
    completeness_score REAL NOT NULL DEFAULT 0.0,
    created_at INTEGER NOT NULL,
    FOREIGN KEY (case_id) REFERENCES cases(id) ON DELETE CASCADE
);
CREATE INDEX idx_symptoms_case ON symptom_records(case_id);

-- 5. REPERTORY_RUBRICS TABLE (Canonical Classical Catalog)
CREATE TABLE IF NOT EXISTS repertory_rubrics (
    id TEXT PRIMARY KEY NOT NULL,
    chapter TEXT NOT NULL, -- 'MIND', 'VERTIGO', 'HEAD', 'EYES', 'STOMACH', etc.
    full_path TEXT NOT NULL UNIQUE, -- e.g. "HEAD - PAIN - sun, from exposure to"
    school TEXT NOT NULL, -- 'KENT', 'BOENNINGHAUSEN', 'BOERICKE'
    remedy_count INTEGER NOT NULL DEFAULT 0,
    created_at INTEGER NOT NULL
);
CREATE INDEX idx_rubrics_chapter ON repertory_rubrics(chapter);
CREATE INDEX idx_rubrics_path ON repertory_rubrics(full_path);

-- 6. CASE_RUBRICS TABLE (Active Rubrics in Consultation)
CREATE TABLE IF NOT EXISTS case_rubrics (
    id TEXT PRIMARY KEY NOT NULL,
    case_id TEXT NOT NULL,
    rubric_id TEXT NOT NULL,
    weight INTEGER NOT NULL DEFAULT 2, -- 1 to 3
    is_eliminating INTEGER NOT NULL DEFAULT 0,
    created_at INTEGER NOT NULL,
    FOREIGN KEY (case_id) REFERENCES cases(id) ON DELETE CASCADE,
    FOREIGN KEY (rubric_id) REFERENCES repertory_rubrics(id) ON DELETE RESTRICT
);
CREATE INDEX idx_case_rubrics_case ON case_rubrics(case_id);

-- 7. REMEDIES TABLE (Canonical Materia Medica)
CREATE TABLE IF NOT EXISTS remedies (
    code TEXT PRIMARY KEY NOT NULL, -- 'Nat-m', 'Nux-v', 'Lyc', 'Ars'
    latin_name TEXT NOT NULL, -- 'Natrum Muriaticum'
    common_name TEXT NOT NULL, -- 'Chloride of Sodium / Common Salt'
    kingdom TEXT NOT NULL, -- 'MINERAL', 'PLANT', 'ANIMAL', 'NOSODE', 'SARCODE'
    thermal TEXT NOT NULL, -- 'CHILLY', 'HOT', 'AMBITHERMAL'
    miasm TEXT NOT NULL, -- 'PSORA', 'SYCOSIS', 'SYPHILIS', 'TUBERCULAR'
    essence TEXT NOT NULL,
    ailments_from TEXT NOT NULL,
    mental_generals TEXT NOT NULL,
    physical_generals TEXT NOT NULL,
    modalities_agg TEXT NOT NULL,
    modalities_amel TEXT NOT NULL,
    created_at INTEGER NOT NULL
);

-- 8. RUBRIC_REMEDY_GRADES TABLE (Repertory Association Matrix)
CREATE TABLE IF NOT EXISTS rubric_remedy_grades (
    rubric_id TEXT NOT NULL,
    remedy_code TEXT NOT NULL,
    grade INTEGER NOT NULL, -- 1, 2, or 3
    PRIMARY KEY (rubric_id, remedy_code),
    FOREIGN KEY (rubric_id) REFERENCES repertory_rubrics(id) ON DELETE CASCADE,
    FOREIGN KEY (remedy_code) REFERENCES remedies(code) ON DELETE CASCADE
);
CREATE INDEX idx_rrg_remedy ON rubric_remedy_grades(remedy_code);

-- 9. PRESCRIPTIONS TABLE (Clinical Posology Record)
CREATE TABLE IF NOT EXISTS prescriptions (
    id TEXT PRIMARY KEY NOT NULL,
    case_id TEXT NOT NULL UNIQUE,
    remedy_code TEXT NOT NULL,
    potency TEXT NOT NULL, -- '30C', '200C', '1M', '10M', 'LM1', 'Q'
    scale TEXT NOT NULL, -- 'CENTESIMAL', 'MILLISIMAL_LM', 'DECIMAL', 'MOTHER_TINCTURE'
    repetition TEXT NOT NULL, -- 'SINGLE_DOSE', 'DAILY_WATER_SPLIT', 'TDS', 'SOS'
    vehicle TEXT NOT NULL, -- 'SUGAR_GLOBULES', 'DISTILLED_WATER', 'BLANK_TABLETS'
    dietary_prohibitions TEXT, -- 'No raw camphor, coffee, eucalyptus'
    instructions TEXT NOT NULL,
    status TEXT NOT NULL DEFAULT 'DRAFT', -- 'DRAFT', 'DISPENSED', 'COMPLETED'
    created_at INTEGER NOT NULL,
    updated_at INTEGER NOT NULL,
    FOREIGN KEY (case_id) REFERENCES cases(id) ON DELETE CASCADE,
    FOREIGN KEY (remedy_code) REFERENCES remedies(code) ON DELETE RESTRICT
);

-- 10. FOLLOW_UPS TABLE (Hering's Law Outcomes)
CREATE TABLE IF NOT EXISTS follow_ups (
    id TEXT PRIMARY KEY NOT NULL,
    previous_prescription_id TEXT NOT NULL,
    session_date INTEGER NOT NULL,
    above_downward INTEGER NOT NULL DEFAULT 0, -- Boolean 0 or 1
    inside_outward INTEGER NOT NULL DEFAULT 0,
    vital_to_less_vital INTEGER NOT NULL DEFAULT 0,
    reverse_order_appearance INTEGER NOT NULL DEFAULT 0,
    kent_observation_index INTEGER NOT NULL, -- 1 to 12
    clinical_assessment TEXT NOT NULL,
    recommended_action TEXT NOT NULL, -- 'WAIT_AND_WATCH_SAC_LAC', 'REPEAT_SAME_POTENCY', 'HIGHER_POTENCY', 'CHANGE_REMEDY', 'ANTIDOTE'
    created_at INTEGER NOT NULL,
    FOREIGN KEY (previous_prescription_id) REFERENCES prescriptions(id) ON DELETE CASCADE
);
```

`[ASSUMPTION-SCHEMA-01]` SQLite foreign keys (`PRAGMA foreign_keys = ON;`) are strictly enforced by the Room Database configuration.

# Similimum AI — Comprehensive Data Dictionary
`Location: /docs/data/data-dictionary.md`

---

## 1. Patients Table Field Definitions

| Column Name | Data Type | Nullable | Description & Clinical Semantics |
|---|---|---|---|
| `id` | `TEXT (UUID)` | No | Globally unique identifier for the patient record. |
| `doctor_id` | `TEXT (UUID)` | No | Foreign key linking patient to the owning practitioner. |
| `medical_record_number` | `TEXT` | No | Clinic-facing MRN (e.g., `PAT-2026-0812`) used for anonymized de-identification. |
| `full_name` | `TEXT` | No | Patient legal or clinic registered name. |
| `age` | `INTEGER` | No | Age in years (0 for pediatric neonates). |
| `biological_sex` | `TEXT` | No | Anatomical biological sex (`MALE`, `FEMALE`, `OTHER`) influencing rubrics (e.g. menses, prostate). |
| `phone_number` | `TEXT` | Yes | Contact number for follow-up notifications. |
| `thermal_state` | `TEXT` | No | Baseline constitutional response to ambient heat/cold (`CHILLY`, `HOT`, `AMBITHERMAL`). |
| `thirst_tendency` | `TEXT` | No | Baseline thirst pattern (`THIRSTLESS`, `LARGE_INTERVALS`, `SMALL_FREQUENT`). |
| `side_affinity` | `TEXT` | No | Predominant lateralization of physical complaints (`RIGHT`, `LEFT`, `BILATERAL`). |
| `dominant_miasm` | `TEXT` | No | Underlying chronic diathesis (`PSORA`, `SYCOSIS`, `SYPHILIS`, `TUBERCULAR`). |

---

## 2. Cases Table Field Definitions

| Column Name | Data Type | Nullable | Description & Clinical Semantics |
|---|---|---|---|
| `id` | `TEXT (UUID)` | No | Unique consultation identifier. |
| `patient_id` | `TEXT (UUID)` | No | Foreign key referencing `patients(id)`. |
| `case_mode` | `TEXT` | No | Clinical workflow mode: `CHRONIC`, `ACUTE`, or `FOLLOW_UP`. |
| `case_status` | `TEXT` | No | Current session state: `ACTIVE`, `COMPLETED`, or `ARCHIVED`. |
| `chief_complaint` | `TEXT` | No | Primary reason for consultation in patient/doctor terminology. |
| `duration_seconds` | `INTEGER` | No | Elapsed recording/consultation time in seconds. |
| `transcript_text` | `TEXT` | Yes | Verbatim speech-to-text transcript feed with speaker tags. |
| `clinical_notes` | `TEXT` | Yes | Doctor's private observations and somatic examination findings. |
| `repertory_school` | `TEXT` | No | Active weighting engine: `KENT_HIERARCHY`, `BOENNINGHAUSEN_TPB`, or `BOGER_SYNOPTIC`. |

---

## 3. Symptom_Records Table Field Definitions

| Column Name | Data Type | Nullable | Description & Clinical Semantics |
|---|---|---|---|
| `id` | `TEXT (UUID)` | No | Unique symptom record ID. |
| `case_id` | `TEXT (UUID)` | No | Parent case consultation foreign key. |
| `raw_utterance` | `TEXT` | No | Verbatim colloquial phrase spoken by patient or doctor. |
| `location` | `TEXT` | Yes | Anatomical site, tissue, or radiation pathway (Boenninghausen L). |
| `sensation` | `TEXT` | Yes | Nature or quality of pain/discomfort (Boenninghausen S). |
| `modalities` | `TEXT` | Yes | Aggravating ($<$) and ameliorating ($>$) factors (Boenninghausen M). |
| `concomitant` | `TEXT` | Yes | Associated synchronous complaints (Boenninghausen C). |
| `category` | `TEXT` | No | Clinical hierarchy category (`MENTAL_GENERAL`, `PHYSICAL_GENERAL`, `PQRS`, etc.). |
| `is_pqrs` | `INTEGER` | No | Boolean flag indicating Organon §153 striking characteristic. |
| `completeness_score` | `REAL` | No | Float between 0.0 and 1.0 tracking LSMC completeness ratio. |

---

## 4. Prescriptions Table Field Definitions

| Column Name | Data Type | Nullable | Description & Clinical Semantics |
|---|---|---|---|
| `id` | `TEXT (UUID)` | No | Unique prescription identifier. |
| `case_id` | `TEXT (UUID)` | No | Consultation session foreign key. |
| `remedy_code` | `TEXT` | No | Canonical Latin code (e.g. `Nat-m`, `Nux-v`, `Puls`). |
| `potency` | `TEXT` | No | Selected dilution level (e.g. `30C`, `200C`, `LM1`). |
| `scale` | `TEXT` | No | Dilution scale: `CENTESIMAL`, `MILLISIMAL_LM`, `DECIMAL`, `MOTHER_TINCTURE`. |
| `repetition` | `TEXT` | No | Posology frequency instructions. |
| `vehicle` | `TEXT` | No | Dispensing medium (`SUGAR_GLOBULES`, `DISTILLED_WATER`). |
| `dietary_prohibitions` | `TEXT` | Yes | Antidoting agents to avoid during treatment. |
| `status` | `TEXT` | No | `DRAFT`, `DISPENSED`, or `COMPLETED`. |

`[ASSUMPTION-DICT-01]` All timestamps (`created_at`, `updated_at`) are stored as 64-bit Unix Epoch milliseconds (`INTEGER`).

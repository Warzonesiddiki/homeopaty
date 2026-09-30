# Similimum AI — Entity Relationship Diagram (ERD)
`Location: /docs/data/erd.md`

---

## 1. Domain Entity Relationship Architecture

The data model captures the complete homeopathic clinical lifecycle: from Doctor and Patient registration through Case consultations, Boenninghausen LSMC Symptom decomposition, Repertorization Rubrics, Materia Medica references, Prescriptions, and Hering's Law Follow-Up assessments.

```
┌─────────────────┐       1:N       ┌─────────────────┐
│     Doctor      │────────────────►│     Patient     │
│  (Practitioner) │                 │  (Demographics) │
└─────────────────┘                 └────────┬────────┘
                                             │ 1:N
                                             ▼
                                    ┌─────────────────┐
                                    │      Case       │
                                    │ (Consultation)  │
                                    └────────┬────────┘
                                             │
               ┌─────────────────────────────┼─────────────────────────────┐
               │ 1:N                         │ 1:N                         │ 1:1
               ▼                             ▼                             ▼
      ┌─────────────────┐           ┌─────────────────┐           ┌─────────────────┐
      │  SymptomRecord  │           │   CaseRubric    │           │  Prescription   │
      │ (LSMC Breakdown)│           │ (Link / Weight) │           │ (Remedy, Potency│
      └─────────────────┘           └────────┬────────┘           └────────┬────────┘
                                             │                             │
                                             │ N:1                         │ 1:N
                                             ▼                             ▼
                                    ┌─────────────────┐           ┌─────────────────┐
                                    │ RepertoryRubric │           │    FollowUp     │
                                    │ (Kent/Boericke) │           │(Hering's Law &  │
                                    └────────┬────────┘           │Kent Observation)│
                                             │ N:M                └─────────────────┘
                                             ▼
                                    ┌─────────────────┐
                                    │     Remedy      │
                                    │ (Materia Medica)│
                                    └─────────────────┘
```

---

## 2. Core Entity Descriptions & Cardinality

1. **Doctor (1) to Patient (N)**: Each licensed practitioner manages their private local patient panel.
2. **Patient (1) to Case (N)**: A patient has multiple consultation sessions over time (Initial Chronic Case, Acute Intercurrents, Follow-ups).
3. **Case (1) to SymptomRecord (N)**: Each consultation records verbatim utterances decomposed into Location, Sensation, Modality (< agg / > amel), and Concomitant.
4. **Case (1) to CaseRubric (N) to RepertoryRubric (1)**: Many-to-many relationship linking canonical repertory rubrics to the case with custom clinical weights (1x, 2x, 3x) and an `is_eliminating` flag.
5. **RepertoryRubric (N) to Remedy (M)**: Classic repertory matrix linking remedies to rubrics with historical grades (Grade 1: Ordinary, Grade 2: Italics, Grade 3: Bold).
6. **Case (1) to Prescription (1)**: The draft and finalized remedy prescription for that consultation session.
7. **Prescription (1) to FollowUp (N)**: Tracks the clinical outcome, direction of cure, and Kent's 12 Prognostic Observations across subsequent visits.

`[ASSUMPTION-DATA-01]` SQLite with Room persistence enforces cascading deletes from Patient -> Case -> SymptomRecords while keeping standard canonical RepertoryRubric and Remedy tables immutable.

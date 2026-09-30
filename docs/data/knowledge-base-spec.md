# Similimum AI — Homeopathic Knowledge Base Ingestion Specification
`Location: /docs/data/knowledge-base-spec.md`

---

## 1. Classical Literature Provenance & Public Domain Verification

To guarantee medical-grade precision without copyright infringement or licensing restrictions, Similimum AI ingests exclusively canonical classical works that have entered the worldwide **public domain** (all published prior to 1928):

1. **James Tyler Kent**: *Repertory of the Homeopathic Materia Medica* (1897 / 1904).
2. **Clemens von Boenninghausen**: *Therapeutic Pocket Book (TPB)* (1846, revised by T.F. Allen 1891).
3. **William Boericke**: *Pocket Manual of Homeopathic Materia Medica and Repertory* (9th Edition, 1927).
4. **Henry C. Allen**: *Keynotes and Characteristics with Comparisons of Some of the Leading Remedies of the Materia Medica* (1898).
5. **Constantine Hering**: *The Guiding Symptoms of Our Materia Medica* (1879–1891).
6. **Samuel Hahnemann**: *Organon of Medicine* (5th & 6th Editions, translated by R.E. Dudgeon & William Boericke).

---

## 2. Ingestion & Structured Parsing Pipeline

Raw texts are converted into normalized SQLite tables and JSON seed files structured into three primary domains:

### 2.1 Repertory Hierarchy Schema
- **Chapter**: Standard anatomical or functional heading (e.g. `MIND`, `HEAD`, `STOMACH`, `GENERALITIES`).
- **Rubric Path**: Colon or hyphen delimited taxonomy:
  - `MIND - CONSOLATION - agg.`
  - `HEAD - PAIN - sun, from exposure to`
  - `STOMACH - THIRST - small quantities, for - frequently`
- **Remedy Grades**: Standardized numerical scale:
  - `Grade 3 (Bold)`: 3 points. Found in high frequency during provings and repeatedly verified clinically.
  - `Grade 2 (Italics)`: 2 points. Proven and confirmed in clinical practice.
  - `Grade 1 (Ordinary)`: 1 point. Reported in provings or occasional clinical verification.

### 2.2 Materia Medica Monograph Schema
Each remedy record is parsed into structured Markdown/JSON fields:
- `Essence & Temperament`
- `Ailments From (Etiology / Causation)`
- `Mental Generals`
- `Physical Generals & Thermals`
- `Modalities: Aggravations (<)`
- `Modalities: Ameliorations (>)`
- `Characteristic Keynotes (PQRS)`
- `Complementary, Inimical, and Antidotal Relationships`

`[ASSUMPTION-KB-01]` All ingested texts are curated from verified scholarly digitizations (e.g., National Center for Homeopathy archives and CCRH publications) ensuring zero OCR transcription corruption.

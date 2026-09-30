# Similimum AI — Product Vision & Strategic Rationale
`Location: /docs/product/vision.md`

---

## 1. Executive Problem Statement

Homeopathy is a sophisticated medical art founded on Dr. Samuel Hahnemann’s *Totality of Symptoms* and the law of similars (*Similia Similibus Curentur*). Unlike conventional allopathic medicine, which maps pathological entities to standardized pharmaceutical protocols, classical homeopathic case-taking demands uncovering deep physical, emotional, constitutional, and miasmatic layers. A thorough constitutional consultation routinely lasts 45 to 90 minutes.

### The Pain Points of Practicing Homeopaths:
1. **Severe Cognitive Load**: The physician must simultaneously listen empathically, maintain eye contact, observe subtle somatic cues (e.g., restlessness, twitching, weeping), translate colloquial vernacular phrases into archaic repertory terminology, and search through 50,000+ rubrics across Kent or Boericke repertories.
2. **Note-Taking Friction Breaks the Healing Bond**: Writing notes on paper or typing on a computer disrupts the rapport essential to Hahnemannian case-taking (§84–§90 *Organon of Medicine*).
3. **Incomplete Symptoms**: Patients frequently describe sensations without modalities (e.g., *"My head hurts"*, omitting whether it is worse in sun, better cold applications, or accompanied by thirstlessness). Clinicians in high-throughput clinics (30–50 OPD patients/day) unintentionally miss asking critical differentiating modalities.
4. **Follow-Up Assessment Complexity**: Evaluating whether a patient is truly improving versus experiencing a dangerous suppression requires evaluating Hering’s Law of Cure and Kent’s 12 Prognostic Observations—a mental calculation that is often hurried.

---

## 2. Why AI + Homeopathy? Why Now?

- **Semantic Bridging of Vernacular to Canonical Rubrics**: Modern Large Language Models and natural language embeddings excel at mapping non-standard, colloquial speech (e.g., Hindi *"thoda thoda pani baar baar peena"* or English *"bursting forehead pain when exposed to sun"*) into canonical repertorial rubrics (`STOMACH - THIRST - small quantities, for - frequently` -> *Ars*, *Bell*; `HEAD - PAIN - sun, from exposure to` -> *Nat-m*, *Glon*).
- **Ambient Acoustic AI**: Modern Android mobile hardware allows real-time speech-to-text without cloud roundtrip lag, enabling ambient listening directly from the consultation desk.
- **The Modern Indian Healthcare Opportunity**: In India, AYUSH-licensed practitioners deliver vital primary and specialized care to hundreds of millions. High patient volumes demand tools that preserve classical precision while saving time.

---

## 3. Product Mission & Critical Decision-Support Guardrail

**Mission**: To be an invisible, hyper-intelligent clinical co-pilot that sits quietly on the consulting desk, observes the patient-doctor dialogue, detects symptom gaps in real-time, suggests high-yield follow-up questions, and dynamically computes weighted repertorization differentials.

### The Non-Negotiable Decision-Support Framing:
> **MANDATORY LEGAL & CLINICAL DIRECTIVE**: Similimum AI is strictly a **Clinical Decision Support System (CDSS)**. It never diagnoses, never prescribes, never dispenses, and never supersedes the licensed physician's clinical autonomy. Every output displays explicit decision-support disclaimers, and every remedy candidate requires physician verification.

`[ASSUMPTION-PROD-01]` Target doctors hold valid homeopathic medical qualifications (BHMS, MD-Hom, or international equivalent) and assume full statutory responsibility for prescriptions.

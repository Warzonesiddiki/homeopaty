# Similimum AI — Clinical AI Evaluation & Benchmarking Plan
`Location: /docs/ai/ai-eval-plan.md`

---

## 1. Evaluation Methodology & Classical Ground Truth

Unlike conventional NLP tasks, homeopathic clinical AI must be benchmarked against **verified historical cures** documented in peer-reviewed classical literature:
- Dr. J.T. Kent's *Lesser Writings & Clinical Cases*
- Dr. E.B. Nash's *Leaders in Homoeopathic Therapeutics*
- Dr. M.L. Tyler's *Homoeopathic Drug Pictures*
- Dr. George Vithoulkas's *Classical Case Studies*

---

## 2. Benchmark Golden Dataset (50 Canonical Cases)

The evaluation suite incorporates 50 curated clinical case vignettes with known curative remedies. 

### Sample Golden Test Cases:

| Case ID | Case Vignette Summary | Expected Rubrics | Target Similimum (Rank 1–3) |
|---|---|---|---|
| **GOLD-01** | High fever after exposure to dry cold wind. Intense anxiety, restlessness, fear of impending death. Thirst for cold water. Full, bounding pulse. Sudden onset at midnight. | MIND - FEAR - death of; FEVER - DRY heat; GENERALS - COLD - dry weather - agg. | **Aconitum Napellus** (Rank 1) |
| **GOLD-02** | Severe right lower lobe pneumonia with stitching chest pain. Patient holds chest when coughing, completely immobilized (< any motion). Extreme thirst for cold water in large drafts. Dry parched lips. | CHEST - PAIN - stitching - motion, on - agg.; STOMACH - THIRST - large quantities, for | **Bryonia Alba** (Rank 1) |
| **GOLD-03** | Chronic digestive distress with early satiety. Distension of lower abdomen, worse 4 PM to 8 PM. Desires warm food and drinks. Intellectually sharp, physically weak. | ABDOMEN - DISTENSION - 4 to 8 p.m. - agg.; STOMACH - APPETITE - easy satiety | **Lycopodium Clavatum** (Rank 1) |
| **GOLD-04** | Weeping disposition, seeks comfort, relieved by open air. Absence of thirst with all complaints. Wandering, shifting pains. Menses delayed, scanty. | MIND - WEEPING - tearful mood; GENERALS - AIR - open - amel.; STOMACH - THIRSTLESSNESS | **Pulsatilla Pratensis** (Rank 1) |

---

## 3. Core Evaluation Metrics & Target SLAs

```
┌──────────────────────────────────────┬─────────────┬─────────────┐
│ CLINICAL METRIC                      │ TARGET SLA  │ TEST HARNESS│
├──────────────────────────────────────┼─────────────┼─────────────┤
│ Similimum Top-3 Recall               │ >= 92.0%    │ Automated CI│
│ Similimum Top-5 Recall               │ >= 98.0%    │ Automated CI│
│ LSMC Extraction Precision (Boenning.)│ >= 90.0%    │ Eval Script │
│ Red Flag Emergency Detection Recall  │ 100.0%      │ Unit Gate   │
│ Remedy Hallucination Rate            │ 0.0% (ZERO) │ CI Pipeline │
│ Latency (p90 on Gemini 2.5 Flash)    │ < 850 ms    │ Performance │
└──────────────────────────────────────┴─────────────┴─────────────┘
```

---

## 4. Automated CI/CD Regression Pipeline

Whenever prompt templates or system instructions in `/docs/ai/prompt-templates.md` are updated, GitHub Actions executes `scripts/eval_golden_cases.py`:

```bash
# Automated evaluation runner executed in CI
python3 scripts/eval_clinical_suite.py \
  --model gemini-2.5-flash \
  --test-dataset datasets/golden_cases_50.json \
  --min-top3-recall 0.92 \
  --max-hallucination-rate 0.0 \
  --require-emergency-recall 1.0
```

If any commit causes the Top-3 Similimum Recall to drop below 92%, or if a single unverified remedy leaks through, the build fails immediately.

---

## 5. Evaluation Assumptions

- **[ASSUMPTION-EVAL-01]** The Golden 50 benchmark cases are derived exclusively from public domain classical literature (Kent, Nash, Boericke).
- **[ASSUMPTION-EVAL-02]** Automated clinical testing is executed using mock Gemini responses in local JVM tests and real LLM calls in scheduled nightly CI runs.

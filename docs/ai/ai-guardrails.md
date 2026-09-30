# Similimum AI — Clinical AI Guardrails & Safety Architecture
`Location: /docs/ai/ai-guardrails.md`

---

## 1. Ethical & Regulatory Boundary Rules

Similimum AI enforces non-negotiable architectural guardrails at the prompt level, inference layer, and UI rendering layer:

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                        IMMUTABLE GUARDRAIL CONTRACT                         │
├──────────────────────────┬──────────────────────────────────────────────────┤
│ PROHIBITED SYSTEM ACTION │ MANDATORY SYSTEM RESPONSE                        │
├──────────────────────────┼──────────────────────────────────────────────────┤
│ Diagnose Disease         │ Present "Differential Diagnostic Considerations" │
│ Prescribe Remedy         │ Suggest "Candidate Similimum Options"            │
│ Dictate Potency & Dosage │ Provide "Literature References (Kent/Boericke)"  │
│ Replace Physician Choice │ Require Doctor Explicit Touch-Confirmation       │
│ Unreferenced Remedies    │ Omit any remedy lacking canonical bibliography   │
└──────────────────────────┴──────────────────────────────────────────────────┘
```

---

## 2. Real-Time Red Flag & Emergency Screening

Homeopathy recognizes surgical, structural, and critical emergency boundaries (Organon §67). If speech or clinical notes contain signs of acute life-threatening pathology, the AI immediately displays an urgent red-alert banner advising conventional emergency stabilization:

| Clinical Red Flag Detected | Mandatory Warning Banner in UI | Action Required |
|---|---|---|
| Crushing retrosternal chest pain radiating to left arm / jaw | **RED ALERT: Potential Acute Coronary Syndrome.** Immediate emergency ECG / cardiac referral mandatory. | AI disables chronic repertorization flow; presents emergency triage card. |
| Sudden unilateral facial droop, arm weakness, slurred speech | **RED ALERT: Suspected Acute Cerebrovascular Event (Stroke).** Immediate tertiary stroke center transfer. | AI displays FAST protocol checklist. |
| Rigid board-like abdomen, rebound tenderness, high fever | **RED ALERT: Signs of Acute Peritonitis / Surgical Abdomen.** Urgent surgical consultation required. | AI flags surgical contraindication for remedy waiting. |

---

## 3. Hallucination Mitigation & Entity Verification Engine

To prevent the LLM from generating fictitious remedies, imaginary rubrics, or incorrect potencies, all model output passes through an on-device deterministic filter before rendering:

```kotlin
class ClinicalOutputSanitizer(
    private val remedyDao: RemedyDao,
    private val rubricDao: RubricDao
) {
    suspend fun sanitizeAiOutput(rawResponse: AiSynthesisResponse): SanitizedClinicalOutput {
        val verifiedRemedies = rawResponse.candidateRemedies.filter { candidate ->
            // 1. Verify remedy exists in canonical 300+ homeopathic pharmacopoeia
            remedyDao.findByName(candidate.remedyName) != null
        }
        
        val verifiedRubrics = rawResponse.suggestedRubrics.filter { rubric ->
            // 2. Verify rubric exists in Kent/Boericke database
            rubricDao.findByPath(rubric.rubricPath) != null
        }
        
        return SanitizedClinicalOutput(
            lsmc = rawResponse.lsmc,
            candidateRemedies = verifiedRemedies,
            suggestedRubrics = verifiedRubrics,
            disclaimerNotice = "Clinical Decision Support Only. Verify remedy in Materia Medica."
        )
    }
}
```

---

## 4. Confidence Score Granularity & Color Coding

Every remedy recommendation displayed in the app displays an honest confidence tier based on the rubric coverage score:

| Confidence Level | Rubric Totality Coverage | UI Visual Indicator | Doctor Action Guidance |
|---|---|---|---|
| **High Match** | >= 85% coverage of Mentals + Physical Generals | Deep Forest Green (`#1E5E3A`) | Primary contender; verify modalities in Boericke. |
| **Moderate Match** | 65% – 84% coverage of Generals | Warm Gold (`#B78103`) | Strong partial; check complementary/antithetical remedies. |
| **Speculative Match** | < 65% coverage (mostly Particulars) | Muted Terracotta (`#A34235`) | Incomplete totality; further inquiry required. |

---

## 5. Mandatory "Verify with Materia Medica" UI Contract

Every remedy card in the application features an unclosable bottom banner:
> **"Hahnemannian Precaution (§257–§258):** The physician must never choose a remedy based on prejudice or single keynote. Tap to view the full Materia Medica proving before dispensing."

---

## 6. Guardrail Assumptions

- **[ASSUMPTION-GUARD-01]** Red-flag screening executes locally via regex keyword matching for zero-latency emergency detection before cloud LLM transmission.
- **[ASSUMPTION-GUARD-02]** Any LLM response containing a remedy not found in the local SQLite pharmacopoeia database is silently stripped at the ViewModel layer.

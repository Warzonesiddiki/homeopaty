# Similimum AI — Copy Style Guide & Clinical Disclaimers
`Location: /docs/design/copy-style-guide.md`

---

## 1. Brand Voice & Tone Principles

- **Clinical, Serious & Reverent**: Respects the depth of classical homeopathic philosophy (Hahnemann, Kent, Boenninghausen) without sounding archaic or pseudo-scientific.
- **Supportive & Non-Prescriptive**: The AI never says *"You must prescribe Bryonia"*; it states *"Totality strongly indicates Bryonia Alba (Score: 28) based on motion aggravation and thirst for large quantities."*
- **Concise & Scannable**: Doctors have mere seconds to glance at the screen. High-yield questions must be punchy, gentle, and non-leading.
- **Bilingual Context**: Naturally incorporates accepted Indian clinical terminology (e.g., *Ailments from Grief / Gussa dabana*, *Thirstlessness / Pyaas na lagna*).

---

## 2. Mandatory Clinical Disclaimers & Legal Notice Copy

### 2.1 First-Launch Doctor Onboarding Acknowledgment (Modal Dialog)
> **Similimum AI Clinical Decision Support Acknowledgment**
> *"Similimum AI is an advanced clinical co-pilot designed exclusively to assist licensed homeopathic practitioners in symptom structuring, repertorization reference, and decision support. 
> 
> The application does NOT diagnose medical conditions, does NOT autonomously prescribe treatments, and does NOT replace the clinical judgment, physical examination, or statutory responsibility of the registered physician. 
> 
> By proceeding, you acknowledge that you are a qualified medical practitioner and assume complete clinical responsibility for all diagnostic and therapeutic decisions."*
> 
> `[ I ACKNOWLEDGE & ACCEPT ]`

### 2.2 Persistent Footer Badge (Displayed on Prescription and Repertory Screens)
> `"Clinical Decision Support Only • Requires Registered Physician Validation"`

### 2.3 Red-Flag Emergency Triage Notice
> `⚠️ URGENT MEDICAL ADVISORY: Suspected [Condition Name]. Patient symptoms indicate a potential acute life-threatening emergency requiring immediate allopathic / emergency hospital intervention prior to constitutional homeopathic repertorization.`

---

## 3. Microcopy & Button Action Glossary

| Intent / Action | Approved Copy | Banned / Prohibited Copy |
|---|---|---|
| Audio Capture | *"Start Ambient Mic"* / *"Pause Mic"* | *"Listen in Secret"*, *"Record Patient"* |
| Question Deck | *"Ask Next (High Yield)"* | *"AI Interrogation"*, *"Mandatory Questions"* |
| Repertorization | *"Totality Ranking"* / *"Repertory Matrix"* | *"AI Diagnosis"*, *"AI Cure"* |
| Prescription Pad | *"Draft Prescription Summary"* | *"Final Medicine Order"*, *"Automated Rx"* |
| Drug Warning | *"Inimical Drug Contraindication"* | *"Dangerous Poison"*, *"Banned Medicine"* |

`[ASSUMPTION-COPY-01]` Mandatory onboarding disclaimer acceptance is stored in encrypted SharedPreferences and must be re-accepted upon major version upgrades.

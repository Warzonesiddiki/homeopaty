# Similimum AI — UX States Specification (Loading, Empty, Error, Success)
`Location: /docs/design/ux-states.md`

---

## 1. UX State Architecture

Every screen and interactive component in Similimum AI adheres to four standardized, explicit UI states to ensure the doctor is never faced with a silent hang, an uninformative spinner, or ambiguous feedback.

---

## 2. Screen-by-Screen State Matrix

### 2.1 Co-Pilot HUD (`SCREEN_HUD`)
- **Empty State**: Displays an inviting, calming desk banner: *"Consultation Ready • Tap 'Start Ambient Mic' or 'Stream Simulated Patient' to begin active listening"*. Shows 3 sample utterance pill prompts (*"Sar mein dhoop se dard..."*, *"Gussa dabane se pet kharab..."*).
- **Loading State**: Subtle pulsing emerald glow around the microphone badge with live audio waveform bars moving in real time.
- **Success State**: Continuous auto-scrolling transcript with speaker chips (`PATIENT` in Indigo, `DOCTOR` in Emerald) and detected symptom tags highlighted with rubric badges.
- **Error State**: Microphone permission denied: High-contrast modal: *"Microphone Access Required for Ambient Case-Taking • Tap to Open Android Settings"*. Non-blocking manual text input remains fully accessible.

### 2.2 LSMC & Radar (`SCREEN_LSMC`)
- **Empty State**: Radar displays 12 empty grey outline pillars with label: *"No symptoms recorded yet. As symptoms are reported in the consultation, LSMC completeness and constitutional coverage will populate here."*
- **Loading / Partial State**: Amber highlight on missing components (e.g., Modalities: `[Missing Time/Thermal Modality]`).
- **Success State**: Green completion checkmarks with percentage score (e.g., `85% Complete`), 12-pillar radar filled with clinical domains.
- **Error State**: Invalid symptom entry: Inline red validation message: *"Please specify anatomical location or sensation."*

### 2.3 Repertory Matrix (`SCREEN_REPERTORY`)
- **Empty State**: Displays an empty grid with an illustrated repertory book icon and button: *"[+ Ingest Canonical Rubrics from Dialogue]"* or *"[Search 150+ Standard Rubrics]"*.
- **Loading State**: Instantaneous local calculation (<15ms) displays no spinner; for external cloud synthesis, a subtle progress track bar appears atop the leaderboard.
- **Success State**: Full Rubric x Remedy matrix with colored grade badges (Grade 3: Bold Green, Grade 2: Blue, Grade 1: Grey) and live confidence ranking.
- **Error State (Elimination Conflict)**: If an eliminating rubric disqualifies all candidate remedies, a distinct warning card displays: *"Zero remedies cover all eliminating rubrics. Consider unchecking the strict eliminator on [Rubric Name]"*.

### 2.4 Materia Medica Differential (`SCREEN_MATERIA`)
- **Empty State**: *"Add at least 2 rubrics in the Repertory workspace to generate comparative polychrest differentials."*
- **Success State**: Clean side-by-side comparison columns for top 3 remedies with collapsible keynote checklists.
- **Error / Safety Warning State**: Critical Inimical Blocker: High-visibility amber card warning of incompatible remedy combinations (*Apis* vs *Rhus Tox*).

### 2.5 Prescription & Hering's Law (`SCREEN_PRESCRIPTION`)
- **Empty State**: *"Select a leading remedy from the Repertory or Leaderboard to configure posology and Hering's law evaluation."*
- **Success State**: Formatted prescription sheet with one-tap copy button and toast confirmation: *"Case Sheet copied to clipboard"*.
- **Error State**: Missing required posology selection: Red outline on unselected potency picker.

`[ASSUMPTION-UX-01]` Zero-data states provide actionable 1-tap demo actions (e.g., loading sample cases) so first-time users can immediately explore all screens.

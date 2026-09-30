# Similimum AI — Homeopathic Clinical Co-Pilot
## 04. UI/UX Design System Specification

---

### 1. Visual Identity & Color System (Botanical Clinical)

The design system merges the clinical seriousness of medical software with the organic, living principles of classical homeopathy:

```
┌────────────────────────────────────────────────────────────────────────┐
│                   BOTANICAL CLINICAL PALETTE                           │
├─────────────────────┬───────────────────┬──────────────────────────────┤
│ Token Name          │ Hex Value         │ Clinical Semantics           │
├─────────────────────┼───────────────────┼──────────────────────────────┤
│ EmeraldPrimary      │ #0D6E52           │ Primary Vitality & Brand     │
│ EmeraldContainer    │ #D1FAE5           │ Active Selection, Chips      │
│ IndigoGenerals      │ #3730A3           │ Mind, Emotions & Causation   │
│ AmberModalities     │ #B45309           │ Aggravations (<) / Amel (>)  │
│ VioletPqrs          │ #6D28D9           │ Peculiar, Rare, Striking     │
│ CrimsonRedFlag      │ #DC2626           │ Emergency Allopathic Triage  │
│ SlateBackgroundDark │ #0F172A           │ Dark Theme Clinical Canvas   │
│ SlateSurfaceDark    │ #1E293B           │ Dark Elevation Cards         │
│ OffWhiteLight       │ #F8FAFC           │ Clean Ambient Light Canvas   │
└─────────────────────┴───────────────────┴──────────────────────────────┘
```

---

### 2. Ergonomics: The Zero-Distraction Consulting Desk
A homeopathic consultation is an intimate dialogue. A glowing, animated interface can distract the patient and break the physician's flow.
Similimum AI employs:
1. **Low-Visual-Noise HUD**: Information appears in compact, muted badges with high legibility.
2. **Peripheral Glance Architecture**: Key information (such as missing modalities or top remedy) is placed at screen boundaries with large typography (18sp–22sp bold) readable from 2 feet away on a desk stand.
3. **Silent Observation Chips**: 1-tap quick buttons for physical signs observed by the doctor (e.g., *Restless in Chair*, *Weeping while Narrating*, *Dry Chapped Lips*, *Avoids Eye Contact*) that immediately inject into the Totality without typing.

---

### 3. Navigation Workspaces

The bottom navigation bar transitions the doctor across five dedicated clinical workspaces:

1. **CO-PILOT HUD**: Live audio recording, real-time transcript streaming, Ask-Next high-yield question cards, and silent doctor observations.
2. **LSMC & RADAR**: Boenninghausen symptom completeness checker, 12-pillar constitutional coverage grid, and physical tongue/nail sign analyzer.
3. **REPERTORY MATRIX**: Interactive Rubric × Remedy grid with school selection (Kent, Boenninghausen, Boger), weighting, and eliminating filters.
4. **MATERIA MEDICA**: Side-by-side comparative analysis of competing polychrests, Keynote verification checklists, and miasmatic breakdown.
5. **RX & HERING'S**: Prescription pad, LM-potency dilution calculator, dietary prohibitions, and Hering's Law follow-up tracker.

---

### 4. Accessibility & Touch Targets
- All interactive buttons and chips have a minimum bounding box of **48dp × 48dp**.
- Font scale changes in Android system settings are fully supported with flexible column layouts and `Modifier.weight()` rather than hardcoded pixel dimensions.
- High-contrast visual cues for color-blind practitioners (icons accompany all color-coded status states).

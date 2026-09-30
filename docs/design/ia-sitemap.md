# Similimum AI — Information Architecture & Sitemap
`Location: /docs/design/ia-sitemap.md`

---

## 1. Information Architecture Overview

Similimum AI employs a **Single-Activity, Flat-Workspace Architecture** optimized for high-efficiency clinical use. Rather than forcing the doctor to navigate through deep hierarchical menus during a consultation, the app organizes all active case-taking functions into five horizontally accessible workspaces anchored by a persistent top clinical status bar and a sticky bottom mini-remedy leaderboard.

```
┌────────────────────────────────────────────────────────────────────────┐
│                        MAIN ACTIVITY CONTAINER                         │
├────────────────────────────────────────────────────────────────────────┤
│ TopClinicalStatusBar (Persistent across all workspaces)                │
│  - Patient Header (Name, Age, Sex, Thermal Temperament)                │
│  - Consultation Mode Selector (Chronic / Acute / Follow-Up)            │
│  - Audio Waveform RMS Visualizer & Mic Control                         │
│  - Patient Consent Toggle Indicator                                    │
├────────────────────────────────────────────────────────────────────────┤
│ RedFlagEmergencyBanner (Conditional: Appears only on critical triggers)│
├────────────────────────────────────────────────────────────────────────┤
│ WORKSPACE CONTENT AREA (Switchable via Bottom Navigation Bar)          │
│                                                                        │
│  [1] HUD (Live Case-Taking Desk)                                       │
│      ├── Audio Control Deck & Simulated Stream Controller              │
│      ├── Manual Colloquial Utterance Input Bar                         │
│      ├── High-Yield "Ask Next" Question Cards                          │
│      ├── Silent Doctor Observation Chips                               │
│      └── Live Annotated Dialogue Feed (Speaker Diarized)               │
│                                                                        │
│  [2] LSMC & RADAR (Completeness & Constitutional Coverage)             │
│      ├── Boenninghausen LSMC Breakdown Cards (Location/Sens/Mod/Conc)  │
│      ├── 12-Pillar Constitutional Coverage Radar                       │
│      └── Physical Signs & Objective Tongue Diagnostics                 │
│                                                                        │
│  [3] REPERTORY MATRIX (Interactive Rubric x Remedy Grid)               │
│      ├── Repertory School Selector (Kent / Boenninghausen TPB / Boger) │
│      ├── Active Rubrics Manager (Weights x1-x3, Eliminator Toggle)     │
│      ├── Quick Rubric Ingestion Chips                                  │
│      └── Live Dynamic Repertorization Ranking Matrix                   │
│                                                                        │
│  [4] MATERIA MEDICA (Comparative Differential Diagnostics)             │
│      ├── Top 3 Head-to-Head Comparative Cards                          │
│      ├── Keynote Verification Checklists                               │
│      ├── Miasmatic Breakdown (Psora / Sycosis / Syphilis / Tubercular) │
│      └── Drug Relationships & Inimical Safety Warnings                 │
│                                                                        │
│  [5] RX & HERING'S (Prescription & Prognosis)                          │
│      ├── Potency Selector (6C - 10M, LM1 - LM30, Q Tinctures)          │
│      ├── LM 50-Millesimal Dilution Protocol Calculator                 │
│      ├── Dietary & Lifestyle Prohibitions (Antidote Warnings)          │
│      ├── Complete Case Sheet & Formatted Export Generator              │
│      └── Hering's Law & Kent's 12 Observations Follow-up Evaluator     │
├────────────────────────────────────────────────────────────────────────┤
│ MiniRemedyLeaderboard (Sticky Top-3 floating rank above navigation)   │
├────────────────────────────────────────────────────────────────────────┤
│ BottomNavigationBar (5 Workspace tabs with icon badges)                │
└────────────────────────────────────────────────────────────────────────┘
```

---

## 2. Screen ID & Deep-Link Route Hierarchy

| Workspace / Screen ID | Primary Composable | Key Child Modals / Dialogs |
|---|---|---|
| `SCREEN_HUD` | `CoPilotHudScreen` | Quick Patient Edit Dialog, Case Reset Confirmation |
| `SCREEN_LSMC` | `LsmcRadarScreen` | Symptom Detail Drawer, Tongue Sign Selector |
| `SCREEN_REPERTORY` | `RepertoryMatrixScreen` | Add Rubric Catalog Dialog, Weight Adjustment Sheet |
| `SCREEN_MATERIA` | `MateriaMedicaDiffScreen` | Full Drug Monograph Modal, Relationship Graph |
| `SCREEN_PRESCRIPTION` | `RxHeringsScreen` | Case Sheet Copy Confirmation, LM Preparation Step Guide |

`[ASSUMPTION-IA-01]` Flat 5-tab workspace switching preserves internal state (scroll positions, unsaved notes, audio recording status) across all tab transitions.

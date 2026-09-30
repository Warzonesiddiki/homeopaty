# Similimum AI — Responsive Layout & Offline UX Architecture
`Location: /docs/design/responsive.md`

---

## 1. Multi-Device Canonical Layouts (Phone vs Tablet)

Homeopathic doctors utilize diverse hardware in their consulting rooms: compact handheld smartphones (OPD mobility) and 10–12 inch Android tablets on desk stands. The application dynamically adapts using **Material 3 Window Size Classes** (`Compact`, `Medium`, `Expanded`).

```
┌────────────────────────────────────────────────────────────────────────┐
│                        RESPONSIVE FORM FACTORS                         │
├───────────────────────────────────┬────────────────────────────────────┤
│ COMPACT (Mobile Phones < 600dp)   │ EXPANDED (Tablets >= 840dp)        │
├───────────────────────────────────┼────────────────────────────────────┤
│ • Vertical single-column stack    │ • Dual-pane side-by-side layout    │
│ • Bottom Navigation Bar           │ • Left Navigation Rail (ergonomic) │
│ • Sticky Bottom Mini-Leaderboard  │ • Left Pane: Live Ambient HUD Feed │
│ • Modal bottom sheets for details │ • Right Pane: Live Repertory Grid  │
│ • Full-screen workspace switching │ • Always-visible persistent Materia│
└───────────────────────────────────┴────────────────────────────────────┘
```

---

## 2. Adaptive Screen Strategies

### 2.1 Co-Pilot HUD Responsive Rules
- **Compact (Phones)**: Vertical flow with Question Deck pinned to top, followed by Silent Observation Chips (horizontal scroll row), and Live Transcript taking the remaining screen height.
- **Expanded (Tablets / Foldables)**:
  - Left Pane (45% width): Audio waveform, live transcript, and manual text input.
  - Right Pane (55% width): Question Deck, Boenninghausen LSMC Breakdown, and Mini-Leaderboard running concurrently side-by-side.

### 2.2 Repertory Matrix Responsive Rules
- **Compact (Phones)**: Horizontally scrollable grid table (`Modifier.horizontalScroll()`). Remedy abbreviations fixed at top header; rubrics listed vertically.
- **Expanded (Tablets)**: Full widescreen matrix displaying all 5 candidate remedy columns without horizontal scroll truncation, showing remedy Latin names, total scores, and grade cells simultaneously.

---

## 3. Offline UI Indicators & Resilience

- **Always-Ready Offline Badge**: A discrete, non-intrusive status pill in the top bar indicates:
  - `● Offline Engine Active (30+ Remedies, 150+ Rubrics Local)` when no network is present.
  - `● Hybrid Cloud Connected (Gemini 2.5 Flash Enabled)` when Wi-Fi/4G is active.
- **No Blocking Network Dialogs**: The application never displays a modal "Internet Disconnected" popup that interrupts clinical case-taking. All queries resolve against the local SQLite / Kotlin deterministic engine instantly.

`[ASSUMPTION-RESP-01]` Tablet landscape orientation (1280x800dp or 1920x1200dp) represents the primary consulting desk hardware configuration for private practitioners.

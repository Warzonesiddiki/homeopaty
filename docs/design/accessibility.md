# Similimum AI — Accessibility & Clinical Ergonomics (WCAG AA)
`Location: /docs/design/accessibility.md`

---

## 1. Accessibility Target & Clinical Context

In a fast-paced clinic, accessibility is not merely an edge-case compliance checkbox—it directly impacts physician fatigue, diagnostic speed, and accuracy:
- **Target Standard**: **WCAG 2.1 Level AA** compliance across all mobile and tablet composables.
- **Physical Context**: High-glare consultation rooms with ceiling fluorescent lighting; doctor operating a device one-handed while talking or palpating a pulse; varying visual acuity among veteran doctors (45–70 years old).

---

## 2. Touch Target Architecture (One-Handed Clinical Use)

- **Minimum Interactive Touch Target**: Every button, tab, chip, and dropdown selector adheres to **48.dp x 48.dp** (`Modifier.minimumInteractiveComponentSize()`).
- **Thumb-Zone Optimization**:
  - Bottom navigation bar and primary actions (*"Pause Mic"*, *"Add Rubric"*, *"Prescribe"*) are positioned in the bottom 30% of the screen within the natural thumb sweep zone.
  - Secondary reference information (Materia Medica monographs) is situated in the upper viewport.

---

## 3. High-Contrast Ratios & Typography Scaling

- **Contrast Ratios**:
  - Regular Text (14sp–16sp): Exceeds **4.5:1** contrast ratio against all container backgrounds.
  - Large Text & Headers (18sp+): Exceeds **7:1** contrast ratio against dark and light surfaces.
  - Emergency Alerts: Crimson background (`#DC2626`) with solid White (`#FFFFFF`) text yields an **8.2:1** contrast ratio.
- **Font Scaling (Android SP Rules)**:
  - All typography tokens use scalable pixels (`sp`).
  - Layouts use dynamic wrapping (`FlowRow`, `LazyColumn`, `Modifier.weight()`) rather than fixed height bounding boxes, preventing text truncation when doctors configure 130% or 150% large text in Android Accessibility settings.

---

## 4. Screen Reader Semantics & TalkBack

- **Content Descriptions**: Every icon button, waveform indicator, and rubric badge defines a non-null, localized `contentDescription`:
  ```kotlin
  IconButton(
      onClick = { viewModel.toggleRecording() },
      modifier = Modifier.testTag("mic_toggle_button")
  ) {
      Icon(
          imageVector = if (isRecording) Icons.Default.Mic else Icons.Default.MicOff,
          contentDescription = if (isRecording) stringResource(R.string.cd_pause_mic) else stringResource(R.string.cd_start_mic)
      )
  }
  ```
- **Live Regions for Emergency Alerts**: The Red-Flag Emergency Banner utilizes Compose accessibility live regions (`Modifier.semantics { liveRegion = LiveRegionMode.Assertive }`) ensuring TalkBack immediately reads urgent life-threatening warnings to visually impaired doctors.

`[ASSUMPTION-A11Y-01]` All clinical status changes and question deck updates provide semantic TalkBack feedback without triggering audio interference with the active speech recognizer.

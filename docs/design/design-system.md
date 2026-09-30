# Similimum AI — Material 3 Design System Specification
`Location: /docs/design/design-system.md`

---

## 1. Botanical Clinical Theme Philosophy

The design system blends medical-grade precision with the living botanical ethos of classical homeopathy. It adheres strictly to **Material Design 3 (M3)** using standard Jetpack Compose tokens, prioritizing peripheral glance legibility and high contrast in brightly lit consultation rooms.

---

## 2. Color System & Design Tokens

```kotlin
// Botanical Clinical Emerald (Brand Primary & Vitality)
val EmeraldPrimary       = Color(0xFF0D6E52) // Main action & brand identity
val EmeraldOnPrimary     = Color(0xFFFFFFFF)
val EmeraldContainer     = Color(0xFFD1FAE5) // Selected states, subtle active cards
val EmeraldOnContainer   = Color(0xFF064E3B)

// Deep Mind Indigo (Mental Generals & Emotional Causation)
val IndigoGenerals       = Color(0xFF3730A3)
val IndigoContainer      = Color(0xFFE0E7FF)
val IndigoOnContainer    = Color(0xFF1E1B4B)

// Modality Amber (Aggravations < and Ameliorations >)
val AmberModalities      = Color(0xFFB45309)
val AmberContainer       = Color(0xFFFEF3C7)
val AmberOnContainer     = Color(0xFF78350F)

// Peculiar Violet (PQRS §153 Striking Characteristics)
val VioletPqrs           = Color(0xFF6D28D9)
val VioletContainer      = Color(0xFFEDE9FE)
val VioletOnContainer    = Color(0xFF4C1D95)

// Emergency Crimson (Allopathic Red-Flag Triage)
val CrimsonRedFlag       = Color(0xFFDC2626)
val CrimsonContainer     = Color(0xFFFEE2E2)
val CrimsonOnContainer   = Color(0xFF7F1D1D)

// Clinical Neutrals (Dark & Light Surface Hierarchy)
val SlateBackgroundDark  = Color(0xFF0F172A) // Dark theme desk canvas
val SlateSurfaceDark     = Color(0xFF1E293B) // Dark elevated cards
val SlateBorderDark      = Color(0xFF334155)

val OffWhiteLight        = Color(0xFFF8FAFC) // Light clean canvas
val WhiteSurfaceLight    = Color(0xFFFFFFFF)
val SlateBorderLight     = Color(0xFFE2E8F0)
```

---

## 3. Typography Hierarchy

The type scale balances compact data density (essential for repertory grids) with high-visibility headlines for peripheral reading from across the consulting desk:

| Token Name | Font Style / Weight | Size / Line Height | Primary Clinical Usage |
|---|---|---|---|
| `displaySmall` | SemiBold (600) | 24sp / 32sp | Top Similimum Remedy Name |
| `headlineMedium` | SemiBold (600) | 20sp / 26sp | Workspace Screen Titles, Red Flag Header |
| `titleMedium` | Medium (500) | 16sp / 22sp | High-Yield Question Deck Text |
| `titleSmall` | Bold (700) | 14sp / 20sp | Rubric Chapter, Remedy Latin Code |
| `bodyMedium` | Normal (400) | 14sp / 20sp | Patient Dialogue Transcript, Case Notes |
| `bodySmall` | Normal (400) | 12sp / 16sp | Modalities, Concomitants, Clinical Rationales |
| `labelSmall` | Bold (700) | 10sp / 14sp | Rubric Grade Badges (1, 2, 3), Category Tags |

---

## 4. Spacing, Elevation & Surface Shape Tokens

- **Grid Basis**: Standard **8.dp** increment grid (`4.dp`, `8.dp`, `12.dp`, `16.dp`, `24.dp`, `32.dp`).
- **Corner Radii**:
  - Small Elements (Chips, Badges): `RoundedCornerShape(6.dp)`.
  - Cards & Workspaces: `RoundedCornerShape(12.dp)`.
  - Floating Leaderboard & Bottom Bar: `RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)`.
- **Touch Targets**: All interactive elements (observation chips, question deck cards, tabs) enforce a minimum touch bounding box of **48.dp x 48.dp** (`Modifier.minimumInteractiveComponentSize()`).

---

## 5. Standard Component Guidelines
- **Filled vs Outlined**: Primary actions (e.g., *"Start Ambient Mic"*, *"Prescribe"*) use `ButtonDefaults.buttonColors(containerColor = EmeraldPrimary)`. Secondary toggles use `OutlinedButton` with `SlateBorderLight`.
- **Waveform Canvas**: Dynamic live audio visualizer drawn on Compose `Canvas` using smoothed sinusoidal curves with normalized RMS amplitude float values.

`[ASSUMPTION-DESIGN-01]` Material 3 Dynamic Theming on Android 12+ is supported with fallback to custom Botanical Emerald tokens for clinical branding consistency.

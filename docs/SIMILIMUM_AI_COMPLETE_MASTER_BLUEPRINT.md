# Similimum AI — Homeopathic Clinical Co-Pilot
## Complete Master Blueprint

---

### 1. Vision, Philosophy, and Principles

Classical Homeopathy is rooted in the individualization of human suffering. While conventional medicine asks *"What disease does this patient have?"*, the homeopath asks *"How does this individual uniquely express imbalance in their physical, emotional, and mental totality?"*

Similimum AI is built to honor this fundamental philosophy. It is not an impersonal algorithmic diagnosis tool, but rather a quiet, erudite colleague seated beside the practitioner in the consultation room.

```
       "Similia Similibus Curentur" — Let Likes Be Cured by Likes
                                    §
    Totality of Symptoms  ───►  LSMC Completeness  ───►  Materia Medica
             ▲                        ▲                        ▲
             │                        │                        │
       Patient Story           Doctor Questions         Similimum Match
```

---

### 2. Comprehensive System Architecture

```
+-------------------------------------------------------------------------+
|                         PRESENTATION LAYER                              |
|                                                                         |
|  +-------------------------------------------------------------------+  |
|  | Top Bar: Patient Profile | Mode Badge | Waveform | Gemini Status |  |
|  +-------------------------------------------------------------------+  |
|  | Red Flag Emergency Banner (High-contrast alert if triage needed)  |  |
|  +-------------------------------------------------------------------+  |
|  | Dynamic Workspace Views:                                          |  |
|  |  [1] HUD: Live Transcript, Question Deck, Silent Observations    |  |
|  |  [2] LSMC Radar: Completeness matrix, 12-pillar coverage, Tongue  |  |
|  |  [3] Repertory: Interactive rubric grid, weights, elimination     |  |
|  |  [4] Materia Medica: Head-to-head comparison, Keynote checks      |  |
|  |  [5] Rx & Hering's: Posology, LM dilution calculator, Follow-ups  |  |
|  +-------------------------------------------------------------------+  |
|  | Mini-Remedy Leaderboard (Sticky bottom bar showing Top-3 rank)    |  |
|  +-------------------------------------------------------------------+  |
|  | Bottom Navigation Bar (5 core clinical tabs)                      |  |
+-------------------------------------------------------------------------+
                                    |
+-------------------------------------------------------------------------+
|                        APPLICATION LAYER                                |
|  ConsultationViewModel                                                  |
|  - Manages reactive state flow: ConsultationUiState                     |
|  - Controls SpeechRecognizer & Audio Waveform streaming                 |
|  - Orchestrates multi-turn clinical simulation                          |
|  - Connects Knowledge Engine with UI actions                            |
+-------------------------------------------------------------------------+
                                    |
+-------------------------------------------------------------------------+
|                         DOMAIN & DATA LAYER                             |
|  HomeopathyKnowledgeEngine                                              |
|  - 30+ Classical Polychrest Remedies (Latin, Thermal, Keynotes)         |
|  - 150+ Canonical Kent & Boericke Rubrics with grades & weights         |
|  - Boenninghausen LSMC Symptom Completeness Analyzer                    |
|  - Multi-School Repertorization (Kent, Boenninghausen TPB, Boger)       |
|  - Inimical & Complementary Drug Safety Matrix                          |
|  - Physical Signs & Tongue Diagnostic Map                               |
|  - Allopathic Red-Flag Emergency Screener                               |
|                                                                         |
|  GeminiClinicalService (Hybrid Reasoning)                               |
|  - Cloud reasoning with Gemini 2.5 Flash via structured JSON            |
|  - Seamless automatic fallback to on-device engine                      |
+-------------------------------------------------------------------------+
```

---

### 3. Repertory Coverage and Remedy Polychrests

The on-device clinical repository contains full keynote profiles, thermal affinities, miasmatic associations, and relationships for major polychrests including:

- **Aconitum Napellus**: Acute, sudden violent onset from dry cold wind, intense anguish, fear of death, bounding pulse, unquenchable cold thirst.
- **Apis Mellifica**: Stinging, burning pains, marked puffiness and edema, thirstlessness, extreme aggravation from heat and hot rooms, amelioration from cold applications.
- **Arsenicum Album**: Prostration out of proportion, midnight aggravation (1–2 AM), burning pains relieved by heat, extreme chilliness, sipping small sips of water, fastidious anxiety.
- **Belladonna**: Violent throbbing, flushed face, hot dry skin, dilated pupils, wild delirium, aggravation from light, noise, touch, and jarring.
- **Bryonia Alba**: Stitching tearing pains, extreme aggravation from the slightest motion, ameliorated by absolute rest and lying on painful side, large thirst at long intervals.
- **Calcarea Carbonica**: Leucophlegmatic constitution, chilly, cold damp feet, head sweats profusely during sleep, craving for boiled eggs and indigestible things, easily fatigued from exertion.
- **Carbo Vegetabilis**: State of collapse, air-hunger demanding to be fanned continuously, cold breath, blueness, sluggish venous system.
- **Causticum**: Sympathetic nature, ailments from grief and injustice, paralytic weakness, involuntary urination when coughing, ameliorated by damp wet weather.
- **Chamomilla**: Unendurable irritability, anger, one cheek red and hot while other is pale and cold, pains accompanied by numbness, weeping demanding to be carried.
- **China Officinalis (Cinchona)**: Exhaustion from loss of vital fluids, extreme sensitivity to light touch yet relieved by hard pressure, periodic intermittent fevers.
- **Gelsemium Sempervirens**: Motor paralysis, dullness, dizziness, drowsiness, trembling from emotional shock or anticipation, complete thirstlessness.
- **Hepar Sulphuris**: Extreme hypersensitivity to cold air and touch, splinter-like pains, suppurative tendency, violent temper, ameliorated in warm moist weather.
- **Ignatia Amara**: Paradoxical symptoms, ailments from acute grief and romantic disappointment, silent brooding with involuntary deep sighing, spasms.
- **Kali Carbonicum**: Stitching pains, aggravation between 2–4 AM, puffiness of upper eyelids, backache demanding hard pressure.
- **Lachesis Mutus**: Left-sided affections moving to right, extreme intolerance to tight clothing around neck and waist, loquacity, aggravation after sleep, suspicious jealousy.
- **Lycopodium Clavatum**: Right-sided complaints moving to left, 4–8 PM aggravation, severe abdominal bloating, craving for warm food/drinks, intellectual yet physically weak.
- **Mercurius Solubilis**: Metallic taste in mouth, copious offensive perspiration that gives no relief, trembling hands, nocturnal bone pains, aggravated by both heat and cold.
- **Natrum Muriaticum**: Ailments from silent grief and betrayal, mapped tongue, bursting headache aggravated by 10 AM to 3 PM sun, craving for salt, aversion to consolation.
- **Nux Vomica**: Sedentary, irritable executive, overindulgence in stimulants/coffee/spices, ineffectual urging for stool, hypersensitive to noise and drafts of cold air.
- **Phosphorus**: Tall slender build, clairvoyant, open, craving for ice-cold drinks, burning along spine and palms, easy hemorrhages, fear of thunderstorms and twilight.
- **Pulsatilla Pratensis**: Mild, yielding disposition, weeps easily, ameliorated by consolation, complete thirstlessness with dry mouth, marked relief in open fresh cool air.
- **Rhus Toxicodendron**: Restlessness, painful stiffness, triangular red tip on tongue, aggravation on beginning motion and in cold wet weather, relief from continued motion and warm applications.
- **Sepia Officinalis**: Indifference to loved ones, sensation of bearing down in pelvic organs, brownish saddle across nose/cheeks, ameliorated by vigorous exercise and violent exertion.
- **Silicea Terra**: Lack of grit and confidence, chilly, deficient assimilation, profuse offensive foot sweat, suppurative tendencies, splinter-like foreign body sensations.
- **Staphysagria**: Ailments from suppressed anger, indignity, wounded honor, trembling with anger, sensation of round ball in forehead, honeymoon cystitis.
- **Sulphur**: Philosophical ragged sovereign, burning heat on vertex and soles of feet at night, sinking sensation in epigastrium at 11 AM, aversion to bathing, skin eruptions.
- **Thuja Occidentalis**: Sycotic hydrogenoid constitution, warty excrescences, fixed ideas (legs made of glass, living animal in abdomen), aggravation from damp cold and 3 AM / 3 PM.

---

### 4. Zero Compromise Quality Checklists

Before any clinical consultation concludes, Similimum AI checks the following gates:
- [x] **Totality Coherence**: Does the prescribed remedy cover the primary mental generals and causations?
- [x] **Inimical Check**: Is the remedy safe and compatible with previous prescriptions?
- [x] **Thermal Accordance**: Does the patient's thermal state (Chilly vs Hot) align with the remedy's pathogenesis?
- [x] **Posology Safety**: Is the potency selected appropriately based on Hahnemann's Organon rules on susceptibility and organic changes?
- [x] **Prognostic Baseline**: Are the baseline complaints documented cleanly so Hering's Law can be tracked during the next visit?

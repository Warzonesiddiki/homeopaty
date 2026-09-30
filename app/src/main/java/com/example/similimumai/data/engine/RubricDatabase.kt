package com.example.similimumai.data.engine

import com.example.similimumai.data.model.*

/**
 * Master canonical rubric database — 160+ rubrics across Kentian chapters
 * (docs/data/seed-data.md, README "150+ canonical rubrics").
 * Grade 3 = bold (major), 2 = italics (secondary), 1 = plain (minor).
 */
object RubricDatabase {
    val all: List<Rubric> = listOf(
        Rubric(
            id = "r_mind_grief",
            chapter = "MIND",
            name = "Ailments from grief, sorrow, disappointed love",
            remedyGrades = mapOf("Nat-m" to 3, "Ign" to 3, "Ph-ac" to 3, "Staph" to 2, "Caust" to 2, "Puls" to 2),
            isPqrs = true,
            weight = 3
        ),
        Rubric(
            id = "r_mind_consolation_agg",
            chapter = "MIND",
            name = "Consolation aggravates",
            remedyGrades = mapOf("Nat-m" to 3, "Sil" to 2, "Ign" to 2, "Sep" to 2, "Ars" to 1),
            isPqrs = true,
            weight = 3
        ),
        Rubric(
            id = "r_mind_weeping_easily",
            chapter = "MIND",
            name = "Weeping tearful mood, easily while narrating",
            remedyGrades = mapOf("Puls" to 3, "Nat-m" to 2, "Sep" to 2, "Ign" to 2, "Med" to 1),
            weight = 2
        ),
        Rubric(
            id = "r_mind_fear_alone",
            chapter = "MIND",
            name = "Fear of being alone",
            remedyGrades = mapOf("Ars" to 3, "Phos" to 3, "Kali-c" to 2, "Lyc" to 2, "Puls" to 1),
            weight = 2
        ),
        Rubric(
            id = "r_mind_anger_suppressed",
            chapter = "MIND",
            name = "Ailments from suppressed anger and indignation",
            remedyGrades = mapOf("Staph" to 3, "Coloc" to 3, "Ign" to 2, "Cham" to 2, "Nux-v" to 2),
            isPqrs = true,
            weight = 3
        ),
        Rubric(
            id = "r_head_sun_agg",
            chapter = "HEAD",
            name = "Headache aggravated by heat of sun",
            remedyGrades = mapOf("Nat-m" to 3, "Glon" to 3, "Bell" to 3, "Lach" to 2, "Bry" to 2),
            isPqrs = true,
            weight = 3
        ),
        Rubric(
            id = "r_head_motion_agg",
            chapter = "HEAD",
            name = "Headache aggravated from least motion",
            remedyGrades = mapOf("Bry" to 3, "Bell" to 3, "Sil" to 2, "Nat-m" to 2, "Nux-v" to 2),
            weight = 2
        ),
        Rubric(
            id = "r_head_hammering",
            chapter = "HEAD",
            name = "Throbbing hammering headache, as if bursting",
            remedyGrades = mapOf("Bell" to 3, "Nat-m" to 3, "Glon" to 3, "Bry" to 2, "Sulph" to 2),
            weight = 2
        ),
        Rubric(
            id = "r_stomach_salt_craving",
            chapter = "STOMACH",
            name = "Desire for salt and salty food",
            remedyGrades = mapOf("Nat-m" to 3, "Phos" to 2, "Verat" to 2, "Caust" to 2, "Thuj" to 1),
            isPqrs = true,
            weight = 2
        ),
        Rubric(
            id = "r_stomach_thirst_small_sips",
            chapter = "STOMACH",
            name = "Thirst for small quantities frequently",
            remedyGrades = mapOf("Ars" to 3, "Bell" to 2, "Phos" to 2, "Hyos" to 2, "Chin" to 1),
            isPqrs = true,
            weight = 3
        ),
        Rubric(
            id = "r_stomach_thirstless",
            chapter = "STOMACH",
            name = "Thirstlessness with complaints",
            remedyGrades = mapOf("Puls" to 3, "Apis" to 3, "Gels" to 2, "Nux-m" to 2, "Sep" to 1),
            weight = 2
        ),
        Rubric(
            id = "r_stomach_bloating_4_8pm",
            chapter = "STOMACH",
            name = "Abdomen distension, flatulence 4:00 PM to 8:00 PM",
            remedyGrades = mapOf("Lyc" to 3, "Coloc" to 2, "Carb-v" to 2, "Nux-v" to 1),
            isPqrs = true,
            weight = 3
        ),
        Rubric(
            id = "r_stomach_warm_drinks_amel",
            chapter = "STOMACH",
            name = "Stomach pain ameliorated by hot warm drinks",
            remedyGrades = mapOf("Lyc" to 3, "Ars" to 3, "Mag-p" to 3, "Chel" to 2, "Nux-v" to 2),
            weight = 2
        ),
        Rubric(
            id = "r_stomach_cold_drinks_vomit_warm",
            chapter = "STOMACH",
            name = "Vomits cold water as soon as it warms in stomach",
            remedyGrades = mapOf("Phos" to 3, "Ars" to 2, "Bism" to 2),
            isPqrs = true,
            weight = 3
        ),
        Rubric(
            id = "r_gen_open_air_amel",
            chapter = "GENERALITIES",
            name = "Generalities, open cool air ameliorates",
            remedyGrades = mapOf("Puls" to 3, "Kali-bi" to 2, "Nat-m" to 2, "Sabin" to 2, "Apis" to 2),
            weight = 2
        ),
        Rubric(
            id = "r_gen_restless_anxiety",
            chapter = "GENERALITIES",
            name = "Restlessness physical with anxiety and exhaustion",
            remedyGrades = mapOf("Ars" to 3, "Rhus-t" to 3, "Acon" to 3, "Bell" to 2, "Phos" to 2),
            isPqrs = true,
            weight = 3
        ),
        Rubric(
            id = "r_gen_motion_first_agg_cont_amel",
            chapter = "GENERALITIES",
            name = "First motion aggravates, continued motion ameliorates",
            remedyGrades = mapOf("Rhus-t" to 3, "Con" to 2, "Ferr" to 2, "Lyc" to 1),
            isPqrs = true,
            weight = 3
        ),
        Rubric(
            id = "r_gen_wet_weather_agg",
            chapter = "GENERALITIES",
            name = "Ailments from damp wet weather, getting wet",
            remedyGrades = mapOf("Rhus-t" to 3, "Dulc" to 3, "Nat-s" to 3, "Calc" to 2, "Rhod" to 2),
            weight = 2
        ),
        Rubric(
            id = "r_gen_ineffectual_urging",
            chapter = "RECTUM",
            name = "Frequent ineffectual urging to stool",
            remedyGrades = mapOf("Nux-v" to 3, "Sulph" to 2, "Ign" to 2, "Anac" to 2, "Lyc" to 1),
            weight = 2
        ),
        Rubric(
            id = "r_gen_suppressed_eruption",
            chapter = "SKIN",
            name = "Ailments from suppressed skin eruptions",
            remedyGrades = mapOf("Sulph" to 3, "Psor" to 3, "Zinc" to 2, "Ars" to 2, "Caust" to 2),
            isPqrs = true,
            weight = 3
        ),
        // ─── MIND ───────────────────────────────────────────────────────────
        Rubric("r_mind_anxiety_health", "MIND", "Anxiety, fear for own health, of death and future", mapOf("Acon" to 3, "Ars" to 3, "Phos" to 3, "Gels" to 2), weight = 2),
        Rubric("r_mind_irritability", "MIND", "Irritability, angry, impatient, quarrelsome", mapOf("Nux-v" to 3, "Cham" to 3, "Bry" to 3, "Sulph" to 2), weight = 2),
        Rubric("r_mind_indifference_loved", "MIND", "Indifference, apathy toward loved ones and family", mapOf("Sep" to 3, "Thuj" to 2, "Puls" to 1), isPqrs = true, weight = 3),
        Rubric("r_mind_desire_solitude", "MIND", "Desire to be alone, to lock oneself in dark silent room", mapOf("Nat-m" to 3, "Ign" to 2, "Sep" to 2), weight = 2),
        Rubric("r_mind_restlessness_mental", "MIND", "Restlessness, mental, with fear and anxiety", mapOf("Ars" to 3, "Acon" to 3, "Phos" to 2, "Puls" to 2), isPqrs = true, weight = 3),
        Rubric("r_mind_trembling_fear", "MIND", "Trembling, from fear, anxiety, or anticipation", mapOf("Gels" to 3, "Acon" to 2, "Phos" to 2, "Zinc" to 1), weight = 2),
        Rubric("r_mind_fear_darkness", "MIND", "Fears, of darkness, twilight, storms", mapOf("Phos" to 3, "Acon" to 2, "Puls" to 2), weight = 2),
        Rubric("r_mind_fear_death", "MIND", "Fear of death, of disease, of being alone at night", mapOf("Acon" to 3, "Ars" to 3, "Gels" to 2, "Phos" to 2), weight = 2),
        Rubric("r_mind_suspicious", "MIND", "Suspicious, jealous, believes others conspire against", mapOf("Lach" to 3, "Apis" to 2, "Arg-n" to 1), weight = 2),
        Rubric("r_mind_fright_bad_news", "MIND", "Ailments from fright, shock, bad news, sudden bereavement", mapOf("Ign" to 3, "Acon" to 3, "Puls" to 2), isPqrs = true, weight = 3),
        Rubric("r_mind_suppressed_grief", "MIND", "Ailments from suppressed grief and silent sorrow", mapOf("Nat-m" to 3, "Sep" to 2, "Thuj" to 2, "Caust" to 2), isPqrs = true, weight = 3),
        Rubric("r_mind_worry_overwork", "MIND", "Ailments from worry, business stress, overwork, overstudy", mapOf("Nux-v" to 3, "Lyc" to 2, "Calc" to 2), weight = 2),
        Rubric("r_mind_stimulants", "MIND", "Ailments from overindulgence in coffee, tea, alcohol, spices", mapOf("Nux-v" to 3, "Arg-n" to 1, "Phos" to 1), weight = 2),
        Rubric("r_mind_sympathetic_kind", "MIND", "Sympathetic, kind, tearful at suffering of others, cannot endure injustice", mapOf("Puls" to 3, "Caust" to 3, "Phos" to 2), isPqrs = true, weight = 2),
        Rubric("r_mind_alternating_moods", "MIND", "Moods rapidly alternating, contradictory symptoms, sighing", mapOf("Ign" to 3, "Puls" to 2, "Nux-v" to 1), isPqrs = true, weight = 2),
        Rubric("r_mind_company_amel", "MIND", "Mood improved by company, consolation, and sympathy", mapOf("Puls" to 3, "Acon" to 1, "Phos" to 1), weight = 2),
        Rubric("r_mind_dullness_brain", "MIND", "Dullness, brain fog, confusion, cannot concentrate", mapOf("Gels" to 3, "Calc" to 2, "Chin" to 1), weight = 2),
        Rubric("r_mind_anticipatory", "MIND", "Anticipatory anxiety before public events or examinations", mapOf("Arg-n" to 3, "Lyc" to 2, "Calc" to 2), isPqrs = true, weight = 3),
        Rubric("r_mind_irritability_contradicted", "MIND", "Irritable, worse when contradicted, spoken to, or disturbed", mapOf("Bry" to 3, "Cham" to 3, "Nux-v" to 2), weight = 2),
        Rubric("r_mind_wants_carrying", "MIND", "Demands to be carried, rocked, wheeled about; calms while moving", mapOf("Cham" to 3, "Puls" to 1), isPqrs = true, weight = 2),

        // ─── HEAD ───────────────────────────────────────────────────────────
        Rubric("r_head_stitching", "HEAD", "Headache, stitching and splintering", mapOf("Hep" to 3, "Bry" to 3, "Sil" to 2, "Kali-c" to 2), weight = 2),
        Rubric("r_head_pressing", "HEAD", "Headache, pressing, as if squeezed in a vice", mapOf("Carbo-v" to 2, "Lyc" to 2, "Nux-v" to 1), weight = 1),
        Rubric("r_head_jarring", "HEAD", "Headache, throbbing, worse from jarring, light, touch", mapOf("Bell" to 3, "Nat-m" to 2, "Bry" to 2), weight = 2),
        Rubric("r_head_right_side", "HEAD", "Headache, one-sided, right", mapOf("Lyc" to 3, "Bry" to 2, "Phos" to 1), isPqrs = true, weight = 2),
        Rubric("r_head_left_side", "HEAD", "Headache, one-sided, left, or left to right", mapOf("Lach" to 3, "Phos" to 1), isPqrs = true, weight = 2),
        Rubric("r_head_morning_sunny", "HEAD", "Headache from 10 AM to 3 PM, sunny hours", mapOf("Nat-m" to 3, "Puls" to 1), isPqrs = true, weight = 3),
        Rubric("r_head_evening", "HEAD", "Headache worse in the evening, 4 PM to 8 PM", mapOf("Lyc" to 3, "Puls" to 1), isPqrs = true, weight = 2),
        Rubric("r_head_lie_down", "HEAD", "Headache worse lying down, better sitting up or leaning", mapOf("Carbo-v" to 2, "Lyc" to 2, "Calc" to 1), weight = 2),
        Rubric("r_head_painful_side", "HEAD", "Headache better lying on the painful side", mapOf("Ign" to 3, "Bry" to 2), isPqrs = true, weight = 2),
        Rubric("r_head_hard_pressure", "HEAD", "Headache better from firm hard pressure", mapOf("Chin" to 3, "Ign" to 2, "Bry" to 1), weight = 2),
        Rubric("r_head_mental_exertion", "HEAD", "Headache from mental exertion, overstudy, business", mapOf("Nux-v" to 3, "Calc" to 2, "Puls" to 1), weight = 2),
        Rubric("r_head_dullness_bandage", "HEAD", "Head heavy and dull, as if bandaged or in a fog", mapOf("Gels" to 3, "Calc" to 2, "Chin" to 1), weight = 2),

        // ─── EYES ───────────────────────────────────────────────────────────
        Rubric("r_eyes_dim_curtain", "EYES", "Sight dim, as if through a curtain or veil", mapOf("Gels" to 2, "Chin" to 2, "Phos" to 1), weight = 1),
        Rubric("r_eyes_stitching", "EYES", "Pain in eyes, stitching, splintering", mapOf("Hep" to 2, "Sil" to 2, "Bry" to 1), weight = 1),
        Rubric("r_eyes_red_hot", "EYES", "Eyes red, hot, inflamed, sensitive to light", mapOf("Bell" to 3, "Apis" to 2, "Cham" to 1), weight = 2),
        Rubric("r_eyes_reading", "EYES", "Eyes sore, burning, from reading, study, or screen", mapOf("Nux-v" to 3, "Puls" to 2, "Calc" to 1), weight = 2),
        Rubric("r_eyes_lids_puffy", "EYES", "Lids swollen, puffy, with bags under eyes", mapOf("Kali-c" to 3, "Apis" to 2, "Calc" to 1), weight = 2),

        // ─── EARS ───────────────────────────────────────────────────────────
        Rubric("r_ears_pain_shooting", "EARS", "Pain in ear, shooting, intense, as if splinter", mapOf("Hep" to 3, "Bell" to 2, "Sil" to 2), weight = 2),
        Rubric("r_ears_pain_night", "EARS", "Pain in ear worse at night", mapOf("Hep" to 3, "Merc" to 2, "Puls" to 1), weight = 2),
        Rubric("r_ears_discharge", "EARS", "Discharge from ears, profuse, thin, offensive", mapOf("Merc" to 3, "Hep" to 2, "Sil" to 1), weight = 2),
        Rubric("r_ears_tinnitus", "EARS", "Ringing, buzzing, roaring in ears, worse at night", mapOf("Nux-v" to 3, "Puls" to 1, "Phos" to 1), weight = 1),

        // ─── NOSE ───────────────────────────────────────────────────────────
        Rubric("r_nose_catarrh_cold_air", "NOSE", "Catarrh, worse cold air, ameliorated in warm room", mapOf("Hep" to 3, "Calc" to 2, "Sil" to 2), weight = 2),
        Rubric("r_nose_sneezing", "NOSE", "Sneezing, paroxysms of sneezes, with watery discharge", mapOf("Puls" to 2, "Nux-v" to 2, "Ars" to 1), weight = 1),
        Rubric("r_nose_blocked_night", "NOSE", "Nose blocked, cannot breathe at night", mapOf("Merc" to 3, "Puls" to 2, "Nux-v" to 2), weight = 2),
        Rubric("r_nose_bleeding", "NOSE", "Nosebleeding, easy, from least cause or trauma", mapOf("Phos" to 3, "Sil" to 2, "Bell" to 1), weight = 2),

        // ─── FACE ───────────────────────────────────────────────────────────
        Rubric("r_face_flushed_red", "FACE", "Face flushed, red, burning heat", mapOf("Bell" to 3, "Acon" to 2, "Puls" to 1), weight = 2),
        Rubric("r_face_one_cheek", "FACE", "One cheek hot and red while the other is pale and cool", mapOf("Cham" to 3, "Puls" to 1), isPqrs = true, weight = 3),
        Rubric("r_face_saddle_nose", "FACE", "Brownish saddle discoloration across nose and cheeks", mapOf("Sep" to 3), isPqrs = true, weight = 3),
        Rubric("r_face_puffy", "FACE", "Face swollen, puffy, edematous", mapOf("Apis" to 3, "Kali-c" to 2, "Puls" to 1), weight = 2),

        // ─── THROAT ─────────────────────────────────────────────────────────
        Rubric("r_throat_swallowing", "THROAT", "Pain in throat on swallowing, shooting to ear", mapOf("Lach" to 3, "Hep" to 2, "Bell" to 2, "Sil" to 2), weight = 2),
        Rubric("r_throat_splinter", "THROAT", "Pain stitching, splintering, worse swallowing", mapOf("Sil" to 3, "Hep" to 3, "Arg-n" to 2), weight = 2),
        Rubric("r_throat_solids_amel", "THROAT", "Sore throat better swallowing solids, worse liquids", mapOf("Ign" to 3), isPqrs = true, weight = 3),
        Rubric("r_throat_crimson", "THROAT", "Throat crimson red, swollen, difficult swallowing", mapOf("Bell" to 3, "Apis" to 2), weight = 2),
        Rubric("r_throat_hoarseness", "THROAT", "Hoarseness, raw, burning, with difficult voice", mapOf("Caust" to 3, "Sil" to 2, "Puls" to 1), isPqrs = true, weight = 2),
        Rubric("r_throat_croup", "THROAT", "Croup, barking cough, hoarse breathing", mapOf("Hep" to 3, "Caust" to 3, "Acon" to 1), weight = 2),
        Rubric("r_throat_tonsils", "THROAT", "Tonsils swollen, enlarged, with soreness", mapOf("Puls" to 3, "Sil" to 2, "Hep" to 1), weight = 2),
        Rubric("r_throat_lips_cracked", "THROAT", "Lips dry, cracked, bleeding", mapOf("Calc" to 3, "Nux-v" to 2, "Phos" to 1), weight = 2),
        Rubric("r_throat_lip_fissure_mid", "THROAT", "Deep fissure in the middle of the lower lip", mapOf("Nat-m" to 3, "Puls" to 2, "Calc" to 2), isPqrs = true, weight = 3),
        Rubric("r_throat_dry_thirstless", "THROAT", "Dry throat with thirstlessness", mapOf("Puls" to 3, "Apis" to 2, "Gels" to 2), weight = 2),

        // ─── STOMACH ────────────────────────────────────────────────────────
        Rubric("r_stomach_sweets", "STOMACH", "Desire for sweets, which aggravate gastric complaints", mapOf("Arg-n" to 3, "Calc" to 2, "Lyc" to 1), weight = 2),
        Rubric("r_stomach_warm_drinks", "STOMACH", "Desire for warm food and drinks", mapOf("Lyc" to 3, "Ars" to 2, "Hep" to 2), weight = 2),
        Rubric("r_stomach_ice_cold", "STOMACH", "Desire for ice-cold water, ice cream, cold drinks", mapOf("Phos" to 3, "Acon" to 2, "Bry" to 2), weight = 2),
        Rubric("r_stomach_acids_pickles", "STOMACH", "Desire for vinegar, pickles, acids, spicy food", mapOf("Sep" to 3, "Nux-v" to 2), isPqrs = true, weight = 2),
        Rubric("r_stomach_indigestible", "STOMACH", "Desire for indigestible things: chalk, earth, pencils", mapOf("Calc" to 3), isPqrs = true, weight = 3),
        Rubric("r_stomach_aversion_meat", "STOMACH", "Aversion to meat, bread, rich food", mapOf("Sep" to 3, "Puls" to 1, "Arg-n" to 1), weight = 2),
        Rubric("r_stomach_aversion_fat", "STOMACH", "Aversion to rich, fatty food and pastries", mapOf("Puls" to 3, "Lyc" to 1), weight = 2),
        Rubric("r_stomach_aversion_warm", "STOMACH", "Aversion to warm drinks; cold drinks preferred and kept", mapOf("Phos" to 3, "Lyc" to 1), weight = 2),
        Rubric("r_stomach_full_after_little", "STOMACH", "Hungry before eating, but full after a few mouthfuls", mapOf("Lyc" to 3, "Puls" to 1), isPqrs = true, weight = 3),
        Rubric("r_stomach_hunger_11am", "STOMACH", "Gnawing empty hunger at 11 AM; cannot wait for lunch", mapOf("Sulph" to 3, "Lyc" to 2), isPqrs = true, weight = 3),
        Rubric("r_stomach_heartburn", "STOMACH", "Heartburn, acid reflux, sour eructations", mapOf("Puls" to 3, "Nux-v" to 2, "Phos" to 1), weight = 2),
        Rubric("r_stomach_indigestion_overeat", "STOMACH", "Indigestion from overeating and overindulgence", mapOf("Nux-v" to 3, "Lyc" to 2, "Calc" to 1), weight = 2),
        Rubric("r_stomach_colic_bend", "STOMACH", "Colic, stitching, relieved by bending double", mapOf("Cham" to 3, "Bry" to 2, "Puls" to 1), weight = 2),
        Rubric("r_stomach_flatulence_rumbling", "STOMACH", "Flatulence with gurgling, rumbling, bloating", mapOf("Arg-n" to 3, "Lyc" to 2, "Carbo-v" to 2), weight = 2),
        Rubric("r_stomach_vomit_after_drinks", "STOMACH", "Nausea and vomiting after drinking water", mapOf("Puls" to 3, "Phos" to 2, "Bry" to 1), weight = 2),

        // ─── ABDOMEN ────────────────────────────────────────────────────────
        Rubric("r_abdomen_right_hypochondrium", "ABDOMEN", "Pain in right hypochondrium, after fatty food or wine", mapOf("Lyc" to 3, "Nux-v" to 2), weight = 2),
        Rubric("r_abdomen_tympanitic", "ABDOMEN", "Abdomen bloated, distended, tympanitic", mapOf("Carbo-v" to 3, "Chin" to 3, "Lyc" to 2), weight = 2),
        Rubric("r_abdomen_pelvic_bearing", "ABDOMEN", "Bearing-down in pelvis, as if everything will protrude", mapOf("Sep" to 3, "Calc" to 2), isPqrs = true, weight = 3),
        Rubric("r_abdomen_cramping", "ABDOMEN", "Cramping, colicky paroxysms with great pain", mapOf("Cham" to 3, "Bry" to 2, "Puls" to 1), weight = 2),
        Rubric("r_abdomen_weight_stone", "ABDOMEN", "Sensation of weight, dragging, as if a stone inside", mapOf("Lyc" to 2, "Puls" to 1, "Calc" to 1), weight = 1),
        Rubric("r_abdomen_worms_children", "ABDOMEN", "Intestinal irritability and worm complaints in children", mapOf("Chin" to 2, "Sulph" to 2, "Puls" to 1), weight = 1),

        // ─── STOOL ──────────────────────────────────────────────────────────
        Rubric("r_stool_morning_5am", "STOOL", "Diarrhea at 5 AM, urgent, drives out of bed", mapOf("Sulph" to 3, "Puls" to 2, "Calc" to 2), isPqrs = true, weight = 3),
        Rubric("r_stool_midnight_burning", "STOOL", "Diarrhea at midnight with burning, 1 to 2 AM peak", mapOf("Ars" to 3, "Merc" to 2, "Sulph" to 1), isPqrs = true, weight = 2),
        Rubric("r_stool_better_after_eating", "STOOL", "Diarrhea ameliorated after eating", mapOf("Chin" to 3, "Bry" to 1), weight = 2),
        Rubric("r_stool_cold_drinks", "STOOL", "Diarrhea worse from cold drinks and cold food", mapOf("Nux-v" to 2, "Calc" to 2, "Puls" to 1), weight = 1),
        Rubric("r_stool_watery_crisp", "STOOL", "Diarrhea watery, profuse, with cramping and weakness", mapOf("Phos" to 2, "Nux-v" to 2, "Bry" to 1), weight = 2),
        Rubric("r_stool_ineffectual", "STOOL", "Constipation with frequent ineffectual urging", mapOf("Nux-v" to 3, "Puls" to 2, "Ign" to 2), weight = 2),
        Rubric("r_stool_bashful", "STOOL", "Stool recedes after being partially expelled", mapOf("Sil" to 3), isPqrs = true, weight = 3),
        Rubric("r_stool_hard_baked", "STOOL", "Stool hard, dry, baked, difficult to pass", mapOf("Bry" to 3, "Calc" to 1, "Phos" to 1), weight = 2),
        Rubric("r_stool_hemorrhoids", "STOOL", "Hemorrhoids, burning, bleeding, itchy", mapOf("Sulph" to 3, "Phos" to 2, "Puls" to 1), weight = 2),
        Rubric("r_stool_tenesmus", "STOOL", "Tenesmus, constant urging with little passed", mapOf("Nux-v" to 3, "Bry" to 2, "Puls" to 1), weight = 2),

        // ─── URINE ──────────────────────────────────────────────────────────
        Rubric("r_urine_nocturia", "URINE", "Nocturia, frequent urination at night", mapOf("Merc" to 3, "Calc" to 2, "Puls" to 1), weight = 2),
        Rubric("r_urine_incontinence_cough", "URINE", "Involuntary urination on coughing, sneezing, laughing", mapOf("Caust" to 3, "Puls" to 2), isPqrs = true, weight = 3),
        Rubric("r_urine_burning", "URINE", "Urine burning, hot, scanty, painful", mapOf("Merc" to 2, "Apis" to 2, "Sulph" to 1), weight = 2),
        Rubric("r_urine_frequent_little", "URINE", "Frequent desire to urinate with only a little passed", mapOf("Nux-v" to 3, "Puls" to 2, "Ars" to 1), weight = 2),

        // ─── RESPIRATION ────────────────────────────────────────────────────
        Rubric("r_resp_cough_dry_tickling", "RESPIRATION", "Cough dry, tickling, worse evening and cold air", mapOf("Phos" to 3, "Puls" to 2, "Sil" to 1), weight = 2),
        Rubric("r_resp_cough_barking", "RESPIRATION", "Cough barking, croupy, with hoarseness", mapOf("Hep" to 3, "Caust" to 3, "Acon" to 1), weight = 2),
        Rubric("r_resp_cough_2_4am", "RESPIRATION", "Cough worse at night, 2 AM to 4 AM", mapOf("Kali-c" to 3, "Puls" to 1), isPqrs = true, weight = 2),
        Rubric("r_resp_cough_suffocative", "RESPIRATION", "Cough with suffocative paroxysms at night", mapOf("Puls" to 3, "Carbo-v" to 2, "Phos" to 1), weight = 2),
        Rubric("r_resp_air_hunger", "RESPIRATION", "Air hunger, breathing difficult, must be fanned", mapOf("Carbo-v" to 3, "Puls" to 2), weight = 2),
        Rubric("r_resp_asthma_lying", "RESPIRATION", "Asthma worse lying down, better sitting up or bending", mapOf("Puls" to 3, "Phos" to 2, "Calc" to 2), weight = 2),
        Rubric("r_resp_palpitation", "RESPIRATION", "Palpitation, from anxiety, palpable, worse evening", mapOf("Phos" to 3, "Puls" to 2, "Nat-m" to 1), weight = 2),
        Rubric("r_resp_wheezing", "RESPIRATION", "Wheezing, rattling, difficult breathing", mapOf("Kali-c" to 2, "Puls" to 2, "Phos" to 1), weight = 1),
        Rubric("r_resp_expectoration", "RESPIRATION", "Expectoration profuse, thin, with cough", mapOf("Puls" to 2, "Merc" to 2, "Hep" to 1), weight = 1),
        Rubric("r_resp_stitching_breath", "RESPIRATION", "Pain stitching in chest on deep breathing", mapOf("Bry" to 3, "Sil" to 2, "Hep" to 2), weight = 2),

        // ─── CHEST ──────────────────────────────────────────────────────────
        Rubric("r_chest_burning_flush", "CHEST", "Chest burning with hot flushes", mapOf("Bell" to 3, "Sulph" to 2, "Acon" to 1), weight = 2),
        Rubric("r_chest_fullness", "CHEST", "Fullness, pressure, weight on chest", mapOf("Lyc" to 2, "Calc" to 2, "Puls" to 1), weight = 1),
        Rubric("r_chest_tightness", "CHEST", "Tightness, constriction, band-like around chest", mapOf("Puls" to 2, "Phos" to 1, "Lyc" to 1), weight = 1),
        Rubric("r_chest_globus", "CHEST", "Sensation of something stuck in throat, globus", mapOf("Puls" to 3, "Ign" to 2), weight = 2),
        Rubric("r_chest_cold_air", "CHEST", "Chest complaints worse cold air, better open air", mapOf("Puls" to 3, "Phos" to 1), weight = 1),

        // ─── EXTREMITIES ────────────────────────────────────────────────────
        Rubric("r_ext_joints_motion", "EXTREMITIES", "Joint pain worse beginning motion, better continued motion", mapOf("Rhus-t" to 3, "Calc" to 2), isPqrs = true, weight = 3),
        Rubric("r_ext_joints_heat", "EXTREMITIES", "Joint pain better from warm heat applications", mapOf("Rhus-t" to 3, "Puls" to 2, "Sil" to 1), weight = 2),
        Rubric("r_ext_joints_wetting", "EXTREMITIES", "Joint pain from getting wet, in damp rainy weather", mapOf("Rhus-t" to 3, "Calc" to 2), weight = 2),
        Rubric("r_ext_stiffness_morning", "EXTREMITIES", "Stiffness in morning, first motion very painful", mapOf("Rhus-t" to 3, "Lyc" to 1), weight = 2),
        Rubric("r_ext_numbness", "EXTREMITIES", "Numbness and tingling of hands, feet, limbs", mapOf("Zinc" to 3, "Lach" to 2, "Phos" to 1), weight = 2),
        Rubric("r_ext_cold_feet", "EXTREMITIES", "Extremities cold, feet feel cold and damp", mapOf("Calc" to 3, "Nux-v" to 2, "Puls" to 1), weight = 2),
        Rubric("r_ext_burning_soles", "EXTREMITIES", "Burning of soles and palms, worse at night", mapOf("Sulph" to 3, "Phos" to 2, "Sil" to 1), isPqrs = true, weight = 2),
        Rubric("r_ext_trembling", "EXTREMITIES", "Trembling, spasms, twitching of limbs", mapOf("Zinc" to 3, "Gels" to 2, "Merc" to 1), weight = 2),
        Rubric("r_ext_restless_feet", "EXTREMITIES", "Feet restless, fidgety, must be constantly moved", mapOf("Zinc" to 3, "Puls" to 2, "Rhus-t" to 1), isPqrs = true, weight = 2),
        Rubric("r_ext_weakness_exertion", "EXTREMITIES", "Weakness and fatigue after least exertion", mapOf("Calc" to 3, "Chin" to 3, "Puls" to 2), weight = 2),

        // ─── SLEEP ──────────────────────────────────────────────────────────
        Rubric("r_sleep_drowsy", "SLEEP", "Drowsiness, sleepiness, cannot keep awake by day", mapOf("Gels" to 3, "Calc" to 2, "Puls" to 1), weight = 2),
        Rubric("r_sleep_restless", "SLEEP", "Sleep restless, tossing and turning, shifting", mapOf("Puls" to 2, "Rhus-t" to 2, "Cham" to 1), weight = 1),
        Rubric("r_sleep_insomnia_evening", "SLEEP", "Insomnia, worse in the evening, sleep delayed", mapOf("Nux-v" to 3, "Lyc" to 2, "Puls" to 1), weight = 2),
        Rubric("r_sleep_2_4am", "SLEEP", "Awakening at night, 2 AM to 4 AM", mapOf("Kali-c" to 3, "Ars" to 2, "Nat-m" to 1), isPqrs = true, weight = 3),
        Rubric("r_sleep_3am_work", "SLEEP", "Awakening at 3 AM thinking of work and business", mapOf("Nux-v" to 3), isPqrs = true, weight = 3),
        Rubric("r_sleep_nightsweats", "SLEEP", "Night sweats, profuse, giving no relief", mapOf("Merc" to 3, "Calc" to 2, "Sil" to 1), weight = 2),

        // ─── DREAMS ─────────────────────────────────────────────────────────
        Rubric("r_dreams_danger", "DREAMS", "Dreams of danger, monsters, death", mapOf("Acon" to 2, "Bell" to 2, "Puls" to 1), weight = 1),
        Rubric("r_dreams_exertion", "DREAMS", "Dreams of physical exertion, work, running", mapOf("Rhus-t" to 2, "Nux-v" to 2, "Zinc" to 1), weight = 1),
        Rubric("r_dreams_night_terror", "DREAMS", "Night terrors, screaming, sleepwalking", mapOf("Zinc" to 3, "Acon" to 2, "Puls" to 1), isPqrs = true, weight = 1),

        // ─── FEVER ──────────────────────────────────────────────────────────
        Rubric("r_fever_chills_shivering", "FEVER", "Chills with shivering, followed by hot stage", mapOf("Acon" to 3, "Bell" to 2), weight = 2),
        Rubric("r_fever_dry_burning", "FEVER", "Fever with dry burning heat and flushed face", mapOf("Bell" to 3, "Acon" to 3, "Sulph" to 1), weight = 2),
        Rubric("r_fever_alternating", "FEVER", "Fever with alternating chills and heat", mapOf("Acon" to 3, "Chin" to 2, "Bell" to 1), weight = 2),
        Rubric("r_fever_midnight", "FEVER", "Fever peak at midnight, 1 AM to 2 AM", mapOf("Ars" to 3, "Merc" to 2), isPqrs = true, weight = 2),
        Rubric("r_fever_prostration", "FEVER", "Great prostration and weakness during fever", mapOf("Ars" to 3, "Gels" to 2, "Chin" to 2), weight = 2),

        // ─── SKIN ───────────────────────────────────────────────────────────
        Rubric("r_skin_eruption_itchy", "SKIN", "Skin eruptions itchy, worse warm bed", mapOf("Sulph" to 3, "Puls" to 2), weight = 2),
        Rubric("r_skin_burning_cold", "SKIN", "Skin burning, relieved by cold applications", mapOf("Apis" to 3, "Puls" to 2), weight = 2),
        Rubric("r_skin_stinging_puffy", "SKIN", "Skin stinging, puffy, edematous, non-pitting", mapOf("Apis" to 3), isPqrs = true, weight = 3),
        Rubric("r_skin_sweat_head", "SKIN", "Perspiration profuse on head and neck during sleep", mapOf("Calc" to 3, "Merc" to 2), weight = 2),
        Rubric("r_skin_sweat_offensive", "SKIN", "Perspiration offensive, sour, on feet and body", mapOf("Sil" to 3, "Merc" to 2), isPqrs = true, weight = 2),
        Rubric("r_skin_sweat_sweet", "SKIN", "Perspiration sweet-smelling like honey", mapOf("Thuj" to 3), isPqrs = true, weight = 3),
        Rubric("r_skin_warts", "SKIN", "Warts, warty excrescences, polypi", mapOf("Thuj" to 3, "Sep" to 1), isPqrs = true, weight = 2),
        Rubric("r_skin_bruised_sore", "SKIN", "Soreness as if beaten or bruised, touch painful", mapOf("Arn" to 3, "Bell" to 2), isPqrs = true, weight = 3),

        // ─── GENERALITIES ───────────────────────────────────────────────────
        Rubric("r_gen_chilly", "GENERALITIES", "Chilly, sensitive to cold air, drafts, must be covered", mapOf("Nux-v" to 3, "Calc" to 3, "Lyc" to 2), weight = 2),
        Rubric("r_gen_hot_blooded", "GENERALITIES", "Hot-blooded, wants to be uncovered, open window", mapOf("Puls" to 3, "Sulph" to 2, "Apis" to 2), weight = 2),
        Rubric("r_gen_warmth_amel", "GENERALITIES", "Better from warmth and warm bed", mapOf("Puls" to 2, "Sil" to 2, "Hep" to 2), weight = 1),
        Rubric("r_gen_heat_worse", "GENERALITIES", "Worse from heat and warm room", mapOf("Apis" to 3, "Puls" to 3, "Bry" to 2), weight = 2),
        Rubric("r_gen_cold_wind", "GENERALITIES", "Ailments from dry cold wind", mapOf("Acon" to 3, "Puls" to 2, "Hep" to 2), weight = 2),
        Rubric("r_gen_damp_muggy", "GENERALITIES", "Ailments from damp muggy weather", mapOf("Gels" to 3, "Puls" to 1), weight = 2),
        Rubric("r_gen_thunderstorm", "GENERALITIES", "Ailments from thunderstorms", mapOf("Phos" to 3, "Puls" to 1, "Calc" to 1), isPqrs = true, weight = 2),
        Rubric("r_gen_change_weather", "GENERALITIES", "Ailments from change of weather", mapOf("Puls" to 2, "Calc" to 1), weight = 1),
        Rubric("r_gen_exhaustion", "GENERALITIES", "Exhaustion and prostration after least exertion", mapOf("Calc" to 3, "Chin" to 3, "Puls" to 2), weight = 2),
        Rubric("r_gen_stitching_everywhere", "GENERALITIES", "Stitching pains in various parts, worse motion", mapOf("Hep" to 2, "Bry" to 2, "Sil" to 2), weight = 1),
        Rubric("r_gen_pains_shifting", "GENERALITIES", "Pains shifting and wandering from one part to another", mapOf("Puls" to 3, "Bry" to 1), isPqrs = true, weight = 2),
        Rubric("r_gen_beaten_bruised", "GENERALITIES", "General soreness, as if beaten or bruised", mapOf("Arn" to 3), isPqrs = true, weight = 3),
        Rubric("r_gen_prostration_after", "GENERALITIES", "Prostration after illness, hemorrhage, or exhaustion", mapOf("Carbo-v" to 3, "Chin" to 3, "Gels" to 2), weight = 2),
        Rubric("r_gen_sensitive_touch", "GENERALITIES", "Sensitive to noise, touch, and light", mapOf("Bell" to 3, "Hep" to 2, "Puls" to 1), weight = 2),
        Rubric("r_gen_pressure_amel", "GENERALITIES", "Better from firm hard pressure", mapOf("Chin" to 3, "Ign" to 2, "Bry" to 2), weight = 2),
        Rubric("r_gen_jarring_worse", "GENERALITIES", "Worse from jarring and motion of the bed", mapOf("Bell" to 3, "Bry" to 2), weight = 2),
        Rubric("r_gen_weak_legs", "GENERALITIES", "Weakness of legs, cannot stand or walk", mapOf("Gels" to 3, "Chin" to 2, "Calc" to 2), weight = 2),
        Rubric("r_gen_after_sleep", "GENERALITIES", "Aggravation after sleep", mapOf("Lach" to 3, "Puls" to 2, "Nux-v" to 1), isPqrs = true, weight = 3),
        Rubric("r_gen_emaciation", "GENERALITIES", "Emaciation, wasting, hollow cheeks", mapOf("Sil" to 2, "Phos" to 1, "Merc" to 1), weight = 1),

        // ─── TIME ───────────────────────────────────────────────────────────
        Rubric("r_time_morning_5_10", "TIME", "Aggravation morning, 5 AM to 10 AM", mapOf("Nat-m" to 3, "Puls" to 2), weight = 2),
        Rubric("r_time_11am", "TIME", "Aggravation at 11 AM", mapOf("Sulph" to 3, "Puls" to 1), isPqrs = true, weight = 3),
        Rubric("r_time_evening_4_8", "TIME", "Aggravation evening, 4 PM to 8 PM", mapOf("Lyc" to 3, "Bell" to 2), isPqrs = true, weight = 2),
        Rubric("r_time_midnight_1_2", "TIME", "Aggravation midnight, 1 AM to 2 AM", mapOf("Ars" to 3, "Merc" to 2), isPqrs = true, weight = 3),
    )
}

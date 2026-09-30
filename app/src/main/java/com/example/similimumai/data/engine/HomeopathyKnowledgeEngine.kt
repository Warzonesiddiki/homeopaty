package com.example.similimumai.data.engine

import com.example.similimumai.data.model.*
import java.util.UUID

object HomeopathyKnowledgeEngine {

    val polychrests: List<Remedy> = listOf(
        Remedy(
            id = "nat_m",
            abbreviation = "Nat-m",
            fullName = "Natrum Muriaticum",
            commonName = "Chloride of Sodium / Sea Salt",
            thermalState = ThermalState.HOT,
            dominantMiasm = Miasm.PSORA,
            keynotes = listOf(
                "Ailments from silent grief, betrayed trust, emotional disappointment",
                "Consolation aggravates weeping; introverted, dwells on past disagreeable events",
                "Headache from sunrise to sunset; hammering throbbing like little hammers",
                "Craving for salt and salty food; deep fissure in the middle of lower lip",
                "Mapped tongue; geographic patches; weakness of limbs on waking in morning"
            ),
            inimicalRemedies = listOf(),
            complementaryRemedies = listOf("Apis", "Sep", "Ign"),
            antidoteRemedies = listOf("Camph", "Phos", "Spir-nit-d")
        ),
        Remedy(
            id = "lyc",
            abbreviation = "Lyc",
            fullName = "Lycopodium Clavatum",
            commonName = "Club Moss",
            thermalState = ThermalState.CHILLY,
            dominantMiasm = Miasm.PSORA,
            keynotes = listOf(
                "Aggravation specifically between 4:00 PM and 8:00 PM",
                "Right-sided symptoms or symptoms migrating from right to left",
                "Severe abdominal bloating; full after eating just a few mouthfuls",
                "Desire for warm food, hot drinks; aversion to cold drinks",
                "Intellectually keen but physically weak; anticipatory anxiety before public events"
            ),
            inimicalRemedies = listOf(),
            complementaryRemedies = listOf("Iod", "Chel", "Puls", "Lach"),
            antidoteRemedies = listOf("Camph", "Puls", "Caust")
        ),
        Remedy(
            id = "ars_alb",
            abbreviation = "Ars",
            fullName = "Arsenicum Album",
            commonName = "White Arsenic",
            thermalState = ThermalState.CHILLY,
            dominantMiasm = Miasm.SYPHILIS,
            keynotes = listOf(
                "Great mental and physical restlessness with rapid prostration and exhaustion",
                "Intense anxiety and fear of death; believes medicine is useless",
                "Burning pains ameliorated by hot applications and warm drinks",
                "Thirst for small sips of water at very frequent intervals",
                "Midnight aggravation: attacks peak between 1:00 AM and 2:00 AM"
            ),
            inimicalRemedies = listOf(),
            complementaryRemedies = listOf("All-s", "Carb-v", "Phos", "Thuj"),
            antidoteRemedies = listOf("Camph", "Chin", "Ferr", "Hep", "Ipec")
        ),
        Remedy(
            id = "phos",
            abbreviation = "Phos",
            fullName = "Phosphorus",
            commonName = "Phosphorus",
            thermalState = ThermalState.CHILLY,
            dominantMiasm = Miasm.TUBERCULAR,
            keynotes = listOf(
                "Tall, slender, narrow-chested, sensitive, highly impressionable, clairvoyant",
                "Craves ice-cold drinks, ice cream, spicy and refreshing food",
                "Cold drinks relieve stomach immediately, but vomited as soon as water warms in stomach",
                "Severe fear of thunderstorms, twilight, being alone, and the dark",
                "Burning sensations in spots, along spine, between scapulae; hemorrhagic tendency"
            ),
            inimicalRemedies = listOf("Caust"), // Inimical to Causticum!
            complementaryRemedies = listOf("Ars", "All-c", "Carb-v", "Lyc"),
            antidoteRemedies = listOf("Camph", "Coff", "Nux-v")
        ),
        Remedy(
            id = "puls",
            abbreviation = "Puls",
            fullName = "Pulsatilla Pratensis",
            commonName = "Wind Flower",
            thermalState = ThermalState.HOT,
            dominantMiasm = Miasm.PSORA,
            keynotes = listOf(
                "Mild, gentle, yielding, tearful disposition; weeps easily while narrating complaints",
                "Thirstlessness in almost all complaints, even with dry mouth",
                "All symptoms ameliorated in open fresh cool air; worse in warm stuffy rooms",
                "Symptoms ever changing and wandering (pains shift rapidly from one joint to another)",
                "Craves consolation and sympathy; rich fatty foods, pastries, and pork aggravate"
            ),
            inimicalRemedies = listOf(),
            complementaryRemedies = listOf("Lyc", "Sil", "Kali-bi", "Kali-s"),
            antidoteRemedies = listOf("Cham", "Coff", "Ign", "Nux-v")
        ),
        Remedy(
            id = "nux_v",
            abbreviation = "Nux-v",
            fullName = "Nux Vomica",
            commonName = "Poison Nut",
            thermalState = ThermalState.CHILLY,
            dominantMiasm = Miasm.PSORA,
            keynotes = listOf(
                "Sedentary lifestyle, high-stress executive, impatient, irritable, quarrelsome",
                "Ailments from over-study, business worry, stimulants, alcohol, coffee, spices",
                "Frequent ineffectual urging for stool and urination; passes small quantity with partial relief",
                "Extremely chilly; sensitive to drafts of cold air; cannot uncover without shivering",
                "Morning aggravation; wakes at 3:00 AM thinking of work, sleeps late and wakes tired"
            ),
            inimicalRemedies = listOf("Ign"),
            complementaryRemedies = listOf("Sulph", "Sep", "Kali-c"),
            antidoteRemedies = listOf("Acon", "Bell", "Camph", "Cham")
        ),
        Remedy(
            id = "sulph",
            abbreviation = "Sulph",
            fullName = "Sulphur",
            commonName = "Sublimed Sulphur",
            thermalState = ThermalState.HOT,
            dominantMiasm = Miasm.PSORA,
            keynotes = listOf(
                "The great antipsoric; philosophical, ragged theorist; aversion to bathing and washing",
                "Burning sensations everywhere: soles of feet stuck out of covers in bed, crown of head",
                "Weak, empty, faint goneness in epigastrium at 11:00 AM; cannot wait for lunch",
                "Morning diarrhea drives out of bed at 5:00 AM with urgency",
                "Skin eruptions itchy, voluptuous scratch, burns after scratching; worse warm bed"
            ),
            inimicalRemedies = listOf(),
            complementaryRemedies = listOf("Aloe", "Psor", "Acon", "Nux-v"),
            antidoteRemedies = listOf("Acon", "Camph", "Cham", "Chin", "Merc")
        ),
        Remedy(
            id = "calc_c",
            abbreviation = "Calc",
            fullName = "Calcarea Carbonica",
            commonName = "Oyster Shell Carbonate of Lime",
            thermalState = ThermalState.CHILLY,
            dominantMiasm = Miasm.PSORA,
            keynotes = listOf(
                "Fair, fat, flabby, perspires profusely, especially on back of neck and head during sleep",
                "Extremely chilly, cold damp feet feel like wet stockings; worse cold wet weather",
                "Craves boiled eggs, sweets, indigestible things (chalk, pencils, earth); milk disagrees",
                "Great apprehension of losing mind or that people will observe mental confusion",
                "Physical exertion easily causes shortness of breath and profuse exhaustion"
            ),
            inimicalRemedies = listOf(),
            complementaryRemedies = listOf("Bell", "Rhus-t", "Lyc", "Sil"),
            antidoteRemedies = listOf("Camph", "Ip", "Nit-ac", "Nux-v")
        ),
        Remedy(
            id = "apis",
            abbreviation = "Apis",
            fullName = "Apis Mellifica",
            commonName = "Honey Bee",
            thermalState = ThermalState.HOT,
            dominantMiasm = Miasm.SYCOSIS,
            keynotes = listOf(
                "Stinging, burning, pricking pains with acute edematous, puffy, rosy swelling",
                "Complete thirstlessness in acute dropsical conditions, cystitis, and fevers",
                "Aggravated by all forms of heat, warm room, fire; ameliorated by cold water applications",
                "Extreme jealousy, awkwardness, drops things from hands; high-pitched cri encéphalique in sleep"
            ),
            inimicalRemedies = listOf("Rhus-t"), // STRICTLY INIMICAL TO RHUS TOX!
            complementaryRemedies = listOf("Nat-m", "Bar-c", "Puls"),
            antidoteRemedies = listOf("Canth", "Ipec", "Lach", "Led")
        ),
        Remedy(
            id = "rhus_t",
            abbreviation = "Rhus-t",
            fullName = "Rhus Toxicodendron",
            commonName = "Poison Ivy",
            thermalState = ThermalState.CHILLY,
            dominantMiasm = Miasm.PSORA,
            keynotes = listOf(
                "Ailments from getting wet while perspiring, sprains, over-lifting, sleeping on damp ground",
                "Great physical restlessness; cannot lie still; must constantly shift position",
                "First motion is extremely painful and stiff; continued gentle motion brings relief",
                "Aggravated in damp rainy cold weather, rest, and beginning of movement; amel warm dry heat",
                "Red triangular tip of tongue; metallic taste; dreams of intense physical exertion"
            ),
            inimicalRemedies = listOf("Apis"), // STRICTLY INIMICAL TO APIS!
            complementaryRemedies = listOf("Bry", "Calc", "Med", "Phyt"),
            antidoteRemedies = listOf("Anac", "Bell", "Bry", "Camph", "Graph")
        ),
        Remedy(
            id = "bry",
            abbreviation = "Bry",
            fullName = "Bryonia Alba",
            commonName = "White Bryony",
            thermalState = ThermalState.HOT,
            dominantMiasm = Miasm.PSORA,
            keynotes = listOf(
                "Aggravation from the least motion; absolute relief from complete absolute rest",
                "Ameliorated by firm pressure and lying on the painful side",
                "Excessive dryness of all mucous membranes; lips parched cracked, dry hard burnt stools",
                "Thirst for large quantities of cold water at long intervals",
                "Irritable, business delirium; talks constantly of his business during fever"
            ),
            inimicalRemedies = listOf(),
            complementaryRemedies = listOf("Alum", "Rhus-t", "Kali-c"),
            antidoteRemedies = listOf("Acon", "Camph", "Cham", "Ign", "Nux-v")
        ),
        Remedy(
            id = "sep",
            abbreviation = "Sep",
            fullName = "Sepia Succus",
            commonName = "Inky Juice of Cuttlefish",
            thermalState = ThermalState.CHILLY,
            dominantMiasm = Miasm.SYCOSIS,
            keynotes = listOf(
                "Complete emotional indifference and apathy to loved ones, family, occupation",
                "Bearing-down sensation in pelvis as if everything would protrude; must cross legs",
                "Yellow saddle across bridge of nose and cheeks (chloasma)",
                "Aversion to meat, bread, and fat; desire for vinegar, pickles, acids, spicy food",
                "Ameliorated by vigorous violent physical exercise (aerobics, fast dancing)"
            ),
            inimicalRemedies = listOf("Lach"),
            complementaryRemedies = listOf("Nat-m", "Puls", "Sulph"),
            antidoteRemedies = listOf("Acon", "Ant-c", "Ant-t", "Bell")
        ),
        Remedy(
            id = "sil",
            abbreviation = "Sil",
            fullName = "Silicea Terra",
            commonName = "Pure Flint",
            thermalState = ThermalState.CHILLY,
            dominantMiasm = Miasm.SYPHILIS,
            keynotes = listOf(
                "Lack of grit, timid, yields easily, dreads failure, yet capable when challenged",
                "Extreme chilliness, wants warm wraps around head; aversion to drafts",
                "Offensive, sour, acrid foot sweat with suppressed perspiration causing complaints",
                "Promotes expulsion of foreign bodies, splinters, ingrown nails, deep fistulae",
                "Constipation: stool recedes after being partially expelled (bashful stool)"
            ),
            inimicalRemedies = listOf("Merc"), // INIMICAL TO MERCURIUS!
            complementaryRemedies = listOf("Thuj", "Sanic", "Flu-ac"),
            antidoteRemedies = listOf("Camph", "Fl-ac", "Hep")
        ),
        Remedy(
            id = "ign",
            abbreviation = "Ign",
            fullName = "Ignatia Amara",
            commonName = "St. Ignatius Bean",
            thermalState = ThermalState.AMBITHERMAL,
            dominantMiasm = Miasm.PSORA,
            keynotes = listOf(
                "Acute emotional grief, heartbreak, bad news, sudden bereavement",
                "Rapidly alternating moods, contradictory symptoms; tears turn to laughter",
                "Frequent involuntary deep sighing; sensation of a lump in throat (globus hystericus)",
                "Sore throat better swallowing solids; headache relieved by lying on painful side",
                "Cannot tolerate tobacco smoke or coffee"
            ),
            inimicalRemedies = listOf("Coff", "Nux-v", "Tab"),
            complementaryRemedies = listOf("Nat-m", "Sep", "Zinc"),
            antidoteRemedies = listOf("Arn", "Camph", "Cham", "Cocc", "Puls")
        ),
        Remedy(
            id = "lach",
            abbreviation = "Lach",
            fullName = "Lachesis Mutus",
            commonName = "Surukuku Bushmaster Snake Venom",
            thermalState = ThermalState.HOT,
            dominantMiasm = Miasm.SYPHILIS,
            keynotes = listOf(
                "Left-sided affections or beginning on left side and spreading to right",
                "Extreme intolerance of constricting collars, tight neck bands, waistband",
                "Aggravation after sleep; sleeps into aggravation; wakes with suffocative paroxysms",
                "Tremendous loquacity, jumps rapidly from one topic to another without pause",
                "Purplish, bluish, dark mottled appearance of affected tissues and throat"
            ),
            inimicalRemedies = listOf("Sep", "Dulc", "Psor"),
            complementaryRemedies = listOf("Lyc", "Hep", "Nit-ac"),
            antidoteRemedies = listOf("Alum", "Ars", "Bell", "Camph")
        ),
        Remedy(
            id = "caust",
            abbreviation = "Caust",
            fullName = "Causticum Hahnemanni",
            commonName = "Tinctura acris sine Kali",
            thermalState = ThermalState.CHILLY,
            dominantMiasm = Miasm.SYCOSIS,
            keynotes = listOf(
                "Intense empathy for the suffering of others; cannot endure injustice or tyranny",
                "Paralytic weakness of single organs (facial nerve, vocal cords, bladder sphincter)",
                "Involuntary urination while coughing, sneezing, laughing, or blowing nose",
                "Ameliorated in damp wet rainy weather; worse in clear fine dry cold weather",
                "Raw burning soreness in trachea with difficult hoarseness"
            ),
            inimicalRemedies = listOf("Phos", "Coff"), // INIMICAL TO PHOSPHORUS!
            complementaryRemedies = listOf("Carb-v", "Petros", "Staph"),
            antidoteRemedies = listOf("Asaf", "Coff", "Coloc", "Nux-v", "Spir-nit-d")
        ),
        Remedy(
            id = "merc_sol",
            abbreviation = "Merc",
            fullName = "Mercurius Solubilis",
            commonName = "Quicksilver / Hahnemann's Soluble Mercury",
            thermalState = ThermalState.AMBITHERMAL,
            dominantMiasm = Miasm.SYPHILIS,
            keynotes = listOf(
                "Human thermometer: sensitive to both extreme cold and extreme heat",
                "Profuse, oily, nocturnal perspiration that affords no relief whatsoever",
                "Flabby, swollen tongue showing deep indents of teeth on lateral borders",
                "Tremendous salivation with metallic sweetish taste and offensive fetid breath",
                "Nocturnal bone pains, bone suppuration, ulcerations with ragged edges"
            ),
            inimicalRemedies = listOf("Sil"), // INIMICAL TO SILICEA!
            complementaryRemedies = listOf("Bad", "Bell", "Hep"),
            antidoteRemedies = listOf("Aur", "Bell", "Camph", "Chin", "Dulc", "Hep", "Iod", "Nit-ac", "Staph", "Sulph")
        ),
        Remedy(
            id = "thuj",
            abbreviation = "Thuj",
            fullName = "Thuja Occidentalis",
            commonName = "Arbor Vitae / Tree of Life",
            thermalState = ThermalState.CHILLY,
            dominantMiasm = Miasm.SYCOSIS,
            keynotes = listOf(
                "The king of antisycotics; fig-warts, condylomata, polypi, excrescences",
                "Ailments from suppressed gonorrhea, bad effects of vaccination (vaccinosis)",
                "Delusion of limbs made of glass that would break easily; something alive in abdomen",
                "Perspiration sweet-smelling like honey, exclusively on uncovered parts",
                "Splitting headache like a nail driven into parietal bone"
            ),
            inimicalRemedies = listOf(),
            complementaryRemedies = listOf("Med", "Nat-s", "Sil", "Sabin"),
            antidoteRemedies = listOf("Cham", "Cocc", "Merc", "Puls", "Sulph")
        ),
        Remedy(
            id = "staph",
            abbreviation = "Staph",
            fullName = "Staphysagria",
            commonName = "Stavesacre",
            thermalState = ThermalState.CHILLY,
            dominantMiasm = Miasm.SYCOSIS,
            keynotes = listOf(
                "Ailments from suppressed indignation, mortification, insult, suppressed anger",
                "Trembling with anger; throws things at people or suppresses fury with trembling",
                "Honeymoon cystitis: burning in urethra when not urinating; sensation of drop rolling",
                "Styes on eyelids leaving hard chalazions; crumbling carious teeth with black rims",
                "Surgical incised wounds with intense cutting stitching pain"
            ),
            inimicalRemedies = listOf(),
            complementaryRemedies = listOf("Caust", "Coloc"),
            antidoteRemedies = listOf("Camph")
        ),
        Remedy(
            id = "bell",
            abbreviation = "Bell",
            fullName = "Belladonna",
            commonName = "Deadly Nightshade",
            thermalState = ThermalState.HOT,
            dominantMiasm = Miasm.PSORA,
            keynotes = listOf(
                "Sudden, violent, stormy onset of symptoms; acute congestions and high pyrexia",
                "Flushed red face, throbbing carotids, dilated pupils, glazed staring eyes",
                "Throbbing hammering headache, worse jarring of bed, touch, bright light, draft",
                "Throat crimson red, swollen, swallowing difficult, worse right side",
                "Hallucinations of monsters, black dogs, strikes and bites attendants"
            ),
            inimicalRemedies = listOf(),
            complementaryRemedies = listOf("Calc", "Hep", "Merc"),
            antidoteRemedies = listOf("Camph", "Coff", "Hep", "Hyos", "Op", "Puls")
        )
    )

    // Master Canonical Rubrics Database
    val rubrics: List<Rubric> = listOf(
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
        )
    )

    // Doctor Silent Observation Presets
    val doctorObservationOptions: List<DoctorObservationOption> = listOf(
        DoctorObservationOption("obs_weep", "Weeping while narrating", "Weeping tearful mood, easily while narrating", "MIND", true),
        DoctorObservationOption("obs_restless", "Restless shifting in chair", "Restlessness physical with anxiety", "GENERALITIES", true),
        DoctorObservationOption("obs_sigh", "Frequent deep sighing", "Sighing involuntary, frequent", "RESPIRATION", true),
        DoctorObservationOption("obs_lip_crack", "Deep middle lower lip fissure", "Cracked lower lip in middle", "FACE", true),
        DoctorObservationOption("obs_tongue_red_tip", "Red triangular tip of tongue", "Red triangular tip of tongue", "MOUTH", true),
        DoctorObservationOption("obs_tongue_imprints", "Teeth indents on tongue edge", "Swollen tongue showing teeth imprints", "MOUTH", false),
        DoctorObservationOption("obs_tight_neck", "Loosens shirt collar / throat", "Intolerance of tight clothing around neck", "THROAT", true),
        DoctorObservationOption("obs_bashful_eye", "Avoids direct eye contact", "Timid, bashful, yielding", "MIND", false)
    )

    // Emergency Red-Flag Evaluator
    fun screenForRedFlags(text: String): RedFlagAlert? {
        val lower = text.lowercase()
        return when {
            (lower.contains("chest pain") || lower.contains("chhati me dard")) &&
                    (lower.contains("left arm") || lower.contains("sweat") || lower.contains("jaw") || lower.contains("breathless")) -> {
                RedFlagAlert(
                    id = "rf_acs",
                    condition = "Possible Acute Coronary Syndrome (Myocardial Infarction)",
                    matchedSymptoms = listOf("Chest pain with radiating pain / autonomic signs"),
                    urgencyLevel = UrgencyLevel.EMERGENCY,
                    immediateAction = "Refer to Emergency Room / Cardiology immediately. Administer oxygen and emergency protocols."
                )
            }
            (lower.contains("worst headache") || lower.contains("thunderclap") || lower.contains("sudden severe head")) &&
                    (lower.contains("vomit") || lower.contains("neck stiff") || lower.contains("stiffness")) -> {
                RedFlagAlert(
                    id = "rf_sah",
                    condition = "Suspected Subarachnoid Hemorrhage / Acute Meningitis",
                    matchedSymptoms = listOf("Sudden severe thunderclap headache with meningeal irritation"),
                    urgencyLevel = UrgencyLevel.EMERGENCY,
                    immediateAction = "Immediate emergency neuro-imaging (CT scan) and hospital admission required."
                )
            }
            lower.contains("numbness in groin") || lower.contains("saddle") || lower.contains("loss of bowel") || lower.contains("urine incontin") -> {
                RedFlagAlert(
                    id = "rf_ces",
                    condition = "Suspected Cauda Equina Syndrome",
                    matchedSymptoms = listOf("Saddle anesthesia with acute sphincter disturbance"),
                    urgencyLevel = UrgencyLevel.EMERGENCY,
                    immediateAction = "Surgical emergency: Immediate MRI lumbar spine and neurosurgical consultation within 24 hours."
                )
            }
            lower.contains("facial droop") || lower.contains("arm weakness") || lower.contains("slurred speech") || lower.contains("one side weak") -> {
                RedFlagAlert(
                    id = "rf_cva",
                    condition = "Acute Cerebrovascular Event (Stroke / CVA)",
                    matchedSymptoms = listOf("F.A.S.T. stroke signs detected"),
                    urgencyLevel = UrgencyLevel.EMERGENCY,
                    immediateAction = "Call Emergency Services (Code Stroke). Transfer to nearest stroke-ready hospital immediately."
                )
            }
            else -> null
        }
    }

    // Natural Language / Vernacular Utterance to LSMC Symptom & Rubric Parser
    fun parseUtteranceToSymptom(utterance: String): Symptom {
        val lower = utterance.lowercase()
        var loc = ""
        var sens = ""
        var mod = ""
        var conc = ""
        var matchedRubricId = ""
        var isPqrs = false

        // Detect Location
        when {
            lower.contains("head") || lower.contains("sar") || lower.contains("forehead") || lower.contains("temple") -> loc = "Head (Cephalic)"
            lower.contains("stomach") || lower.contains("pet") || lower.contains("abdomen") || lower.contains("tummy") -> loc = "Stomach & Abdomen"
            lower.contains("throat") || lower.contains("gala") -> loc = "Throat (Pharynx/Larynx)"
            lower.contains("joint") || lower.contains("knee") || lower.contains("jodo") || lower.contains("shoulder") || lower.contains("back") -> loc = "Extremities & Joints"
            lower.contains("chest") || lower.contains("lung") || lower.contains("cough") || lower.contains("khasi") -> loc = "Chest & Respiratory"
            lower.contains("mind") || lower.contains("mood") || lower.contains("grief") || lower.contains("anger") || lower.contains("anxiety") || lower.contains("gussa") || lower.contains("rona") -> loc = "Mind & Disposition"
            else -> loc = "General Body"
        }

        // Detect Sensation
        when {
            lower.contains("throbbing") || lower.contains("hammering") || lower.contains("dhadak") || lower.contains("fatne") -> sens = "Throbbing, Hammering, Bursting"
            lower.contains("burning") || lower.contains("jalan") || lower.contains("hot") -> sens = "Burning intense heat"
            lower.contains("bloating") || lower.contains("gas") || lower.contains("heavy") || lower.contains("full") -> sens = "Excessive distension & flatulent bloating"
            lower.contains("stiff") || lower.contains("pain") || lower.contains("dard") -> sens = "Aching stiffness and soreness"
            lower.contains("grief") || lower.contains("sad") || lower.contains("cry") || lower.contains("rona") -> sens = "Silent sorrow, despair, easy weeping"
            else -> sens = "Unspecified discomfort"
        }

        // Detect Modalities
        when {
            lower.contains("sun") || lower.contains("dhoop") || lower.contains("heat") -> {
                mod = "Aggravated by exposure to Sun / Heat (< sun)"
                matchedRubricId = "r_head_sun_agg"
                isPqrs = true
            }
            lower.contains("4 to 8") || lower.contains("4-8") || lower.contains("evening") || lower.contains("shaam") -> {
                mod = "Aggravated strictly between 4:00 PM and 8:00 PM (< 4-8 PM)"
                matchedRubricId = "r_stomach_bloating_4_8pm"
                isPqrs = true
            }
            lower.contains("warm drink") || lower.contains("hot water") || lower.contains("garam paani") -> {
                mod = "Ameliorated by warm drinks and hot applications (> warm drinks)"
                matchedRubricId = "r_stomach_warm_drinks_amel"
            }
            lower.contains("open air") || lower.contains("thandi hawa") || lower.contains("fresh air") -> {
                mod = "Ameliorated in cool open air, worse in warm room (> open air)"
                matchedRubricId = "r_gen_open_air_amel"
            }
            lower.contains("motion") && lower.contains("first") -> {
                mod = "Aggravated at beginning of motion, relieved on continued motion (< first motion, > continued)"
                matchedRubricId = "r_gen_motion_first_agg_cont_amel"
                isPqrs = true
            }
            lower.contains("wet") || lower.contains("rain") || lower.contains("bheegh") || lower.contains("damp") -> {
                mod = "Aggravated by getting wet in rain / damp weather (< damp wet)"
                matchedRubricId = "r_gen_wet_weather_agg"
            }
            lower.contains("consol") || lower.contains("samjhane") -> {
                mod = "Aggravated by consolation or sympathy (< consolation)"
                matchedRubricId = "r_mind_consolation_agg"
                isPqrs = true
            }
            lower.contains("grief") || lower.contains("anger") || lower.contains("gusse") -> {
                mod = "Causation: Ailments from emotional trauma / suppressed anger"
                matchedRubricId = if (lower.contains("anger") || lower.contains("gusse")) "r_mind_anger_suppressed" else "r_mind_grief"
                isPqrs = true
            }
        }

        // Detect Concomitants
        when {
            lower.contains("salt") || lower.contains("namak") -> {
                conc = "Craving for intense salty food / middle lower lip crack"
                if (matchedRubricId.isEmpty()) matchedRubricId = "r_stomach_salt_craving"
                isPqrs = true
            }
            lower.contains("small sip") || (lower.contains("thoda") && lower.contains("paani")) || lower.contains("frequent sip") -> {
                conc = "Intense thirst for small sips of water at frequent intervals"
                matchedRubricId = "r_stomach_thirst_small_sips"
                isPqrs = true
            }
            lower.contains("no thirst") || lower.contains("pyaas nahi") || lower.contains("thirstless") -> {
                conc = "Complete absence of thirst with dry mouth"
                if (matchedRubricId.isEmpty()) matchedRubricId = "r_stomach_thirstless"
            }
            lower.contains("cold water") && lower.contains("vomit") -> {
                conc = "Craves ice water, vomited as soon as warm in stomach"
                matchedRubricId = "r_stomach_cold_drinks_vomit_warm"
                isPqrs = true
            }
            lower.contains("restless") || lower.contains("bechaini") -> {
                conc = "Restlessness with intense anxiety and weakness"
                if (matchedRubricId.isEmpty()) matchedRubricId = "r_gen_restless_anxiety"
            }
        }

        // Calculate LSMC Completeness (Location 25%, Sensation 25%, Modality 25%, Concomitant 25%)
        var comp = 0
        if (loc.isNotBlank()) comp += 25
        if (sens.isNotBlank() && sens != "Unspecified discomfort") comp += 25
        if (mod.isNotBlank()) comp += 25
        if (conc.isNotBlank()) comp += 25

        val finalRubric = rubrics.find { it.id == matchedRubricId }?.name ?: ""

        return Symptom(
            id = UUID.randomUUID().toString(),
            location = loc,
            sensation = sens,
            modalities = mod,
            concomitants = conc,
            intensity = if (isPqrs) 3 else 2,
            isPqrs = isPqrs,
            rawUtterance = utterance,
            canonicalRubric = finalRubric,
            completenessScore = comp
        )
    }

    // High-Yield Question Generator for Incomplete Symptoms
    fun generateHighYieldQuestions(symptoms: List<Symptom>): List<HighYieldQuestion> {
        val questions = mutableListOf<HighYieldQuestion>()

        // Check for missing modalities
        val missingMod = symptoms.any { it.modalities.isBlank() }
        if (missingMod) {
            questions.add(
                HighYieldQuestion(
                    id = "q_mod_thermal",
                    question = "How does temperature affect your discomfort? Does cool fresh air soothe it, or do warm applications and wrapping up help?",
                    targetDimension = "Modalities (< / >)",
                    clinicalRationale = "Differentiates warm-blooded polychrests (Puls, Nat-m, Apis) from chilly polychrests (Ars, Lyc, Rhus-t, Calc)."
                )
            )
            questions.add(
                HighYieldQuestion(
                    id = "q_mod_time",
                    question = "At what specific time of day or night does the problem flare up or peak?",
                    targetDimension = "Time Modality",
                    clinicalRationale = "Detects critical clock keynotes: 4–8 PM (Lycopodium), Midnight 1–2 AM (Arsenicum), 11 AM (Sulphur)."
                )
            )
        }

        // Check for missing concomitants / thirst / appetite
        val missingConc = symptoms.any { it.concomitants.isBlank() }
        if (missingConc) {
            questions.add(
                HighYieldQuestion(
                    id = "q_thirst",
                    question = "During this illness, how is your thirst? Do you crave large gulps, small frequent sips, or do you have zero thirst?",
                    targetDimension = "Concomitant Physical Generals",
                    clinicalRationale = "Vital Hahnemannian physical general distinguishing Ars (small frequent sips) vs Bry (large gulps) vs Puls/Apis (thirstless)."
                )
            )
            questions.add(
                HighYieldQuestion(
                    id = "q_emotional_causation",
                    question = "Before this complaint began, was there any significant grief, anger, disappointment, or shock in your life?",
                    targetDimension = "Etiology (§153 PQRS)",
                    clinicalRationale = "Ailments from emotional mortification or silent grief uncovers high-grade constitutional polychrests (Nat-m, Staph, Ign)."
                )
            )
        }

        // Always provide at least one deep constitutional question
        questions.add(
            HighYieldQuestion(
                id = "q_cravings",
                question = "What distinct foods or flavors do you intensely crave or strongly dislike (salt, sweets, spicy, fatty food)?",
                targetDimension = "Constitutional Generals",
                clinicalRationale = "Strong cravings reflect deep metabolic and miasmatic diathesis (Nat-m: salt, Lyc: sweets/warm, Puls: avers fat)."
            )
        )

        return questions.take(3)
    }

    // Dynamic Repertorization Matrix Calculation
    fun calculateRepertorization(
        activeRubrics: List<Rubric>,
        school: RepertorySchool = RepertorySchool.KENT,
        eliminateThermal: ThermalState? = null
    ): List<RemedyScore> {
        val scores = mutableListOf<RemedyScore>()

        for (remedy in polychrests) {
            // Apply thermal elimination filter if active
            if (eliminateThermal != null && eliminateThermal != ThermalState.AMBITHERMAL) {
                if (remedy.thermalState != ThermalState.AMBITHERMAL && remedy.thermalState != eliminateThermal) {
                    continue // Disqualified by elimination rubric
                }
            }

            var totalWeightedScore = 0
            var rubricsCovered = 0
            var gradeSum = 0

            for (rubric in activeRubrics) {
                val grade = rubric.remedyGrades[remedy.abbreviation] ?: 0
                if (grade > 0) {
                    rubricsCovered++
                    gradeSum += grade

                    // School Multiplier logic
                    val schoolMultiplier = when (school) {
                        RepertorySchool.KENT -> {
                            when (rubric.chapter) {
                                "MIND" -> 3.0
                                "GENERALITIES" -> 2.0
                                else -> 1.0
                            }
                        }
                        RepertorySchool.BOENNINGHAUSEN -> {
                            if (rubric.name.contains("ameliorat", ignoreCase = true) || rubric.name.contains("aggravat", ignoreCase = true)) {
                                2.5
                            } else 1.5
                        }
                        RepertorySchool.BOGER -> {
                            if (rubric.isPqrs) 3.0 else 1.5
                        }
                    }

                    val rubricContribution = (grade * rubric.weight * schoolMultiplier).toInt()
                    totalWeightedScore += rubricContribution
                }
            }

            scores.add(
                RemedyScore(
                    remedy = remedy,
                    totalScore = totalWeightedScore,
                    rubricsCovered = rubricsCovered,
                    totalRubrics = activeRubrics.size,
                    gradeSum = gradeSum
                )
            )
        }

        // Rank by totalScore descending, then by rubricsCovered descending
        return scores.sortedWith(
            compareByDescending<RemedyScore> { it.totalScore }
                .thenByDescending { it.rubricsCovered }
        )
    }

    // Inimical Drug Conflict Checker
    fun checkInimicalConflict(primaryRemedy: Remedy, candidateRemedy: Remedy): Pair<Boolean, String> {
        if (primaryRemedy.inimicalRemedies.contains(candidateRemedy.abbreviation) ||
            candidateRemedy.inimicalRemedies.contains(primaryRemedy.abbreviation)) {
            val reason = "STRICTLY INIMICAL: ${primaryRemedy.abbreviation} and ${candidateRemedy.abbreviation} must NEVER be prescribed in succession or combined! They act as hostile antidotes that trigger violent physiological agitation and vital suppression."
            return Pair(true, reason)
        }
        return Pair(false, "")
    }

    // Evaluate Hering's Law of Cure
    fun evaluateHeringProgression(
        insideToOutside: Boolean,
        aboveDownwards: Boolean,
        moreVitalToLess: Boolean,
        reverseOrder: Boolean
    ): HeringEvaluation {
        val positiveVectors = listOf(insideToOutside, aboveDownwards, moreVitalToLess, reverseOrder).count { it }

        return when {
            positiveVectors >= 3 -> HeringEvaluation(
                insideToOutside = insideToOutside,
                aboveDownwards = aboveDownwards,
                moreVitalToLess = moreVitalToLess,
                reverseOrderOfTime = reverseOrder,
                prognosisVerdict = "True Hahnemannian Cure in Progress (Hering's Law Validated)",
                clinicalGuidance = "Vital force is successfully externalizing disease from internal vital organs toward periphery. Do NOT alter remedy or repeat dose while improvement continues (§245)."
            )
            positiveVectors == 2 -> HeringEvaluation(
                insideToOutside = insideToOutside,
                aboveDownwards = aboveDownwards,
                moreVitalToLess = moreVitalToLess,
                reverseOrderOfTime = reverseOrder,
                prognosisVerdict = "Partial Amelioration with Ambiguous Direction",
                clinicalGuidance = "Some vectors show healing direction while others linger. Wait and observe; check if old symptoms re-emerge (§161 homeopathic aggravation vs true cure)."
            )
            else -> HeringEvaluation(
                insideToOutside = insideToOutside,
                aboveDownwards = aboveDownwards,
                moreVitalToLess = moreVitalToLess,
                reverseOrderOfTime = reverseOrder,
                prognosisVerdict = "Warning: Suspected Disease Suppression",
                clinicalGuidance = "Symptoms are shifting inwards (from superficial skin to deep chest/mind) or from below upwards. Inimical or incorrect remedy action suspected. Antidote may be required."
            )
        }
    }
}

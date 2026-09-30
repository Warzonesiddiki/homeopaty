package com.example.similimumai.data.engine

import com.example.similimumai.data.model.*

/**
 * Pre-seeded classical polychrest catalog — 30 core polychrests
 * (docs/data/seed-data.md). Public-domain materia medica reference data.
 */
object PolychrestDatabase {
    val all: List<Remedy> = listOf(
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
        ),
        Remedy(
            id = "acon",
            abbreviation = "Acon",
            fullName = "Aconitum Napellus",
            commonName = "Monk's Hood / Wolfsbane",
            thermalState = ThermalState.HOT,
            dominantMiasm = Miasm.PSORA,
            keynotes = listOf(
                "Acute sudden violent onset from exposure to dry cold wind; first stage of all inflammatory complaints",
                "Intense panic, restless anguish, fears of death; constantly changes position and demands company",
                "Unquenchable thirst for large quantities of cold water",
                "Dry burning heat of the whole body; face flushed red; hot dry skin with rapid pulse",
                "Symptoms alternate rapidly from one organ to another (alternating chills and heat)"
            ),
            inimicalRemedies = listOf(),
            complementaryRemedies = listOf("Gels", "Bell", "Ant-c"),
            antidoteRemedies = listOf("Camph", "Gels", "Op")
        ),
        Remedy(
            id = "arg_n",
            abbreviation = "Arg-n",
            fullName = "Argentum Nitricum",
            commonName = "Silver Nitrate",
            thermalState = ThermalState.AMBITHERMAL,
            dominantMiasm = Miasm.SYPHILIS,
            keynotes = listOf(
                "Anticipatory anxiety weeks before an event; time passes too slowly; hasty, impatient, hurried",
                "Craving for sweets which aggravate gastric complaints; desires ice cream, sugared wine",
                "Splinter-like or stitchy pains in throat and abdomen, worse on swallowing",
                "Flatulence with gurgling; rumbling of gas especially in the evening",
                "Premonitions of future misfortunes; fear of public speaking and of being alone in the dark"
            ),
            inimicalRemedies = listOf(),
            complementaryRemedies = listOf("Chin", "Sulph", "Nux-v"),
            antidoteRemedies = listOf("Coff", "Op", "Verat")
        ),
        Remedy(
            id = "arn",
            abbreviation = "Arn",
            fullName = "Arnica Montana",
            commonName = "Mountain Tar / Leopard's Bane",
            thermalState = ThermalState.HOT,
            dominantMiasm = Miasm.PSORA,
            keynotes = listOf(
                "Bruised, sore, lame sensation after every physical trauma, fall, or over-exertion",
                "Beds feel too hard; patient prefers to stay lying still; afraid of motion",
                "Claims nothing is wrong despite marked prostration and exhaustion",
                "Bitter taste in the mouth; aversion to food, especially meat",
                "Confusion with dizziness; feels as if struck on the head"
            ),
            inimicalRemedies = listOf(),
            complementaryRemedies = listOf("Rhus-t", "Bry", "Led"),
            antidoteRemedies = listOf("Apis", "Cham", "Nux-v")
        ),
        Remedy(
            id = "carbo_v",
            abbreviation = "Carbo-v",
            fullName = "Carbo Vegetabilis",
            commonName = "Vegetable Charcoal",
            thermalState = ThermalState.HOT,
            dominantMiasm = Miasm.PSORA,
            keynotes = listOf(
                "State of collapse with sluggish venous circulation; cold sweat and cold breath",
                "Air hunger demanding continuous fanning; worse in warm room, better fresh open air",
                "Great flatulence with bloating of abdomen; eructations give no relief",
                "Prostration after exhaustion, hemorrhage, or prolonged fever; delirium from inertia",
                "Better warm drinks; worse 11 PM to 3 AM; skin looks livid or cyanotic"
            ),
            inimicalRemedies = listOf(),
            complementaryRemedies = listOf("Nux-v", "Ipec", "Phos"),
            antidoteRemedies = listOf("Camph", "Chin", "Nux-v")
        ),
        Remedy(
            id = "cham",
            abbreviation = "Cham",
            fullName = "Chamomilla",
            commonName = "German Chamomile",
            thermalState = ThermalState.HOT,
            dominantMiasm = Miasm.PSORA,
            keynotes = listOf(
                "Unbearable irritability and anger; screams and strikes when contradicted or disappointed",
                "One cheek hot and red while the other is pale and cool",
                "Demands to be carried, rocked, and wheeled about; calms while moving",
                "Ailments from teething, indigestion, and anger; wants to be alone yet cannot bear solitude",
                "Nausea and vomiting with intense thirst for cold water"
            ),
            inimicalRemedies = listOf(),
            complementaryRemedies = listOf("Puls", "Nux-v", "Ign"),
            antidoteRemedies = listOf("Arn", "Bell", "Cham")
        ),
        Remedy(
            id = "chin",
            abbreviation = "Chin",
            fullName = "China Officinalis",
            commonName = "Cinchona Bark / Quinine",
            thermalState = ThermalState.HOT,
            dominantMiasm = Miasm.PSORA,
            keynotes = listOf(
                "Debility and prostration from loss of vital fluids (blood, sweat, menses, diarrhea)",
                "Periodic fevers with remission and relapse; chills that alternate with great heat",
                "Extreme hypersensitivity to slightest touch yet relieved by firm hard pressure",
                "Enlarged, tense, tympanitic abdomen with great flatulence; bloating after small meals",
                "Craves stimulants and tobacco; cannot bear uncovering; weak pulse with large volume"
            ),
            inimicalRemedies = listOf(),
            complementaryRemedies = listOf("Nux-v", "Ars", "Carbo-v"),
            antidoteRemedies = listOf("Nux-v", "Op", "Verat")
        ),
        Remedy(
            id = "gels",
            abbreviation = "Gels",
            fullName = "Gelsemium Sempervirens",
            commonName = "Yellow Jasmine / Wild Senna",
            thermalState = ThermalState.HOT,
            dominantMiasm = Miasm.PSORA,
            keynotes = listOf(
                "Motor paralysis and dullness: drowsiness, tremulous weakness, cannot stand or hold objects",
                "Dizziness and trembling from anticipation (exams, public speaking, fear of danger)",
                "Complete thirstlessness even in febrile states; heavy drooping of eyelids",
                "Lead-heavy, drowsy headache with dimmed vision; feels everything as if in a fog",
                "Ailments from damp muggy weather, thunderstorms, and suppressed urination"
            ),
            inimicalRemedies = listOf(),
            complementaryRemedies = listOf("Arn", "Acon", "Bell"),
            antidoteRemedies = listOf("Bell", "Nux-v", "Verat")
        ),
        Remedy(
            id = "hep",
            abbreviation = "Hep",
            fullName = "Hepar Sulphuris",
            commonName = "Liver Sulphur",
            thermalState = ThermalState.CHILLY,
            dominantMiasm = Miasm.SYPHILIS,
            keynotes = listOf(
                "Hypersensitive to cold air and lightest touch; must be warmly covered yet perspires",
                "Sharp splinter-like, stitching pains worse at night; cough with hoarse barking voice",
                "Violent temper; irritable, cross, and cannot endure contradiction",
                "Ameliorated by moist warmth; worse 9 AM to 1 PM and 5 to 9 PM",
                "Suppurations with thin serous discharge; abscesses that spread rather than concentrate"
            ),
            inimicalRemedies = listOf(),
            complementaryRemedies = listOf("Sil", "Merc", "Puls"),
            antidoteRemedies = listOf("Ant-c", "Bry", "Cham")
        ),
        Remedy(
            id = "kali_c",
            abbreviation = "Kali-c",
            fullName = "Kali Carbonicum",
            commonName = "Potash Carbonate",
            thermalState = ThermalState.AMBITHERMAL,
            dominantMiasm = Miasm.SYPHILIS,
            keynotes = listOf(
                "Stitching and throbbing pains; great aggravation from 2:00 AM to 4:00 AM",
                "Puffiness and swelling of upper eyelids and lips; dropsy from heart and kidney derangement",
                "Backache demanding firm support and leaning forward; worse sitting and bending",
                "Family duty and responsibility weigh heavily; conscientious, patient, unselfish",
                "Cough worse at night from 2 to 4 AM; asthma with rattling; craves salt and bread"
            ),
            inimicalRemedies = listOf(),
            complementaryRemedies = listOf("Sulph", "Phos", "Calc"),
            antidoteRemedies = listOf("Calc", "Nux-v", "Phos")
        ),
        Remedy(
            id = "zinc",
            abbreviation = "Zinc",
            fullName = "Zincum Metallicum",
            commonName = "Metallic Zinc",
            thermalState = ThermalState.CHILLY,
            dominantMiasm = Miasm.PSORA,
            keynotes = listOf(
                "Fidgety, restless feet that must be constantly moved; twitching of the limbs",
                "Brain exhaustion and mental fatigue; dullness with inability to concentrate",
                "Convulsive and spasmodic complaints from suppressed skin eruptions",
                "Night terrors with screaming; sleepwalking and enuresis; grinding of teeth",
                "Ailments from suppressed gonorrhea and mercury abuse; burning soles worse at night"
            ),
            inimicalRemedies = listOf(),
            complementaryRemedies = listOf("Calc", "Merc", "Borax"),
            antidoteRemedies = listOf("Bell", "Nux-v", "Op")
        )
    )
}

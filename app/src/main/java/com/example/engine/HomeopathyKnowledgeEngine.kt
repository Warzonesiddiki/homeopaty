package com.example.engine

import com.example.model.*
import java.util.UUID

object HomeopathyKnowledgeEngine {

    val allRemedies: Map<String, RemedyMateriaMedica> = listOf(
        RemedyMateriaMedica(
            code = "Nat-m",
            name = "Natrum Muriaticum",
            commonName = "Chloride of Sodium (Common Salt)",
            essence = "Deep psychic trauma, silent unexpressed grief, profound introversion, vulnerability covered by dignity and emotional reservation.",
            causation = "Ailments from silent grief, disappointed love, humiliation, loss of loved ones, prolonged sorrow.",
            mindGenerals = "Consolation severely aggravates; irritable when sympathized with; weeps alone; dwells excessively on past disagreeable events; reserved, dignified, defensive.",
            thermal = "Warm / Hot patient (greatly aggravated by exposure to the sun/summer heat, yet catches colds easily).",
            thirstAndFood = "Great craving for SALT and salty food; aversion to bread and fats; unquenchable thirst for large quantities of cold water.",
            keyAggravations = "Sun heat, 10:00 AM – 3:00 PM, Consolation/sympathy, seaside, mental exertion, lying down.",
            keyAmeliorations = "Open cool air, lying on right side, sweating, fasting, tight clothing on back.",
            characteristicKeynotes = listOf(
                "Bursting, hammering headache like little hammers knocking on the brain, particularly 10 AM to 3 PM",
                "Severe aggravation from consolation or pity; locks herself in room to weep alone",
                "Intense craving for extra salt on food or salty snacks",
                "Mapped tongue with red patches; herpes labialis (cold sores) around lips during fever"
            ),
            complementary = "Apis, Sepia, Ignatia (acute analog)",
            followsWell = "Calc-p, Puls, Sep, Sulph, Thuja",
            inimical = "None directly recorded, but avoid repetitive high potencies without clear indication",
            antidotes = "Ars, Camph, Phos, Nit-s-d",
            primaryMiasm = Miasm.PSORA,
            kingdom = SankaranKingdom.MINERAL,
            periodicTableRow = 3,
            periodicTableCol = 1,
            dominantTissue = "Mucous membranes & Blood"
        ),
        RemedyMateriaMedica(
            code = "Ign",
            name = "Ignatia Amara",
            commonName = "St. Ignatius Bean",
            essence = "Acute emotional storm, hysterical contradictions, sudden shocks, grief where emotions are turbulent, rapid mood swings with frequent deep sighing.",
            causation = "Recent acute grief, heartbreak, bad news, mortification, reprimand, sudden fright.",
            mindGenerals = "Rapid mood shifts: laughs hysterically then weeps bitterly; silent weeping; deep involuntary sighing; brooding over acute loss.",
            thermal = "Chilly patient; sensitive to cold drafts and open air.",
            thirstAndFood = "Craves sour/indigestible items; paradoxically swallowing solids relieves throat pain while swallowing liquids aggravates.",
            keyAggravations = "Consolation, emotions, coffee, tobacco smoke, cold air, sweets.",
            keyAmeliorations = "Change of position, warm room, hard pressure, deep sighing, swallowing solids.",
            characteristicKeynotes = listOf(
                "Frequent involuntary deep sighing and yawning from internal emotional oppression",
                "Sensation of a lump (globus hystericus) in the throat, relieved only by swallowing solid food",
                "Paradoxical symptoms: sore throat better swallowing solids; cough increases the more one coughs",
                "Spasmodic laughter alternating with tears and deep sobbing"
            ),
            complementary = "Nat-m (chronic counterpart), Ph-ac",
            followsWell = "Nat-m, Puls, Sep, Zinc",
            inimical = "Coffea, Nux-v, Tabacum",
            antidotes = "Camph, Cham, Cocc, Puls",
            primaryMiasm = Miasm.PSORA,
            kingdom = SankaranKingdom.PLANT,
            dominantTissue = "Central Nervous System & Nerves"
        ),
        RemedyMateriaMedica(
            code = "Ars",
            name = "Arsenicum Album",
            commonName = "Arsenious Acid (White Arsenic)",
            essence = "Severe anguish, physical and mental restlessness, terror of death, burning pains relieved by heat, prostration out of proportion to illness.",
            causation = "Spoiled food, cold drinks, ptomaine poisoning, chilling in water, financial ruin, fright.",
            mindGenerals = "Great anguish and mortal fear of death (believes it is useless to take medicine); fastidious: cannot rest if a picture is crooked; restless tossing.",
            thermal = "Extremely chilly (wants heavy blankets, warm room, wraps head in wool).",
            thirstAndFood = "Intense thirst for small quantities of warm or cold water drank frequently (sips every few minutes); craves warm drinks.",
            keyAggravations = "Midnight to 2:00 AM (especially 1:00 AM), cold air, cold drinks/food, lying on affected side.",
            keyAmeliorations = "Warmth in general, hot applications, warm drinks, head elevated in bed.",
            characteristicKeynotes = listOf(
                "Burning pains like coals of fire, paradoxically relieved by warm applications and hot drinks",
                "Midnight aggravation (1:00 AM – 2:00 AM) with anxious tossing and fear of dying",
                "Thirst for frequent sips of water; stomach cannot retain cold liquids",
                "Fastidiousness: neat, orderly, anxious perfectionist even while critically ill in bed"
            ),
            complementary = "All-s, Carbo-v, Phos, Thuja, Pyrog",
            followsWell = "Bell, Calc, Lyc, Nux-v, Rhus-t, Sulph",
            inimical = "None recorded; highly reactive",
            antidotes = "Camph, Chin, Ferr, Hep, Ip, Nux-v, Tab",
            primaryMiasm = Miasm.SYPHILIS,
            kingdom = SankaranKingdom.MINERAL,
            periodicTableRow = 4,
            periodicTableCol = 15,
            dominantTissue = "Gastrointestinal tract, Heart, Blood"
        ),
        RemedyMateriaMedica(
            code = "Puls",
            name = "Pulsatilla Nigricans",
            commonName = "Wind Flower",
            essence = "Mild, gentle, tearful, yielding disposition; seeks consolation and affection; constantly shifting and changeable physical and emotional symptoms.",
            causation = "Getting wet feet, rich/fatty foods, pork, ice cream, emotional neglect, puberty, suppressed menses.",
            mindGenerals = "Weeps easily while recounting symptoms; desires and is greatly relieved by sympathy and consolation; indecisive, mild, clingy.",
            thermal = "Distinctly Hot patient; suffocates in warm stuffy rooms; craves open cool fresh air and open windows.",
            thirstAndFood = "Completely THIRSTLESS even with a dry mouth and high fever; aversion to fats, butter, and rich greasy food; craves pastries.",
            keyAggravations = "Warm closed room, evening, rich fatty foods, lying on left side, getting wet.",
            keyAmeliorations = "Open cool air, walking slowly in fresh air, gentle motion, weeping, cold applications.",
            characteristicKeynotes = listOf(
                "Complete absence of thirst with dry mouth, especially in fever and acute catarrh",
                "Weeps easily while talking; consolation and gentle affection brings immense comfort and smiles",
                "Craves open air and cool drafts; suffocates and feels faint in warm, closed rooms",
                "Shifting pains that fly rapidly from one joint or part of the body to another"
            ),
            complementary = "Lyc, Sil, Kali-bi, Kali-s",
            followsWell = "Bell, Cham, Ign, Nux-v, Rhus-t, Sep, Sulph",
            inimical = "None directly recorded",
            antidotes = "Cham, Coff, Ign, Nux-v",
            primaryMiasm = Miasm.PSORA,
            kingdom = SankaranKingdom.PLANT,
            dominantTissue = "Mucous membranes, Veins, Synovial joints"
        ),
        RemedyMateriaMedica(
            code = "Rhus-t",
            name = "Rhus Toxicodendron",
            commonName = "Poison Ivy",
            essence = "Fibrous tissue and tendon stiffness; restless motor urge; triangular red tip of tongue; symptoms worse on initial motion, better on continued walking.",
            causation = "Getting wet while overheated, strain of tendons/muscles, sprains, cold damp weather, cellar air.",
            mindGenerals = "Restless, must change position constantly; anxious at night in bed; apprehensive of impending calamity.",
            thermal = "Very chilly; sensitive to cold air and dampness.",
            thirstAndFood = "Dry mouth with thirst for cold water or milk; craves cold milk and sweets.",
            keyAggravations = "First beginning to move after rest, damp cold weather, night, getting wet.",
            keyAmeliorations = "Continued motion, walking about, dry hot weather, hot baths, hard pressure.",
            characteristicKeynotes = listOf(
                "Severe stiffness and aching on first beginning to move, gradually limbering up and feeling better from continued walking",
                "Triangular red tip on a coated white tongue",
                "Great restlessness at night in bed, forcing constant tossing and turning to find an easy spot",
                "Aggravation before and during rainstorms, damp cold changes in the weather"
            ),
            complementary = "Bry (antagonistic analog), Calc-f, Med, Phos",
            followsWell = "Ars, Bry, Calc, Med, Puls, Sep, Sulph",
            inimical = "Apis Mellifica (STRICTLY INIMICAL - Never give before or after!)",
            antidotes = "Anac, Bell, Bry, Camph, Coff, Graph, Sulph",
            primaryMiasm = Miasm.PSORA,
            kingdom = SankaranKingdom.PLANT,
            dominantTissue = "Fibrous tissue, Tendons, Ligaments, Skin"
        ),
        RemedyMateriaMedica(
            code = "Apis",
            name = "Apis Mellifica",
            commonName = "Honey Bee Poison",
            essence = "Violent burning, stinging pains like red-hot needles; bag-like edema of lower eyelids; complete thirstlessness in dropsies; intolerant of heat.",
            causation = "Insect stings, suppressed eruptions, grief, jealousy, rage, fright.",
            mindGenerals = "Jealous, suspicious, irritable, hard to please; awkward drops things from hands; high-pitched shrill cry (hydrocephalic cry) during sleep.",
            thermal = "INTENSELY HOT patient; intolerant of the least heat, warm room, or hot bath.",
            thirstAndFood = "Completely THIRSTLESS in edema, fever, and kidney diseases; craves cold milk.",
            keyAggravations = "Heat in any form, warm room, hot bath, 3:00 PM – 5:00 PM, touch, pressure.",
            keyAmeliorations = "Cold water bathing, ice applications, uncovering, open fresh air.",
            characteristicKeynotes = listOf(
                "Burning, stinging pains like bee stings with puffy bag-like swelling (edema of eyelids, throat, joints)",
                "Complete absence of thirst with fluid retention and dropsy",
                "Extreme aggravation from heat in all forms; relief from ice-cold applications",
                "Clumsy, drops things from hands due to loss of tactile sensitivity"
            ),
            complementary = "Nat-m, Baryta-c",
            followsWell = "Ars, Graph, Kali-bi, Lyc, Puls, Sep, Sulph",
            inimical = "Rhus Toxicodendron (STRICTLY INIMICAL - Do NOT alternate or follow!)",
            antidotes = "Canth, Chin, Ip, Lachesis, Nat-m",
            primaryMiasm = Miasm.SYCOSIS,
            kingdom = SankaranKingdom.ANIMAL,
            dominantTissue = "Cellular tissue, Serous membranes, Kidneys"
        ),
        RemedyMateriaMedica(
            code = "Bry",
            name = "Bryonia Alba",
            commonName = "White Bryony",
            essence = "Extreme dryness of mucous membranes, stitching pains, absolute intolerance of the slightest movement, firm relief from hard pressure.",
            causation = "Anger/chagrin, overheating then chilling, cold dry winds, suppressed discharges.",
            mindGenerals = "Irritable, wants to be left alone; talks business during delirium; fearful of poverty; wishes to go home.",
            thermal = "Warm/Chilly; desires cool room but worse from drafts.",
            thirstAndFood = "Great thirst for LARGE QUANTITIES of cold water at long intervals; dry cracked lips; craves meat and coffee.",
            keyAggravations = "Any motion, even moving eyes or deep breath; morning on waking; warmth.",
            keyAmeliorations = "Lying still on painful side, hard pressure, rest, cold drinks.",
            characteristicKeynotes = listOf(
                "Pains worse from the slightest movement; even coughing requires holding the chest firmly with both hands",
                "Relief from lying motionless on the affected side and from hard pressure",
                "Intense thirst for large quantities of water at long intervals with parchment-dry lips and tongue",
                "Irritable, taciturn; wants to be left completely undisturbed"
            ),
            complementary = "Alum, Rhus-t",
            followsWell = "Acon, Ars, Bell, Kali-c, Nux-v, Phos, Puls, Rhus-t, Sulph",
            inimical = "None directly recorded",
            antidotes = "Acon, Cham, Ign, Nux-v",
            primaryMiasm = Miasm.PSORA,
            kingdom = SankaranKingdom.PLANT,
            dominantTissue = "Serous membranes (Pleura, Peritoneum, Synovium)"
        ),
        RemedyMateriaMedica(
            code = "Nux-v",
            name = "Nux Vomica",
            commonName = "Poison Nut",
            essence = "High-strung, ambitious, sedentary executive; hypersensitive to all external stimuli; ineffectual urging for stool and vomiting.",
            causation = "Sedentary lifestyle, excessive stimulant use (coffee, alcohol, spicy food), overwork, anger.",
            mindGenerals = "Impatient, fiery, irritable, fault-finding; cannot bear contradiction, noise, light, or odors; workaholic.",
            thermal = "Extremely chilly; cannot uncover even the least without feeling chilly; drafts bring on complaints.",
            thirstAndFood = "Craves spicy, rich food, stimulants, fatty dishes; gastric distress 1-2 hours after eating.",
            keyAggravations = "Morning on waking, 3:00 AM – 4:00 AM, cold open air, stimulants, mental exertion.",
            keyAmeliorations = "Evening, rest, warm room, damp wet weather, short catnaps if uninterrupted.",
            characteristicKeynotes = listOf(
                "Frequent, ineffectual urging to stool; passes small amounts with temporary relief",
                "Extreme sensitivity to external stimuli: noise, light, drafts, and odors cause intense irritation",
                "Chilly patient who must be covered in every stage of fever or chill",
                "Awakens at 3:00 AM with anxious thoughts of work, falls asleep when time to get up"
            ),
            complementary = "Kali-c, Sepia, Sulphur",
            followsWell = "Ars, Bell, Bry, Calc, Lyc, Phos, Puls, Sep, Sulph",
            inimical = "Zincum, Ignatia",
            antidotes = "Acon, Bell, Cham, Coff, Ign, Puls",
            primaryMiasm = Miasm.PSORA,
            kingdom = SankaranKingdom.PLANT,
            dominantTissue = "Gastrointestinal tract, Nervous System"
        ),
        RemedyMateriaMedica(
            code = "Phos",
            name = "Phosphorus",
            commonName = "Phosphorus",
            essence = "Open, affectionate, clairvoyant, impressionable temperament; hemorrhagic diathesis; burning pains and craving for ice-cold water.",
            causation = "Loss of vital fluids, grief, thunderstorm, electrical changes, lightning, sprains.",
            mindGenerals = "Extroverted, highly sympathetic; fears darkness, solitude, thunderstorms, ghost stories; relieved by company.",
            thermal = "Chilly body but burning palms/feet; craves cold food and icy drinks.",
            thirstAndFood = "Thirst for ice-cold water, which is vomited as soon as it becomes warm in the stomach; craves salt, ice cream, spices.",
            keyAggravations = "Twilight, evening till midnight, lying on left side, thunderstorms, cold air.",
            keyAmeliorations = "Lying on right side, sleep (even a short nap), cold food, being rubbed/magnetized.",
            characteristicKeynotes = listOf(
                "Craves ice-cold drinks which satisfy, but are vomited as soon as they become warm in the stomach",
                "Burning sensations: in spots along spine, palms, chest, and stomach",
                "Great anxiety when alone in the dark, during twilight, or before thunderstorms",
                "Easy hemorrhages from any orifice: bright red blood that is difficult to clot"
            ),
            complementary = "Ars, All-s, Carbo-v, Lyc, Sep, Tarent",
            followsWell = "Ars, Bell, Bry, Calc, Lyc, Nux-v, Puls, Rhus-t, Sep, Sil, Sulph",
            inimical = "Causticum (STRICTLY INIMICAL - Never give before or after!)",
            antidotes = "Camph, Coff, Nux-v, Ter",
            primaryMiasm = Miasm.TUBERCULAR,
            kingdom = SankaranKingdom.MINERAL,
            periodicTableRow = 3,
            periodicTableCol = 15,
            dominantTissue = "Blood, Lungs, Nervous tissue, Bones"
        ),
        RemedyMateriaMedica(
            code = "Caust",
            name = "Causticum",
            commonName = "Hahnemann's Tinctura Acris Sine Kali",
            essence = "Deep sympathetic sorrow for the injustices suffered by others; gradual paralysis of single muscle groups; involuntary urine when coughing.",
            causation = "Long-lasting grief, sorrow, burns, night-watching, cold dry wind exposure.",
            mindGenerals = "Intensely sympathetic (weeps at another's troubles); champion of social justice; anxious anticipation; fearful in dark.",
            thermal = "Chilly patient; sensitive to cold dry north/east winds.",
            thirstAndFood = "Aversion to sweets; craves smoked meats, bacon, salt; cold water sips relieve cough.",
            keyAggravations = "Clear, dry fine weather, cold dry winds, 3:00 AM – 4:00 AM, twilight.",
            keyAmeliorations = "Damp, wet, rainy weather, warm air, sips of cold water, heat of bed.",
            characteristicKeynotes = listOf(
                "Involuntary spurting of urine when coughing, sneezing, or blowing the nose",
                "Gradual motor weakness and paralysis of local parts (eyelids droop, vocal cords, facial palsy from cold wind)",
                "Symptoms markedly better in damp, wet, rainy weather and worse in clear, dry cold days",
                "Intense emotional sensitivity to the injustices and suffering of others"
            ),
            complementary = "Carbo-v, Coloc, Petros",
            followsWell = "Calc, Guai, Lyc, Nux-v, Puls, Rhus-t, Sep, Sil, Sulph",
            inimical = "Phosphorus (STRICTLY INIMICAL - Never give before or after!), Coffea",
            antidotes = "Asaf, Coff, Coloc, Dulc, Guai, Nit-s-d, Nux-v",
            primaryMiasm = Miasm.SYPHILIS,
            kingdom = SankaranKingdom.MINERAL,
            dominantTissue = "Motor nerves, Vocal cords, Bladder"
        ),
        RemedyMateriaMedica(
            code = "Lyc",
            name = "Lycopodium Clavatum",
            commonName = "Club Moss",
            essence = "Intellectual brilliance with physical weakness; low self-confidence masked by authoritarian arrogance; right-to-left direction; 4-8 PM aggravation.",
            causation = "Anticipation, public speaking anxiety, mortification, suppressed anger, rich pastry.",
            mindGenerals = "Fear of undertaking new responsibilities yet performs well once started; dictatorial at home, timid outside; irritable on waking.",
            thermal = "Generally warm-blooded yet loves warm drinks; right foot cold, left foot hot.",
            thirstAndFood = "Wants warm food and hot drinks; intense craving for SWEETS and sugar; early satiety (takes few bites and feels bloated).",
            keyAggravations = "4:00 PM – 8:00 PM, right side or right to left, warm room, pressure of clothing around waist.",
            keyAmeliorations = "Warm drinks, motion, open cool air, uncovering head, passing flatus.",
            characteristicKeynotes = listOf(
                "Aggravation regularly between 4:00 PM and 8:00 PM",
                "Right-sided complaints or symptoms moving from right to left (throat, chest, ovaries, kidney)",
                "Excessive abdominal flatulence with loud rumbling; clothes feel intolerably tight around waist",
                "Craving for sweets and warm foods/drinks; early satiety after just a few bites"
            ),
            complementary = "Iod, Chel, Kali-c",
            followsWell = "Bell, Bry, Calc, Graph, Hyos, Ign, Lach, Led, Nux-v, Puls, Rhus-t, Sep, Sil, Sulph",
            inimical = "Coffee",
            antidotes = "Acon, Camph, Cham, Coff, Graph, Nux-v, Puls",
            primaryMiasm = Miasm.PSORA,
            kingdom = SankaranKingdom.PLANT,
            dominantTissue = "Liver, Digestive tract, Urinary organs"
        ),
        RemedyMateriaMedica(
            code = "Sep",
            name = "Sepia Officinalis",
            commonName = "Inky Juice of the Cuttlefish",
            essence = "Venous stasis, hormonal exhaustion, emotional numbness and indifference to loved ones, bearing-down pelvic sensation, better vigorous exercise.",
            causation = "Overwork, anger, disappointment, childbirth, lactation, menopause, tobacco.",
            mindGenerals = "Indifference to family, children, and nearest loved ones; weeping when telling symptoms; irritable; seeks solitude; sarcastic.",
            thermal = "Chilly patient; lack of vital warmth; cold feet and hands.",
            thirstAndFood = "Craves vinegar, pickles, acids, spicy foods; aversion to meat and fat; faint empty feeling in pit of stomach.",
            keyAggravations = "Morning and evening, cold damp air, laundry work, before/during menses.",
            keyAmeliorations = "Vigorous violent exercise (running, dancing), crossing legs, warmth of bed.",
            characteristicKeynotes = listOf(
                "Apathy and complete indifference to family and loved ones with emotional exhaustion",
                "Bearing-down sensation in pelvis as if everything would protrude through vagina, relieved by crossing legs",
                "Yellow saddle-like pigmentation across bridge of nose and upper cheeks (chloasma)",
                "Marked relief of all physical and mental symptoms from vigorous, active exertion and dancing"
            ),
            complementary = "Nat-m (essential partner), Phos, Sabin",
            followsWell = "Bell, Bry, Calc, Guai, Lyc, Nux-v, Puls, Rhus-t, Sil, Sulph",
            inimical = "Lachesis (frequently incompatible)",
            antidotes = "Acon, Ant-t, Bell, Cham, Ferr, Phos",
            primaryMiasm = Miasm.SYCOSIS,
            kingdom = SankaranKingdom.ANIMAL,
            dominantTissue = "Venous system, Female pelvic organs, Skin"
        ),
        RemedyMateriaMedica(
            code = "Sil",
            name = "Silicea",
            commonName = "Pure Flint (Silica)",
            essence = "Grit without stamina; yielding mentally yet stubborn; offensive foot sweat; suppuration; cold head relieved by wrapping warmly.",
            causation = "Vaccination, suppressed foot sweat, exposure to drafts, stone cutting.",
            mindGenerals = "Yielding, timid, lacks self-confidence, dreads failure yet executes well; fixed ideas; needle phobia.",
            thermal = "Extremely Chilly patient; hugs the stove, sensitive to the slightest draft of air.",
            thirstAndFood = "Aversion to mother's milk, warm cooked food, meat; craves cold food, ice cream, sand.",
            keyAggravations = "Cold, drafts, dampness, uncovering head, during menses, new moon.",
            keyAmeliorations = "Warmth, wrapping head warmly in wool, profuse urination.",
            characteristicKeynotes = listOf(
                "Headache relieved by wrapping the head up warmly in wool or a shawl",
                "Profuse, offensive, excoriating foot sweat that ruins socks",
                "Great lack of physical grit: every little cut or injury suppurates and heals slowly",
                "Constipation: stool recedes back into rectum after being partially expelled (bashful stool)"
            ),
            complementary = "Thuja, Sanic, Puls, Fluor-ac",
            followsWell = "Bell, Bry, Calc, Hep, Lyc, Nux-v, Phos, Puls, Rhus-t, Sep, Sulph",
            inimical = "Mercurius Solubilis (STRICTLY INIMICAL - Never give before or after!)",
            antidotes = "Camph, Fluor-ac, Hep",
            primaryMiasm = Miasm.SYPHILIS,
            kingdom = SankaranKingdom.MINERAL,
            periodicTableRow = 3,
            periodicTableCol = 14,
            dominantTissue = "Connective tissue, Bones, Glands, Skin"
        ),
        RemedyMateriaMedica(
            code = "Merc",
            name = "Mercurius Solubilis",
            commonName = "Hahnemann's Soluble Quicksilver",
            essence = "Human thermometer (< heat and < cold); profuse nocturnal oily sweat giving no relief; metallic taste, flabby indented tongue.",
            causation = "Damp weather, autumn nights, suppressed gonorrhea, wet feet.",
            mindGenerals = "Hurried, restless, suspicious; impulse to kill or commit violence on slight contradiction; stammering speech.",
            thermal = "Sensitive to both extremes of temperature (aggravated by cold air AND warm room/bed).",
            thirstAndFood = "Intense unquenchable thirst with moist salivating tongue; craves bread and butter.",
            keyAggravations = "Night, warmth of bed, damp cold night air, lying on right side, perspiring.",
            keyAmeliorations = "Rest, moderate even room temperature.",
            characteristicKeynotes = listOf(
                "Profuse perspiration at night that stains linen yellow, smells sour/fetid, and gives NO relief",
                "Flabby, swollen tongue showing deep scalloped imprints of the teeth along edges",
                "Intense thirst for cold water despite having a mouth dripping with foul saliva",
                "Aggravation from both heat and cold; 'the human thermometer'"
            ),
            complementary = "Bad, Bell, Hep",
            followsWell = "Acon, Bell, Hep, Lach, Nit-ac, Puls, Rhus-t, Sulph",
            inimical = "Silicea Terra (STRICTLY INIMICAL - Never give before or after!)",
            antidotes = "Aur, Bell, Camph, Carb-v, Chin, Dulc, Ferr, Hep, Iod, Kali-i, Lach, Nit-ac, Op, Phyt, Sars, Staph, Sulph",
            primaryMiasm = Miasm.SYPHILIS,
            kingdom = SankaranKingdom.MINERAL,
            periodicTableRow = 6,
            periodicTableCol = 12,
            dominantTissue = "Glands, Mucous membranes, Bones"
        ),
        RemedyMateriaMedica(
            code = "Sulph",
            name = "Sulphur",
            commonName = "Brimstone / Sublimed Sulphur",
            essence = "The ragged philosopher; venous congestion, burning soles, empty sinking at 11 AM, aversion to bathing, voluptuous itching.",
            causation = "Suppression of skin eruptions, vaccination, sun, alcohol, over-exertion.",
            mindGenerals = "Philosophical, inventive genius with disheveled attire; selfish, critical, egotistical; averse to mental labor.",
            thermal = "Intensely Hot patient; sticks feet out of bedcovers at night to cool them.",
            thirstAndFood = "Great thirst for cold beer and drinks; empty faint hunger at 11:00 AM; craves sweets, fats, spicy foods; aversion to eggs.",
            keyAggravations = "Heat of bed, bathing/washing, 11:00 AM, standing, sweets, milk.",
            keyAmeliorations = "Dry warm weather, lying on right side, motion.",
            characteristicKeynotes = listOf(
                "Burning heat in soles of feet at night, sticking them out from under the covers",
                "Empty, faint, gnawing hunger in stomach regularly at 11:00 AM",
                "Aversion to bathing, washing aggravates skin eruptions and general symptoms",
                "Voluptuous itching of skin: feels delicious to scratch, followed by intense burning"
            ),
            complementary = "Aloe, Nux-v, Psor, Pyrog",
            followsWell = "Acon, Bell, Bry, Calc, Cham, Chin, Coloc, Graph, Ign, Kali-c, Lyc, Merc, Nux-v, Puls, Rhus-t, Sep, Sil",
            inimical = "Never give Calcarea Carb immediately BEFORE Sulphur!",
            antidotes = "Acon, Camph, Cham, Chin, Merc, Nux-v, Puls, Sep",
            primaryMiasm = Miasm.PSORA,
            kingdom = SankaranKingdom.MINERAL,
            periodicTableRow = 3,
            periodicTableCol = 16,
            dominantTissue = "Skin, Venous circulation, Mucous outlets"
        ),
        RemedyMateriaMedica(
            code = "Thuj",
            name = "Thuja Occidentalis",
            commonName = "Arbor Vitae (Tree of Life)",
            essence = "Sycotic dyscrasia, warty excrescences, fixed delusions (body made of glass, something alive in abdomen), greasy skin, sweated parts uncovered.",
            causation = "Bad effects of vaccination, gonorrhea, suppressed gonorrhea, sunstroke.",
            mindGenerals = "Fixed ideas: limbs made of glass and will break, someone walking beside them, something alive in abdomen; hurried speech.",
            thermal = "Chilly patient; worse in cold damp air.",
            thirstAndFood = "Desire for cold food and drinks, tea; aversion to onions, potatoes, meat.",
            keyAggravations = "Cold damp weather, 3:00 AM, 3:00 PM, vaccination, onions, tea.",
            keyAmeliorations = "Warm dry air, drawing up limbs, sneezing, rubbing.",
            characteristicKeynotes = listOf(
                "Warty excrescences, cauliflower-like condylomata, pedunculated skin tags",
                "Perspiration smelling like sweet honey or garlic, only on uncovered parts",
                "Fixed delusions: body made of brittle glass, something alive jumping in abdomen",
                "Forked or split urinary stream with severe cutting after urination"
            ),
            complementary = "Med, Nat-s, Sil",
            followsWell = "Calc, Ign, Lyc, Merc, Nit-ac, Puls, Sil, Sulph",
            inimical = "None directly recorded",
            antidotes = "Cham, Cocc, Merc, Puls, Sulph",
            primaryMiasm = Miasm.SYCOSIS,
            kingdom = SankaranKingdom.PLANT,
            dominantTissue = "Epithelium, Genito-urinary tract, Skin"
        )
    ).associateBy { it.code }

    val allRubrics: List<Rubric> = listOf(
        Rubric(
            id = "R1",
            path = "MIND - AILMENTS FROM - grief",
            chapter = "MIND",
            weight = 3,
            grades = mapOf("Nat-m" to 3, "Ign" to 3, "Sep" to 2, "Puls" to 2, "Sulph" to 1, "Ars" to 1),
            bilingualKeywords = listOf("grief", "sadness", "loss", "father died", "mother died", "death of", "heartbreak", "gam", "dukh", "sadma", "shok", "breakup"),
            tissueTropism = "Central Nervous System"
        ),
        Rubric(
            id = "R2",
            path = "MIND - CONSOLATION - agg.",
            chapter = "MIND",
            weight = 3,
            grades = mapOf("Nat-m" to 3, "Ign" to 3, "Sep" to 3, "Sil" to 2, "Sulph" to 1),
            bilingualKeywords = listOf("consolation agg", "hate sympathy", "hate comfort", "dilasa dene se", "samjhane se gussa", "sympathy makes worse", "leave me alone", "irritated when comforted"),
            tissueTropism = "Emotions & Will"
        ),
        Rubric(
            id = "R3",
            path = "MIND - CONSOLATION - amel.",
            chapter = "MIND",
            weight = 3,
            grades = mapOf("Puls" to 3, "Phos" to 2, "Sep" to 1),
            bilingualKeywords = listOf("consolation amel", "likes sympathy", "wants comfort", "dilasa accha lagta hai", "comforting relieves", "loves being pampered", "pyaar se baat"),
            tissueTropism = "Emotions & Will"
        ),
        Rubric(
            id = "R4",
            path = "MIND - WEEPING - easily",
            chapter = "MIND",
            weight = 3,
            grades = mapOf("Puls" to 3, "Nat-m" to 3, "Ign" to 3, "Sep" to 2, "Lyc" to 2, "Phos" to 2, "Rhus-t" to 1),
            bilingualKeywords = listOf("weeps easily", "crying easily", "tearful", "rona aata hai", "aankhon mein aansu", "crying while talking"),
            tissueTropism = "Emotions & Will"
        ),
        Rubric(
            id = "R5",
            path = "MIND - SIGHING - involuntary",
            chapter = "MIND",
            weight = 3,
            grades = mapOf("Ign" to 3, "Nat-m" to 2, "Sep" to 1, "Bry" to 1),
            bilingualKeywords = listOf("sighing", "deep breath", "aah bharna", "lambi saans", "involuntary sighing"),
            tissueTropism = "Respiratory Nerves"
        ),
        Rubric(
            id = "R6",
            path = "MIND - RESTLESSNESS - night - midnight, after",
            chapter = "MIND",
            weight = 3,
            grades = mapOf("Ars" to 3, "Rhus-t" to 3, "Nux-v" to 1),
            bilingualKeywords = listOf("restless midnight", "restless at night", "bechaini raat ko", "1 baje bechaini", "tossing in bed"),
            tissueTropism = "Motor Nerves"
        ),
        Rubric(
            id = "R7",
            path = "MIND - FASTIDIOUS",
            chapter = "MIND",
            weight = 3,
            grades = mapOf("Ars" to 3, "Nux-v" to 2, "Nat-m" to 1),
            bilingualKeywords = listOf("fastidious", "neat and clean", "perfectionist", "har cheez apni jagah", "safai pasand", "orderly"),
            tissueTropism = "Intellect & Will"
        ),
        Rubric(
            id = "R8",
            path = "MIND - FEAR - death, of",
            chapter = "MIND",
            weight = 3,
            grades = mapOf("Ars" to 3, "Phos" to 2),
            bilingualKeywords = listOf("fear of death", "afraid of dying", "maut ka darr", "jaan nikal jayegi", "predicts death"),
            tissueTropism = "Emotions & Will"
        ),
        Rubric(
            id = "R9",
            path = "HEAD - PAIN - hammering",
            chapter = "HEAD",
            weight = 2,
            grades = mapOf("Nat-m" to 3, "Ign" to 1, "Sep" to 1, "Bry" to 1, "Sulph" to 2),
            bilingualKeywords = listOf("hammering headache", "throbbing head", "hathode jaisa dard", "knocking in head", "bursting head"),
            tissueTropism = "Cerebral circulation"
        ),
        Rubric(
            id = "R10",
            path = "HEAD - PAIN - daytime - 10-15 h",
            chapter = "HEAD",
            weight = 2,
            grades = mapOf("Nat-m" to 3, "Sulph" to 1, "Bry" to 1),
            bilingualKeywords = listOf("headache 10 am to 3 pm", "10 to 3", "subah 10 baje se dopehar 3", "noon headache"),
            tissueTropism = "Cerebral circulation"
        ),
        Rubric(
            id = "R11",
            path = "GENERALS - SUN - exposure to, agg.",
            chapter = "GENERALS",
            weight = 2,
            grades = mapOf("Nat-m" to 3, "Puls" to 2, "Sulph" to 1),
            bilingualKeywords = listOf("sun agg", "sun headache", "dhoop se dard", "dhoop bilkul nahi", "sunlight worse"),
            tissueTropism = "Thermal regulation"
        ),
        Rubric(
            id = "R12",
            path = "GENERALS - FOOD and DRINKS - salt - desire",
            chapter = "GENERALS",
            weight = 2,
            grades = mapOf("Nat-m" to 3, "Phos" to 3, "Sep" to 1, "Sulph" to 1),
            bilingualKeywords = listOf("craves salt", "salt craving", "extra namak", "namkeen cheez", "salty food"),
            tissueTropism = "Mineral assimilation"
        ),
        Rubric(
            id = "R13",
            path = "GENERALS - FOOD and DRINKS - sweets - desire",
            chapter = "GENERALS",
            weight = 2,
            grades = mapOf("Lyc" to 3, "Sulph" to 3, "Rhus-t" to 1),
            bilingualKeywords = listOf("craves sweets", "sweet tooth", "meetha pasand", "mithai", "desire for sugar"),
            tissueTropism = "Carbohydrate metabolism"
        ),
        Rubric(
            id = "R14",
            path = "STOMACH - PAIN - burning",
            chapter = "STOMACH",
            weight = 2,
            grades = mapOf("Ars" to 3, "Phos" to 3, "Nux-v" to 2, "Sulph" to 2),
            bilingualKeywords = listOf("burning stomach", "heartburn", "gastritis", "pet mein jalan", "pet jalta hai", "acid reflux"),
            tissueTropism = "Gastric Mucosa"
        ),
        Rubric(
            id = "R15",
            path = "STOMACH - PAIN - burning - warm drinks, amel.",
            chapter = "STOMACH",
            weight = 3,
            grades = mapOf("Ars" to 3, "Lyc" to 2, "Nux-v" to 1),
            bilingualKeywords = listOf("warm drinks relieve", "hot tea amel", "garam paani se aaram", "better warm sips"),
            tissueTropism = "Gastric Mucosa"
        ),
        Rubric(
            id = "R16",
            path = "STOMACH - THIRST - small quantities, for - often",
            chapter = "STOMACH",
            weight = 3,
            grades = mapOf("Ars" to 3, "Lyc" to 1, "Rhus-t" to 1),
            bilingualKeywords = listOf("thirst small sips", "frequent sips", "thoda thoda paani", "do do ghoont", "sips often"),
            tissueTropism = "Fluid homeostasis"
        ),
        Rubric(
            id = "R17",
            path = "STOMACH - THIRSTLESS",
            chapter = "STOMACH",
            weight = 3,
            grades = mapOf("Puls" to 3, "Apis" to 3, "Bry" to 1),
            bilingualKeywords = listOf("thirstless", "no thirst", "pyaas nahi lagti", "paani peene ka mann nahi", "never thirsty"),
            tissueTropism = "Fluid homeostasis"
        ),
        Rubric(
            id = "R18",
            path = "GENERALS - MOTION - beginning of, agg. - continued, amel.",
            chapter = "GENERALS",
            weight = 3,
            grades = mapOf("Rhus-t" to 3, "Puls" to 2, "Lyc" to 1),
            bilingualKeywords = listOf("first motion agg", "continued motion amel", "shuru mein dard chalne se aaram", "stiff on rising better walking", "limbers up"),
            tissueTropism = "Fibrous tissue & Tendons"
        ),
        Rubric(
            id = "R19",
            path = "GENERALS - MOTION - agg.",
            chapter = "GENERALS",
            weight = 3,
            grades = mapOf("Bry" to 3, "Nux-v" to 2, "Merc" to 1),
            bilingualKeywords = listOf("motion agg", "slightest movement worse", "hilne se dard badhta", "wants to lie still", "absolute rest amel"),
            tissueTropism = "Serous Membranes"
        ),
        Rubric(
            id = "R20",
            path = "CHEST - PAIN - crushing - radiating to left arm",
            chapter = "CHEST",
            weight = 3,
            grades = mapOf("Ars" to 2, "Apis" to 1),
            bilingualKeywords = listOf("crushing chest pain", "radiating to left arm", "seene mein dabav", "left hath mein dard", "cardiac pain", "chhati mein dard"),
            tissueTropism = "Myocardium & Coronary Arteries"
        ),
        // Multimodal Vision Rubrics: Tongue
        Rubric(
            id = "R_TONGUE_MAPPED",
            path = "MOUTH - MAPPED tongue",
            chapter = "MOUTH",
            weight = 3,
            grades = mapOf("Nat-m" to 3, "Ars" to 3, "Merc" to 3, "Rhus-t" to 2),
            bilingualKeywords = listOf("mapped tongue", "geographic tongue", "denuded patches on tongue"),
            tissueTropism = "Lingual Epithelium"
        ),
        Rubric(
            id = "R_TONGUE_INDENTED",
            path = "MOUTH - INDENTED - tongue",
            chapter = "MOUTH",
            weight = 3,
            grades = mapOf("Merc" to 3, "Rhus-t" to 3, "Ars" to 2),
            bilingualKeywords = listOf("teeth marks on tongue", "scalloped tongue", "indented tongue"),
            tissueTropism = "Lingual Musculature"
        ),
        Rubric(
            id = "R_TONGUE_TRIANGLE",
            path = "MOUTH - DISCOLORATION - Tongue - red - tip - triangular",
            chapter = "MOUTH",
            weight = 3,
            grades = mapOf("Rhus-t" to 3, "Ars" to 2, "Sulph" to 2),
            bilingualKeywords = listOf("red triangular tip", "triangle at tip of tongue"),
            tissueTropism = "Lingual Papillae"
        ),
        Rubric(
            id = "R_TONGUE_WHITE",
            path = "MOUTH - DISCOLORATION - Tongue - white - milk, like",
            chapter = "MOUTH",
            weight = 3,
            grades = mapOf("Bry" to 2, "Puls" to 2, "Nux-v" to 2),
            bilingualKeywords = listOf("milky white tongue", "thick white coating on tongue"),
            tissueTropism = "Gastric Reflex"
        ),
        // Multimodal Vision Rubrics: Skin & Nails
        Rubric(
            id = "R_SKIN_HONEY",
            path = "SKIN - ERUPTIONS - discharging - glutinous, sticky, honey-like",
            chapter = "SKIN",
            weight = 3,
            grades = mapOf("Nat-m" to 1, "Rhus-t" to 1),
            bilingualKeywords = listOf("honey like oozing", "sticky yellow discharge", "cracks behind ears"),
            tissueTropism = "Dermis & Epidermis"
        ),
        Rubric(
            id = "R_SKIN_WARTS",
            path = "SKIN - WARTS - bleeding / cauliflower",
            chapter = "SKIN",
            weight = 3,
            grades = mapOf("Thuj" to 3, "Caust" to 3, "Sep" to 1),
            bilingualKeywords = listOf("warts bleeding", "cauliflower warts", "masse"),
            tissueTropism = "Epithelial overgrowth"
        ),
        Rubric(
            id = "R_NAILS_CRUMBLING",
            path = "EXTREMITIES - NAILS - thick, deformed, crumbling",
            chapter = "EXTREMITIES",
            weight = 3,
            grades = mapOf("Sil" to 3, "Thuj" to 2),
            bilingualKeywords = listOf("deformed nails", "thick crumbling nails", "white spots on nails"),
            tissueTropism = "Nail Matrix & Keratin"
        ),
        // Sehgal Mind Rubrics
        Rubric(
            id = "R_SEHGAL_LIGHT",
            path = "MIND - LIGHT - desire for (Knowledge of disease)",
            chapter = "MIND",
            weight = 3,
            grades = mapOf("Ars" to 3, "Phos" to 3, "Nux-v" to 2),
            bilingualKeywords = listOf("wants to know what disease", "will I get cured", "anxious for diagnosis"),
            tissueTropism = "Mind & Intellect"
        ),
        Rubric(
            id = "R_SEHGAL_CARRIED_FAST",
            path = "MIND - CARRIED - desire to be - fast (Instant cure demand)",
            chapter = "MIND",
            weight = 3,
            grades = mapOf("Ars" to 3, "Nux-v" to 2),
            bilingualKeywords = listOf("cure me fast", "immediate relief needed", "cannot wait"),
            tissueTropism = "Mind & Impatience"
        ),
        Rubric(
            id = "R_SEHGAL_WELL",
            path = "MIND - WELL - says he is, when very sick",
            chapter = "MIND",
            weight = 3,
            grades = mapOf("Sep" to 1, "Puls" to 1),
            bilingualKeywords = listOf("says I am fine", "minimizes sickness", "koi badi baat nahi"),
            tissueTropism = "Mind & Denial"
        )
    )

    // 10 Classic Homeopathic Tongue Signs
    val tongueSigns: List<TongueSign> = listOf(
        TongueSign("T1", "Mapped / Geographic Tongue", "Irregular denuded red patches with raised borders like a map", "MOUTH - MAPPED tongue", listOf("Nat-m", "Ars", "Merc", "Lach"), "🗺️"),
        TongueSign("T2", "Indented Teeth Marks on Edges", "Flabby swollen tongue showing scalloped dental impressions", "MOUTH - INDENTED - tongue", listOf("Merc", "Rhus-t", "Chel", "Hydr"), "🦷"),
        TongueSign("T3", "Red Triangular Tip", "White/yellow dorsum with bright-red triangular patch at tip", "MOUTH - DISCOLORATION - Tongue - red - tip - triangular", listOf("Rhus-t", "Ars", "Phyt"), "🔺"),
        TongueSign("T4", "Thick Milky-White Coating", "Heavy uniform white coat as if painted with whitewash", "MOUTH - DISCOLORATION - Tongue - white - milk, like", listOf("Ant-c", "Bry", "Puls"), "🥛"),
        TongueSign("T5", "Golden-Yellow Coating at Base", "Creamy or bright yellow coat concentrated at root of tongue", "STOMACH - PAIN - burning", listOf("Nat-p", "Kali-b", "Chel"), "🟡"),
        TongueSign("T6", "Clean, Smooth, Glazed Red", "Entirely denuded, fiery glossy beefy red tongue", "MOUTH - MAPPED tongue", listOf("Pyrog", "Ter", "Nit-ac", "Apis"), "🥩"),
        TongueSign("T7", "Central Longitudinal Fissure", "Deep vertical crack running right down center of tongue", "MOUTH - MAPPED tongue", listOf("Nit-ac", "Fl-ac", "Nat-m"), "⚡"),
        TongueSign("T8", "Trembling on Protrusion", "Tongue quivers, fibrillates, or catches behind lower teeth", "MIND - RESTLESSNESS - night - midnight, after", listOf("Lach", "Gels", "Merc"), "〰️"),
        TongueSign("T9", "Froth & Foam on Edges", "Stringy frothy saliva bubbles along lateral margins", "MOUTH - MAPPED tongue", listOf("Nat-m", "Ign", "Puls"), "🫧"),
        TongueSign("T10", "Strawberry Tongue", "Prominent erect bright-red papillae through light coating", "GENERALS - SUN - exposure to, agg.", listOf("Bell", "Apis", "Arum-t"), "🍓")
    )

    // Objective Physical Keynote Signs (Skin, Nails, Face)
    val objectiveSigns: List<ObjectivePhysicalSign> = listOf(
        ObjectivePhysicalSign("S1", "SKIN", "Sticky Honey-Like Oozing", "Cracks behind ears, flexures discharging thick viscid fluid", "SKIN - ERUPTIONS - discharging - glutinous, sticky, honey-like", listOf("Graph", "Mez", "Nat-m"), "🍯"),
        ObjectivePhysicalSign("S2", "SKIN", "Deep Bleeding Winter Cracks", "Rough leathery palms/fingertips with bleeding fissures in cold", "SKIN - ERUPTIONS - discharging - glutinous, sticky, honey-like", listOf("Petr", "Sars", "Graph"), "❄️"),
        ObjectivePhysicalSign("S3", "SKIN", "Cauliflower / Bleeding Warts", "Stalked, fan-shaped, or bleeding sycotic excrescences", "SKIN - WARTS - bleeding / cauliflower", listOf("Thuj", "Caust", "Nit-ac"), "🥦"),
        ObjectivePhysicalSign("S4", "NAILS", "Crumbling Deformed Nails", "Thickened, distorted nails with white spots and offensive foot sweat", "EXTREMITIES - NAILS - thick, deformed, crumbling", listOf("Sil", "Ant-c", "Graph"), "💅"),
        ObjectivePhysicalSign("S5", "FACE", "Yellow Saddle Across Nose", "Yellowish-brown chloasma across bridge of nose and cheeks", "GENERALS - FOOD and DRINKS - salt - desire", listOf("Sep"), "🦋"),
        ObjectivePhysicalSign("S6", "FACE", "Bag-like Upper Eyelid Swelling", "Edematous pouching under eyebrow above upper lid", "GENERALS - MOTION - agg.", listOf("Kali-c"), "👁️"),
        ObjectivePhysicalSign("S7", "FACE", "Bag-like Lower Eyelid Swelling", "Puffy watery hanging sacs below lower eyelids", "STOMACH - THIRSTLESS", listOf("Apis", "Ars"), "💧")
    )

    // Lab Investigation Correlators
    val labInvestigations: List<LabInvestigationSign> = listOf(
        LabInvestigationSign(
            id = "LAB-1",
            testName = "Serum Uric Acid (> 7.5 mg/dL)",
            abnormality = "Hyperuricemia / Gouty Diathesis with severe joint stiffness",
            organAffinity = "Kidneys, Small Joints, Big Toe",
            supportiveRemedies = listOf("Ledum", "Colchicum", "Benzoic Acid", "Urtica Urens Q", "Lycopodium"),
            suggestedModalityQuestion = "Are the painful joints relieved by ice-cold water (Ledum) or aggravated by the slightest touch/smell of food (Colchicum)?"
        ),
        LabInvestigationSign(
            id = "LAB-2",
            testName = "Serum TSH (> 8.0 mIU/L)",
            abnormality = "Hypothyroidism with sluggish metabolism, chilliness & weight gain",
            organAffinity = "Thyroid Axis, Endocrine System",
            supportiveRemedies = listOf("Calcarea Carb", "Thyroidinum 3X", "Graphites", "Sepia", "Natrum Mur"),
            suggestedModalityQuestion = "Do you have cold damp feet at night and crave eggs (Calcarea) or feel emotionally indifferent to family (Sepia)?"
        ),
        LabInvestigationSign(
            id = "LAB-3",
            testName = "Liver Function Tests (High SGPT/SGOT, Bilirubin)",
            abnormality = "Hepatocellular injury / Fatty Liver / Sluggish Bile Flow",
            organAffinity = "Liver & Gallbladder",
            supportiveRemedies = listOf("Chelidonium", "Carduus Marianus Q", "Lycopodium", "Natrum Sulph", "Phosphorus"),
            suggestedModalityQuestion = "Do you have a fixed pain under the right shoulder blade (Chelidonium) or gas between 4 PM and 8 PM (Lycopodium)?"
        ),
        LabInvestigationSign(
            id = "LAB-4",
            testName = "Kidney Ultrasound: Left Renal Calculus",
            abnormality = "Left Renal / Ureteric Stone with radiating colicky spasms",
            organAffinity = "Left Kidney & Ureter",
            supportiveRemedies = listOf("Berberis Vulgaris Q", "Pareira Brava Q", "Tabacum", "Cantharis"),
            suggestedModalityQuestion = "Does the pain radiate outward and down into the bladder/thigh with bubbling sensations (Berberis Vulgaris)?"
        ),
        LabInvestigationSign(
            id = "LAB-5",
            testName = "Complete Blood Count: Hemoglobin (< 8.5 g/dL)",
            abnormality = "Microcytic Hypochromic Anemia with paleness and easy breathlessness",
            organAffinity = "Bone Marrow & Erythropoiesis",
            supportiveRemedies = listOf("Ferrum Metallicum", "Ferrum Phos 6X", "China", "Natrum Mur"),
            suggestedModalityQuestion = "Does your face flush bright red from the slightest emotion despite being pale and anemic (Ferrum Met)?"
        )
    )

    // Vocal Biomarkers & Prosody
    val vocalBiomarkers: List<VocalBiomarker> = listOf(
        VocalBiomarker("VB-1", "Rapid Loquacity (>175 WPM)", "Jumps rapidly from one subject to another without finishing", "MIND - AILMENTS FROM - grief", listOf("Lach", "Hyos", "Stram")),
        VocalBiomarker("VB-2", "Involuntary Deep Sighing", "Periodic deep audible inhalation-exhalation breaks in speech", "MIND - SIGHING - involuntary", listOf("Ign", "Nat-m", "Sep")),
        VocalBiomarker("VB-3", "Tearful Weeping Voice", "Pitch tremor and voice cracking while reciting symptoms", "MIND - WEEPING - easily", listOf("Puls", "Sep", "Nat-m")),
        VocalBiomarker("VB-4", "Clipped Hurried Speech", "Interrupts abruptly, speaks with sharp impatience", "MIND - RESTLESSNESS - night - midnight, after", listOf("Nux-v", "Ars", "Hep")),
        VocalBiomarker("VB-5", "Hesitant / Forgets Words", "Long mid-sentence pauses, loses thread of conversation", "MIND - FASTIDIOUS", listOf("Sil", "Lyc", "Merc"))
    )

    // Inimical (Incompatible) Rulebook
    fun checkInimicalCompatibility(previousRemedyCode: String?, selectedRemedyCode: String): InimicalWarning? {
        if (previousRemedyCode == null) return null
        val prev = previousRemedyCode.lowercase()
        val sel = selectedRemedyCode.lowercase()

        return when {
            (prev == "apis" && sel == "rhus-t") || (prev == "rhus-t" && sel == "apis") -> {
                InimicalWarning(
                    remedyA = "Apis Mellifica",
                    remedyB = "Rhus Toxicodendron",
                    warningText = "⛔ DANGEROUS INIMICAL COMBINATION: Apis and Rhus Tox are strictly inimical in classical homeopathy. Alternating or following them spoils the case and causes intense urticarial aggravation.",
                    safeAlternative = "Consider Natrum Mur, Arsenicum, or Bryonia instead."
                )
            }
            (prev == "phos" && sel == "caust") || (prev == "caust" && sel == "phos") -> {
                InimicalWarning(
                    remedyA = "Phosphorus",
                    remedyB = "Causticum",
                    warningText = "⛔ STRICTLY INIMICAL: Hahnemann and Kent warned that Phosphorus and Causticum neutralize and distort each other's curative action.",
                    safeAlternative = "Consider Carbo Veg, Lycopodium, or Sepia instead."
                )
            }
            (prev == "merc" && sel == "sil") || (prev == "sil" && sel == "merc") -> {
                InimicalWarning(
                    remedyA = "Mercurius Sol",
                    remedyB = "Silicea",
                    warningText = "⛔ STRICTLY INIMICAL: Mercurius and Silica should never be given in close succession; they produce violent suppuration or abort healing.",
                    safeAlternative = "Consider Hepar Sulph or Thuja instead."
                )
            }
            (prev == "calc" && sel == "sulph") -> {
                InimicalWarning(
                    remedyA = "Calcarea Carb",
                    remedyB = "Sulphur",
                    warningText = "⚠️ ORDER WARNING: Classical Hahnemannian rule: Sulphur should precede Calcarea Carb, never give Calcarea immediately BEFORE Sulphur.",
                    safeAlternative = "Follow the canonical cycle: Sulphur -> Calcarea -> Lycopodium."
                )
            }
            (prev == "ign" && sel == "nux-v") || (prev == "nux-v" && sel == "ign") -> {
                InimicalWarning(
                    remedyA = "Ignatia",
                    remedyB = "Nux Vomica",
                    warningText = "⚠️ INCOMPATIBLE ANALOGS: Ignatia and Nux Vomica both contain strychnine alkaloids and act as confusing antidotes to one another.",
                    safeAlternative = "Consider Natrum Mur or Sepia instead."
                )
            }
            else -> null
        }
    }

    // 5-School Repertorization Engine
    fun repertorizeWithSchool(activeRubrics: List<Rubric>, school: RepertorySchool): List<RepertorizationEntry> {
        if (activeRubrics.isEmpty()) return emptyList()

        val scoreMap = mutableMapOf<String, Int>()
        val countMap = mutableMapOf<String, Int>()
        val gradesPerRemedy = mutableMapOf<String, MutableMap<String, Int>>()

        for (rubric in activeRubrics) {
            val multiplier = when (school) {
                RepertorySchool.KENT_HIERARCHY -> {
                    when (rubric.chapter) {
                        "MIND" -> 3
                        "GENERALS" -> 2
                        else -> 1
                    }
                }
                RepertorySchool.BOENNINGHAUSEN_TPB -> {
                    // Elevates modalities (< and >) and concomitants
                    if (rubric.path.contains("agg") || rubric.path.contains("amel") || rubric.path.contains("concomitant")) 3 else 1
                }
                RepertorySchool.BOGER_BBCR -> {
                    // Elevates tissue affinity and pathological generals
                    if (rubric.tissueTropism != null) 3 else 1
                }
                RepertorySchool.SANKARAN_KINGDOM -> {
                    if (rubric.chapter == "MIND" || rubric.path.contains("grief") || rubric.path.contains("CONSOLATION")) 3 else 2
                }
                RepertorySchool.SEHGAL_MIND -> {
                    if (rubric.path.contains("SEHGAL") || rubric.chapter == "MIND") 4 else 1
                }
            }

            for ((remedyCode, grade) in rubric.grades) {
                val points = grade * multiplier
                scoreMap[remedyCode] = (scoreMap[remedyCode] ?: 0) + points
                countMap[remedyCode] = (countMap[remedyCode] ?: 0) + 1
                val map = gradesPerRemedy.getOrPut(remedyCode) { mutableMapOf() }
                map[rubric.id] = grade
            }
        }

        val maxScore = scoreMap.values.maxOrNull() ?: 1
        val totalCount = activeRubrics.size

        return scoreMap.mapNotNull { (code, totalScore) ->
            val remedy = allRemedies[code] ?: return@mapNotNull null
            val covered = countMap[code] ?: 0
            val scoreRatio = totalScore.toDouble() / maxScore
            val coverRatio = covered.toDouble() / totalCount
            val confidence = ((scoreRatio * 0.55 + coverRatio * 0.45) * 100).toInt().coerceIn(10, 99)

            RepertorizationEntry(
                remedyCode = code,
                remedyName = remedy.name,
                totalWeightedScore = totalScore,
                rubricsCovered = covered,
                totalRubricsCount = totalCount,
                confidencePercent = confidence,
                gradeInRubric = gradesPerRemedy[code] ?: emptyMap(),
                kingdom = remedy.kingdom
            )
        }.sortedByDescending { it.confidencePercent }
    }

    // Specialized Question Trees by Medical Specialty
    fun getSpecialtyQuestions(specialty: SpecialtyTree): List<FollowUpQuestion> {
        return when (specialty) {
            SpecialtyTree.CONSTITUTIONAL -> listOf(
                FollowUpQuestion("SQ-C1", QuestionPriority.HIGH, QuestionType.MENTAL_CAUSATION, "Did this health problem begin after any deep emotional shock, grief, anger, or bereavement?", "Organon §5: Investigating Exciting Cause (Ailments From)", listOf("Nat-m", "Ign", "Staph"), "MIND - AILMENTS FROM - grief", aphorismCite = "Organon §5"),
                FollowUpQuestion("SQ-C2", QuestionPriority.HIGH, QuestionType.DIFFERENTIATE_REMEDY, "When you feel sad or unwell, how do you react if someone offers comfort or sympathy?", "Consolation < in Nat-m/Ign/Sep; Consolation > in Puls", listOf("Nat-m", "Puls"), "MIND - CONSOLATION - agg.", aphorismCite = "Organon §210"),
                FollowUpQuestion("SQ-C3", QuestionPriority.MEDIUM, QuestionType.PHYSICAL_GENERAL, "How is your thirst throughout the day, and do you crave extra salt, sweets, or warm drinks?", "Physical Generals individualize constitutional polychrests", listOf("Nat-m", "Ars", "Lyc"), "GENERALS - FOOD and DRINKS - salt - desire", aphorismCite = "Organon §105")
            )
            SpecialtyTree.PEDIATRIC -> listOf(
                FollowUpQuestion("SQ-P1", QuestionPriority.HIGH, QuestionType.COMPLETE_LSMC, "When the baby is sick or crying, does carrying them fast (Chamomilla) or slowly in open air (Pulsatilla) soothe them?", "Pediatric motor comfort modality", listOf("Cham", "Puls", "Ars"), "MIND - CONSOLATION - amel.", aphorismCite = "Organon §88"),
                FollowUpQuestion("SQ-P2", QuestionPriority.HIGH, QuestionType.PHYSICAL_GENERAL, "Does the child sweat profusely on the back of the head during sleep, soaking the pillow wet?", "Calcarea Carb keynote (sweat on occiput in sleep)", listOf("Calc", "Sil"), "GENERALS - FOOD and DRINKS - salt - desire", aphorismCite = "Organon §153 PQRS"),
                FollowUpQuestion("SQ-P3", QuestionPriority.MEDIUM, QuestionType.COMPLETE_LSMC, "Does the infant cry or reach out in terror whenever being lowered down into the crib?", "Fear of downward motion in Borax and Gelsemium", listOf("Gels", "Calc"), null, aphorismCite = "Organon §153 PQRS")
            )
            SpecialtyTree.DERMATOLOGY -> listOf(
                FollowUpQuestion("SQ-D1", QuestionPriority.HIGH, QuestionType.MENTAL_CAUSATION, "Were any steroid ointments or creams used in the past to suppress this skin rash before internal symptoms started?", "Uncovering Miasmatic Suppression (Hering's Law)", listOf("Sulph", "Psor", "Graph"), "GENERALS - MOTION - agg.", aphorismCite = "Organon §201"),
                FollowUpQuestion("SQ-D2", QuestionPriority.HIGH, QuestionType.COMPLETE_LSMC, "What happens to the itching when you get into a warm bed at night (Sulphur/Merc) vs undressing (Rumex)?", "Key thermal modality of eruptions", listOf("Sulph", "Merc"), null, aphorismCite = "Boenninghausen LSMC"),
                FollowUpQuestion("SQ-D3", QuestionPriority.MEDIUM, QuestionType.COMPLETE_LSMC, "Is there any discharge from the cracks, and is it thick sticky yellow like honey (Graphites)?", "Morphological discharge keynote of Graphites", listOf("Graph", "Nat-m"), "SKIN - ERUPTIONS - discharging - glutinous, sticky, honey-like", aphorismCite = "Allen Keynotes")
            )
            SpecialtyTree.RHEUMATOLOGY -> listOf(
                FollowUpQuestion("SQ-R1", QuestionPriority.HIGH, QuestionType.DIFFERENTIATE_REMEDY, "Is the joint stiffness worse on first standing up but limbers up after walking (Rhus Tox), or worse from the slightest move (Bryonia)?", "The Classic Motion Paradox of Rheumatology", listOf("Rhus-t", "Bry"), "GENERALS - MOTION - beginning of, agg. - continued, amel.", aphorismCite = "Organon §86"),
                FollowUpQuestion("SQ-R2", QuestionPriority.HIGH, QuestionType.COMPLETE_LSMC, "Does the joint pain move upward from toes and ankles toward the hips (Ledum), or downwards?", "Direction of joint involvement in Ledum vs Kalmia", listOf("Rhus-t", "Lyc"), null, aphorismCite = "Boenninghausen Modality"),
                FollowUpQuestion("SQ-R3", QuestionPriority.MEDIUM, QuestionType.PHYSICAL_GENERAL, "Does putting the painful joint in ice-cold water give surprising relief despite feeling chilly?", "Peculiar PQRS keynote of Ledum Palustre", listOf("Rhus-t", "Bry"), null, aphorismCite = "Organon §153 PQRS")
            )
            SpecialtyTree.GYNECOLOGY -> listOf(
                FollowUpQuestion("SQ-G1", QuestionPriority.HIGH, QuestionType.COMPLETE_LSMC, "Do your headaches, irritability, or pelvic pains vanish the moment your menstrual flow starts (Lachesis)?", "Dramatic relief from onset of discharge in Lachesis & Zincum", listOf("Sep", "Nat-m"), "EXTERNAL THROAT - CLOTHING - agg.", aphorismCite = "Organon §153 PQRS"),
                FollowUpQuestion("SQ-G2", QuestionPriority.HIGH, QuestionType.COMPLETE_LSMC, "Do you feel an intolerable bearing-down sensation in the pelvis as if everything would drop out, needing to cross your legs?", "Hallmark keynote of Sepia Officinalis", listOf("Sep"), "GENERALS - FOOD and DRINKS - salt - desire", aphorismCite = "Allen Keynotes"),
                FollowUpQuestion("SQ-G3", QuestionPriority.MEDIUM, QuestionType.PHYSICAL_GENERAL, "How is your mood before menses—do you feel indifferent to your loved ones and desire vigorous exercise?", "Sepia constitutional state", listOf("Sep", "Puls"), "MIND - CONSOLATION - agg.", aphorismCite = "Organon §210")
            )
            SpecialtyTree.PSYCHIATRY_DREAMS -> listOf(
                FollowUpQuestion("SQ-M1", QuestionPriority.HIGH, QuestionType.MENTAL_CAUSATION, "Do you have recurrent vivid dreams of robbers in the house, waking up and checking under the bed?", "Canonical keynote dream of Natrum Muriaticum", listOf("Nat-m", "Ars"), "MIND - AILMENTS FROM - grief", aphorismCite = "Organon §211"),
                FollowUpQuestion("SQ-M2", QuestionPriority.HIGH, QuestionType.MENTAL_CAUSATION, "Do you frequently dream of snakes, reptiles, or being pursued?", "Lachesis & Arg-n unconscious symbol", listOf("Nat-m", "Ars"), null, aphorismCite = "Organon §211"),
                FollowUpQuestion("SQ-M3", QuestionPriority.MEDIUM, QuestionType.MENTAL_CAUSATION, "Do you dream of strenuous unfinished business or hard day's work, waking unrefreshed?", "Bryonia & Rhus Tox work dreams", listOf("Bry", "Nux-v", "Rhus-t"), "GENERALS - MOTION - agg.", aphorismCite = "Organon §211")
            )
        }
    }

    // LM Potency Preparation Protocol (Organon §246-248)
    fun calculateLmProtocol(potency: String, isHypersensitive: Boolean): LmPotencyCalculation {
        val succussions = if (isHypersensitive) 4 else 8
        val dilutionMethod = if (isHypersensitive) {
            "2nd Cup Method: Take 1 tsp from Glass #1, stir into Glass #2 (100ml water), patient drinks 1 tsp from Glass #2."
        } else {
            "Standard 1st Cup Method: Stir 1 tsp (5ml) into 100ml water, patient drinks 1 tsp and discards remainder."
        }

        val directions = "Dissolve 1 poppy-seed globule of $potency in 100ml distilled water with 15 drops alcohol. Daily: succuss $succussions times before taking dose. Stop immediately if symptoms temporarily intensify."

        return LmPotencyCalculation(
            potency = potency,
            bottleVolumeMl = 100,
            succussions = succussions,
            dilutionMethod = dilutionMethod,
            patientDirections = directions
        )
    }

    // 7-Day WhatsApp Check-In Generator
    fun generateWhatsAppFollowUpMessage(patientName: String, remedyName: String, potency: String, dayNumber: Int): String {
        return """
🌿 Namaste $patientName,
This is a routine Day-$dayNumber check-in from your Homeopathic Doctor regarding your prescription of $remedyName $potency:

Please reply briefly with these 3 quick updates:
1. Overall Vitality: How are your energy, sleep quality, and mood? (Better / Same / Worse)
2. Main Complaint: On a 0-to-10 scale, what is the intensity today?
3. Healing Direction: Did you notice any temporary 1-2 day initial increase, or did any old past cold or skin rash briefly return?

⚠️ Reminder: A brief return of old mild skin eruptions is a natural sign of internal healing (Hering's Law). Please do not apply steroid creams or take suppressing tablets without messaging us first!
        """.trimIndent()
    }

    fun checkRedFlag(text: String): RedFlagAlert? {
        val lower = text.lowercase()
        return when {
            lower.contains("crushing") && (lower.contains("chest") || lower.contains("arm")) ||
            lower.contains("shooting down my left arm") || lower.contains("elephant sitting on") -> {
                RedFlagAlert(
                    id = "RF-CARDIAC",
                    severity = RedFlagSeverity.EMERGENCY,
                    condition = "Suspected Acute Coronary Syndrome (ACS / Myocardial Infarction)",
                    immediateAction = "🚨 CARDIAC EMERGENCY: Call 108/911 for immediate cardiac ER transport. Administer 300mg chewable Aspirin if not contraindicated. Keep patient seated upright, check BP, pulse, SpO2."
                )
            }
            lower.contains("slurred speech") || lower.contains("facial droop") || lower.contains("one side weak") ||
            (lower.contains("thunderclap") && lower.contains("headache")) -> {
                RedFlagAlert(
                    id = "RF-NEURO",
                    severity = RedFlagSeverity.EMERGENCY,
                    condition = "Suspected Acute Cerebrovascular Stroke (CVA) / Subarachnoid Hemorrhage",
                    immediateAction = "🚨 STROKE ALERT (FAST): Immediate emergency transfer to CT/Stroke center within golden hour. Check blood glucose and blood pressure immediately."
                )
            }
            lower.contains("rigid abdomen") || lower.contains("board-like") || lower.contains("vomiting blood") ||
            lower.contains("black tarry stool") -> {
                RedFlagAlert(
                    id = "RF-SURGICAL",
                    severity = RedFlagSeverity.EMERGENCY,
                    condition = "Acute Surgical Abdomen / Massive GI Bleeding",
                    immediateAction = "🚨 SURGICAL RED FLAG: Immediate surgical evaluation. Keep NPO (nil by mouth), secure IV access, urgent ultrasound/CT scan."
                )
            }
            lower.contains("kill myself") || lower.contains("suicide") || lower.contains("end my life") -> {
                RedFlagAlert(
                    id = "RF-PSYCH",
                    severity = RedFlagSeverity.EMERGENCY,
                    condition = "Active Psychiatric Self-Harm Crisis",
                    immediateAction = "🚨 PSYCHIATRIC SAFETY PROTOCOL: Do not leave patient unattended. Involve family immediately and contact mental health crisis intervention."
                )
            }
            else -> null
        }
    }

    fun extractSymptomsAndRubrics(text: String): List<ExtractedSymptom> {
        val lower = text.lowercase()
        val extracted = mutableListOf<ExtractedSymptom>()

        for (rubric in allRubrics) {
            val matchedKeyword = rubric.bilingualKeywords.firstOrNull { kw -> lower.contains(kw.lowercase()) }
            if (matchedKeyword != null) {
                val category = when (rubric.chapter) {
                    "MIND" -> if (rubric.path.contains("AILMENTS FROM")) SymptomCategory.CAUSATION else SymptomCategory.MENTAL_GENERAL
                    "GENERALS" -> SymptomCategory.PHYSICAL_GENERAL
                    else -> SymptomCategory.PARTICULAR
                }

                val hasLoc = rubric.chapter.isNotEmpty()
                val hasSens = lower.contains("burning") || lower.contains("hammering") || lower.contains("throbbing") || lower.contains("stiff") || lower.contains("crushing")
                val hasMod = lower.contains("amel") || lower.contains("agg") || lower.contains("relief") || lower.contains("worse") || lower.contains("better") || lower.contains("sun") || lower.contains("warm") || lower.contains("cold")
                var comp = 40
                if (hasLoc) comp += 20
                if (hasSens) comp += 20
                if (hasMod) comp += 20

                extracted.add(
                    ExtractedSymptom(
                        id = "SX-${rubric.id}-${UUID.randomUUID().toString().take(4)}",
                        category = category,
                        clinicalSummary = rubric.path,
                        verbatimQuote = "\"$matchedKeyword...\"",
                        location = rubric.chapter,
                        sensation = if (hasSens) "Characterized per rubric" else "",
                        modalitiesAggravation = if (rubric.path.contains("agg")) listOf(rubric.path) else emptyList(),
                        modalitiesAmelioration = if (rubric.path.contains("amel")) listOf(rubric.path) else emptyList(),
                        lsmcCompletenessPercent = comp.coerceAtMost(100),
                        linkedRubricPath = rubric.path
                    )
                )
            }
        }
        return extracted
    }

    fun calculateMiasmDistribution(activeRubrics: List<Rubric>): Map<Miasm, Int> {
        var psora = 40
        var sycosis = 20
        var syphilis = 20
        var tubercular = 20

        for (rubric in activeRubrics) {
            when {
                rubric.path.contains("grief") || rubric.path.contains("CONSOLATION") || rubric.path.contains("PAIN") -> psora += 15
                rubric.path.contains("wart") || rubric.path.contains("WARTS") || rubric.path.contains("HONEY") -> sycosis += 20
                rubric.path.contains("burning") || rubric.path.contains("crushing") || rubric.path.contains("CRACKED") -> syphilis += 20
                rubric.path.contains("SUN") || rubric.path.contains("salt") || rubric.path.contains("dry") -> tubercular += 15
            }
        }
        val total = (psora + sycosis + syphilis + tubercular).coerceAtLeast(1)
        return mapOf(
            Miasm.PSORA to (psora * 100 / total),
            Miasm.SYCOSIS to (sycosis * 100 / total),
            Miasm.SYPHILIS to (syphilis * 100 / total),
            Miasm.TUBERCULAR to (tubercular * 100 / total)
        )
    }

    val silentObservations: List<SilentObservation> = listOf(
        SilentObservation("SO-1", "Weeping Easily", "MIND - WEEPING - easily", SymptomCategory.MENTAL_GENERAL),
        SilentObservation("SO-2", "Hates Sympathy/Consolation", "MIND - CONSOLATION - agg.", SymptomCategory.MENTAL_GENERAL),
        SilentObservation("SO-3", "Frequent Deep Sighing", "MIND - SIGHING - involuntary", SymptomCategory.MENTAL_GENERAL),
        SilentObservation("SO-4", "Restless Tossing", "MIND - RESTLESSNESS - night - midnight, after", SymptomCategory.MENTAL_GENERAL),
        SilentObservation("SO-5", "Fastidious / Neat", "MIND - FASTIDIOUS", SymptomCategory.MENTAL_GENERAL),
        SilentObservation("SO-6", "Chilly (Wants Shawl)", "GENERALS - MOTION - agg.", SymptomCategory.PHYSICAL_GENERAL),
        SilentObservation("SO-7", "Hot (Wants Fan/Air)", "STOMACH - THIRSTLESS", SymptomCategory.PHYSICAL_GENERAL),
        SilentObservation("SO-8", "Mapped Tongue", "MOUTH - MAPPED tongue", SymptomCategory.PQRS),
        SilentObservation("SO-9", "Red Triangular Tongue Tip", "MOUTH - DISCOLORATION - Tongue - red - tip - triangular", SymptomCategory.PQRS)
    )

    val all12Pillars: List<PillarStatus> = listOf(
        PillarStatus(1, "1. Causation (Ailments From)", false, "Grief, fright, mortification, anger, cold wet exposure", "Did any shock, grief, or sudden exposure trigger this complaint?"),
        PillarStatus(2, "2. Mind: Emotions & Will", false, "Grief, weeping, consolation response, irritability", "How do you react when sad, and how does consolation affect you?"),
        PillarStatus(3, "3. Mind: Intellect & Fears", false, "Fastidiousness, memory, fear of death/darkness/alone", "What are your deepest fears (darkness, health, failure, solitude)?"),
        PillarStatus(4, "4. Thermal State", false, "Hot vs Chilly, drafts, desire for open air or warmth", "Are you generally a chilly person or do you feel hot easily?"),
        PillarStatus(5, "5. Thirst & Appetite", false, "Quantity, frequency, temperature of water preferred", "How is your thirst: large gulps, frequent small sips, or thirstless?"),
        PillarStatus(6, "6. Food Desires & Aversions", false, "Salt, sweet, sour, spicy, fat, milk, eggs", "What food cravings or aversions do you notice?"),
        PillarStatus(7, "7. Sleep & Dreams", false, "Sleep position, waking times (1-2 AM, 3-4 AM), dreams", "What position do you sleep in and do you wake at specific hours?"),
        PillarStatus(8, "8. Perspiration / Sweat", false, "Profuse, localized (head/feet), odor, staining", "Where do you sweat most, and does sweating relieve you?"),
        PillarStatus(9, "9. Chief Complaint LSMC", false, "Location, Sensation, Modality (< / >), Concomitants", "Can you describe the exact site, feel, and what relieves the pain?"),
        PillarStatus(10, "10. Concomitants", false, "Associated symptoms occurring simultaneously", "When the pain peaks, what other symptoms occur at the same time?"),
        PillarStatus(11, "11. Elimination / Menses", false, "Stool ineffectual urging, diarrhea, menstrual cycle", "How are your bowel movements and cycle regularity?"),
        PillarStatus(12, "12. Miasm & Family History", false, "Suppressed skin eruptions, warts, diabetes, asthma", "Did you have any skin eruptions suppressed with creams in the past?")
    )

    val preloadedCases: List<SimulatedCase> = listOf(
        SimulatedCase(
            id = "CASE-1",
            patientName = "Mrs. Kavita Sharma",
            age = 34,
            gender = "Female",
            mode = CaseMode.CHRONIC,
            title = "Chronic Hammering Migraine after Grief",
            chiefComplaint = "Severe throbbing/hammering headaches 10 AM – 3 PM since loss of father 6 months ago",
            expectedRemedy = "Natrum Muriaticum",
            differentialRemedies = "Ignatia, Sepia, Pulsatilla",
            isEmergency = false,
            turns = listOf(
                SimulatedTurn(Speaker.PATIENT, "Doctor, since my father passed away 6 months ago, my health has gone completely downhill. I have this terrible hammering headache almost every day.", 3500L, "Deep sigh"),
                SimulatedTurn(Speaker.DOCTOR, "I'm so sorry for your loss, Kavita ji. Can you tell me what time of day it usually begins and what it feels like?", 3000L),
                SimulatedTurn(Speaker.PATIENT, "It starts around 10:00 AM as the sun rises, peaks around noon with little hammers pounding in my forehead, and begins easing off after 3:00 PM.", 4000L),
                SimulatedTurn(Speaker.PATIENT, "Dhoop mein nikalna toh bilkul mushkil hai. If I step out in the sun, my head feels like it will burst into pieces.", 3500L),
                SimulatedTurn(Speaker.DOCTOR, "When you feel sad or the headache is severe, how do you feel if your family tries to comfort or console you?", 3000L),
                SimulatedTurn(Speaker.PATIENT, "Honestly doctor, dilasa dene se mujhe aur gussa aata hai! I hate sympathy. I just lock myself in my bedroom and cry alone where nobody can see me.", 4000L),
                SimulatedTurn(Speaker.ATTENDANT, "(Husband): Doctor, she puts heaps of extra salt on her food and gets irritated if I even ask if she needs water.", 3500L)
            )
        ),
        SimulatedCase(
            id = "CASE-2",
            patientName = "Mr. Rajesh Patel",
            age = 42,
            gender = "Male",
            mode = CaseMode.ACUTE,
            title = "Acute Burning Gastritis at 1:30 AM",
            chiefComplaint = "Severe burning epigastric pain and vomiting after street food; extreme restlessness and midnight anguish",
            expectedRemedy = "Arsenicum Album",
            differentialRemedies = "Phosphorus, Nux Vomica, Lycopodium",
            isEmergency = false,
            turns = listOf(
                SimulatedTurn(Speaker.PATIENT, "Doctor sahib, kal raat se pet mein aag lagi hui hai. I ate some outside street food and since midnight I've been in agony.", 3500L),
                SimulatedTurn(Speaker.DOCTOR, "Describe the sensation in your stomach, Rajesh ji. Is it burning, cramping, or cutting?", 3000L),
                SimulatedTurn(Speaker.PATIENT, "It feels like someone placed burning hot coals inside my stomach! And it gets unbearable right around 1:00 to 2:00 AM.", 4000L),
                SimulatedTurn(Speaker.ATTENDANT, "(Wife): He was pacing the room the entire night in terror, doctor! He made me check his pulse ten times and said he won't survive.", 4000L),
                SimulatedTurn(Speaker.DOCTOR, "What happens when you drink water? Do you prefer cold water or warm water?", 3000L),
                SimulatedTurn(Speaker.PATIENT, "My mouth is parched, but I can only take two small sips at a time. Cold water makes me vomit immediately, but sipping hot water gives me instant soothing relief!", 4000L)
            )
        ),
        SimulatedCase(
            id = "CASE-3",
            patientName = "Master Aarav",
            age = 7,
            gender = "Male",
            mode = CaseMode.ACUTE,
            title = "Clingy Pediatric Cough & Earache, Thirstless",
            chiefComplaint = "Loose rattling cough and earache after playing in rain; tearful, clingy, refuses water",
            expectedRemedy = "Pulsatilla Nigricans",
            differentialRemedies = "Chamomilla, Hepar Sulph, Belladonna",
            isEmergency = false,
            turns = listOf(
                SimulatedTurn(Speaker.ATTENDANT, "(Mother): Doctor, Aarav got drenched in rain two days ago. He has developed a bad chest cough and right earache.", 3500L),
                SimulatedTurn(Speaker.ATTENDANT, "He is crying constantly and won't leave my lap. He weeps easily and clings to me.", 3500L),
                SimulatedTurn(Speaker.DOCTOR, "How does he react when you cuddle and comfort him?", 3000L),
                SimulatedTurn(Speaker.ATTENDANT, "Consolation pacifies him immediately! As long as I carry him slowly and hold him close, he smiles through his tears.", 3500L),
                SimulatedTurn(Speaker.ATTENDANT, "In the closed bedroom he feels suffocated. He keeps pointing to the balcony and wants open fresh air.", 3500L),
                SimulatedTurn(Speaker.DOCTOR, "How is his thirst with this fever and cough?", 3000L),
                SimulatedTurn(Speaker.PATIENT, "(Child weeps softly): I don't want water... mouth is dry but I don't want to drink.", 4000L)
            )
        ),
        SimulatedCase(
            id = "CASE-4",
            patientName = "Mr. Deshmukh",
            age = 56,
            gender = "Male",
            mode = CaseMode.CHRONIC,
            title = "Sciatica & Joint Stiffness < First Motion, > Walking",
            chiefComplaint = "Severe right lumbar and sciatic stiffness after working in damp garden; worse resting, better moving",
            expectedRemedy = "Rhus Toxicodendron",
            differentialRemedies = "Bryonia Alba, Ruta Graveolens, Colocynthis",
            isEmergency = false,
            turns = listOf(
                SimulatedTurn(Speaker.PATIENT, "Doctor, my lower back and right leg are so stiff that getting out of bed in the morning is agony.", 3500L),
                SimulatedTurn(Speaker.DOCTOR, "Tell me what happens when you start walking, Mr. Deshmukh.", 3000L),
                SimulatedTurn(Speaker.PATIENT, "Shuru mein uthne par akdan bahut hoti hai. The first few steps feel crippled. But after walking around for 10 minutes, the joints loosen up and I feel so much better!", 4000L),
                SimulatedTurn(Speaker.PATIENT, "Damp weather or rain makes it flared up. At night I can't keep my legs still in bed; I toss and turn continuously.", 3500L),
                SimulatedTurn(Speaker.DOCTOR, "Does warmth or hot fermentation help?", 3000L),
                SimulatedTurn(Speaker.PATIENT, "A hot shower or electric heating pad gives immense relief. Warmth and continuous walking are my only lifesavers.", 3500L)
            )
        ),
        SimulatedCase(
            id = "CASE-5",
            patientName = "Mr. Verma",
            age = 61,
            gender = "Male",
            mode = CaseMode.ACUTE,
            title = "Acute Crushing Chest Pain Radiating to Left Arm",
            chiefComplaint = "CRITICAL: Crushing retrosternal chest pain radiating to left arm and jaw with cold sweat",
            expectedRemedy = "🚨 EMERGENCY ALLOPATHIC TRANSFER (Aconite/Cactus supportive only)",
            differentialRemedies = "Aconite, Cactus Grandiflorus, Arsenic",
            isEmergency = true,
            turns = listOf(
                SimulatedTurn(Speaker.PATIENT, "Doctor... I feel a terrible crushing weight on my chest, like an elephant is sitting on it...", 3000L),
                SimulatedTurn(Speaker.PATIENT, "The pain is shooting down my left arm and up into my jaw... I'm breaking out in cold sweat and can barely breathe...", 3500L),
                SimulatedTurn(Speaker.DOCTOR, "Mr. Verma, sit completely still! Do not exert. Nurse, fetch the vitals monitor and call emergency ambulance immediately!", 2500L)
            )
        )
    )

    fun repertorize(activeRubrics: List<Rubric>): List<RepertorizationEntry> {
        return repertorizeWithSchool(activeRubrics, RepertorySchool.KENT_HIERARCHY)
    }

    fun getFollowUpQuestions(
        activeRubrics: List<Rubric>,
        topRemedies: List<RepertorizationEntry>,
        unexploredPillars: List<Int>
    ): List<FollowUpQuestion> {
        val questions = mutableListOf<FollowUpQuestion>()

        // 1. Modality/Sensation completeness check
        val hasHeadache = activeRubrics.any { it.path.contains("HEAD - PAIN") }
        val hasSunModality = activeRubrics.any { it.path.contains("SUN") }
        if (hasHeadache && !hasSunModality) {
            questions.add(
                FollowUpQuestion(
                    id = "Q-HEAD-SUN",
                    priority = QuestionPriority.HIGH,
                    questionType = QuestionType.COMPLETE_LSMC,
                    questionText = "Does exposure to sunlight, warmth, or physical motion change the intensity of your headache?",
                    clinicalRationale = "Completes Modality (M): Sun/heat agg. strongly indicates Nat-m; > cool open air indicates Puls.",
                    targetRemedies = listOf("Nat-m", "Puls"),
                    expectedRubricIfPositive = "GENERALS - SUN - exposure to, agg.",
                    aphorismCite = "Boenninghausen LSMC Doctrine"
                )
            )
        }

        // 2. Differential question between top 1 and top 2 remedies
        if (topRemedies.size >= 2) {
            val r1 = topRemedies[0].remedyCode
            val r2 = topRemedies[1].remedyCode

            if ((r1 == "Nat-m" && r2 == "Puls") || (r1 == "Puls" && r2 == "Nat-m")) {
                questions.add(
                    FollowUpQuestion(
                        id = "Q-DIFF-CONSOLATION",
                        priority = QuestionPriority.HIGH,
                        questionType = QuestionType.DIFFERENTIATE_REMEDY,
                        questionText = "When you feel low or tearful, does someone trying to comfort you make you feel soothed, or does it irritate you?",
                        clinicalRationale = "Crucial Differential: Consolation aggravates in Natrum Mur; Consolation brings immediate relief in Pulsatilla.",
                        targetRemedies = listOf("Nat-m", "Puls"),
                        expectedRubricIfPositive = "MIND - CONSOLATION - agg.",
                        aphorismCite = "Organon §210 (Mental State in Similimum)"
                    )
                )
            } else if ((r1 == "Ars" && r2 == "Phos") || (r1 == "Phos" && r2 == "Ars")) {
                questions.add(
                    FollowUpQuestion(
                        id = "Q-DIFF-DRINKS",
                        priority = QuestionPriority.HIGH,
                        questionType = QuestionType.DIFFERENTIATE_REMEDY,
                        questionText = "When drinking water, do hot warm drinks give relief, or do you crave icy-cold water?",
                        clinicalRationale = "Direct Materia Medica Keynote: Ars requires warm drinks to relieve stomach burning; Phos craves ice-cold water.",
                        targetRemedies = listOf("Ars", "Phos"),
                        expectedRubricIfPositive = "STOMACH - PAIN - burning - warm drinks, amel.",
                        aphorismCite = "Organon §153 (PQRS Modality)"
                    )
                )
            } else if ((r1 == "Rhus-t" && r2 == "Bry") || (r1 == "Bry" && r2 == "Rhus-t")) {
                questions.add(
                    FollowUpQuestion(
                        id = "Q-DIFF-MOTION",
                        priority = QuestionPriority.HIGH,
                        questionType = QuestionType.DIFFERENTIATE_REMEDY,
                        questionText = "Do you feel better lying completely still, or does walking around slowly limber up the stiffness?",
                        clinicalRationale = "Antagonistic Modality: Bryonia is worse from slightest motion; Rhus-t is better on continued motion.",
                        targetRemedies = listOf("Rhus-t", "Bry"),
                        expectedRubricIfPositive = "GENERALS - MOTION - beginning of, agg. - continued, amel.",
                        aphorismCite = "Organon §86 (Non-Leading Probing)"
                    )
                )
            }
        }

        // 3. Physical General & Causation gaps
        val hasSaltOrSweet = activeRubrics.any { it.path.contains("FOOD and DRINKS") }
        if (!hasSaltOrSweet) {
            questions.add(
                FollowUpQuestion(
                    id = "Q-CRAVING",
                    priority = QuestionPriority.MEDIUM,
                    questionType = QuestionType.PHYSICAL_GENERAL,
                    questionText = "Do you have any strong cravings or aversions for specific foods—such as extra salt, sweets, sour pickles, or spicy dishes?",
                    clinicalRationale = "Uncovers Constitutional Physical Generals: Salt = Nat-m/Phos; Sweets = Lyc/Sulph; Sours = Sep.",
                    targetRemedies = listOf("Nat-m", "Lyc", "Sep", "Phos"),
                    expectedRubricIfPositive = "GENERALS - FOOD and DRINKS - salt - desire",
                    aphorismCite = "Organon §105 (Physical Generals)"
                )
            )
        }

        // 4. Causation / Ailments From check
        val hasCausation = activeRubrics.any { it.path.contains("AILMENTS FROM") }
        if (!hasCausation) {
            questions.add(
                FollowUpQuestion(
                    id = "Q-CAUSATION",
                    priority = QuestionPriority.HIGH,
                    questionType = QuestionType.MENTAL_CAUSATION,
                    questionText = "Looking back, did this illness begin after any major emotional shock, grief, anger, humiliation, or sudden weather exposure?",
                    clinicalRationale = "Hahnemann's Exciting Cause (Causation / Ailments From): Often holds 3x weight in repertorization.",
                    targetRemedies = listOf("Nat-m", "Ign"),
                    expectedRubricIfPositive = "MIND - AILMENTS FROM - grief",
                    aphorismCite = "Organon §5 (Causation & Maintaining Cause)"
                )
            )
        }

        return questions.take(3)
    }

    val kentObservations: List<KentObservation> = listOf(
        KentObservation(1, "Observation 1", "Prolonged aggravation, then slow final recovery", "Deep seated tissue pathology, cure is taking place slowly", "Wait and watch. Give Placebo (Sac Lac). Do not repeat."),
        KentObservation(2, "Observation 2", "Long aggravation, then final decline of patient", "Patient had gross incurable tissue destruction; high potency caused over-reaction", "Antidote or lower potency; palliative care only."),
        KentObservation(3, "Observation 3", "Aggravation is quick, short, and strong, with rapid amelioration", "Ideal homeopathic cure! The remedy was the precise Similimum.", "Wait and watch. Never interfere with vital force."),
        KentObservation(4, "Observation 4", "Recovery without any aggravation", "High vital force and functional disease; perfect Similimum.", "Wait and watch; no repetition needed."),
        KentObservation(5, "Observation 5", "Amelioration comes first, aggravation comes later", "Remedy was palliative, not constitutional similimum.", "Re-take the case thoroughly from constitutional totality.")
    )
}

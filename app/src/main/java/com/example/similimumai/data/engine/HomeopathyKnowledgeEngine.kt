package com.example.similimumai.data.engine

import com.example.similimumai.data.model.*
import java.util.UUID

/**
 * The deterministic clinical heart of Similimum AI (docs/02, docs/engineering/project-structure.md).
 *
 * Reference data (30 polychrests, 160+ rubrics) is delegated to the pure-data objects
 * [PolychrestDatabase] and [RubricDatabase] to keep this file within the project's
 * 500-line source guideline. All algorithms remain pure functions for thread safety
 * and deterministic JVM testing.
 */
object HomeopathyKnowledgeEngine {

    // ─── Reference Data (delegated) ─────────────────────────────────────────
    val polychrests: List<Remedy>
        get() = PolychrestDatabase.all

    val rubrics: List<Rubric>
        get() = RubricDatabase.all

    // QA-suite API surface (docs/engineering/testing-qa-strategy.md)
    val allPolychrests: List<Remedy>
        get() = PolychrestDatabase.all

    val allRubrics: List<Rubric>
        get() = RubricDatabase.all

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

    // QA-suite API surface
    fun checkRedFlag(text: String): RedFlagAlert? = screenForRedFlags(text)

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
            lower.contains("4 to 8") || lower.contains("4-8") || lower.contains("shaam") ||
                (lower.contains("pm") && lower.contains("4") && lower.contains("8")) -> {
                mod = "Aggravated strictly between 4:00 PM and 8:00 PM (< 4-8 PM)"
                matchedRubricId = "r_stomach_bloating_4_8pm"
                isPqrs = true
            }
            lower.contains("warm drink") || lower.contains("hot water") || lower.contains("garam paani") -> {
                mod = "Ameliorated by warm drinks and hot applications (> warm drinks)"
                matchedRubricId = "r_stomach_warm_drinks_amel"
            }
            lower.contains("open air") || lower.contains("thandi hawa") || lower.contains("fresh air") ||
                lower.contains("breeze") -> {
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
            lower.contains("left alone") || lower.contains("alone in the dark") ||
                lower.contains("fear of being alone") || lower.contains("afraid of the dark") -> {
                mod = "Fear of being alone / of the dark"
                matchedRubricId = "r_mind_fear_alone"
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
            lower.contains("ice cold") || lower.contains("ice-cold") ||
                (lower.contains("cold water") && (lower.contains("crave") || lower.contains("ice"))) -> {
                conc = "Craving for ice-cold water / ice cream"
                if (matchedRubricId.isEmpty()) matchedRubricId = "r_stomach_ice_cold"
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

    // ─── Multi-School Repertorization (docs/02 §4) ──────────────────────────
    private fun schoolMultiplier(rubric: Rubric, school: RepertorySchool): Double = when (school) {
        RepertorySchool.KENT -> when (rubric.chapter) {
            "MIND" -> 3.0
            "GENERALITIES" -> 2.0
            else -> 1.0
        }
        RepertorySchool.BOENNINGHAUSEN -> {
            if (rubric.name.contains("ameliorat", ignoreCase = true) || rubric.name.contains("aggravat", ignoreCase = true)) {
                2.5
            } else 1.5
        }
        RepertorySchool.BOGER -> if (rubric.isPqrs) 3.0 else 1.5
    }

    fun calculateRepertorization(
        activeRubrics: List<Rubric>,
        school: RepertorySchool = RepertorySchool.KENT,
        eliminateThermal: ThermalState? = null
    ): List<RemedyScore> {
        val scores = mutableListOf<RemedyScore>()

        // Theoretical maximum for a remedy scoring grade 3 on every active rubric
        val maxPossible = activeRubrics.sumOf { (3 * it.weight * schoolMultiplier(it, school)).toInt() }

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

                    val rubricContribution = (grade * rubric.weight * schoolMultiplier(rubric, school)).toInt()
                    totalWeightedScore += rubricContribution
                }
            }

            val confidencePercent = if (maxPossible > 0) {
                (100 * totalWeightedScore / maxPossible).coerceIn(0, 100)
            } else 0

            scores.add(
                RemedyScore(
                    remedy = remedy,
                    totalScore = totalWeightedScore,
                    rubricsCovered = rubricsCovered,
                    totalRubrics = activeRubrics.size,
                    gradeSum = gradeSum,
                    confidencePercent = confidencePercent
                )
            )
        }

        // Rank by totalScore descending, then by rubricsCovered descending
        return scores.sortedWith(
            compareByDescending<RemedyScore> { it.totalScore }
                .thenByDescending { it.rubricsCovered }
        )
    }

    // QA-suite API surface
    fun repertorize(activeRubrics: List<Rubric>): List<RemedyScore> =
        calculateRepertorization(activeRubrics, RepertorySchool.KENT)

    fun repertorizeWithSchool(
        activeRubrics: List<Rubric>,
        school: RepertorySchool,
        eliminateThermal: ThermalState? = null
    ): List<RemedyScore> = calculateRepertorization(activeRubrics, school, eliminateThermal)

    // Inimical Drug Conflict Checker (docs/02 §5)
    fun checkInimicalConflict(primaryRemedy: Remedy, candidateRemedy: Remedy): Pair<Boolean, String> {
        if (primaryRemedy.inimicalRemedies.contains(candidateRemedy.abbreviation) ||
            candidateRemedy.inimicalRemedies.contains(primaryRemedy.abbreviation)) {
            val reason = "STRICTLY INIMICAL: ${primaryRemedy.abbreviation} and ${candidateRemedy.abbreviation} must NEVER be prescribed in succession or combined! They act as hostile antidotes that trigger violent physiological agitation and vital suppression."
            return Pair(true, reason)
        }
        return Pair(false, "")
    }

    // QA-suite API surface (string-based lookup)
    fun checkInimicalCompatibility(remedyA: String, remedyB: String): String? {
        val a = polychrests.find { it.abbreviation.equals(remedyA, ignoreCase = true) } ?: return null
        val b = polychrests.find { it.abbreviation.equals(remedyB, ignoreCase = true) } ?: return null
        val (isConflict, _) = checkInimicalConflict(a, b)
        return if (isConflict) {
            "DANGEROUS INIMICAL COMBINATION: ${a.abbreviation} and ${b.abbreviation} are strictly inimical — never prescribe in succession or combine them."
        } else null
    }

    // ─── LM 50-Millesimal Posology Protocol (Organon §270-§272) ─────────────
    fun calculateLmProtocol(potency: String, isHypersensitive: Boolean = false): LmProtocol {
        val level = potency.trim().uppercase().removePrefix("LM").trim().toIntOrNull()?.coerceIn(1, 30) ?: 1
        val succussions = if (isHypersensitive) 4 else 10
        val name = "LM $level"
        val source = if (level <= 1) "the LM mother tincture" else "the LM ${level - 1} medicinal solution"

        return LmProtocol(
            potency = name,
            grainsOfMedicine = 4,
            waterDrops = 10,
            dilutionRatio = "1 : 50,000 (one grain : fifty thousand grains of water)",
            succussions = succussions,
            dilutionMethod = "Hahnemann's 2nd Cup Method: 4 grains of $source + 10 drops distilled water; succuss ${succussions} times",
            doseInstructions = "Take 4 drops of the 2nd-cup solution under the tongue, fasting in the morning; prepare a fresh dilution each day",
            splitDosing = if (isHypersensitive) {
                "Hypersensitive constitution: single small morning sip only — do not split or repeat during the day"
            } else {
                "Chronic cases: split the daily 2nd-cup dose — half morning, half evening, never near meals"
            }
        )
    }

    // ─── Kent's 12 Prognostic Observations (J. H. Kent, Prognosis of Homeopathic Treatment) ───
    val kentObservations: List<KentObservation>
        get() = KentObservationDatabase.all

    fun evaluateKentObservation(input: KentReactionInput): KentObservationResult {
        val obs = when {
            input.pattern == KentReactionPattern.NO_REACTION -> kentObservations[9]
            input.pattern == KentReactionPattern.AGGRAVATION && input.severity == KentSeverity.VIOLENT -> kentObservations[6]
            input.pattern == KentReactionPattern.AGGRAVATION && input.outcome == KentOutcome.DEEPENING -> kentObservations[11]
            input.pattern == KentReactionPattern.AGGRAVATION && input.outcome == KentOutcome.RELAPSE -> kentObservations[5]
            input.pattern == KentReactionPattern.AGGRAVATION &&
                    input.duration == KentDuration.PROLONGED && input.outcome == KentOutcome.RAPID_IMPROVEMENT -> kentObservations[0]
            input.pattern == KentReactionPattern.AGGRAVATION &&
                    input.duration == KentDuration.SHORT && input.outcome == KentOutcome.RAPID_IMPROVEMENT -> kentObservations[2]
            input.pattern == KentReactionPattern.AGGRAVATION && input.outcome == KentOutcome.SLOW_IMPROVEMENT -> kentObservations[3]
            input.pattern == KentReactionPattern.AGGRAVATION -> kentObservations[1]
            input.pattern == KentReactionPattern.AMELIORATION && input.oldSuppressedSymptomsReappeared -> kentObservations[7]
            input.pattern == KentReactionPattern.AMELIORATION && input.outcome == KentOutcome.DEEPENING -> kentObservations[8]
            input.pattern == KentReactionPattern.AMELIORATION && input.outcome == KentOutcome.RELAPSE -> kentObservations[5]
            input.pattern == KentReactionPattern.AMELIORATION && input.outcome == KentOutcome.SLOW_IMPROVEMENT -> kentObservations[10]
            input.pattern == KentReactionPattern.AMELIORATION -> kentObservations[4]
            else -> kentObservations[1]
        }
        return KentObservationResult(
            observation = obs,
            action = obs.action,
            summary = "Kent Observation ${obs.number}: ${obs.title} — ${obs.action.label}"
        )
    }

    // Evaluate Hering's Law of Cure (docs/02 §6)
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

    // ─── Vision Lab: visual inspection findings & remedy associations ──────
    val visualFindings: List<VisualFinding> = listOf(
        // Tongue
        VisualFinding("v_tongue_red_tip", VisionPanel.TONGUE, "Red, dry tongue with red triangular tip", listOf("Rhus-t", "Phos", "Bell"), "r_mouth_tongue_red_tip", "MOUTH"),
        VisualFinding("v_tongue_imprints", VisionPanel.TONGUE, "Swollen flabby tongue with teeth imprints", listOf("Merc", "Puls", "Calc"), "r_mouth_tongue_imprints", "MOUTH"),
        VisualFinding("v_tongue_pale", VisionPanel.TONGUE, "Pale swollen tongue, trembles at the tip", listOf("Calc", "Puls", "Chin"), "r_mouth_tongue_pale_swollen", "MOUTH"),
        VisualFinding("v_tongue_geographic", VisionPanel.TONGUE, "Mapped / geographic tongue with smooth patches", listOf("Nat-m", "Puls", "Ign"), "r_mouth_tongue_geographic", "MOUTH"),
        VisualFinding("v_tongue_yellow", VisionPanel.TONGUE, "Thick yellow or brownish coating", listOf("Lyc", "Calc", "Puls"), "r_mouth_tongue_yellow_coating", "MOUTH"),
        VisualFinding("v_tongue_white", VisionPanel.TONGUE, "Clean white coating, thin and moist", listOf("Puls", "Sil", "Chin"), "r_mouth_tongue_white_coating", "MOUTH"),
        VisualFinding("v_tongue_scalded", VisionPanel.TONGUE, "Dry red tongue, scalded-looking", listOf("Bell", "Phos", "Ars"), "r_mouth_tongue_dry_red", "MOUTH"),
        VisualFinding("v_tongue_cracked", VisionPanel.TONGUE, "Cracked, fissured tongue in the middle", listOf("Nat-m", "Phos", "Sulph"), "r_mouth_tongue_cracked", "MOUTH"),
        VisualFinding("v_tongue_trembling", VisionPanel.TONGUE, "Tremulous tongue, shakes when protruded", listOf("Merc", "Bry", "Bell"), "r_mouth_tongue_trembling", "MOUTH"),
        // Skin & perspiration
        VisualFinding("v_skin_itchy", VisionPanel.SKIN, "Itchy eruptions, worse warm bed", listOf("Sulph", "Puls"), "r_skin_eruption_itchy", "SKIN"),
        VisualFinding("v_skin_puffy", VisionPanel.SKIN, "Stinging, puffy, edematous swelling", listOf("Apis", "Kali-c"), "r_skin_stinging_puffy", "SKIN"),
        VisualFinding("v_skin_warts", VisionPanel.SKIN, "Warts and warty excrescences", listOf("Thuj", "Sep"), "r_skin_warts", "SKIN"),
        VisualFinding("v_skin_bruised", VisionPanel.SKIN, "Skin sore as if beaten or bruised", listOf("Arn", "Bell"), "r_skin_bruised_sore", "SKIN"),
        VisualFinding("v_skin_offensive_sweat", VisionPanel.SKIN, "Offensive sour perspiration (feet/body)", listOf("Sil", "Merc"), "r_skin_sweat_offensive", "SKIN"),
        VisualFinding("v_skin_flexural", VisionPanel.SKIN, "Eczema in the flexures of the joints", listOf("Puls", "Sil", "Merc"), "r_skin_flexural_eczema", "SKIN"),
        VisualFinding("v_skin_burning_beds", VisionPanel.SKIN, "Burning skin, worse warm bed, better uncovered", listOf("Sulph", "Puls", "Phos"), "r_skin_burning_beds", "SKIN"),
        // General physical signs
        VisualFinding("v_gen_one_cheek", VisionPanel.GENERAL, "One cheek hot and red, other pale and cool", listOf("Cham", "Puls"), "r_face_one_cheek", "FACE"),
        VisualFinding("v_gen_saddle_nose", VisionPanel.GENERAL, "Brownish saddle discoloration across the nose", listOf("Sep"), "r_face_saddle_nose", "FACE"),
        VisualFinding("v_gen_cold_feet", VisionPanel.GENERAL, "Feet cold and damp, like wet stockings", listOf("Calc", "Nux-v"), "r_ext_cold_feet", "GENERALITIES"),
        VisualFinding("v_gen_burning_soles", VisionPanel.GENERAL, "Burning soles of feet, worse at night", listOf("Sulph", "Phos"), "r_ext_burning_soles", "GENERALITIES"),
        VisualFinding("v_gen_head_sweat", VisionPanel.GENERAL, "Profuse sweat on head and neck during sleep", listOf("Calc", "Merc"), "r_skin_sweat_head", "SKIN"),
        VisualFinding("v_gen_puffy_eyelids", VisionPanel.GENERAL, "Puffy upper eyelids and swollen lips", listOf("Kali-c", "Apis"), "r_eyes_lids_puffy", "EYES")
    )

    /** Remedies classically associated with a recorded visual finding. */
    fun remediesForFinding(finding: VisualFinding): List<Remedy> =
        polychrests.filter { finding.remedyCodes.contains(it.abbreviation) }

    // ─── Plain-Text Case Sheet & Prescription Generator (MVP MVE criterion 4) ───
    fun generateCaseSheet(
        patientName: String,
        patientAge: Int,
        patientSex: String,
        thermal: ThermalState,
        miasm: Miasm,
        chiefComplaint: String,
        caseMode: String,
        symptoms: List<Symptom>,
        activeRubrics: List<Rubric>,
        remedyScores: List<RemedyScore>,
        rxRemedy: String,
        rxPotency: String,
        rxScale: String,
        rxPosology: String,
        dietaryRestrictions: List<String>,
        hering: HeringEvaluation? = null,
        lmProtocol: LmProtocol? = null
    ): String {
        val sb = StringBuilder()
        sb.appendLine("═══════════════════════════════════════════════")
        sb.appendLine("        SIMILIMUM AI — CLINICAL CASE SHEET")
        sb.appendLine("═══════════════════════════════════════════════")
        sb.appendLine()
        sb.appendLine("PATIENT: $patientName, ${patientAge} ${patientSex}")
        sb.appendLine("THERMAL: ${thermal.label()}   |   MIASM: ${miasm.name}")
        sb.appendLine("CASE MODE: $caseMode")
        sb.appendLine("CHIEF COMPLAINT: $chiefComplaint")
        sb.appendLine()
        sb.appendLine("── SYMPTOM TOTALITY (LSMC) ──────────────────")
        if (symptoms.isEmpty()) {
            sb.appendLine("(No symptoms recorded)")
        }
        symptoms.forEachIndexed { i, s ->
            sb.appendLine("${i + 1}. [${s.location}] ${s.sensation} (intensity ${s.intensity}/3${if (s.isPqrs) " — PQRS §153" else ""})")
            if (s.modalities.isNotBlank()) sb.appendLine("   Modality: ${s.modalities}")
            if (s.concomitants.isNotBlank()) sb.appendLine("   Concomitant: ${s.concomitants}")
        }
        sb.appendLine()
        sb.appendLine("── ACTIVE REPERTORY RUBRICS (${activeRubrics.size}) ────────────")
        activeRubrics.forEachIndexed { i, r ->
            val pqrsTag = if (r.isPqrs) "  [PQRS]" else ""
            sb.appendLine("${i + 1}. ${r.path} (w${r.weight})$pqrsTag")
        }
        sb.appendLine()
        sb.appendLine("── TOP SIMILIMUM CANDIDATES ──────────────────")
        remedyScores.filter { it.totalScore > 0 }.take(5).forEachIndexed { i, s ->
            sb.appendLine("${i + 1}. ${s.remedy.fullName.padEnd(28)} score=${s.totalScore}  coverage=${s.rubricsCovered}/${s.totalRubrics}  confidence=${s.confidencePercent}%")
        }
        if (remedyScores.none { it.totalScore > 0 }) sb.appendLine("(No active rubrics scored)")
        sb.appendLine()
        sb.appendLine("── PRESCRIPTION (Organon §245-§285) ──────────")
        sb.appendLine("REMEDY: $rxRemedy $rxPotency  [$rxScale scale]")
        sb.appendLine("POSOLOGY: $rxPosology")
        lmProtocol?.let { lm ->
            sb.appendLine("LM PROTOCOL: ${lm.dilutionMethod}; ${lm.succussions} succussions; ratio ${lm.dilutionRatio}")
            sb.appendLine("  Dose: ${lm.doseInstructions}")
            sb.appendLine("  Split dosing: ${lm.splitDosing}")
        }
        sb.appendLine("DIETARY PROHIBITIONS: ${dietaryRestrictions.joinToString(", ")}")
        hering?.let { h ->
            sb.appendLine()
            sb.appendLine("── HERING'S LAW (follow-up) ──────────────────")
            sb.appendLine("VERDICT: ${h.prognosisVerdict}")
            sb.appendLine("GUIDANCE: ${h.clinicalGuidance}")
        }
        sb.appendLine()
        sb.appendLine("This case sheet was generated by Similimum AI as clinical")
        sb.appendLine("decision support. Final prescription remains the doctor's")
        sb.appendLine("professional judgment per classical Homeopathic practice.")
        return sb.toString()
    }
}

private fun ThermalState.label(): String = when (this) {
    ThermalState.CHILLY -> "Chilly (Sensitive to Cold)"
    ThermalState.HOT -> "Hot / Warm-Blooded (Sensitive to Heat)"
    ThermalState.AMBITHERMAL -> "Ambithermal (Sensitive to both)"
}

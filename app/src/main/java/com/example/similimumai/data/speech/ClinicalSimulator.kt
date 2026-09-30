package com.example.similimumai.data.speech

data class SimulatedCase(
    val id: String,
    val title: String,
    val patientProfile: String,
    val expectedRemedy: String,
    val turns: List<SimulatedTurn>
)

data class SimulatedTurn(
    val speaker: String, // "Patient" or "Doctor"
    val utterance: String,
    val delayMs: Long = 2000
)

object ClinicalSimulator {

    val availableCases: List<SimulatedCase> = listOf(
        SimulatedCase(
            id = "case_nat_m",
            title = "Case 1: Natrum Mur — Chronic Migraine & Silent Grief",
            patientProfile = "Priya S., 32F, Severe throbbing headaches for 8 months",
            expectedRemedy = "Nat-m 200C",
            turns = listOf(
                SimulatedTurn("Doctor", "Namaste Priya ji, please tell me what trouble brings you to the clinic today?"),
                SimulatedTurn("Patient", "Doctor, I have been suffering from terrible blinding headaches almost every week. The pain starts around 9 AM when the sun rises, reaches its worst at noon, and only eases as the sun goes down."),
                SimulatedTurn("Doctor", "Can you describe what the headache actually feels like inside your head?"),
                SimulatedTurn("Patient", "It feels like a hundred little hammers pounding inside my temples and forehead (dhoop mein sar fatne lagta hai). Light and any conversation makes it so much worse."),
                SimulatedTurn("Doctor", "When people try to sympathize with you or comfort you during this pain, how do you feel?"),
                SimulatedTurn("Patient", "I despise sympathy. When my mother or husband tries to console me, I get furious. I just want to lock myself in a dark silent room and weep alone."),
                SimulatedTurn("Doctor", "Tell me about your food habits, cravings, and any emotional stress before these headaches started."),
                SimulatedTurn("Patient", "I have an uncontrollable craving for extra salt on everything. And truth be told, doctor, these headaches began after a devastating betrayal by my former business partner last year that broke my heart.")
            )
        ),
        SimulatedCase(
            id = "case_lyc",
            title = "Case 2: Lycopodium — Chronic GERD & 4–8 PM Flatulence",
            patientProfile = "Rajesh K., 46M, Chronic indigestion, gas & acid reflux",
            expectedRemedy = "Lyc 200C",
            turns = listOf(
                SimulatedTurn("Doctor", "Hello Rajesh ji, what digestive symptoms have you been struggling with?"),
                SimulatedTurn("Patient", "Doctor, my stomach becomes a drum with gas every single day strictly between 4:00 PM and 8:00 PM. I feel so bloated I have to loosen my belt and trouser waist."),
                SimulatedTurn("Doctor", "How is your appetite, and what happens when you sit down for a meal?"),
                SimulatedTurn("Patient", "I feel famished before sitting down, but after just two or three mouthfuls I feel completely stuffed and unable to take another bite."),
                SimulatedTurn("Doctor", "What kind of drinks or foods feel best on your stomach?"),
                SimulatedTurn("Patient", "I can only drink piping hot water or warm tea. Cold water from the fridge makes my stomach knot up. And I crave sweets and desserts constantly."),
                SimulatedTurn("Doctor", "Any right-sided pains or stress at work?"),
                SimulatedTurn("Patient", "Yes! The gas pain is mostly on the right side of my abdomen. At work, I get terrible anticipatory stage fright before any board meeting, though once I begin speaking I do fine.")
            )
        ),
        SimulatedCase(
            id = "case_ars_alb",
            title = "Case 3: Arsenicum Alb — Acute Gastroenteritis & Midnight Agony",
            patientProfile = "Aman V., 29M, Sudden vomiting, purging after contaminated food",
            expectedRemedy = "Ars 30C",
            turns = listOf(
                SimulatedTurn("Doctor", "Aman, you look very exhausted. When did this acute diarrhea and vomiting start?"),
                SimulatedTurn("Patient", "Doctor, last night around midnight I woke up with violent burning pain in my stomach and watery stools. It was at 1:00 AM when the burning reached its peak."),
                SimulatedTurn("Doctor", "Tell me about your thirst and what you feel like drinking."),
                SimulatedTurn("Patient", "My mouth is parched dry, but I can only swallow a small sip of warm water at a time (thoda thoda paani baar baar peeta hoon). Cold water burns my stomach."),
                SimulatedTurn("Doctor", "How are you feeling emotionally and physically right now?"),
                SimulatedTurn("Patient", "I am terribly restless. I cannot stay in bed, I pace back and forth, but my legs are trembling with weakness. Doctor, please tell me I won't die, I have this terrible anxiety!")
            )
        ),
        SimulatedCase(
            id = "case_phos",
            title = "Case 4: Phosphorus — Post-Viral Cough & Cold Water Craving",
            patientProfile = "Meera D., 24F, Persistent dry tickling cough after bronchitis",
            expectedRemedy = "Phos 30C",
            turns = listOf(
                SimulatedTurn("Doctor", "Meera, tell me about this persistent cough you've had."),
                SimulatedTurn("Patient", "Doctor, ever since my viral flu two weeks ago, I have this raw tickling cough in my throat and chest whenever I speak or go into cold air."),
                SimulatedTurn("Doctor", "What drinks do you feel like having right now?"),
                SimulatedTurn("Patient", "I crave ice-cold water and ice cream like crazy! Drinking ice cold water soothes my chest right away, though once it warms in my stomach I feel slightly nauseated."),
                SimulatedTurn("Doctor", "Any fears or sleep issues?"),
                SimulatedTurn("Patient", "I get terrified when left alone in the dark, and summer thunderstorms scare me terribly. I feel burning heat between my shoulder blades at night.")
            )
        ),
        SimulatedCase(
            id = "case_puls",
            title = "Case 5: Pulsatilla — Pediatric Otitis Media & Clinginess",
            patientProfile = "Aarav (Mother reporting), 6M, Ear pain following cold",
            expectedRemedy = "Puls 30C",
            turns = listOf(
                SimulatedTurn("Doctor", "Namaste, how is young Aarav doing?"),
                SimulatedTurn("Patient", "Doctor, he has had painful earache since yesterday. He has become so gentle, tearful, and clingy. He cries if I leave his side for a moment and wants continuous cuddles."),
                SimulatedTurn("Doctor", "How is his thirst, and does he prefer warm or cool environments?"),
                SimulatedTurn("Patient", "He hasn't asked for a single glass of water all day, completely thirstless despite fever! And when we took him outdoors into the open cool evening breeze, he immediately calmed down.")
            )
        )
    )
}

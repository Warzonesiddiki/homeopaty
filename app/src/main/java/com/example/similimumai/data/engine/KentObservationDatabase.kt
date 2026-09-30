package com.example.similimumai.data.engine

import com.example.similimumai.data.model.*

/**
 * Kent's 12 Prognostic Observations — reference data
 * (J. H. Kent, "The Prognosis of Homeopathic Treatment").
 */
object KentObservationDatabase {
    val all: List<KentObservation> = listOf(
    KentObservation(
        number = 1,
        title = "Prolonged Aggravation, Then Rapid Decline",
        description = "Aggravation lasts several hours, then subsides and is followed by rapid improvement.",
        action = KentObservationAction.WAIT_AND_WATCH_SAC_LAC,
        clinicalGuidance = "The prolonged reaction is the vital force throwing off disease. Do not interfere; give only Sac Lac and wait."
    ),
    KentObservation(
        number = 2,
        title = "Mild Transient Aggravation",
        description = "Slight, brief aggravation that fades without disturbance; improvement is pending.",
        action = KentObservationAction.WAIT_AND_WATCH_SAC_LAC,
        clinicalGuidance = "Normal initial reaction to the similimum. Observe silently; no dose is to be repeated."
    ),
    KentObservation(
        number = 3,
        title = "Quick Short Aggravation, Rapid Recovery",
        description = "Sharp but brief aggravation followed by prompt, marked recovery.",
        action = KentObservationAction.WAIT_AND_WATCH_SAC_LAC,
        clinicalGuidance = "Classic sign of a well-chosen similimum: short sharp aggravation then long cure. Cure is in progress."
    ),
    KentObservation(
        number = 4,
        title = "Moderate Aggravation, Gradual Amelioration",
        description = "Moderate aggravation followed by slow, steady improvement over days.",
        action = KentObservationAction.WAIT_AND_WATCH_SAC_LAC,
        clinicalGuidance = "Favorable vital reaction with a slower tempo. Wait and watch; re-evaluate in 7 days."
    ),
    KentObservation(
        number = 5,
        title = "Amelioration First, Aggravation Later",
        description = "Immediate improvement followed by a temporary aggravation that then settles.",
        action = KentObservationAction.WAIT_AND_WATCH_SAC_LAC,
        clinicalGuidance = "The delayed aggravation is the vital reaction surfacing after early relief. It settles without interference."
    ),
    KentObservation(
        number = 6,
        title = "Too-Short Relief (Premature Cessation)",
        description = "Relief was brief; the original symptoms return after the reaction ceases.",
        action = KentObservationAction.REPEAT_SAME_POTENCY,
        clinicalGuidance = "The reaction has ceased prematurely and the remedy effect is spent. Repeat the same potency once, then observe."
    ),
    KentObservation(
        number = 7,
        title = "Violent Aggravation with General Decline",
        description = "Prostrating, violent aggravation with delirium, cold sweat, and failing strength.",
        action = KentObservationAction.ANTIDOTE_IMMEDIATELY,
        clinicalGuidance = "Dangerous over-action of the remedy. Antidote immediately (cold applications, strong coffee or tea, or Camphora) and reduce the dose."
    ),
    KentObservation(
        number = 8,
        title = "Return of Old Suppressed Symptoms",
        description = "Old, long-suppressed superficial symptoms re-emerge in reverse order of appearance.",
        action = KentObservationAction.WAIT_AND_WATCH_SAC_LAC,
        clinicalGuidance = "Heringian cure: the vital force is re-running the old disease course in reverse. Do not repeat; let it complete."
    ),
    KentObservation(
        number = 9,
        title = "Centripetal Deepening (Wrong Direction)",
        description = "Superficial complaints clear while deeper organ or mental symptoms intensify.",
        action = KentObservationAction.CHANGE_REMEDY,
        clinicalGuidance = "Movement is centripetal — the opposite of Hering's direction. The similimum is wrong; change remedy on fresh case-taking."
    ),
    KentObservation(
        number = 10,
        title = "No Reaction at All",
        description = "No aggravation and no improvement after the full dose and adequate observation.",
        action = KentObservationAction.INCREASE_POTENCY,
        clinicalGuidance = "The vital force did not register the dose. Increase potency (next centesimal step or next LM level) and re-observe."
    ),
    KentObservation(
        number = 11,
        title = "Favorable Reaction, Incomplete Cure",
        description = "Direction is good but disease clearly remains; new curative symptoms appear.",
        action = KentObservationAction.INCREASE_POTENCY,
        clinicalGuidance = "Cure is underway but the totality is not exhausted. Increase potency one step for the next repetition."
    ),
    KentObservation(
        number = 12,
        title = "Suppression: Superficial Loss, Vital Aggravation",
        description = "Local symptoms vanish while vital-organ or mental symptoms worsen.",
        action = KentObservationAction.CHANGE_REMEDY,
        clinicalGuidance = "Classic suppression — likely inimical interference or wrong remedy. Change remedy after re-evaluating the full totality."
    )
    )
}

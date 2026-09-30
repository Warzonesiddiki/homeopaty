package com.example.similimumai.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.similimumai.data.engine.HomeopathyKnowledgeEngine
import com.example.similimumai.data.model.*
import com.example.similimumai.data.speech.ClinicalSimulator
import com.example.similimumai.ui.components.AudioWaveformVisualizer
import com.example.similimumai.ui.components.MiniRemedyLeaderboard
import com.example.similimumai.ui.theme.*
import com.example.similimumai.R
import com.example.similimumai.ui.viewmodel.ConsultationUiState
import com.example.similimumai.ui.viewmodel.ConsultationViewModel
import com.example.similimumai.ui.viewmodel.NavigationTab

/**
 * Live ambient HUD workspace.
 *
 * docs/design/responsive.md §2.1 — Adaptive Screen Strategy:
 *  - Compact (phones): vertical single-column stack (Question Deck pinned to
 *    top, silent observation chips, Live Transcript filling the remaining
 *    screen height).
 *  - Expanded (tablets/foldables): dual-pane — left 45% audio waveform /
 *    live transcript / manual text input, right 55% question deck / AI
 *    synthesis / mini-leaderboard running concurrently.
 */
@Composable
fun HudScreen(
    uiState: ConsultationUiState,
    viewModel: ConsultationViewModel,
    modifier: Modifier = Modifier
) {
    var manualUtterance by remember { mutableStateOf("") }
    var showCasePicker by remember { mutableStateOf(false) }

    val windowSizeClass = computeWindowSizeClass()

    if (windowSizeClass.isExpanded) {
        Row(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .testTag("hud_screen"),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Left pane (45%): audio ingestion, waveform, live transcript,
            // manual doctor input (responsive.md §2.1)
            LazyColumn(
                modifier = Modifier
                    .weight(0.45f)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Spacer(modifier = Modifier.height(4.dp))
                    AudioControlCard(
                        uiState = uiState,
                        onToggleMic = { viewModel.toggleMicrophone(!uiState.isMicListening) },
                        onSelectSimCase = { showCasePicker = true },
                        onStopSim = { viewModel.stopSimulation() }
                    )
                }
                item { HudWaveformCard(uiState) }
                item { HudTranscriptHeader(uiState) }
                if (uiState.transcript.isEmpty()) {
                    item {
                        HudTranscriptEmptyState {
                            viewModel.processUtterance(it, SpeakerType.PATIENT)
                        }
                    }
                } else {
                    items(uiState.transcript) { entry ->
                        TranscriptBubble(entry = entry)
                    }
                }
                item {
                    HudInputBar(
                        utterance = manualUtterance,
                        onValueChange = { manualUtterance = it },
                        onSubmit = {
                            if (manualUtterance.isNotBlank()) {
                                viewModel.processUtterance(manualUtterance, SpeakerType.PATIENT)
                                manualUtterance = ""
                            }
                        }
                    )
                }
            }

            // Right pane (55%): question deck, AI synthesis and the
            // persistent mini-leaderboard (responsive.md §2.1)
            LazyColumn(
                modifier = Modifier
                    .weight(0.55f)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Spacer(modifier = Modifier.height(4.dp))
                    HudObservationChips { viewModel.addDoctorObservation(it) }
                }
                if (uiState.highYieldQuestions.isNotEmpty()) {
                    item { HudQuestionDeck(uiState) }
                }
                item {
                    HudAiSynthesisCard(uiState) { viewModel.triggerGeminiDeepAnalysis() }
                }
                item {
                    MiniRemedyLeaderboard(
                        scores = uiState.remedyScores,
                        onRemedyClick = { remedy ->
                            viewModel.selectRemedyDetail(remedy)
                            viewModel.selectNavigationTab(NavigationTab.MATERIA_MEDICA)
                        }
                    )
                }
            }
        }
    } else {
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .testTag("hud_screen"),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(4.dp))
                // Ambient Audio & Case Simulator Control Card
                AudioControlCard(
                    uiState = uiState,
                    onToggleMic = { viewModel.toggleMicrophone(!uiState.isMicListening) },
                    onSelectSimCase = { showCasePicker = true },
                    onStopSim = { viewModel.stopSimulation() }
                )
            }

            // Live Audio Waveform & Status Indicator
            item { HudWaveformCard(uiState) }

            // Doctor Silent Physical Observations Section
            item { HudObservationChips { viewModel.addDoctorObservation(it) } }

            // Ask-Next High-Yield Questions Cards
            if (uiState.highYieldQuestions.isNotEmpty()) {
                item { HudQuestionDeck(uiState) }
            }

            // Gemini AI Deep Constitutional Synthesis Card
            item {
                HudAiSynthesisCard(uiState) { viewModel.triggerGeminiDeepAnalysis() }
            }

            // Verbatim Dialogue Transcript
            item { HudTranscriptHeader(uiState) }

            if (uiState.transcript.isEmpty()) {
                // ux-states.md §2.1 + ASSUMPTION-UX-01: zero-data state offers
                // actionable 1-tap sample utterances for first-time exploration.
                item {
                    HudTranscriptEmptyState {
                        viewModel.processUtterance(it, SpeakerType.PATIENT)
                    }
                }
            } else {
                items(uiState.transcript) { entry ->
                    TranscriptBubble(entry = entry)
                }
            }

            // Manual Doctor Note / Utterance Entry Bar
            item {
                HudInputBar(
                    utterance = manualUtterance,
                    onValueChange = { manualUtterance = it },
                    onSubmit = {
                        if (manualUtterance.isNotBlank()) {
                            viewModel.processUtterance(manualUtterance, SpeakerType.PATIENT)
                            manualUtterance = ""
                        }
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }

    // Case Selection Dialog
    if (showCasePicker) {
        AlertDialog(
            onDismissRequest = { showCasePicker = false },
            title = {
                Text(
                    text = stringResource(R.string.hud_select_simulation),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    ClinicalSimulator.availableCases.forEach { caseItem ->
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .border(1.dp, EmeraldPrimary.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                                .clickable {
                                    viewModel.startSimulation(caseItem)
                                    showCasePicker = false
                                }
                                .padding(10.dp)
                                .testTag("select_case_${caseItem.id}")
                        ) {
                            Column {
                                Text(
                                    text = caseItem.title,
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = EmeraldPrimary
                                )
                                Text(
                                    text = caseItem.patientProfile,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = "Target Similimum: ${caseItem.expectedRemedy}",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.SemiBold,
                                    color = IndigoGenerals
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showCasePicker = false }) {
                    Text(stringResource(R.string.common_close))
                }
            }
        )
    }
}

/** Live audio waveform + streaming status card (responsive.md §2.1 left pane). */
@Composable
private fun HudWaveformCard(uiState: ConsultationUiState) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(12.dp),
        shadowElevation = 1.dp
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (uiState.isMicListening) "AMBIENT MICROPHONE STREAMING"
                    else if (uiState.isSimulationRunning) "SIMULATING: ${uiState.currentSimulatedCase?.title ?: ""}"
                    else "STANDBY — READY FOR CONSULTATION",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (uiState.isMicListening || uiState.isSimulationRunning) EmeraldPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (uiState.isMicListening || uiState.isSimulationRunning) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(CrimsonRedFlag)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            AudioWaveformVisualizer(
                rmsLevel = uiState.liveRmsDb,
                isActive = uiState.isMicListening || uiState.isSimulationRunning
            )
        }
    }
}

/** Doctor silent physical observation chips (responsive.md §2.1). */
@Composable
private fun HudObservationChips(
    onObservation: (DoctorObservationOption) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(
            text = stringResource(R.string.hud_silent_observation_title),
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(HomeopathyKnowledgeEngine.doctorObservationOptions) { obs ->
                FilterChip(
                    selected = false,
                    onClick = { onObservation(obs) },
                    label = {
                        Text(
                            text = obs.label,
                            style = MaterialTheme.typography.labelSmall
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp),
                            tint = IndigoGenerals
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        containerColor = IndigoContainer.copy(alpha = 0.5f),
                        labelColor = IndigoOnContainer
                    ),
                    modifier = Modifier.testTag("obs_chip_${obs.id}").minimumInteractiveComponentSize())
            }
        }
    }
}

/** Ask-next high-yield question deck (responsive.md §2.1 right pane). */
@Composable
private fun HudQuestionDeck(uiState: ConsultationUiState) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.hud_ask_next_title),
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = AmberModalities
            )
            Text(
                text = stringResource(R.string.hud_totality_label),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        uiState.highYieldQuestions.forEach { q ->
            Card(
                colors = CardDefaults.cardColors(containerColor = AmberContainer.copy(alpha = 0.35f)),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("question_card_${q.id}")
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            tint = AmberModalities,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "[${q.targetDimension}]",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = AmberModalities
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = q.question,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Rationale: ${q.clinicalRationale}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

/** Gemini AI deep constitutional synthesis card (responsive.md §2.1 right pane). */
@Composable
private fun HudAiSynthesisCard(
    uiState: ConsultationUiState,
    onTrigger: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = VioletContainer.copy(alpha = 0.4f)),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = VioletPqrs,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = stringResource(R.string.hud_ai_synthesis_title),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = VioletPqrs
                    )
                }

                Button(
                    onClick = onTrigger,
                    enabled = !uiState.isAiAnalyzing,
                    colors = ButtonDefaults.buttonColors(containerColor = VioletPqrs),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                    modifier = Modifier
                        .height(36.dp)
                        .testTag("trigger_gemini_analysis_button")
                ) {
                    if (uiState.isAiAnalyzing) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            text = stringResource(R.string.hud_synthesize_button),
                            style = MaterialTheme.typography.labelMedium,
                            color = Color.White
                        )
                    }
                }
            }

            if (uiState.aiConstitutionalSynthesis.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = uiState.aiConstitutionalSynthesis,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

/** Verbatim dialogue transcript header row. */
@Composable
private fun HudTranscriptHeader(uiState: ConsultationUiState) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.hud_dialogue_stream_title),
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = "${uiState.transcript.size} turns",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

/** Zero-data transcript state with 1-tap sample utterances (ux-states.md §2.1). */
@Composable
private fun HudTranscriptEmptyState(onSampleUtterance: (String) -> Unit) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = stringResource(R.string.hud_no_dialogue_hint),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "TRY A SAMPLE PATIENT UTTERANCE:",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                listOf(
                    "Sar mein dhoop se dard hota hai, doctor.",
                    "Gussa dabane se pet kharab ho jata hai.",
                    "Raat ko 2 baje se neend nahi aati, paani ki pyaas rehti hai."
                ).forEach { sample ->
                    Surface(
                        color = IndigoContainer,
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .clickable { onSampleUtterance(sample) }
                            .testTag("sample_utterance_pill")
                            .minimumInteractiveComponentSize()
                    ) {
                        Text(
                            text = sample,
                            style = MaterialTheme.typography.labelSmall,
                            color = IndigoOnContainer,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        )
                    }
                }
            }
        }
    }
}

/** Manual doctor note / utterance entry bar (responsive.md §2.1 left pane). */
@Composable
private fun HudInputBar(
    utterance: String,
    onValueChange: (String) -> Unit,
    onSubmit: () -> Unit
) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(12.dp),
        shadowElevation = 2.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = utterance,
                onValueChange = onValueChange,
                placeholder = { Text(stringResource(R.string.hud_input_placeholder)) },
                modifier = Modifier
                    .weight(1f)
                    .testTag("manual_utterance_input"),
                singleLine = true,
                textStyle = MaterialTheme.typography.bodyMedium
            )

            IconButton(
                onClick = onSubmit,
                colors = IconButtonDefaults.filledIconButtonColors(containerColor = EmeraldPrimary),
                modifier = Modifier.testTag("submit_utterance_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "Submit Utterance",
                    tint = Color.White
                )
            }
        }
    }
}

@Composable
private fun AudioControlCard(
    uiState: ConsultationUiState,
    onToggleMic: () -> Unit,
    onSelectSimCase: () -> Unit,
    onStopSim: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = stringResource(R.string.hud_audio_ingestion_title),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = stringResource(R.string.hud_audio_ingestion_subtitle),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Live Ambient Mic Button
                Button(
                    onClick = onToggleMic,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (uiState.isMicListening) CrimsonRedFlag else EmeraldPrimary
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .testTag("toggle_mic_button")
                ) {
                    Icon(
                        imageVector = if (uiState.isMicListening) Icons.Default.Close else Icons.Default.PlayArrow,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (uiState.isMicListening) stringResource(R.string.stop_mic) else stringResource(R.string.start_mic),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Simulated Case Stream Button
                if (uiState.isSimulationRunning) {
                    Button(
                        onClick = onStopSim,
                        colors = ButtonDefaults.buttonColors(containerColor = StatusWarning),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("stop_simulation_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = stringResource(R.string.hud_pause_stream_button),
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                } else {
                    OutlinedButton(
                        onClick = onSelectSimCase,
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("stream_simulated_patient_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = null,
                            tint = EmeraldPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = stringResource(R.string.hud_simulate_case_button),
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = EmeraldPrimary
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TranscriptBubble(entry: TranscriptEntry) {
    val isDoctor = entry.speaker == SpeakerType.DOCTOR
    val isSystem = entry.speaker == SpeakerType.SYSTEM

    val bgColor = when {
        isDoctor -> IndigoContainer.copy(alpha = 0.5f)
        isSystem -> SlateDarkSurfaceVariant.copy(alpha = 0.2f)
        else -> EmeraldContainer.copy(alpha = 0.45f)
    }

    val speakerLabel = when {
        isDoctor -> "Dr. Homoeopath"
        isSystem -> "System Observation"
        else -> "Patient Voice"
    }

    val labelColor = when {
        isDoctor -> IndigoGenerals
        isSystem -> MaterialTheme.colorScheme.onSurfaceVariant
        else -> EmeraldPrimary
    }

    Surface(
        color = bgColor,
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("transcript_entry_${entry.id}")
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Text(
                text = speakerLabel,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = labelColor
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = entry.text,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

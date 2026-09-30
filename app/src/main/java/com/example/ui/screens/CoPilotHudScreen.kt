package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.engine.HomeopathyKnowledgeEngine
import com.example.model.*
import com.example.ui.theme.*
import com.example.viewmodel.ConsultationUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoPilotHudScreen(
    uiState: ConsultationUiState,
    onToggleRecording: () -> Unit,
    onToggleSimulatedStream: () -> Unit,
    onStepSimulatedTurn: () -> Unit,
    onManualUtterance: (Speaker, String) -> Unit,
    onQuestionAnswered: (String, Boolean) -> Unit,
    onTogglePinQuestion: (String) -> Unit,
    onSilentObservation: (SilentObservation) -> Unit,
    onSelectSpecialty: (SpecialtyTree) -> Unit,
    onInjectVocalBiomarker: (VocalBiomarker) -> Unit,
    modifier: Modifier = Modifier
) {
    var showManualInputDialog by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()

    // Auto-scroll transcript to bottom when new turns arrive
    LaunchedEffect(uiState.transcriptTurns.size) {
        if (uiState.transcriptTurns.isNotEmpty()) {
            listState.animateScrollToItem(uiState.transcriptTurns.size - 1)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Control Bar for Voice & Simulation
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 2.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Mic Button
                FilledTonalButton(
                    onClick = onToggleRecording,
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = if (uiState.isRecording) EmergencyCrimson else EmeraldPrimary,
                        contentColor = Color.White
                    ),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                    modifier = Modifier.height(34.dp).testTag("mic_toggle_button")
                ) {
                    Icon(
                        imageVector = if (uiState.isRecording) Icons.Default.MicOff else Icons.Default.Mic,
                        contentDescription = "Microphone",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (uiState.isRecording) "Stop Mic" else "Live Mic",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Auto-Stream Simulation Toggle
                Button(
                    onClick = onToggleSimulatedStream,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (uiState.isSimulatingStream) ModalityAmber else IndigoMind,
                        contentColor = Color.White
                    ),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                    modifier = Modifier.height(34.dp).testTag("stream_case_button")
                ) {
                    Icon(
                        imageVector = if (uiState.isSimulatingStream) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = "Stream Case",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (uiState.isSimulatingStream) "Pause Stream" else "▶ Stream Case",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                // Step Next Utterance
                OutlinedButton(
                    onClick = onStepSimulatedTurn,
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                    modifier = Modifier.height(34.dp).testTag("next_utterance_button")
                ) {
                    Icon(Icons.Default.SkipNext, contentDescription = "Next Turn", modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(2.dp))
                    Text("Next", fontSize = 11.sp)
                }

                // Manual Input
                IconButton(
                    onClick = { showManualInputDialog = true },
                    modifier = Modifier.size(34.dp).testTag("manual_input_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.AddComment,
                        contentDescription = "Type Utterance",
                        tint = EmeraldPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        // Section: Specialty Question Tree Selector
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 8.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            SpecialtyTree.values().forEach { spec ->
                val isSelected = uiState.selectedSpecialty == spec
                FilterChip(
                    selected = isSelected,
                    onClick = { onSelectSpecialty(spec) },
                    label = { Text("${spec.icon} ${spec.title}", fontSize = 10.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = EmeraldPrimary.copy(alpha = 0.15f),
                        selectedLabelColor = EmeraldPrimary
                    ),
                    modifier = Modifier.height(26.dp)
                )
            }
        }

        // Section 1: ASK NEXT Priority Follow-Up Question Deck
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "🎯 ASK NEXT",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = EmeraldPrimary,
                            letterSpacing = 0.5.sp
                        )
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "[${uiState.selectedSpecialty.title}]",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = ModalityAmber
                        )
                    )
                }

                if (uiState.isGeminiProcessing) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        CircularProgressIndicator(modifier = Modifier.size(12.dp), strokeWidth = 2.dp, color = EmeraldPrimary)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Gemini Reasoning...", fontSize = 10.sp, color = EmeraldPrimary)
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            if (uiState.priorityFollowUpQuestions.isEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "Stream a simulated case or speak via mic. High-yield follow-up questions will surface here live to complete LSMC and differentiate remedies.",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = 16.sp
                        ),
                        modifier = Modifier.padding(10.dp)
                    )
                }
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    uiState.priorityFollowUpQuestions.take(3).forEach { question ->
                        QuestionGlanceCard(
                            question = question,
                            onAnsweredPositive = { onQuestionAnswered(question.id, true) },
                            onAnsweredNegative = { onQuestionAnswered(question.id, false) },
                            onTogglePin = { onTogglePinQuestion(question.id) }
                        )
                    }
                }
            }
        }

        // Section 2: Silent Doctor Observations & Vocal Prosody Biomarkers
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 2.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "👁️ OBSERVATIONS & VOCAL PROSODY (Organon §88-90)",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 10.sp
                    )
                )
            }
            Spacer(modifier = Modifier.height(2.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // Vocal prosody chips
                HomeopathyKnowledgeEngine.vocalBiomarkers.forEach { vb ->
                    SuggestionChip(
                        onClick = { onInjectVocalBiomarker(vb) },
                        label = { Text("🎙️ ${vb.name}", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                        colors = SuggestionChipDefaults.suggestionChipColors(
                            containerColor = PqrsViolet.copy(alpha = 0.12f),
                            labelColor = PqrsViolet
                        ),
                        modifier = Modifier.height(26.dp)
                    )
                }

                // Silent observations
                HomeopathyKnowledgeEngine.silentObservations.forEach { obs ->
                    SuggestionChip(
                        onClick = { onSilentObservation(obs) },
                        label = { Text("+ ${obs.label}", fontSize = 10.sp, fontWeight = FontWeight.Medium) },
                        colors = SuggestionChipDefaults.suggestionChipColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.8f),
                            labelColor = MaterialTheme.colorScheme.onSurface
                        ),
                        modifier = Modifier.height(26.dp)
                    )
                }
            }
        }

        Divider(modifier = Modifier.padding(vertical = 4.dp), color = MaterialTheme.colorScheme.outlineVariant)

        // Section 3: Live 3-Speaker Annotated Transcript Feed
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        ) {
            if (uiState.transcriptTurns.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.Hearing,
                            contentDescription = null,
                            tint = Slate400,
                            modifier = Modifier.size(36.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Awaiting patient narration...",
                            style = MaterialTheme.typography.bodyMedium.copy(color = Slate500)
                        )
                        Text(
                            text = "Tap '▶ Stream Case' above to start automated case dialogue",
                            style = MaterialTheme.typography.bodySmall.copy(color = Slate400, fontSize = 11.sp)
                        )
                    }
                }
            } else {
                LazyColumn(
                    state = listState,
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(vertical = 4.dp),
                    modifier = Modifier.fillMaxSize().testTag("transcript_list")
                ) {
                    items(uiState.transcriptTurns, key = { it.id }) { turn ->
                        TranscriptTurnCard(turn = turn)
                    }
                }
            }
        }
    }

    // Manual Utterance Input Dialog with 3-Speaker Diarization
    if (showManualInputDialog) {
        ManualUtteranceDialog(
            onDismiss = { showManualInputDialog = false },
            onSubmit = { speaker, text ->
                onManualUtterance(speaker, text)
                showManualInputDialog = false
            }
        )
    }
}

@Composable
private fun QuestionGlanceCard(
    question: FollowUpQuestion,
    onAnsweredPositive: () -> Unit,
    onAnsweredNegative: () -> Unit,
    onTogglePin: () -> Unit
) {
    val badgeColor = when (question.questionType) {
        QuestionType.RED_FLAG -> EmergencyCrimson
        QuestionType.COMPLETE_LSMC -> AmeliorationSky
        QuestionType.DIFFERENTIATE_REMEDY -> ModalityAmber
        QuestionType.MENTAL_CAUSATION -> IndigoMind
        QuestionType.PHYSICAL_GENERAL -> PqrsViolet
        QuestionType.KINGDOM_MIASM -> EmeraldPrimary
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = if (question.isPinned) androidx.compose.foundation.BorderStroke(1.5.dp, badgeColor) else null
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            // Badge & Actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(badgeColor.copy(alpha = 0.15f))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = question.questionType.badge,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = badgeColor,
                            fontSize = 10.sp
                        )
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = question.aphorismCite,
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    IconButton(
                        onClick = onTogglePin,
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = if (question.isPinned) Icons.Default.PushPin else Icons.Default.PinDrop,
                            contentDescription = "Pin",
                            tint = if (question.isPinned) badgeColor else Slate400,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // 18sp Bold Open-Ended Question Text
            Text(
                text = question.questionText,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 21.sp
                )
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Clinical Rationale ("Why Ask?")
            Row(verticalAlignment = Alignment.Top) {
                Text(
                    text = "💡 Why: ",
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = ModalityAmber,
                        fontSize = 11.sp
                    )
                )
                Text(
                    text = question.clinicalRationale,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 11.sp,
                        lineHeight = 15.sp
                    )
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = onAnsweredNegative,
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp),
                    modifier = Modifier.height(28.dp)
                ) {
                    Text("✕ No / Neg", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = onAnsweredPositive,
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 2.dp),
                    modifier = Modifier.height(28.dp)
                ) {
                    Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("✓ Yes / Confirm", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun TranscriptTurnCard(turn: TranscriptTurn) {
    val bubbleColor = when (turn.speaker) {
        Speaker.PATIENT -> MaterialTheme.colorScheme.surface
        Speaker.ATTENDANT -> ModalityAmber.copy(alpha = 0.08f)
        Speaker.DOCTOR -> EmeraldPrimary.copy(alpha = 0.08f)
    }

    val speakerColor = when (turn.speaker) {
        Speaker.PATIENT -> IndigoMind
        Speaker.ATTENDANT -> ModalityAmber
        Speaker.DOCTOR -> EmeraldPrimary
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = bubbleColor),
        border = if (turn.isRedFlagTrigger) androidx.compose.foundation.BorderStroke(1.dp, EmergencyCrimson) else null
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(18.dp)
                            .clip(CircleShape)
                            .background(speakerColor),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = when (turn.speaker) {
                                Speaker.PATIENT -> "P"
                                Speaker.ATTENDANT -> "A"
                                Speaker.DOCTOR -> "Dr"
                            },
                            color = Color.White,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = turn.speaker.label,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = speakerColor,
                            letterSpacing = 0.5.sp
                        )
                    )
                }

                if (turn.detectedVocalBiomarker != null) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(PqrsViolet.copy(alpha = 0.15f))
                            .padding(horizontal = 6.dp, vertical = 1.dp)
                    ) {
                        Text(
                            text = "🎙️ ${turn.detectedVocalBiomarker}",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = PqrsViolet
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = turn.text,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 13.sp,
                    lineHeight = 19.sp
                )
            )

            // Extracted Symptom Badges
            if (turn.extractedSymptoms.isNotEmpty()) {
                Spacer(modifier = Modifier.height(6.dp))
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    turn.extractedSymptoms.forEach { sx ->
                        val tagColor = when (sx.category) {
                            SymptomCategory.CAUSATION -> EmergencyCrimson
                            SymptomCategory.MENTAL_GENERAL -> IndigoMind
                            SymptomCategory.PHYSICAL_GENERAL -> ModalityAmber
                            SymptomCategory.PARTICULAR -> AmeliorationSky
                            SymptomCategory.PQRS -> PqrsViolet
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(tagColor.copy(alpha = 0.12f))
                                .padding(horizontal = 6.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = sx.category.label,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    color = tagColor,
                                    fontSize = 10.sp
                                )
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = sx.clinicalSummary,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colorScheme.onSurface
                                ),
                                maxLines = 1
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ManualUtteranceDialog(
    onDismiss: () -> Unit,
    onSubmit: (Speaker, String) -> Unit
) {
    var speaker by remember { mutableStateOf(Speaker.PATIENT) }
    var text by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Log Clinical Dialogue Turn") },
        text = {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    FilterChip(
                        selected = speaker == Speaker.PATIENT,
                        onClick = { speaker = Speaker.PATIENT },
                        label = { Text("Patient", fontSize = 11.sp) }
                    )
                    FilterChip(
                        selected = speaker == Speaker.ATTENDANT,
                        onClick = { speaker = Speaker.ATTENDANT },
                        label = { Text("Attendant", fontSize = 11.sp) }
                    )
                    FilterChip(
                        selected = speaker == Speaker.DOCTOR,
                        onClick = { speaker = Speaker.DOCTOR },
                        label = { Text("Doctor", fontSize = 11.sp) }
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    label = { Text("Type English / Hindi / Hinglish quote") },
                    placeholder = { Text("e.g. Dhoop mein sar dard badhta hai...") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onSubmit(speaker, text) },
                enabled = text.isNotBlank()
            ) {
                Text("Add to Case")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

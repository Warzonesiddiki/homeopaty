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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.similimumai.data.engine.HomeopathyKnowledgeEngine
import com.example.similimumai.data.model.*
import com.example.similimumai.data.speech.ClinicalSimulator
import com.example.similimumai.ui.components.AudioWaveformVisualizer
import com.example.similimumai.ui.theme.*
import com.example.similimumai.ui.viewmodel.ConsultationUiState
import com.example.similimumai.ui.viewmodel.ConsultationViewModel

@Composable
fun HudScreen(
    uiState: ConsultationUiState,
    viewModel: ConsultationViewModel,
    modifier: Modifier = Modifier
) {
    var manualUtterance by remember { mutableStateOf("") }
    var showCasePicker by remember { mutableStateOf(false) }

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
        item {
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

        // Doctor Silent Physical Observations Section
        item {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = "DOCTOR'S SILENT PHYSICAL OBSERVATIONS (§83)",
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
                            onClick = { viewModel.addDoctorObservation(obs) },
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
                            modifier = Modifier.testTag("obs_chip_${obs.id}")
                        )
                    }
                }
            }
        }

        // Ask-Next High-Yield Questions Cards
        if (uiState.highYieldQuestions.isNotEmpty()) {
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "ASK-NEXT HIGH-YIELD QUESTIONS",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = AmberModalities
                        )
                        Text(
                            text = "Hahnemannian Totality §153",
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
        }

        // Gemini AI Deep Constitutional Synthesis Card
        item {
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
                                text = "CONSTITUTIONAL AI SYNTHESIS",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = VioletPqrs
                            )
                        }

                        Button(
                            onClick = { viewModel.triggerGeminiDeepAnalysis() },
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
                                    text = "Synthesize Case",
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

        // Verbatim Dialogue Transcript
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "CONSULTATION DIALOGUE STREAM",
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

        if (uiState.transcript.isEmpty()) {
            item {
                Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No dialogue yet. Click 'SIMULATE CASE' or start the ambient microphone to begin.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        } else {
            items(uiState.transcript) { entry ->
                TranscriptBubble(entry = entry)
            }
        }

        // Manual Doctor Note / Utterance Entry Bar
        item {
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
                        value = manualUtterance,
                        onValueChange = { manualUtterance = it },
                        placeholder = { Text("Enter patient phrase or clinical note...") },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("manual_utterance_input"),
                        singleLine = true,
                        textStyle = MaterialTheme.typography.bodyMedium
                    )

                    IconButton(
                        onClick = {
                            if (manualUtterance.isNotBlank()) {
                                viewModel.processUtterance(manualUtterance, SpeakerType.PATIENT)
                                manualUtterance = ""
                            }
                        },
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
            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    // Case Selection Dialog
    if (showCasePicker) {
        AlertDialog(
            onDismissRequest = { showCasePicker = false },
            title = {
                Text(
                    text = "Select Clinical Simulation Case",
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
                    Text("Close")
                }
            }
        )
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
                        text = "Clinical Audio Ingestion",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Continuous speech stream or multi-turn simulation",
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
                        text = if (uiState.isMicListening) "STOP MIC" else "START AMBIENT MIC",
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
                            text = "PAUSE STREAM",
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
                            text = "SIMULATE CASE",
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

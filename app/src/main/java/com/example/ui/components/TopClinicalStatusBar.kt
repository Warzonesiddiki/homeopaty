package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.engine.HomeopathyKnowledgeEngine
import com.example.model.CaseMode
import com.example.ui.theme.*
import com.example.viewmodel.ConsultationUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopClinicalStatusBar(
    uiState: ConsultationUiState,
    onCaseSelected: (String) -> Unit,
    onModeChange: (CaseMode) -> Unit,
    onToggleConsent: (Boolean) -> Unit,
    onToggleDark: () -> Unit,
    onOpenRecords: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var caseMenuExpanded by remember { mutableStateOf(false) }
    var modeMenuExpanded by remember { mutableStateOf(false) }

    // Waveform pulse animation
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.3f,
        animationSpec = infiniteRepeatable(
            animation = tween(700, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseScale"
    )

    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surfaceVariant,
        tonalElevation = 3.dp,
        shadowElevation = 2.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            // Row 1: Patient Info + REC Timer + Mode Badge + Theme
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Patient Name & Demographics
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(EmeraldPrimary),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.MedicalServices,
                            contentDescription = "Doctor",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = uiState.activePatient.name,
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = "${uiState.activePatient.age}y ${uiState.activePatient.gender}",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 11.sp
                            )
                        )
                    }
                }

                // Recording status indicator & Timer
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(if (uiState.isRecording) EmergencyCrimson.copy(alpha = 0.15f) else Color.Transparent)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(if (uiState.isRecording) (8 * pulseScale).dp else 8.dp)
                            .clip(CircleShape)
                            .background(if (uiState.isRecording) EmergencyCrimson else Slate400)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    val mins = uiState.recordingSeconds / 60
                    val secs = uiState.recordingSeconds % 60
                    Text(
                        text = String.format("%s %02d:%02d", if (uiState.isRecording) "REC" else "PAUSE", mins, secs),
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = if (uiState.isRecording) EmergencyCrimson else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Mode Selector Dropdown
                Box {
                    AssistChip(
                        onClick = { modeMenuExpanded = true },
                        label = {
                            Text(
                                text = uiState.activePatient.mode.name,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        },
                        trailingIcon = {
                            Icon(Icons.Default.ArrowDropDown, contentDescription = "Mode Dropdown", modifier = Modifier.size(16.dp))
                        },
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = when (uiState.activePatient.mode) {
                                CaseMode.CHRONIC -> IndigoMind.copy(alpha = 0.12f)
                                CaseMode.ACUTE -> ModalityAmber.copy(alpha = 0.15f)
                                CaseMode.FOLLOW_UP -> EmeraldPrimary.copy(alpha = 0.15f)
                            },
                            labelColor = when (uiState.activePatient.mode) {
                                CaseMode.CHRONIC -> IndigoMind
                                CaseMode.ACUTE -> ModalityAmber
                                CaseMode.FOLLOW_UP -> EmeraldPrimary
                            }
                        ),
                        modifier = Modifier.height(28.dp)
                    )

                    DropdownMenu(
                        expanded = modeMenuExpanded,
                        onDismissRequest = { modeMenuExpanded = false }
                    ) {
                        CaseMode.values().forEach { mode ->
                            DropdownMenuItem(
                                text = { Text(mode.label, fontSize = 13.sp) },
                                onClick = {
                                    onModeChange(mode)
                                    modeMenuExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.width(4.dp))

                // Dark/Light Mode Toggle
                IconButton(
                    onClick = onToggleDark,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = if (uiState.isDarkMode) Icons.Default.LightMode else Icons.Default.DarkMode,
                        contentDescription = "Toggle Theme",
                        modifier = Modifier.size(18.dp)
                    )
                }

                Spacer(modifier = Modifier.width(4.dp))

                // Patient Records Archive Button
                IconButton(
                    onClick = onOpenRecords,
                    modifier = Modifier.size(32.dp)
                ) {
                    BadgedBox(
                        badge = {
                            if (uiState.savedPatients.isNotEmpty()) {
                                Badge(containerColor = EmeraldPrimary) {
                                    Text("${uiState.savedPatients.size}", fontSize = 9.sp, color = Color.White)
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.FolderShared,
                            contentDescription = "Patient Records Archive",
                            tint = EmeraldPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Row 2: Live Audio Waveform + Consent Switch + Case Selector
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Waveform Bars
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                    modifier = Modifier
                        .height(18.dp)
                        .weight(1f)
                ) {
                    val bars = 18
                    for (i in 0 until bars) {
                        val baseHeight = if (uiState.isRecording) {
                            val waveVar = kotlin.math.sin((i * 0.4) + (uiState.liveAudioWaveform * 8)).toFloat()
                            val h = (8f + (uiState.liveAudioWaveform * 16f * (0.5f + (waveVar * 0.5f)))).coerceIn(3f, 18f)
                            h.dp
                        } else {
                            3.dp
                        }
                        Box(
                            modifier = Modifier
                                .width(3.dp)
                                .height(baseHeight)
                                .clip(RoundedCornerShape(2.dp))
                                .background(if (uiState.isRecording) EmeraldPrimary else Slate300())
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Patient Consent Chip
                FilterChip(
                    selected = uiState.activePatient.consentObtained,
                    onClick = { onToggleConsent(!uiState.activePatient.consentObtained) },
                    label = {
                        Text(
                            text = if (uiState.activePatient.consentObtained) "Consent ✓" else "Consent ?",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = if (uiState.activePatient.consentObtained) Icons.Default.CheckCircle else Icons.Default.Warning,
                            contentDescription = "Consent",
                            tint = if (uiState.activePatient.consentObtained) EmeraldPrimary else EmergencyCrimson,
                            modifier = Modifier.size(14.dp)
                        )
                    },
                    modifier = Modifier.height(26.dp)
                )

                Spacer(modifier = Modifier.width(6.dp))

                // Simulate Case Dropdown Selector
                Box {
                    Button(
                        onClick = { caseMenuExpanded = true },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                            contentColor = MaterialTheme.colorScheme.primary
                        ),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                        modifier = Modifier.height(28.dp)
                    ) {
                        Icon(Icons.Default.PlayCircle, contentDescription = "Simulate", modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Simulate Case ▼",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    DropdownMenu(
                        expanded = caseMenuExpanded,
                        onDismissRequest = { caseMenuExpanded = false }
                    ) {
                        HomeopathyKnowledgeEngine.preloadedCases.forEach { simCase ->
                            DropdownMenuItem(
                                leadingIcon = {
                                    Icon(
                                        imageVector = if (simCase.isEmergency) Icons.Default.Warning else Icons.Default.Psychology,
                                        contentDescription = null,
                                        tint = if (simCase.isEmergency) EmergencyCrimson else EmeraldPrimary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                },
                                text = {
                                    Column {
                                        Text(
                                            text = "${simCase.patientName} (${simCase.age}${simCase.gender.first()})",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 12.sp
                                        )
                                        Text(
                                            text = simCase.title,
                                            fontSize = 11.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                },
                                onClick = {
                                    onCaseSelected(simCase.id)
                                    caseMenuExpanded = false
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun Slate300(): Color = Slate400.copy(alpha = 0.4f)

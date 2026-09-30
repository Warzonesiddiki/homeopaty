package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.engine.HomeopathyKnowledgeEngine
import com.example.model.*
import com.example.ui.theme.*
import com.example.viewmodel.ConsultationUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VisionLabScreen(
    uiState: ConsultationUiState,
    onAddRubricByPath: (String) -> Unit,
    onQueueLabQuestion: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var selectedVisionTab by remember { mutableStateOf(0) } // 0 = Tongue, 1 = Skin/Nails, 2 = Labs

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Top Header
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 2.dp
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CameraAlt,
                        contentDescription = null,
                        tint = EmeraldPrimary,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "👁️ MULTIMODAL VISION & LAB DIAGNOSTICS",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Black,
                                color = EmeraldPrimary,
                                fontSize = 14.sp
                            )
                        )
                        Text(
                            text = "AI Glossoscopy, Physical Keynotes & Lab Investigation Rubric Correlator",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 11.sp
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Tab Switcher
                PrimaryTabRow(
                    selectedTabIndex = selectedVisionTab,
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = EmeraldPrimary
                ) {
                    Tab(
                        selected = selectedVisionTab == 0,
                        onClick = { selectedVisionTab = 0 },
                        text = { Text("👅 Tongue (Glossoscopy)", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) }
                    )
                    Tab(
                        selected = selectedVisionTab == 1,
                        onClick = { selectedVisionTab = 1 },
                        text = { Text("🔬 Skin & Nails", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) }
                    )
                    Tab(
                        selected = selectedVisionTab == 2,
                        onClick = { selectedVisionTab = 2 },
                        text = { Text("🧪 Lab Investigations", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) }
                    )
                }
            }
        }

        // Body Content
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            when (selectedVisionTab) {
                0 -> {
                    // AI Glossoscopy
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = "HOMEOPATHIC TONGUE EXAMINATION (ORGANON §88-90)",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Tap any observed morphological tongue sign to immediately inject its canonical MOUTH rubric into the active repertory sheet:",
                                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                )
                            }
                        }
                    }

                    items(HomeopathyKnowledgeEngine.tongueSigns, key = { it.id }) { sign ->
                        TongueSignCard(
                            sign = sign,
                            isAdded = uiState.activeRubrics.any { it.path == sign.rubricPath },
                            onAddRubric = {
                                onAddRubricByPath(sign.rubricPath)
                                Toast.makeText(context, "Added '${sign.rubricPath}' to Repertory!", Toast.LENGTH_SHORT).show()
                            }
                        )
                    }
                }
                1 -> {
                    // Skin & Nails
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = "OBJECTIVE DERMATOLOGY & NAIL SIGNS",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Select visible morphological features to match Allen & Boericke keynote indications:",
                                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                )
                            }
                        }
                    }

                    items(HomeopathyKnowledgeEngine.objectiveSigns, key = { it.id }) { sign ->
                        ObjectiveSignCard(
                            sign = sign,
                            isAdded = uiState.activeRubrics.any { it.path == sign.rubricPath },
                            onAddRubric = {
                                onAddRubricByPath(sign.rubricPath)
                                Toast.makeText(context, "Added '${sign.rubricPath}' to Repertory!", Toast.LENGTH_SHORT).show()
                            }
                        )
                    }
                }
                2 -> {
                    // Lab Reports Correlator
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = "LAB INVESTIGATION & PATHOLOGICAL GENERALS",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Correlates patient blood/urine/scan abnormalities with organ-affinity remedies, mother tinctures, and targeted modality questions:",
                                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                )
                            }
                        }
                    }

                    items(HomeopathyKnowledgeEngine.labInvestigations, key = { it.id }) { lab ->
                        LabInvestigationCard(
                            lab = lab,
                            onQueueQuestion = {
                                onQueueLabQuestion(lab.testName, lab.suggestedModalityQuestion)
                                Toast.makeText(context, "Queued inquiry into HUD!", Toast.LENGTH_SHORT).show()
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TongueSignCard(
    sign: TongueSign,
    isAdded: Boolean,
    onAddRubric: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isAdded) EmeraldPrimary.copy(alpha = 0.08f) else MaterialTheme.colorScheme.surface
        ),
        border = if (isAdded) androidx.compose.foundation.BorderStroke(1.dp, EmeraldPrimary) else androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(EmeraldPrimary.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Text(sign.iconEmoji, fontSize = 18.sp)
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = sign.name,
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = sign.description,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 11.sp
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Key Remedies: ${sign.keyRemedies.joinToString(", ")}",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = EmeraldPrimary,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 10.sp
                    )
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = onAddRubric,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isAdded) EmeraldPrimary else MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = if (isAdded) Color.White else MaterialTheme.colorScheme.onSurface
                ),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                modifier = Modifier.height(28.dp)
            ) {
                Text(if (isAdded) "Added ✓" else "+ Rubric", fontSize = 10.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun ObjectiveSignCard(
    sign: ObjectivePhysicalSign,
    isAdded: Boolean,
    onAddRubric: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isAdded) IndigoMind.copy(alpha = 0.08f) else MaterialTheme.colorScheme.surface
        ),
        border = if (isAdded) androidx.compose.foundation.BorderStroke(1.dp, IndigoMind) else androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(IndigoMind.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Text(sign.iconEmoji, fontSize = 18.sp)
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(3.dp))
                            .background(IndigoMind.copy(alpha = 0.12f))
                            .padding(horizontal = 4.dp, vertical = 1.dp)
                    ) {
                        Text(sign.category, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = IndigoMind)
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = sign.name,
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )
                }
                Text(
                    text = sign.description,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 11.sp
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Key Remedies: ${sign.keyRemedies.joinToString(", ")}",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = IndigoMind,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 10.sp
                    )
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = onAddRubric,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isAdded) IndigoMind else MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = if (isAdded) Color.White else MaterialTheme.colorScheme.onSurface
                ),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                modifier = Modifier.height(28.dp)
            ) {
                Text(if (isAdded) "Added ✓" else "+ Rubric", fontSize = 10.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun LabInvestigationCard(
    lab: LabInvestigationSign,
    onQueueQuestion: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = lab.testName,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Black,
                        color = EmergencyCrimson
                    ),
                    modifier = Modifier.weight(1f)
                )
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(EmergencyCrimson.copy(alpha = 0.12f))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text("ABNORMAL", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = EmergencyCrimson)
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = lab.abnormality,
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface)
            )

            Spacer(modifier = Modifier.height(6.dp))

            Surface(
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                shape = RoundedCornerShape(6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    Text(
                        text = "Organ Affinity: ${lab.organAffinity}",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = ModalityAmber)
                    )
                    Text(
                        text = "Supportive Remedies & Tinctures: ${lab.supportiveRemedies.joinToString(", ")}",
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Suggested Probe: \"${lab.suggestedModalityQuestion}\"",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontSize = 11.sp,
                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                Button(
                    onClick = onQueueQuestion,
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp),
                    modifier = Modifier.height(28.dp)
                ) {
                    Icon(Icons.Default.HelpOutline, contentDescription = null, modifier = Modifier.size(12.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Queue Question into HUD", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

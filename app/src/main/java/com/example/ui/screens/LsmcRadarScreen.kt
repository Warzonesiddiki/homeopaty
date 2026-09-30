package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ExtractedSymptom
import com.example.model.PillarStatus
import com.example.ui.theme.*
import com.example.viewmodel.ConsultationUiState

@Composable
fun LsmcRadarScreen(
    uiState: ConsultationUiState,
    onExplorePillar: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val exploredCount = uiState.pillarsStatus.count { it.isExplored }
    val progress = exploredCount.toFloat() / uiState.pillarsStatus.size

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Header Card with Case Coverage Progress
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "🧬 12-PILLAR CASE RADAR",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Black,
                                    color = EmeraldPrimary,
                                    letterSpacing = 0.5.sp
                                )
                            )
                            Text(
                                text = "Hahnemannian Constitutional Completeness",
                                style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                            )
                        }

                        Text(
                            text = "${(progress * 100).toInt()}% Explored",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = EmeraldPrimary
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = EmeraldPrimary,
                        trackColor = Slate200
                    )

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "$exploredCount of 12 Spheres Covered. Tap any open sphere to queue targeted question!",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 11.sp
                        )
                    )
                }
            }
        }

        // 12-Pillar Cards Grid
        item {
            Text(
                text = "CONSTITUTIONAL SPHERES",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }

        items(uiState.pillarsStatus.chunked(2)) { pair ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                pair.forEach { pillar ->
                    PillarCard(
                        pillar = pillar,
                        onExplore = { onExplorePillar(pillar.id) },
                        modifier = Modifier.weight(1f)
                    )
                }
                if (pair.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }

        // Section: Boenninghausen LSMC 4-Legged Stool Breakdown
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "🪑 BOENNINGHAUSEN LSMC 4-PILLAR CARDS",
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = ModalityAmber
                    )
                )
            }
            Text(
                text = "Location, Sensation, Modality (< / >), and Concomitants per complaint",
                style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
            )
        }

        if (uiState.extractedSymptoms.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                ) {
                    Text(
                        text = "No symptoms extracted yet. Stream a simulated case or speak into the mic on the Co-Pilot HUD to see structured LSMC symptom breakdowns here.",
                        style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }
        } else {
            items(uiState.extractedSymptoms, key = { it.id }) { symptom ->
                LsmcCard(symptom = symptom, onQueueQuestion = { onExplorePillar(9) })
            }
        }
    }
}

@Composable
private fun PillarCard(
    pillar: PillarStatus,
    onExplore: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.clickable { onExplore() },
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (pillar.isExplored) EmeraldPrimary.copy(alpha = 0.1f) else MaterialTheme.colorScheme.surface
        ),
        border = if (pillar.isExplored) androidx.compose.foundation.BorderStroke(1.dp, EmeraldPrimary) else androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = pillar.name,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (pillar.isExplored) EmeraldPrimary else MaterialTheme.colorScheme.onSurface
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = if (pillar.isExplored) Icons.Default.CheckCircle else Icons.Default.AddCircleOutline,
                    contentDescription = null,
                    tint = if (pillar.isExplored) EmeraldPrimary else Slate400,
                    modifier = Modifier.size(16.dp)
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = pillar.details,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 13.sp
                ),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun LsmcCard(
    symptom: ExtractedSymptom,
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
                    text = symptom.clinicalSummary,
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.weight(1f)
                )
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(if (symptom.lsmcCompletenessPercent >= 75) EmeraldPrimary.copy(alpha = 0.15f) else ModalityAmber.copy(alpha = 0.15f))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "LSMC ${symptom.lsmcCompletenessPercent}%",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = if (symptom.lsmcCompletenessPercent >= 75) EmeraldPrimary else ModalityAmber,
                            fontSize = 10.sp
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // 4 Pillars of LSMC
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                // L: Location
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("L — Location: ", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = IndigoMind)
                    Text(
                        text = if (symptom.location.isNotBlank()) symptom.location else "Needs extension/side clarification",
                        fontSize = 11.sp,
                        color = if (symptom.location.isNotBlank()) MaterialTheme.colorScheme.onSurface else Slate500
                    )
                }

                // S: Sensation
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("S — Sensation: ", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = ModalityAmber)
                    Text(
                        text = if (symptom.sensation.isNotBlank()) symptom.sensation else "Unspecified pain character",
                        fontSize = 11.sp,
                        color = if (symptom.sensation.isNotBlank()) MaterialTheme.colorScheme.onSurface else Slate500
                    )
                }

                // M: Modalities (< and >)
                Row(verticalAlignment = Alignment.Top) {
                    Text("M — Modalities: ", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = EmergencyCrimson)
                    Column {
                        if (symptom.modalitiesAggravation.isNotEmpty()) {
                            Text(
                                text = "< Agg: " + symptom.modalitiesAggravation.joinToString(", "),
                                fontSize = 11.sp,
                                color = EmergencyCrimson,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                        if (symptom.modalitiesAmelioration.isNotEmpty()) {
                            Text(
                                text = "> Amel: " + symptom.modalitiesAmelioration.joinToString(", "),
                                fontSize = 11.sp,
                                color = AmeliorationSky,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                        if (symptom.modalitiesAggravation.isEmpty() && symptom.modalitiesAmelioration.isEmpty()) {
                            Text("Missing heat/cold/motion/time modalities", fontSize = 11.sp, color = Slate500)
                        }
                    }
                }

                // C: Concomitants
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("C — Concomitant: ", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = PqrsViolet)
                    Text(
                        text = if (symptom.concomitants.isNotEmpty()) symptom.concomitants.joinToString(", ") else "None recorded yet",
                        fontSize = 11.sp,
                        color = Slate500
                    )
                }
            }

            if (symptom.lsmcCompletenessPercent < 100) {
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedButton(
                    onClick = onQueueQuestion,
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                    modifier = Modifier.height(26.dp)
                ) {
                    Icon(Icons.Default.HelpOutline, contentDescription = null, modifier = Modifier.size(12.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Queue Question to Complete Missing Leg", fontSize = 10.sp)
                }
            }
        }
    }
}

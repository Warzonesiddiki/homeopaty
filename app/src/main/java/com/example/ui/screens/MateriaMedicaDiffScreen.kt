package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Compare
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.engine.HomeopathyKnowledgeEngine
import com.example.model.KeynoteVerification
import com.example.model.Miasm
import com.example.model.RemedyMateriaMedica
import com.example.model.SankaranKingdom
import com.example.ui.theme.*
import com.example.viewmodel.ConsultationUiState

@Composable
fun MateriaMedicaDiffScreen(
    uiState: ConsultationUiState,
    onToggleKeynote: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val topEntries = uiState.repertorizationResults.take(3)
    val remedies = topEntries.mapNotNull { HomeopathyKnowledgeEngine.allRemedies[it.remedyCode] }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Header
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Compare, contentDescription = null, tint = EmeraldPrimary, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "📚 MATERIA MEDICA & SANKARAN KINGDOM RADAR",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Black,
                                color = EmeraldPrimary,
                                fontSize = 14.sp
                            )
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Side-by-side comparison across Materia Medica, Sankaran Kingdom & Periodic Table.",
                        style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                    )
                }
            }
        }

        // Section: Sankaran Kingdom Distribution Meter
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = EmeraldPrimary, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "SANKARAN VITAL SENSATION KINGDOM DISTRIBUTION",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = EmeraldPrimary
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Compute kingdom weights from top entries
                    val totalTop = topEntries.size.coerceAtLeast(1)
                    val plantCount = topEntries.count { it.kingdom == SankaranKingdom.PLANT }
                    val mineralCount = topEntries.count { it.kingdom == SankaranKingdom.MINERAL }
                    val animalCount = topEntries.count { it.kingdom == SankaranKingdom.ANIMAL }
                    val nosodeCount = topEntries.count { it.kingdom == SankaranKingdom.NOSODE }

                    val plantPct = (plantCount * 100 / totalTop).coerceAtLeast(10)
                    val mineralPct = (mineralCount * 100 / totalTop).coerceAtLeast(10)
                    val animalPct = (animalCount * 100 / totalTop).coerceAtLeast(10)
                    val nosodePct = (nosodeCount * 100 / totalTop).coerceAtLeast(5)

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(14.dp)
                            .clip(RoundedCornerShape(7.dp))
                    ) {
                        Box(modifier = Modifier.weight(plantPct.toFloat()).fillMaxHeight().background(EmeraldPrimary))
                        Box(modifier = Modifier.weight(mineralPct.toFloat()).fillMaxHeight().background(IndigoMind))
                        Box(modifier = Modifier.weight(animalPct.toFloat()).fillMaxHeight().background(EmergencyCrimson))
                        Box(modifier = Modifier.weight(nosodePct.toFloat()).fillMaxHeight().background(PqrsViolet))
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        MiasmLegend(label = "Plant (Sensitivity)", percent = plantPct, color = EmeraldPrimary)
                        MiasmLegend(label = "Mineral (Structure)", percent = mineralPct, color = IndigoMind)
                        MiasmLegend(label = "Animal (Survival)", percent = animalPct, color = EmergencyCrimson)
                        MiasmLegend(label = "Nosode (Block)", percent = nosodePct, color = PqrsViolet)
                    }
                }
            }
        }

        // Section: Miasmatic Bar Breakdown
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.PieChart, contentDescription = null, tint = IndigoMind, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "MIASMATIC LOAD DISTRIBUTION",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = IndigoMind
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    val miasms = uiState.miasmDistribution
                    val psora = miasms[Miasm.PSORA] ?: 40
                    val sycosis = miasms[Miasm.SYCOSIS] ?: 20
                    val syphilis = miasms[Miasm.SYPHILIS] ?: 20
                    val tubercular = miasms[Miasm.TUBERCULAR] ?: 20

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(14.dp)
                            .clip(RoundedCornerShape(7.dp))
                    ) {
                        Box(modifier = Modifier.weight(psora.toFloat().coerceAtLeast(1f)).fillMaxHeight().background(EmeraldPrimary))
                        Box(modifier = Modifier.weight(sycosis.toFloat().coerceAtLeast(1f)).fillMaxHeight().background(ModalityAmber))
                        Box(modifier = Modifier.weight(syphilis.toFloat().coerceAtLeast(1f)).fillMaxHeight().background(EmergencyCrimson))
                        Box(modifier = Modifier.weight(tubercular.toFloat().coerceAtLeast(1f)).fillMaxHeight().background(PqrsViolet))
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        MiasmLegend(label = "Psora", percent = psora, color = EmeraldPrimary)
                        MiasmLegend(label = "Sycosis", percent = sycosis, color = ModalityAmber)
                        MiasmLegend(label = "Syphilis", percent = syphilis, color = EmergencyCrimson)
                        MiasmLegend(label = "Tubercular", percent = tubercular, color = PqrsViolet)
                    }
                }
            }
        }

        // Section: Confirmatory Keynotes Checklists
        if (uiState.keynoteVerifications.isNotEmpty()) {
            item {
                Text(
                    text = "CONFIRMATORY CLINICAL KEYNOTES (ORGANON §153)",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }

            items(uiState.keynoteVerifications, key = { it.id }) { kn ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (kn.isVerified) EmeraldPrimary.copy(alpha = 0.1f) else MaterialTheme.colorScheme.surface
                    ),
                    border = if (kn.isVerified) androidx.compose.foundation.BorderStroke(1.dp, EmeraldPrimary) else androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = kn.isVerified,
                            onCheckedChange = { onToggleKeynote(kn.id) }
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "[${kn.remedyCode.uppercase()}]",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    color = EmeraldPrimary
                                )
                            )
                            Text(
                                text = kn.keynoteText,
                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp, lineHeight = 16.sp)
                            )
                        }
                    }
                }
            }
        }

        // Section: Side-by-Side Comparison
        if (remedies.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                ) {
                    Text(
                        text = "Differential columns will appear once symptoms are processed and candidate remedies are ranked.",
                        style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
                        modifier = Modifier.padding(14.dp)
                    )
                }
            }
        } else {
            item {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "HEAD-TO-HEAD COMPARISON TABLE",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }

            items(remedies, key = { it.code }) { remedy ->
                RemedyDifferentialCard(remedy = remedy)
            }
        }
    }
}

@Composable
private fun MiasmLegend(label: String, percent: Int, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(color)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = "$label $percent%", fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun RemedyDifferentialCard(remedy: RemedyMateriaMedica) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            // Remedy Name, Code & Kingdom
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = remedy.name,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black, color = EmeraldPrimary)
                    )
                    Text(
                        text = "${remedy.commonName} • ${remedy.kingdom.label}",
                        style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 11.sp)
                    )
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(EmeraldPrimary.copy(alpha = 0.15f))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = remedy.code.uppercase(),
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.ExtraBold, color = EmeraldPrimary)
                    )
                }
            }

            if (remedy.periodicTableRow != null) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Periodic Table: Row ${remedy.periodicTableRow} (Identity/Security), Column ${remedy.periodicTableCol}",
                    style = MaterialTheme.typography.labelSmall.copy(color = IndigoMind, fontWeight = FontWeight.Bold, fontSize = 10.sp)
                )
            }

            Divider(modifier = Modifier.padding(vertical = 8.dp), color = MaterialTheme.colorScheme.outlineVariant)

            AttributeRow(title = "Tissue Tropism (Boger BBCR)", content = remedy.dominantTissue, titleColor = ModalityAmber)
            AttributeRow(title = "Essence", content = remedy.essence, titleColor = IndigoMind)
            AttributeRow(title = "Causation (Ailments From)", content = remedy.causation, titleColor = EmergencyCrimson)
            AttributeRow(title = "Mind Generals", content = remedy.mindGenerals, titleColor = IndigoMind)
            AttributeRow(title = "Thermal State", content = remedy.thermal, titleColor = ModalityAmber)
            AttributeRow(title = "Thirst & Cravings", content = remedy.thirstAndFood, titleColor = ModalityAmber)
            AttributeRow(title = "Key Aggravations (<)", content = remedy.keyAggravations, titleColor = EmergencyCrimson)
            AttributeRow(title = "Key Ameliorations (>)", content = remedy.keyAmeliorations, titleColor = AmeliorationSky)
            AttributeRow(title = "Complementary & Inimicals", content = "Comp: ${remedy.complementary} | Inimical: ${remedy.inimical}", titleColor = PqrsViolet)
        }
    }
}

@Composable
private fun AttributeRow(title: String, content: String, titleColor: Color) {
    Column(modifier = Modifier.padding(vertical = 3.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                color = titleColor,
                fontSize = 11.sp
            )
        )
        Text(
            text = content,
            style = MaterialTheme.typography.bodySmall.copy(
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 11.sp,
                lineHeight = 15.sp
            )
        )
    }
}

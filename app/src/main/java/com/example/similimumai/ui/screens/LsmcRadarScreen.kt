package com.example.similimumai.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.similimumai.data.model.Miasm
import com.example.similimumai.data.model.Symptom
import com.example.similimumai.data.model.ThermalState
import com.example.similimumai.ui.theme.*
import com.example.similimumai.R
import com.example.similimumai.ui.viewmodel.ConsultationUiState
import com.example.similimumai.ui.viewmodel.ConsultationViewModel

@Composable
fun LsmcRadarScreen(
    uiState: ConsultationUiState,
    viewModel: ConsultationViewModel,
    modifier: Modifier = Modifier
) {
    val overallCompleteness = if (uiState.symptoms.isNotEmpty()) {
        uiState.symptoms.map { it.completenessScore }.average().toInt()
    } else {
        0
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .testTag("lsmc_radar_screen"),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(4.dp))
            // Hahnemannian Totality Completeness Header
            TotalityCompletenessCard(
                completenessPercent = overallCompleteness,
                symptomCount = uiState.symptoms.size,
                rubricsCount = uiState.activeRubrics.size
            )
        }

        // 12-Pillar Constitutional Assessment Panel
        item {
            ConstitutionalAssessmentCard(
                uiState = uiState,
                onThermalChange = { newThermal ->
                    viewModel.updatePatientProfile(
                        uiState.patientName,
                        uiState.patientAge,
                        uiState.patientSex,
                        newThermal,
                        uiState.patientMiasm,
                        uiState.chiefComplaint
                    )
                },
                onMiasmChange = { newMiasm ->
                    viewModel.updatePatientProfile(
                        uiState.patientName,
                        uiState.patientAge,
                        uiState.patientSex,
                        uiState.patientThermal,
                        newMiasm,
                        uiState.chiefComplaint
                    )
                }
            )
        }

        // Boenninghausen LSMC Decomposition Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.lsmc_decomposition_title),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "${uiState.symptoms.size} symptoms",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        if (uiState.symptoms.isEmpty()) {
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
                            text = stringResource(R.string.lsmc_no_symptoms_hint),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        } else {
            items(uiState.symptoms) { symptom ->
                LsmcSymptomCard(symptom = symptom)
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun TotalityCompletenessCard(
    completenessPercent: Int,
    symptomCount: Int,
    rubricsCount: Int
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = EmeraldContainer.copy(alpha = 0.5f)),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = stringResource(R.string.lsmc_totality_gauge_title),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = EmeraldOnContainer
                    )
                    Text(
                        text = stringResource(R.string.lsmc_totality_gauge_subtitle),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Surface(
                    color = EmeraldPrimary,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "$completenessPercent%",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            LinearProgressIndicator(
                progress = { (completenessPercent / 100f).coerceIn(0f, 1f) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = EmeraldPrimary,
                trackColor = Color.White.copy(alpha = 0.5f)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "$symptomCount Structured Symptoms",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "$rubricsCount Canonical Rubrics Mapped",
                    style = MaterialTheme.typography.labelSmall,
                    color = EmeraldPrimary,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun ConstitutionalAssessmentCard(
    uiState: ConsultationUiState,
    onThermalChange: (ThermalState) -> Unit,
    onMiasmChange: (Miasm) -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = EmeraldPrimary,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = stringResource(R.string.lsmc_pillar_assessment_title),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Thermal Affinity Selector
            Text(
                text = stringResource(R.string.lsmc_thermal_state_title),
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                ThermalState.values().forEach { thermal ->
                    FilterChip(
                        selected = uiState.patientThermal == thermal,
                        onClick = { onThermalChange(thermal) },
                        label = { Text(thermal.name) },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("thermal_chip_${thermal.name}").minimumInteractiveComponentSize())
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Miasmatic Dominance Selector
            Text(
                text = stringResource(R.string.lsmc_miasm_title),
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Miasm.values().forEach { miasm ->
                    FilterChip(
                        selected = uiState.patientMiasm == miasm,
                        onClick = { onMiasmChange(miasm) },
                        label = { Text(miasm.name, style = MaterialTheme.typography.labelSmall) },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("miasm_chip_${miasm.name}").minimumInteractiveComponentSize())
                }
            }
        }
    }
}

@Composable
private fun LsmcSymptomCard(symptom: Symptom) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("symptom_card_${symptom.id}")
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = symptom.location.ifBlank { "Location Not Specified" },
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = EmeraldPrimary
                )

                if (symptom.isPqrs) {
                    Surface(
                        color = VioletContainer,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.pqrs_153_label),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = VioletPqrs,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // LSMC Grid
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                LsmcRowItem(label = "Sensation", value = symptom.sensation, defaultText = "Missing sensation nature")
                LsmcRowItem(label = "Modalities", value = symptom.modalities, defaultText = "Missing modalities (< / >)", isWarning = symptom.modalities.isBlank())
                LsmcRowItem(label = "Concomitant", value = symptom.concomitants, defaultText = "No concomitant noted")
            }

            if (symptom.canonicalRubric.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    color = IndigoContainer.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = IndigoGenerals,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = symptom.canonicalRubric,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = IndigoOnContainer
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun LsmcRowItem(label: String, value: String, defaultText: String, isWarning: Boolean = false) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "$label:",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.width(80.dp)
        )
        Text(
            text = value.ifBlank { defaultText },
            style = MaterialTheme.typography.bodySmall,
            fontWeight = if (value.isNotBlank()) FontWeight.Medium else FontWeight.Normal,
            color = if (value.isNotBlank()) MaterialTheme.colorScheme.onSurface
            else if (isWarning) AmberModalities
            else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
        )
    }
}

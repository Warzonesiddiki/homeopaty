package com.example.similimumai.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.similimumai.data.local.entity.PatientEntity
import com.example.similimumai.ui.theme.*
import com.example.similimumai.R
import com.example.similimumai.ui.viewmodel.ConsultationUiState
import com.example.similimumai.ui.viewmodel.ConsultationViewModel

/**
 * Patient record search & registry (docs/product/mvp-scope.md:
 * "patient record search"). Records accumulate as cases are saved; the
 * doctor searches by name or MNR and loads a record as the active patient.
 */
@Composable
fun PatientRegistryScreen(
    uiState: ConsultationUiState,
    viewModel: ConsultationViewModel,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .testTag("patient_registry_screen"),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(R.string.patient_search_title),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = uiState.patientSearchQuery,
                onValueChange = { viewModel.setPatientSearchQuery(it) },
                placeholder = { Text(stringResource(R.string.patient_search_placeholder)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("patient_search_input"),
                singleLine = true
            )
        }

        if (uiState.patients.isEmpty()) {
            item {
                Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("patient_registry_empty")
                ) {
                    Text(
                        text = stringResource(R.string.patient_registry_empty),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(14.dp)
                    )
                }
            }
        } else {
            items(uiState.patients, key = { it.id }) { patient ->
                PatientRecordCard(
                    patient = patient,
                    isActive = patient.id == uiState.activePatientId,
                    onSelect = { viewModel.selectPatient(patient) }
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun PatientRecordCard(
    patient: PatientEntity,
    isActive: Boolean,
    onSelect: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = if (isActive) EmeraldContainer.copy(alpha = 0.35f)
            else MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onSelect)
            .testTag("patient_card_${patient.id}")
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = EmeraldPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                    Column {
                        Text(
                            text = patient.name,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = "MNR: ${patient.mnr}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                if (isActive) {
                    Surface(
                        color = EmeraldContainer,
                        shape = RoundedCornerShape(6.dp),
                        modifier = Modifier.testTag("patient_active_chip")
                    ) {
                        Text(
                            text = stringResource(R.string.patient_active_chip),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = EmeraldOnContainer,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "${patient.age} ${patient.sex} • ${patient.thermalState} • ${patient.dominantMiasm}",
                style = MaterialTheme.typography.labelSmall,
                color = IndigoGenerals
            )
            Text(
                text = patient.chiefComplaint,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

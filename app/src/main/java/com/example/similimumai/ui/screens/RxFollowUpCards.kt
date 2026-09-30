package com.example.similimumai.ui.screens

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.core.content.FileProvider
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Share
import java.io.File
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.similimumai.data.model.*
import com.example.similimumai.ui.theme.*
import com.example.similimumai.R
import com.example.similimumai.ui.viewmodel.ConsultationUiState
import com.example.similimumai.ui.viewmodel.ConsultationViewModel

/**
 * Workspace 5 extension cards (docs/05 Phase 3):
 * LM 50-Millesimal dilution calculator, Kent's 12 Prognostic Observations
 * evaluator, and plain-text Case Sheet clipboard generator.
 */
@Composable
fun LmDilutionCard(
    uiState: ConsultationUiState,
    viewModel: ConsultationViewModel,
    modifier: Modifier = Modifier
) {
    val lmLevels = listOf("LM1", "LM2", "LM3", "LM4", "LM6", "LM12", "LM24", "LM30")
    val protocol = uiState.lmProtocol

    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Calculate,
                    contentDescription = null,
                    tint = AmberModalities,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = stringResource(R.string.lm_calculator_title),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = AmberModalities
                )
            }

            Text(
                text = stringResource(R.string.lm_calculator_subtitle),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                lmLevels.forEach { level ->
                    FilterChip(
                        selected = uiState.lmPotencyName == level,
                        onClick = {
                            viewModel.updateLmParameters(level, uiState.lmHypersensitive)
                        },
                        label = {
                            Text(level, style = MaterialTheme.typography.labelSmall)
                        },
                        modifier = Modifier.testTag("lm_chip_${level.lowercase()}")
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.lm_hypersensitive_question),
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.weight(1f)
                )
                Switch(
                    checked = uiState.lmHypersensitive,
                    onCheckedChange = {
                        viewModel.updateLmParameters(uiState.lmPotencyName, it)
                    },
                    colors = SwitchDefaults.colors(
                        checkedTrackColor = AmberModalities,
                        checkedThumbColor = Color(0xFFF59E0B)
                    ),
                    modifier = Modifier.testTag("lm_hypersensitive_switch")
                )
            }

            if (protocol != null) {
                Spacer(modifier = Modifier.height(10.dp))
                Surface(
                    color = AmberContainer,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        LmProtocolRow("POTENCY", protocol.potency)
                        LmProtocolRow("DILUTION METHOD", protocol.dilutionMethod)
                        LmProtocolRow("DILUTION RATIO", protocol.dilutionRatio)
                        LmProtocolRow("PREPARATION", "${protocol.grainsOfMedicine} grains medicine + ${protocol.waterDrops} drops distilled water")
                        LmProtocolRow("SUCCUSIONS", "${protocol.succussions} vigorous shakes of the bottle")
                        LmProtocolRow("DOSE", protocol.doseInstructions)
                        LmProtocolRow("SPLIT DOSING", protocol.splitDosing)
                    }
                }
            }
        }
    }
}

@Composable
private fun LmProtocolRow(label: String, value: String) {
    Row(modifier = Modifier.padding(vertical = 2.dp)) {
        Text(
            text = "$label: ",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = AmberOnContainer
        )
        Text(
            text = value,
            style = MaterialTheme.typography.labelSmall,
            color = AmberOnContainer
        )
    }
}

@Composable
fun KentObservationsCard(
    uiState: ConsultationUiState,
    viewModel: ConsultationViewModel,
    modifier: Modifier = Modifier
) {
    val input = uiState.kentInput
    val result = uiState.kentObservationResult

    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = VioletPqrs,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = stringResource(R.string.kent_observations_title),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = VioletPqrs
                )
            }

            Text(
                text = stringResource(R.string.kent_observations_subtitle),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(10.dp))

            KentChipRow(
                title = "Initial reaction",
                options = KentReactionPattern.values().map { it.label },
                selected = input.pattern.label,
                onPick = { index ->
                    viewModel.evaluateKentObservation(
                        input.copy(pattern = KentReactionPattern.values()[index])
                    )
                }
            )
            KentChipRow(
                title = "Aggravation severity",
                options = KentSeverity.values().map { it.label },
                selected = input.severity.label,
                enabled = input.pattern == KentReactionPattern.AGGRAVATION,
                onPick = { index ->
                    viewModel.evaluateKentObservation(
                        input.copy(severity = KentSeverity.values()[index])
                    )
                }
            )
            KentChipRow(
                title = "Aggravation duration",
                options = KentDuration.values().map { it.label },
                selected = input.duration.label,
                enabled = input.pattern == KentReactionPattern.AGGRAVATION,
                onPick = { index ->
                    viewModel.evaluateKentObservation(
                        input.copy(duration = KentDuration.values()[index])
                    )
                }
            )
            KentChipRow(
                title = "Outcome after reaction",
                options = KentOutcome.values().map { it.label },
                selected = input.outcome.label,
                onPick = { index ->
                    viewModel.evaluateKentObservation(
                        input.copy(outcome = KentOutcome.values()[index])
                    )
                }
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = input.oldSuppressedSymptomsReappeared,
                    onCheckedChange = {
                        viewModel.evaluateKentObservation(
                            input.copy(oldSuppressedSymptomsReappeared = it)
                        )
                    },
                    modifier = Modifier.testTag("kent_old_symptoms_checkbox")
                )
                Text(
                    text = stringResource(R.string.kent_old_suppressed_label),
                    style = MaterialTheme.typography.bodySmall
                )
            }

            if (result != null) {
                Spacer(modifier = Modifier.height(8.dp))
                val isAlarm = result.action == KentObservationAction.ANTIDOTE_IMMEDIATELY ||
                        result.action == KentObservationAction.CHANGE_REMEDY
                Surface(
                    color = if (isAlarm) CrimsonContainer else VioletContainer,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "OBSERVATION ${result.observation.number} — ${result.observation.title}",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = if (isAlarm) CrimsonOnContainer else VioletOnContainer
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = result.observation.description,
                            style = MaterialTheme.typography.bodySmall,
                            color = if (isAlarm) CrimsonOnContainer else VioletOnContainer
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "ACTION: ${result.action.label}",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = if (isAlarm) CrimsonRedFlag else VioletOnContainer
                        )
                        Text(
                            text = result.observation.clinicalGuidance,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun KentChipRow(
    title: String,
    options: List<String>,
    selected: String,
    enabled: Boolean = true,
    onPick: (Int) -> Unit
) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(4.dp))
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            options.forEachIndexed { index, option ->
                FilterChip(
                    selected = selected == option,
                    enabled = enabled,
                    onClick = { onPick(index) },
                    label = {
                        Text(option, style = MaterialTheme.typography.labelSmall)
                    }
                )
            }
        }
    }
}

@Composable
fun CaseSheetCard(
    uiState: ConsultationUiState,
    viewModel: ConsultationViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ContentCopy,
                    contentDescription = null,
                    tint = EmeraldPrimary,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = stringResource(R.string.case_sheet_export_title),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = EmeraldPrimary
                )
            }

            Text(
                text = stringResource(R.string.case_sheet_export_subtitle),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                CaseMode.values().forEach { mode ->
                    FilterChip(
                        selected = uiState.caseMode == mode,
                        onClick = { viewModel.setCaseMode(mode) },
                        label = {
                            Text(mode.label, style = MaterialTheme.typography.labelSmall)
                        },
                        modifier = Modifier.testTag("case_mode_${mode.name.lowercase()}")
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = {
                    viewModel.copyCaseSheetToClipboard()
                    Toast.makeText(context, "Case sheet copied to clipboard!", Toast.LENGTH_SHORT).show()
                },
                colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("copy_case_sheet_button")
            ) {
                Icon(
                    imageVector = Icons.Default.ContentCopy,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.case_sheet_copy_button),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Print-ready PDF Case Record (docs/05 Phase 4)
            OutlinedButton(
                onClick = {
                    val path = viewModel.exportCaseSheetPdf()
                    if (path == null) {
                        Toast.makeText(context, "PDF export failed", Toast.LENGTH_SHORT).show()
                        return@OutlinedButton
                    }
                    val file = File(path)
                    val uri = FileProvider.getUriForFile(
                        context,
                        "${context.packageName}.fileprovider",
                        file
                    )
                    val shareIntent = Intent(Intent.ACTION_SEND).apply {
                        type = "application/pdf"
                        putExtra(Intent.EXTRA_STREAM, uri)
                        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    }
                    context.startActivity(
                        Intent.createChooser(shareIntent, "Share PDF Case Record")
                    )
                },
                border = BorderStroke(1.dp, EmeraldPrimary),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("export_pdf_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.case_sheet_pdf_button),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

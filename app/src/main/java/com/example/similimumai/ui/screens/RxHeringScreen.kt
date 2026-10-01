package com.example.similimumai.ui.screens

import android.widget.Toast
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.similimumai.data.engine.HomeopathyKnowledgeEngine
import com.example.similimumai.data.local.entity.SessionEntity
import com.example.similimumai.data.validation.ClinicalValidationRules
import com.example.similimumai.ui.theme.*
import com.example.similimumai.R
import com.example.similimumai.ui.viewmodel.ConsultationUiState
import com.example.similimumai.ui.viewmodel.ConsultationViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun RxHeringScreen(
    uiState: ConsultationUiState,
    viewModel: ConsultationViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    // Local form state
    var remedyName by remember(uiState.rxRemedyName) { mutableStateOf(uiState.rxRemedyName) }
    var potency by remember(uiState.rxPotency) { mutableStateOf(uiState.rxPotency) }
    var posology by remember(uiState.rxPosology) { mutableStateOf(uiState.rxPosology) }

    // Hering vectors
    var vectorInsideToOut by remember { mutableStateOf(true) }
    var vectorAboveDown by remember { mutableStateOf(true) }
    var vectorVitalToLess by remember { mutableStateOf(true) }
    var vectorReverseTime by remember { mutableStateOf(true) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .testTag("rx_hering_screen"),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(4.dp))
            // Prescription Pad Card
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
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
                                imageVector = Icons.Default.Favorite,
                                contentDescription = null,
                                tint = EmeraldPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = stringResource(R.string.rx_prescription_pad_title),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = EmeraldPrimary
                            )
                        }

                        Surface(
                            color = EmeraldContainer,
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.rx_organon_ref),
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = EmeraldOnContainer,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = remedyName,
                            onValueChange = {
                                remedyName = it
                                viewModel.updatePrescription(it, potency, uiState.rxScale, posology)
                            },
                            label = { Text(stringResource(R.string.rx_remedy_label)) },
                            modifier = Modifier
                                .weight(1.5f)
                                .testTag("rx_remedy_input"),
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = potency,
                            onValueChange = {
                                potency = it
                                viewModel.updatePrescription(remedyName, it, uiState.rxScale, posology)
                            },
                            label = { Text(stringResource(R.string.rx_potency_label)) },
                            // ux-states.md §2.5: red outline on unselected potency picker
                            isError = potency.isBlank(),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("rx_potency_input"),
                            singleLine = true
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Potency Quick Presets
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf("30C", "200C", "1M", "10M", "LM 1").forEach { p ->
                            FilterChip(
                                selected = potency == p,
                                onClick = {
                                    potency = p
                                    viewModel.updatePrescription(remedyName, p, uiState.rxScale, posology)
                                },
                                label = { Text(p, style = MaterialTheme.typography.labelSmall) },
                                modifier = Modifier.minimumInteractiveComponentSize()
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = posology,
                        onValueChange = {
                            posology = it
                            viewModel.updatePrescription(remedyName, potency, uiState.rxScale, it)
                        },
                        label = { Text(stringResource(R.string.rx_posology_label)) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("rx_posology_input"),
                        minLines = 2
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = stringResource(R.string.rx_dietary_restrictions_label),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        uiState.rxDietaryRestrictions.forEach { r ->
                            Surface(
                                color = CrimsonContainer.copy(alpha = 0.5f),
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = "✕ $r",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = CrimsonOnContainer,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Classical antidotes for the prescribed remedy
                    // (docs/product/mvp-scope.md: "antidotes & dietary prohibitions")
                    val antidotes = HomeopathyKnowledgeEngine.antidotesFor(remedyName)
                    Text(
                        text = stringResource(R.string.rx_antidotes_label),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("rx_antidotes"),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        if (antidotes.isEmpty()) {
                            Text(
                                text = stringResource(R.string.rx_antidotes_none),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        } else {
                            antidotes.forEach { a ->
                                Surface(
                                    color = IndigoContainer,
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        text = a,
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.SemiBold,
                                        color = IndigoOnContainer,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                                    )
                                }
                            }
                        }
                    }

                    // §2.6 Inimical Check Rule: strictly inimical transition gate
                    if (uiState.inimicalConflict != null) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Surface(
                            color = CrimsonContainer,
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("inimical_conflict_card")
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = uiState.inimicalConflict,
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = FontWeight.SemiBold,
                                    color = CrimsonOnContainer
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                OutlinedTextField(
                                    value = uiState.inimicalJustification,
                                    onValueChange = { viewModel.setInimicalJustification(it) },
                                    label = { Text("Clinical justification to override (required)") },
                                    modifier = Modifier.fillMaxWidth(),
                                    textStyle = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Save Case to Room Database Button
                    Button(
                        onClick = {
                            viewModel.saveSessionToDatabase()
                            val blockedByInimical = ClinicalValidationRules.inimicalJustificationRequired(
                                uiState.inimicalConflict, uiState.inimicalJustification
                            )
                            if (!blockedByInimical) {
                                Toast.makeText(context, "Consultation saved to SQLite database!", Toast.LENGTH_SHORT).show()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("save_session_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = stringResource(R.string.rx_save_record_button),
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Clinical validation gate feedback (docs/data/validation-rules.md)
                    uiState.saveError?.let { error ->
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = error,
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.SemiBold,
                            color = CrimsonOnContainer,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("save_error")
                        )
                    }
                }
            }
        }

        // LM 50-Millesimal Dilution Protocol Calculator (docs/05 Phase 3, Organon §270-§272)
        item {
            LmDilutionCard(
                uiState = uiState,
                viewModel = viewModel
            )
        }

        // Hering's Law of Cure Evaluator Card
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = IndigoGenerals,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = stringResource(R.string.hering_evaluator_title),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = IndigoGenerals
                        )
                    }

                    Text(
                        text = stringResource(R.string.hering_evaluator_subtitle),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    HeringCheckboxRow(
                        checked = vectorInsideToOut,
                        onCheckedChange = {
                            vectorInsideToOut = it
                            viewModel.evaluateHering(it, vectorAboveDown, vectorVitalToLess, vectorReverseTime)
                        },
                        title = "1. From Within Outward (Center to Periphery)",
                        subtitle = "Mental/emotional symptoms improve first; skin or surface symptoms appear."
                    )

                    HeringCheckboxRow(
                        checked = vectorAboveDown,
                        onCheckedChange = {
                            vectorAboveDown = it
                            viewModel.evaluateHering(vectorInsideToOut, it, vectorVitalToLess, vectorReverseTime)
                        },
                        title = "2. From Above Downward",
                        subtitle = "Headaches or chest pains disappear before knee or foot complaints."
                    )

                    HeringCheckboxRow(
                        checked = vectorVitalToLess,
                        onCheckedChange = {
                            vectorVitalToLess = it
                            viewModel.evaluateHering(vectorInsideToOut, vectorAboveDown, it, vectorReverseTime)
                        },
                        title = "3. From More Vital to Less Vital Organs",
                        subtitle = "Cardiac or respiratory complaints ease; nasal catarrh or eczema re-emerges."
                    )

                    HeringCheckboxRow(
                        checked = vectorReverseTime,
                        onCheckedChange = {
                            vectorReverseTime = it
                            viewModel.evaluateHering(vectorInsideToOut, vectorAboveDown, vectorVitalToLess, it)
                        },
                        title = "4. In Reverse Order of Their Appearance",
                        subtitle = "Most recent symptoms vanish first; historical ailments re-appear temporarily."
                    )

                    // Display Hering Verdict
                    val heringResult = uiState.heringEvaluation ?: run {
                        viewModel.evaluateHering(vectorInsideToOut, vectorAboveDown, vectorVitalToLess, vectorReverseTime)
                        uiState.heringEvaluation
                    }

                    if (heringResult != null) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Surface(
                            color = if (heringResult.prognosisVerdict.contains("True")) EmeraldContainer else CrimsonContainer,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = heringResult.prognosisVerdict,
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = if (heringResult.prognosisVerdict.contains("True")) EmeraldOnContainer else CrimsonRedFlag
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = heringResult.clinicalGuidance,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
            }
        }

        // Kent's 12 Prognostic Observations Follow-Up Evaluator
        item {
            KentObservationsCard(
                uiState = uiState,
                viewModel = viewModel
            )
        }

        // Plain-text Case Sheet generator + case mode selector (MVP MVE criterion 4)
        item {
            CaseSheetCard(
                uiState = uiState,
                viewModel = viewModel
            )
        }

        // Local practitioner profile (docs/data/schema.md table 1)
        item {
            PractitionerProfileCard(
                uiState = uiState,
                viewModel = viewModel
            )
        }

        // Room Database Historical Records
        item {
            Text(
                text = "SAVED CLINIC RECORDS IN ROOM DATABASE (${uiState.savedSessions.size})",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        if (uiState.savedSessions.isEmpty()) {
            item {
                Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.rx_no_saved_sessions_hint),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        } else {
            items(uiState.savedSessions) { session ->
                SavedSessionCard(session = session, uiState = uiState)
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun HeringCheckboxRow(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    title: String,
    subtitle: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.Top
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
        Column(modifier = Modifier.padding(top = 4.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PractitionerProfileCard(
    uiState: ConsultationUiState,
    viewModel: ConsultationViewModel
) {
    val doctor = uiState.doctorProfile
    var name by remember(doctor?.fullName) { mutableStateOf(doctor?.fullName ?: "") }
    var registration by remember(doctor?.registrationNumber) { mutableStateOf(doctor?.registrationNumber ?: "") }
    var qualification by remember(doctor?.qualification) { mutableStateOf(doctor?.qualification ?: "") }

    Card(
        colors = CardDefaults.cardColors(containerColor = IndigoContainer),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("practitioner_profile_card")
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = IndigoOnContainer,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.practitioner_profile_title),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = IndigoOnContainer
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text(stringResource(R.string.practitioner_full_name_label)) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = registration,
                    onValueChange = { registration = it },
                    label = { Text(stringResource(R.string.practitioner_registration_label)) },
                    singleLine = true,
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = qualification,
                    onValueChange = { qualification = it },
                    label = { Text(stringResource(R.string.practitioner_qualification_label)) },
                    singleLine = true,
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedButton(
                onClick = {
                    viewModel.updateDoctorProfile(
                        fullName = name.trim(),
                        registrationNumber = registration.trim(),
                        qualification = qualification.trim(),
                        clinicName = doctor?.clinicName ?: "",
                        clinicAddress = doctor?.clinicAddress ?: "",
                        phoneNumber = doctor?.phoneNumber ?: "",
                        email = doctor?.email ?: ""
                    )
                },
                enabled = name.isNotBlank(),
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = IndigoOnContainer)
            ) {
                Text(stringResource(R.string.practitioner_save_button))
            }
            uiState.doctorProfileError?.let { error ->
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = error,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.SemiBold,
                    color = CrimsonOnContainer,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("doctor_profile_error")
                )
            }
        }
    }
}

@Composable
private fun SavedSessionCard(session: SessionEntity, uiState: ConsultationUiState) {
    val dateString = remember(session.sessionDate) {
        val sdf = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())
        sdf.format(Date(session.sessionDate))
    }
    val symptomCount = remember(session.id, uiState.savedSymptomRecords) {
        uiState.savedSymptomRecords.count { it.sessionId == session.id }
    }
    val rubricCount = remember(session.id, uiState.savedCaseRubrics) {
        uiState.savedCaseRubrics.count { it.sessionId == session.id }
    }
    val followUpCount = remember(session.id, uiState.savedFollowUps) {
        uiState.savedFollowUps.count { it.sessionId == session.id }
    }
    val lastKentAction = remember(session.id, uiState.savedFollowUps) {
        uiState.savedFollowUps
            .filter { it.sessionId == session.id && it.recommendedAction.isNotBlank() }
            .lastOrNull()?.recommendedAction
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${session.prescribedRemedy} ${session.potency}",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = EmeraldPrimary
                )
                Text(
                    text = dateString,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Posology: ${session.posology}",
                style = MaterialTheme.typography.bodySmall
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = "Hering Status: ${session.heringStatus}",
                style = MaterialTheme.typography.labelSmall,
                color = if (session.heringStatus.contains("True")) StatusSuccess else MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                HistoryStatChip("$symptomCount symptoms", EmeraldContainer, EmeraldOnContainer)
                HistoryStatChip("$rubricCount rubrics", VioletContainer, VioletOnContainer)
                HistoryStatChip("$followUpCount follow-ups", IndigoContainer, IndigoOnContainer)
                lastKentAction?.let {
                    HistoryStatChip("Kent: $it", AmberContainer, AmberOnContainer)
                }
                // offline-strategy.md §2.4: locally saved cases flagged PENDING
                // until the WorkManager sync pipeline pushes them
                if (session.syncStatus == "PENDING") {
                    SyncPendingChip()
                }
            }
        }
    }
}

@Composable
private fun SyncPendingChip() {
    Surface(
        color = AmberContainer,
        shape = RoundedCornerShape(6.dp),
        modifier = Modifier.testTag("sync_pending_chip")
    ) {
        Text(
            text = "PENDING SYNC",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.SemiBold,
            color = AmberOnContainer,
            modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp)
        )
    }
}

@Composable
private fun HistoryStatChip(text: String, container: Color, onContainer: Color) {
    Surface(
        color = container,
        shape = RoundedCornerShape(6.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.SemiBold,
            color = onContainer,
            modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp)
        )
    }
}

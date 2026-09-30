package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.engine.HomeopathyKnowledgeEngine
import com.example.model.Prescription
import com.example.ui.theme.*
import com.example.viewmodel.ConsultationUiState
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RxHeringsScreen(
    uiState: ConsultationUiState,
    onUpdatePrescription: (String, String, String, String, String, String) -> Unit,
    onUpdateHerings: (Boolean, Boolean, Boolean, Boolean, Boolean, Boolean) -> Unit,
    onSetGeminiKey: (String) -> Unit,
    onToggleHybrid: (Boolean) -> Unit,
    onDismissInimical: () -> Unit,
    onSaveConsultation: () -> Unit = {},
    onTriggerGemini: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var selectedRemedyName by remember(uiState.prescription.selectedRemedyName) {
        mutableStateOf(uiState.prescription.selectedRemedyName)
    }
    var selectedPotency by remember(uiState.prescription.potency) {
        mutableStateOf(uiState.prescription.potency)
    }
    var selectedPosology by remember(uiState.prescription.posology) {
        mutableStateOf(uiState.prescription.posology)
    }
    var selectedVehicle by remember(uiState.prescription.vehicle) {
        mutableStateOf(uiState.prescription.vehicle)
    }
    var clinicalNotes by remember(uiState.prescription.clinicalNotes) {
        mutableStateOf(uiState.prescription.clinicalNotes)
    }

    // Hering states
    var energyMoodBetter by remember { mutableStateOf(uiState.heringsAssessment.energyMoodSleepBetter) }
    var directionAboveDownward by remember { mutableStateOf(uiState.heringsAssessment.directionAboveDownward) }
    var directionWithinOutward by remember { mutableStateOf(uiState.heringsAssessment.directionWithinOutward) }
    var reverseOrder by remember { mutableStateOf(uiState.heringsAssessment.reverseOrderOfAppearance) }
    var oldSkinReturned by remember { mutableStateOf(uiState.heringsAssessment.oldSkinEruptionReturned) }
    var warningInwardShift by remember { mutableStateOf(uiState.heringsAssessment.warningInwardSuppression) }

    // LM sensitivity state
    var isHypersensitivePatient by remember { mutableStateOf(false) }

    // Gemini API Key state
    var apiKeyInput by remember(uiState.geminiApiKey) { mutableStateOf(uiState.geminiApiKey) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Section: Inimical Warning Banner (Gibson Miller Safety Blocker)
        if (uiState.activeInimicalWarning != null) {
            item {
                Surface(
                    color = EmergencyCrimson,
                    shape = RoundedCornerShape(10.dp),
                    shadowElevation = 6.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Block, contentDescription = null, tint = Color.Yellow, modifier = Modifier.size(22.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "INIMICAL COMPATIBILITY BLOCKER",
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Black, color = Color.White)
                                )
                            }
                            IconButton(onClick = onDismissInimical, modifier = Modifier.size(24.dp)) {
                                Icon(Icons.Default.Close, contentDescription = "Dismiss", tint = Color.White, modifier = Modifier.size(16.dp))
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = uiState.activeInimicalWarning.warningText,
                            style = MaterialTheme.typography.bodySmall.copy(color = Color.White, lineHeight = 16.sp)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Safe Alternative: ${uiState.activeInimicalWarning.safeAlternative}",
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold, color = Color.Yellow)
                        )
                    }
                }
            }
        }

        // Section 1: Prescription Pad Builder
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Medication, contentDescription = null, tint = EmeraldPrimary, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "💊 PRESCRIPTION PAD & POSOLOGY",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Black,
                                    color = EmeraldPrimary,
                                    fontSize = 14.sp
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Selected Remedy Field
                    OutlinedTextField(
                        value = selectedRemedyName,
                        onValueChange = {
                            selectedRemedyName = it
                            onUpdatePrescription(it, uiState.prescription.selectedRemedyCode, selectedPotency, selectedPosology, selectedVehicle, clinicalNotes)
                        },
                        label = { Text("Selected Similimum Remedy") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Potency Selector Chips
                    Text("Potency (Scale & Dynamization):", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold))
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf("30C", "200C", "1M", "10M", "LM1", "LM2", "Q Tincture").forEach { pot ->
                            FilterChip(
                                selected = selectedPotency == pot,
                                onClick = {
                                    selectedPotency = pot
                                    onUpdatePrescription(selectedRemedyName, uiState.prescription.selectedRemedyCode, pot, selectedPosology, selectedVehicle, clinicalNotes)
                                },
                                label = { Text(pot, fontSize = 11.sp) }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Posology Selector
                    Text("Posology & Repetition Mode:", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold))
                    Spacer(modifier = Modifier.height(4.dp))
                    val posologies = listOf(
                        "Single Constitutional Dose (Stat 4 pills)",
                        "Split Water Doses (3 doses, 12h gap)",
                        "50-Millesimal LM Daily (8 succussions in 100ml water)",
                        "Acute 4-Hourly repetition in water"
                    )
                    posologies.forEach { pos ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp)
                        ) {
                            RadioButton(
                                selected = selectedPosology == pos,
                                onClick = {
                                    selectedPosology = pos
                                    onUpdatePrescription(selectedRemedyName, uiState.prescription.selectedRemedyCode, selectedPotency, pos, selectedVehicle, clinicalNotes)
                                }
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(pos, fontSize = 12.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Clinical Notes
                    OutlinedTextField(
                        value = clinicalNotes,
                        onValueChange = {
                            clinicalNotes = it
                            onUpdatePrescription(selectedRemedyName, uiState.prescription.selectedRemedyCode, selectedPotency, selectedPosology, selectedVehicle, it)
                        },
                        label = { Text("Physician Notes / Diet & Regimen") },
                        placeholder = { Text("e.g. Avoid raw camphor and eucalyptus; take on empty tongue; review in 3 weeks.") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 2
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = onSaveConsultation,
                        enabled = !uiState.isDatabaseSaving,
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                    ) {
                        if (uiState.isDatabaseSaving) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(18.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Saving Case to Local Room DB...", fontSize = 13.sp)
                        } else {
                            Icon(Icons.Default.Save, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Save Consultation to Room Database", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                    }

                    if (uiState.databaseStatusMessage != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Surface(
                            color = EmeraldPrimary.copy(alpha = 0.12f),
                            shape = RoundedCornerShape(6.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = EmeraldPrimary,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = uiState.databaseStatusMessage,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = EmeraldPrimary,
                                        fontWeight = FontWeight.Medium,
                                        fontSize = 11.sp
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }

        // Section: 50-Millesimal (LM Potency) Protocol Calculator
        if (selectedPotency.startsWith("LM")) {
            item {
                val lmProtocol = remember(selectedPotency, isHypersensitivePatient) {
                    HomeopathyKnowledgeEngine.calculateLmProtocol(selectedPotency, isHypersensitivePatient)
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = EmeraldPrimary.copy(alpha = 0.08f)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, EmeraldPrimary)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "🧪 50-MILLESIMAL (LM) PROTOCOL CALCULATOR",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = EmeraldPrimary)
                            )
                            FilterChip(
                                selected = isHypersensitivePatient,
                                onClick = { isHypersensitivePatient = !isHypersensitivePatient },
                                label = { Text("Hypersensitive?", fontSize = 10.sp) }
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = lmProtocol.patientDirections,
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp, lineHeight = 16.sp)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Method: ${lmProtocol.dilutionMethod} | Succussions: ${lmProtocol.succussions} times against palm before dose",
                            style = MaterialTheme.typography.labelSmall.copy(color = ModalityAmber, fontWeight = FontWeight.Bold, fontSize = 10.sp)
                        )
                    }
                }
            }
        }

        // Section 2: Hering's Law Follow-Up Evaluator
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.TrendingUp, contentDescription = null, tint = IndigoMind, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "⚖️ HERING'S LAW & KENT'S 12 OBSERVATIONS",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Black,
                                color = IndigoMind,
                                fontSize = 14.sp
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Follow-up verification: cure proceeds from above downward, within outward, more important to less important organs, and in reverse order of appearance.",
                        style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 11.sp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    HeringsCheckbox(
                        label = "General Vitality, Energy, Sleep & Mood are improved",
                        checked = energyMoodBetter,
                        onChecked = {
                            energyMoodBetter = it
                            onUpdateHerings(it, directionAboveDownward, directionWithinOutward, reverseOrder, oldSkinReturned, warningInwardShift)
                        }
                    )

                    HeringsCheckbox(
                        label = "Direction: From Above Downward (Head/chest better, limbs affected)",
                        checked = directionAboveDownward,
                        onChecked = {
                            directionAboveDownward = it
                            onUpdateHerings(energyMoodBetter, it, directionWithinOutward, reverseOrder, oldSkinReturned, warningInwardShift)
                        }
                    )

                    HeringsCheckbox(
                        label = "Direction: From Within Outward (Internal organ better, external discharge)",
                        checked = directionWithinOutward,
                        onChecked = {
                            directionWithinOutward = it
                            onUpdateHerings(energyMoodBetter, directionAboveDownward, it, reverseOrder, oldSkinReturned, warningInwardShift)
                        }
                    )

                    HeringsCheckbox(
                        label = "Reverse Order of Appearance (Most recent complaint vanished first)",
                        checked = reverseOrder,
                        onChecked = {
                            reverseOrder = it
                            onUpdateHerings(energyMoodBetter, directionAboveDownward, directionWithinOutward, it, oldSkinReturned, warningInwardShift)
                        }
                    )

                    HeringsCheckbox(
                        label = "Old suppressed skin rash or discharge has temporarily reappeared",
                        checked = oldSkinReturned,
                        onChecked = {
                            oldSkinReturned = it
                            onUpdateHerings(energyMoodBetter, directionAboveDownward, directionWithinOutward, reverseOrder, it, warningInwardShift)
                        }
                    )

                    HeringsCheckbox(
                        label = "⚠️ WARNING: Exterior symptom cleared but internal anxiety/dyspnea started (Suppression)",
                        checked = warningInwardShift,
                        onChecked = {
                            warningInwardShift = it
                            onUpdateHerings(energyMoodBetter, directionAboveDownward, directionWithinOutward, reverseOrder, oldSkinReturned, it)
                        },
                        isWarning = true
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Surface(
                        color = if (warningInwardShift) EmergencyCrimson.copy(alpha = 0.12f) else EmeraldPrimary.copy(alpha = 0.12f),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = if (warningInwardShift) Icons.Default.Warning else Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = if (warningInwardShift) EmergencyCrimson else EmeraldPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = uiState.heringsAssessment.recommendation,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    color = if (warningInwardShift) EmergencyCrimson else EmeraldPrimary,
                                    fontSize = 11.sp
                                )
                            )
                        }
                    }
                }
            }
        }

        // Section: 1-Tap WhatsApp Patient Follow-Up Message Generator
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Chat, contentDescription = null, tint = EmeraldPrimary, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "💬 7-DAY WHATSAPP PATIENT CHECK-IN",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Black, color = EmeraldPrimary)
                            )
                        }

                        Button(
                            onClick = {
                                val message = HomeopathyKnowledgeEngine.generateWhatsAppFollowUpMessage(
                                    patientName = uiState.activePatient.name,
                                    remedyName = selectedRemedyName,
                                    potency = selectedPotency,
                                    dayNumber = 7
                                )
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                val clip = ClipData.newPlainText("WhatsApp Check-In", message)
                                clipboard.setPrimaryClip(clip)
                                Toast.makeText(context, "WhatsApp check-in copied to clipboard!", Toast.LENGTH_SHORT).show()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                            modifier = Modifier.height(28.dp)
                        ) {
                            Text("Copy Check-In", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Generates a structured bilingual patient check-in message to track initial aggravation and Hering's Law direction of cure remotely.",
                        style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 11.sp)
                    )
                }
            }
        }

        // Section 3: Formatted Case Record & Export
        item {
            val fullCaseReport = remember(uiState) {
                generateCaseSheetReport(uiState)
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Description, contentDescription = null, tint = EmeraldPrimary, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "📄 CLINICAL CASE SHEET & SUMMARY",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Black,
                                    color = EmeraldPrimary,
                                    fontSize = 14.sp
                                )
                            )
                        }

                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            OutlinedButton(
                                onClick = {
                                    val sendIntent = android.content.Intent().apply {
                                        action = android.content.Intent.ACTION_SEND
                                        putExtra(android.content.Intent.EXTRA_TEXT, fullCaseReport)
                                        putExtra(android.content.Intent.EXTRA_SUBJECT, "Similimum AI - Case Record (${uiState.activePatient.name})")
                                        type = "text/plain"
                                    }
                                    val shareIntent = android.content.Intent.createChooser(sendIntent, "Share Clinical Case Sheet")
                                    context.startActivity(shareIntent)
                                },
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                modifier = Modifier.height(28.dp)
                            ) {
                                Icon(Icons.Default.Share, contentDescription = "Share", modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Share", fontSize = 11.sp)
                            }

                            Button(
                                onClick = {
                                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                    val clip = ClipData.newPlainText("Homeopathic Case Record", fullCaseReport)
                                    clipboard.setPrimaryClip(clip)
                                    Toast.makeText(context, "Full Case Record copied to clipboard!", Toast.LENGTH_SHORT).show()
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp),
                                modifier = Modifier.height(28.dp)
                            ) {
                                Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Copy Sheet", fontSize = 11.sp)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = fullCaseReport,
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontFamily = FontFamily.Monospace,
                                fontSize = 10.sp,
                                lineHeight = 14.sp
                            ),
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                }
            }
        }

        // Section 4: Gemini Cloud Engine & API Settings
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = PqrsViolet, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "⚡ GEMINI REASONING ENGINE SETTINGS",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Black,
                                color = PqrsViolet,
                                fontSize = 14.sp
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Similimum AI includes a zero-latency Local Engine (<10ms) pre-seeded with 35+ classical remedies and 120+ rubrics. Optionally provide a Gemini API Key to enable cloud reasoning with gemini-2.5-flash.",
                        style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 11.sp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = apiKeyInput,
                        onValueChange = {
                            apiKeyInput = it
                            onSetGeminiKey(it)
                        },
                        label = { Text("Gemini API Key (Optional)") },
                        placeholder = { Text("AIzaSy...") },
                        trailingIcon = {
                            if (apiKeyInput.isNotBlank()) {
                                Icon(Icons.Default.CheckCircle, contentDescription = "Configured", tint = EmeraldPrimary)
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Hybrid Mode (Local Engine + Cloud Fallback)",
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium)
                        )
                        Switch(
                            checked = uiState.isHybridMode,
                            onCheckedChange = { onToggleHybrid(it) }
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Button(
                        onClick = onTriggerGemini,
                        enabled = !uiState.isGeminiProcessing,
                        colors = ButtonDefaults.buttonColors(containerColor = PqrsViolet),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth().height(42.dp)
                    ) {
                        if (uiState.isGeminiProcessing) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Synthesizing via Gemini 2.5 Flash...", fontSize = 12.sp)
                        } else {
                            Icon(Icons.Default.Psychology, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Run Gemini Constitutional Synthesis", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }

                    if (uiState.geminiSummary != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Surface(
                            color = IndigoMind.copy(alpha = 0.08f),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(8.dp)) {
                                Text(
                                    text = "Last Gemini Reasoning Synthesis:",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = IndigoMind)
                                )
                                Text(
                                    text = uiState.geminiSummary,
                                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun HeringsCheckbox(
    label: String,
    checked: Boolean,
    onChecked: (Boolean) -> Unit,
    isWarning: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onChecked,
            colors = CheckboxDefaults.colors(
                checkedColor = if (isWarning) EmergencyCrimson else EmeraldPrimary
            )
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall.copy(
                fontSize = 12.sp,
                color = if (isWarning && checked) EmergencyCrimson else MaterialTheme.colorScheme.onSurface,
                fontWeight = if (isWarning && checked) FontWeight.Bold else FontWeight.Normal
            )
        )
    }
}

private fun generateCaseSheetReport(uiState: ConsultationUiState): String {
    val date = SimpleDateFormat("dd-MMM-yyyy HH:mm", Locale.getDefault()).format(Date())
    val p = uiState.activePatient
    val top = uiState.repertorizationResults.take(3)

    return buildString {
        appendLine("==========================================================")
        appendLine("           SIMILIMUM AI — CLINICAL CASE RECORD           ")
        appendLine("==========================================================")
        appendLine("Date: $date | Consultation: ${p.mode.label}")
        appendLine("Patient: ${p.name}, Age: ${p.age}y, Sex: ${p.gender}")
        appendLine("Informed Verbal Consent: ${if (p.consentObtained) "OBTAINED ✓" else "PENDING"}")
        appendLine("Chief Complaint: ${p.chiefComplaintPreview}")
        appendLine("Methodological School: ${uiState.selectedSchool.label}")
        appendLine("----------------------------------------------------------")
        appendLine("ACTIVE TOTALITY & REPERTORY RUBRICS (${uiState.activeRubrics.size}):")
        uiState.activeRubrics.forEachIndexed { i, r ->
            appendLine("  ${i + 1}. [×${r.weight}] ${r.path}")
        }
        appendLine("----------------------------------------------------------")
        appendLine("TOP 3 REPERTORIZATION CANDIDATES:")
        top.forEachIndexed { i, entry ->
            appendLine("  #${i + 1} ${entry.remedyName} (${entry.remedyCode.uppercase()}) -> ${entry.confidencePercent}% [Score: ${entry.totalWeightedScore}, Covered: ${entry.rubricsCovered}/${entry.totalRubricsCount}]")
        }
        appendLine("----------------------------------------------------------")
        appendLine("PRESCRIPTION & DISPENSING INSTRUCTION:")
        appendLine("  Remedy: ${uiState.prescription.selectedRemedyName} ${uiState.prescription.potency}")
        appendLine("  Posology: ${uiState.prescription.posology}")
        appendLine("  Vehicle: ${uiState.prescription.vehicle}")
        appendLine("  Diet & Regimen: ${uiState.prescription.auxiliaryRegimen.joinToString("; ")}")
        if (uiState.prescription.clinicalNotes.isNotBlank()) {
            appendLine("  Physician Notes: ${uiState.prescription.clinicalNotes}")
        }
        appendLine("----------------------------------------------------------")
        appendLine("HERING'S LAW FOLLOW-UP EVALUATION:")
        appendLine("  Recommendation: ${uiState.heringsAssessment.recommendation}")
        appendLine("==========================================================")
        appendLine("Physician Authority: Final prescription decision rests solely")
        appendLine("with the qualified consulting homeopathic physician.")
        appendLine("==========================================================")
    }
}

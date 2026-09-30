package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.local.entity.PatientEntity
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.EmergencyCrimson
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatientRecordsDialog(
    savedPatients: List<PatientEntity>,
    onDismiss: () -> Unit,
    onSelectPatient: (PatientEntity) -> Unit,
    onDeletePatient: (Long) -> Unit,
    onCreateNewPatient: (String, Int, String, String) -> Unit
) {
    var showCreateForm by remember { mutableStateOf(false) }

    var newName by remember { mutableStateOf("") }
    var newAge by remember { mutableStateOf("30") }
    var newGender by remember { mutableStateOf("Female") }
    var newChiefComplaint by remember { mutableStateOf("") }

    val dateFormat = remember { SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault()) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.85f)
                .testTag("patient_records_dialog"),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.FolderShared,
                            contentDescription = null,
                            tint = EmeraldPrimary,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (showCreateForm) "New Patient Intake" else "Saved Patient Records",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                if (showCreateForm) {
                    // Create New Patient Form
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(vertical = 4.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedTextField(
                            value = newName,
                            onValueChange = { newName = it },
                            label = { Text("Full Name *") },
                            placeholder = { Text("e.g. Ramesh Patel") },
                            modifier = Modifier.fillMaxWidth().testTag("new_patient_name_input"),
                            singleLine = true
                        )

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = newAge,
                                onValueChange = { newAge = it.filter { char -> char.isDigit() } },
                                label = { Text("Age") },
                                modifier = Modifier.weight(1f),
                                singleLine = true
                            )

                            // Gender chips
                            Column(modifier = Modifier.weight(2f)) {
                                Text("Gender:", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                    listOf("Female", "Male", "Other").forEach { g ->
                                        FilterChip(
                                            selected = newGender == g,
                                            onClick = { newGender = g },
                                            label = { Text(g, fontSize = 11.sp) }
                                        )
                                    }
                                }
                            }
                        }

                        OutlinedTextField(
                            value = newChiefComplaint,
                            onValueChange = { newChiefComplaint = it },
                            label = { Text("Chief Complaint / Ailment") },
                            placeholder = { Text("e.g. Chronic recurrent asthma since childhood") },
                            modifier = Modifier.fillMaxWidth(),
                            minLines = 2
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = { showCreateForm = false }) {
                            Text("Back to Records")
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = {
                                if (newName.isNotBlank()) {
                                    onCreateNewPatient(
                                        newName.trim(),
                                        newAge.toIntOrNull() ?: 30,
                                        newGender,
                                        newChiefComplaint.trim()
                                    )
                                }
                            },
                            enabled = newName.isNotBlank(),
                            colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary)
                        ) {
                            Text("Start Consultation", fontWeight = FontWeight.Bold)
                        }
                    }
                } else {
                    // Patient List View
                    if (savedPatients.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    imageVector = Icons.Default.MedicalInformation,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
                                    modifier = Modifier.size(48.dp)
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = "No saved patients in local database yet.",
                                    style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Save consultations from the Rx/Hering screen, or register a new patient below.",
                                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                )
                            }
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(savedPatients) { patient ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { onSelectPatient(patient) },
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(10.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Text(
                                                    text = patient.fullName,
                                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                                                )
                                                Spacer(modifier = Modifier.width(6.dp))
                                                Surface(
                                                    color = EmeraldPrimary.copy(alpha = 0.15f),
                                                    shape = RoundedCornerShape(4.dp)
                                                ) {
                                                    Text(
                                                        text = patient.mrn,
                                                        fontSize = 10.sp,
                                                        color = EmeraldPrimary,
                                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                                    )
                                                }
                                            }
                                            Spacer(modifier = Modifier.height(2.dp))
                                            Text(
                                                text = "${patient.age}y, ${patient.gender} • Miasm: ${patient.miasmaticTendency}",
                                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                            )
                                            Text(
                                                text = "Registered: ${dateFormat.format(Date(patient.createdAt))}",
                                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp, color = MaterialTheme.colorScheme.outline)
                                            )
                                        }

                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            IconButton(onClick = { onSelectPatient(patient) }) {
                                                Icon(Icons.Default.ArrowForward, contentDescription = "Load Patient", tint = EmeraldPrimary)
                                            }
                                            IconButton(onClick = { onDeletePatient(patient.id) }) {
                                                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = EmergencyCrimson.copy(alpha = 0.8f))
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = { showCreateForm = true },
                        modifier = Modifier.fillMaxWidth().testTag("add_walk_in_patient_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(Icons.Default.PersonAdd, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Add New Walk-In Patient", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

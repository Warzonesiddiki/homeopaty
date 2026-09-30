package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.engine.HomeopathyKnowledgeEngine
import com.example.model.RepertorizationEntry
import com.example.model.RepertorySchool
import com.example.model.Rubric
import com.example.ui.theme.*
import com.example.viewmodel.ConsultationUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepertoryMatrixScreen(
    uiState: ConsultationUiState,
    onAddRubric: (Rubric) -> Unit,
    onRemoveRubric: (String) -> Unit,
    onSetWeight: (String, Int) -> Unit,
    onToggleEliminating: (String) -> Unit,
    onSelectSchool: (RepertorySchool) -> Unit,
    modifier: Modifier = Modifier
) {
    var showAddRubricDialog by remember { mutableStateOf(false) }
    val topRemedies = uiState.repertorizationResults.take(8)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Repertory Header & Action Bar
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 2.dp
        ) {
            Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "📊 MULTI-SCHOOL REPERTORIZATION MATRIX",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Black,
                                color = EmeraldPrimary,
                                letterSpacing = 0.5.sp
                            )
                        )
                        Text(
                            text = "${uiState.activeRubrics.size} Active Rubrics | Graded 1, 2, 3 Hierarchy",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 11.sp
                            )
                        )
                    }

                    Button(
                        onClick = { showAddRubricDialog = true },
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                        modifier = Modifier.height(32.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Add", modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Add Rubric", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // 5-School Switcher Bar
                Text(
                    text = "METHODOLOGICAL LENS (Tap to Re-weight Totality):",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 9.sp
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    RepertorySchool.values().forEach { school ->
                        val isSelected = uiState.selectedSchool == school
                        FilterChip(
                            selected = isSelected,
                            onClick = { onSelectSchool(school) },
                            label = { Text(school.label, fontSize = 10.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = EmeraldPrimary.copy(alpha = 0.15f),
                                selectedLabelColor = EmeraldPrimary
                            ),
                            modifier = Modifier.height(26.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "${uiState.selectedSchool.author} • ${uiState.selectedSchool.focusDescription}",
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 10.sp,
                        color = IndigoMind,
                        fontStyle = FontStyle.Italic
                    )
                )
            }
        }

        if (uiState.activeRubrics.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f))
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.GridOn,
                            contentDescription = null,
                            tint = Slate400,
                            modifier = Modifier.size(40.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "No Rubrics Active Yet",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Speak into the mic, stream a simulated case, or tap '+ Add Rubric' above to repertorize candidate remedies across schools.",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center
                            )
                        )
                    }
                }
            }
        } else {
            // Interactive Rubric Matrix Table
            val horizontalScrollState = rememberScrollState()

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        // Header Row: Remedy names & codes
                        item {
                            Surface(
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                tonalElevation = 3.dp
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .horizontalScroll(horizontalScrollState)
                                        .padding(vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // Rubric Column Header
                                    Box(
                                        modifier = Modifier
                                            .width(220.dp)
                                            .padding(horizontal = 8.dp)
                                    ) {
                                        Text(
                                            text = "SYMPTOM / RUBRIC",
                                            fontWeight = FontWeight.ExtraBold,
                                            fontSize = 11.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }

                                    // Remedy Columns Headers
                                    topRemedies.forEach { entry ->
                                        Column(
                                            modifier = Modifier
                                                .width(72.dp)
                                                .padding(horizontal = 2.dp),
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Text(
                                                text = entry.remedyCode.uppercase(),
                                                style = MaterialTheme.typography.labelSmall.copy(
                                                    fontWeight = FontWeight.Black,
                                                    color = EmeraldPrimary,
                                                    fontSize = 11.sp
                                                )
                                            )
                                            Text(
                                                text = "${entry.confidencePercent}%",
                                                style = MaterialTheme.typography.labelSmall.copy(
                                                    fontWeight = FontWeight.Bold,
                                                    color = ModalityAmber,
                                                    fontSize = 10.sp
                                                )
                                            )
                                            Text(
                                                text = "${entry.totalWeightedScore} pts",
                                                fontSize = 9.sp,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        // Data Rows: Each rubric with weights & grades
                        items(uiState.activeRubrics, key = { it.id }) { rubric ->
                            Surface(
                                color = MaterialTheme.colorScheme.surface,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .horizontalScroll(horizontalScrollState)
                                        .padding(vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // Rubric Details & Weight Controls
                                    Column(
                                        modifier = Modifier
                                            .width(220.dp)
                                            .padding(horizontal = 8.dp)
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Text(
                                                text = rubric.path,
                                                style = MaterialTheme.typography.bodySmall.copy(
                                                    fontWeight = FontWeight.SemiBold,
                                                    fontSize = 11.sp,
                                                    lineHeight = 14.sp
                                                ),
                                                maxLines = 2,
                                                overflow = TextOverflow.Ellipsis,
                                                modifier = Modifier.weight(1f)
                                            )
                                            IconButton(
                                                onClick = { onRemoveRubric(rubric.id) },
                                                modifier = Modifier.size(20.dp)
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Close,
                                                    contentDescription = "Remove",
                                                    tint = Slate400,
                                                    modifier = Modifier.size(14.dp)
                                                )
                                            }
                                        }

                                        Spacer(modifier = Modifier.height(2.dp))

                                        // Hierarchy Weight Selector Chips
                                        Row(
                                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            listOf(1, 2, 3).forEach { wt ->
                                                val isSelected = rubric.weight == wt
                                                Box(
                                                    modifier = Modifier
                                                        .clip(RoundedCornerShape(3.dp))
                                                        .background(if (isSelected) EmeraldPrimary else Slate200)
                                                        .clickable { onSetWeight(rubric.id, wt) }
                                                        .padding(horizontal = 5.dp, vertical = 1.dp)
                                                ) {
                                                    Text(
                                                        text = "×$wt",
                                                        fontSize = 9.sp,
                                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                                        color = if (isSelected) Color.White else Slate700
                                                    )
                                                }
                                            }

                                            Spacer(modifier = Modifier.width(4.dp))

                                            // Eliminating Rubric Toggle
                                            Box(
                                                modifier = Modifier
                                                    .clip(RoundedCornerShape(3.dp))
                                                    .background(if (rubric.isEliminating) EmergencyCrimson else Color.Transparent)
                                                    .clickable { onToggleEliminating(rubric.id) }
                                                    .padding(horizontal = 4.dp, vertical = 1.dp)
                                            ) {
                                                Text(
                                                    text = if (rubric.isEliminating) "ELIM" else "Elim?",
                                                    fontSize = 9.sp,
                                                    color = if (rubric.isEliminating) Color.White else Slate500,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }
                                        }
                                    }

                                    // Remedy Grade Cells
                                    topRemedies.forEach { entry ->
                                        val grade = rubric.grades[entry.remedyCode] ?: 0
                                        Box(
                                            modifier = Modifier
                                                .width(72.dp)
                                                .padding(horizontal = 2.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            GradeCell(grade = grade)
                                        }
                                    }
                                }
                            }
                            Divider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                        }
                    }
                }
            }
        }
    }

    // Add Rubric Dialog
    if (showAddRubricDialog) {
        AddRubricBrowserDialog(
            onDismiss = { showAddRubricDialog = false },
            onSelectRubric = { rubric ->
                onAddRubric(rubric)
                showAddRubricDialog = false
            }
        )
    }
}

@Composable
private fun GradeCell(grade: Int) {
    when (grade) {
        3 -> {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(EmeraldPrimary.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "3",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Black,
                        color = EmeraldPrimary,
                        fontSize = 13.sp
                    )
                )
            }
        }
        2 -> {
            Text(
                text = "2",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Italic,
                    color = ModalityAmber,
                    fontSize = 12.sp
                )
            )
        }
        1 -> {
            Text(
                text = "1",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = Slate600,
                    fontSize = 11.sp
                )
            )
        }
        else -> {
            Text(
                text = "—",
                color = Slate400,
                fontSize = 11.sp
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddRubricBrowserDialog(
    onDismiss: () -> Unit,
    onSelectRubric: (Rubric) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    val filteredRubrics = remember(searchQuery) {
        if (searchQuery.isBlank()) {
            HomeopathyKnowledgeEngine.allRubrics
        } else {
            HomeopathyKnowledgeEngine.allRubrics.filter {
                it.path.contains(searchQuery, ignoreCase = true) ||
                it.bilingualKeywords.any { kw -> kw.contains(searchQuery, ignoreCase = true) }
            }
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Rubric Browser") },
        text = {
            Column(modifier = Modifier.fillMaxWidth().height(350.dp)) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search symptom, rubric, Hindi keyword...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(filteredRubrics) { rubric ->
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onSelectRubric(rubric) },
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Column(modifier = Modifier.padding(8.dp)) {
                                Text(
                                    text = rubric.path,
                                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)
                                )
                                Text(
                                    text = "Remedies: " + rubric.grades.keys.joinToString(", "),
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        fontSize = 10.sp
                                    )
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close")
            }
        }
    )
}

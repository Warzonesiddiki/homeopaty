package com.example.similimumai.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.similimumai.data.engine.HomeopathyKnowledgeEngine
import com.example.similimumai.data.model.Remedy
import com.example.similimumai.ui.theme.*
import com.example.similimumai.ui.viewmodel.ConsultationUiState
import com.example.similimumai.ui.viewmodel.ConsultationViewModel

@Composable
fun MateriaMedicaScreen(
    uiState: ConsultationUiState,
    viewModel: ConsultationViewModel,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    val allRemedies = HomeopathyKnowledgeEngine.polychrests
    val filteredRemedies = remember(searchQuery) {
        if (searchQuery.isBlank()) allRemedies
        else allRemedies.filter {
            it.fullName.contains(searchQuery, ignoreCase = true) ||
                    it.abbreviation.contains(searchQuery, ignoreCase = true) ||
                    it.commonName.contains(searchQuery, ignoreCase = true)
        }
    }

    val selectedRemedy = uiState.selectedRemedyDetail ?: allRemedies[0]
    var showCompareDialog by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .testTag("materia_medica_screen"),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(4.dp))
            // Search Bar & Comparative Mode Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search 30+ Polychrests...") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search"
                        )
                    },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("remedy_search_input"),
                    singleLine = true
                )

                Button(
                    onClick = { showCompareDialog = true },
                    colors = ButtonDefaults.buttonColors(containerColor = IndigoGenerals),
                    modifier = Modifier.testTag("open_compare_dialog_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Compare")
                }
            }
        }

        // Quick Remedy Selector Horizontal Chips
        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(filteredRemedies) { remedy ->
                    FilterChip(
                        selected = selectedRemedy.id == remedy.id,
                        onClick = { viewModel.selectRemedyDetail(remedy) },
                        label = {
                            Text(
                                text = remedy.abbreviation,
                                fontWeight = if (selectedRemedy.id == remedy.id) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = EmeraldPrimary,
                            selectedLabelColor = Color.White
                        ),
                        modifier = Modifier.testTag("materia_chip_${remedy.abbreviation}")
                    )
                }
            }
        }

        // Detailed Selected Remedy Card
        item {
            RemedyDetailCard(remedy = selectedRemedy)
        }

        // Inimical & Drug Relationship Safety Matrix
        item {
            RemedyRelationshipCard(remedy = selectedRemedy)
        }

        // Side-by-Side Comparison Section (if set)
        if (uiState.compareRemedyA != null && uiState.compareRemedyB != null) {
            item {
                ComparativeAnalysisCard(
                    remedyA = uiState.compareRemedyA,
                    remedyB = uiState.compareRemedyB,
                    conflictMessage = uiState.inimicalAlertMessage
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    // Remedy Comparison Selection Dialog
    if (showCompareDialog) {
        var tempRemedyA by remember { mutableStateOf(uiState.compareRemedyA ?: allRemedies[0]) }
        var tempRemedyB by remember { mutableStateOf(uiState.compareRemedyB ?: allRemedies[1]) }

        AlertDialog(
            onDismissRequest = { showCompareDialog = false },
            title = {
                Text(
                    text = "Comparative Materia Medica",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = "Select two competing polychrests to compare keynotes and verify inimical safety:",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Text("Remedy A:", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelSmall)
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        items(allRemedies.take(12)) { r ->
                            FilterChip(
                                selected = tempRemedyA.id == r.id,
                                onClick = { tempRemedyA = r },
                                label = { Text(r.abbreviation) }
                            )
                        }
                    }

                    Text("Remedy B:", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelSmall)
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        items(allRemedies.take(12)) { r ->
                            FilterChip(
                                selected = tempRemedyB.id == r.id,
                                onClick = { tempRemedyB = r },
                                label = { Text(r.abbreviation) }
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.setComparisonRemedies(tempRemedyA, tempRemedyB)
                        showCompareDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary)
                ) {
                    Text("Compare Side-by-Side")
                }
            },
            dismissButton = {
                TextButton(onClick = { showCompareDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun RemedyDetailCard(remedy: Remedy) {
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
                Column {
                    Text(
                        text = remedy.fullName,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = EmeraldPrimary
                    )
                    Text(
                        text = "Common: ${remedy.commonName}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Surface(
                    color = EmeraldContainer,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = remedy.abbreviation,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = EmeraldOnContainer,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Thermal & Miasm Badges
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Surface(
                    color = IndigoContainer,
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = "Thermal: ${remedy.thermalState.name}",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = IndigoOnContainer,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
                Surface(
                    color = VioletContainer,
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = "Miasm: ${remedy.dominantMiasm.name}",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = VioletOnContainer,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "CLASSICAL KEYNOTES & CHARACTERISTICS (§153 PQRS)",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(6.dp))

            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                remedy.keynotes.forEachIndexed { i, keynote ->
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Text(
                            text = "•",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = EmeraldPrimary
                        )
                        Text(
                            text = keynote,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun RemedyRelationshipCard(remedy: Remedy) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = null,
                    tint = if (remedy.inimicalRemedies.isNotEmpty()) CrimsonRedFlag else StatusSuccess,
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = "CLINICAL DRUG RELATIONSHIPS & INIMICAL SAFETY",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (remedy.inimicalRemedies.isNotEmpty()) {
                Surface(
                    color = CrimsonContainer,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text(
                            text = "STRICTLY INIMICAL (NEVER PRESCRIBE IN SEQUENCE):",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.ExtraBold,
                            color = CrimsonRedFlag
                        )
                        Text(
                            text = remedy.inimicalRemedies.joinToString(", "),
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = CrimsonOnContainer
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Complementary & Antidotes
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Complementary Follow-ups:",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = StatusSuccess
                    )
                    Text(
                        text = remedy.complementaryRemedies.ifEmpty { listOf("None listed") }.joinToString(", "),
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Antidotes:",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = remedy.antidoteRemedies.ifEmpty { listOf("Camphora") }.joinToString(", "),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}

@Composable
private fun ComparativeAnalysisCard(
    remedyA: Remedy,
    remedyB: Remedy,
    conflictMessage: String?
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = if (conflictMessage != null) CrimsonContainer else IndigoContainer.copy(alpha = 0.4f)
        ),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = "COMPARATIVE ANALYSIS: ${remedyA.abbreviation} vs ${remedyB.abbreviation}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = if (conflictMessage != null) CrimsonRedFlag else IndigoGenerals
            )

            if (conflictMessage != null) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "⚠️ $conflictMessage",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                    color = CrimsonOnContainer
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Column Remedy A
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = remedyA.fullName,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Thermal: ${remedyA.thermalState.name}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = remedyA.keynotes.firstOrNull() ?: "",
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                // Column Remedy B
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = remedyB.fullName,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Thermal: ${remedyB.thermalState.name}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = remedyB.keynotes.firstOrNull() ?: "",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}

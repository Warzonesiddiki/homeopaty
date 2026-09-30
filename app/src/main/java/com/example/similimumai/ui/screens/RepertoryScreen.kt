package com.example.similimumai.ui.screens

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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.similimumai.data.engine.HomeopathyKnowledgeEngine
import com.example.similimumai.data.model.*
import com.example.similimumai.data.validation.ClinicalValidationRules
import com.example.similimumai.ui.theme.*
import com.example.similimumai.R
import com.example.similimumai.ui.viewmodel.ConsultationUiState
import com.example.similimumai.ui.viewmodel.ConsultationViewModel
import com.example.similimumai.ui.viewmodel.NavigationTab

@Composable
fun RepertoryScreen(
    uiState: ConsultationUiState,
    viewModel: ConsultationViewModel,
    modifier: Modifier = Modifier
) {
    var showAddRubricDialog by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .testTag("repertory_screen"),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(4.dp))
            // School & Elimination Filter Controls
            RepertoryControlsCard(
                selectedSchool = uiState.selectedSchool,
                onSchoolChange = { viewModel.setRepertorySchool(it) },
                thermalFilter = uiState.thermalEliminationFilter,
                onThermalFilterChange = { viewModel.setThermalEliminationFilter(it) }
            )
        }

        // Active Rubrics Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.repertory_active_rubrics_title),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                TextButton(
                    onClick = { showAddRubricDialog = true },
                    modifier = Modifier.testTag("add_rubric_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(stringResource(R.string.repertory_add_rubric_button))
                }
            }
        }

        // §2.5 Eliminator guard: warn (not block) when too many rubrics empty the candidate set
        ClinicalValidationRules.eliminatingRubricWarning(
            uiState.activeRubrics.count { it.isEliminating }
        )?.let { warning ->
            item {
                Surface(
                    color = AmberContainer,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("eliminating_warning")
                ) {
                    Text(
                        text = warning,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = AmberOnContainer,
                        modifier = Modifier.padding(10.dp)
                    )
                }
            }
        }

        // Active Rubrics List Chips
        if (uiState.activeRubrics.isEmpty()) {
            item {
                Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.repertory_no_rubrics_hint),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        } else {
            items(uiState.activeRubrics) { rubric ->
                RubricItemCard(
                    rubric = rubric,
                    onDelete = { viewModel.removeRubric(rubric.id) }
                )
            }
        }

        // Repertorization Candidate Leaderboard
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.repertory_candidates_title),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = EmeraldPrimary
                )
                Text(
                    text = "${uiState.remedyScores.size} evaluated",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // ux-states.md §2.3: elimination-conflict + empty leaderboard states
        item {
            val hasRubrics = uiState.activeRubrics.isNotEmpty()
            val allZero = uiState.remedyScores.isNotEmpty() &&
                uiState.remedyScores.all { it.totalScore == 0 }
            when {
                hasRubrics && allZero -> {
                    val elimName = uiState.activeRubrics.firstOrNull { it.isEliminating }?.name
                        ?: uiState.thermalEliminationFilter?.let {
                            "Thermal Elimination Filter (${it.name})"
                        }
                    Surface(
                        color = AmberContainer,
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("elimination_conflict_card")
                    ) {
                        Text(
                            text = "Zero remedies cover all active rubrics." +
                                (elimName?.let { " Consider unchecking the strict eliminator on $it." }
                                    ?: " Review or remove the strictest rubrics."),
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.SemiBold,
                            color = AmberOnContainer,
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                }
                !hasRubrics -> {
                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("leaderboard_empty_state")
                    ) {
                        Text(
                            text = "No rubrics ingested yet — stream a case on the HUD or tap '+ Add Rubric' above to build the candidate matrix.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(14.dp)
                        )
                    }
                }
            }
        }

        // Remedy Scores Table
        items(uiState.remedyScores.take(10)) { score ->
            RemedyScoreRow(
                score = score,
                onClick = {
                    viewModel.selectRemedyDetail(score.remedy)
                    viewModel.selectNavigationTab(NavigationTab.MATERIA_MEDICA)
                }
            )
        }

        // responsive.md §2.2: candidate matrix — rubric rows × top-5 remedy
        // columns. Compact: horizontally scrollable grid; Expanded: full
        // widescreen matrix without horizontal scroll truncation.
        if (uiState.activeRubrics.isNotEmpty() && uiState.remedyScores.isNotEmpty()) {
            item {
                CandidateMatrixCard(
                    rubrics = uiState.activeRubrics,
                    scores = uiState.remedyScores.take(5),
                    isExpanded = computeWindowSizeClass().isExpanded
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    // Add Rubric Picker Dialog
    if (showAddRubricDialog) {
        AlertDialog(
            onDismissRequest = { showAddRubricDialog = false },
            title = {
                Text(
                    text = stringResource(R.string.repertory_add_rubric_title),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                LazyColumn(
                    modifier = Modifier.heightIn(max = 400.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(HomeopathyKnowledgeEngine.rubrics) { r ->
                        val isAlreadyAdded = uiState.activeRubrics.any { it.id == r.id }
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .border(
                                    1.dp,
                                    if (isAlreadyAdded) IndigoGenerals else MaterialTheme.colorScheme.outlineVariant,
                                    RoundedCornerShape(8.dp)
                                )
                                .clickable(enabled = !isAlreadyAdded) {
                                    viewModel.addRubric(r)
                                    showAddRubricDialog = false
                                }
                                .padding(10.dp)
                                .testTag("pick_rubric_${r.id}")
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "${r.chapter}: ${r.name}",
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.SemiBold,
                                        color = if (isAlreadyAdded) IndigoGenerals else MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = "${r.remedyGrades.size} remedies mapped",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                if (isAlreadyAdded) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Added",
                                        tint = IndigoGenerals,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showAddRubricDialog = false }) {
                    Text(stringResource(R.string.common_done))
                }
            }
        )
    }
}

@Composable
private fun RepertoryControlsCard(
    selectedSchool: RepertorySchool,
    onSchoolChange: (RepertorySchool) -> Unit,
    thermalFilter: ThermalState?,
    onThermalFilterChange: (ThermalState?) -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = stringResource(R.string.repertory_school_title),
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                RepertorySchool.values().forEach { school ->
                    FilterChip(
                        selected = selectedSchool == school,
                        onClick = { onSchoolChange(school) },
                        label = {
                            Text(
                                text = when (school) {
                                    RepertorySchool.KENT -> "Kent (Mind 3x)"
                                    RepertorySchool.BOENNINGHAUSEN -> "TPB (Modalities)"
                                    RepertorySchool.BOGER -> "Boger (PQRS)"
                                },
                                style = MaterialTheme.typography.labelSmall
                            )
                        },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("school_chip_${school.name}").minimumInteractiveComponentSize())
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = stringResource(R.string.repertory_thermal_filter_title),
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                FilterChip(
                    selected = thermalFilter == null,
                    onClick = { onThermalFilterChange(null) },
                    label = { Text("All", style = MaterialTheme.typography.labelSmall) },
                    modifier = Modifier.weight(1f).minimumInteractiveComponentSize())
                FilterChip(
                    selected = thermalFilter == ThermalState.HOT,
                    onClick = { onThermalFilterChange(ThermalState.HOT) },
                    label = { Text("Only Hot", style = MaterialTheme.typography.labelSmall) },
                    modifier = Modifier.weight(1f).minimumInteractiveComponentSize())
                FilterChip(
                    selected = thermalFilter == ThermalState.CHILLY,
                    onClick = { onThermalFilterChange(ThermalState.CHILLY) },
                    label = { Text("Only Chilly", style = MaterialTheme.typography.labelSmall) },
                    modifier = Modifier.weight(1f).minimumInteractiveComponentSize())
            }
        }
    }
}

@Composable
private fun RubricItemCard(rubric: Rubric, onDelete: () -> Unit) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("active_rubric_${rubric.id}")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "[${rubric.chapter}]",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = IndigoGenerals
                    )
                    if (rubric.isPqrs) {
                        Surface(
                            color = VioletContainer,
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.pqrs_153_label_alt),
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = VioletPqrs,
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = rubric.name,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            IconButton(
                onClick = onDelete,
                modifier = Modifier
                    .size(32.dp)
                    .testTag("remove_rubric_${rubric.id}")
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Remove Rubric",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun RemedyScoreRow(score: RemedyScore, onClick: () -> Unit) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .testTag("remedy_row_${score.remedy.abbreviation}")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = score.remedy.abbreviation,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = EmeraldPrimary
                    )
                    Text(
                        text = score.remedy.fullName,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = "${score.remedy.thermalState.name} • ${score.remedy.dominantMiasm.name} • ${score.remedy.commonName}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Surface(
                    color = EmeraldContainer,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "${score.totalScore} pts",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = EmeraldOnContainer,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "${score.rubricsCovered} / ${score.totalRubrics} rubrics",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

/**
 * Candidate remedy matrix (docs/design/responsive.md §2.2): active rubrics
 * listed vertically, the top candidate remedy abbreviations fixed in the
 * header row, grade cells (1/2/3) and total scores per remedy.
 */
@Composable
private fun CandidateMatrixCard(
    rubrics: List<Rubric>,
    scores: List<RemedyScore>,
    isExpanded: Boolean
) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(12.dp),
        shadowElevation = 1.dp,
        modifier = Modifier
            .fillMaxWidth()
            .testTag("candidate_matrix")
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = "CANDIDATE MATRIX",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))
            if (isExpanded) {
                CandidateMatrixGrid(rubrics = rubrics, scores = scores, isExpanded = true)
            } else {
                Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
                    CandidateMatrixGrid(rubrics = rubrics, scores = scores, isExpanded = false)
                }
            }
        }
    }
}

@Composable
private fun CandidateMatrixGrid(
    rubrics: List<Rubric>,
    scores: List<RemedyScore>,
    isExpanded: Boolean
) {
    val columnModifier = if (isExpanded) {
        Modifier.weight(1f)
    } else {
        Modifier.width(64.dp)
    }
    val labelWidth = if (isExpanded) 160.dp else 120.dp

    // Fixed remedy-abbreviation header (responsive.md §2.2)
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "RUBRIC",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.width(labelWidth)
        )
        scores.forEach { score ->
            Column(
                modifier = columnModifier,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = score.remedy.abbreviation,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.ExtraBold,
                    color = EmeraldPrimary
                )
                Text(
                    text = score.remedy.fullName,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }

    Spacer(modifier = Modifier.height(6.dp))

    // Rubric rows with per-remedy grade cells
    rubrics.take(8).forEach { rubric ->
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${rubric.chapter}: ${rubric.name}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.width(labelWidth),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            scores.forEach { score ->
                val grade = rubric.remedyGrades[score.remedy.abbreviation]
                val (cellColor, cellText) = when (grade) {
                    3 -> EmeraldContainer to "3"
                    2 -> IndigoContainer to "2"
                    1 -> AmberContainer to "1"
                    else -> MaterialTheme.colorScheme.surfaceVariant to "—"
                }
                Surface(
                    color = cellColor,
                    shape = RoundedCornerShape(6.dp),
                    modifier = columnModifier
                ) {
                    Text(
                        text = cellText,
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
    }

    // Total score row
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "TOTAL",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = EmeraldPrimary,
            modifier = Modifier.width(labelWidth)
        )
        scores.forEach { score ->
            Surface(
                color = EmeraldContainer,
                shape = RoundedCornerShape(6.dp),
                modifier = columnModifier
            ) {
                Text(
                    text = "${score.totalScore}",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = EmeraldOnContainer,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                )
            }
        }
    }
}

package com.example.similimumai.ui.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Insights
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.similimumai.data.engine.HomeopathyKnowledgeEngine
import com.example.similimumai.data.model.VisualFinding
import com.example.similimumai.data.model.VisionPanel
import com.example.similimumai.ui.theme.*
import com.example.similimumai.R
import com.example.similimumai.ui.viewmodel.ConsultationUiState
import com.example.similimumai.ui.viewmodel.ConsultationViewModel

/**
 * Workspace 6: Diagnostic visual inspection lab
 * (docs/engineering/project-structure.md — VisionLabScreen).
 *
 * Silent tongue, skin and general physical sign inspection. Each finding,
 * when recorded, injects a structured symptom into the consultation and
 * activates its canonical repertory rubric so rankings update live.
 */
@Composable
fun VisionLabScreen(
    uiState: ConsultationUiState,
    viewModel: ConsultationViewModel,
    modifier: Modifier = Modifier
) {
    val findings = HomeopathyKnowledgeEngine.visualFindings

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .testTag("vision_lab_screen"),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(4.dp))
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Insights,
                        contentDescription = null,
                        tint = VioletPqrs,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = stringResource(R.string.vision_lab_title),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = VioletPqrs
                        )
                        Text(
                            text = stringResource(R.string.vision_lab_subtitle),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        VisionPanel.values().forEach { panel ->
            val panelFindings = findings.filter { it.panel == panel }
            item(key = "panel_${panel.name}") {
                PanelSectionHeader(panel)
            }
            items(panelFindings, key = { it.id }) { finding ->
                VisualFindingCard(
                    finding = finding,
                    isRecorded = isFindingRecorded(uiState, finding),
                    onToggle = { viewModel.recordVisualFinding(finding) },
                    remedies = HomeopathyKnowledgeEngine.remediesForFinding(finding)
                        .map { it.abbreviation }
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

private fun isFindingRecorded(uiState: ConsultationUiState, finding: VisualFinding): Boolean =
    uiState.symptoms.any { it.rawUtterance == "Vision Lab finding: ${finding.label}" }

@Composable
private fun PanelSectionHeader(panel: VisionPanel) {
    Surface(
        color = VioletContainer,
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = panel.label.uppercase(),
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = VioletOnContainer,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
        )
    }
}

@Composable
private fun VisualFindingCard(
    finding: VisualFinding,
    isRecorded: Boolean,
    onToggle: () -> Unit,
    remedies: List<String>
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = if (isRecorded) EmeraldContainer else MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = if (isRecorded) 2.dp else 1.dp,
                color = if (isRecorded) EmeraldPrimary else Color.Transparent,
                shape = RoundedCornerShape(10.dp)
            )
            .clickable(onClick = onToggle)
            .testTag("vision_finding_${finding.id}")
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = finding.label,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = if (isRecorded) FontWeight.SemiBold else FontWeight.Normal,
                    color = if (isRecorded) EmeraldOnContainer else MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                FindingChipRow(remedies)
            }
            if (isRecorded) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Recorded",
                    tint = EmeraldPrimary,
                    modifier = Modifier.size(22.dp)
                )
            }
        }
    }
}

@Composable
private fun FindingChipRow(remedies: List<String>) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        remedies.forEach { code ->
            Surface(
                color = IndigoContainer,
                shape = RoundedCornerShape(4.dp)
            ) {
                Text(
                    text = code,
                    style = MaterialTheme.typography.labelSmall,
                    color = IndigoOnContainer,
                    modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.dp)
                )
            }
        }
    }
}

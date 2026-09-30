package com.example.similimumai.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.similimumai.data.model.Remedy
import com.example.similimumai.data.model.RemedyScore
import com.example.similimumai.ui.theme.*

@Composable
fun MiniRemedyLeaderboard(
    scores: List<RemedyScore>,
    onRemedyClick: (Remedy) -> Unit,
    modifier: Modifier = Modifier
) {
    if (scores.isEmpty()) return

    val topScores = scores.take(3)

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .testTag("mini_remedy_leaderboard"),
        color = EmeraldContainer.copy(alpha = 0.6f),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Similimum",
                    tint = EmeraldPrimary,
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = "TOP SIMILIMUM:",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = EmeraldOnContainer
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                topScores.forEachIndexed { index, score ->
                    val rankColor = when (index) {
                        0 -> EmeraldPrimary
                        1 -> IndigoGenerals
                        else -> AmberModalities
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.surface)
                            .clickable { onRemedyClick(score.remedy) }
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                            .testTag("remedy_chip_${score.remedy.abbreviation}")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = "#${index + 1} ${score.remedy.abbreviation}",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.ExtraBold,
                                color = rankColor
                            )
                            Text(
                                text = "${score.totalScore} pts",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}

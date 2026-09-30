package com.example.similimumai.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.similimumai.data.model.ThermalState
import com.example.similimumai.ui.theme.*
import com.example.similimumai.ui.viewmodel.ConnectionMode
import com.example.similimumai.ui.viewmodel.ConnectionStatus

@Composable
fun TopClinicalStatusBar(
    patientName: String,
    patientAge: Int,
    patientSex: String,
    thermalState: ThermalState,
    isMicActive: Boolean,
    isSimulating: Boolean,
    isGeminiAvailable: Boolean,
    isOnline: Boolean = true,
    modifier: Modifier = Modifier
) {
    val connectionMode = ConnectionStatus.mode(isOnline, isGeminiAvailable)
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .testTag("clinical_status_bar"),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 2.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Patient Summary
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(18.dp))
                            .background(EmeraldContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Patient",
                            tint = EmeraldPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Column {
                        Text(
                            text = patientName,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "$patientAge yrs • $patientSex",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // Status Badges
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Thermal State Chip
                    val thermalColor = when (thermalState) {
                        ThermalState.HOT -> CrimsonRedFlag
                        ThermalState.CHILLY -> StatusInfo
                        ThermalState.AMBITHERMAL -> StatusWarning
                    }
                    Surface(
                        color = thermalColor.copy(alpha = 0.12f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = "Thermal State",
                                tint = thermalColor,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = thermalState.name,
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = thermalColor
                            )
                        }
                    }

                    // Mic / Sim Active Badge
                    if (isMicActive) {
                        Surface(
                            color = CrimsonRedFlag.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PlayArrow,
                                    contentDescription = "Mic Live",
                                    tint = CrimsonRedFlag,
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(
                                    text = "LIVE",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = CrimsonRedFlag
                                )
                            }
                        }
                    } else if (isSimulating) {
                        Surface(
                            color = IndigoContainer,
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PlayArrow,
                                    contentDescription = "Simulating",
                                    tint = IndigoGenerals,
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(
                                    text = "STREAMING",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = IndigoGenerals
                                )
                            }
                        }
                    }

                    // Connectivity + AI Badge (docs/ai/offline-strategy.md §2.1):
                    // green "AI Connected" flips to amber "Offline Mode (Local
                    // Knowledge Base Active)" the instant the network drops.
                    Surface(
                        color = when (connectionMode) {
                            ConnectionMode.OFFLINE -> AmberContainer
                            else -> EmeraldContainer
                        },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.testTag("connection_status_pill")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = when (connectionMode) {
                                    ConnectionMode.OFFLINE -> Icons.Default.CloudOff
                                    ConnectionMode.ONLINE_CLOUD -> Icons.Default.Cloud
                                    ConnectionMode.ONLINE_LOCAL -> Icons.Default.Cloud
                                },
                                contentDescription = "Connection status",
                                tint = when (connectionMode) {
                                    ConnectionMode.OFFLINE -> AmberOnContainer
                                    else -> EmeraldOnContainer
                                },
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = ConnectionStatus.pillLabel(isOnline, isGeminiAvailable),
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = when (connectionMode) {
                                    ConnectionMode.OFFLINE -> AmberOnContainer
                                    else -> EmeraldOnContainer
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

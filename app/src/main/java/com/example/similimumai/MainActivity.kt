package com.example.similimumai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.similimumai.ui.components.MiniRemedyLeaderboard
import com.example.similimumai.ui.components.RedFlagBanner
import com.example.similimumai.ui.components.TopClinicalStatusBar
import com.example.similimumai.ui.screens.*
import com.example.similimumai.ui.theme.EmeraldPrimary
import com.example.similimumai.ui.theme.SimilimumAITheme
import com.example.similimumai.ui.viewmodel.ConsultationViewModel
import com.example.similimumai.ui.viewmodel.NavigationTab

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            SimilimumAITheme {
                val viewModel: ConsultationViewModel = viewModel()
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                val context = LocalContext.current

                // Permission launcher for ambient audio capture
                val recordAudioPermissionLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestPermission()
                ) { isGranted ->
                    if (isGranted) {
                        viewModel.toggleMicrophone(true)
                    }
                }

                // Handle system back navigation
                if (uiState.currentTab != NavigationTab.HUD) {
                    BackHandler {
                        viewModel.selectNavigationTab(NavigationTab.HUD)
                    }
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        Column(modifier = Modifier.statusBarsPadding()) {
                            TopClinicalStatusBar(
                                patientName = uiState.patientName,
                                patientAge = uiState.patientAge,
                                patientSex = uiState.patientSex,
                                thermalState = uiState.patientThermal,
                                isMicActive = uiState.isMicListening,
                                isSimulating = uiState.isSimulationRunning,
                                isGeminiAvailable = uiState.isGeminiAvailable,
                                isOnline = uiState.isOnline
                            )

                            // Red-flag triage banner if emergency detected
                            uiState.activeRedFlag?.let { redFlag ->
                                RedFlagBanner(
                                    alert = redFlag,
                                    onDismiss = { viewModel.dismissRedFlag() }
                                )
                            }

                            // Sticky Top-3 candidate remedy tracker
                            MiniRemedyLeaderboard(
                                scores = uiState.remedyScores,
                                onRemedyClick = { remedy ->
                                    viewModel.selectRemedyDetail(remedy)
                                    viewModel.selectNavigationTab(NavigationTab.MATERIA_MEDICA)
                                }
                            )
                        }
                    },
                    bottomBar = {
                        NavigationBar(
                            modifier = Modifier.testTag("bottom_nav_bar"),
                            tonalElevation = 4.dp
                        ) {
                            NavigationTab.values().forEach { tab ->
                                val (icon, tag) = when (tab) {
                                    NavigationTab.HUD -> Icons.Default.PlayArrow to "nav_hud"
                                    NavigationTab.LSMC_RADAR -> Icons.Default.Search to "nav_lsmc"
                                    NavigationTab.REPERTORY -> Icons.Default.Star to "nav_repertory"
                                    NavigationTab.MATERIA_MEDICA -> Icons.Default.Favorite to "nav_materia"
                                    NavigationTab.RX_HERING -> Icons.Default.Check to "nav_rx"
                                    NavigationTab.VISION_LAB -> Icons.Default.Insights to "nav_vision_lab"
                                }

                                NavigationBarItem(
                                    selected = uiState.currentTab == tab,
                                    onClick = { viewModel.selectNavigationTab(tab) },
                                    icon = {
                                        Icon(
                                            imageVector = icon,
                                            contentDescription = tab.label
                                        )
                                    },
                                    label = { Text(tab.label) },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = EmeraldPrimary,
                                        selectedTextColor = EmeraldPrimary,
                                        indicatorColor = MaterialTheme.colorScheme.primaryContainer
                                    ),
                                    modifier = Modifier.testTag(tag)
                                )
                            }
                        }
                    }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        when (uiState.currentTab) {
                            NavigationTab.HUD -> HudScreen(
                                uiState = uiState,
                                viewModel = viewModel
                            )
                            NavigationTab.LSMC_RADAR -> LsmcRadarScreen(
                                uiState = uiState,
                                viewModel = viewModel
                            )
                            NavigationTab.REPERTORY -> RepertoryScreen(
                                uiState = uiState,
                                viewModel = viewModel
                            )
                            NavigationTab.MATERIA_MEDICA -> MateriaMedicaScreen(
                                uiState = uiState,
                                viewModel = viewModel
                            )
                            NavigationTab.RX_HERING -> RxHeringScreen(
                                uiState = uiState,
                                viewModel = viewModel
                            )
                            NavigationTab.VISION_LAB -> VisionLabScreen(
                                uiState = uiState,
                                viewModel = viewModel
                            )
                        }
                    }
                }
            }
        }
    }
}

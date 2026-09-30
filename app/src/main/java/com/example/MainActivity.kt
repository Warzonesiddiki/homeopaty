package com.example

import android.Manifest
import android.content.pm.PackageManager
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.MiniRemedyLeaderboard
import com.example.ui.components.PatientRecordsDialog
import com.example.ui.components.RedFlagBanner
import com.example.ui.components.TopClinicalStatusBar
import com.example.ui.screens.*
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.SimilimumAITheme
import com.example.viewmodel.ConsultationViewModel

enum class NavWorkspace(val label: String, val icon: ImageVector, val tag: String) {
    CO_PILOT_HUD("HUD", Icons.Default.Hearing, "nav_copilot_hud"),
    LSMC_RADAR("LSMC", Icons.Default.Radar, "nav_lsmc_radar"),
    VISION_LAB("Vision/Lab", Icons.Default.CameraAlt, "nav_vision_lab"),
    REPERTORY("Repertory", Icons.Default.GridOn, "nav_repertory"),
    DIFFERENTIAL("Diff", Icons.Default.Compare, "nav_differential"),
    RX_HERING("Rx/Hering", Icons.Default.Medication, "nav_rx_hering")
}

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val viewModel: ConsultationViewModel = viewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            val context = LocalContext.current

            var currentWorkspace by remember { mutableStateOf(NavWorkspace.CO_PILOT_HUD) }

            // Handle back button on secondary tabs
            BackHandler(enabled = currentWorkspace != NavWorkspace.CO_PILOT_HUD) {
                currentWorkspace = NavWorkspace.CO_PILOT_HUD
            }

            // Audio permission request
            val audioPermissionLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.RequestPermission()
            ) { isGranted ->
                if (isGranted) {
                    viewModel.toggleRecording()
                }
            }

            fun handleMicToggle() {
                val hasPermission = ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.RECORD_AUDIO
                ) == PackageManager.PERMISSION_GRANTED

                if (hasPermission) {
                    viewModel.toggleRecording()
                } else {
                    audioPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                }
            }

            SimilimumAITheme(darkTheme = uiState.isDarkMode) {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        Column {
                            TopClinicalStatusBar(
                                uiState = uiState,
                                onCaseSelected = { viewModel.loadSimulatedCase(it) },
                                onModeChange = { viewModel.setCaseMode(it) },
                                onToggleConsent = { viewModel.setConsentObtained(it) },
                                onToggleDark = { viewModel.toggleDarkMode() },
                                onOpenRecords = { viewModel.setPatientRecordsDialogVisible(true) }
                            )
                            RedFlagBanner(
                                alert = uiState.activeRedFlag,
                                onDismiss = { viewModel.dismissRedFlag() }
                            )
                        }
                    },
                    bottomBar = {
                        Column {
                            MiniRemedyLeaderboard(
                                topRemedies = uiState.repertorizationResults,
                                onNavigateToRepertory = { currentWorkspace = NavWorkspace.REPERTORY }
                            )

                            NavigationBar(
                                tonalElevation = 8.dp,
                                modifier = Modifier.height(64.dp)
                            ) {
                                NavWorkspace.values().forEach { ws ->
                                    val isSelected = currentWorkspace == ws
                                    NavigationBarItem(
                                        selected = isSelected,
                                        onClick = { currentWorkspace = ws },
                                        icon = {
                                            Icon(
                                                imageVector = ws.icon,
                                                contentDescription = ws.label,
                                                modifier = Modifier.size(20.dp)
                                            )
                                        },
                                        label = {
                                            Text(
                                                text = ws.label,
                                                fontSize = 9.sp,
                                                maxLines = 1
                                            )
                                        },
                                        colors = NavigationBarItemDefaults.colors(
                                            selectedIconColor = EmeraldPrimary,
                                            selectedTextColor = EmeraldPrimary,
                                            indicatorColor = EmeraldPrimary.copy(alpha = 0.15f)
                                        ),
                                        modifier = Modifier.testTag(ws.tag)
                                    )
                                }
                            }
                        }
                    }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        when (currentWorkspace) {
                            NavWorkspace.CO_PILOT_HUD -> {
                                CoPilotHudScreen(
                                    uiState = uiState,
                                    onToggleRecording = { handleMicToggle() },
                                    onToggleSimulatedStream = { viewModel.toggleSimulatedStream() },
                                    onStepSimulatedTurn = { viewModel.stepNextSimulatedTurn() },
                                    onManualUtterance = { speaker, text -> viewModel.addManualUtterance(speaker, text) },
                                    onQuestionAnswered = { qId, isPos -> viewModel.onQuestionAnswered(qId, isPos) },
                                    onTogglePinQuestion = { qId -> viewModel.togglePinQuestion(qId) },
                                    onSilentObservation = { obs -> viewModel.injectSilentObservation(obs) },
                                    onSelectSpecialty = { spec -> viewModel.setSpecialtyTree(spec) },
                                    onInjectVocalBiomarker = { vb -> viewModel.injectVocalBiomarker(vb) }
                                )
                            }
                            NavWorkspace.LSMC_RADAR -> {
                                LsmcRadarScreen(
                                    uiState = uiState,
                                    onExplorePillar = { pId ->
                                        viewModel.explorePillar(pId)
                                        currentWorkspace = NavWorkspace.CO_PILOT_HUD
                                    }
                                )
                            }
                            NavWorkspace.VISION_LAB -> {
                                VisionLabScreen(
                                    uiState = uiState,
                                    onAddRubricByPath = { path -> viewModel.addRubricByPath(path) },
                                    onQueueLabQuestion = { test, q ->
                                        viewModel.queueLabQuestion(test, q)
                                        currentWorkspace = NavWorkspace.CO_PILOT_HUD
                                    }
                                )
                            }
                            NavWorkspace.REPERTORY -> {
                                RepertoryMatrixScreen(
                                    uiState = uiState,
                                    onAddRubric = { rubric -> viewModel.addRubric(rubric) },
                                    onRemoveRubric = { rId -> viewModel.removeRubric(rId) },
                                    onSetWeight = { rId, wt -> viewModel.setRubricWeight(rId, wt) },
                                    onToggleEliminating = { rId -> viewModel.toggleEliminatingRubric(rId) },
                                    onSelectSchool = { school -> viewModel.setRepertorySchool(school) }
                                )
                            }
                            NavWorkspace.DIFFERENTIAL -> {
                                MateriaMedicaDiffScreen(
                                    uiState = uiState,
                                    onToggleKeynote = { knId -> viewModel.toggleKeynoteVerification(knId) }
                                )
                            }
                            NavWorkspace.RX_HERING -> {
                                RxHeringsScreen(
                                    uiState = uiState,
                                    onUpdatePrescription = { name, code, pot, pos, veh, notes ->
                                        viewModel.updatePrescription(name, code, pot, pos, veh, notes)
                                    },
                                    onUpdateHerings = { b1, b2, b3, b4, b5, b6 ->
                                        viewModel.updateHeringsAssessment(b1, b2, b3, b4, b5, b6)
                                    },
                                    onSetGeminiKey = { key -> viewModel.setGeminiApiKey(key) },
                                    onToggleHybrid = { enabled -> viewModel.toggleHybridMode(enabled) },
                                    onDismissInimical = { viewModel.dismissInimicalWarning() },
                                    onSaveConsultation = { viewModel.saveCurrentConsultation() },
                                    onTriggerGemini = { viewModel.runGeminiSynthesisNow() }
                                )
                            }
                        }
                    }
                }

                // Patient Records Archive Modal Dialog
                if (uiState.showPatientRecordsDialog) {
                    PatientRecordsDialog(
                        savedPatients = uiState.savedPatients,
                        onDismiss = { viewModel.setPatientRecordsDialogVisible(false) },
                        onSelectPatient = { patient -> viewModel.loadSavedPatient(patient) },
                        onDeletePatient = { id -> viewModel.deleteSavedPatient(id) },
                        onCreateNewPatient = { name, age, gender, complaint ->
                            viewModel.createNewWalkInPatient(name, age, gender, complaint)
                        }
                    )
                }
            }
        }
    }
}

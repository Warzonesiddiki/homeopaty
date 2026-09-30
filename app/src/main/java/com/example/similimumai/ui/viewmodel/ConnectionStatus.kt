package com.example.similimumai.ui.viewmodel

/**
 * Connectivity status for the clinical status bar
 * (docs/ai/offline-strategy.md §2: "the green AI Connected pill switches
 * instantly to an amber Offline Mode indicator" when the network drops).
 *
 * Pure Kotlin so the mapping is unit-testable on the JVM; the Compose layer
 * only maps [ConnectionMode] to palette colors.
 */
enum class ConnectionMode {
    ONLINE_CLOUD,   // network up + Gemini cloud synthesis available
    ONLINE_LOCAL,   // network up but no Gemini key — local KB only
    OFFLINE         // network down — local knowledge base fully active
}

object ConnectionStatus {

    fun mode(isOnline: Boolean, geminiAvailable: Boolean): ConnectionMode = when {
        !isOnline -> ConnectionMode.OFFLINE
        geminiAvailable -> ConnectionMode.ONLINE_CLOUD
        else -> ConnectionMode.ONLINE_LOCAL
    }

    fun pillLabel(isOnline: Boolean, geminiAvailable: Boolean): String = when (mode(isOnline, geminiAvailable)) {
        ConnectionMode.ONLINE_CLOUD -> "AI CONNECTED"
        ConnectionMode.ONLINE_LOCAL -> "ONLINE • LOCAL KB"
        ConnectionMode.OFFLINE -> "OFFLINE MODE • LOCAL KB ACTIVE"
    }
}

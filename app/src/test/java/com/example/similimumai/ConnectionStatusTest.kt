package com.example.similimumai

import com.example.similimumai.ui.viewmodel.ConnectionMode
import com.example.similimumai.ui.viewmodel.ConnectionStatus
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Tier 1: pure JVM tests for the connectivity pill mapping
 * (docs/ai/offline-strategy.md §2.1).
 */
class ConnectionStatusTest {

    @Test
    fun `online with gemini key is the cloud connected mode`() {
        assertEquals(ConnectionMode.ONLINE_CLOUD, ConnectionStatus.mode(isOnline = true, geminiAvailable = true))
        assertEquals("AI CONNECTED", ConnectionStatus.pillLabel(true, true))
    }

    @Test
    fun `online without gemini key is online local-kb mode`() {
        assertEquals(ConnectionMode.ONLINE_LOCAL, ConnectionStatus.mode(isOnline = true, geminiAvailable = false))
        assertEquals("ONLINE • LOCAL KB", ConnectionStatus.pillLabel(true, false))
    }

    @Test
    fun `offline overrides gemini availability`() {
        assertEquals(ConnectionMode.OFFLINE, ConnectionStatus.mode(isOnline = false, geminiAvailable = true))
        assertEquals(ConnectionMode.OFFLINE, ConnectionStatus.mode(isOnline = false, geminiAvailable = false))
        assertEquals("OFFLINE MODE • LOCAL KB ACTIVE", ConnectionStatus.pillLabel(false, true))
        assertEquals("OFFLINE MODE • LOCAL KB ACTIVE", ConnectionStatus.pillLabel(false, false))
    }
}

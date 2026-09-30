package com.example.similimumai

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.example.similimumai.ui.theme/Typography
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * Tier 2: multi-device canonical layouts + design tokens
 * (docs/design/responsive.md §1, docs/design/design-system.md §3).
 *
 * Robolectric's default qualifiers (320x470dp) place the activity in the
 * Compact size class; the expanded-width tests opt into a 1000dp-wide
 * tablet-like configuration.
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [35])
class ResponsiveLayoutTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    /**
     * COMPACT (phones < 600dp): vertical single-column stack with a bottom
     * Navigation Bar (responsive.md §1).
     */
    @Test
    fun `compact form factor uses the bottom navigation bar`() {
        composeRule.waitForIdle()

        composeRule.onNodeWithTag("bottom_nav_bar").assertExists()
        composeRule.onNodeWithTag("navigation_rail").assertDoesNotExist()
        composeRule.onNodeWithTag("hud_screen").assertExists()
    }

    /**
     * EXPANDED (tablets >= 600dp): left Navigation Rail replaces the bottom
     * bar for ergonomic left-edge reach on desk-stand tablets (responsive.md §1).
     */
    @Config(qualifiers = "w1000dp-h700dp-240dpi")
    @Test
    fun `expanded form factor uses the left navigation rail`() {
        composeRule.waitForIdle()

        composeRule.onNodeWithTag("navigation_rail").assertExists()
        composeRule.onNodeWithTag("bottom_nav_bar").assertDoesNotExist()
        composeRule.onNodeWithTag("hud_screen").assertExists()

        // all six workspaces reachable from the rail
        composeRule.onNodeWithTag("nav_hud").assertExists()
        composeRule.onNodeWithTag("nav_lsmc").assertExists()
        composeRule.onNodeWithTag("nav_repertory").assertExists()
        composeRule.onNodeWithTag("nav_materia").assertExists()
        composeRule.onNodeWithTag("nav_rx").assertExists()
        composeRule.onNodeWithTag("nav_vision_lab").assertExists()
    }

    /**
     * Type scale tokens per design-system.md §3: compact data density with
     * high-visibility headlines for peripheral reading.
     */
    @Test
    fun `type scale matches the design-system tokens`() {
        // displaySmall — Top Similimum Remedy Name
        assertEquals(24f, Typography.displaySmall.fontSize.value)
        assertEquals(32f, Typography.displaySmall.lineHeight.value)

        // headlineMedium — Workspace Screen Titles, Red Flag Header
        assertEquals(20f, Typography.headlineMedium.fontSize.value)
        assertEquals(26f, Typography.headlineMedium.lineHeight.value)

        // titleMedium — High-Yield Question Deck Text
        assertEquals(16f, Typography.titleMedium.fontSize.value)
        assertEquals(22f, Typography.titleMedium.lineHeight.value)

        // titleSmall — Rubric Chapter, Remedy Latin Code
        assertEquals(14f, Typography.titleSmall.fontSize.value)
        assertEquals(20f, Typography.titleSmall.lineHeight.value)

        // bodyMedium — Patient Dialogue Transcript, Case Notes
        assertEquals(14f, Typography.bodyMedium.fontSize.value)
        assertEquals(20f, Typography.bodyMedium.lineHeight.value)

        // bodySmall — Modalities, Concomitants, Clinical Rationales
        assertEquals(12f, Typography.bodySmall.fontSize.value)
        assertEquals(16f, Typography.bodySmall.lineHeight.value)

        // labelSmall — Rubric Grade Badges (1, 2, 3), Category Tags
        assertEquals(10f, Typography.labelSmall.fontSize.value)
        assertEquals(14f, Typography.labelSmall.lineHeight.value)
    }
}

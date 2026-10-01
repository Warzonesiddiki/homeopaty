package com.example.similimumai

import android.content.Context
import android.content.res.Configuration
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.util.Locale

/**
 * Tier 2: locale resource resolution (docs/design/i18n-plan.md §2).
 * Verifies values-hi (Devanagari) and values-b+hi+Latn (Hinglish Latin)
 * resolve through the Android resource system.
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [35])
class LocaleResourcesTest {

    private fun contextFor(locale: Locale): Context {
        val context = ApplicationProvider.getApplicationContext<android.app.Application>()
        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)
        return context.createConfigurationContext(config)
    }

    private fun hinglishLocale(): Locale =
        Locale.Builder().setLanguage("hi").setScript("Latn").build()

    // keys whose default (English) value is known
    private val translatedKeys: Map<Int, String> = mapOf(
        R.string.hud_dialogue_stream_title to "CONSULTATION DIALOGUE STREAM",
        R.string.common_close to "Close",
        R.string.rx_potency_label to "Potency",
        R.string.vision_lab_title to "DIAGNOSTIC VISION LAB",
        R.string.hud_simulate_case_button to "SIMULATE CASE",
        R.string.case_sheet_copy_button to "GENERATE & COPY CASE SHEET",
        R.string.patient_search_title to "PATIENT RECORDS"
    )

    @Test
    fun `hindi locale resolves devanagari translations`() {
        val ctx = contextFor(Locale("hi", "IN"))

        val appName = ctx.resources.getString(R.string.app_name)
        assertTrue(
            "hi app_name should be Devanagari, got '$appName'",
            appName.any { it in '\u0900'..'\u097F' }
        )

        for ((id, english) in translatedKeys) {
            val hi = ctx.resources.getString(id)
            assertNotEquals("hi must translate key $id", english, hi)
            assertTrue(
                "hi value '$hi' should use Devanagari",
                hi.any { it in '\u0900'..'\u097F' }
            )
        }
    }

    @Test
    fun `hinglish latin locale resolves without devanagari`() {
        val ctx = contextFor(hinglishLocale())

        for ((id, english) in translatedKeys) {
            val latn = ctx.resources.getString(id)
            assertFalse(
                "b+hi+Latn value '$latn' must stay Latin script",
                latn.any { it in '\u0900'..'\u097F' }
            )
            assertNotEquals("b+hi+Latn must translate key $id", english, latn)
        }

        // brand name intentionally stays latin in the Hinglish locale
        assertEquals("Similimum AI", ctx.resources.getString(R.string.app_name))
    }

    @Test
    fun `default locale still serves english copy`() {
        val ctx = ApplicationProvider.getApplicationContext<android.app.Application>()
        for ((id, english) in translatedKeys) {
            assertEquals(english, ctx.resources.getString(id))
        }
    }
}

package com.example.similimumai

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import com.example.similimumai.data.local.ConsultationDatabase
import com.example.similimumai.data.local.SecureDbKey
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * MVP "encrypted on-device storage" (docs/product/mvp-scope.md): the Room
 * database is SQLCipher-encrypted and the passphrase is a stable 256-bit
 * value persisted through the Android Keystore.
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [35])
class EncryptedStorageTest {

    @Test
    fun `database file on disk is not plaintext SQLite`() {
        val context = ApplicationProvider.getApplicationContext<Application>()
        ConsultationDatabase.getDatabase(context)

        val file = context.getDatabasePath("similimum_ai_db")
        assertTrue("DB file must exist after first open", file.exists() && file.length() > 0)

        val header = String(file.readBytes().take(16).toByteArray(), Charsets.ISO_8859_1)
        assertFalse(
            "DB file must not start with the plaintext 'SQLite format 3' header",
            header.startsWith("SQLite format")
        )
    }

    @Test
    fun `db passphrase is stable across calls and 256-bit hex`() {
        val context = ApplicationProvider.getApplicationContext<Application>()
        val first = SecureDbKey.getOrCreatePassphrase(context)
        val second = SecureDbKey.getOrCreatePassphrase(context)

        assertEquals("passphrase must be stable across app launches", first, second)
        assertEquals(64, first.length) // 32 bytes hex-encoded = 256-bit
        assertTrue(first.all { it in '0'..'9' || it in 'a'..'f' })
    }
}

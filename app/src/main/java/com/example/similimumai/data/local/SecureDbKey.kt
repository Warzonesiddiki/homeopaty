package com.example.similimumai.data.local

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import java.security.SecureRandom

/**
 * Passphrase management for the SQLCipher-encrypted Room database
 * (docs/product/mvp-scope.md: "encrypted on-device storage").
 *
 * A random 32-byte (256-bit) passphrase is generated on first launch and
 * persisted in [EncryptedSharedPreferences] — keys wrapped by the Android
 * Hardware Keystore (AES-256-GCM master key), values encrypted with
 * AES-256-GCM, keys sealed with AES-256-SIV.
 */
object SecureDbKey {

    private const val PREFS_FILE = "similimum_secure_db_prefs"
    private const val DB_PASSPHRASE_KEY = "db_passphrase"

    /** Returns the stable DB passphrase, creating it on first launch. */
    fun getOrCreatePassphrase(context: Context): String {
        val prefs = securePrefs(context.applicationContext)
        return prefs.getString(DB_PASSPHRASE_KEY, null) ?: createAndStore(prefs)
    }

    private fun securePrefs(context: Context): SharedPreferences =
        EncryptedSharedPreferences.create(
            context,
            PREFS_FILE,
            MasterKey.Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build(),
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

    private fun createAndStore(prefs: SharedPreferences): String {
        val bytes = ByteArray(32)
        SecureRandom().nextBytes(bytes)
        val passphrase = bytes.joinToString("") { "%02x".format(it) }
        prefs.edit().putString(DB_PASSPHRASE_KEY, passphrase).apply()
        return passphrase
    }
}

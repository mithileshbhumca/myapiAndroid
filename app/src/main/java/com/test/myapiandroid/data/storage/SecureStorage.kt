package com.test.myapiandroid.data.storage

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class SecureStorage(context: Context) {
    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val prefs = EncryptedSharedPreferences.create(
        context,
        "secure_prefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun saveTokens(accessToken: String, refreshToken: String, expirySeconds: Long) {
        val expiryTime = System.currentTimeMillis() + expirySeconds * 1000
        prefs.edit()
            .putString("access_token", accessToken)
            .putString("refresh_token", refreshToken)
            .putLong("refresh_expiry", expiryTime)
            .putLong("refresh_seconds_fallback", expirySeconds)
            .apply()
    }

    fun getAccessToken(): String? = prefs.getString("access_token", null)
    fun getRefreshToken(): String? = prefs.getString("refresh_token", null)
    fun getRefreshExpiry(): Long = prefs.getLong("refresh_expiry", 0)
    fun getRefreshSecondsFallback(): Long = prefs.getLong("refresh_seconds_fallback", 7L * 24 * 60 * 60)

    fun clearTokens() { prefs.edit().clear().apply() }
}

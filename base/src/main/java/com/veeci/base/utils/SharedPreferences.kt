package com.veeci.base.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys

object SharedPreferencesUtil {
    private const val ACCOUNT_PREFERENCES_FILE = "account_preference_file"

    private fun getEncryptedSharedPreferences(context: Context): SharedPreferences {
        val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
        return EncryptedSharedPreferences.create(
            ACCOUNT_PREFERENCES_FILE,
            masterKeyAlias,
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM,
        )
    }

    fun getString(
        context: Context,
        key: String,
        defaultValue: String? = null,
    ): String? {
        return getEncryptedSharedPreferences(context).getString(key, defaultValue)
    }

    fun putString(
        context: Context,
        key: String,
        value: String,
    ) {
        getEncryptedSharedPreferences(context).edit().putString(key, value).apply()
    }

    fun getInt(
        context: Context,
        key: String,
        defaultValue: Int = 0,
    ): Int {
        return getEncryptedSharedPreferences(context).getInt(key, defaultValue)
    }

    fun putInt(
        context: Context,
        key: String,
        value: Int,
    ) {
        getEncryptedSharedPreferences(context).edit().putInt(key, value).apply()
    }

    fun getBoolean(
        context: Context,
        key: String,
        defaultValue: Boolean = false,
    ): Boolean {
        return getEncryptedSharedPreferences(context).getBoolean(key, defaultValue)
    }

    fun putBoolean(
        context: Context,
        key: String,
        value: Boolean,
    ) {
        getEncryptedSharedPreferences(context).edit().putBoolean(key, value).apply()
    }

    fun clear(
        context: Context,
        key: String,
    ) {
        getEncryptedSharedPreferences(context).edit().remove(key).apply()
    }
}

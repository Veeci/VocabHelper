package com.example.base

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale

open class BaseActivity : AppCompatActivity() {

    override fun attachBaseContext(newBase: Context?) {
        val newLocale = Locale(getSavedLanguage(newBase))
        val context = updateLocale(newBase ?: return, newLocale)
        super.attachBaseContext(context)
    }

    private fun updateLocale(context: Context, locale: Locale): Context {
        Locale.setDefault(locale)
        val resources: Resources = context.resources
        val config: Configuration = resources.configuration
        config.setLocale(locale)
        return context.createConfigurationContext(config)
    }

    private fun getSavedLanguage(context: Context?): String {
        val sharedPreferences = context?.getSharedPreferences("AppSettings", Context.MODE_PRIVATE)
        val savedLanguage = sharedPreferences?.getString("AppLanguage", Locale.getDefault().language) ?: Locale.getDefault().language
        Log.d("BaseActivity", "Saved Language: $savedLanguage")
        return savedLanguage
    }


    fun changeLanguage(languageCode: String) {
        val sharedPreferences = getSharedPreferences("AppSettings", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putString("AppLanguage", languageCode)
            commit()
        }
        recreate()
    }
}

package com.example.vocabhelper.domain

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import androidx.lifecycle.ViewModel
import com.example.vocabhelper.presentation.auth.AuthActivity
import com.example.vocabhelper.presentation.main.MainActivity
import java.util.Locale

class SettingViewModel: ViewModel() {

    fun changeAppLanguage(activity: Activity, languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val resources: Resources = activity.resources
        val config: Configuration = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)

        val refreshIntent = Intent(activity, AuthActivity::class.java)
        refreshIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        activity.recreate()
        activity.startActivity(refreshIntent)
    }
}
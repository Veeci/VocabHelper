package com.example.vocabhelper.presentation.auth.fragments

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.vocabhelper.R

class SplashFragment : Fragment() {

    companion object {
        const val PREFS_NAME = "SkipSplashScreen"
        const val PREF_SPLASH_SHOWN = "SplashScreenShown"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val prefs = requireActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val splashScreenShown = prefs.getBoolean(PREF_SPLASH_SHOWN, false)

        if (splashScreenShown) {
            // Skip the splash screen and navigate directly to the next screen
            findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
        } else {
            // Show the splash screen and set the preference
            Handler(Looper.getMainLooper()).postDelayed({
                if (isAdded) {
                    findNavController().navigate(R.id.action_splashFragment_to_onboardingViewpagerFragment)
                    prefs.edit().putBoolean(PREF_SPLASH_SHOWN, true).apply()
                }
            }, 2000)
        }
    }
}

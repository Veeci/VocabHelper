package com.veeci.vocabhelper.presentation.auth.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.veeci.base.utils.SharedPreferencesUtil
import com.veeci.vocabhelper.R

class SplashFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val splashScreenShown =
            SharedPreferencesUtil.getBoolean(requireContext(), "splashScreenShown", false)

        if (splashScreenShown) {
            findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
        } else {
            Handler(Looper.getMainLooper()).postDelayed({
                if (isAdded) {
                    findNavController().navigate(R.id.action_splashFragment_to_onboardingViewpagerFragment)
                    SharedPreferencesUtil.putBoolean(requireContext(), "splashScreenShown", true)
                }
            }, 3000)
        }
    }
}

package com.veeci.vocabhelper.presentation.auth.fragments.tabs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.veeci.vocabhelper.R
import com.veeci.vocabhelper.databinding.FragmentSixthBinding

class SixthFragment : Fragment() {

    private var _binding: FragmentSixthBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSixthBinding.inflate(layoutInflater, container, false)

        setupFunction()

        return binding.root
    }

    private fun setupFunction() {
        binding.buttonBack.setOnClickListener {
            findNavController().navigate(R.id.action_sixthFragment_to_onboardingViewpagerFragment)
        }

        binding.loginButton.setOnClickListener {
            findNavController().navigate(R.id.action_sixthFragment_to_loginFragment)
        }

        binding.signupButton.setOnClickListener {
            findNavController().navigate(R.id.action_sixthFragment_to_signupFragment)
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
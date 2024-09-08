package com.example.vocabhelper.presentation.auth.fragments.tabs

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.vocabhelper.R
import com.example.vocabhelper.databinding.FragmentFourthBinding

class FourthFragment : Fragment() {

    private var _binding: FragmentFourthBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFourthBinding.inflate(layoutInflater, container, false)

        setupFunction()

        return binding.root
    }

    private fun setupFunction() {
        binding.buttonBack.setOnClickListener {
            findNavController().navigate(R.id.action_fourthFragment_to_onboardingViewpagerFragment)
        }

        binding.loginButton.setOnClickListener {
            findNavController().navigate(R.id.action_fourthFragment_to_loginFragment)
        }

        binding.signupButton.setOnClickListener {
            findNavController().navigate(R.id.action_fourthFragment_to_signupFragment)
        }
    }

}
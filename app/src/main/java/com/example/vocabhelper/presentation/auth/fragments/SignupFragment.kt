package com.example.vocabhelper.presentation.auth.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.vocabhelper.R
import com.example.vocabhelper.databinding.FragmentSignupBinding

class SignupFragment : Fragment() {

    private var _binding: FragmentSignupBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignupBinding.inflate(layoutInflater, container, false)

        setupFunction()

        return binding.root
    }

    private fun setupFunction() {
        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.registerButton.setOnClickListener {
            findNavController().navigate(R.id.action_signupFragment_to_loginFragment)
        }

        binding.registerWithGoogle.setOnClickListener {

        }

        binding.goToLogin.setOnClickListener {
            findNavController().navigate(R.id.action_signupFragment_to_loginFragment)
        }
    }
}
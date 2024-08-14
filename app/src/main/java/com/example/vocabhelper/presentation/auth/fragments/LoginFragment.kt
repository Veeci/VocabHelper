package com.example.vocabhelper.presentation.auth.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.vocabhelper.R
import com.example.vocabhelper.databinding.FragmentLoginBinding
import com.example.vocabhelper.presentation.main.MainActivity

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(layoutInflater, container, false)

        setUpFunction()

        return binding.root
    }

    private fun setUpFunction() {
        binding.loginButton.setOnClickListener {
            val intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)
        }

        binding.goToRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signupFragment)
        }

        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.loginWithGoogle.setOnClickListener {

        }
    }

}
package com.veeci.vocabhelper.presentation.auth.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.veeci.vocabhelper.R
import com.veeci.vocabhelper.databinding.FragmentSignupBinding
import com.veeci.vocabhelper.domain.AuthViewModel

class SignupFragment : Fragment() {

    private var _binding: FragmentSignupBinding? = null
    private val binding get() = _binding!!

    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignupBinding.inflate(layoutInflater, container, false)

        setupFunction()

        return binding.root
    }

    private fun setupFunction() {
        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.registerButton.setOnClickListener {
            if(validateInfo())
            {
                authViewModel.signUp(binding.emailET.text.toString(), binding.passwordET.text.toString(), binding.fullnameET.text.toString(),
                    onSuccess = {
                        Toast.makeText(context, "Sign up successfully", Toast.LENGTH_SHORT).show()
                        authViewModel.verifyEmail(it,
                            onSuccess = {
                                Toast.makeText(context, "Go to your email to verify your account, then enjoy the app!", Toast.LENGTH_SHORT).show()
                                findNavController().navigate(R.id.action_signupFragment_to_loginFragment)
                            },
                            onFailure = {
                                Toast.makeText(context, "Email verification failed", Toast.LENGTH_SHORT).show()
                            })
                    },
                    onFailure = {
                        Toast.makeText(context, "Sign up failed", Toast.LENGTH_SHORT).show()
                    })
            }
        }

        binding.goToLogin.setOnClickListener {
            findNavController().navigate(R.id.action_signupFragment_to_loginFragment)
        }
    }

    private fun validateInfo() : Boolean
    {
        val passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$".toRegex()

        if(binding.fullnameET.text.toString().isEmpty() || binding.emailET.text.toString().isEmpty() || binding.passwordET.text.toString().isEmpty() || binding.confirmPasswordET.text.toString().isEmpty())
        {
            Toast.makeText(requireContext(), "Please fill all the fields", Toast.LENGTH_SHORT).show()
            return false
        }

        if(binding.passwordET.text.toString() != binding.confirmPasswordET.text.toString())
        {
            Toast.makeText(requireContext(), "Password and confirm password do not match", Toast.LENGTH_SHORT).show()
            return false
        }

        if(!passwordRegex.matches(binding.passwordET.text.toString()))
        {
            binding.passwordRegex.isVisible = true
            return false
        }

        return true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
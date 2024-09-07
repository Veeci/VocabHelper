package com.example.vocabhelper.presentation.auth.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.vocabhelper.R
import com.example.vocabhelper.databinding.FragmentSignupBinding
import com.example.vocabhelper.domain.AuthViewModel

class SignupFragment : Fragment() {

    private val binding by lazy{FragmentSignupBinding.inflate(layoutInflater)}

    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setupFunction()

        return binding.root
    }

    private fun setupFunction() {
        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.registerButton.setOnClickListener {
            if(authViewModel.validateInfo(binding.fullnameET.text.toString(), binding.emailET.text.toString(), binding.passwordET.text.toString(), binding.confirmPasswordET.text.toString()))
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
            else
            {
                binding.passwordRegex.visibility = View.VISIBLE
            }
        }

        binding.goToLogin.setOnClickListener {
            findNavController().navigate(R.id.action_signupFragment_to_loginFragment)
        }
    }
}
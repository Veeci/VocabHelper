package com.example.vocabhelper.presentation.auth.fragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.vocabhelper.R
import com.example.vocabhelper.databinding.FragmentLoginBinding
import com.example.vocabhelper.domain.AuthViewModel
import com.example.vocabhelper.presentation.main.MainActivity
import com.example.vocabhelper.presentation.main.fragments.setting.SettingFragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var googleSignInClient: GoogleSignInClient

    private val authViewModel: AuthViewModel by activityViewModels()
    private lateinit var prefs: SharedPreferences

    private val googleSignInLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            account?.let {
                authViewModel.firebaseAuthWithGoogle(it,
                    onSuccess = {
                        val intent = Intent(context, MainActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK )
                        Toast.makeText(context, "Google Sign in successful", Toast.LENGTH_SHORT).show()
                        context?.startActivity(intent)
                    },
                    onFailure = {
                        Toast.makeText(requireContext(), "Google Sign-In failed.", Toast.LENGTH_SHORT).show()
                    })
                val googleProfilePicUrl = it.photoUrl.toString()

                authViewModel.setProfilePicUrl(googleProfilePicUrl)
            }
        } catch (e: ApiException) {
            e.printStackTrace()
            Toast.makeText(requireContext(), "Google Sign-In failed.", Toast.LENGTH_SHORT).show()
        }
    }




    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentLoginBinding.inflate(layoutInflater, container, false)

        setUpFunction()

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireContext(), gso)
        prefs = requireContext().getSharedPreferences(SettingFragment.PREFS_NAME, Context.MODE_PRIVATE)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setRememberPassword()
    }

    private fun setUpFunction() {
        binding.loginButton.setOnClickListener {
            authViewModel.signIn(binding.emailET.text.toString(), binding.passwordET.text.toString(),
                onSuccess = {
                    authViewModel.setEmailAndPassword(binding.emailET.text.toString(), binding.passwordET.text.toString())
                    Toast.makeText(context, "Sign in successful", Toast.LENGTH_SHORT).show()
                    val intent = Intent(context, MainActivity::class.java)
                    context?.startActivity(intent)
                },
                onFailure = {
                    Toast.makeText(context, "Invalid Email or Password!", Toast.LENGTH_SHORT).show()
                })
        }

        binding.goToRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signupFragment)
        }

        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.loginWithGoogle.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            googleSignInLauncher.launch(signInIntent)
        }
    }

    private fun setRememberPassword() {
        val isRememberEnabled = prefs.getBoolean(SettingFragment.PREF_TOGGLE_SWITCH, false)

        if (isRememberEnabled) {
            val savedEmail = prefs.getString(SettingFragment.PREF_REMEMBER_EMAIL, "abc")
            val savedPassword = prefs.getString(SettingFragment.PREF_REMEMBER_PASSWORD, "123")

            if (!savedEmail.isNullOrEmpty() && !savedPassword.isNullOrEmpty()) {
                binding.emailET.text = Editable.Factory.getInstance().newEditable(savedEmail)
                binding.passwordET.text = Editable.Factory.getInstance().newEditable(savedPassword)
            }
            else
            {
                Log.e("LoginFragment", "Saved email or password in SharedPreferences is null or empty.")
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

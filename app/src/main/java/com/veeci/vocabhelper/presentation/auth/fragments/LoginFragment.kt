package com.veeci.vocabhelper.presentation.auth.fragments

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.veeci.base.utils.SharedPreferencesUtil
import com.veeci.vocabhelper.R
import com.veeci.vocabhelper.databinding.FragmentLoginBinding
import com.veeci.vocabhelper.domain.AuthViewModel
import com.veeci.vocabhelper.presentation.main.MainActivity
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var googleSignInClient: GoogleSignInClient

    private val authViewModel: AuthViewModel by activityViewModels()

    private val executor by lazy {
        this.context?.let { ContextCompat.getMainExecutor(it) }
    }

    private val googleSignInLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                account?.let {
                    authViewModel.firebaseAuthWithGoogle(it,
                        onSuccess = {
                            val intent = Intent(context, MainActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            Toast.makeText(context, "Google Sign in successful", Toast.LENGTH_SHORT)
                                .show()
                            context?.startActivity(intent)
                        },
                        onFailure = { errorMessage ->
                            Toast.makeText(
                                requireContext(),
                                "Google Sign-In failed: $errorMessage",
                                Toast.LENGTH_SHORT
                            ).show()
                        })
                    val googleProfilePicUrl = it.photoUrl.toString()

                    authViewModel.setProfilePicUrl(googleProfilePicUrl)
                }
            } catch (e: ApiException) {
                e.printStackTrace()
                Log.e("GoogleSignIn", "SignIn failed, error code: ${e.statusCode}")
                Toast.makeText(requireContext(), "Google Sign-In failed.", Toast.LENGTH_SHORT)
                    .show()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireContext(), gso)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpFunction()
        setRememberPassword()
    }

    private fun setUpFunction() {
        binding.loginButton.setOnClickListener {
            try {
                val email = binding.emailET.text.toString()
                val password = binding.passwordET.text.toString()

                FirebaseCrashlytics.getInstance().log("Login button clicked = {$email}")

                authViewModel.signIn(email, password,
                    onSuccess = {
                        Toast.makeText(context, "Sign in successful", Toast.LENGTH_SHORT).show()
                        val intent = Intent(context, MainActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        viewLifecycleOwner.lifecycleScope.launch {
                            if (email.isNotEmpty() && password.isNotEmpty()) {
                                SharedPreferencesUtil.putString(
                                    requireContext(),
                                    "encryptedEmail",
                                    email
                                )
                                SharedPreferencesUtil.putString(
                                    requireContext(),
                                    "encryptedPassword",
                                    password
                                )

                                FirebaseCrashlytics.getInstance().log(
                                    "Encrypted email: ${
                                        SharedPreferencesUtil.getString(
                                            requireContext(),
                                            "encryptedEmail",
                                            ""
                                        )
                                    }"
                                )

                                context?.startActivity(intent)
                            } else {
                                Toast.makeText(
                                    context,
                                    "Please enter email and password",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }.invokeOnCompletion {
                            setRememberPassword()
                        }
                    },
                    onFailure = {
                        Toast.makeText(context, "Invalid Email or Password!", Toast.LENGTH_SHORT)
                            .show()
                    }
                )
            } catch (e: Exception) {
                e.printStackTrace()
                FirebaseCrashlytics.getInstance().recordException(e)
                Toast.makeText(context, "Error encrypting password", Toast.LENGTH_SHORT).show()
            }
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

        binding.biometricLogin.setOnClickListener {
            setupBiometricAuth()
        }
    }

    private fun setRememberPassword() {
        val isRememberEnabled = SharedPreferencesUtil.getBoolean(requireContext(), "toggleSwitch", false)

        if (isRememberEnabled) {
            val decryptedEmail = SharedPreferencesUtil.getString(requireContext(), "encryptedEmail", "").toString().trim()
            val decryptedPassword = SharedPreferencesUtil.getString(requireContext(), "encryptedPassword", "")

            if (decryptedEmail.isNotEmpty() && decryptedPassword.toString().isNotEmpty()) {
                binding.emailET.text = Editable.Factory.getInstance().newEditable(decryptedEmail)
                binding.passwordET.text = Editable.Factory.getInstance().newEditable(decryptedPassword)
            } else {
                Log.e("LoginFragment", "Decrypted email or password is empty.")
            }
        } else {
            SharedPreferencesUtil.clear(requireContext(), "encryptedEmail")
            SharedPreferencesUtil.clear(requireContext(), "encryptedPassword")
        }
    }

    private fun setupBiometricAuth() {
        val biometricManager = BiometricManager.from(requireContext())

        val biometricPrompt = executor?.let {
            BiometricPrompt(this, it,
                object : BiometricPrompt.AuthenticationCallback() {
                    override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                        super.onAuthenticationError(errorCode, errString)
                        authViewModel.onBiometricAuthenticationResult(false)
                        Toast.makeText(
                            context,
                            "Biometric authentication error: $errString",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    override fun onAuthenticationFailed() {
                        super.onAuthenticationFailed()
                        authViewModel.onBiometricAuthenticationResult(false)
                        Toast.makeText(
                            context,
                            "Biometric authentication failed",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                        super.onAuthenticationSucceeded(result)
                        val user = FirebaseAuth.getInstance().currentUser
                        if (user != null) {
                            authViewModel.onBiometricAuthenticationResult(true)
                            Toast.makeText(
                                context,
                                "Biometric authentication succeeded",
                                Toast.LENGTH_SHORT
                            ).show()
                            val intent = Intent(context, MainActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            context?.startActivity(intent)
                        } else {
                            Toast.makeText(context, "User not logged in", Toast.LENGTH_SHORT).show()
                        }
                    }
                })
        }

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric login")
            .setSubtitle("Logging in using your device's biometric system")
            .setNegativeButtonText("Cancel")
            .build()

        val availabilityMessage = authViewModel.evaluateBiometricAvailability(biometricManager)
        if (availabilityMessage == "App can authenticate using biometrics") {
            biometricPrompt?.authenticate(promptInfo)
        } else {
            Toast.makeText(context, availabilityMessage, Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
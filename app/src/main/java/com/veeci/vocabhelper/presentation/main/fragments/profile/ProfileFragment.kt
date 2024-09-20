package com.veeci.vocabhelper.presentation.main.fragments.profile

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import coil.load
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.veeci.vocabhelper.R
import com.veeci.vocabhelper.databinding.FragmentProfileBinding
import com.veeci.vocabhelper.domain.AuthViewModel
import com.veeci.vocabhelper.domain.MainViewModel
import com.veeci.vocabhelper.presentation.auth.AuthActivity

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val authViewModel: AuthViewModel by activityViewModels()
    private val mainViewModel: MainViewModel by activityViewModels()

    private lateinit var googleSignInClient: GoogleSignInClient

    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            authViewModel.uploadProfileImage(it,
                onSuccess = {
                    Toast.makeText(requireContext(), "Profile image uploaded successfully", Toast.LENGTH_SHORT).show()
                },
                onFailure = {
                    Toast.makeText(requireContext(), "Failed to upload profile image", Toast.LENGTH_SHORT).show()
                }
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

        setupFunctions()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainViewModel.profilePicUrl.observe(viewLifecycleOwner) { url ->
            binding.profilePic.load(url)
        }
    }

    private fun setupFunctions() {
        binding.changeAccountAvatar.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

        authViewModel.fullName.observe(viewLifecycleOwner)
        {
            binding.fullName.text = it
        }

        binding.feedback.setOnClickListener {
            val dialog = Dialog(requireContext())
            dialog.setContentView(R.layout.feedback_dialog)
            dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            dialog.setCancelable(true)

            val title = dialog.findViewById<TextInputEditText>(R.id.titleET)
            val feedback = dialog.findViewById<TextInputEditText>(R.id.feedbackET)
            val sendButton = dialog.findViewById<TextView>(R.id.sendFeedbackButton)

            sendButton.setOnClickListener {
                val titleText = title.text.toString()
                val feedbackText = feedback.text.toString()

                val i: Intent = Intent(Intent.ACTION_SEND)
                i.setType("message/html")
                i.putExtra(Intent.EXTRA_EMAIL, arrayOf("doanvietquang15102003@gmail.com"))
                i.putExtra(Intent.EXTRA_SUBJECT, "Feedback for VocabHelper")
                i.putExtra(Intent.EXTRA_TEXT, "Title: ${titleText} \nFeedback: ${feedbackText}")
                try {
                    startActivity(Intent.createChooser(i, "Send mail through..."))
                }
                catch (e: Exception) {
                    Toast.makeText(requireContext(), "Failed to send email", Toast.LENGTH_SHORT).show()
                }
                dialog.dismiss()
            }

            dialog.show()
        }

        binding.changeAccountPassword.setOnClickListener {
            val dialog = Dialog(requireContext())
            dialog.setContentView(R.layout.reset_password_dialog)
            dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            dialog.setCancelable(true)

            val emailText = dialog.findViewById<TextView>(R.id.email_text)
            val sendButton = dialog.findViewById<TextView>(R.id.send_button)
            val cancelButton = dialog.findViewById<TextView>(R.id.cancel_button)

            emailText.text = Firebase.auth.currentUser?.email ?: ""

            sendButton.setOnClickListener {
                authViewModel.sendPasswordResetEmail(emailText.text.toString(),
                    onSuccess = {
                        dialog.dismiss()
                        Toast.makeText(requireContext(), "Password reset email sent", Toast.LENGTH_SHORT).show()
                    },
                    onFailure = {
                        Toast.makeText(requireContext(), "Failed to send password reset email", Toast.LENGTH_SHORT).show()
                    }
                )
            }

            cancelButton.setOnClickListener {
                dialog.dismiss()
            }

            dialog.show()
        }

        binding.logout.setOnClickListener {
            val dialog = Dialog(requireContext())
            dialog.setContentView(R.layout.logout_dialog)
            dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            dialog.setCancelable(false)
            dialog.window?.attributes?.windowAnimations = R.style.animation

            val okayText = dialog.findViewById<TextView>(R.id.okay_text)
            val cancelText = dialog.findViewById<TextView>(R.id.cancel_text)

            okayText.setOnClickListener {
                dialog.dismiss()
                authViewModel.logOut(googleSignInClient,
                    onSuccess = {
                        Toast.makeText(context, "Sign out successful", Toast.LENGTH_SHORT).show()
                        val intent = Intent(context, AuthActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        context?.startActivity(intent)
                    },
                    onFailure = {
                        Toast.makeText(context, "Sign out failed", Toast.LENGTH_SHORT).show()
                    }
                )
            }

            cancelText.setOnClickListener {
                dialog.dismiss()
            }

            dialog.show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

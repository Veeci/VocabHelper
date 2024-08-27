package com.example.vocabhelper.presentation.main.fragments.profile

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.viewModels
import coil.load
import com.example.vocabhelper.R
import com.example.vocabhelper.databinding.FragmentProfileBinding
import com.example.vocabhelper.domain.AuthViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val authViewModel: AuthViewModel by viewModels()

    private lateinit var googleSignInClient: GoogleSignInClient

    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            authViewModel.uploadProfileImage(it, requireContext())
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

    private fun setupFunctions() {
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
                authViewModel.logOut(requireContext(), googleSignInClient)
            }

            cancelText.setOnClickListener {
                dialog.dismiss()
            }

            dialog.show()
        }

        binding.changeAccountAvatar.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.profilePic.load(Firebase.auth.currentUser?.photoUrl)

        authViewModel.profilePicUrl.observe(viewLifecycleOwner) { url ->
            binding.profilePic.load(url)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

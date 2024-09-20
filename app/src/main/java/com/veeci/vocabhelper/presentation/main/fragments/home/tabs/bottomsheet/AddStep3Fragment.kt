package com.veeci.vocabhelper.presentation.main.fragments.home.tabs.bottomsheet

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.firebase.storage.FirebaseStorage
import com.veeci.vocabhelper.R
import com.veeci.vocabhelper.data.api.APIService
import com.veeci.vocabhelper.data.implementation.WordRepoImplementation
import com.veeci.vocabhelper.databinding.FragmentAddStep3Binding
import com.veeci.vocabhelper.domain.WordViewModel
import java.io.File

class AddStep3Fragment : Fragment(R.layout.fragment_add_step3) {

    private val binding by lazy { FragmentAddStep3Binding.inflate(layoutInflater) }

    private val apiService by lazy { APIService.create() }

    private val wordRepository by lazy { WordRepoImplementation(apiService) }

    private val wordViewModel: WordViewModel by activityViewModels {
        WordViewModel.Factory(wordRepository)
    }

    var onCompleteClick: (() -> Unit)? = null
    var onPreviousClick: (() -> Unit)? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backStep3Button.setOnClickListener {
            onPreviousClick?.invoke()
        }

        binding.finishButton.setOnClickListener {
            wordViewModel.collocation = binding.collocationET.text.toString()
            wordViewModel.example = binding.exampleET.text.toString()
            wordViewModel.saveWord()
            uploadPronunciationAudio()
            onCompleteClick?.invoke()
            wordViewModel.fetchWords()
        }
    }

    private fun uploadPronunciationAudio()
    {
        val audioFilePath = wordViewModel.audioUrl
        if(audioFilePath != null)
        {

            val file = Uri.fromFile(File(audioFilePath))
            val storageRef = FirebaseStorage.getInstance().reference.child("audio/${file.lastPathSegment}")

            val uploadTask = storageRef.putFile(file)
             uploadTask.addOnSuccessListener {
                 storageRef.downloadUrl.addOnSuccessListener { uri ->
                     wordViewModel.audioUrl = uri.toString()
                     Toast.makeText(requireContext(), "Audio uploaded successfully", Toast.LENGTH_SHORT).show()
                 }
             }.addOnFailureListener {
                 Log.e("UploadError", "Failed to upload audio", it)
                 Toast.makeText(requireContext(), "Failed to upload audio", Toast.LENGTH_SHORT).show()
             }
        }
        else
        {
            Toast.makeText(requireContext(), "No audio file selected", Toast.LENGTH_SHORT).show()
        }
    }
}

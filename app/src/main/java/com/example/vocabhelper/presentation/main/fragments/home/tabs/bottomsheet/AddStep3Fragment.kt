package com.example.vocabhelper.presentation.main.fragments.home.tabs.bottomsheet

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.vocabhelper.R
import com.example.vocabhelper.data.api.APIService
import com.example.vocabhelper.data.repository.WordRepository
import com.example.vocabhelper.databinding.FragmentAddStep3Binding
import com.example.vocabhelper.domain.WordViewModel
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class AddStep3Fragment : Fragment(R.layout.fragment_add_step3) {

    private val binding by lazy { FragmentAddStep3Binding.inflate(layoutInflater) }

    private val wordViewModel: WordViewModel by activityViewModels {
        WordViewModel.Factory(WordRepository(apiService = APIService.create()))
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

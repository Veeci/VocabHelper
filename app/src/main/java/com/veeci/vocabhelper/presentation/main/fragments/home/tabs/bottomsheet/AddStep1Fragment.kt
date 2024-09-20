package com.veeci.vocabhelper.presentation.main.fragments.home.tabs.bottomsheet

import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Spinner
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.veeci.vocabhelper.R
import com.veeci.vocabhelper.data.api.APIService
import com.veeci.vocabhelper.data.implementation.WordRepoImplementation
import com.veeci.vocabhelper.databinding.FragmentAddStep1Binding
import com.veeci.vocabhelper.domain.WordViewModel
import com.veeci.vocabhelper.presentation.main.fragments.home.tabs.adapter.CategorySpinnerAdapter

class AddStep1Fragment : Fragment() {

    private val binding by lazy { FragmentAddStep1Binding.inflate(layoutInflater) }
    private var mediaRecorder: MediaRecorder? = null
    private var audioFilePath: String? = null

    private val apiService by lazy { APIService.create() }

    private val wordRepository by lazy { WordRepoImplementation(apiService) }

    private val wordViewModel: WordViewModel by activityViewModels {
        WordViewModel.Factory(wordRepository)
    }

    var onNextClick: (() -> Unit)? = null


    companion object {
        private const val PERMISSION_REQUEST_CODE = 200
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val spinner: Spinner = binding.categorySpinner
        val categories = resources.getStringArray(R.array.categories_array)
        val adapter = CategorySpinnerAdapter(requireContext(), categories)
        spinner.adapter = adapter


        checkPermissions()

        binding.recordButton.setOnClickListener {
            startRecording()
        }

        binding.stopRecordingButton.setOnClickListener {
            stopRecording()
        }

        binding.nextStep1Button.setOnClickListener {
            wordViewModel.wordToAdd = binding.wordET.text.toString()
            wordViewModel.meaning = binding.meaningET.text.toString()
            wordViewModel.category = binding.categorySpinner.selectedItem.toString()
            wordViewModel.audioUrl = audioFilePath
            onNextClick?.invoke()
        }
    }

    private fun checkPermissions() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(
                    android.Manifest.permission.RECORD_AUDIO,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                PERMISSION_REQUEST_CODE
            )
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(requireContext(), "Permission granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun startRecording() {
        if (audioFilePath.isNullOrEmpty()) {
            audioFilePath =
                "${requireContext().externalCacheDir?.absolutePath}/audio_${binding.wordET.text.toString()}.mp3"
        }

        if (mediaRecorder == null) {
            mediaRecorder = MediaRecorder().apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                setOutputFile(audioFilePath)
                try {
                    Toast.makeText(requireContext(), "Recording started", Toast.LENGTH_SHORT).show()
                    prepare()
                    start()
                } catch (e: Exception) {
                    Log.e("RecordingError", "Failed to start recording", e)
                    Toast.makeText(requireContext(), "Recording failed", Toast.LENGTH_SHORT).show()
                    releaseMediaRecorder()
                }
            }
        } else {
            Toast.makeText(
                requireContext(),
                "MediaRecorder is already initialized",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun stopRecording() {
        if (mediaRecorder != null) {
            try {
                mediaRecorder?.apply {
                    stop()
                    release()
                }
                Toast.makeText(requireContext(), "Recording stopped", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Log.e("RecordingError", "Failed to stop recording", e)
                Toast.makeText(requireContext(), "Stopping recording failed", Toast.LENGTH_SHORT)
                    .show()
            } finally {
                mediaRecorder = null
            }
        } else {
            Toast.makeText(requireContext(), "No recording in progress", Toast.LENGTH_SHORT).show()
        }
    }

    private fun releaseMediaRecorder() {
        mediaRecorder?.release()
        mediaRecorder = null
    }

    override fun onDestroy() {
        super.onDestroy()
        releaseMediaRecorder()
    }
}
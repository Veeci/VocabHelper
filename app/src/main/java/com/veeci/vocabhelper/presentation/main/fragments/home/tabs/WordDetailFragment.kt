package com.veeci.vocabhelper.presentation.main.fragments.home.tabs

import android.media.MediaPlayer
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.veeci.vocabhelper.R
import com.veeci.vocabhelper.data.api.APIService
import com.veeci.vocabhelper.data.implementation.WordRepoImplementation
import com.veeci.vocabhelper.data.models.WordData
import com.veeci.vocabhelper.databinding.FragmentWordDetailBinding
import com.veeci.vocabhelper.domain.WordViewModel
import com.veeci.vocabhelper.presentation.main.fragments.home.tabs.adapter.CategorySpinnerAdapter

class WordDetailFragment : Fragment() {

    private val binding by lazy { FragmentWordDetailBinding.inflate(layoutInflater) }

    private val apiService by lazy { APIService.create() }

    private val wordRepository by lazy { WordRepoImplementation(apiService) }

    private val wordViewModel: WordViewModel by activityViewModels {
        WordViewModel.Factory(wordRepository)
    }

    private var mediaPlayer: MediaPlayer? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        setupFunction()
    }

    private fun observeViewModel() {
        wordViewModel.wordRemember.observe(viewLifecycleOwner) { word ->
            wordViewModel.getWordDetail(word)
        }

        wordViewModel.wordDetail.observe(viewLifecycleOwner) { wordDetail ->
            displayWord(wordDetail)
        }
    }

    private fun setupFunction() {
        binding.saveButton.setOnClickListener {
            updateWord()
        }
        binding.deleteButton.setOnClickListener {
            deleteWord()
        }
    }

    private fun displayWord(word: List<WordData>) {
        val wordData = word[0]
        binding.apply {
            wordDetail.text = wordData.word
            meaningDetail.text = Editable.Factory.getInstance().newEditable(wordData.meaning)
            synonymDetail.text = Editable.Factory.getInstance().newEditable(wordData.synonym.toString())
            antonymDetail.text = Editable.Factory.getInstance().newEditable(wordData.antonym.toString())
            collocationDetail.text = Editable.Factory.getInstance().newEditable(wordData.collocation.toString())
            exampleDetail.text = Editable.Factory.getInstance().newEditable(wordData.example.toString())

            val categories = resources.getStringArray(R.array.categories_array)
            val adapter = CategorySpinnerAdapter(requireContext(), categories)
            categoryDetail.adapter = adapter
            categoryDetail.setSelection(adapter.getPosition(wordData.category))

            pronunciationDetail.setOnClickListener {
                val audioUrl = wordData.audioUrl
                if (!audioUrl.isNullOrEmpty()) {
                    playAudio(audioUrl)
                } else {
                    Toast.makeText(requireContext(), "No audio available", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            back.setOnClickListener {
                requireActivity().onBackPressed()
            }
        }
    }

    private fun playAudio(audioUrl: String) {
        mediaPlayer?.release()

        try {
            mediaPlayer = MediaPlayer().apply {
                setDataSource(audioUrl)
                prepareAsync()
                setOnPreparedListener {
                    it.start()
                }
                setOnCompletionListener {
                    it.release()
                }
                setOnErrorListener { mp, what, extra ->
                    mp.release()
                    true
                }
            }
        } catch (e: Exception) {
            Log.e("MediaPlayer", "Error playing audio from path: $audioUrl, ${e.message}")
        }
    }

    private fun updateWord() {
        wordViewModel.wordDetail.observe(viewLifecycleOwner) { word ->
            val updatedData = HashMap<String, Any>()
            updatedData["meaning"] = binding.meaningDetail.text.toString()
            updatedData["category"] = binding.categoryDetail.selectedItem.toString()
            updatedData["synonym"] = binding.synonymDetail.text.toString()
            updatedData["antonym"] = binding.antonymDetail.text.toString()
            updatedData["collocation"] = binding.collocationDetail.text.toString()
            updatedData["example"] = binding.exampleDetail.text.toString()
            wordViewModel.updateWord(word[0], updatedData)
        }
    }

    private fun deleteWord() {
        wordViewModel.wordDetail.observe(viewLifecycleOwner) { word ->
            wordViewModel.deleteWord(word[0])
        }
    }

    private fun releaseMediaPlayer() {
        mediaPlayer?.release()
        mediaPlayer = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        releaseMediaPlayer()
    }
}
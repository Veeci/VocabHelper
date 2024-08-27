package com.example.vocabhelper.presentation.main.fragments.home.tabs

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
import androidx.lifecycle.Observer
import com.example.vocabhelper.R
import com.example.vocabhelper.data.api.APIService
import com.example.vocabhelper.data.models.WordData
import com.example.vocabhelper.data.repository.WordRepository
import com.example.vocabhelper.databinding.FragmentWordDetailBinding
import com.example.vocabhelper.domain.WordViewModel
import com.example.vocabhelper.presentation.main.fragments.home.tabs.adapter.CategorySpinnerAdapter

class WordDetailFragment : Fragment() {

    private val binding by lazy { FragmentWordDetailBinding.inflate(layoutInflater) }

    private val wordViewModel: WordViewModel by activityViewModels {
        WordViewModel.Factory(WordRepository(apiService = APIService.create()))
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
        wordViewModel.wordRemember.observe(viewLifecycleOwner, Observer { word ->
            wordViewModel.getWordDetail(word)
        })

        wordViewModel.wordDetail.observe(viewLifecycleOwner, Observer { wordDetail ->
            displayWord(wordDetail)
        })
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
        val word = word[0]
        binding.apply {
            wordDetail.text = word.word
            meaningDetail.text = Editable.Factory.getInstance().newEditable(word.meaning)
            synonymDetail.text = Editable.Factory.getInstance().newEditable(word.synonym.toString())
            antonymDetail.text = Editable.Factory.getInstance().newEditable(word.antonym.toString())
            collocationDetail.text = Editable.Factory.getInstance().newEditable(word.collocation.toString())
            exampleDetail.text = Editable.Factory.getInstance().newEditable(word.example.toString())

            val categories = resources.getStringArray(R.array.categories_array)
            val adapter = CategorySpinnerAdapter(requireContext(), categories)
            categoryDetail.adapter = adapter
            categoryDetail.setSelection(adapter.getPosition(word.category))

            pronunciationDetail.setOnClickListener {
                val audioUrl = word.audioUrl
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
        wordViewModel.wordDetail.observe(viewLifecycleOwner, Observer { word ->
            val updatedData = HashMap<String, Any>()
            updatedData["meaning"] = binding.meaningDetail.text.toString()
            updatedData["category"] = binding.categoryDetail.selectedItem.toString()
            updatedData["synonym"] = binding.synonymDetail.text.toString()
            updatedData["antonym"] = binding.antonymDetail.text.toString()
            updatedData["collocation"] = binding.collocationDetail.text.toString()
            updatedData["example"] = binding.exampleDetail.text.toString()
            wordViewModel.updateWord(word[0], updatedData)
        })
    }


    private fun deleteWord() {
        wordViewModel.wordDetail.observe(viewLifecycleOwner, Observer { word ->
            wordViewModel.deleteWord(word[0])
        })
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
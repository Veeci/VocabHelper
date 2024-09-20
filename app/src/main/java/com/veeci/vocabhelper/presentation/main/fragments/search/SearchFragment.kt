package com.veeci.vocabhelper.presentation.main.fragments.search

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.mancj.materialsearchbar.MaterialSearchBar
import com.veeci.vocabhelper.R
import com.veeci.vocabhelper.data.api.APIService
import com.veeci.vocabhelper.data.implementation.WordRepoImplementation
import com.veeci.vocabhelper.data.models.Response
import com.veeci.vocabhelper.databinding.FragmentSearchBinding
import com.veeci.vocabhelper.domain.WordViewModel
import com.veeci.vocabhelper.presentation.main.fragments.search.adapter.WordSearchAdapter

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val apiService by lazy { APIService.create() }

    private val wordRepository by lazy { WordRepoImplementation(apiService) }

    private val wordViewModel: WordViewModel by activityViewModels {
        WordViewModel.Factory(wordRepository)
    }

    private lateinit var wordSearchAdapter: WordSearchAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)

        setupRecyclerView()
        observeViewModel()

        return binding.root
    }

    private fun setupRecyclerView() {
        wordSearchAdapter = WordSearchAdapter()
        binding.meaningsRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = wordSearchAdapter
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.searchBar.setOnSearchActionListener(object :
            MaterialSearchBar.OnSearchActionListener {
            override fun onSearchStateChanged(enabled: Boolean) {
            }

            override fun onSearchConfirmed(text: CharSequence?) {
                text?.let { query ->
                    wordViewModel.getWordDefinition(query.toString())
                    val inputMethodManager =
                        context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                    inputMethodManager?.hideSoftInputFromWindow(view.windowToken, 0)
                }
            }

            override fun onButtonClicked(buttonCode: Int) {
            }
        })
    }

    private fun observeViewModel() {
        wordViewModel.word.observe(viewLifecycleOwner, Observer { word ->
            if (word != null) {
                displayWord(word)
                binding.searchResult.visibility = View.VISIBLE
                binding.emptySearchresult.visibility = View.GONE
            } else {
                binding.searchResult.visibility = View.GONE
                binding.emptySearchresult.visibility = View.VISIBLE
            }
        })
    }

    private fun displayWord(word: Response) {
        binding.word.text = word.word
        binding.phonetic.text = word.phonetic
        word.meanings?.let { meanings ->
            if (meanings.isEmpty()) {
                binding.meaningsTV.visibility = View.GONE
                binding.meaningsRecyclerView.visibility = View.GONE
            } else {
                wordSearchAdapter.updateMeanings(meanings.filterNotNull())
                binding.meaningsTV.visibility = View.VISIBLE
                binding.meaningsRecyclerView.visibility = View.VISIBLE
            }

        }
        binding.pronunciation.setOnClickListener {
            var audioUrl = ""
            for (audio in word.phonetics!!) {
                if (!audio?.audio.isNullOrEmpty()) {
                    audioUrl = audio?.audio!!
                }
            }

            if (audioUrl.isNotEmpty())
            {
                wordViewModel.playAudio(requireContext(), audioUrl)
            }
            else
            {
                Toast.makeText(context, getString(R.string.no_audio_available), Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        wordViewModel.releaseMediaPlayer()
        _binding = null
    }
}
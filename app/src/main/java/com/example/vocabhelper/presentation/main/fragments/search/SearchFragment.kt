package com.example.vocabhelper.presentation.main.fragments.search

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vocabhelper.data.api.APIService
import com.example.vocabhelper.data.database.WordDatabase
import com.example.vocabhelper.data.models.Response
import com.example.vocabhelper.data.repository.WordRepository
import com.example.vocabhelper.databinding.FragmentSearchBinding
import com.example.vocabhelper.domain.WordViewModel
import com.example.vocabhelper.presentation.main.fragments.search.adapter.WordSearchAdapter
import com.mancj.materialsearchbar.MaterialSearchBar

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val wordViewModel: WordViewModel by activityViewModels {
        WordViewModel.Factory(
            WordRepository(
                apiService = APIService.create(),
                wordDAO = WordDatabase.getDatabase(requireContext()).wordDao()
            )
        )
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
                // Handle search state changes if needed
            }

            override fun onSearchConfirmed(text: CharSequence?) {
                text?.let { query ->
                    wordViewModel.getWordDefinition(query.toString())
                    // Hide the keyboard
                    val inputMethodManager =
                        context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                    inputMethodManager?.hideSoftInputFromWindow(view.windowToken, 0)
                }
            }

            override fun onButtonClicked(buttonCode: Int) {
                // Handle button clicks if needed
            }
        })
    }

    private fun observeViewModel() {
        // Observe changes in word LiveData
        wordViewModel.word.observe(viewLifecycleOwner, Observer { word ->
            if (word != null) {
                displayWord(word)
            } else {
                binding.word.text = "Word not found"
            }
        })
    }

    private fun displayWord(word: Response) {
        binding.word.text = word.word
        binding.phonetic.text = word.phonetic
        word.meanings?.let { meanings ->
            wordSearchAdapter.updateMeanings(meanings.filterNotNull())
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
package com.example.vocabhelper.presentation.main.fragments.home.tabs

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vocabhelper.R
import com.example.vocabhelper.data.api.APIService
import com.example.vocabhelper.data.implementation.WordRepoImplementation
import com.example.vocabhelper.databinding.FragmentWordListBinding
import com.example.vocabhelper.domain.WordViewModel
import com.example.vocabhelper.presentation.main.fragments.home.tabs.adapter.WordAdapter

class WordListFragment : Fragment() {

    private val binding by lazy { FragmentWordListBinding.inflate(layoutInflater) }

    private val apiService by lazy { APIService.create() }

    private val wordRepository by lazy { WordRepoImplementation(apiService) }

    private val wordViewModel: WordViewModel by activityViewModels {
        WordViewModel.Factory(wordRepository)
    }

    private lateinit var wordAdapter: WordAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        wordAdapter = WordAdapter { wordData ->
            wordViewModel.getWordDetail(wordData.word)
            findNavController().navigate(R.id.action_wordListFragment_to_wordDetailFragment)
        }

        binding.wordListRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = wordAdapter
        }

        binding.back.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun observeViewModel() {
        wordViewModel.wordsByCategory.observe(viewLifecycleOwner) { words ->
            if(words.isNotEmpty()){
                wordAdapter.updateData(words)
            }
            else
            {
                binding.emptyCategoryLayout.visibility = View.VISIBLE
                binding.wordListRecyclerView.visibility = View.GONE
            }
        }
    }
}

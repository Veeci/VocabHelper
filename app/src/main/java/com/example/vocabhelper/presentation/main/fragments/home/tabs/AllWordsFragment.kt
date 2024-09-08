package com.example.vocabhelper.presentation.main.fragments.home.tabs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vocabhelper.R
import com.example.vocabhelper.data.api.APIService
import com.example.vocabhelper.data.implementation.WordRepoImplementation
import com.example.vocabhelper.databinding.FragmentAllWordsBinding
import com.example.vocabhelper.domain.WordViewModel
import com.example.vocabhelper.presentation.main.fragments.home.tabs.adapter.WordAdapter

class AllWordsFragment : Fragment() {

    private val binding by lazy { FragmentAllWordsBinding.inflate(layoutInflater) }

    private val apiService by lazy { APIService.create() }

    private val wordRepository by lazy { WordRepoImplementation(apiService) }

    private val wordViewModel: WordViewModel by activityViewModels {
        WordViewModel.Factory(wordRepository)
    }

    private lateinit var adapter: WordAdapter

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
        val wordAdapter = WordAdapter { wordData ->
            wordViewModel.getWordDetail(wordData.word)
            findNavController().navigate(R.id.action_mainFragment_to_wordDetailFragment)
        }

        binding.allWordsRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = wordAdapter
        }
    }

    private fun observeViewModel() {
        wordViewModel.wordStored.observe(viewLifecycleOwner){ wordData ->
            (binding.allWordsRecyclerView.adapter as WordAdapter).updateData(wordData)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        wordViewModel.releaseMediaPlayer()
        if (::adapter.isInitialized) {
            adapter.releaseMediaPlayer()
        }
    }
}
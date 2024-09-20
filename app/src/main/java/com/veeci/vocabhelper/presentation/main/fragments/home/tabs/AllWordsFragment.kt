package com.veeci.vocabhelper.presentation.main.fragments.home.tabs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.veeci.vocabhelper.R
import com.veeci.vocabhelper.data.api.APIService
import com.veeci.vocabhelper.data.implementation.WordRepoImplementation
import com.veeci.vocabhelper.databinding.FragmentAllWordsBinding
import com.veeci.vocabhelper.domain.WordViewModel
import com.veeci.vocabhelper.presentation.main.fragments.home.tabs.adapter.WordAdapter

class AllWordsFragment : Fragment() {

    private var _binding: FragmentAllWordsBinding? = null
    private val binding get() = _binding!!

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

        _binding = FragmentAllWordsBinding.inflate(layoutInflater, container, false)
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
            if(wordData.isEmpty())
            {
                binding.allWordsRecyclerView.visibility = View.GONE
                binding.placeholder.visibility = View.VISIBLE
            }
            else {
                (binding.allWordsRecyclerView.adapter as WordAdapter).updateData(wordData)
                binding.allWordsRecyclerView.visibility = View.VISIBLE
                binding.placeholder.visibility = View.GONE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        wordViewModel.releaseMediaPlayer()
        if (::adapter.isInitialized) {
            adapter.releaseMediaPlayer()
        }
        _binding = null
    }
}
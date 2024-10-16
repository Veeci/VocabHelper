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
import com.veeci.vocabhelper.databinding.FragmentWordListBinding
import com.veeci.vocabhelper.domain.WordViewModel
import com.veeci.vocabhelper.presentation.main.fragments.home.tabs.adapter.WordAdapter

class WordListFragment : Fragment() {

    private var _binding: FragmentWordListBinding? = null
    private val binding get() = _binding!!

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
        _binding = FragmentWordListBinding.inflate(inflater, container, false)

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
            requireActivity().onBackPressed()
        }
    }

    private fun observeViewModel() {
        wordViewModel.wordsByCategory.observe(viewLifecycleOwner) { words ->
            if(words.isEmpty())
            {
                binding.wordListRecyclerView.visibility = View.GONE
                binding.empty.visibility = View.VISIBLE
                return@observe
            }
            else {
                binding.wordListRecyclerView.visibility = View.VISIBLE
                binding.empty.visibility = View.GONE
                wordAdapter.updateData(words)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
package com.example.vocabhelper.presentation.main.fragments.home.tabs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.vocabhelper.R
import com.example.vocabhelper.data.api.APIService
import com.example.vocabhelper.data.implementation.WordRepoImplementation
import com.example.vocabhelper.databinding.FragmentWordsByCategoryBinding
import com.example.vocabhelper.domain.WordViewModel
import com.example.vocabhelper.presentation.main.fragments.home.tabs.adapter.CategoryAdapter

class WordsByCategoryFragment : Fragment() {

    private var _binding: FragmentWordsByCategoryBinding? = null
    private val binding get() = _binding!!

    private val apiService by lazy { APIService.create() }

    private val wordRepository by lazy { WordRepoImplementation(apiService) }

    private val wordViewModel: WordViewModel by activityViewModels {
        WordViewModel.Factory(wordRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWordsByCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        val categoryAdapter = CategoryAdapter { category ->
            val categoryName = getString(category.nameResId)
            wordViewModel.fetchWordsByCategory(categoryName)
            wordViewModel.setCategoryRemember(categoryName)
            findNavController().navigate(R.id.action_mainFragment_to_wordListFragment)
        }

        binding.categoriesRecyclerView.apply {
            layoutManager = GridLayoutManager(requireContext(), 3)
            adapter = categoryAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

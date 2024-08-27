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
import com.example.vocabhelper.databinding.FragmentWordsByCategoryBinding
import com.example.vocabhelper.domain.WordViewModel
import com.example.vocabhelper.presentation.main.fragments.home.tabs.adapter.CategoryAdapter

class WordsByCategoryFragment : Fragment() {

    private val binding by lazy { FragmentWordsByCategoryBinding.inflate(layoutInflater) }

    private val wordViewModel: WordViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        val categoryAdapter = CategoryAdapter { category ->
            wordViewModel.fetchWordsByCategory(category.name)
            wordViewModel.setCategoryRemember(category.name)
            findNavController().navigate(R.id.action_mainFragment_to_wordListFragment)
        }

        binding.categoriesRecyclerView.apply {
            layoutManager = GridLayoutManager(requireContext(), 3)
            adapter = categoryAdapter
        }
    }
}

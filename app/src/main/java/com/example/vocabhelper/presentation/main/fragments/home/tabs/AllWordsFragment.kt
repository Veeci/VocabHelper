package com.example.vocabhelper.presentation.main.fragments.home.tabs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vocabhelper.data.api.APIService
import com.example.vocabhelper.data.repository.WordRepository
import com.example.vocabhelper.databinding.FragmentAllWordsBinding
import com.example.vocabhelper.domain.WordViewModel
import com.example.vocabhelper.presentation.main.fragments.home.tabs.adapter.WordAdapter

class AllWordsFragment : Fragment() {

    private val binding by lazy { FragmentAllWordsBinding.inflate(layoutInflater) }

    private val wordViewModel: WordViewModel by activityViewModels {
        WordViewModel.Factory(WordRepository(apiService = APIService.create()))
    }

    private lateinit var adapter: WordAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        setupRecyclerView()

        return binding.root
    }

    private fun setupRecyclerView() {
        adapter = WordAdapter()
        binding.allWordsRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@AllWordsFragment.adapter
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewModel()
    }

    private fun observeViewModel() {
        wordViewModel.wordStored.observe(viewLifecycleOwner) { words ->
            adapter.updateData(words)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        wordViewModel.releaseMediaPlayer()
    }
}
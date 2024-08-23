package com.example.vocabhelper.presentation.main.fragments.home.tabs

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vocabhelper.R
import com.example.vocabhelper.data.api.APIService
import com.example.vocabhelper.data.models.WordData
import com.example.vocabhelper.data.repository.WordRepository
import com.example.vocabhelper.databinding.FragmentAllWordsBinding
import com.example.vocabhelper.domain.WordViewModel
import com.example.vocabhelper.presentation.main.fragments.home.tabs.adapter.WordAdapter
import com.google.firebase.firestore.FirebaseFirestore

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

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        wordViewModel.fetchWords().observe(viewLifecycleOwner) { words ->
            adapter = WordAdapter(words)
            binding.allWordsRecyclerView.apply {
                layoutManager = LinearLayoutManager(requireContext())
                this.adapter = adapter
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        wordViewModel.releaseMediaPlayer()
    }
}
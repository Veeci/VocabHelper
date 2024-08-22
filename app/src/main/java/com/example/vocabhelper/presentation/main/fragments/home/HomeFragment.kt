package com.example.vocabhelper.presentation.main.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.vocabhelper.data.api.APIService
import com.example.vocabhelper.data.repository.WordRepository
import com.example.vocabhelper.databinding.FragmentHomeBinding
import com.example.vocabhelper.domain.WordViewModel


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val wordViewModel: WordViewModel by activityViewModels {
        WordViewModel.Factory(WordRepository(apiService = APIService.create()))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

}
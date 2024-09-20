package com.veeci.vocabhelper.presentation.main.fragments.home.tabs.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.veeci.vocabhelper.data.api.APIService
import com.veeci.vocabhelper.data.implementation.WordRepoImplementation
import com.veeci.vocabhelper.databinding.FragmentAddStep2Binding
import com.veeci.vocabhelper.domain.WordViewModel

class AddStep2Fragment : Fragment() {

    private val binding by lazy { FragmentAddStep2Binding.inflate(layoutInflater) }

    private val apiService by lazy { APIService.create() }

    private val wordRepository by lazy { WordRepoImplementation(apiService) }

    private val wordViewModel: WordViewModel by activityViewModels {
        WordViewModel.Factory(wordRepository)
    }

    var onNextClick: (() -> Unit)? = null
    var onPreviousClick: (() -> Unit)? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.nextStep2Button.setOnClickListener {
            wordViewModel.synonym = binding.synonymET.text.toString()
            wordViewModel.antonym = binding.antonymET.text.toString()
            onNextClick?.invoke()
        }

        binding.backStep2Button.setOnClickListener {
            onPreviousClick?.invoke()
        }
    }
}

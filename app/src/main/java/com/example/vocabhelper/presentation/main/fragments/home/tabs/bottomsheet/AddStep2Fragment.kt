package com.example.vocabhelper.presentation.main.fragments.home.tabs.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.vocabhelper.R
import com.example.vocabhelper.data.api.APIService
import com.example.vocabhelper.data.repository.WordRepository
import com.example.vocabhelper.databinding.FragmentAddStep2Binding
import com.example.vocabhelper.domain.WordViewModel

class AddStep2Fragment : Fragment() {

    private val binding by lazy { FragmentAddStep2Binding.inflate(layoutInflater) }

    private val wordViewModel: WordViewModel by activityViewModels {
        WordViewModel.Factory(WordRepository(apiService = APIService.create()))
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

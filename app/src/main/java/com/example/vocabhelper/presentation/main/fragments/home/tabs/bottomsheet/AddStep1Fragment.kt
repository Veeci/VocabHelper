package com.example.vocabhelper.presentation.main.fragments.home.tabs.bottomsheet

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.vocabhelper.R
import com.example.vocabhelper.databinding.FragmentAddStep1Binding

class AddStep1Fragment : Fragment() {

    private val binding by lazy { FragmentAddStep1Binding.inflate(layoutInflater) }

    var onNextClick: (() -> Unit)? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.nextStep1Button.setOnClickListener {
            onNextClick?.invoke()
        }
    }

}
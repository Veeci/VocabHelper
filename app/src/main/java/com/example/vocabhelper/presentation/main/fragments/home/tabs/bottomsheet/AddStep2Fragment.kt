package com.example.vocabhelper.presentation.main.fragments.home.tabs.bottomsheet

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.vocabhelper.R
import com.example.vocabhelper.databinding.FragmentAddStep2Binding

class AddStep2Fragment : Fragment() {

    private val binding by lazy { FragmentAddStep2Binding.inflate(layoutInflater) }
    var onNextClick: (() -> Unit)? = null
    var onPreviousClick: (() -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.nextStep2Button.setOnClickListener {
            onNextClick?.invoke()
        }

        binding.backStep2Button.setOnClickListener {
            onPreviousClick?.invoke()
        }
    }
}
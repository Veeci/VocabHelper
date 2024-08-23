package com.example.vocabhelper.presentation.main.fragments.home.tabs

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.vocabhelper.R
import com.example.vocabhelper.databinding.FragmentWordsByCategoryBinding

class WordsByCategoryFragment : Fragment() {

    private val binding by lazy { FragmentWordsByCategoryBinding.inflate(layoutInflater) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return binding.root
    }

}
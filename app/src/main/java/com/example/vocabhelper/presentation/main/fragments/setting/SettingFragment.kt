package com.example.vocabhelper.presentation.main.fragments.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.vocabhelper.R
import com.example.vocabhelper.databinding.FragmentSettingBinding

class SettingFragment : Fragment() {

    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingBinding.inflate(inflater, container, false)

        setupFunction()

        return binding.root
    }

    private fun setupFunction() {
        binding.back.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

}
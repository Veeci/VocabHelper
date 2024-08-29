package com.example.vocabhelper.presentation.auth.fragments.tabs

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.vocabhelper.R
import com.example.vocabhelper.databinding.FragmentFirstBinding
import com.example.vocabhelper.databinding.FragmentSecondBinding

class SecondFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSecondBinding.inflate(layoutInflater, container, false)

        val viewPager = activity?.findViewById<ViewPager2>(R.id.onboardingViewpager)

        binding.nextButton.setOnClickListener {
            viewPager?.currentItem = 2
        }

        binding.backButton.setOnClickListener {
            viewPager?.currentItem = 1
        }

        binding.skip.setOnClickListener {
            findNavController().navigate(R.id.action_onboardingViewpagerFragment_to_fourthFragment)
        }

        return binding.root
    }

}
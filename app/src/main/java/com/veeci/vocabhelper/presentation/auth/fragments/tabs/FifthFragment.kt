package com.veeci.vocabhelper.presentation.auth.fragments.tabs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.veeci.vocabhelper.R
import com.veeci.vocabhelper.databinding.FragmentFifthBinding
import com.veeci.vocabhelper.databinding.FragmentFourthBinding
import com.veeci.vocabhelper.databinding.FragmentThirdBinding

class FifthFragment : Fragment() {

    private var _binding: FragmentFifthBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFifthBinding.inflate(layoutInflater, container, false)

        val viewPager = activity?.findViewById<ViewPager2>(R.id.onboardingViewpager)

        binding.nextButton.setOnClickListener {
            findNavController().navigate(R.id.action_onboardingViewpagerFragment_to_sixthFragment)
        }

        binding.backButton.setOnClickListener {
            viewPager?.currentItem = 3
        }

        binding.skip.setOnClickListener {
            findNavController().navigate(R.id.action_onboardingViewpagerFragment_to_sixthFragment)
        }

        return binding.root
    }

}
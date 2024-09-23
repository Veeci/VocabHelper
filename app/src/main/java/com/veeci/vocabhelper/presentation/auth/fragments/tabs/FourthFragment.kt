package com.veeci.vocabhelper.presentation.auth.fragments.tabs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import coil.load
import com.veeci.vocabhelper.R
import com.veeci.vocabhelper.databinding.FragmentFourthBinding

class FourthFragment : Fragment() {

    private var _binding: FragmentFourthBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFourthBinding.inflate(layoutInflater, container, false)

        val viewPager = activity?.findViewById<ViewPager2>(R.id.onboardingViewpager)

        binding.nextButton.setOnClickListener {
            viewPager?.currentItem = 4
        }

        binding.backButton.setOnClickListener {
            viewPager?.currentItem = 2
        }

        binding.skip.setOnClickListener {
            findNavController().navigate(R.id.action_onboardingViewpagerFragment_to_sixthFragment)
        }

        binding.banner.load(R.drawable.banner_banner4_onboarding)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
package com.example.vocabhelper.presentation.main.fragments.home.tabs.bottomsheet

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.activityViewModels
import com.example.vocabhelper.data.api.APIService
import com.example.vocabhelper.data.implementation.WordRepoImplementation
import com.example.vocabhelper.databinding.FragmentBottomSheetBinding
import com.example.vocabhelper.presentation.ViewPagerAdapter
import com.example.vocabhelper.domain.WordViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentBottomSheetBinding? = null
    private val binding get() = _binding!!

    private val apiService by lazy { APIService.create() }

    private val wordRepository by lazy { WordRepoImplementation(apiService) }

    private val wordViewModel: WordViewModel by activityViewModels {
        WordViewModel.Factory(wordRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBottomSheetBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewpager(view)
    }

    private fun setupViewpager(view: View) {
        val step1Fragment = AddStep1Fragment()
        val step2Fragment = AddStep2Fragment()
        val step3Fragment = AddStep3Fragment()

        val fragmentList = arrayListOf<Fragment>(
            step1Fragment,
            step2Fragment,
            step3Fragment
        )

        val adapter = ViewPagerAdapter(
            fragmentList,
            requireActivity().supportFragmentManager,
            lifecycle
        )

        val viewpager = binding.bottomsheetViewpager
        viewpager.adapter = adapter
        viewpager.isUserInputEnabled = false
        viewpager.offscreenPageLimit = 3

        step1Fragment.onNextClick = {
            viewpager.currentItem = 1
        }

        step2Fragment.onNextClick = {
            viewpager.currentItem = 2
        }

        step2Fragment.onPreviousClick = {
            viewpager.currentItem = 0
        }

        step3Fragment.onPreviousClick = {
            viewpager.currentItem = 1
        }

        step3Fragment.onCompleteClick = {
            setupFinishButton()
        }
    }

    private fun setupFinishButton() {


        dismiss()
    }



}
package com.example.vocabhelper.presentation.main.fragments.home.tabs.bottomsheet

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.activityViewModels
import com.example.vocabhelper.R
import com.example.vocabhelper.data.api.APIService
import com.example.vocabhelper.data.database.WordDatabase
import com.example.vocabhelper.data.database.WordEntity
import com.example.vocabhelper.data.repository.WordRepository
import com.example.vocabhelper.databinding.FragmentBottomSheetBinding
import com.example.vocabhelper.presentation.ViewPagerAdapter
import com.example.vocabhelper.ui.viewmodel.WordViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentBottomSheetBinding? = null
    private val binding get() = _binding!!

    private var wordET: AppCompatTextView? = null
    private var phoneticET: AppCompatTextView? = null
    private var meaningET: AppCompatTextView? = null
    private var synonymET: AppCompatTextView? = null
    private var antonymET: AppCompatTextView? = null
    private var collocationET: AppCompatTextView? = null
    private var exampleET: AppCompatTextView? = null

    private val wordViewModel: WordViewModel by activityViewModels {
        WordViewModel.Factory(
            WordRepository(
                apiService = APIService.create(),
                wordDAO = WordDatabase.getDatabase(requireContext()).wordDao()
            )
        )
    }

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
////        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
//
//    }

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
        val word = wordET?.text.toString()
        val phonetic = phoneticET?.text.toString()
        val meaning = meaningET?.text.toString()
        val synonym = synonymET?.text.toString().split(",")
        val antonym = antonymET?.text.toString().split(",")
        val collocation = collocationET?.text.toString().split(",")
        val example = exampleET?.text.toString().split(",")

        val wordEntity = WordEntity(
            word = word,
            phonetic = phonetic,
            meaning = meaning,
            category = "category_placeholder",
            collocation = collocation,
            synonym = synonym,
            antonym = antonym,
            example = example,
            phonetics = null,
            meanings = null,
            license = null,
            sourceUrls = null
        )

        wordViewModel.insertWord(wordEntity)

        dismiss()
    }
}
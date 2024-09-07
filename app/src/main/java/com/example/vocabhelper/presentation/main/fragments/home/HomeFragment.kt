package com.example.vocabhelper.presentation.main.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import com.example.vocabhelper.R
import com.example.vocabhelper.data.api.APIService
import com.example.vocabhelper.data.implementation.WordRepoImplementation
import com.example.vocabhelper.databinding.FragmentHomeBinding
import com.example.vocabhelper.domain.WordViewModel
import com.example.vocabhelper.presentation.main.fragments.home.tabs.adapter.HomeTabLayout
import com.example.vocabhelper.presentation.main.fragments.home.tabs.adapter.WordAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class HomeFragment : Fragment() {

    private val binding by lazy{FragmentHomeBinding.inflate(layoutInflater)}

    private val apiService by lazy { APIService.create() }

    private val wordRepository by lazy { WordRepoImplementation(apiService) }

    private val wordViewModel: WordViewModel by activityViewModels {
        WordViewModel.Factory(wordRepository)
    }

    private lateinit var viewpager: ViewPager2
    private lateinit var tabLayout: TabLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        wordViewModel.fetchWords()
        setupFunction()
    }

    private fun setupFunction()
    {
        tabLayout = binding.homeTabLayout
        viewpager = binding.homeViewPager

        val adapter = HomeTabLayout(this)
        viewpager.adapter = adapter
        viewpager.offscreenPageLimit = 2

        TabLayoutMediator(tabLayout, viewpager) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.tab_all_words)
                1 -> tab.text = getString(R.string.tab_categories)
            }
        }.attach()
    }
}
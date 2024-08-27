package com.example.vocabhelper.presentation.main.fragments.home.tabs.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.vocabhelper.presentation.main.fragments.home.tabs.AllWordsFragment
import com.example.vocabhelper.presentation.main.fragments.home.tabs.WordsByCategoryFragment

class HomeTabLayout(fragment: Fragment): FragmentStateAdapter(fragment)
{
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> AllWordsFragment()
            1 -> WordsByCategoryFragment()
            else -> throw IllegalArgumentException("Invalid position: $position")
        }
    }

}
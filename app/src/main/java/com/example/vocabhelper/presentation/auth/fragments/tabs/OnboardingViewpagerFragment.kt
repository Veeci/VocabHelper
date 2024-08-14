package com.example.vocabhelper.presentation.auth.fragments.tabs

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.example.vocabhelper.R
import com.example.vocabhelper.presentation.ViewPagerAdapter
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator

class OnboardingViewpagerFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_onboarding_viewpager, container, false)

        val fragmentList = listOf<Fragment>(
            FirstFragment(),
            SecondFragment(),
            ThirdFragment()
        )

        val adapter = ViewPagerAdapter(
            fragmentList,
            requireActivity().supportFragmentManager,
            lifecycle
        )

        val view_pager2 = view.findViewById<ViewPager2>(R.id.onboardingViewpager)
        val dotsIndicator = view.findViewById<DotsIndicator>(R.id.dots_indicator)

        view_pager2.adapter = adapter
        dotsIndicator.attachTo(view_pager2)

        return view
    }

}
package com.veeci.vocabhelper.presentation.auth.fragments.tabs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator
import com.veeci.vocabhelper.R
import com.veeci.vocabhelper.presentation.ViewPagerAdapter

class OnboardingViewpagerFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_onboarding_viewpager, container, false)

        val fragmentList = listOf<Fragment>(
            FirstFragment(),
            SecondFragment(),
            ThirdFragment(),
            FourthFragment(),
            FifthFragment()
        )

        val adapter = ViewPagerAdapter(
            fragmentList,
            requireActivity().supportFragmentManager,
            lifecycle
        )

        val view_pager2 = view.findViewById<ViewPager2>(R.id.onboardingViewpager)
        val dotsIndicator = view.findViewById<DotsIndicator>(R.id.dots_indicator)

        view_pager2.adapter = adapter
        view_pager2.offscreenPageLimit = 5
        dotsIndicator.attachTo(view_pager2)

        return view
    }

}
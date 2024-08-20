package com.example.vocabhelper.presentation.main.fragments

import android.content.res.ColorStateList
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.activityViewModels
import coil.load
import com.example.vocabhelper.R
import com.example.vocabhelper.databinding.FragmentMainBinding
import com.example.vocabhelper.domain.AuthViewModel
import com.example.vocabhelper.presentation.ViewPagerAdapter
import com.example.vocabhelper.presentation.main.fragments.home.HomeFragment
import com.example.vocabhelper.presentation.main.fragments.home.tabs.bottomsheet.BottomSheetFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private val authViewModel: AuthViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Apply insets to the bottom bar
        ViewCompat.setOnApplyWindowInsetsListener(binding.bottomBar.root) { v, insets ->
            val systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                systemBarsInsets.left,
                systemBarsInsets.top,
                systemBarsInsets.right,
                systemBarsInsets.bottom
            )
            insets
        }

        authViewModel.profilePicUrl.observe(viewLifecycleOwner){ url ->
            binding.topBar.profilePicture.load(url)
        }

        setupFunction(view)
    }

    private fun setupFunction(view: View)
    {
        val fragmentList = arrayListOf<Fragment>(
            HomeFragment(),
            SearchFragment(),
            FocusFragment(),
            ProfileFragment()
        )

        val adapter =ViewPagerAdapter(
            fragmentList,
            requireActivity().supportFragmentManager,
            lifecycle
        )

        val viewPager =binding.mainViewPager
        viewPager.adapter = adapter
        viewPager.isUserInputEnabled = false

        val bottomNavigation = view.findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigation.itemIconTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.text_main))
        bottomNavigation.itemTextColor = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.text_main))
        bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_home -> {
                    viewPager.currentItem = 0
                    view.findViewById<AppCompatTextView>(R.id.headerTitle).text = "Index"
                    true
                }
                R.id.menu_search -> {
                    viewPager.currentItem = 1
                    view.findViewById<AppCompatTextView>(R.id.headerTitle).text = "Search"
                    true
                }
                R.id.menu_focus -> {
                    viewPager.currentItem = 2
                    view.findViewById<AppCompatTextView>(R.id.headerTitle).text = "Pomodoro"
                    true
                }
                R.id.menu_profile -> {
                    viewPager.currentItem = 3
                    view.findViewById<AppCompatTextView>(R.id.headerTitle).text = "Profile"
                    true
                }

                else -> false
            }
        }

        val addButton = view.findViewById<FloatingActionButton>(R.id.addWordFAB)
        addButton.setOnClickListener {
            val bottomSheetFragment = BottomSheetFragment()
            bottomSheetFragment.show(requireActivity().supportFragmentManager, bottomSheetFragment.tag)
        }

    }
}
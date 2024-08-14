package com.example.vocabhelper.presentation.main.fragments

import android.content.res.ColorStateList
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.vocabhelper.R
import com.example.vocabhelper.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.bottomNavigation.background = null
        binding.bottomNavigation.menu.getItem(2).isEnabled = false
        binding.bottomNavigation.itemIconTintList = ColorStateList.valueOf(resources.getColor(R.color.text_main))
        binding.bottomNavigation.itemTextColor = ColorStateList.valueOf(resources.getColor(R.color.text_main))

        binding.bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when(item.itemId) {
                R.id.menu_home -> {

                    true
                }
                R.id.menu_search -> {

                    true
                }
                R.id.menu_focus -> {

                    true
                }
                R.id.menu_profile -> {

                    true
                }
                else -> false
            }
        }
    }
}
package com.example.vocabhelper.presentation.main.fragments.setting

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.base.BaseActivity
import com.example.vocabhelper.R
import com.example.vocabhelper.databinding.FragmentSettingBinding
import com.example.vocabhelper.domain.SettingViewModel

class SettingFragment : Fragment() {

    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!

    private val settingViewModel: SettingViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingBinding.inflate(inflater, container, false)

        setupFunction()

        return binding.root
    }

    private fun setupFunction() {
        binding.back.setOnClickListener {
            requireActivity().onBackPressed()
        }

        binding.changeAppLanguage.setOnClickListener {
            val dialog = Dialog(requireContext())
            dialog.setContentView(R.layout.language_setting_dialog)
            dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            dialog.setCancelable(true)

            val english = dialog.findViewById<LinearLayout>(R.id.language_english)
            val vietnamese = dialog.findViewById<LinearLayout>(R.id.language_vietnamese)
            val japanese = dialog.findViewById<LinearLayout>(R.id.language_japanese)
            val french = dialog.findViewById<LinearLayout>(R.id.language_french)

            english.setOnClickListener {
                (activity as? BaseActivity)?.changeLanguage("en")
                dialog.dismiss()
            }

            vietnamese.setOnClickListener {
                (activity as? BaseActivity)?.changeLanguage("vi")
                dialog.dismiss()
            }

            japanese.setOnClickListener {
                (activity as? BaseActivity)?.changeLanguage("ja")
                dialog.dismiss()
            }

            french.setOnClickListener {
                (activity as? BaseActivity)?.changeLanguage("fr")
                dialog.dismiss()
            }

            dialog.show()
        }
    }

}
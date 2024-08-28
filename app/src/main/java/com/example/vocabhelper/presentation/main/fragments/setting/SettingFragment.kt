package com.example.vocabhelper.presentation.main.fragments.setting

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.NumberPicker
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.base.BaseActivity
import com.example.vocabhelper.R
import com.example.vocabhelper.databinding.FragmentSettingBinding
import com.example.vocabhelper.domain.AuthViewModel
import com.example.vocabhelper.domain.SettingViewModel

class SettingFragment : Fragment() {

    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!

    private val settingViewModel: SettingViewModel by activityViewModels()
    private val authViewModel: AuthViewModel by activityViewModels()

    companion object{
        const val PREFS_NAME = "VocabHelperPrefs"
        const val PREF_REMEMBER_EMAIL = "RememberEmail"
        const val PREF_REMEMBER_PASSWORD = "RememberPassword"
    }

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
            dialog.window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
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

        binding.setFocusTime.setOnClickListener {
            val dialog = Dialog(requireContext())
            dialog.setContentView(R.layout.time_setting_dialog)
            dialog.window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            dialog.setCancelable(true)

            val focusPicker = dialog.findViewById<NumberPicker>(R.id.focusPicker)
            val longBreakPicker = dialog.findViewById<NumberPicker>(R.id.longBreakPicker)
            val shortBreakPicker = dialog.findViewById<NumberPicker>(R.id.shortBreakPicker)

            // Set up NumberPicker ranges and values
            focusPicker.minValue = 1
            focusPicker.maxValue = 60
            focusPicker.value = settingViewModel.focusTime / 60

            longBreakPicker.minValue = 1
            longBreakPicker.maxValue = 30
            longBreakPicker.value = settingViewModel.longBreakTime / 60

            shortBreakPicker.minValue = 1
            shortBreakPicker.maxValue = 30
            shortBreakPicker.value = settingViewModel.shortBreakTime / 60

            dialog.findViewById<View>(R.id.saveButton).setOnClickListener {
                settingViewModel.setTime(
                    focusPicker.value,
                    shortBreakPicker.value,
                    longBreakPicker.value
                )
                dialog.dismiss()
            }

            dialog.findViewById<View>(R.id.cancelButton).setOnClickListener {
                dialog.dismiss()
            }

            dialog.show()
        }

        rememberPassword(authViewModel.email.value.toString(), authViewModel.password.value.toString())
    }

    private fun rememberPassword(email: String, password: String)
    {
        val prefs = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        binding.rememberPasswordSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                editor.putString(PREF_REMEMBER_EMAIL, email)
                editor.putString(PREF_REMEMBER_PASSWORD, password)
            } else {
                editor.remove(PREF_REMEMBER_EMAIL)
                editor.remove(PREF_REMEMBER_PASSWORD)
            }
        }

        editor.apply()
    }
}
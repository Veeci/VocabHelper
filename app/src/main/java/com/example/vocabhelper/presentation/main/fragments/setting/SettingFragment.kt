package com.example.vocabhelper.presentation.main.fragments.setting

import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
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

    companion object {
        const val PREFS_NAME = "VocabHelperPrefs"
        const val PREF_REMEMBER_EMAIL = "RememberEmail"
        const val PREF_REMEMBER_PASSWORD = "RememberPassword"
        const val PREF_TOGGLE_SWITCH = "ToggleSwitch"
    }

    private lateinit var prefs: SharedPreferences
    private lateinit var email: String
    private lateinit var password: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingBinding.inflate(inflater, container, false)

        prefs = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val savedToggleSwitch = prefs.getBoolean(PREF_TOGGLE_SWITCH, false)

        email = activity?.intent?.getStringExtra("email") ?: ""
        password = activity?.intent?.getStringExtra("password") ?: ""

        settingViewModel.setToggleSwitch(savedToggleSwitch)

        setupFunction()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        settingViewModel.toggleSwitch.observe(viewLifecycleOwner) { isChecked ->
            binding.rememberPasswordSwitch.isChecked = isChecked
        }

        authViewModel.email.observe(viewLifecycleOwner) { email ->
            authViewModel.password.observe(viewLifecycleOwner) { password ->

            }
        }
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

            setupLanguageSelectionDialog(dialog)
        }

        binding.setFocusTime.setOnClickListener {
            val dialog = Dialog(requireContext())
            dialog.setContentView(R.layout.time_setting_dialog)
            dialog.window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            dialog.setCancelable(true)

            setupTimeSettingDialog(dialog)
        }

        binding.rememberPasswordSwitch.setOnCheckedChangeListener { _, isChecked ->
            settingViewModel.setToggleSwitch(isChecked)
            prefs.edit().putBoolean(PREF_TOGGLE_SWITCH, isChecked).apply()

            if (isChecked) {
                prefs.edit().putString(PREF_REMEMBER_EMAIL, email).apply()
                prefs.edit().putString(PREF_REMEMBER_PASSWORD, password).apply()
                Log.d("SharedPreference", "Saving email: ${prefs.getString(PREF_REMEMBER_EMAIL, "")} " +
                        "and password: ${prefs.getString(PREF_REMEMBER_PASSWORD, "")}.")
            } else {
                prefs.edit().remove(PREF_REMEMBER_EMAIL).apply()
                prefs.edit().remove(PREF_REMEMBER_PASSWORD).apply()
                Log.d("SharedPreference", "Removing email and password.")
            }
            //Log.d("ToggleSwitchState", "is check: $isChecked")
            //Log.d("SharedPreferences", "toggle switch: ${prefs.getBoolean(PREF_TOGGLE_SWITCH, false)}")
        }
    }

    private fun setupLanguageSelectionDialog(dialog: Dialog) {
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

    private fun setupTimeSettingDialog(dialog: Dialog) {
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
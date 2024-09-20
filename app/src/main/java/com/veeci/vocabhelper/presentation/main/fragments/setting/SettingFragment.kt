package com.veeci.vocabhelper.presentation.main.fragments.setting

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
import com.veeci.base.BaseActivity
import com.veeci.vocabhelper.R
import com.veeci.vocabhelper.databinding.FragmentSettingBinding
import com.veeci.vocabhelper.domain.SettingViewModel

class SettingFragment : Fragment() {

    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!

    private val settingViewModel: SettingViewModel by activityViewModels()

    companion object {
        const val PREFS_NAME = "VocabHelperPrefs"
        const val PREF_REMEMBER_EMAIL = "RememberEmail"
        const val PREF_REMEMBER_PASSWORD = "RememberPassword"
        const val PREF_TOGGLE_SWITCH = "ToggleSwitch"
    }

    private lateinit var prefs: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingBinding.inflate(inflater, container, false)

        prefs = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val savedToggleSwitch = prefs.getBoolean(PREF_TOGGLE_SWITCH, false)

        settingViewModel.setToggleSwitch(savedToggleSwitch)

        setupFunction()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        settingViewModel.toggleSwitch.observe(viewLifecycleOwner) { isChecked ->
            binding.rememberPasswordSwitch.isChecked = isChecked
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
            Log.d("SettingFragment", "Toggle switch state changed to ${prefs.getBoolean(PREF_TOGGLE_SWITCH, false)}")
        }
    }

    private fun setupLanguageSelectionDialog(dialog: Dialog) {
        val english = dialog.findViewById<LinearLayout>(R.id.language_english)
        val vietnamese = dialog.findViewById<LinearLayout>(R.id.language_vietnamese)
        val japanese = dialog.findViewById<LinearLayout>(R.id.language_japanese)
        val french = dialog.findViewById<LinearLayout>(R.id.language_french)

        english.setOnClickListener {
            setConfirmDialog {
                (activity as? BaseActivity)?.changeLanguage("en")
                dialog.dismiss()
            }
        }

        vietnamese.setOnClickListener {
            setConfirmDialog {
                (activity as? BaseActivity)?.changeLanguage("vi")
                dialog.dismiss()
            }
        }

        japanese.setOnClickListener {
            setConfirmDialog {
                (activity as? BaseActivity)?.changeLanguage("ja")
                dialog.dismiss()
            }
        }

        french.setOnClickListener {
            setConfirmDialog {
                (activity as? BaseActivity)?.changeLanguage("fr")
                dialog.dismiss()
            }
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

    private fun setConfirmDialog(onConfirm: () -> Unit)
    {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.confirm_changelanguage_dialog)
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.setCancelable(true)

        dialog.findViewById<View>(R.id.okay_text).setOnClickListener {
            onConfirm()
            dialog.dismiss()
        }

        dialog.findViewById<View>(R.id.cancel_text).setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
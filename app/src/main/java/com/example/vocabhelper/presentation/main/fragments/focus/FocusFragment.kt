package com.example.vocabhelper.presentation.main.fragments.focus

import android.app.Dialog
import android.os.Bundle
import android.text.format.DateUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.vocabhelper.R
import com.example.vocabhelper.databinding.FragmentFocusBinding
import com.example.vocabhelper.domain.SettingViewModel

class FocusFragment : Fragment() {

    private val binding by lazy{FragmentFocusBinding.inflate(layoutInflater)}

    private val settingViewModel: SettingViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setupObservers()
        setupListeners()

        return binding.root
    }

    private fun setupObservers() {
        settingViewModel.timeRemaining.observe(viewLifecycleOwner, Observer { timeRemaining ->
            binding.circularTimer.progress = (timeRemaining / 1000).toInt()
            binding.countdownTV.text = formatTime(timeRemaining)
        })

        settingViewModel.isTimerRunning.observe(viewLifecycleOwner, Observer { isRunning ->
            binding.startCountDownButton.text = if (isRunning) getString(R.string.pause) else getString(R.string.start)
        })

        settingViewModel.isFocusSession.observe(viewLifecycleOwner, Observer { isFocusSession ->
            binding.circularTimer.max = if (isFocusSession) {
                settingViewModel.focusTime
            } else {
                if (settingViewModel.sessionCount.value?.rem(4)  == 0)
                {
                    settingViewModel.longBreakTime
                } else {
                    settingViewModel.shortBreakTime
                }
            }
        })
    }

    private fun setupListeners() {
        binding.startCountDownButton.setOnClickListener {
            if (settingViewModel.isTimerRunning.value == true) {
                settingViewModel.pauseTimer()
            } else {
                settingViewModel.startTimer()
            }
        }

        binding.endCountdownButton.setOnClickListener {
            if (settingViewModel.isTimerRunning.value == true) {
                showEndDialog()
            } else {
                showNotStartedDialog()
            }
        }
    }

    private fun showEndDialog() {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.giveup_dialog)
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.setCancelable(false)
        dialog.window?.attributes?.windowAnimations = R.style.animation

        val okayText = dialog.findViewById<TextView>(R.id.okay_text)
        val cancelText = dialog.findViewById<TextView>(R.id.cancel_text)

        okayText.setOnClickListener {
            dialog.dismiss()
            settingViewModel.resetTimer()
        }

        cancelText.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun showNotStartedDialog() {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.not_started_dialog)
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.setCancelable(true)
        dialog.window?.attributes?.windowAnimations = R.style.animation

        val okayText = dialog.findViewById<TextView>(R.id.okay_text)
        okayText.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun formatTime(millis: Long): String {
        val seconds = millis / 1000
        return DateUtils.formatElapsedTime(seconds)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        settingViewModel.resetTimer()
    }
}

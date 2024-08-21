package com.example.vocabhelper.presentation.main.fragments.focus

import android.icu.text.DecimalFormat
import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import com.example.vocabhelper.databinding.FragmentFocusBinding

class FocusFragment : Fragment() {

    private var _binding: FragmentFocusBinding? = null
    private val binding get() = _binding!!

    private lateinit var countDownTimer: CountDownTimer

    // Pomodoro timing configurations
    private val focusTime = 25 * 60 // 25 minutes in seconds
    private val shortBreakTime = 5 * 60 // 5 minutes in seconds
    private val longBreakTime = 10 * 60 // 10 minutes in seconds

    private var sessionCount = 0 // To count the number of focus sessions
    private var timeRemaining = focusTime * 1000L // Time remaining in milliseconds
    private var isTimerRunning = false // Track whether the timer is running or paused
    private var isFocusSession = true // Track whether it's a focus session or break

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFocusBinding.inflate(layoutInflater, container, false)

        setupFunction()

        return binding.root
    }

    private fun setupFunction() {
        binding.circularTimer.max = focusTime
        binding.circularTimer.progress = focusTime

        binding.startCountDownButton.setOnClickListener {
            if (isTimerRunning) {
                pauseTimer()
            } else {
                startTimer()
            }
        }

        binding.endCountdownButton.setOnClickListener {
            resetTimer()
        }
    }

    private fun startTimer() {
        countDownTimer = object : CountDownTimer(timeRemaining, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeRemaining = millisUntilFinished
                val secondsRemaining = (millisUntilFinished / 1000).toInt()
                binding.circularTimer.progress = secondsRemaining
                timeFormat(secondsRemaining, binding.countdownTV)
            }

            override fun onFinish() {
                sessionCount++
                if (isFocusSession) {
                    // Switch to break
                    if (sessionCount % 4 == 0) {
                        // Long break after 3 focus sessions
                        timeRemaining = longBreakTime * 1000L
                        binding.circularTimer.max = longBreakTime
                    } else {
                        // Short break
                        timeRemaining = shortBreakTime * 1000L
                        binding.circularTimer.max = shortBreakTime
                    }
                } else {
                    // Switch back to focus
                    timeRemaining = focusTime * 1000L
                    binding.circularTimer.max = focusTime
                }

                isFocusSession = !isFocusSession
                startTimer() // Automatically start the next session
            }
        }.start()

        binding.startCountDownButton.text = "Pause"
        isTimerRunning = true
    }

    private fun pauseTimer() {
        countDownTimer.cancel()
        binding.startCountDownButton.text = "Continue"
        isTimerRunning = false
    }

    private fun resetTimer() {
        countDownTimer.cancel()
        sessionCount = 0
        timeRemaining = focusTime * 1000L
        binding.circularTimer.max = focusTime
        binding.circularTimer.progress = focusTime
        timeFormat(focusTime, binding.countdownTV)
        binding.startCountDownButton.text = "Start"
        isTimerRunning = false
        isFocusSession = true
    }

    private fun timeFormat(secondLeft: Int, countdownTV: AppCompatTextView) {
        val decimalFormat = DecimalFormat("00")
        val hour = secondLeft / 3600
        val minute = (secondLeft % 3600) / 60
        val second = secondLeft % 60

        val timeFormat = "${decimalFormat.format(hour)}:${decimalFormat.format(minute)}:${decimalFormat.format(second)}"
        countdownTV.text = timeFormat
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        countDownTimer.cancel()
    }
}

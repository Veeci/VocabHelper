package com.example.vocabhelper.presentation.main.fragments

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
    private val countdownTime = 2700 // in seconds
    private val clockTime = (countdownTime * 1000).toLong()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFocusBinding.inflate(layoutInflater, container, false)

        setupFunction()

        return binding.root
    }


    private fun setupFunction() {
        countDownTimer = object : CountDownTimer(clockTime, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsRemaining = (millisUntilFinished / 1000).toInt()
                binding.circularTimer.progress = secondsRemaining
                timeFormat(secondsRemaining, binding.countdownTV)
            }

            override fun onFinish() {
                binding.circularTimer.progress = 0
                timeFormat(0, binding.countdownTV)
            }
        }
        binding.circularTimer.max = countdownTime
        binding.circularTimer.progress = countdownTime
        countDownTimer.start()

        binding.endCountdownButton.setOnClickListener {
            countDownTimer.cancel()
            countDownTimer.start()
            binding.circularTimer.progress = countdownTime
        }
    }

    private fun timeFormat(secondLeft: Int, countdownTV: AppCompatTextView) {
        binding.circularTimer.progress = secondLeft
        val decimalFormat = DecimalFormat("00")
        val hour = secondLeft / 3600
        val minute = (secondLeft % 3600) / 60
        val second = secondLeft % 60

        val timeFormat = decimalFormat.format(hour) + ":" + decimalFormat.format(minute) + ":" + decimalFormat.format(second)

        countdownTV.text = timeFormat
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        countDownTimer.cancel()
    }

}
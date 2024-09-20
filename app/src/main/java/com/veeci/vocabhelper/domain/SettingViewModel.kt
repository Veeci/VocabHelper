package com.veeci.vocabhelper.domain

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SettingViewModel : ViewModel() {

    private val _timeRemaining = MutableLiveData<Long>()
    val timeRemaining: LiveData<Long> get() = _timeRemaining

    private val _isTimerRunning = MutableLiveData<Boolean>()
    val isTimerRunning: LiveData<Boolean> get() = _isTimerRunning

    private val _isFocusSession = MutableLiveData<Boolean>()
    val isFocusSession: LiveData<Boolean> get() = _isFocusSession

    private val _sessionCount = MutableLiveData<Int>()
    val sessionCount: LiveData<Int> get() = _sessionCount

    private val _toggleSwitch = MutableLiveData<Boolean>()
    val toggleSwitch: LiveData<Boolean> get() = _toggleSwitch

    fun setToggleSwitch(toggleSwitch: Boolean)
    {
        _toggleSwitch.postValue(toggleSwitch)
    }

    private var countDownTimer: CountDownTimer? = null

    // Pomodoro timing configurations in seconds
    var focusTime = 25 * 60
    var shortBreakTime = 5 * 60
    var longBreakTime = 10 * 60

    fun setTime(focusTime: Int, shortBreakTime: Int, longBreakTime: Int)
    {
        this.focusTime = focusTime * 60
        this.shortBreakTime = shortBreakTime * 60
        this.longBreakTime = longBreakTime * 60

        resetTimer()
    }

    init {
        _timeRemaining.value = focusTime * 1000L
        _isTimerRunning.value = false
        _isFocusSession.value = true
        _sessionCount.value = 0
    }

    fun startTimer() {
        countDownTimer?.cancel()

        countDownTimer = object : CountDownTimer(_timeRemaining.value!!, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                _timeRemaining.value = millisUntilFinished
            }

            override fun onFinish() {
                handleTimerFinish()
            }
        }.start()

        _isTimerRunning.value = true
    }

    fun pauseTimer() {
        countDownTimer?.cancel()
        _isTimerRunning.value = false
    }

    fun resetTimer() {
        countDownTimer?.cancel()
        _sessionCount.value = 0
        _timeRemaining.value = focusTime * 1000L
        _isFocusSession.value = true
        _isTimerRunning.value = false
    }

    private fun handleTimerFinish() {
        _sessionCount.value = (_sessionCount.value ?: 0) + 1
        _isFocusSession.value = !(_isFocusSession.value ?: true)

        _timeRemaining.value = if (_isFocusSession.value == true) {
            if (_sessionCount.value!! % 4 == 0) longBreakTime * 1000L else shortBreakTime * 1000L
        } else {
            focusTime * 1000L
        }

        startTimer()
    }
}
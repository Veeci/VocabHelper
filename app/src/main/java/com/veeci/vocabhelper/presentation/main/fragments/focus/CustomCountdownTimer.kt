package com.veeci.vocabhelper.presentation.main.fragments.focus

import android.os.CountDownTimer

class CustomCountdownTimer(
    private val millisInFutre: Long,
    private val countDownInterval: Long
) {
    private var millisUntilFinished = millisInFutre
    private var timer = InternalTimer(this, millisInFutre, countDownInterval)
    private var isRunning = false

    var onTick: ((millisUntilFinished: Long) -> Unit)? = null
    var onFinish: (() -> Unit)? = null


    private class InternalTimer(
        private val parent: CustomCountdownTimer,
        millisInFutre: Long,
        countDownInterval: Long
    ) : CountDownTimer(millisInFutre, countDownInterval) {
        var millisUntilFinished = parent.millisUntilFinished

        override fun onTick(millisUntilFinished: Long) {
            this.millisUntilFinished = millisUntilFinished
            parent.onTick?.invoke(millisUntilFinished)
        }

        override fun onFinish() {
            millisUntilFinished = 0
            parent.onFinish?.invoke()
        }
    }

    fun pauseTimer()
    {
        timer.cancel()
        isRunning = false
    }

    fun resumeTimer()
    {
        if(!isRunning && timer.millisUntilFinished > 0)
        {
            timer = InternalTimer(this, timer.millisUntilFinished, countDownInterval)
            startTimer()
        }
    }

    fun startTimer()
    {
        timer.start()
        isRunning = true
    }

    fun restartTimer()
    {
        timer.cancel()
        timer = InternalTimer(this, millisInFutre, countDownInterval)
        startTimer()
    }

    fun destroyTimer()
    {
        timer.cancel()
    }
}
package com.spacesofting.weshare.utils

import android.content.Context
import android.os.CountDownTimer
import android.widget.TextView

class TimerUtils {

    fun timeToMillis(time: String?): Long {
        var result: Long = 0

        if (!time.isNullOrEmpty()) {
            val units = time!!.split(":")
            val hours = units[0].toLong()
            val minutes = units[1].toLong()
            val seconds = units[2].toLong()
            result = (hours * 60 + minutes * 60 + seconds) * 1000
        }
        return result
    }

    fun getTimer(context: Context?, millisInFuture: Long, countDownInterval: Long, idMessage: Int? = null, listener: OnTimerFinishListener? = null, textView: TextView? = null): CountDownTimer {
        return object : CountDownTimer(millisInFuture, countDownInterval) {
            override fun onFinish() {
                listener?.onFinish()
            }

            override fun onTick(millisUntilFinished: Long) {
                val countDownSeconds = millisUntilFinished / 1000
                val hour = countDownSeconds / 3600
                val min = (countDownSeconds % 3600) / 60
                val sec = countDownSeconds % 60

                val time = String.format("%02d:%02d:%02d", hour, min, sec)
                idMessage?.let { textView?.text = context?.resources?.getString(it, time) }
            }
        }
    }

    interface OnTimerFinishListener {
        fun onFinish()
    }
}
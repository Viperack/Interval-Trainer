package com.example.intervaltrainer.presentation

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (null != context) {
            WakeLocker.acquire(context)
            val message = intent?.getStringExtra("EXTRA_MESSAGE") ?: return
            println("Alarm triggered: $message")
        } else {
            println("ERROR: No context received")
        }

    }
}
package com.example.intervaltrainer.presentation

import android.content.Context
import android.os.PowerManager
import android.os.PowerManager.WakeLock


object WakeLocker {
    private var wakeLock: WakeLock? = null
    fun acquire(ctx: Context) {
        wakeLock?.let { wakeLock -> wakeLock.release()  }
        val pm = ctx.getSystemService(Context.POWER_SERVICE) as PowerManager
        wakeLock = pm.newWakeLock(
            PowerManager.FULL_WAKE_LOCK or
                    PowerManager.ACQUIRE_CAUSES_WAKEUP or
                    PowerManager.ON_AFTER_RELEASE, MainActivity.APP_TAG
        )
        wakeLock?.let { wakeLock -> wakeLock.acquire(10*60*1000L /*10 minutes*/)  }
        println("WakeLock activated")
    }

    fun release() {
        if (wakeLock != null) wakeLock!!.release()
        wakeLock = null
    }

    fun isLocked(): Boolean {
        return null != wakeLock
    }
}
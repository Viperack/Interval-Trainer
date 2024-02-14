package com.example.intervaltrainer.presentation

import kotlin.math.roundToLong

class Util {
    companion object {
        fun displayTimeString(milliSeconds: Long): String {
            val partialSeconds = (milliSeconds % 1000 /* Second */)
            val seconds = ((milliSeconds % (60 * 1000) /* Minute */) / 1000.0).roundToLong()
            val minutes = (milliSeconds/ (1000.0 * 60)).toLong()

            var outputString = ""
            if (minutes < 10){
                outputString += "0"
            }
            outputString += "$minutes : "
            if (seconds < 10){
                outputString += "0"
            }
            outputString += "$seconds"


            return outputString
        }
    }
}
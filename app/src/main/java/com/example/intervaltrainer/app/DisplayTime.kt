package com.example.intervaltrainer.app

import kotlin.math.roundToLong

class DisplayTime {
    companion object {
        fun formatMilliSecondsToString(milliSeconds: Int): String {
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
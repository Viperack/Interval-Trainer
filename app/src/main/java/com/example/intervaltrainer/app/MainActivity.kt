/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.intervaltrainer.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController


val exerciseDuration: IntBox = IntBox(5000)
var restDuration: IntBox = IntBox(3000)
var repetitions: IntBox = IntBox(3)
var timeStamp: Long = 0
const val titleFontSize: Int = 10


//class ScreenReceiver : BroadcastReceiver() {
//    override fun onReceive(context: Context, intent: Intent) {
//        if (intent.action == Intent.ACTION_SCREEN_OFF) {
//            // DO WHATEVER YOU NEED TO DO HERE
//            Log.d(TAG, "SCREEN TURNING OFF")
//            wasScreenOn = false
//        } else if (intent.action == Intent.ACTION_SCREEN_ON) {
//            // AND DO WHATEVER YOU NEED TO DO HERE
//            Log.d(TAG, "SCREEN TURNING ON")
//            wasScreenOn = true
//        }
//    }
//
//    companion object {
//        // THANKS JASON
//        var wasScreenOn = true
//    }
//}


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        // INITIALIZE RECEIVER
//        val filter = IntentFilter(Intent.ACTION_SCREEN_ON)
//        filter.addAction(Intent.ACTION_SCREEN_OFF)
//        val mReceiver: BroadcastReceiver = ScreenReceiver()
//        registerReceiver(mReceiver, filter)

        super.onCreate(savedInstanceState)
        setContent {
            WearApp()
        }
    }

//    override fun onPause() {
//
//        // WHEN THE SCREEN IS ABOUT TO TURN OFF
//        if (ScreenReceiver.wasScreenOn) {
//            // THIS IS THE CASE WHEN onPause() IS CALLED BY THE SYSTEM DUE TO A SCREEN STATE CHANGE
//            println("SCREEN TURNED OFF")
//        } else {
//            // THIS IS WHEN onPause() IS CALLED WHEN THE SCREEN STATE HAS NOT CHANGED
//        }
//        super.onPause()
//    }
//
//    override fun onResume() {
//        // ONLY WHEN SCREEN TURNS ON
//        println("onResume called")
//
//        if (!ScreenReceiver.wasScreenOn) {
//            // THIS IS WHEN onResume() IS CALLED DUE TO A SCREEN STATE CHANGE
//            println("SCREEN TURNED ON")
//        } else {
//            // THIS IS WHEN onResume() IS CALLED WHEN THE SCREEN STATE HAS NOT CHANGED
//        }
//        super.onResume()
//    }
}

@Composable
fun WearApp() {

    val navController = rememberSwipeDismissableNavController()
    SwipeDismissableNavHost(
        navController = navController, startDestination = "startPage"
    ) {
        composable("startPage") { StartPageScreen(navController) }
        composable("selectExerciseTime") { SelectTimeScreen(navController, exerciseDuration) }
        composable("selectRestTime") { SelectTimeScreen(navController, restDuration) }
        composable("selectRepetitions") { SelectRepetitions(navController, repetitions) }
    }


}


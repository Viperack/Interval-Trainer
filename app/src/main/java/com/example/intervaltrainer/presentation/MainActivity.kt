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
package com.example.intervaltrainer.presentation

import android.content.BroadcastReceiver
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.wear.compose.material.ChipDefaults
import androidx.wear.compose.material.CompactButton
import androidx.wear.compose.material.CompactChip
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.Text
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import kotlinx.coroutines.delay
import java.time.LocalDateTime


var exerciseDuration: Long = 5000
var restDuration: Long = 3000
var repetitions: Int = 3
var timeStamp: Long = 0
const val titleFontSize: Int = 10

var globalExerciseTime: Long = exerciseDuration
var globalRestTime: Long = restDuration
var globalCurrentTimer: Int = 0

class ScreenReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_SCREEN_OFF) {
            // DO WHATEVER YOU NEED TO DO HERE
            Log.d(TAG, "SCREEN TURNING OFF")
            wasScreenOn = false
        } else if (intent.action == Intent.ACTION_SCREEN_ON) {
            // AND DO WHATEVER YOU NEED TO DO HERE
            Log.d(TAG, "SCREEN TURNING ON")
            wasScreenOn = true
        }
    }

    companion object {
        // THANKS JASON
        var wasScreenOn = true
    }
}


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        WakeLocker.release()

        // INITIALIZE RECEIVER
        val filter = IntentFilter(Intent.ACTION_SCREEN_ON)
        filter.addAction(Intent.ACTION_SCREEN_OFF)
        val mReceiver: BroadcastReceiver = ScreenReceiver()
        registerReceiver(mReceiver, filter)

        super.onCreate(savedInstanceState)
        setContent {
            WearApp(this)
        }
    }

    override fun onPause() {

        // WHEN THE SCREEN IS ABOUT TO TURN OFF
        if (ScreenReceiver.wasScreenOn) {
            // THIS IS THE CASE WHEN ONPAUSE() IS CALLED BY THE SYSTEM DUE TO A SCREEN STATE CHANGE
            val scheduler = AndroidAlarmScheduler(this)
            val sleepDuration = if (globalCurrentTimer == 0) globalExerciseTime  else globalRestTime
            var alarmItem = AlarmItem(
                time = LocalDateTime.now().plusSeconds(sleepDuration),
                message = "SCREEN SHOULD TURN ON"
            )
            alarmItem.let(scheduler::schedule)
            println("SCREEN TURNED OFF")
        } else {
            // THIS IS WHEN ONPAUSE() IS CALLED WHEN THE SCREEN STATE HAS NOT CHANGED
        }
        super.onPause()
    }

    override fun onResume() {
        // ONLY WHEN SCREEN TURNS ON
        println("onResume called")

        if (!ScreenReceiver.wasScreenOn) {
            // THIS IS WHEN ONRESUME() IS CALLED DUE TO A SCREEN STATE CHANGE
            println("SCREEN TURNED ON")
        } else {
            // THIS IS WHEN ONRESUME() IS CALLED WHEN THE SCREEN STATE HAS NOT CHANGED
        }
        super.onResume()
    }

    companion object {
        val APP_TAG = "IntervalTrainer"
    }
}

@Composable
fun WearApp(context: Context) {

    val navController = rememberSwipeDismissableNavController()
    SwipeDismissableNavHost(
        navController = navController, startDestination = "startPage"
    ) {
        composable("startPage") { StartPageScreen(navController, context) }
        composable("selectExerciseTime") { SelectExerciseTimeScreen(navController) }
        composable("selectRestTime") { SelectRestTimeScreen(navController) }


    }


}

@Composable
fun StartPageScreen(
    navController: NavController,
    context: Context
) {
    val context = LocalContext.current

    val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    val vibrationPattern = longArrayOf(0, 500, 50, 300)

    var currentExerciseTime: Long by remember {
        mutableLongStateOf(exerciseDuration)
    }
    var currentRestTime: Long by remember {
        mutableLongStateOf(restDuration)
    }
    var currentRepetitions: Int by remember {
        mutableIntStateOf(repetitions)
    }


    var isTimerRunning: Boolean by remember {
        mutableStateOf(false)
    }
    var currentTimer: Int by remember {
        mutableIntStateOf(0)
    }

    var triggerExercise: Boolean by remember {
        mutableStateOf(false)
    }
    var triggerRest: Boolean by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = currentExerciseTime, key2 = isTimerRunning, key3 = triggerExercise) {
        if (!isTimerRunning) {
            return@LaunchedEffect
        }

        if (currentTimer != 0) {
            return@LaunchedEffect
        }

        if (currentExerciseTime <= 0) {
            currentExerciseTime = exerciseDuration
            Log.d(TAG, "VIBRATING")
            vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE))
            currentTimer = 1
            triggerRest = !triggerRest
            if (WakeLocker.isLocked()) {
                WakeLocker.release()
            }
        } else {
            delay(1000L)
            val currentTime = System.currentTimeMillis()
            currentExerciseTime -= currentTime - timeStamp
            timeStamp = currentTime

        }
    }
    LaunchedEffect(key1 = currentRestTime, key2 = isTimerRunning, key3 = triggerRest) {
        if (!isTimerRunning) {
            return@LaunchedEffect
        }

        if (currentTimer != 1) {
            return@LaunchedEffect
        }

        if (currentRestTime <= 0) {
            currentRestTime = restDuration
            currentRepetitions -= 1
            Log.d(TAG, "VIBRATING")
            vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE))
            currentTimer = 0
            if (currentRepetitions != 0) {
                triggerExercise = !triggerExercise
            } else {
                currentRepetitions = repetitions
                isTimerRunning = false
            }
            if (WakeLocker.isLocked()) {
                WakeLocker.release()
            }
        } else {
            delay(1000L)
            val currentTime = System.currentTimeMillis()
            currentRestTime -= currentTime - timeStamp
            timeStamp = currentTime

        }


    }
    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row {
            DisplayTimer(navController, currentExerciseTime, "Exercise", "selectExerciseTime")
            Spacer(modifier = Modifier.size(5.dp))
            DisplayTimer(navController, currentRestTime, "Rest", "selectRestTime")
        }

        Row {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                CompactChip(onClick = {/*TODO*/ },
                    colors = ChipDefaults.primaryChipColors(),
                    label = {
                        Text(
                            textAlign = TextAlign.Center, text = "$currentRepetitions"
                        )
                    })
                Text(
                    text = "Reps", fontSize = titleFontSize.sp
                )
            }
            CompactButton(

                onClick = {
                    timeStamp = System.currentTimeMillis()
                    isTimerRunning = !isTimerRunning
                },

                ) {
                if (!isTimerRunning) {
                    Icon(Icons.Rounded.PlayArrow, contentDescription = "Start training session")
                } else {
                    Icon(Icons.Rounded.Close, contentDescription = "Start training session")
                }

            }


        }


    }
}


@Composable
fun SelectExerciseTimeScreen(
    navController: NavController
) {
    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("SELECT EXERCISE TIME SCREEN")
        CompactButton(

            onClick = {
                navController.popBackStack()
            },
        ) {
            Icon(
                Icons.Rounded.Done, contentDescription = "Confirm selected exercise time"
            )
        }
    }
}


@Composable
fun SelectRestTimeScreen(
    navController: NavController

) {
    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("SELECT REST TIME SCREEN")
        CompactButton(

            onClick = {
                navController.popBackStack()
            },
        ) {
            Icon(
                Icons.Rounded.Done, contentDescription = "Confirm selected exercise time"
            )
        }
    }
}


@Composable
fun DisplayTimer(
    navController: NavController, currentTime: Long, title: String, route: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = title, fontSize = titleFontSize.sp
        )
        CompactChip(onClick = { navController.navigate(route) }, //; Log.d(TAG, "Chip Pressed")
            colors = ChipDefaults.primaryChipColors(), label = {
                Text(
                    textAlign = TextAlign.Center, text = Util.displayTimeString(currentTime)
                )

            })
    }
}

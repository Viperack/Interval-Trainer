package com.example.intervaltrainer.app

import android.content.ContentValues
import android.content.Context
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import kotlinx.coroutines.delay

@Composable
fun StartPageScreen(
    navController: NavController,
) {
    val context = LocalContext.current

    val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

    var currentExerciseTime: Int by remember {
        mutableIntStateOf(exerciseDuration.getInt())
    }
    var currentRestTime: Int by remember {
        mutableIntStateOf(restDuration.getInt())
    }
    var currentRepetitions: Int by remember {
        mutableIntStateOf(repetitions.getInt())
    }


    var isTimerRunning: Boolean by remember {
        mutableStateOf(false)
    }
    var currentTimer: Int by remember {
        mutableIntStateOf(0)
        // 0 => Exercise
        // 1 => Rest
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
            currentExerciseTime = exerciseDuration.getInt()
            Log.d(ContentValues.TAG, "VIBRATING")
            vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE))
            currentTimer = 1
            triggerRest = !triggerRest
        } else {
            delay(1000L)
            val currentTime = System.currentTimeMillis()
            currentExerciseTime -= (currentTime - timeStamp).toInt()
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
            currentRestTime = restDuration.getInt()
            currentRepetitions -= 1
            Log.d(ContentValues.TAG, "VIBRATING")
            vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE))
            currentTimer = 0
            if (currentRepetitions != 0) {
                triggerExercise = !triggerExercise
            } else {
                currentRepetitions = repetitions.getInt()
                isTimerRunning = false
            }
        } else {
            delay(1000L)
            val currentTime = System.currentTimeMillis()
            currentRestTime -= (currentTime - timeStamp).toInt()
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
                CompactChip(onClick = { navController.navigate("selectRepetitions") },
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
                    isTimerRunning = false
                    currentTimer = 0
                    currentExerciseTime = exerciseDuration.getInt()
                    currentRestTime = restDuration.getInt()
                    currentRepetitions = repetitions.getInt()
                },

                ) {
                Icon(Icons.Rounded.Home, contentDescription = "Start training session")

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
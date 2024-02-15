package com.example.intervaltrainer.app

import android.view.MotionEvent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Done
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.wear.compose.material.CompactButton
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Picker
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.rememberPickerState

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SelectTimeScreen(
    navController: NavController, time: IntBox
) {
    var selectedColumn by remember { mutableIntStateOf(0) }
    val textStyle = MaterialTheme.typography.display1

    var minute: Int = ((time.getInt() / 1000) / 60)
    var second: Int  = ((time.getInt() / 1000) % 60)

    @Composable
    fun Option(column: Int, text: String) = Box(modifier = Modifier.fillMaxSize()) {
        Text(text = text,
            style = textStyle,
            color = if (selectedColumn == column) MaterialTheme.colors.secondary
            else MaterialTheme.colors.onBackground,
            modifier = Modifier
                .align(Alignment.Center)
                .wrapContentSize()
                .pointerInteropFilter {
                    if (it.action == MotionEvent.ACTION_DOWN) selectedColumn = column
                    true
                })
    }

    Row(
        modifier = Modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        val minuteState = rememberPickerState(
            initialNumberOfOptions = 60, initiallySelectedOption = minute
        )
        val minuteContentDescription by remember {
            derivedStateOf { "${minuteState.selectedOption} minutes" }
        }
        Picker(readOnly = selectedColumn != 0,
            state = minuteState,
            modifier = Modifier.size(64.dp, 100.dp),
            contentDescription = minuteContentDescription,
            option = { minuteLambda: Int ->
                Option(0, "%2d".format(minuteLambda))
                minute = selectedOption
            })

        Spacer(Modifier.width(8.dp))
        Text(text = ":", style = textStyle, color = MaterialTheme.colors.onBackground)
        Spacer(Modifier.width(8.dp))

        val secondState =
            rememberPickerState(initialNumberOfOptions = 60, initiallySelectedOption = second)
        val secondContentDescription by remember {
            derivedStateOf { "${secondState.selectedOption} seconds" }
        }
        Picker(readOnly = selectedColumn != 1,
            state = secondState,
            modifier = Modifier.size(64.dp, 100.dp),
            contentDescription = secondContentDescription,
            option = { secondLambda: Int ->
                Option(1, "%02d".format(secondLambda))
                second = selectedOption
            })

        CompactButton(
            onClick = {
                time.setInt((minute * 60 + second) * 1000)
                navController.popBackStack()
            },
        ) {
            Icon(
                Icons.Rounded.Done, contentDescription = "Confirm selected time"
            )
        }
    }
}
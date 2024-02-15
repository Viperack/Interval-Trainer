package com.example.intervaltrainer.app

import android.view.MotionEvent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Done
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
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
fun SelectRepetitions(navController: NavController, repetitions: IntBox) {


    var selectedColumn by remember { mutableStateOf(0) }
    val textStyle = MaterialTheme.typography.display1

    var currentRepetitions = repetitions.getInt()

    @Composable
    fun Option(column: Int, text: String) = Box(modifier = Modifier.fillMaxSize()) {
        Text(
            text = text, style = textStyle,
            color = if (selectedColumn == column) MaterialTheme.colors.secondary
            else MaterialTheme.colors.onBackground,
            modifier = Modifier
                .align(Alignment.Center).wrapContentSize()
                .pointerInteropFilter {
                    if (it.action == MotionEvent.ACTION_DOWN) selectedColumn = column
                    true
                }
        )
    }

    Row(
        modifier = Modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        val repetitionsState = rememberPickerState(
            initialNumberOfOptions = 100,
            initiallySelectedOption = currentRepetitions
        )
        val repetitionsContentDescription by remember {
            derivedStateOf { "${repetitionsState.selectedOption} repetitions" }
        }
        Picker(
            readOnly = selectedColumn != 0,
            state = repetitionsState,
            modifier = Modifier.size(64.dp, 100.dp),
            contentDescription = repetitionsContentDescription,
            option = { repetitionsLambda: Int ->
                Option(0, "%2d".format(repetitionsLambda))
                currentRepetitions = selectedOption
            }
        )
        CompactButton(
            onClick = {
                repetitions.setInt(currentRepetitions)
                navController.popBackStack()
            },
        ) {
            Icon(
                Icons.Rounded.Done, contentDescription = "Confirm selected repetitions"
            )
        }
    }
}
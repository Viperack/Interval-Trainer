package com.example.intervaltrainer.app

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.wear.compose.material.ChipDefaults
import androidx.wear.compose.material.CompactChip
import androidx.wear.compose.material.Text

@Composable
fun DisplayTimer(
    navController: NavController, currentTime: Int, title: String, route: String
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
                    textAlign = TextAlign.Center,
                    text = DisplayTime.formatMilliSecondsToString(currentTime)
                )

            })
    }
}
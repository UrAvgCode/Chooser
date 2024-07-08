package com.uravgcode.chooser.composables.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.uravgcode.chooser.utilities.SettingsManager

@Composable
fun SettingsScreen(settings: SettingsManager, onNavigateBack: () -> Unit) {
    Box(
        Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Button(
            onClick = onNavigateBack,
            modifier = Modifier.align(Alignment.Center)
        ) {
            Text("Back to Chooser")
        }
    }
}

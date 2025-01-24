package com.uravgcode.chooser.composables.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.uravgcode.chooser.composables.Screen
import com.uravgcode.chooser.utilities.SettingsManager

@Composable
fun MainScreen(
    settings: SettingsManager
) {
    var currentScreen by remember { mutableStateOf<Screen>(Screen.Chooser) }

    when (currentScreen) {
        is Screen.Chooser -> ChooserScreen(
            settings = settings,
            onNavigate = { currentScreen = Screen.Settings }
        )

        is Screen.Settings -> SettingsScreen(
            settings = settings,
            onNavigateBack = { currentScreen = Screen.Chooser }
        )
    }
}
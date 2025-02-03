/*
 * Copyright (C) 2025 UrAvgCode
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 *
 * @author UrAvgCode
 * @description MainScreen is the screen that contains the chooser and settings screens.
 */

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
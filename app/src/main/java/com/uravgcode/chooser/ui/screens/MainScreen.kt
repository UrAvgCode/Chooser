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

package com.uravgcode.chooser.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun MainScreen() {
    var isChooserScreen by remember { mutableStateOf(true) }

    BackHandler(enabled = !isChooserScreen) {
        isChooserScreen = true
    }

    if (isChooserScreen) {
        ChooserScreen(
            onNavigate = { isChooserScreen = false }
        )
    } else {
        SettingsScreen(
            onNavigateBack = { isChooserScreen = true }
        )
    }
}

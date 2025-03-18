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
 * @description Navigation composable that sets up the navigation between different screens.
 */

package com.uravgcode.chooser.navigation.presentation

import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.datastore.core.DataStore
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.uravgcode.chooser.chooser.presentation.ChooserScreen
import com.uravgcode.chooser.navigation.domain.Screen
import com.uravgcode.chooser.settings.data.SettingsData
import com.uravgcode.chooser.settings.presentation.SettingsScreen

@Composable
fun Navigation(dataStore: DataStore<SettingsData>) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Chooser,
        modifier = Modifier.background(Color.Black),
        enterTransition = { fadeIn(spring()) + scaleIn(initialScale = 1.1f) },
        exitTransition = { fadeOut(spring()) + scaleOut(targetScale = 1.1f) }
    ) {
        composable<Screen.Chooser> {
            ChooserScreen(
                onNavigate = { navController.navigate(Screen.Settings) },
                dataStore = dataStore
            )
        }
        composable<Screen.Settings> {
            SettingsScreen(
                onNavigateBack = { navController.popBackStack() },
                dataStore = dataStore
            )
        }
    }
}

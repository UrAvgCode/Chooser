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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
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
import com.uravgcode.chooser.tutorial.presentation.TutorialScreen
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@Composable
fun Navigation(dataStore: DataStore<SettingsData>) {
    val navController = rememberNavController()
    val coroutineScope = rememberCoroutineScope()

    val hasSeenTutorial by dataStore.data.map { it.hasSeenTutorial }.collectAsState(initial = true)
    val startDestination = if (hasSeenTutorial) Screen.Chooser else Screen.Tutorial

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = Modifier.background(Color.Black),
        enterTransition = { fadeIn(spring()) + scaleIn(initialScale = 1.1f) },
        exitTransition = { fadeOut(spring()) + scaleOut(targetScale = 1.1f) }
    ) {
        composable<Screen.Tutorial> {
            TutorialScreen(
                onComplete = {
                    coroutineScope.launch {
                        dataStore.updateData {
                            it.copy(hasSeenTutorial = true)
                        }
                        navController.navigate(Screen.Chooser) {
                            popUpTo(Screen.Tutorial) { inclusive = true }
                        }
                    }
                }
            )
        }
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

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

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.serialization.Serializable

@Serializable
data object Chooser

@Serializable
data object Settings

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Chooser
    ) {
        composable<Chooser> {
            ChooserScreen(
                onNavigate = { navController.navigate(Settings) }
            )
        }
        composable<Settings> {
            SettingsScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}

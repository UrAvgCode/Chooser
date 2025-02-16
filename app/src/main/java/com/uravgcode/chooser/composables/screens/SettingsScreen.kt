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
 * @description SettingsScreen is the settings screen of the application.
 */

package com.uravgcode.chooser.composables.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uravgcode.chooser.composables.settings.SettingsRowSlider
import com.uravgcode.chooser.composables.settings.SettingsRowSwitch
import com.uravgcode.chooser.utilities.SettingsManager
import com.uravgcode.chooser.views.Chooser

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun SettingsScreen(onNavigateBack: () -> Unit) {
    val isSoundEnabled = remember { mutableStateOf(SettingsManager.soundEnabled) }
    val isVibrationEnabled = remember { mutableStateOf(SettingsManager.vibrationEnabled) }
    val isEdgeToEdgeEnabled = remember { mutableStateOf(SettingsManager.edgeToEdgeEnabled) }
    val circleSize = remember { mutableFloatStateOf(SettingsManager.circleSize) }

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.windowInsetsPadding(
                    WindowInsets.safeContent.only(WindowInsetsSides.Top)
                ),
                title = {
                    Text(
                        text = "Settings",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontSize = 32.sp
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { onNavigateBack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(padding)
                .padding(16.dp)
        ) {
            SettingsRowSwitch(
                title = "Enable Sound",
                isChecked = isSoundEnabled.value,
                onCheckedChange = { isChecked ->
                    isSoundEnabled.value = isChecked
                    SettingsManager.soundEnabled = isChecked
                }
            )

            SettingsRowSwitch(
                title = "Enable Vibration",
                isChecked = isVibrationEnabled.value,
                onCheckedChange = { isChecked ->
                    isVibrationEnabled.value = isChecked
                    SettingsManager.vibrationEnabled = isChecked
                }
            )

            SettingsRowSwitch(
                title = "Enable Edge-to-Edge",
                isChecked = isEdgeToEdgeEnabled.value,
                onCheckedChange = { isChecked ->
                    isEdgeToEdgeEnabled.value = isChecked
                    SettingsManager.edgeToEdgeEnabled = isChecked
                }
            )

            SettingsRowSlider(
                title = "Circle Size",
                value = circleSize.floatValue,
                onValueChange = { sliderValue ->
                    circleSize.floatValue = sliderValue
                    SettingsManager.circleSize = sliderValue
                    Chooser.circleSize = sliderValue
                },
                valueRange = 10f..100f
            )

        }
    }
}

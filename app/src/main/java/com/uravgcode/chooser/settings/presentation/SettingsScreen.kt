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
 * @author UrAvgCode, Patch4Code
 * @description SettingsScreen is the settings screen of the application.
 */

package com.uravgcode.chooser.settings.presentation

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import com.uravgcode.chooser.settings.domain.SettingsManager
import com.uravgcode.chooser.settings.presentation.component.ResetDialog
import com.uravgcode.chooser.settings.presentation.component.SettingsSeparator
import com.uravgcode.chooser.settings.presentation.component.SettingsTopAppBar
import com.uravgcode.chooser.settings.presentation.row.SettingsRowPaddingSlider
import com.uravgcode.chooser.settings.presentation.row.SettingsRowPercentSlider
import com.uravgcode.chooser.settings.presentation.row.SettingsRowSwitch
import com.uravgcode.chooser.settings.presentation.row.SettingsRowTimeSlider

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun SettingsScreen(onNavigateBack: () -> Unit) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    var showResetDialog by remember { mutableStateOf(false) }

    var isSoundEnabled by remember { mutableStateOf(SettingsManager.soundEnabled) }
    var isVibrationEnabled by remember { mutableStateOf(SettingsManager.vibrationEnabled) }

    var isEdgeToEdgeEnabled by remember { mutableStateOf(SettingsManager.edgeToEdgeEnabled) }
    var additionalTopPadding by remember { mutableFloatStateOf(SettingsManager.additionalTopPadding) }
    var circleSizeFactor by remember { mutableFloatStateOf(SettingsManager.circleSizeFactor) }

    var circleLifetime by remember { mutableLongStateOf(SettingsManager.circleLifetime) }
    var groupCircleLifetime by remember { mutableLongStateOf(SettingsManager.groupCircleLifetime) }
    var orderCircleLifetime by remember { mutableLongStateOf(SettingsManager.orderCircleLifetime) }

    ResetDialog(
        showResetDialog = showResetDialog,
        onDismiss = { showResetDialog = false },
        onReset = {
            SettingsManager.reset()
            onNavigateBack()
        }
    )

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            SettingsTopAppBar(
                onNavigateBack,
                scrollBehavior
            )
        },
        contentWindowInsets = WindowInsets.safeDrawing
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            item {
                SettingsSeparator(
                    heading = "General Settings",
                    showDivider = false,
                )
                SettingsRowSwitch(
                    title = "Enable Sound",
                    isChecked = isSoundEnabled,
                    onCheckedChange = { isChecked ->
                        isSoundEnabled = isChecked
                        SettingsManager.soundEnabled = isChecked
                    }
                )
                SettingsRowSwitch(
                    title = "Enable Vibration",
                    isChecked = isVibrationEnabled,
                    onCheckedChange = { isChecked ->
                        isVibrationEnabled = isChecked
                        SettingsManager.vibrationEnabled = isChecked
                    }
                )
            }

            item {
                SettingsSeparator("Display Settings")
                SettingsRowSwitch(
                    title = "Full Screen Mode",
                    isChecked = isEdgeToEdgeEnabled,
                    onCheckedChange = { isChecked ->
                        isEdgeToEdgeEnabled = isChecked
                        SettingsManager.edgeToEdgeEnabled = isChecked
                    }
                )
                SettingsRowPaddingSlider(
                    title = "Additional Button Padding",
                    value = additionalTopPadding,
                    onValueChange = { sliderValue ->
                        additionalTopPadding = sliderValue
                        SettingsManager.additionalTopPadding = sliderValue
                    },
                    valueRange = 0f..50f,
                    steps = 9
                )
                SettingsRowPercentSlider(
                    title = "Circle Size",
                    value = circleSizeFactor,
                    onValueChange = { sliderValue ->
                        circleSizeFactor = sliderValue
                        SettingsManager.circleSizeFactor = sliderValue
                    },
                    valueRange = 0.5f..1.5f,
                    steps = 9
                )
            }

            item {
                SettingsSeparator("Circle Lifetimes")
                SettingsRowTimeSlider(
                    title = "Circle Lifetime",
                    value = circleLifetime,
                    onValueChange = { sliderValue ->
                        circleLifetime = sliderValue
                        SettingsManager.circleLifetime = sliderValue
                    },
                    valueRange = 0L..3000L,
                    steps = 5,
                )
                SettingsRowTimeSlider(
                    title = "Group Circle Lifetime",
                    value = groupCircleLifetime,
                    onValueChange = { sliderValue ->
                        groupCircleLifetime = sliderValue
                        SettingsManager.groupCircleLifetime = sliderValue
                    },
                    valueRange = 0L..3000L,
                    steps = 5,
                )
                SettingsRowTimeSlider(
                    title = "Order Circle Lifetime",
                    value = orderCircleLifetime,
                    onValueChange = { sliderValue ->
                        orderCircleLifetime = sliderValue
                        SettingsManager.orderCircleLifetime = sliderValue
                    },
                    valueRange = 0L..3000L,
                    steps = 5,
                )
            }

            item {
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                ElevatedButton(
                    content = { Text("Reset Settings") },
                    onClick = { showResetDialog = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )
            }
        }
    }
}

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

package com.uravgcode.chooser.ui.screens

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.uravgcode.chooser.ui.components.settings.ResetDialog
import com.uravgcode.chooser.ui.components.settings.RestartDialog
import com.uravgcode.chooser.ui.components.settings.SettingsRowPaddingSlider
import com.uravgcode.chooser.ui.components.settings.SettingsRowPercentSlider
import com.uravgcode.chooser.ui.components.settings.SettingsRowSwitch
import com.uravgcode.chooser.ui.components.settings.SettingsRowTimeSlider
import com.uravgcode.chooser.ui.components.settings.SettingsSeparator
import com.uravgcode.chooser.ui.components.settings.SettingsTopAppBar
import com.uravgcode.chooser.utilities.SettingsManager

@Composable
fun SettingsScreen(onNavigateBack: () -> Unit) {
    val context = LocalContext.current
    val showResetDialog = remember { mutableStateOf(false) }
    val showRestartDialog = remember { mutableStateOf(false) }

    val isSoundEnabled = remember { mutableStateOf(SettingsManager.soundEnabled) }
    val isVibrationEnabled = remember { mutableStateOf(SettingsManager.vibrationEnabled) }

    val isEdgeToEdgeEnabled = remember { mutableStateOf(SettingsManager.edgeToEdgeEnabled) }
    val additionalTopPadding = remember { mutableFloatStateOf(SettingsManager.additionalTopPadding) }
    val circleSizeFactor = remember { mutableFloatStateOf(SettingsManager.circleSizeFactor) }

    val circleLifetime = remember { mutableLongStateOf(SettingsManager.circleLifetime) }
    val groupCircleLifetime = remember { mutableLongStateOf(SettingsManager.groupCircleLifetime) }
    val orderCircleLifetime = remember { mutableLongStateOf(SettingsManager.orderCircleLifetime) }

    ResetDialog(
        showResetDialog = showResetDialog.value,
        onDismiss = { showResetDialog.value = false },
        onReset = {
            SettingsManager.reset()

            isSoundEnabled.value = SettingsManager.soundEnabled
            isVibrationEnabled.value = SettingsManager.vibrationEnabled

            isEdgeToEdgeEnabled.value = SettingsManager.edgeToEdgeEnabled
            additionalTopPadding.floatValue = SettingsManager.additionalTopPadding
            circleSizeFactor.floatValue = SettingsManager.circleSizeFactor

            circleLifetime.longValue = SettingsManager.circleLifetime
            groupCircleLifetime.longValue = SettingsManager.groupCircleLifetime
            orderCircleLifetime.longValue = SettingsManager.orderCircleLifetime
        }
    )

    RestartDialog(
        showRestartDialog = showRestartDialog.value,
        onDismiss = { showRestartDialog.value = false },
        context = context
    )

    Scaffold(
        topBar = {
            SettingsTopAppBar(onNavigateBack)
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            item {

                SettingsSeparator("General Settings", false)

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

                SettingsSeparator("Display Settings")

                SettingsRowSwitch(
                    title = "Enable Edge-to-Edge",
                    isChecked = isEdgeToEdgeEnabled.value,
                    onCheckedChange = { isChecked ->
                        isEdgeToEdgeEnabled.value = isChecked
                        SettingsManager.edgeToEdgeEnabled = isChecked
                        showRestartDialog.value = true
                    }
                )

                SettingsRowPaddingSlider(
                    title = "Additional Top Padding",
                    value = additionalTopPadding.floatValue,
                    onValueChange = { sliderValue ->
                        additionalTopPadding.floatValue = sliderValue
                        SettingsManager.additionalTopPadding = sliderValue
                    },
                    valueRange = 0f..50f,
                    steps = 9
                )

                SettingsRowPercentSlider(
                    title = "Circle Size",
                    value = circleSizeFactor.floatValue,
                    onValueChange = { sliderValue ->
                        circleSizeFactor.floatValue = sliderValue
                        SettingsManager.circleSizeFactor = sliderValue
                    },
                    valueRange = 0.5f..1.5f,
                    steps = 9
                )

                SettingsSeparator("Circle Lifetimes")

                SettingsRowTimeSlider(
                    title = "Circle Lifetime",
                    value = circleLifetime.longValue,
                    onValueChange = { sliderValue ->
                        circleLifetime.longValue = sliderValue
                        SettingsManager.circleLifetime = sliderValue
                    },
                    valueRange = 0L..3000L,
                    steps = 5,
                )

                SettingsRowTimeSlider(
                    title = "Group Circle Lifetime",
                    value = groupCircleLifetime.longValue,
                    onValueChange = { sliderValue ->
                        groupCircleLifetime.longValue = sliderValue
                        SettingsManager.groupCircleLifetime = sliderValue
                    },
                    valueRange = 0L..3000L,
                    steps = 5,
                )

                SettingsRowTimeSlider(
                    title = "Order Circle Lifetime",
                    value = orderCircleLifetime.longValue,
                    onValueChange = { sliderValue ->
                        orderCircleLifetime.longValue = sliderValue
                        SettingsManager.orderCircleLifetime = sliderValue
                    },
                    valueRange = 0L..3000L,
                    steps = 5,
                )

                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                ElevatedButton(
                    content = { Text("Reset Settings") },
                    onClick = { showResetDialog.value = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )
            }
        }
    }
}

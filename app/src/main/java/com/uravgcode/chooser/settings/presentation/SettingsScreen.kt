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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.datastore.core.DataStore
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.uravgcode.chooser.settings.data.SettingsData
import com.uravgcode.chooser.settings.presentation.button.ExportButton
import com.uravgcode.chooser.settings.presentation.button.ImportButton
import com.uravgcode.chooser.settings.presentation.button.ResetButton
import com.uravgcode.chooser.settings.presentation.component.SettingsSeparator
import com.uravgcode.chooser.settings.presentation.component.SettingsSwitch
import com.uravgcode.chooser.settings.presentation.component.SettingsTopAppBar
import com.uravgcode.chooser.settings.presentation.slider.PaddingSlider
import com.uravgcode.chooser.settings.presentation.slider.PercentSlider
import com.uravgcode.chooser.settings.presentation.slider.TimeSlider
import kotlinx.coroutines.launch

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun SettingsScreen(
    onNavigateBack: () -> Unit,
    dataStore: DataStore<SettingsData>
) {
    val coroutineScope = rememberCoroutineScope()
    val settings by dataStore.data.collectAsStateWithLifecycle(initialValue = SettingsData())

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

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
                SettingsSwitch(
                    title = "Enable Sound",
                    isChecked = settings.soundEnabled,
                    onCheckedChange = { isChecked ->
                        coroutineScope.launch {
                            dataStore.updateData { it.copy(soundEnabled = isChecked) }
                        }
                    }
                )
                SettingsSwitch(
                    title = "Enable Vibration",
                    isChecked = settings.vibrationEnabled,
                    onCheckedChange = { isChecked ->
                        coroutineScope.launch {
                            dataStore.updateData { it.copy(vibrationEnabled = isChecked) }
                        }
                    }
                )
            }

            item {
                SettingsSeparator("Display Settings")
                SettingsSwitch(
                    title = "Full Screen Mode",
                    isChecked = settings.fullScreen,
                    onCheckedChange = { isChecked ->
                        coroutineScope.launch {
                            dataStore.updateData { it.copy(fullScreen = isChecked) }
                        }
                    }
                )
                PaddingSlider(
                    title = "Additional Button Padding",
                    value = settings.additionalButtonPadding,
                    onValueChange = { sliderValue ->
                        coroutineScope.launch {
                            dataStore.updateData { it.copy(additionalButtonPadding = sliderValue) }
                        }
                    },
                    valueRange = 0f..50f,
                    steps = 9
                )
                PercentSlider(
                    title = "Circle Size",
                    value = settings.circleSizeFactor,
                    onValueChange = { sliderValue ->
                        coroutineScope.launch {
                            dataStore.updateData { it.copy(circleSizeFactor = sliderValue) }
                        }
                    },
                    valueRange = 0.5f..1.5f,
                    steps = 9
                )
            }

            item {
                SettingsSeparator("Selection Delays")
                TimeSlider(
                    title = "Single Mode Delay",
                    value = settings.singleDelay,
                    onValueChange = { sliderValue ->
                        coroutineScope.launch {
                            dataStore.updateData { it.copy(singleDelay = sliderValue) }
                        }
                    },
                    valueRange = 0L..5000L,
                    steps = 9,
                )
                TimeSlider(
                    title = "Group Mode Delay",
                    value = settings.groupDelay,
                    onValueChange = { sliderValue ->
                        coroutineScope.launch {
                            dataStore.updateData { it.copy(groupDelay = sliderValue) }
                        }
                    },
                    valueRange = 0L..5000L,
                    steps = 9,
                )
                TimeSlider(
                    title = "Order Mode Delay",
                    value = settings.orderDelay,
                    onValueChange = { sliderValue ->
                        coroutineScope.launch {
                            dataStore.updateData { it.copy(orderDelay = sliderValue) }
                        }
                    },
                    valueRange = 0L..5000L,
                    steps = 9,
                )
            }

            item {
                SettingsSeparator("Circle Lifetimes")
                TimeSlider(
                    title = "Circle Lifetime",
                    value = settings.circleLifetime,
                    onValueChange = { sliderValue ->
                        coroutineScope.launch {
                            dataStore.updateData { it.copy(circleLifetime = sliderValue) }
                        }
                    },
                    valueRange = 0L..3000L,
                    steps = 5,
                )
                TimeSlider(
                    title = "Group Circle Lifetime",
                    value = settings.groupCircleLifetime,
                    onValueChange = { sliderValue ->
                        coroutineScope.launch {
                            dataStore.updateData { it.copy(groupCircleLifetime = sliderValue) }
                        }
                    },
                    valueRange = 0L..3000L,
                    steps = 5,
                )
                TimeSlider(
                    title = "Order Circle Lifetime",
                    value = settings.orderCircleLifetime,
                    onValueChange = { sliderValue ->
                        coroutineScope.launch {
                            dataStore.updateData { it.copy(orderCircleLifetime = sliderValue) }
                        }
                    },
                    valueRange = 0L..3000L,
                    steps = 5,
                )
            }

            item {
                SettingsSeparator()
                ImportButton(dataStore)
                ExportButton(dataStore)
                ResetButton(dataStore)
            }
        }
    }
}

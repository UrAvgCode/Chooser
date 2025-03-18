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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.datastore.core.DataStore
import com.uravgcode.chooser.settings.data.SettingsData
import com.uravgcode.chooser.settings.presentation.component.ResetDialog
import com.uravgcode.chooser.settings.presentation.component.SettingsSeparator
import com.uravgcode.chooser.settings.presentation.component.SettingsTopAppBar
import com.uravgcode.chooser.settings.presentation.row.SettingsRowPaddingSlider
import com.uravgcode.chooser.settings.presentation.row.SettingsRowPercentSlider
import com.uravgcode.chooser.settings.presentation.row.SettingsRowSwitch
import com.uravgcode.chooser.settings.presentation.row.SettingsRowTimeSlider
import kotlinx.coroutines.launch

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun SettingsScreen(
    onNavigateBack: () -> Unit,
    dataStore: DataStore<SettingsData>
) {
    val coroutineScope = rememberCoroutineScope()
    val settings by dataStore.data.collectAsState(initial = SettingsData())

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    var showResetDialog by remember { mutableStateOf(false) }

    ResetDialog(
        showResetDialog = showResetDialog,
        onDismiss = { showResetDialog = false },
        onReset = {
            coroutineScope.launch {
                dataStore.updateData { SettingsData() }
            }
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
                    isChecked = settings.soundEnabled,
                    onCheckedChange = { isChecked ->
                        coroutineScope.launch {
                            dataStore.updateData { it.copy(soundEnabled = isChecked) }
                        }
                    }
                )
                SettingsRowSwitch(
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
                SettingsRowSwitch(
                    title = "Full Screen Mode",
                    isChecked = settings.fullScreen,
                    onCheckedChange = { isChecked ->
                        coroutineScope.launch {
                            dataStore.updateData { it.copy(fullScreen = isChecked) }
                        }
                    }
                )
                SettingsRowPaddingSlider(
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
                SettingsRowPercentSlider(
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
                SettingsSeparator("Circle Lifetimes")
                SettingsRowTimeSlider(
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
                SettingsRowTimeSlider(
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
                SettingsRowTimeSlider(
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

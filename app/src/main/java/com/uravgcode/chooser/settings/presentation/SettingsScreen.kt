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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.datastore.core.DataStore
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.uravgcode.chooser.R
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
                    heading = stringResource(R.string.settings_general),
                    showDivider = false,
                )
                SettingsSwitch(
                    title = stringResource(R.string.sound),
                    isChecked = settings.soundEnabled,
                    onCheckedChange = { isChecked ->
                        coroutineScope.launch {
                            dataStore.updateData { it.copy(soundEnabled = isChecked) }
                        }
                    }
                )
                SettingsSwitch(
                    title = stringResource(R.string.vibration),
                    isChecked = settings.vibrationEnabled,
                    onCheckedChange = { isChecked ->
                        coroutineScope.launch {
                            dataStore.updateData { it.copy(vibrationEnabled = isChecked) }
                        }
                    }
                )
            }

            item {
                SettingsSeparator(stringResource(R.string.settings_display))
                SettingsSwitch(
                    title = stringResource(R.string.full_screen),
                    isChecked = settings.fullScreen,
                    onCheckedChange = { isChecked ->
                        coroutineScope.launch {
                            dataStore.updateData { it.copy(fullScreen = isChecked) }
                        }
                    }
                )
                PaddingSlider(
                    title = stringResource(R.string.button_padding),
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
                    title = stringResource(R.string.circle_size),
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
                SettingsSeparator(stringResource(R.string.settings_selection_delays))
                TimeSlider(
                    title = stringResource(R.string.delay_single_mode),
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
                    title = stringResource(R.string.delay_group_mode),
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
                    title = stringResource(R.string.delay_order_mode),
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
                SettingsSeparator(stringResource(R.string.settings_circle_lifetimes))
                SettingsSwitch(
                    title = stringResource(R.string.clear_on_touch),
                    isChecked = settings.clearOnTouch,
                    onCheckedChange = { isChecked ->
                        coroutineScope.launch {
                            dataStore.updateData { it.copy(clearOnTouch = isChecked) }
                        }
                    }
                )
                TimeSlider(
                    title = stringResource(R.string.lifetime_circle),
                    value = settings.circleLifetime,
                    onValueChange = { sliderValue ->
                        coroutineScope.launch {
                            dataStore.updateData { it.copy(circleLifetime = sliderValue) }
                        }
                    },
                    valueRange = 0L..5000L,
                    steps = 9,
                )
                TimeSlider(
                    title = stringResource(R.string.lifetime_group_circle),
                    value = settings.groupCircleLifetime,
                    onValueChange = { sliderValue ->
                        coroutineScope.launch {
                            dataStore.updateData { it.copy(groupCircleLifetime = sliderValue) }
                        }
                    },
                    valueRange = 0L..5000L,
                    steps = 9,
                )
                TimeSlider(
                    title = stringResource(R.string.lifetime_order_circle),
                    value = settings.orderCircleLifetime,
                    onValueChange = { sliderValue ->
                        coroutineScope.launch {
                            dataStore.updateData { it.copy(orderCircleLifetime = sliderValue) }
                        }
                    },
                    valueRange = 0L..5000L,
                    steps = 9,
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

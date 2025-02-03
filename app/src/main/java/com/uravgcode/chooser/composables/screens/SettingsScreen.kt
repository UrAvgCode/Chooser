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
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.uravgcode.chooser.composables.settings.SettingsRowSwitch
import com.uravgcode.chooser.utilities.SettingsManager

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun SettingsScreen(settings: SettingsManager, onNavigateBack: () -> Unit) {
    val isSoundEnabled = remember { mutableStateOf(settings.isSoundEnabled()) }
    val isEdgeToEdgeEnabled = remember { mutableStateOf(settings.isEdgeToEdgeEnabled()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Settings",
                        style = MaterialTheme.typography.titleLarge
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
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(it)
                .padding(16.dp)
        ) {
            SettingsRowSwitch(
                title = "Enable Sound",
                isChecked = isSoundEnabled.value,
                onCheckedChange = {
                    isSoundEnabled.value = it
                    settings.setSoundEnabled(it)
                }
            )

            SettingsRowSwitch(
                title = "Enable Edge-to-Edge",
                isChecked = isEdgeToEdgeEnabled.value,
                onCheckedChange = {
                    isEdgeToEdgeEnabled.value = it
                    settings.setEdgeToEdgeEnabled(it)
                }
            )
        }
    }
}

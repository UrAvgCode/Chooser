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

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import com.uravgcode.chooser.ui.components.dialogs.ResetDialog
import com.uravgcode.chooser.ui.components.settings.SettingsTopAppBar
import com.uravgcode.chooser.ui.components.settings.sections.CircleLifetimesSection
import com.uravgcode.chooser.ui.components.settings.sections.DisplaySettingsSection
import com.uravgcode.chooser.ui.components.settings.sections.GeneralSettingsSection
import com.uravgcode.chooser.ui.components.settings.sections.ResetSettingsSection
import com.uravgcode.chooser.utilities.SettingsManager

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun SettingsScreen(onNavigateBack: () -> Unit) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val showResetDialog = remember { mutableStateOf(false) }

    ResetDialog(
        showResetDialog = showResetDialog.value,
        onDismiss = { showResetDialog.value = false },
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
            item { GeneralSettingsSection() }
            item { DisplaySettingsSection() }
            item { CircleLifetimesSection() }
            item { ResetSettingsSection(onClick = { showResetDialog.value = true }) }
        }
    }
}

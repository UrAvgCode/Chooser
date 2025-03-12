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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import com.uravgcode.chooser.settings.domain.SettingsManager
import com.uravgcode.chooser.settings.presentation.component.ResetDialog
import com.uravgcode.chooser.settings.presentation.component.SettingsTopAppBar
import com.uravgcode.chooser.settings.presentation.section.CircleLifetimesSection
import com.uravgcode.chooser.settings.presentation.section.DisplaySettingsSection
import com.uravgcode.chooser.settings.presentation.section.GeneralSettingsSection
import com.uravgcode.chooser.settings.presentation.section.ResetSettingsSection

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun SettingsScreen(onNavigateBack: () -> Unit) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    var showResetDialog by remember { mutableStateOf(false) }

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
            item { GeneralSettingsSection() }
            item { DisplaySettingsSection() }
            item { CircleLifetimesSection() }
            item { ResetSettingsSection(onClick = { showResetDialog = true }) }
        }
    }
}

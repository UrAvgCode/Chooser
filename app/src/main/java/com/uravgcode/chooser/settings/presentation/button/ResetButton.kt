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
 * @description ResetButton allows users to restore all settings to their default values.
 */

package com.uravgcode.chooser.settings.presentation.button

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.datastore.core.DataStore
import com.uravgcode.chooser.R
import com.uravgcode.chooser.settings.data.SettingsData
import kotlinx.coroutines.launch

@Composable
fun ResetButton(
    dataStore: DataStore<SettingsData>
) {
    val coroutineScope = rememberCoroutineScope()
    var showResetDialog by remember { mutableStateOf(false) }

    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            confirmButton = {
                Button(
                    onClick = {
                        coroutineScope.launch {
                            dataStore.updateData { SettingsData(hasSeenTutorial = true) }
                            showResetDialog = false
                        }
                    }
                ) {
                    Text(text = stringResource(R.string.reset))
                }
            },
            dismissButton = {
                Button(
                    onClick = { showResetDialog = false }
                ) {
                    Text(text = stringResource(R.string.cancel))
                }
            },
            title = {
                Text(text = stringResource(R.string.settings_reset))
            },
            text = {
                Text(text = stringResource(R.string.settings_reset_confirm))
            },
        )
    }

    SettingsButton(
        text = stringResource(R.string.settings_reset),
        onClick = { showResetDialog = true }
    )
}

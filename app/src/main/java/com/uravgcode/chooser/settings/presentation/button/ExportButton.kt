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
 * @description ExportButton provides functionality to export settings to a file.
 */

package com.uravgcode.chooser.settings.presentation.button

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.datastore.core.DataStore
import com.uravgcode.chooser.R
import com.uravgcode.chooser.settings.data.SettingsData
import com.uravgcode.chooser.settings.data.SettingsSerializer
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@Composable
fun ExportButton(
    dataStore: DataStore<SettingsData>
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val successMessage = stringResource(R.string.settings_export_success)
    val errorMessage = stringResource(R.string.settings_export_failed)

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/json")
    ) { uri ->
        uri?.let {
            scope.launch {
                try {
                    context.contentResolver.openOutputStream(uri)?.use { outputStream ->
                        SettingsSerializer.writeTo(dataStore.data.first(), outputStream)
                    }
                    Toast.makeText(context, successMessage, Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    Toast.makeText(context, "$errorMessage: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    SettingsButton(
        text = stringResource(R.string.settings_export),
        onClick = { launcher.launch("settings.json") }
    )
}

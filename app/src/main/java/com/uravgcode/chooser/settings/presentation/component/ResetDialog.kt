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
 * @description ResetDialog shows a dialog to reset settings to default.
 */

package com.uravgcode.chooser.settings.presentation.component

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun ResetDialog(
    showResetDialog: Boolean,
    onDismiss: () -> Unit,
    onReset: () -> Unit,
) {
    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { onDismiss() },
            confirmButton = {
                Button(
                    onClick = {
                        onReset()
                        onDismiss()
                    }
                ) {
                    Text("Reset")
                }
            },
            dismissButton = {
                Button(onClick = { onDismiss() }) {
                    Text("Cancel")
                }
            },
            title = { Text(text = "Reset Settings") },
            text = { Text(text = "Are you sure you want to reset all settings to their default values?") },
        )
    }
}

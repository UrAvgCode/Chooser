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
 * @description RestartDialog shows a restart dialog if a setting requires a restart to take effect.
 */

package com.uravgcode.chooser.composables.settings

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.uravgcode.chooser.MainActivity

@Composable
fun RestartDialog(
    showRestartDialog: Boolean,
    onDismiss: () -> Unit,
    context: Context
) {
    if (showRestartDialog) {
        AlertDialog(
            onDismissRequest = { onDismiss() },
            title = { Text(text = "Restart Required") },
            text = { Text(text = "Changes will take effect after restart. Do you want to restart now?") },
            confirmButton = {
                Button(onClick = {
                    onDismiss()
                    val intent = Intent(context, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(intent)
                    if (context is Activity) {
                        context.finish()
                    }
                }) {
                    Text("Restart")
                }
            },
            dismissButton = {
                Button(onClick = { onDismiss() }) {
                    Text("Cancel")
                }
            }
        )
    }
}
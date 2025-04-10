package com.uravgcode.chooser.settings.presentation.button

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.datastore.core.DataStore
import com.uravgcode.chooser.settings.data.SettingsData
import kotlinx.coroutines.launch

@Composable
fun SettingsButtonReset(dataStore: DataStore<SettingsData>) {
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
                    Text("Reset")
                }
            },
            dismissButton = {
                Button(
                    onClick = { showResetDialog = false }
                ) {
                    Text("Cancel")
                }
            },
            title = { Text(text = "Reset Settings") },
            text = { Text(text = "Are you sure you want to reset all settings to their default values?") },
        )
    }

    ElevatedButton(
        content = { Text("Reset Settings") },
        onClick = { showResetDialog = true },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    )
}

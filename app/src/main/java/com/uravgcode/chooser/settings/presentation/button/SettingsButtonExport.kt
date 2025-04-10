package com.uravgcode.chooser.settings.presentation.button

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.datastore.core.DataStore
import com.uravgcode.chooser.settings.data.SettingsData
import com.uravgcode.chooser.settings.data.SettingsSerializer
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@Composable
fun SettingsButtonExport(dataStore: DataStore<SettingsData>) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/json")
    ) { uri ->
        uri?.let {
            scope.launch {
                try {
                    context.contentResolver.openOutputStream(uri)?.use { outputStream ->
                        SettingsSerializer.writeTo(dataStore.data.first(), outputStream)
                    }
                    Toast.makeText(context, "Settings exported successfully", Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    Toast.makeText(context, "Failed to export settings: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    SettingsButton(
        text = "Export Settings",
        onClick = { launcher.launch("settings.json") }
    )
}

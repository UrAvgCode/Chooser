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
 * @description MainActivity is the entry point of the application.
 */

package com.uravgcode.chooser

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.uravgcode.chooser.navigation.presentation.Navigation
import com.uravgcode.chooser.settings.data.Settings
import com.uravgcode.chooser.settings.data.SettingsSerializer
import com.uravgcode.chooser.ui.theme.ChooserTheme

class MainActivity : ComponentActivity() {
    private val Context.dataStore: DataStore<Settings> by dataStore(
        fileName = "settings.json",
        serializer = SettingsSerializer
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            ChooserTheme {
                Navigation(dataStore)
            }
        }
    }
}

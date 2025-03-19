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
 * @description Migrate settings from SharedPreferences to DataStore
 */

package com.uravgcode.chooser.settings.data

import android.content.Context
import android.content.Context.MODE_PRIVATE
import androidx.core.content.edit
import androidx.datastore.core.DataStore
import com.uravgcode.chooser.chooser.domain.Mode
import kotlinx.coroutines.runBlocking

fun migrateSettings(
    context: Context,
    dataStore: DataStore<SettingsData>
) {
    val preferences = context.getSharedPreferences("settings", MODE_PRIVATE)
    if (preferences.getBoolean("migrated", false)) {
        return
    }

    runBlocking {
        dataStore.updateData {
            SettingsData(
                mode = Mode.entries[preferences.getInt("mode", 0)],
                count = preferences.getInt("count", 1),

                soundEnabled = preferences.getBoolean("sound", true),
                vibrationEnabled = preferences.getBoolean("vibration", true),

                fullScreen = preferences.getBoolean("edge_to_edge", true),
                additionalButtonPadding = preferences.getFloat("additional_top_padding", 0.0f),
                circleSizeFactor = preferences.getFloat("circle_size_factor", 1.0f),

                circleLifetime = preferences.getLong("circle_lifetime", 1000),
                groupCircleLifetime = preferences.getLong("group_circle_lifetime", 1000),
                orderCircleLifetime = preferences.getLong("order_circle_lifetime", 1500)
            )
        }
    }

    preferences.edit {
        clear()
        putBoolean("migrated", true)
    }
}

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
 * @description SettingsManager manages the application settings.
 */

package com.uravgcode.chooser.utilities

import android.app.Activity.MODE_PRIVATE
import android.content.Context
import android.content.SharedPreferences
import com.uravgcode.chooser.views.Chooser

object SettingsManager {
    private lateinit var preferences: SharedPreferences

    fun init(context: Context) {
        preferences = context.getSharedPreferences("settings", MODE_PRIVATE)

        SoundManager.soundEnabled = soundEnabled
        Chooser.circleSize = circleSize
    }

    var mode: Mode
        get() = Mode.entries[preferences.getInt("mode", 0)]
        set(value) {
            savePreference("mode", value.ordinal)
        }

    var count: Int
        get() = preferences.getInt("count", 1)
        set(value) {
            savePreference("count", value)
        }

    var soundEnabled: Boolean
        get() = preferences.getBoolean("sound", true)
        set(value) {
            savePreference("sound", value)
            SoundManager.soundEnabled = value
        }

    var vibrationEnabled: Boolean
        get() = preferences.getBoolean("vibration", true)
        set(value) {
            savePreference("vibration", value)
        }

    var edgeToEdgeEnabled: Boolean
        get() = preferences.getBoolean("edge_to_edge", false)
        set(value) {
            savePreference("edge_to_edge", value)
        }

    var circleSize: Float
        get() = preferences.getFloat("circle_size", 50f)
        set(value) {
            savePreference("circle_size", value)
        }

    private fun savePreference(key: String, value: Any) {
        with(preferences.edit()) {
            when (value) {
                is Int -> putInt(key, value)
                is Float -> putFloat(key, value)
                is String -> putString(key, value)
                is Boolean -> putBoolean(key, value)
                else -> throw IllegalArgumentException("Invalid type for SharedPreferences")
            }
            apply()
        }
    }

}
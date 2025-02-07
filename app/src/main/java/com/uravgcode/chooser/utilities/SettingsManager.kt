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

import android.content.SharedPreferences
import kotlin.apply

class SettingsManager(private val preferences: SharedPreferences) {

    fun setMode(mode: Mode) {
        savePreference("mode", mode.ordinal)
    }

    fun getMode(): Mode {
        val mode = preferences.getInt("mode", 0)
        return Mode.entries[mode]
    }

    fun setCount(count: Int) {
        savePreference("count", count)
    }

    fun getCount(): Int {
        return preferences.getInt("count", 1)
    }

    fun setSoundEnabled(enabled: Boolean) {
        savePreference("sound", enabled)
    }

    fun isSoundEnabled(): Boolean {
        return preferences.getBoolean("sound", true)
    }

    fun setVibrationEnabled(enabled: Boolean) {
        savePreference("vibration", enabled)
    }

    fun isVibrationEnabled(): Boolean {
        return preferences.getBoolean("vibration", true)
    }

    fun setEdgeToEdgeEnabled(enabled: Boolean) {
        savePreference("edge_to_edge", enabled)
    }

    fun isEdgeToEdgeEnabled(): Boolean {
        return preferences.getBoolean("edge_to_edge", false)
    }

    fun getCircleSize(): Float {
        return preferences.getFloat("circle_size", 50f) // Default size is 50f
    }

    fun setCircleSize(size: Float) {
        preferences.edit().putFloat("circle_size", size).apply()
    }

    private fun savePreference(key: String, value: Any) {
        with(preferences.edit()) {
            when (value) {
                is Int -> putInt(key, value)
                is String -> putString(key, value)
                is Boolean -> putBoolean(key, value)
                else -> throw IllegalArgumentException("Invalid type for SharedPreferences")
            }
            apply()
        }
    }

}
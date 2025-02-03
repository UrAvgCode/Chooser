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

    private fun savePreference(key: String, value: Any) {
        with(preferences.edit()) {
            when (value) {
                is Int -> putInt(key, value)
                is String -> putString(key, value)
                else -> throw IllegalArgumentException("Invalid type for SharedPreferences")
            }
            apply()
        }
    }

}
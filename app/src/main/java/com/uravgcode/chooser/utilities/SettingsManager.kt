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
            preferences.edit().putInt("mode", value.ordinal).apply()
        }

    var count: Int
        get() = preferences.getInt("count", 1)
        set(value) {
            preferences.edit().putInt("count", value).apply()
        }

    var soundEnabled: Boolean
        get() = preferences.getBoolean("sound", true)
        set(value) {
            preferences.edit().putBoolean("sound", value).apply()
            SoundManager.soundEnabled = value
        }

    var vibrationEnabled: Boolean
        get() = preferences.getBoolean("vibration", true)
        set(value) {
            preferences.edit().putBoolean("vibration", value).apply()
        }

    var edgeToEdgeEnabled: Boolean
        get() = preferences.getBoolean("edge_to_edge", false)
        set(value) {
            preferences.edit().putBoolean("edge_to_edge", value).apply()
        }

    var circleSize: Float
        get() = preferences.getFloat("circle_size", 50f)
        set(value) {
            preferences.edit().putFloat("circle_size", value).apply()
        }
}
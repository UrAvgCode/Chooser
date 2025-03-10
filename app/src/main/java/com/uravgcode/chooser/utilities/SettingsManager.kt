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
import com.uravgcode.chooser.circles.Circle
import com.uravgcode.chooser.circles.GroupCircle
import com.uravgcode.chooser.circles.OrderCircle
import com.uravgcode.chooser.views.Chooser
import androidx.core.content.edit

object SettingsManager {
    private lateinit var preferences: SharedPreferences

    fun init(context: Context) {
        preferences = context.getSharedPreferences("settings", MODE_PRIVATE)

        SoundManager.soundEnabled = soundEnabled
        Chooser.vibrationEnabled = vibrationEnabled
        Chooser.circleSizeFactor = circleSizeFactor

        Circle.circleLifetime = circleLifetime
        GroupCircle.circleLifetime = groupCircleLifetime
        OrderCircle.circleLifetime = orderCircleLifetime
    }

    var mode: Mode
        get() = Mode.entries[preferences.getInt("mode", 0)]
        set(value) {
            preferences.edit { putInt("mode", value.ordinal) }
        }

    var count: Int
        get() = preferences.getInt("count", 1)
        set(value) {
            preferences.edit { putInt("count", value) }
        }

    var soundEnabled: Boolean
        get() = preferences.getBoolean("sound", true)
        set(value) {
            preferences.edit { putBoolean("sound", value) }
            SoundManager.soundEnabled = value
        }

    var vibrationEnabled: Boolean
        get() = preferences.getBoolean("vibration", true)
        set(value) {
            preferences.edit { putBoolean("vibration", value) }
            Chooser.vibrationEnabled = value
        }

    var edgeToEdgeEnabled: Boolean
        get() = preferences.getBoolean("edge_to_edge", false)
        set(value) {
            preferences.edit { putBoolean("edge_to_edge", value) }
        }

    var circleSizeFactor: Float
        get() = preferences.getFloat("circle_size_factor", 1.0f)
        set(value) {
            preferences.edit { putFloat("circle_size_factor", value) }
            Chooser.circleSizeFactor = value
        }

    var additionalTopPadding: Float
        get() = preferences.getFloat("additional_top_padding", 0.0f)
        set(value) {
            preferences.edit { putFloat("additional_top_padding", value) }
        }

    var circleLifetime: Long
        get() = preferences.getLong("circle_lifetime", 1000)
        set(value) {
            preferences.edit { putLong("circle_lifetime", value) }
            Circle.circleLifetime = value
        }

    var groupCircleLifetime: Long
        get() = preferences.getLong("group_circle_lifetime", 1000)
        set(value) {
            preferences.edit { putLong("group_circle_lifetime", value) }
            GroupCircle.circleLifetime = value
        }

    var orderCircleLifetime: Long
        get() = preferences.getLong("order_circle_lifetime", 1500)
        set(value) {
            preferences.edit { putLong("order_circle_lifetime", value) }
            OrderCircle.circleLifetime = value
        }


    fun resetToDefault(context: Context) {
        preferences.edit { clear() }
        init(context)
    }
}

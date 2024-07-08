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
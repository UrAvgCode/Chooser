package com.uravgcode.chooser

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import com.uravgcode.chooser.databinding.ActivityMainBinding
import com.uravgcode.chooser.utilities.Mode

class MainActivity : ComponentActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var preferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.chooser.motionLayout = binding.motionLayout

        preferences = getSharedPreferences("Settings", Context.MODE_PRIVATE)
        loadPreferences()

        binding.btnCount.setOnClickListener { updateCount() }
        binding.btnMode.setOnClickListener { updateMode() }
        binding.btnMode.setOnLongClickListener { toggleSound() }
    }

    private fun loadPreferences() {
        with(binding.chooser) {
            count = preferences.getInt("count", 1)
            mode = Mode.valueOf(preferences.getString("mode", "SINGLE")!!)

            updateModeUI()
            updateCountUI()
            motionLayout.jumpToState(mode.state())
        }
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

    private fun updateMode() {
        with(binding.chooser) {
            if (motionLayout.currentState != -1) {
                mode = mode.next()
                count = mode.initialCount()
                updateModeUI()
                updateCountUI()
            }
        }
    }

    private fun updateCount() {
        with(binding.chooser) {
            count = mode.nextCount(count)
            updateCountUI()
        }
    }

    private fun toggleSound(): Boolean {
        binding.chooser.toggleSound()
        return true
    }

    private fun updateModeUI() {
        val mode = binding.chooser.mode
        val drawable = mode.drawable()

        binding.btnMode.foreground = getDrawable(drawable)
        savePreference("mode", mode.toString())
        binding.motionLayout.transitionToState(mode.state())
    }

    private fun updateCountUI() {
        val count = binding.chooser.count
        binding.btnCount.text = count.toString()
        savePreference("count", count)
    }
}
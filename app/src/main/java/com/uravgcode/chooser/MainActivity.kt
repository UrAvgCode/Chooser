package com.uravgcode.chooser

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import com.uravgcode.chooser.Chooser.Mode.GROUP
import com.uravgcode.chooser.Chooser.Mode.ORDER
import com.uravgcode.chooser.Chooser.Mode.SINGLE
import com.uravgcode.chooser.databinding.ActivityMainBinding

class MainActivity : Activity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var preferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
            mode = Chooser.Mode.valueOf(preferences.getString("mode", "SINGLE")!!)

            updateModeUI()
            updateCountUI()
            motionLayout.progress = if (mode == ORDER) 1f else 0f
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
                mode = when (mode) {
                    SINGLE -> GROUP
                    GROUP -> ORDER
                    ORDER -> SINGLE
                }

                count = if (mode == GROUP) 2 else 1
                updateModeUI()
                updateCountUI()
            }
        }
    }

    private fun updateCount() {
        with(binding.chooser) {
            count = when (mode) {
                SINGLE -> count % 5 + 1
                GROUP -> (count - 1) % 4 + 2
                ORDER -> 1
            }

            updateCountUI()
        }
    }

    private fun toggleSound(): Boolean {
        binding.chooser.soundManager.toggleSound()
        return true
    }

    private fun updateModeUI() {
        val mode = binding.chooser.mode
        val drawable = when (mode) {
            SINGLE -> R.drawable.single_icon
            GROUP -> R.drawable.group_icon
            ORDER -> R.drawable.order_icon
        }

        binding.btnMode.foreground = getDrawable(drawable)
        savePreference("mode", mode.toString())
        binding.motionLayout.transitionToState(if (mode == ORDER) R.id.hideCounter else R.id.start)
    }

    private fun updateCountUI() {
        val count = binding.chooser.count
        binding.btnCount.text = count.toString()
        savePreference("count", count)
    }
}
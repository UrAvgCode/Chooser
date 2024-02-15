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
        binding.chooser.apply {
            count = preferences.getInt("count", 1)
            mode = Chooser.Mode.valueOf(preferences.getString("mode", "SINGLE")!!)
            updateModeUI()
            updateCountUI()

            binding.motionLayout.progress = if(mode == ORDER) 1f else 0f
        }
    }

    private fun updateMode() {
        if (binding.motionLayout.currentState != -1) {
            binding.chooser.apply {
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
        binding.chooser.apply {
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
        preferences.edit().putString("mode", mode.toString()).apply()
        binding.motionLayout.transitionToState(if (mode == ORDER) R.id.hideCounter else R.id.start)
    }

    private fun updateCountUI() {
        val count = binding.chooser.count
        binding.btnCount.text = count.toString()
        preferences.edit().putInt("count", count).apply()
    }
}
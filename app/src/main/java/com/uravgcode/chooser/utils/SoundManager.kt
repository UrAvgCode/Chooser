package com.uravgcode.chooser.utils

import android.content.Context
import android.media.MediaPlayer
import android.widget.Toast
import com.uravgcode.chooser.R

class SoundManager(val context: Context) {
    private val fingerUpSound = MediaPlayer.create(context, R.raw.finger_up)
    private val fingerDownSound = MediaPlayer.create(context, R.raw.finger_down)
    private val fingerChosenSound = MediaPlayer.create(context, R.raw.finger_chosen)

    private var soundEnabled = context.getSharedPreferences("Settings", Context.MODE_PRIVATE).getBoolean("soundEnabled", true)

    fun playFingerUp() {
        if (soundEnabled) fingerUpSound.start()
    }

    fun playFingerDown() {
        if (soundEnabled) fingerDownSound.start()
    }

    fun playFingerChosen() {
        if (soundEnabled) fingerChosenSound.start()
    }

    fun toggleSound() {
        soundEnabled = !soundEnabled
        Toast.makeText(context, "Sound ${if (soundEnabled) "enabled" else "disabled"}", Toast.LENGTH_SHORT).show()
        context.getSharedPreferences("Settings", Context.MODE_PRIVATE).edit().putBoolean("soundEnabled", soundEnabled).apply()
    }
}
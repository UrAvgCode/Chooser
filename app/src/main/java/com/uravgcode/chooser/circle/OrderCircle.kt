package com.uravgcode.chooser.circle

import android.graphics.Color
import androidx.core.graphics.ColorUtils

class OrderCircle(x: Float, y: Float, radius: Float) : Circle(x, y, radius) {

    override fun removeFinger() {
        hasFinger = false
        if (winnerCircle) {
            color = ColorUtils.blendARGB(color, Color.WHITE, 0.5f)
            paint.color = color
            strokePaint.color = color
            coreRadius *= 1.2f
        }
    }
}
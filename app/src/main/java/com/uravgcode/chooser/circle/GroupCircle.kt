package com.uravgcode.chooser.circle

import android.graphics.Color
import androidx.core.graphics.ColorUtils

class GroupCircle(x: Float, y: Float, radius: Float) : Circle(x, y, radius, Color.WHITE) {

    private var newColor = color

    init {
        strokePaintLight.color = Color.rgb(190, 190, 190)
    }

    fun fadeColor(fps: Int) {
        if (newColor != color) {
            newColor = ColorUtils.blendARGB(newColor, color, 3f / fps)
            paint.color = newColor
            strokePaint.color = newColor
            strokePaintLight.color = Color.argb(65, 255, 255, 255)
        }
    }
}
package com.uravgcode.chooser.circle

import android.graphics.Color
import androidx.core.graphics.ColorUtils
import kotlin.math.min

class GroupCircle(x: Float, y: Float, radius: Float) : Circle(x, y, radius, Color.WHITE) {

    private var blend = 0f

    init {
        strokePaintLight.color = Color.rgb(180, 180, 180)
    }

    override fun updateValues(deltaTime: Int) {
        super.updateValues(deltaTime)

        if (color != paint.color && blend <= 1) {
            val newColor = ColorUtils.blendARGB(Color.WHITE, color, blend)
            blend = min(blend + deltaTime * 0.01f, 1f)
            paint.color = newColor
            strokePaint.color = newColor
            strokePaintLight.color = ColorUtils.blendARGB(Color.rgb(180, 180, 180), Color.argb(65, 255, 255, 255), blend)
        }
    }
}
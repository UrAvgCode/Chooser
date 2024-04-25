package com.uravgcode.chooser.circle

import android.graphics.Color
import androidx.core.graphics.ColorUtils
import kotlin.math.min

class GroupCircle(x: Float, y: Float, radius: Float) : Circle(x, y, radius, Color.WHITE) {

    private var blend = 0f
    private val grayStroke = Color.rgb(180, 180, 180)
    private val whiteStroke = Color.argb(65, 255, 255, 255)

    init {
        ringPaintLight.color = grayStroke
    }

    override fun update(deltaTime: Int) {
        super.update(deltaTime)

        if (color != corePaint.color && blend <= 1) {
            blend = min(blend + deltaTime * 0.01f, 1f)
            val newColor = ColorUtils.blendARGB(Color.WHITE, color, blend)
            corePaint.color = newColor
            ringPaint.color = newColor
            ringPaintLight.color = ColorUtils.blendARGB(grayStroke, whiteStroke, blend)
        }
    }
}
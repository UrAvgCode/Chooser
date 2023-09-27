package com.uravgcode.chooser.utils

import android.graphics.Paint
import kotlin.math.abs
import kotlin.math.sin

class Number(var x: Float, var y: Float, color: Int, val number: Int, private val size: Float) {
    var alpha = 120f
    val textPaint = Paint()

    init {
        textPaint.color = color
        textPaint.textAlign = Paint.Align.CENTER
        textPaint.textSize = size
    }

    fun updateValues(deltaTime: Int) {
        textPaint.alpha = alpha.coerceIn(0f, 100f).toInt()
        alpha -= (deltaTime * 3f) / abs(alpha)
        x += sin(alpha / 15f) * deltaTime * 0.1f
        y -= deltaTime * 0.06f
    }
}
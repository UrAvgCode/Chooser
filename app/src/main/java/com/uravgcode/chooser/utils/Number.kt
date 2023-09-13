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

    fun updateValues(fps: Int) {
        textPaint.alpha = alpha.coerceIn(0f, 100f).toInt()
        alpha -= 3000f / (fps * abs(alpha))
        x += sin(alpha / 15f) * (size / 200f)
        y -= 60f / fps.toFloat()
    }
}
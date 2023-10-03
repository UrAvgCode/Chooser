package com.uravgcode.chooser.utils

import android.graphics.Paint
import kotlin.math.max
import kotlin.math.sin

class Number(var x: Float, var y: Float, color: Int, val number: Int, private var size: Float) {

    val textPaint = Paint()

    val originX = x

    var alpha = 125f
    var alphaSpeed = 0f

    var time = 0

    init {
        size = 140f
        textPaint.color = color
        textPaint.textAlign = Paint.Align.CENTER
        textPaint.textSize = size
    }

    fun updateValues(deltaTime: Int) {
        alphaSpeed += deltaTime * 0.00003f
        alpha = max(alpha - alphaSpeed * deltaTime, 0f)
        alphaSpeed += deltaTime * 0.00003f

        textPaint.alpha = alpha.coerceIn(0f, 100f).toInt()

        x = originX + sin(time * 0.003).toFloat() * size * 0.25f
        y -= size * deltaTime * 0.0005f

        time += deltaTime
    }
}
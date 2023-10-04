package com.uravgcode.chooser.utils

import android.graphics.Canvas
import android.graphics.Paint
import kotlin.math.sin

class Number(private var x: Float, private var y: Float, color: Int, private val number: Int, private var size: Float) {

    private val textPaint = Paint()

    private val xOrigin = x

    var alpha = 125f
    private var alphaSpeed = 0f

    private var time = 0

    init {
        size = 140f
        textPaint.color = color
        textPaint.textAlign = Paint.Align.CENTER
        textPaint.textSize = size
    }

    fun update(deltaTime: Int) {
        alphaSpeed += deltaTime * 0.00003f
        alpha -= alphaSpeed * deltaTime
        alphaSpeed += deltaTime * 0.00003f

        textPaint.alpha = alpha.coerceIn(0f, 100f).toInt()

        x = xOrigin + sin(time * 0.003).toFloat() * size * 0.25f
        y -= size * deltaTime * 0.0005f

        time += deltaTime
    }

    fun draw(canvas: Canvas) {
        canvas.drawText(number.toString(), x, y, textPaint)
    }
}
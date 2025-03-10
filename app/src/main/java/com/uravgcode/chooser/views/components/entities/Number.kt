/*
 * Copyright (C) 2025 UrAvgCode
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 *
 * @author UrAvgCode
 * @description Number represents the floating numbers displayed in the order mode.
 */

package com.uravgcode.chooser.views.components.entities

import android.graphics.Canvas
import android.graphics.Paint
import kotlin.math.sin

class Number(private var x: Float, private var y: Float, color: Int, private val number: Int, private var size: Float) {

    private val textPaint = Paint().apply {
        this.color = color
        textAlign = Paint.Align.CENTER
        textSize = size
    }

    private val xOrigin = x
    private var alpha = 255f
    private var alphaSpeed = 0f
    private var time = 0

    fun update(deltaTime: Int) {
        alphaSpeed += deltaTime * 0.00004f
        alpha -= alphaSpeed * deltaTime
        alphaSpeed += deltaTime * 0.00004f

        textPaint.alpha = alpha.coerceIn(0f, 255f).toInt()

        x = xOrigin + sin(time * 0.003).toFloat() * size * 0.25f
        y -= size * deltaTime * 0.0005f

        time += deltaTime
    }

    fun draw(canvas: Canvas) {
        canvas.drawText(number.toString(), x, y, textPaint)
    }

    fun isMarkedForDeletion(): Boolean = alpha <= 0
}

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
 * @description OrderCircle is the circle that is drawn in the order mode.
 */

package com.uravgcode.chooser.circles

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Handler
import android.os.Looper
import androidx.core.graphics.ColorUtils
import kotlin.math.sin

class OrderCircle(x: Float, y: Float, radius: Float, color: Int) : Circle(x, y, radius, color) {

    private val textPaint = Paint().apply {
        val hsvColor = FloatArray(3)
        Color.colorToHSV(this@OrderCircle.color, hsvColor)
        hsvColor[1] = 0.2f
        hsvColor[2] = 1f

        this.color = Color.HSVToColor(hsvColor)
        textAlign = Paint.Align.CENTER
    }

    private val textShadowPaint = Paint().apply {
        textAlign = Paint.Align.CENTER
        this.color = Color.argb(80, 0, 0, 0)
    }

    private var number: Int? = null

    override fun update(deltaTime: Int) {
        super.update(deltaTime)
        corePaint.color = if (coreRadius <= defaultRadius) {
            color
        } else {
            ColorUtils.blendARGB(color, Color.WHITE, 0.5f)
        }

        val textSize = coreRadius + radiusVariance * sin(timeMillis * 0.006).toFloat()
        textPaint.textSize = textSize
        textShadowPaint.textSize = textSize
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        number?.let {
            val y = y - (textPaint.descent() + textPaint.ascent()) / 2f
            val shadowOffset = textPaint.textSize * 0.04f
            canvas.drawText(it.toString(), x + shadowOffset, y + shadowOffset, textShadowPaint)
            canvas.drawText(it.toString(), x, y, textPaint)
        }
    }

    override fun setWinner() {
        winnerCircle = true
        coreRadius *= 1.2f
        number = ++counter
    }

    override fun removeFinger() {
        if (winnerCircle) {
            val handler = Handler(Looper.getMainLooper())
            handler.postDelayed({
                hasFinger = false
                counter--
            }, circleLifetime)
        } else {
            hasFinger = false
        }
    }

    companion object {
        private var counter = 0
        var circleLifetime = 1500L
    }
}
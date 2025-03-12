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
 * @description GroupCircle is the circle that is drawn in the group mode.
 */

package com.uravgcode.chooser.chooser.domain.circle

import android.graphics.Color
import android.os.Handler
import android.os.Looper
import androidx.core.graphics.ColorUtils
import kotlin.math.min

class GroupCircle(x: Float, y: Float, radius: Float) : Circle(x, y, radius, Color.WHITE) {

    private var blend = 0f
    private val grayStroke = Color.rgb(180, 180, 180)
    private val whiteStroke = Color.argb(65, 255, 255, 255)

    init {
        ringPaintLight.color = grayStroke
    }

    override fun update(deltaTime: Long) {
        super.update(deltaTime)
        if (color == corePaint.color || blend > 1f) return

        blend = min(blend + deltaTime * 0.01f, 1f)
        val newColor = ColorUtils.blendARGB(Color.WHITE, color, blend)
        corePaint.color = newColor
        ringPaint.color = newColor
        ringPaintLight.color = ColorUtils.blendARGB(grayStroke, whiteStroke, blend)
    }

    override fun removeFinger() {
        if (winnerCircle) {
            val handler = Handler(Looper.getMainLooper())
            handler.postDelayed({ hasFinger = false }, circleLifetime)
        } else {
            hasFinger = false
        }
    }

    companion object {
        var circleLifetime = 1000L
    }
}

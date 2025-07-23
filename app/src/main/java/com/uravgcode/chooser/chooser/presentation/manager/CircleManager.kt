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
 * @description CircleManager manages a map of all the circles.
 */

package com.uravgcode.chooser.chooser.presentation.manager

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.uravgcode.chooser.chooser.presentation.circle.Circle

class CircleManager {
    private val blackPaint = Paint().apply { color = Color.BLACK }

    private val activeCircles = mutableMapOf<Int, Circle>()
    private val deadCircles = mutableListOf<Circle>()

    val ids: MutableSet<Int> get() = activeCircles.keys
    val circles: MutableCollection<Circle> get() = activeCircles.values

    val count: Int get() = activeCircles.size
    fun isEmpty(): Boolean = activeCircles.isEmpty()

    fun add(key: Int, value: Circle): Circle? = activeCircles.put(key, value)
    fun get(key: Int): Circle? = activeCircles[key]

    fun remove(key: Int): Circle? {
        return activeCircles.remove(key)?.also {
            it.removeFinger()
            deadCircles.add(it)
        }
    }

    fun clear() {
        deadCircles.forEach {
            it.removeFingerImmediately()
        }
        activeCircles.values.forEach { circle ->
            circle.removeFingerImmediately()
            deadCircles.add(circle)
        }
        activeCircles.clear()
    }

    fun update(deltaTime: Long) {
        activeCircles.forEach { (_, circle) -> circle.update(deltaTime) }
        deadCircles.forEach { it.update(deltaTime) }
        deadCircles.removeAll { it.isMarkedForDeletion() }
    }

    fun draw(canvas: Canvas) {
        activeCircles.forEach { (_, circle) -> circle.draw(canvas) }
        deadCircles.forEach { it.draw(canvas) }
    }

    fun drawBlackCircles(canvas: Canvas, blackRadius: Float, scale: Float) {
        activeCircles.values.forEach { circle ->
            canvas.drawCircle(circle.x, circle.y, blackRadius, blackPaint)
        }

        deadCircles.filter { it.isWinner() }.forEach { circle ->
            var radius = blackRadius
            if (activeCircles.isNotEmpty()) radius *= circle.getRadius() / (50f * scale)
            canvas.drawCircle(circle.x, circle.y, radius, blackPaint)
        }
    }
}

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

package com.uravgcode.chooser.chooser.domain.manager

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.uravgcode.chooser.chooser.domain.circle.Circle

class CircleManager : MutableMap<Int, Circle> {
    private val blackPaint = Paint().apply { color = Color.BLACK }

    private val activeCircles = mutableMapOf<Int, Circle>()
    private val deadCircles = mutableListOf<Circle>()

    override val size: Int get() = activeCircles.size
    override val entries: MutableSet<MutableMap.MutableEntry<Int, Circle>> get() = activeCircles.entries
    override val keys: MutableSet<Int> get() = activeCircles.keys
    override val values: MutableCollection<Circle> get() = activeCircles.values
    override fun get(key: Int): Circle? = activeCircles[key]
    override fun isEmpty(): Boolean = activeCircles.isEmpty()
    override fun containsKey(key: Int): Boolean = activeCircles.containsKey(key)
    override fun containsValue(value: Circle): Boolean = activeCircles.containsValue(value)
    override fun put(key: Int, value: Circle): Circle? = activeCircles.put(key, value)
    override fun putAll(from: Map<out Int, Circle>) = activeCircles.putAll(from)
    override fun clear() = activeCircles.clear()

    override fun remove(key: Int): Circle? {
        return activeCircles.remove(key)?.also {
            it.removeFinger()
            deadCircles += it
        }
    }

    fun update(deltaTime: Int) {
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

package com.uravgcode.chooser.utilities

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.uravgcode.chooser.circles.Circle

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
    override fun containsValue(value: Circle): Boolean = activeCircles.containsValue(value)
    override fun containsKey(key: Int): Boolean = activeCircles.containsKey(key)

    override fun putAll(from: Map<out Int, Circle>) {
        for ((key, value) in from) {
            activeCircles[key] = value
        }
    }

    override fun put(key: Int, value: Circle): Circle? {
        val previousValue = activeCircles[key]
        activeCircles[key] = value
        return previousValue
    }

    override fun remove(key: Int): Circle? = activeCircles[key]?.let { circle ->
        circle.removeFinger()
        deadCircles.add(circle)
        activeCircles.remove(key)
    }

    override fun clear() {
        activeCircles.clear()
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
        activeCircles.forEach { (_, circle) ->
            canvas.drawCircle(circle.x, circle.y, blackRadius, blackPaint)
        }

        deadCircles.filter { it.isWinner() }.forEach { circle ->
            var radius = blackRadius
            if (activeCircles.isNotEmpty()) radius *= circle.getRadius() / (50f * scale)
            canvas.drawCircle(circle.x, circle.y, radius, blackPaint)
        }
    }
}
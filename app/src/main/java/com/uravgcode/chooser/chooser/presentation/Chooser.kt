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
 * @description Chooser is the main view of the application.
 */

package com.uravgcode.chooser.chooser.presentation

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.os.Build
import android.os.CombinedVibration
import android.os.Handler
import android.os.Looper
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.view.MotionEvent
import android.view.MotionEvent.ACTION_DOWN
import android.view.MotionEvent.ACTION_MOVE
import android.view.MotionEvent.ACTION_POINTER_DOWN
import android.view.MotionEvent.ACTION_POINTER_UP
import android.view.MotionEvent.ACTION_UP
import android.view.View
import com.uravgcode.chooser.chooser.domain.Mode
import com.uravgcode.chooser.chooser.presentation.circle.Circle
import com.uravgcode.chooser.chooser.presentation.circle.GroupCircle
import com.uravgcode.chooser.chooser.presentation.circle.OrderCircle
import com.uravgcode.chooser.chooser.presentation.component.Number
import com.uravgcode.chooser.chooser.presentation.manager.CircleManager
import com.uravgcode.chooser.chooser.presentation.manager.ColorManager
import com.uravgcode.chooser.chooser.presentation.manager.SoundManager
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sign

@SuppressLint("ViewConstructor")
class Chooser(
    context: Context,
    val setButtonVisibility: (Boolean) -> Unit
) : View(context) {

    private val screenHeight = resources.displayMetrics.heightPixels
    private val scale = resources.displayMetrics.density
    private var previousTime = System.currentTimeMillis()

    private val handler = Handler(Looper.getMainLooper())

    private val colorManager = ColorManager()
    private val soundManager = SoundManager(context)
    private val circleManager = CircleManager()
    private val numbers = mutableListOf<Number>()

    private var winnerChosen = false

    private val circleSize = 50.0f
    private val blackRadiusSize = 105.0f
    private var blackRadius = 0f
    private var blackSpeed = 1f

    var mode = Mode.SINGLE
    var count = 1

    init {
        setBackgroundColor(Color.BLACK)
    }

    override fun onDraw(canvas: Canvas) {
        val currentTime = System.currentTimeMillis()
        val deltaTime = currentTime - previousTime
        previousTime = currentTime

        circleManager.update(deltaTime)

        if (winnerChosen && mode == Mode.SINGLE) {
            blackSpeed += deltaTime * 0.04f * sign(blackSpeed)
            blackRadius = max(
                blackRadius + blackSpeed * deltaTime,
                blackRadiusSize * circleSizeFactor * scale
            )
            circleManager.drawBlackCircles(canvas, blackRadius, scale)
        }

        circleManager.draw(canvas)

        numbers.removeIf { number ->
            number.update(deltaTime)
            number.draw(canvas)
            number.isMarkedForDeletion()
        }

        invalidate()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event ?: return false

        val actionIndex = event.actionIndex
        val pointerId = event.getPointerId(actionIndex)

        when (event.actionMasked) {
            ACTION_DOWN, ACTION_POINTER_DOWN -> handleActionDown(event, actionIndex, pointerId)
            ACTION_MOVE -> handleActionMove(event)
            ACTION_UP, ACTION_POINTER_UP -> handleActionUp(pointerId)
        }
        return true
    }

    private fun handleActionDown(event: MotionEvent, actionIndex: Int, pointerId: Int) {
        if (winnerChosen) {
            if (clearOnTouch) {
                circleManager.clear()
                restoreDefaultState()
            }
            return
        }

        setButtonVisibility(false)

        soundManager.playFingerDown()
        circleManager.add(pointerId, createCircle(event.getX(actionIndex), event.getY(actionIndex)))
        handler.removeCallbacksAndMessages(null)
        handler.postDelayed(
            { selectWinner() },
            when (mode) {
                Mode.SINGLE -> singleDelay
                Mode.GROUP -> groupDelay
                Mode.ORDER -> orderDelay
            }
        )
    }

    private fun handleActionMove(event: MotionEvent) {
        for (index in 0 until event.pointerCount) {
            circleManager.get(event.getPointerId(index))?.let { circle ->
                circle.x = event.getX(index)
                circle.y = event.getY(index)
            }
        }
    }

    private fun handleActionUp(pointerId: Int) {
        val removed = circleManager.remove(pointerId)

        if (removed != null && !winnerChosen) {
            soundManager.playFingerUp()
        }

        if (circleManager.isEmpty()) {
            resetGame()
        }
    }

    private fun createCircle(x: Float, y: Float) = when (mode) {
        Mode.SINGLE -> Circle(x, y, circleSize * circleSizeFactor * scale, colorManager.nextColor())
        Mode.GROUP -> GroupCircle(x, y, circleSize * circleSizeFactor * scale)
        Mode.ORDER -> OrderCircle(x, y, circleSize * circleSizeFactor * scale, colorManager.nextColor())
    }

    private fun resetGame() {
        colorManager.generateRandomColorPalette(5)

        if (!winnerChosen) {
            setButtonVisibility(true)
            return
        }

        handler.postDelayed(
            { restoreDefaultState() },
            when (mode) {
                Mode.SINGLE -> Circle.circleLifetime
                Mode.GROUP -> GroupCircle.circleLifetime
                Mode.ORDER -> OrderCircle.circleLifetime
            }
        )
    }

    private fun restoreDefaultState() {
        blackSpeed = 1f
        handler.postDelayed({
            setButtonVisibility(true)
            winnerChosen = false
            setBackgroundColor(Color.BLACK)
        }, 150)
    }

    private fun selectWinner() {
        if (circleManager.count <= count) return

        when (mode) {
            Mode.SINGLE -> chooseFinger()
            Mode.GROUP -> chooseGroup()
            Mode.ORDER -> chooseOrder()
        }

        winnerChosen = true
    }

    private fun chooseFinger() {
        val ids = circleManager.ids.shuffled()
        val chosenIds = ids.takeLast(count)
        val removedIds = ids.dropLast(count)

        chosenIds.forEach { id ->
            circleManager.get(id)?.setWinner()
        }

        removedIds.forEach { id ->
            circleManager.remove(id)
        }

        val colors = circleManager.circles.map { it.color }
        setBackgroundColor(colorManager.averageColor(colors))

        blackSpeed = -1f
        blackRadius = screenHeight.toFloat()
        soundManager.playFingerChosen()
        vibrate(100)
    }

    private fun chooseGroup() {
        val circles = circleManager.circles.shuffled()
        val teamSize = circles.size / count
        var remainder = circles.size % count

        colorManager.generateRandomColorPalette(count)
        var index = 0

        repeat(count) {
            val size = if (remainder-- > 0) teamSize + 1 else teamSize
            val color = colorManager.nextColor()

            repeat(size) {
                val circle = circles[index++]
                circle.color = color
                circle.setWinner()
            }
        }

        vibrate(100)
    }

    private fun chooseOrder(number: Int = 1) {
        val circles = circleManager.circles.filter { !it.isWinner() }
        if (circles.isEmpty()) return
        val circle = circles.random()

        circle.setWinner()
        numbers.add(
            Number(
                circle.x,
                circle.y - circleSize * circleSizeFactor * scale,
                circle.color,
                number,
                circleSize * circleSizeFactor * scale
            )
        )
        soundManager.playFingerUp()
        vibrate(40)

        handler.postDelayed(
            { chooseOrder(number + 1) },
            min(3000L / circleManager.count, 800L)
        )
    }

    private fun vibrate(millis: Long) {
        if (!vibrationEnabled) return
        val effect = VibrationEffect.createOneShot(millis, VibrationEffect.DEFAULT_AMPLITUDE)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val systemService = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE)
            val vibratorManager = systemService as VibratorManager
            vibratorManager.vibrate(CombinedVibration.createParallel(effect))
        } else {
            @Suppress("DEPRECATION")
            val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            vibrator.vibrate(effect)
        }
    }

    companion object {
        var vibrationEnabled = true
        var circleSizeFactor = 1.0f

        var singleDelay = 3000L
        var groupDelay = 3000L
        var orderDelay = 3000L

        var clearOnTouch = true
    }
}

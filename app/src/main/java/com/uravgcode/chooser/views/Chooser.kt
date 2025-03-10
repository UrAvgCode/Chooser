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

package com.uravgcode.chooser.views

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
import com.uravgcode.chooser.views.components.circles.Circle
import com.uravgcode.chooser.views.components.circles.GroupCircle
import com.uravgcode.chooser.views.components.circles.OrderCircle
import com.uravgcode.chooser.utilities.CircleManager
import com.uravgcode.chooser.utilities.ColorManager
import com.uravgcode.chooser.utilities.Mode.GROUP
import com.uravgcode.chooser.utilities.Mode.ORDER
import com.uravgcode.chooser.utilities.Mode.SINGLE
import com.uravgcode.chooser.utilities.SoundManager
import com.uravgcode.chooser.views.components.entities.Number
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sign
import kotlin.random.Random

@SuppressLint("ViewConstructor")
class Chooser(
    context: Context,
    val setButtonVisibility: (Boolean) -> Unit
) : View(context) {

    private val screenHeight = resources.displayMetrics.heightPixels
    private val scale = resources.displayMetrics.density
    private var lastTime = System.currentTimeMillis()

    private val handler = Handler(Looper.getMainLooper())

    private val colorManager = ColorManager()
    private val soundManager = SoundManager(context)
    private val circles = CircleManager()
    private val numbers = mutableListOf<Number>()

    private var winnerChosen = false

    private val circleSize = 50.0f
    private val blackRadiusSize = 105.0f
    private var blackRadius = 0f
    private var blackSpeed = 1f

    var mode = SINGLE
    var count = 1

    init {
        setBackgroundColor(Color.BLACK)
    }

    override fun onDraw(canvas: Canvas) {
        val deltaTime = (System.currentTimeMillis() - lastTime).toInt()
        lastTime = System.currentTimeMillis()

        circles.update(deltaTime)

        if (winnerChosen && mode == SINGLE) {
            blackSpeed += deltaTime * 0.04f * sign(blackSpeed)
            blackRadius = max(
                blackRadius + blackSpeed * deltaTime,
                blackRadiusSize * circleSizeFactor * scale
            )
            circles.drawBlackCircles(canvas, blackRadius, scale)
        }

        circles.draw(canvas)

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
        if (winnerChosen) return

        setButtonVisibility(false)

        soundManager.playFingerDown()
        circles[pointerId] = createCircle(event.getX(actionIndex), event.getY(actionIndex))
        handler.removeCallbacksAndMessages(null)
        handler.postDelayed({ selectWinner() }, 3000)
    }

    private fun handleActionMove(event: MotionEvent) {
        for (index in 0 until event.pointerCount) {
            circles[event.getPointerId(index)]?.let { circle ->
                circle.x = event.getX(index)
                circle.y = event.getY(index)
            }
        }
    }

    private fun handleActionUp(pointerId: Int) {
        circles.remove(pointerId)
        if (!winnerChosen) soundManager.playFingerUp()
        if (circles.isEmpty()) resetGame()
    }

    private fun createCircle(x: Float, y: Float) = when (mode) {
        SINGLE -> Circle(x, y, circleSize * circleSizeFactor * scale, colorManager.nextColor())
        GROUP -> GroupCircle(x, y, circleSize * circleSizeFactor * scale)
        ORDER -> OrderCircle(x, y, circleSize * circleSizeFactor * scale, colorManager.nextColor())
    }

    private fun resetGame() {
        colorManager.generateRandomColorPalette(5)

        if (!winnerChosen) {
            setButtonVisibility(true)
            return
        }

        handler.postDelayed(
            {
                blackSpeed = 1f
                handler.postDelayed({
                    setButtonVisibility(true)
                    winnerChosen = false
                    setBackgroundColor(Color.BLACK)
                }, 150)
            },
            when (mode) {
                SINGLE -> Circle.circleLifetime
                GROUP -> GroupCircle.circleLifetime
                ORDER -> OrderCircle.circleLifetime
            }
        )
    }

    private fun selectWinner() {
        if (circles.size <= count) return

        when (mode) {
            SINGLE -> chooseFinger()
            GROUP -> chooseGroup()
            ORDER -> chooseOrder()
        }

        winnerChosen = true
    }

    private fun chooseFinger() {
        val indexList = circles.keys.toMutableList()
        indexList.shuffle()

        indexList.takeLast(count).forEach { index ->
            circles[index]!!.setWinner()
        }

        indexList.dropLast(count).forEach { index ->
            circles.remove(index)
        }

        val colors = circles.values.map { it.color }
        setBackgroundColor(colorManager.averageColor(colors))

        blackSpeed = -1f
        blackRadius = screenHeight.toFloat()
        soundManager.playFingerChosen()
        vibrate(100)
    }

    private fun chooseGroup() {
        val indexList = circles.keys.toMutableList()
        val teamSize = circles.size / count
        var remainder = circles.size % count

        colorManager.generateRandomColorPalette(count)
        repeat(count) {
            val size = if (remainder-- > 0) teamSize + 1 else teamSize

            val color = colorManager.nextColor()
            repeat(size) {
                val randomIndex = Random.nextInt(indexList.size)
                val circle = circles[indexList[randomIndex]]
                circle!!.color = color
                circle.setWinner()
                indexList.removeAt(randomIndex)
            }
        }

        vibrate(100)
    }

    private fun chooseOrder(number: Int = 1) {
        val selectionMap = circles.filterValues { !it.isWinner() }
        if (selectionMap.isEmpty()) return

        val randomIndex = selectionMap.keys.random()
        val circle = circles[randomIndex]!!

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

        handler.postDelayed({
            chooseOrder(number + 1)
        }, (min(3000 / circles.size, 800).toLong()))
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
    }
}

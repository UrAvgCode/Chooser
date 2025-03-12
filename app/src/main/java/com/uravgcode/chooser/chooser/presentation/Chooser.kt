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

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.PointerId
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import com.uravgcode.chooser.chooser.domain.circle.Circle
import com.uravgcode.chooser.chooser.domain.circle.GroupCircle
import com.uravgcode.chooser.chooser.domain.circle.OrderCircle
import com.uravgcode.chooser.chooser.domain.entity.Number
import com.uravgcode.chooser.chooser.domain.manager.CircleManager
import com.uravgcode.chooser.chooser.domain.manager.ColorManager
import com.uravgcode.chooser.chooser.domain.manager.SoundManager
import com.uravgcode.chooser.chooser.domain.manager.VibrationService
import com.uravgcode.chooser.chooser.domain.model.Mode
import com.uravgcode.chooser.chooser.domain.model.Mode.GROUP
import com.uravgcode.chooser.chooser.domain.model.Mode.ORDER
import com.uravgcode.chooser.chooser.domain.model.Mode.SINGLE
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sign
import kotlin.random.Random

@Composable
fun Chooser(
    mode: Mode,
    count: Int,
    setButtonVisibility: (Boolean) -> Unit,
    circleSizeFactor: Float,
    vibrationEnabled: Boolean,
) {
    val context = LocalContext.current
    val density = LocalDensity.current.density
    val screenHeight = LocalContext.current.resources.displayMetrics.heightPixels.toFloat()

    val coroutineScope = rememberCoroutineScope()
    var selectionJob by remember { mutableStateOf<Job?>(null) }

    var lastUpdateTime by remember { mutableLongStateOf(System.currentTimeMillis()) }

    val mode by rememberUpdatedState(mode)
    val count by rememberUpdatedState(count)

    val colorManager = remember { ColorManager() }
    val soundManager = remember { SoundManager(context) }
    val vibrationService = remember { VibrationService(context) }

    val circles = remember { CircleManager() }
    val numbers = remember { mutableStateListOf<Number>() }

    var winnerChosen by remember { mutableStateOf(false) }

    val circleSize = remember { 50.0f * circleSizeFactor * density }
    val blackRadiusSize = remember { 105.0f * circleSizeFactor * density }

    var blackRadius by remember { mutableFloatStateOf(0f) }
    var blackSpeed by remember { mutableFloatStateOf(1f) }
    var backgroundColor by remember { mutableStateOf(Color.Black) }

    LaunchedEffect(colorManager) {
        colorManager.generateRandomColorPalette(5)
    }

    fun createCircle(x: Float, y: Float) = when (mode) {
        SINGLE -> Circle(x, y, circleSize, colorManager.nextColor())
        GROUP -> GroupCircle(x, y, circleSize)
        ORDER -> OrderCircle(x, y, circleSize, colorManager.nextColor())
    }

    fun resetGame() {
        colorManager.generateRandomColorPalette(5)

        if (!winnerChosen) {
            setButtonVisibility(true)
            return
        }

        coroutineScope.launch {
            val circleLifetime = when (mode) {
                SINGLE -> Circle.circleLifetime
                GROUP -> GroupCircle.circleLifetime
                ORDER -> OrderCircle.circleLifetime
            }

            delay(circleLifetime)
            blackSpeed = 1f

            delay(150)
            setButtonVisibility(true)
            winnerChosen = false
            backgroundColor = Color.Black
        }
    }

    fun chooseFinger() {
        val indexList = circles.keys.toMutableList()
        indexList.shuffle()

        indexList.takeLast(count).forEach { index ->
            circles[index]!!.setWinner()
        }

        indexList.dropLast(count).forEach { index ->
            circles.remove(index)
        }

        val colors = circles.values.map { it.color }
        backgroundColor = Color(colorManager.averageColor(colors))

        blackSpeed = -1f
        blackRadius = screenHeight
        soundManager.playFingerChosen()

        vibrationService.vibrate(100, vibrationEnabled)
    }

    fun chooseGroup() {
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

        vibrationService.vibrate(100, vibrationEnabled)
    }

    fun chooseOrder(number: Int = 1) {
        val selectionMap = circles.filterValues { !it.isWinner() }
        if (selectionMap.isEmpty()) return

        val randomIndex = selectionMap.keys.random()
        val circle = circles[randomIndex]!!

        circle.setWinner()
        numbers.add(
            Number(
                circle.x,
                circle.y - circleSize,
                circle.color,
                number,
                circleSize
            )
        )
        soundManager.playFingerUp()
        vibrationService.vibrate(40, vibrationEnabled)

        coroutineScope.launch {
            delay(min(3000 / circles.size, 800).toLong())
            chooseOrder(number + 1)
        }
    }

    fun selectWinner() {
        if (circles.size <= count) return

        when (mode) {
            SINGLE -> chooseFinger()
            GROUP -> chooseGroup()
            ORDER -> chooseOrder()
        }

        winnerChosen = true
    }

    fun onTouchDown(pointerId: PointerId, position: Offset) {
        if (winnerChosen) return

        setButtonVisibility(false)
        soundManager.playFingerDown()

        circles[pointerId] = createCircle(position.x, position.y)

        selectionJob?.cancel()
        selectionJob = coroutineScope.launch {
            delay(3000)
            selectWinner()
        }
    }

    fun onDrag(pointerId: PointerId, position: Offset) {
        circles[pointerId]?.let { circle ->
            circle.x = position.x
            circle.y = position.y
        }
    }

    fun onTouchUp(pointerId: PointerId) {
        circles.remove(pointerId)

        if (!winnerChosen) soundManager.playFingerUp()
        if (circles.isEmpty()) resetGame()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .pointerInput(Unit) {
                onTouchEvent(
                    onTouchDown = { pointerId, position ->
                        onTouchDown(pointerId, position)
                    },
                    onDrag = { pointerId, position ->
                        onDrag(pointerId, position)
                    },
                    onTouchUp = { pointerId ->
                        onTouchUp(pointerId)
                    }
                )
            }
    ) {
        Canvas(
            modifier = Modifier.fillMaxSize()
        ) {
            val currentTime = System.currentTimeMillis()
            val deltaTime = (currentTime - lastUpdateTime)
            lastUpdateTime = currentTime

            val canvas = drawContext.canvas.nativeCanvas

            circles.update(deltaTime)

            if (winnerChosen && mode == SINGLE) {
                blackSpeed += deltaTime * 0.04f * sign(blackSpeed)
                blackRadius = max(
                    blackRadius + blackSpeed * deltaTime,
                    blackRadiusSize
                )
                circles.drawBlackCircles(canvas, blackRadius, density)
            }

            circles.draw(canvas)

            numbers.removeIf { number ->
                number.update(deltaTime)
                number.draw(canvas)
                number.isMarkedForDeletion()
            }
        }
    }
}

suspend fun PointerInputScope.onTouchEvent(
    onTouchDown: (PointerId, Offset) -> Unit,
    onDrag: (PointerId, Offset) -> Unit,
    onTouchUp: (PointerId) -> Unit
) {
    awaitPointerEventScope {
        while (true) {
            val event = awaitPointerEvent()
            for (change in event.changes) {
                when {
                    change.pressed && change.previousPressed -> {
                        onDrag(change.id, change.position)
                    }

                    change.pressed -> {
                        onTouchDown(change.id, change.position)
                    }

                    !change.pressed && change.previousPressed -> {
                        onTouchUp(change.id)
                    }
                }
                change.consume()
            }
        }
    }
}

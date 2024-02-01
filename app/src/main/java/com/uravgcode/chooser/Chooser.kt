package com.uravgcode.chooser

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import android.os.Build
import android.os.CombinedVibration
import android.os.Handler
import android.os.Looper
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.constraintlayout.motion.widget.MotionLayout
import com.uravgcode.chooser.Chooser.Mode.*
import com.uravgcode.chooser.circle.Circle
import com.uravgcode.chooser.circle.GroupCircle
import com.uravgcode.chooser.circle.OrderCircle
import com.uravgcode.chooser.utils.ColorGenerator
import com.uravgcode.chooser.utils.Number
import com.uravgcode.chooser.utils.SoundManager
import kotlin.math.max
import kotlin.math.sign
import kotlin.random.Random

class Chooser(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private val screenHeight = resources.displayMetrics.heightPixels
    private val scale = resources.displayMetrics.density
    private var lastTime = System.currentTimeMillis()

    var motionLayout: MotionLayout? = null
    val soundManager = SoundManager(context!!)

    private val handler = Handler(Looper.getMainLooper())

    private val mapOfCircles = mutableMapOf<Int, Circle>()
    private val listOfDeadCircles = mutableListOf<Circle>()
    private val listOfNumbers = mutableListOf<Number>()

    private var winnerChosen = false

    private val blackPaint = Paint()
    private var blackRadius = 0f
    private var blackSpeed = 1f

    enum class Mode {
        SINGLE, GROUP, ORDER
    }

    var mode = SINGLE
    var count = 1

    init {
        setBackgroundColor(Color.BLACK)
        blackPaint.color = Color.BLACK
    }

    override fun onDraw(canvas: Canvas) {
        val deltaTime = (System.currentTimeMillis() - lastTime).toInt()
        lastTime = System.currentTimeMillis()

        listOfDeadCircles.removeIf { it.coreRadius <= 0 }

        val circles = mapOfCircles.values + listOfDeadCircles

        if (winnerChosen && mode == SINGLE) {
            blackSpeed += deltaTime * 0.02f * sign(blackSpeed)
            blackRadius = max(blackRadius + blackSpeed * deltaTime, 105 * scale)
            blackSpeed += deltaTime * 0.02f * sign(blackSpeed)

            circles.filter { it.isWinner() }.forEach { circle ->
                var radius = blackRadius
                if (mapOfCircles.isNotEmpty()) radius *= circle.coreRadius / (50f * scale)
                canvas.drawCircle(circle.x, circle.y, radius, blackPaint)
            }
        }

        circles.forEach { circle ->
            circle.update(deltaTime)
            circle.draw(canvas)
        }

        listOfNumbers.removeIf { number ->
            number.update(deltaTime)
            number.draw(canvas)
            number.alpha <= 0
        }

        invalidate()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val pointerIndex = event!!.actionIndex
        val pointerId = event.getPointerId(pointerIndex)

        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_POINTER_DOWN -> {
                if (!winnerChosen) {
                    setButtonVisibility(false)
                    val pos = PointF(event.getX(pointerIndex), event.getY(pointerIndex))

                    soundManager.playFingerDown()
                    mapOfCircles[pointerId] = createCircle(pos.x, pos.y)
                    handler.removeCallbacksAndMessages(null)
                    handler.postDelayed({ selectWinner() }, 3000)
                }
            }

            MotionEvent.ACTION_MOVE -> {
                for (index in 0 until event.pointerCount) {
                    mapOfCircles[event.getPointerId(index)]?.let { circle ->
                        circle.x = event.getX(index)
                        circle.y = event.getY(index)
                    }
                }
            }

            MotionEvent.ACTION_UP, MotionEvent.ACTION_POINTER_UP -> {
                removeCircle(pointerId)
                if (!winnerChosen) soundManager.playFingerUp()
                if (mapOfCircles.isEmpty()) resetGame()
            }
        }
        return true
    }

    private fun createCircle(x: Float, y: Float) = when (mode) {
        SINGLE -> Circle(x, y, 50f * scale)
        GROUP -> GroupCircle(x, y, 50f * scale)
        ORDER -> OrderCircle(x, y, 50f * scale)
    }

    private fun resetGame() {
        ColorGenerator.newColorPalette(5)
        if (winnerChosen) {
            handler.postDelayed({
                blackSpeed = 1f
                handler.postDelayed({
                    setButtonVisibility(true)
                    winnerChosen = false
                    setBackgroundColor(Color.BLACK)
                }, 150)
            }, 1000)
        } else {
            setButtonVisibility(true)
        }
    }

    private fun selectWinner() {
        if (mapOfCircles.size > count) {
            when (mode) {
                SINGLE -> chooseFinger()
                GROUP -> chooseGroup()
                ORDER -> chooseOrder()
            }
            vibrate()
            winnerChosen = true
        }
    }

    private fun chooseFinger() {
        val indexList = mapOfCircles.keys.toMutableList()
        indexList.shuffle()

        indexList.takeLast(count).forEach { index ->
            mapOfCircles[index]!!.setWinner()
        }

        indexList.dropLast(count).forEach { index ->
            removeCircle(index)
        }

        val colors = mapOfCircles.values.map { it.color }
        setBackgroundColor(ColorGenerator.averageColor(colors))

        blackSpeed = -1f
        blackRadius = screenHeight.toFloat()
        soundManager.playFingerChosen()
    }

    private fun chooseGroup() {
        val indexList = mapOfCircles.keys.toMutableList()
        val teamSize = mapOfCircles.size / count
        var remainder = mapOfCircles.size % count

        ColorGenerator.newColorPalette(count)
        repeat(count) {
            val size = if (remainder-- > 0) teamSize + 1 else teamSize

            val color = ColorGenerator.nextColor()
            repeat(size) {
                val randomIndex = Random.nextInt(indexList.size)
                val circle = mapOfCircles[indexList[randomIndex]]
                circle!!.color = color
                circle.setWinner()
                indexList.removeAt(randomIndex)
            }
        }
    }

    private fun chooseOrder(number: Int = 1) {
        val selectionMap = mapOfCircles.filterValues { !it.isWinner() }
        if (selectionMap.isEmpty()) return

        val randomIndex = selectionMap.keys.random()
        val circle = mapOfCircles[randomIndex]!!

        circle.setWinner()
        listOfNumbers.add(Number(circle.x, circle.y - 50 * scale, circle.color, number, 50 * scale))
        soundManager.playFingerUp()

        handler.postDelayed({
            if (selectionMap.size > 1) {
                chooseOrder(number + 1)
                vibrate()
            } else {
                OrderCircle.counter = 0
            }
        }, 1000)
    }

    private fun removeCircle(pointerId: Int) {
        mapOfCircles[pointerId]?.let { circle ->
            circle.removeFinger()
            listOfDeadCircles.add(circle)
            mapOfCircles.remove(pointerId)
        }
    }

    private fun setButtonVisibility(visible: Boolean) {
        motionLayout?.transitionToState(
            if (visible) when (mode) {
                SINGLE, GROUP -> R.id.start
                ORDER -> R.id.hideCounter
            } else R.id.end
        )
    }

    private fun vibrate() {
        val effect = VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.vibrate(CombinedVibration.createParallel(effect))
        } else {
            @Suppress("DEPRECATION")
            val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            vibrator.vibrate(effect)
        }
    }
}
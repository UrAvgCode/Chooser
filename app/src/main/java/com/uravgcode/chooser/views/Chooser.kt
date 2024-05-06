package com.uravgcode.chooser.views

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
import com.uravgcode.chooser.R
import com.uravgcode.chooser.circles.Circle
import com.uravgcode.chooser.circles.GroupCircle
import com.uravgcode.chooser.circles.OrderCircle
import com.uravgcode.chooser.utilities.ColorGenerator
import com.uravgcode.chooser.utilities.Number
import com.uravgcode.chooser.utilities.SoundManager
import com.uravgcode.chooser.views.Chooser.Mode.GROUP
import com.uravgcode.chooser.views.Chooser.Mode.ORDER
import com.uravgcode.chooser.views.Chooser.Mode.SINGLE
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sign
import kotlin.random.Random

class Chooser(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private val screenHeight = resources.displayMetrics.heightPixels
    private val scale = resources.displayMetrics.density
    private var lastTime = System.currentTimeMillis()

    lateinit var motionLayout: MotionLayout
    val soundManager = SoundManager(context)

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

        listOfDeadCircles.removeIf { it.isMarkedForDeletion() }

        val circles = mapOfCircles.values + listOfDeadCircles

        if (winnerChosen && mode == SINGLE) {
            blackSpeed += deltaTime * 0.02f * sign(blackSpeed)
            blackRadius = max(blackRadius + blackSpeed * deltaTime, 105 * scale)
            blackSpeed += deltaTime * 0.02f * sign(blackSpeed)

            circles.filter { it.isWinner() }.forEach { circle ->
                var radius = blackRadius
                if (mapOfCircles.isNotEmpty()) radius *= circle.getRadius() / (50f * scale)
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
            number.isMarkedForDeletion()
        }

        invalidate()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event ?: return false

        val pointerIndex = event.actionIndex
        val pointerId = event.getPointerId(pointerIndex)

        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_POINTER_DOWN -> handleActionDown(event, pointerIndex, pointerId)
            MotionEvent.ACTION_MOVE -> handleActionMove(event)
            MotionEvent.ACTION_UP, MotionEvent.ACTION_POINTER_UP -> handleActionUp(pointerId)
        }
        return true
    }

    private fun handleActionDown(event: MotionEvent, pointerIndex: Int, pointerId: Int) {
        if (!winnerChosen) {
            setButtonVisibility(false)
            val pos = PointF(event.getX(pointerIndex), event.getY(pointerIndex))

            soundManager.playFingerDown()
            mapOfCircles[pointerId] = createCircle(pos.x, pos.y)
            handler.removeCallbacksAndMessages(null)
            handler.postDelayed({ selectWinner() }, 3000)
        }
    }

    private fun handleActionMove(event: MotionEvent) {
        for (index in 0 until event.pointerCount) {
            mapOfCircles[event.getPointerId(index)]?.let { circle ->
                circle.x = event.getX(index)
                circle.y = event.getY(index)
            }
        }
    }

    private fun handleActionUp(pointerId: Int) {
        removeCircle(pointerId)
        if (!winnerChosen) soundManager.playFingerUp()
        if (mapOfCircles.isEmpty()) resetGame()
    }

    private fun createCircle(x: Float, y: Float) = when (mode) {
        SINGLE -> Circle(x, y, 50f * scale)
        GROUP -> GroupCircle(x, y, 50f * scale)
        ORDER -> OrderCircle(x, y, 50f * scale)
    }

    private fun resetGame() {
        ColorGenerator.generateRandomColorPalette(5)
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
        vibrate(100)
    }

    private fun chooseGroup() {
        val indexList = mapOfCircles.keys.toMutableList()
        val teamSize = mapOfCircles.size / count
        var remainder = mapOfCircles.size % count

        ColorGenerator.generateRandomColorPalette(count)
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

        vibrate(100)
    }

    private fun chooseOrder(number: Int = 1) {
        val selectionMap = mapOfCircles.filterValues { !it.isWinner() }
        if (selectionMap.isEmpty()) return

        val randomIndex = selectionMap.keys.random()
        val circle = mapOfCircles[randomIndex]!!

        circle.setWinner()
        listOfNumbers.add(Number(circle.x, circle.y - 50 * scale, circle.color, number, 50 * scale))
        soundManager.playFingerUp()
        vibrate(40)

        handler.postDelayed({
            if (selectionMap.size > 1) {
                chooseOrder(number + 1)
            } else {
                OrderCircle.counter = 0
            }
        }, (min(3000 / mapOfCircles.size, 800).toLong()))
    }

    private fun removeCircle(pointerId: Int) {
        mapOfCircles[pointerId]?.let { circle ->
            circle.removeFinger()
            listOfDeadCircles.add(circle)
            mapOfCircles.remove(pointerId)
        }
    }

    private fun setButtonVisibility(visible: Boolean) {
        motionLayout.transitionToState(
            if (visible) when (mode) {
                SINGLE, GROUP -> R.id.start
                ORDER -> R.id.hideCounter
            } else R.id.end
        )
    }

    private fun vibrate(millis: Long) {
        val effect = VibrationEffect.createOneShot(millis, VibrationEffect.DEFAULT_AMPLITUDE)

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
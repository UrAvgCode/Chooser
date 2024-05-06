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
import android.view.MotionEvent.ACTION_DOWN
import android.view.MotionEvent.ACTION_MOVE
import android.view.MotionEvent.ACTION_POINTER_DOWN
import android.view.MotionEvent.ACTION_POINTER_UP
import android.view.MotionEvent.ACTION_UP
import android.view.View
import androidx.constraintlayout.motion.widget.MotionLayout
import com.uravgcode.chooser.R
import com.uravgcode.chooser.circles.Circle
import com.uravgcode.chooser.circles.GroupCircle
import com.uravgcode.chooser.circles.OrderCircle
import com.uravgcode.chooser.utilities.ColorManager
import com.uravgcode.chooser.utilities.Mode.GROUP
import com.uravgcode.chooser.utilities.Mode.ORDER
import com.uravgcode.chooser.utilities.Mode.SINGLE
import com.uravgcode.chooser.utilities.Number
import com.uravgcode.chooser.utilities.SoundManager
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sign
import kotlin.random.Random

class Chooser(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private val screenHeight = resources.displayMetrics.heightPixels
    private val scale = resources.displayMetrics.density
    private var lastTime = System.currentTimeMillis()

    lateinit var motionLayout: MotionLayout
    private val handler = Handler(Looper.getMainLooper())

    private val colorManager = ColorManager()
    private val soundManager = SoundManager(context)

    private val mapOfCircles = mutableMapOf<Int, Circle>()
    private val listOfDeadCircles = mutableListOf<Circle>()
    private val listOfNumbers = mutableListOf<Number>()

    private var winnerChosen = false

    private val blackPaint = Paint()
    private var blackRadius = 0f
    private var blackSpeed = 1f

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
        if (!winnerChosen) {
            setButtonVisibility(false)
            val pos = PointF(event.getX(actionIndex), event.getY(actionIndex))

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
        SINGLE -> Circle(x, y, 50f * scale, colorManager.nextColor())
        GROUP -> GroupCircle(x, y, 50f * scale)
        ORDER -> OrderCircle(x, y, 50f * scale, colorManager.nextColor())
    }

    private fun resetGame() {
        colorManager.generateRandomColorPalette(5)
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
        setBackgroundColor(colorManager.averageColor(colors))

        blackSpeed = -1f
        blackRadius = screenHeight.toFloat()
        soundManager.playFingerChosen()
        vibrate(100)
    }

    private fun chooseGroup() {
        val indexList = mapOfCircles.keys.toMutableList()
        val teamSize = mapOfCircles.size / count
        var remainder = mapOfCircles.size % count

        colorManager.generateRandomColorPalette(count)
        repeat(count) {
            val size = if (remainder-- > 0) teamSize + 1 else teamSize

            val color = colorManager.nextColor()
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
        motionLayout.transitionToState(if (visible) mode.state() else R.id.end)
    }

    private fun vibrate(millis: Long) {
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

    fun toggleSound() {
        soundManager.toggleSound()
    }
}
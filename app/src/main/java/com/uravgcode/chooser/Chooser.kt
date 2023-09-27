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
import androidx.core.graphics.ColorUtils
import com.uravgcode.chooser.circle.Circle
import com.uravgcode.chooser.circle.GroupCircle
import com.uravgcode.chooser.circle.OrderCircle
import com.uravgcode.chooser.utils.ColorGenerator
import com.uravgcode.chooser.utils.Number
import kotlin.math.max
import kotlin.math.sign
import kotlin.random.Random

class Chooser(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private val screenHeight = resources.displayMetrics.heightPixels
    private val scale = resources.displayMetrics.density
    private var lastTime = System.currentTimeMillis()
    var motionLayout: MotionLayout? = null

    private val handler = Handler(Looper.getMainLooper())

    private var mapOfCircles = mutableMapOf<Int, Circle>()
    private var listOfDeadCircles = mutableListOf<Circle>()
    private val listOfNumbers = mutableListOf<Number>()

    private var winnerChosen = false

    private val blackPaint = Paint()
    private var blackRadius = 0f
    private var blackSpeed = 1f

    enum class Mode {
        SINGLE, GROUP, ORDER
    }

    var mode = Mode.SINGLE
    var count = 1

    init {
        setBackgroundColor(Color.BLACK)
        blackPaint.color = Color.BLACK
    }

    override fun onDraw(canvas: Canvas) {
        val time = System.currentTimeMillis()
        val deltaTime = (time - lastTime).toInt()
        lastTime = time

        for (circle in listOfDeadCircles.reversed())
            if (circle.coreRadius <= 0) listOfDeadCircles.remove(circle)

        val circles = mapOfCircles.values.plus(listOfDeadCircles)

        if (winnerChosen && mode == Mode.SINGLE) {
            blackSpeed += deltaTime * 0.02f * sign(blackSpeed)
            blackRadius = max(blackRadius + blackSpeed * deltaTime, 105 * scale)
            blackSpeed += deltaTime * 0.02f * sign(blackSpeed)

            for (cir in circles) {
                if(cir.winnerCircle) {
                    val radius = if(mapOfCircles.isEmpty()) blackRadius else blackRadius * cir.coreRadius / (50f * scale)
                    canvas.drawCircle(cir.x, cir.y, radius, blackPaint)
                }
            }
        }

        for(cir in circles) {
            cir.updateValues(deltaTime)
            if (mode == Mode.GROUP) (cir as GroupCircle).fadeColor(deltaTime)
            canvas.drawOval(cir.center, cir.paint)
            canvas.drawArc(cir.ring, cir.startAngle, cir.sweepAngle, false, cir.strokePaint)
            canvas.drawArc(cir.center, cir.startAngle + 180f, cir.sweepAngle / 2f, false, cir.strokePaintLight)
            canvas.drawArc(cir.ring, cir.startAngle, cir.sweepAngle / 2f, false, cir.strokePaintLight)
        }

        for (number in listOfNumbers.reversed()) {
            number.updateValues(deltaTime)
            canvas.drawText(number.number.toString(), number.x, number.y, number.textPaint)
            if (number.alpha <= 0) listOfNumbers.remove(number)
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
                    hideMenu(true)
                    val pos = PointF(event.getX(pointerIndex), event.getY(pointerIndex))
                    mapOfCircles[pointerId] = when (mode) {
                        Mode.SINGLE -> Circle(pos.x, pos.y, 50f * scale)
                        Mode.GROUP -> GroupCircle(pos.x, pos.y, 50f * scale)
                        Mode.ORDER -> OrderCircle(pos.x, pos.y, 50f * scale)
                    }
                    handler.removeCallbacksAndMessages(null)
                    handler.postDelayed({ selectWinner() }, 3000)
                }
            }

            MotionEvent.ACTION_MOVE -> {
                for (index in 0..<event.pointerCount) {
                    val id = event.getPointerId(index)
                    val circle = mapOfCircles[id]
                    if (circle != null) {
                        circle.x = event.getX(index)
                        circle.y = event.getY(index)
                    }
                }
            }

            MotionEvent.ACTION_UP, MotionEvent.ACTION_POINTER_UP -> {
                removeCircle(pointerId)
                if (mapOfCircles.isEmpty()) {
                    ColorGenerator.newColorPalette(5)
                    if (winnerChosen) {
                        handler.postDelayed({
                            blackSpeed = 1f
                            handler.postDelayed({
                                hideMenu(false)
                                winnerChosen = false
                                setBackgroundColor(Color.BLACK)
                            }, 150)
                        }, 1000)
                    } else {
                        hideMenu(false)
                    }
                }
            }
        }
        return true
    }

    private fun selectWinner() {
        if (mapOfCircles.size > count) {
            when (mode) {
                Mode.SINGLE -> chooseFinger()
                Mode.GROUP -> chooseGroup()
                Mode.ORDER -> chooseOrder()
            }
            vibrate()
            winnerChosen = true
        }
    }

    private fun chooseFinger() {
        val indexList = mapOfCircles.keys.toMutableList()

        for (i in indexList.size - 1 downTo 0) {
            if (i >= count) {
                val randomIndex = Random.nextInt(indexList.size)
                removeCircle(indexList[randomIndex])
                indexList.removeAt(randomIndex)
            } else {
                mapOfCircles[indexList[i]]!!.winnerCircle = true
            }
        }

        var color = mapOfCircles[indexList[0]]!!.color
        for (circle in mapOfCircles.values)
            color = ColorUtils.blendARGB(circle.color, color, 0.5f)

        blackSpeed = -1f
        blackRadius = screenHeight.toFloat()
        setBackgroundColor(color)
    }

    private fun chooseGroup() {
        val indexList = mapOfCircles.keys.toMutableList()
        val teamSize = mapOfCircles.size / count
        var remainder = mapOfCircles.size % count

        ColorGenerator.newColorPalette(count)
        for (i in 0..<count) {
            val size = if (remainder > 0) teamSize + 1 else teamSize
            remainder--

            val color = ColorGenerator.nextColor()
            for (a in 0..<size) {
                val randomIndex = Random.nextInt(indexList.size)
                val circle = mapOfCircles[indexList[randomIndex]]
                circle!!.color = color
                circle.winnerCircle = true
                indexList.removeAt(randomIndex)
            }
        }
    }

    private fun chooseOrder(number: Int = 1) {
        val indexList = mapOfCircles.keys.toList()
        val randomIndex: Int = indexList[Random.nextInt(indexList.size)]
        val circle: Circle = mapOfCircles[randomIndex]!!

        circle.winnerCircle = true
        removeCircle(randomIndex)
        listOfNumbers.add(Number(circle.x, circle.y - 50 * scale, circle.color, number, 50 * scale))

        handler.postDelayed({
            if (mapOfCircles.isEmpty()) {
                winnerChosen = false
            } else {
                chooseOrder(number + 1)
                vibrate()
            }
        }, 1000)
    }

    private fun removeCircle(pointerId: Int) {
        val removedCircle: Circle? = mapOfCircles[pointerId]
        if (removedCircle != null) {
            removedCircle.removeFinger()
            listOfDeadCircles.add(removedCircle)
            mapOfCircles.remove(pointerId)
        }
    }

    fun hideMenu(hide: Boolean) {
        val state = if (hide) R.id.end else if (mode == Mode.ORDER) R.id.hideCounter else R.id.start
        motionLayout?.transitionToState(state)
    }

    fun nextMode() {
        if (mapOfCircles.isEmpty()) mode = when (mode) {
            Mode.SINGLE -> Mode.GROUP
            Mode.GROUP -> Mode.ORDER
            Mode.ORDER -> Mode.SINGLE
        }
    }

    fun nextCount() {
        if (mode != Mode.ORDER) count = count % 5 + 1
        if (mode == Mode.GROUP) count = max(count, 2)
    }

    private fun vibrate() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            val effect = VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE)
            vibratorManager.vibrate(CombinedVibration.createParallel(effect))
        } else {
            @Suppress("DEPRECATION")
            val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE))
        }
    }
}
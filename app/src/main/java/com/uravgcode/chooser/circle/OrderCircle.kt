package com.uravgcode.chooser.circle

class OrderCircle(x: Float, y: Float, radius: Float) : Circle(x, y, radius) {

    override fun setWinner() {
        winnerCircle = true
        coreRadius *= 1.1f
    }

}
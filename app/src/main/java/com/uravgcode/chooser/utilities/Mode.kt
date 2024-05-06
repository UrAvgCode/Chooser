package com.uravgcode.chooser.utilities

import com.uravgcode.chooser.R

enum class Mode {
    SINGLE, GROUP, ORDER;

    fun next(): Mode = when (this) {
        SINGLE -> GROUP
        GROUP -> ORDER
        ORDER -> SINGLE
    }

    fun initialCount(): Int = when (this) {
        SINGLE, ORDER -> 1
        GROUP -> 2
    }

    fun nextCount(count: Int): Int = when (this) {
        SINGLE -> count % 5 + 1
        GROUP -> (count - 1) % 4 + 2
        ORDER -> 1
    }

    fun drawable(): Int = when (this) {
        SINGLE -> R.drawable.single_icon
        GROUP -> R.drawable.group_icon
        ORDER -> R.drawable.order_icon
    }

    fun state(): Int = when (this) {
        SINGLE, GROUP -> R.id.start
        ORDER -> R.id.hideCounter
    }
}
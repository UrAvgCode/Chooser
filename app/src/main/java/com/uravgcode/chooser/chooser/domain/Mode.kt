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
 * @description Mode represents the different modes of the application.
 */

package com.uravgcode.chooser.chooser.domain

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
}

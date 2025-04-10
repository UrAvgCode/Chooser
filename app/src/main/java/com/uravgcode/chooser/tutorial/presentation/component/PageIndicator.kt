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
 * @description PageIndicator shows the current page position with animated indicators.
 */

package com.uravgcode.chooser.tutorial.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlin.math.absoluteValue

@Composable
fun PageIndicator(
    modifier: Modifier = Modifier,
    pagerState: PagerState
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        val page = pagerState.currentPage
        val offset = pagerState.currentPageOffsetFraction

        repeat(pagerState.pageCount) { index ->
            val distance = (index - (page + offset)).absoluteValue
            val percentage = (1f - distance).coerceAtLeast(0f)

            val width = 8.dp + (10.dp * percentage)
            val alpha = 0.5f + (0.5f * percentage)

            Box(
                modifier = Modifier
                    .width(width)
                    .height(8.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primary.copy(alpha = alpha),
                        shape = CircleShape
                    )
            )
        }
    }
}

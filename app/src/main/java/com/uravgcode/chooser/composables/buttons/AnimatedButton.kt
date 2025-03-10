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
 * @description AnimatedButton is a button that animates in and out.
 */

package com.uravgcode.chooser.composables.buttons

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AnimatedButton(
    visible: Boolean,
    onClick: () -> Unit,
    onLongClick: (() -> Unit)? = null,
    content: @Composable () -> Unit,
    alignment: Alignment,
    additionalTopPadding: Float
) {
    Box(modifier = Modifier.fillMaxSize()) {
        AnimatedVisibility(
            visible = visible,
            enter = slideInVertically(
                initialOffsetY = { fullHeight -> -2 * fullHeight },
                animationSpec = tween(durationMillis = 400)
            ),
            exit = slideOutVertically(
                targetOffsetY = { fullHeight -> -2 * fullHeight - additionalTopPadding.toInt()},
                animationSpec = tween(durationMillis = 400)
            ),
            modifier = Modifier
                .align(alignment)
                .padding(24.dp)
                .padding(top = additionalTopPadding.dp)
        ) {
            BaseButton(
                onClick = onClick,
                onLongClick = onLongClick,
                content = content,
                radius = 56.dp
            )
        }
    }
}

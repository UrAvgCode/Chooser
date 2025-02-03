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
 * @description BaseButton is the base component for buttons in the application.
 */

package com.uravgcode.chooser.composables.buttons

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics


@Composable
@OptIn(ExperimentalFoundationApi::class)
fun BaseButton(
    onClick: () -> Unit,
    onLongClick: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    val haptic = LocalHapticFeedback.current
    Surface(shape = CircleShape,
        color = Color(0xFF2B2B2B),
        contentColor = Color.White,
        modifier = Modifier
            .semantics {
                role = Role.Button
            }
            .combinedClickable(
                onClick = onClick,
                onLongClick = {
                    onLongClick?.let {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        it()
                    }
                }, onLongClickLabel = "Settings"
            )
    ) {
        Box(
            contentAlignment = Alignment.Center,
        ) {
            content()
        }
    }
}

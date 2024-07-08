package com.uravgcode.chooser.composables.buttons

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
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

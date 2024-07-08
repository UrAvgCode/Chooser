package com.uravgcode.chooser.composables.buttons

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
    alignment: Alignment
) {
    Box(modifier = Modifier.fillMaxSize()) {
        AnimatedVisibility(
            visible = visible,
            enter = slideInVertically(
                initialOffsetY = { fullHeight -> -2 * fullHeight },
                animationSpec = tween(durationMillis = 400)
            ),
            exit = slideOutVertically(
                targetOffsetY = { fullHeight -> -2 * fullHeight },
                animationSpec = tween(durationMillis = 400)
            ),
            modifier = Modifier
                .align(alignment)
                .padding(24.dp)
                .size(56.dp)
        ) {
            BaseButton(
                onClick = onClick,
                onLongClick = onLongClick,
                content = content,
            )
        }
    }
}
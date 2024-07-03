package com.uravgcode.chooser.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun AnimatedButton(
    visible: Boolean,
    onClick: () -> Unit,
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
            FloatingActionButton(
                onClick = onClick,
                shape = CircleShape,
                containerColor = Color(0xFF2B2B2B),
                contentColor = Color.White,
                content = content
            )
        }
    }
}

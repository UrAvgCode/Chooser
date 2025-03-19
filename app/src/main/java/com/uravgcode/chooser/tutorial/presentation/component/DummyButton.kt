package com.uravgcode.chooser.tutorial.presentation.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp

@Composable
fun DummyButton(
    content: @Composable () -> Unit,
    radius: Dp
) {
    Surface(
        shape = CircleShape,
        color = MaterialTheme.colorScheme.secondaryContainer,
        contentColor = Color.White,
        modifier = Modifier.size(radius)
    ) {
        Box(
            contentAlignment = Alignment.Center,
        ) {
            content()
        }
    }
}

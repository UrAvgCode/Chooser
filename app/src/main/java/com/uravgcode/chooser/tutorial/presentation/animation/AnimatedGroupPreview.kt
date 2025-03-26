package com.uravgcode.chooser.tutorial.presentation.animation

import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.animation.graphics.res.rememberAnimatedVectorPainter
import androidx.compose.animation.graphics.vector.AnimatedImageVector
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import com.uravgcode.chooser.R
import kotlinx.coroutines.delay

@Composable
@OptIn(ExperimentalAnimationGraphicsApi::class)
fun AnimatedGroupPreview() {
    val image = AnimatedImageVector.animatedVectorResource(R.drawable.group_preview_animated)
    var atEnd by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(1000)
        atEnd = !atEnd
    }

    Image(
        painter = rememberAnimatedVectorPainter(image, atEnd),
        contentDescription = "Group Preview",
        modifier = Modifier
            .fillMaxWidth(0.8f)
            .aspectRatio(1f),
        contentScale = ContentScale.Crop
    )
}

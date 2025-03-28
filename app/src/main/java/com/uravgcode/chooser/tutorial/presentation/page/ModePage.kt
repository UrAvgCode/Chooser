package com.uravgcode.chooser.tutorial.presentation.page

import androidx.annotation.DrawableRes
import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.animation.graphics.res.rememberAnimatedVectorPainter
import androidx.compose.animation.graphics.vector.AnimatedImageVector
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
@OptIn(ExperimentalAnimationGraphicsApi::class)
fun ModePage(
    @DrawableRes iconId: Int,
    @DrawableRes previewId: Int,
    title: String,
    description: String,
    previewDescription: String,
    isVisible: Boolean = true,
) {
    val image = AnimatedImageVector.animatedVectorResource(previewId)
    var atEnd by remember { mutableStateOf(false) }

    LaunchedEffect(isVisible) {
        if (isVisible) {
            delay(600)
            atEnd = true
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(iconId),
                contentDescription = null,
                modifier = Modifier
                    .size(38.dp)
                    .padding(end = 8.dp),
            )
            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
        }
        Text(
            text = description,
            textAlign = TextAlign.Center
        )

        Card(
            modifier = Modifier.padding(8.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardColors(
                containerColor = Color.Black,
                contentColor = Color.White,
                disabledContainerColor = Color.Black,
                disabledContentColor = Color.White
            ),
            border = BorderStroke(
                width = 2.dp,
                color = MaterialTheme.colorScheme.outlineVariant
            )
        ) {
            Image(
                painter = rememberAnimatedVectorPainter(image, atEnd),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f),
                contentScale = ContentScale.Crop
            )
        }

        Text(
            text = previewDescription,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )
    }
}

package com.uravgcode.chooser

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.uravgcode.chooser.utilities.Mode
import com.uravgcode.chooser.views.Chooser

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val preferences = getSharedPreferences("Settings", Context.MODE_PRIVATE)

        setContent {
            val chooserMode =
                remember { mutableStateOf(Mode.valueOf(preferences.getString("mode", "SINGLE")!!)) }
            val chooserCount = remember { mutableIntStateOf(preferences.getInt("count", 1)) }
            val isVisible = remember { mutableStateOf(true) }

            AndroidView(
                factory = { context ->
                    Chooser(context, setButtonVisibility = { newVisible ->
                        isVisible.value = newVisible
                    })
                },
                update = { view ->
                    view.mode = chooserMode.value
                    view.count = chooserCount.intValue
                },
                modifier = Modifier
            )

            AnimatedButton(
                visible = isVisible.value,
                onClick = {
                    if (isVisible.value) {
                        chooserMode.value = chooserMode.value.next()
                        chooserCount.intValue = chooserMode.value.initialCount()
                        savePreference(preferences, "mode", chooserMode.value.toString())
                    }
                },
                content = {
                    Icon(
                        painter = painterResource(id = chooserMode.value.drawable()),
                        contentDescription = "Mode"
                    )
                },
                alignment = Alignment.TopStart
            )

            AnimatedButton(
                visible = chooserMode.value != Mode.ORDER && isVisible.value,
                onClick = {
                    if (isVisible.value) {
                        chooserCount.intValue = chooserMode.value.nextCount(chooserCount.intValue)
                        savePreference(preferences, "count", chooserCount.intValue)
                    }
                },
                content = {
                    Text(
                        text = chooserCount.intValue.toString(),
                        fontSize = 36.sp
                    )
                },
                alignment = Alignment.TopEnd
            )
        }
    }

    private fun savePreference(preferences: SharedPreferences, key: String, value: Any) {
        with(preferences.edit()) {
            when (value) {
                is Int -> putInt(key, value)
                is String -> putString(key, value)
                else -> throw IllegalArgumentException("Invalid type for SharedPreferences")
            }
            apply()
        }
    }
}

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

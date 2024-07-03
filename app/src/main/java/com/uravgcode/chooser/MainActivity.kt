package com.uravgcode.chooser

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.uravgcode.chooser.composables.AnimatedButton
import com.uravgcode.chooser.utilities.Mode
import com.uravgcode.chooser.views.Chooser

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val preferences = getSharedPreferences("Settings", Context.MODE_PRIVATE)
        val initialMode = Mode.valueOf(preferences.getString("mode", "SINGLE")!!)
        val initialCount = preferences.getInt("count", 1)

        setContent {
            val chooserMode = remember { mutableStateOf(initialMode) }
            val chooserCount = remember { mutableIntStateOf(initialCount) }
            val isVisible = remember { mutableStateOf(true) }

            AndroidView(
                factory = { context ->
                    Chooser(context, setButtonVisibility = {
                        isVisible.value = it
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

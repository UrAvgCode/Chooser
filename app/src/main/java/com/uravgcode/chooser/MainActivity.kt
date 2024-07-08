package com.uravgcode.chooser

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.uravgcode.chooser.composables.screens.MainScreen
import com.uravgcode.chooser.utilities.SettingsManager

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val preferences = getSharedPreferences("settings", Context.MODE_PRIVATE)
        val settings = SettingsManager(preferences)

        setContent {
            MainScreen(settings)
        }
    }
}

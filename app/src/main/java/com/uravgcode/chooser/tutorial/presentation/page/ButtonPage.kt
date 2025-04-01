package com.uravgcode.chooser.tutorial.presentation.page

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uravgcode.chooser.R
import com.uravgcode.chooser.tutorial.presentation.component.ButtonExplanation

@Composable
@OptIn(ExperimentalLayoutApi::class)
fun ButtonPage() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly,
    ) {
        Text(
            text = "How to Use",
            modifier = Modifier.padding(bottom = 8.dp),
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        ButtonExplanation(
            title = "Mode Button",
            description = "Tap to switch between Single, Group, and Order modes",
            buttonContent = {
                Icon(
                    painter = painterResource(id = R.drawable.single_icon),
                    contentDescription = null,
                    modifier = Modifier.size(38.dp),
                )
            }
        )

        ButtonExplanation(
            title = "Number Button",
            description = "Tap to change the number of winners or groups",
            buttonContent = {
                Text(
                    text = "1",
                    fontSize = 38.sp,
                    textAlign = TextAlign.Center,
                )
            }
        )

        ButtonExplanation(
            title = "Settings",
            description = "Long press on the Mode button to access settings",
            buttonContent = {
                Icon(
                    imageVector = Icons.Filled.Settings,
                    contentDescription = null,
                    modifier = Modifier.size(38.dp),
                )
            }
        )
    }
}

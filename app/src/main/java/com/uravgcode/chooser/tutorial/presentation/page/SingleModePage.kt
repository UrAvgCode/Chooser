package com.uravgcode.chooser.tutorial.presentation.page

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import com.uravgcode.chooser.R

@Composable
fun SingleModePage() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(id = R.drawable.single_icon),
                contentDescription = null,
                modifier = Modifier
                    .size(38.dp)
                    .padding(end = 8.dp),
            )
            Text(
                text = "Single Mode",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
        }
        Text(
            text = "Chooses a random finger from all placed on screen",
            textAlign = TextAlign.Center
        )

        Icon(
            painter = painterResource(id = R.drawable.single_icon),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(0.5f),
        )

        Text(
            text = "Perfect for making quick decisions or selecting a winner",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )
    }
}

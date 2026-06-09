package com.uravgcode.chooser.settings.presentation.button

import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.net.toUri
import com.uravgcode.chooser.R

@Composable
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
fun LanguageButton() {
    val context = LocalContext.current

    SettingsButton(
        text = stringResource(R.string.language),
        onClick = {
            try {
                Intent(Settings.ACTION_APP_LOCALE_SETTINGS, "package:${context.packageName}".toUri()).apply {
                    context.startActivity(this)
                }
            } catch (exception: Exception) {
                Toast.makeText(context, exception.message, Toast.LENGTH_SHORT).show()
            }
        }
    )
}

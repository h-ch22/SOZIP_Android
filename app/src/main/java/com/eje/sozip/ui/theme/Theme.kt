package com.eje.sozip.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val DarkColorScheme = darkColorScheme(
    primary = accent,
    secondary = accent,
    tertiary = accent
)

private val LightColorScheme = lightColorScheme(
    primary = accent,
    secondary = accent,
    tertiary = accent
)

private val onLightColorPalette = ColorPalette(
    background = background,
    txtColor = txtColor,
    btnColor = btnColor,
    txtFieldColor = txtField
)

private val onDarkColorPalette = ColorPalette(
    background = backgroundAsDark,
    txtColor = txtColorAsDark,
    btnColor = btnColorAsDark,
    txtFieldColor = txtFieldAsDark
)

@Composable
fun SOZIPTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val colorPalette =
        if (darkTheme) onDarkColorPalette
        else onLightColorPalette

    CompositionLocalProvider (
        SOZIPColorPalette provides colorPalette
    ){
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }

    val systemUiController = rememberSystemUiController()

    if(darkTheme){
        systemUiController.setSystemBarsColor(
            color = backgroundAsDark
        )
    } else{
        systemUiController.setSystemBarsColor(
            color = background
        )
    }
}
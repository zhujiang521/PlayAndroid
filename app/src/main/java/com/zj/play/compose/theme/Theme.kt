package com.zj.play.compose.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color


@Composable
fun PlayTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        PlayThemeDark
    } else {
        PlayThemeLight
    }
    MaterialTheme(
        colors = colors,
        typography = typography,
        content = content
    )
}

private val PlayThemeLight = lightColors(
    primary = blue,
    onPrimary = Color.White,
    primaryVariant = blue,
    secondary = blue
)

private val PlayThemeDark = darkColors(
    primary = blueDark,
    onPrimary = Color.White,
    secondary = blueDark,
    surface = blueDark
)
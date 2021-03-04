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
    primary = yellow,
    onPrimary = Color.White,
    primaryVariant = yellow,
    secondary = yellow500
)

private val PlayThemeDark = darkColors(
    primary = yellow200,
    onPrimary = Color.White,
    secondary = yellow500,
    surface = blueDarkPrimary
)
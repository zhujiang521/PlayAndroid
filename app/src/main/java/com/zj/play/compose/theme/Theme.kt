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

@Composable
fun Play2Theme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        Play2ThemeDark
    } else {
        Play2ThemeLight
    }
    MaterialTheme(
        colors = colors,
        typography = typography,
        content = content
    )
}

private val Play2ThemeLight = lightColors(
    primary = Purple300,
    onPrimary = Color.White,
    primaryVariant = Purple300,
    secondary = Purple300,
    secondaryVariant = Color(0xFF018786),
    background = Purple300,
    surface = Purple300,
    error = Color(0xFFB00020),
    onSecondary = Purple300,
    onBackground = Purple300,
    onSurface = Purple300,
    onError = Color.White
)

private val Play2ThemeDark = lightColors(
    primary = Purple700,
    onPrimary = Color.White,
    primaryVariant = Purple700,
    secondary = Purple700,
    secondaryVariant = Purple700,
    background = Purple700,
    surface = Purple700,
    error = Color(0xFFB00020),
    onSecondary = Purple700,
    onBackground = Purple700,
    onSurface = Purple700,
    onError = Color.White
)

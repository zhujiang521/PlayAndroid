package com.zj.play.ui.theme

import androidx.compose.material.Colors
import androidx.compose.material.darkColors
import androidx.compose.ui.graphics.Color

val white = Color.White

// 天蓝色
val sky_blue_theme = Color(0xFF85B4FC)

// 灰色
val gray_theme = Color(0xFF888888)

// 深蓝色
val deep_blue_theme = Color(0xFF0000FF)

// 绿色
val green_theme = Color(0xFF00FF00)

// 紫色
val purple_theme = Color(0xFF9932CD)

// 橘黄色
val orange_theme = Color(0xFFFFA500)

// 棕色
val brown_theme = Color(0xFF804000)

// 红色
val red_theme = Color(0xFFFF0000)

// 青色
val cyan_theme = Color(0xFF00FFFF)

// 品红色
val magenta_theme = Color(0xFFFF00FF)

// Light 主题颜色
val pageLight = Color(0xFFf6f6f6)

val primaryLight = Color(0xFF85B4FC)
val primaryVariantLight = Color(0xFF3700B3)
val secondaryLight = Color(0xFF3f2c2c)
val backgroundLight = pageLight
val surfaceLight = pageLight
val onPrimaryLight = Color(0xFF232323)
val onSecondaryLight = Color(0xFFD8D7D7)
val onBackgroundLight = Color(0xFF232325)
val onSurfaceLight = Color(0xFF232323)


// Dark 主题颜色
val pageDark = Color(0xFF000000)

val primaryDark = pageDark
val primaryVariantDark = Color(0xFF3700B3)
val secondaryDark = Color(0xFFE0E0F0)
val backgroundDark = Color(0xFF1B1B1B)
val surfaceDark = Color(0xFF232323)
val onPrimaryDark = white
val onSecondaryDark = Color(0xFF3A3A3A)
val onBackgroundDark = white
val onSurfaceDark = white


/**
 * 玩安卓浅色主题方法
 */
fun playLightColors(
    primary: Color = primaryLight,
    primaryVariant: Color = primaryVariantLight,
    secondary: Color = secondaryLight,
    secondaryVariant: Color = Color(0xFF018786),
    background: Color = backgroundLight,
    surface: Color = surfaceLight,
    error: Color = Color(0xFFB00020),
    onPrimary: Color = onPrimaryLight,
    onSecondary: Color = onSecondaryLight,
    onBackground: Color = onBackgroundLight,
    onSurface: Color = onSurfaceLight,
    onError: Color = Color.White
): Colors = Colors(
    primary,
    primaryVariant,
    secondary,
    secondaryVariant,
    background,
    surface,
    error,
    onPrimary,
    onSecondary,
    onBackground,
    onSurface,
    onError,
    true
)


/**
 * 深色主题颜色
 */
val DarkColorPalette = darkColors(
    primary = primaryDark,
    primaryVariant = primaryVariantDark,
    secondary = secondaryDark,
    background = backgroundDark,
    surface = surfaceDark,
    onPrimary = onPrimaryDark,
    onSecondary = onSecondaryDark,
    onBackground = onBackgroundDark,
    onSurface = onSurfaceDark,
)
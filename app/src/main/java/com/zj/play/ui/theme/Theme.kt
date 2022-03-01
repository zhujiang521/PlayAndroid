package com.zj.play.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// 天蓝色
const val SKY_BLUE_THEME = 0

// 灰色
const val GRAY_THEME = 1

// 深蓝色
const val DEEP_BLUE_THEME = 2

// 绿色
const val GREEN_THEME = 3

// 紫色
const val PURPLE_THEME = 4

// 橘黄色
const val ORANGE_THEME = 5

// 棕色
const val BROWN_THEME = 6

// 红色
const val RED_THEME = 7

// 青色
const val CYAN_THEME = 8

// 品红色
const val MAGENTA_THEME = 9

/**
 * @param theme 主题，这里需要注意，只有在浅色模式下可以进行更换主题，
 * 在深色模式下不支持更换主题
 */
@Composable
fun PlayAndroidTheme(
    theme: Int = 0,
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    Color.Gray
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        playLightColors(
            primary = getLightPrimaryColor(theme)
        )
    }

    MaterialTheme(
        colors = colors,
        typography = typography,
        shapes = Shapes,
        content = content
    )
}

private fun getLightPrimaryColor(theme: Int) = when (theme) {
    SKY_BLUE_THEME -> {
        primaryLight
    }
    GRAY_THEME -> {
        gray_theme
    }
    DEEP_BLUE_THEME -> {
        deep_blue_theme
    }
    GREEN_THEME -> {
        green_theme
    }
    PURPLE_THEME -> {
        purple_theme
    }
    ORANGE_THEME -> {
        orange_theme
    }
    BROWN_THEME -> {
        brown_theme
    }
    RED_THEME -> {
        red_theme
    }
    CYAN_THEME -> {
        cyan_theme
    }
    MAGENTA_THEME -> {
        magenta_theme
    }
    else -> {
        primaryLight
    }
}
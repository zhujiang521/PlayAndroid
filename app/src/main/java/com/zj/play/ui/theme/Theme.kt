package com.zj.play.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.zj.play.CHANGED_THEME
import com.zj.utils.DataStoreUtils

const val DYNAMIC_COLOR_SCHEME = "dynamicLightColorScheme"

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
 * 主题状态
 */
val themeTypeState: MutableState<Int> by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
    mutableStateOf(getDefaultThemeId())
}

/**
 * 获取当前默认主题
 */
fun getDefaultThemeId(): Int = DataStoreUtils.getSyncData(CHANGED_THEME, SKY_BLUE_THEME)

/**
 * @param themeId 主题，这里需要注意，只有在浅色模式下可以进行更换主题，
 * 在深色模式下不支持更换主题
 */
@Composable
fun PlayAndroidTheme(
    themeId: Int = 0,
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
//    val dynamic = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
//    val isDynamicColorScheme = DataStoreUtils.getSyncData(DYNAMIC_COLOR_SCHEME, true)
//    if (dynamic && isDynamicColorScheme) {
//        val context = LocalContext.current
//        val colorScheme =
//            if (darkTheme) dynamicLightColorScheme(context) else dynamicDarkColorScheme(context)
//        MaterialTheme(
//            colors = getSchemeColor(colorScheme, darkTheme),
//            typography = typography,
//            shapes = Shapes,
//            content = content
//        )
//    } else {
//        val colors = if (darkTheme) {
//            playDarkColors()
//        } else {
//            getThemeForThemeId(themeId)
//        }
//        MaterialTheme(
//            colors = colors,
//            typography = typography,
//            shapes = Shapes,
//            content = content
//        )
//    }

    val colors = if (darkTheme) {
        playDarkColors()
    } else {
        getThemeForThemeId(themeId)
    }
    MaterialTheme(
        colors = colors,
        typography = typography,
        shapes = Shapes,
        content = content
    )
}

/**
 * 获取根据壁纸获取的颜色
 *
 * @param colorScheme 主题
 * @param darkTheme 是否为深色主题
 */
//fun getSchemeColor(colorScheme: ColorScheme, darkTheme: Boolean): Colors {
//    return Colors(
//        primary = colorScheme.primary,
//        primaryVariant = colorScheme.primaryContainer,
//        secondary = colorScheme.secondary,
//        secondaryVariant = colorScheme.secondaryContainer,
//        background = colorScheme.background,
//        surface = colorScheme.surface,
//        error = colorScheme.error,
//        onPrimary = colorScheme.onPrimary,
//        onSecondary = colorScheme.onSecondary,
//        onBackground = colorScheme.onBackground,
//        onSurface = colorScheme.onSurface,
//        onError = colorScheme.onError,
//        isLight = !darkTheme
//    )
//}

/**
 * 通过主题 ID 来获取需要的主题
 */
private fun getThemeForThemeId(themeId: Int) = when (themeId) {
    SKY_BLUE_THEME -> {
        playLightColors(
            primary = primaryLight
        )
    }
    GRAY_THEME -> {
        playLightColors(
            primary = gray_theme
        )
    }
    DEEP_BLUE_THEME -> {
        playDarkColors(
            primary = deep_blue_theme
        )
    }
    GREEN_THEME -> {
        playLightColors(
            primary = green_theme
        )
    }
    PURPLE_THEME -> {
        playLightColors(
            primary = purple_theme
        )
    }
    ORANGE_THEME -> {
        playLightColors(
            primary = orange_theme
        )
    }
    BROWN_THEME -> {
        playDarkColors(
            primary = brown_theme
        )
    }
    RED_THEME -> {
        playDarkColors(
            primary = red_theme
        )

    }
    CYAN_THEME -> {
        playLightColors(
            primary = cyan_theme
        )
    }
    MAGENTA_THEME -> {
        playLightColors(
            primary = magenta_theme
        )
    }
    else -> {
        playLightColors(
            primary = primaryLight
        )
    }
}
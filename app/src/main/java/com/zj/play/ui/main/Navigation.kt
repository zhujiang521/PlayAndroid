package com.zj.play.ui.main

import androidx.compose.foundation.background
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.zj.play.ui.main.nav.CourseTabs
import com.zj.play.ui.theme.getCurrentColors
import com.zj.play.ui.view.LandNavigation
import com.zj.play.ui.view.LandNavigationItem
import java.util.Locale


@Composable
fun PlayLandNavigation(
    tabs: Array<CourseTabs>,
    position: CourseTabs?,
    onPositionChanged: (CourseTabs) -> Unit
) {
    LandNavigation {
        val reversalColor = reversalColor()
        tabs.forEach { tab ->
            val color =
                if (tab == position) reversalColor else MaterialTheme.colors.secondary
            LandNavigationItem(
                modifier = Modifier.background(MaterialTheme.colors.primary),
                icon = {
                    val painter: Painter = if (tab == position) {
                        painterResource(tab.selectIcon)
                    } else {
                        painterResource(tab.icon)
                    }
                    Icon(painter, contentDescription = null, tint = color)
                },
                label = {
                    Text(
                        stringResource(tab.title).uppercase(Locale.ROOT),
                        color = color
                    )
                },
                selected = tab == position,
                onClick = {
                    onPositionChanged(tab)
                },
                alwaysShowLabel = true,
            )
        }
    }
}

@Composable
fun PlayBottomNavigation(
    tabs: Array<CourseTabs>,
    position: CourseTabs?,
    onPositionChanged: (CourseTabs) -> Unit
) {
    BottomNavigation {
        val reversalColor = reversalColor()
        tabs.forEach { tab ->
            val color =
                if (tab == position) reversalColor else MaterialTheme.colors.secondary
            BottomNavigationItem(
                modifier = Modifier.background(MaterialTheme.colors.primary),
                icon = {
                    val painter: Painter = if (tab == position) {
                        painterResource(tab.selectIcon)
                    } else {
                        painterResource(tab.icon)
                    }
                    Icon(painter, contentDescription = null, tint = color)
                },
                label = {
                    Text(
                        stringResource(tab.title).uppercase(Locale.ROOT),
                        color = color
                    )
                },
                selected = tab == position,
                onClick = {
                    onPositionChanged(tab)
                },
                alwaysShowLabel = true,
            )
        }
    }
}


@Composable
private fun reversalColor(): Color {
    val primaryColor = getCurrentColors().primary
    return Color(
        red = (255f - (primaryColor.red * 255f)) / 255f,
        green = (255f - (primaryColor.green * 255f)) / 255f,
        blue = (255f - (primaryColor.blue * 255f)) / 255f,
    )
}
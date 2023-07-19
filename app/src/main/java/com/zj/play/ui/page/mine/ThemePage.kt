package com.zj.play.ui.page.mine

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Checkbox
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.zj.play.CHANGED_THEME
import com.zj.play.R
import com.zj.play.ui.main.nav.PlayActions
import com.zj.play.ui.theme.BROWN_THEME
import com.zj.play.ui.theme.CYAN_THEME
import com.zj.play.ui.theme.DEEP_BLUE_THEME
import com.zj.play.ui.theme.DYNAMIC_COLOR_SCHEME
import com.zj.play.ui.theme.GRAY_THEME
import com.zj.play.ui.theme.GREEN_THEME
import com.zj.play.ui.theme.MAGENTA_THEME
import com.zj.play.ui.theme.ORANGE_THEME
import com.zj.play.ui.theme.PURPLE_THEME
import com.zj.play.ui.theme.RED_THEME
import com.zj.play.ui.theme.SKY_BLUE_THEME
import com.zj.play.ui.theme.brown_theme
import com.zj.play.ui.theme.cyan_theme
import com.zj.play.ui.theme.deep_blue_theme
import com.zj.play.ui.theme.gray_theme
import com.zj.play.ui.theme.green_theme
import com.zj.play.ui.theme.magenta_theme
import com.zj.play.ui.theme.orange_theme
import com.zj.play.ui.theme.primaryLight
import com.zj.play.ui.theme.purple_theme
import com.zj.play.ui.theme.red_theme
import com.zj.play.ui.theme.select_theme
import com.zj.play.ui.theme.themeTypeState
import com.zj.play.ui.view.bar.PlayAppBar
import com.zj.utils.DataStoreUtils

data class ThemeModel(val color: Color, val colorId: Int, val colorName: String)

// 主题model列表
private val themeList = arrayListOf(
    ThemeModel(primaryLight, SKY_BLUE_THEME, "天蓝色"),
    ThemeModel(gray_theme, GRAY_THEME, "灰色"),
    ThemeModel(deep_blue_theme, DEEP_BLUE_THEME, "深蓝色"),
    ThemeModel(green_theme, GREEN_THEME, "绿色"),
    ThemeModel(purple_theme, PURPLE_THEME, "紫色"),
    ThemeModel(orange_theme, ORANGE_THEME, "橘黄色"),
    ThemeModel(brown_theme, BROWN_THEME, "棕色"),
    ThemeModel(red_theme, RED_THEME, "红色"),
    ThemeModel(cyan_theme, CYAN_THEME, "青色"),
    ThemeModel(magenta_theme, MAGENTA_THEME, "品红色"),
)

@Composable
fun ThemePage(actions: PlayActions) {
    var playTheme by remember { mutableIntStateOf(ORANGE_THEME) }
    LaunchedEffect(Unit) {
        playTheme = DataStoreUtils.getSyncData(CHANGED_THEME, ORANGE_THEME)
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colors.primary)
    ) {
        PlayAppBar(stringResource(R.string.theme_change), click = {
            actions.upPress()
        })

        LazyVerticalGrid(
            columns = GridCells.Fixed(5),
            modifier = Modifier.padding(horizontal = 10.dp)
        ) {
            items(themeList) { item: ThemeModel ->
                ThemeItem(playTheme, item) {
                    playTheme = item.colorId
                    themeTypeState.value = playTheme
                    DataStoreUtils.putSyncData(CHANGED_THEME, playTheme)
                }
            }
        }

//        ThemeCheckbox()

        Text(
            text = stringResource(R.string.theme_warn),
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp),
            color = MaterialTheme.colors.secondary,
        )

    }
}

@Composable
fun ThemeCheckbox() {
    var isDynamicColorScheme by remember {
        mutableStateOf(
            DataStoreUtils.getSyncData(
                DYNAMIC_COLOR_SCHEME,
                true
            )
        )
    }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(horizontal = 20.dp)
    ) {
        Text(
            text = stringResource(R.string.dynamic_color),
            modifier = Modifier.weight(1f),
            color = MaterialTheme.colors.secondary,
        )
        Checkbox(checked = isDynamicColorScheme, onCheckedChange = {
            isDynamicColorScheme = it
            DataStoreUtils.putSyncData(DYNAMIC_COLOR_SCHEME, it)
            themeTypeState.value = SKY_BLUE_THEME
        })
    }

}

@Composable
private fun ThemeItem(playTheme: Int, item: ThemeModel, onClick: () -> Unit) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable {
                onClick()
            }
            .padding(10.dp)
    ) {
        val isCurrentTheme = item.colorId == playTheme
        Box(
            modifier = Modifier
                .size(50.dp)
                .shadow(2.dp, shape = MaterialTheme.shapes.medium)
                .background(color = item.color),
            contentAlignment = Alignment.Center,

            ) {
            if (isCurrentTheme) {
                Icon(
                    imageVector = Icons.Rounded.Check,
                    contentDescription = "back",
                    tint = select_theme
                )
            }
        }
        Text(
            text = item.colorName,
            color = MaterialTheme.colors.secondary,
            modifier = Modifier.padding(top = 10.dp)
        )
    }
}

package com.zj.play.ui.page.system

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.zj.play.logic.model.AndroidSystemModel
import com.zj.play.logic.model.PlayState
import com.zj.play.ui.page.search.Chip
import com.zj.play.ui.view.PlayAppBar
import com.zj.play.ui.view.lce.LcePage

private const val TAG = "SystemPage"

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SystemPage(
    modifier: Modifier,
    androidSystemState: PlayState<List<AndroidSystemModel>>,
    loadData: () -> Unit,
    loadArticle: (Int, String) -> Unit
) {
    var loadArticleState by rememberSaveable { mutableStateOf(false) }
    if (!loadArticleState) {
        loadArticleState = true
        loadData()
    }
    val listState = rememberLazyListState()
    val childrenState = rememberLazyListState()
    var systemState by remember { mutableStateOf(AndroidSystemModel(arrayListOf())) }
    Log.e(TAG, "SystemPage: $androidSystemState")
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PlayAppBar(title = "体系", showBack = false)
        LcePage(playState = androidSystemState, onErrorClick = {}) {
            loadArticleState = true
            Row(
                Modifier
                    .fillMaxSize()
                    .padding(horizontal = 10.dp)
            ) {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.weight(1f)
                ) {
                    items(it) { systemModel ->
                        Chip(text = systemModel.name, modifier = Modifier
                            .padding(8.dp)
                            .background(
                                if (systemState.id == systemModel.id) {
                                    Color.Red
                                } else Color.Blue
                            )
                            .clickable {
                                systemState = systemModel
                            })
                        if (systemState.id == 0) {
                            systemState = systemModel
                        }
                    }
                }
                LazyVerticalGrid(
                    cells = GridCells.Fixed(2),
                    state = childrenState,
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1.5f)
                ) {
                    items(systemState.children) { systemModel ->
                        Chip(text = systemModel.name, modifier = Modifier
                            .padding(8.dp)
                            .clickable {
                                loadArticle(systemState.id, systemState.name)
                            })
                    }
                }
            }
        }
    }

//    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.weather_rain))
//
//    val composition1 by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.weather_rain))
//    val composition2 by rememberLottieComposition(LottieCompositionSpec.Url("动画文件网址"))
//    val composition3 by rememberLottieComposition(LottieCompositionSpec.File("文件名称"))
//    val composition4 by rememberLottieComposition(LottieCompositionSpec.Asset("文件"))
//    val composition5 by rememberLottieComposition(LottieCompositionSpec.JsonString("动画json文件字符串"))
//
//    val progress by animateLottieCompositionAsState(
//        composition = composition,
//        speed = 2f,
//        iterations = LottieConstants.IterateForever
//    )
////    Column(
////        modifier = Modifier.fillMaxSize(),
////        horizontalAlignment = Alignment.CenterHorizontally
////    ) {
////        PlayAppBar(title = "体系", showBack = false)
////        LottieAnimation(composition = composition, progress = progress)
////    }
//
//    val rememberLottieDynamicProperties = rememberLottieDynamicProperties()
//    LottieAnimation(composition = composition, progress = progress)
}
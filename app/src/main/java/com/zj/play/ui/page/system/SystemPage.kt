package com.zj.play.ui.page.system

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zj.banner.utils.ImageLoader
import com.zj.play.R
import com.zj.play.logic.model.AndroidSystemModel
import com.zj.play.logic.model.PlayState
import com.zj.play.logic.model.SystemChildren
import com.zj.play.ui.theme.Shapes
import com.zj.play.ui.view.PlayAppBar
import com.zj.play.ui.view.lce.LcePage
import kotlinx.coroutines.DelicateCoroutinesApi

private const val TAG = "SystemPage"

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SystemPage(
    modifier: Modifier,
    androidSystemState: PlayState<List<AndroidSystemModel>>,
    systemState: AndroidSystemModel,
    saveSystemState: (AndroidSystemModel) -> Unit,
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
//    val coroutineScope = rememberCoroutineScope()
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
                        val modifiers = Modifier
                            .fillMaxWidth()
                            .height(45.dp)
                            .padding(bottom = 5.dp)
                            .shadow(1.dp, shape = Shapes.small)
                            .background(
                                if (systemState.id == systemModel.id) {
                                    Color.Gray
                                } else MaterialTheme.colors.background
                            )
                            .clickable {
                                saveSystemState(systemModel)
                            }

                        Column(
                            verticalArrangement = Arrangement.Center,
                            modifier = modifiers
                        ) {
                            Text(
                                text = systemModel.name,
                                fontSize = 15.sp,
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center
                            )
                        }

                        if (systemState.id == 0) {
                            saveSystemState(systemModel)
                        }
                    }
//                    coroutineScope.launch {
//                        listState.animateScrollToItem(it.indexOf(systemState))
//                    }
                }
                LazyVerticalGrid(
                    cells = GridCells.Fixed(2),
                    state = childrenState,
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(2f)
                ) {
                    items(systemState.children) { systemModel ->
                        SystemCard(loadArticle, systemModel)
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

@Composable
private fun SystemCard(
    loadArticle: (Int, String) -> Unit,
    systemModel: SystemChildren
) {
    Column(
        modifier = Modifier
            .padding(4.dp)
            .clickable {
                loadArticle(systemModel.id, systemModel.name)
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Card(
            shape = Shapes.small,
        ) {
            ImageLoader(
                R.drawable.img_default,
                Modifier
                    .shadow(1.dp, shape = Shapes.small)
                    .size(70.dp)
            )
        }


        Text(text = systemModel.name, fontSize = 14.sp)
    }
}
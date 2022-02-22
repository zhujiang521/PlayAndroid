package com.zj.play.ui.page.system

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.annotation.ExperimentalCoilApi
import com.zj.banner.utils.ImageLoader
import com.zj.play.R
import com.zj.play.logic.model.AndroidSystemModel
import com.zj.play.logic.model.PlayState
import com.zj.play.logic.model.SystemChildren
import com.zj.play.logic.utils.XLog
import com.zj.play.ui.theme.Shapes
import com.zj.play.ui.view.PlayAppBar
import com.zj.play.ui.view.lce.LcePage

@OptIn(ExperimentalFoundationApi::class, ExperimentalCoilApi::class)
@Composable
fun SystemPage(
    modifier: Modifier,
    androidSystemState: PlayState<List<AndroidSystemModel>>,
    systemState: AndroidSystemModel,
    saveSystemState: (AndroidSystemModel) -> Unit,
    loadArticle: (Int, String) -> Unit
) {
    val listState = rememberLazyListState()
    val childrenState = rememberLazyGridState()
    XLog.e("SystemPage 测试 two")
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PlayAppBar(title = "体系", showBack = false)
        LcePage(playState = androidSystemState, onErrorClick = {}) {
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
}

@ExperimentalCoilApi
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
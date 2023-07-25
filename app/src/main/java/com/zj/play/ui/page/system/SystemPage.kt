package com.zj.play.ui.page.system

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zj.banner.utils.ImageLoader
import com.zj.model.AndroidSystemModel
import com.zj.model.PlayLoading
import com.zj.model.PlayState
import com.zj.model.SystemChildren
import com.zj.play.R
import com.zj.play.ui.main.nav.PlayActions
import com.zj.play.ui.theme.Shapes
import com.zj.play.ui.view.bar.PlayAppBar
import com.zj.play.ui.view.lce.LcePage

@Composable
fun SystemPage(modifier: Modifier, actions: PlayActions, viewModel: SystemViewModel) {
    val androidSystemState by viewModel.androidSystemState.observeAsState(
        PlayLoading
    )
    val systemState by viewModel.systemState.observeAsState(
        AndroidSystemModel(arrayListOf())
    )
    SystemPageContent(modifier, androidSystemState, systemState,
        saveSystemState = {
            viewModel.onSystemModelChanged(it)
        }) { cid, name ->
        actions.toSystemArticleList(cid, name)
    }
}

@Composable
fun SystemPageContent(
    modifier: Modifier,
    androidSystemState: PlayState<List<AndroidSystemModel>>,
    systemState: AndroidSystemModel,
    saveSystemState: (AndroidSystemModel) -> Unit,
    loadArticle: (Int, String) -> Unit
) {
    val listState = rememberLazyListState()
    val childrenState = rememberLazyGridState()
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PlayAppBar(title = stringResource(R.string.system), showBack = false)
        LcePage(playState = androidSystemState, onErrorClick = {}) {
            Row(
                Modifier.fillMaxSize()
            ) {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.weight(1f)
                ) {
                    items(it) { systemModel ->
                        SystemItem(systemState, systemModel, saveSystemState)
                    }
                }
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(130.dp),
                    state = childrenState,
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(3f)
                ) {
                    items(systemState.children) { systemModel ->
                        SystemCard(loadArticle, systemModel)
                    }
                }
            }
        }
    }
}

@Composable
private fun SystemItem(
    systemState: AndroidSystemModel,
    systemModel: AndroidSystemModel,
    saveSystemState: (AndroidSystemModel) -> Unit
) {
    val modifiers = Modifier
        .fillMaxWidth()
        .height(45.dp)
        .padding(bottom = 5.dp)
        .clickable {
            saveSystemState(systemModel)
        }

    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifiers
    ) {
        val color = if (systemState.id == systemModel.id) {
            MaterialTheme.colors.error
        } else Color.Unspecified
        Spacer(
            modifier = Modifier
                .height(30.dp)
                .width(5.dp)
                .background(color)
        )

        Text(
            text = systemModel.name,
            fontSize = 15.sp,
            color = color,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
    }

    if (systemState.id == 0 && systemModel.order == 1) {
        saveSystemState(systemModel)
    }
}

@Composable
private fun SystemCard(
    loadArticle: (Int, String) -> Unit,
    systemModel: SystemChildren
) {
    Column(
        modifier = Modifier
            .padding(top = 8.dp, bottom = 8.dp, end = 8.dp)
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

@Preview(name = "")
@Composable
fun SystemCardPreview() {
    SystemCard(
        loadArticle = { _, _ ->

        }, systemModel = SystemChildren(
            arrayListOf(), 1, 1,
            "进程启动相关", 1, 1,
            true, 1
        )
    )
}
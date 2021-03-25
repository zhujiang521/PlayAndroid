package com.zj.play.compose.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.ExperimentalPagingApi
import com.zj.model.room.entity.BannerBean
import com.zj.play.R
import com.zj.play.compose.MainActions
import com.zj.play.compose.common.PlayAppBar
import com.zj.play.compose.common.article.ArticleListPaging
import com.zj.play.compose.common.article.Banner
import com.zj.play.compose.common.article.ToTopButton
import com.zj.play.compose.common.lce.SetLcePage
import com.zj.play.compose.model.PlayLoading
import com.zj.play.compose.model.PlaySuccess
import com.zj.play.compose.model.Query
import com.zj.play.compose.viewmodel.HomePageViewModel


@ExperimentalPagingApi
@Composable
fun HomePage(
    actions: MainActions,
    modifier: Modifier = Modifier,
    viewModel: HomePageViewModel = viewModel()
) {
    val listState = rememberLazyListState()
    val bannerData by viewModel.bannerState.observeAsState(PlayLoading)
    var loadArticleState by remember { mutableStateOf(false) }
    if (!loadArticleState) {
        loadArticleState = true
        viewModel.getBanner()
        viewModel.searchArticle(Query())
    }

    Column(modifier = Modifier.fillMaxSize()) {
        PlayAppBar(
            stringResource(id = R.string.home_page),
            false,
            showRight = true,
            rightImg = Icons.Rounded.Search,
            rightClick = actions.toSearch
        )
        SetLcePage(
            playState = bannerData,
            onErrorClick = {
                viewModel.getBanner()
            }
        ) {
            val data = bannerData as PlaySuccess<List<BannerBean>>
            Column {
                Banner(data, actions.enterArticle)
                ArticleListPaging(modifier, listState, viewModel, actions.enterArticle)
            }
        }
    }

    if (loadArticleState && bannerData is PlaySuccess<*>) {
        ToTopButton(listState)
    }
}
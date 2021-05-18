package com.zj.play.ui.page.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.ExperimentalPagingApi
import androidx.paging.compose.collectAsLazyPagingItems
import com.zj.banner.BannerPager
import com.zj.banner.ui.indicator.NumberIndicator
import com.zj.play.R
import com.zj.play.logic.model.*
import com.zj.play.ui.main.PlayActions
import com.zj.play.ui.page.article.list.ArticleListPaging
import com.zj.play.ui.view.PlayAppBar
import com.zj.play.ui.view.lce.LcePage

@ExperimentalPagingApi
@Composable
fun HomePage(
    actions: PlayActions,
    modifier: Modifier = Modifier,
    viewModel: HomePageViewModel = viewModel()
) {
    val listState = rememberLazyListState()
    val bannerData by viewModel.bannerState.observeAsState(PlayLoading)
    var loadArticleState by remember { mutableStateOf(false) }
    val lazyPagingItems = viewModel.articleResult.collectAsLazyPagingItems()
    if (!loadArticleState) {
        loadArticleState = true
        viewModel.getData()
    }

    Column(modifier = modifier.fillMaxSize()) {
        PlayAppBar(
            stringResource(id = R.string.home_page),
            false
        )
        LcePage(
            playState = bannerData,
            onErrorClick = {
                viewModel.getData()
            }
        ) {
            val data = bannerData as PlaySuccess<List<BannerBean>>
            BannerPager(items = data.data, indicator = NumberIndicator()) {
                actions.enterArticle(
                    ArticleModel(
                        title = it.title,
                        link = it.url
                    )
                )
            }
            ArticleListPaging(
                Modifier,
                listState,
                lazyPagingItems,
                actions.enterArticle
            )
        }
    }

}
package com.zj.play.ui.page.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.ExperimentalPagingApi
import androidx.paging.compose.LazyPagingItems
import com.google.accompanist.pager.ExperimentalPagerApi
import com.zj.banner.BannerPager
import com.zj.banner.ui.indicator.NumberIndicator
import com.zj.play.R
import com.zj.play.logic.model.*
import com.zj.play.ui.main.PlayActions
import com.zj.play.ui.page.article.list.ArticleListPaging
import com.zj.play.ui.view.PlayAppBar
import com.zj.play.ui.view.lce.LcePage

@ExperimentalPagerApi
@ExperimentalPagingApi
@Composable
fun HomePage(
    modifier: Modifier = Modifier,
    bannerData: PlayState,
    lazyPagingItems: LazyPagingItems<ArticleModel>,
    loadData: () -> Unit,
    toArticleDetails: (ArticleModel) -> Unit
) {
    val listState = rememberLazyListState()
    var loadArticleState by remember { mutableStateOf(false) }
    if (!loadArticleState) {
        loadArticleState = true
        loadData()
    }

    Column(modifier = modifier.fillMaxSize()) {
        PlayAppBar(
            stringResource(id = R.string.home_page),
            false
        )
        LcePage(
            playState = bannerData,
            onErrorClick = {
                loadData()
            }
        ) {
            val data = bannerData as PlaySuccess<List<BannerBean>>
            BannerPager(items = data.data, indicator = NumberIndicator()) {
                toArticleDetails(
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
                toArticleDetails
            )
        }
    }

}
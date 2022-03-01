package com.zj.play.ui.page.home

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.zj.banner.BannerPager
import com.zj.banner.ui.config.BannerConfig
import com.zj.play.R
import com.zj.model.*
import com.zj.play.ui.main.HomeViewModel
import com.zj.play.ui.main.nav.PlayActions
import com.zj.play.ui.page.article.list.ArticleListPaging
import com.zj.play.ui.view.bar.PlayAppBar
import com.zj.play.ui.view.lce.LcePage

@Composable
fun HomePage(
    modifier: Modifier,
    isLand: Boolean,
    homeViewModel: HomeViewModel,
    actions: PlayActions
) {
    val bannerData by homeViewModel.bannerState.observeAsState(PlayLoading)
    val lazyPagingItems = homeViewModel.articleResult.collectAsLazyPagingItems()
    HomePageContent(modifier, isLand, bannerData, lazyPagingItems, {
        initHomeData(bannerData, homeViewModel, lazyPagingItems)
    }, toSearch = {
        actions.toSearch()
    }) {
        actions.enterArticle(it)
    }
}

private fun initHomeData(
    bannerData: PlayState<List<BannerBean>>,
    homeViewModel: HomeViewModel,
    lazyPagingItems: LazyPagingItems<ArticleModel>
) {
    if (bannerData !is PlaySuccess<*>) {
        homeViewModel.getBanner()
    }
    if (lazyPagingItems.itemCount <= 0) {
        homeViewModel.getHomeArticle()
    }
}


@Composable
fun HomePageContent(
    modifier: Modifier = Modifier,
    isLand: Boolean = false,
    bannerData: PlayState<List<BannerBean>>,
    lazyPagingItems: LazyPagingItems<ArticleModel>,
    loadData: () -> Unit,
    toSearch: () -> Unit,
    toArticleDetails: (ArticleModel) -> Unit
) {
    Column(modifier = modifier.fillMaxSize()) {
        PlayAppBar(
            stringResource(id = R.string.home_page),
            showBack = false,
            rightImg = Icons.Rounded.Search,
            showRight = true,
            rightClick = toSearch
        )
        LcePage(
            playState = bannerData,
            onErrorClick = {
                loadData()
            }
        ) { data ->
            if (isLand) {
                Row(modifier = Modifier.fillMaxSize()) {
                    val bannerModifier = Modifier
                        .fillMaxHeight()
                        .weight(1f)
                        .padding(vertical = 15.dp)
                    val articleModifier = Modifier.weight(1.5f)
                    HomeContent(
                        data,
                        bannerModifier,
                        articleModifier,
                        lazyPagingItems,
                        toArticleDetails
                    )
                }
            } else {
                val bannerModifier = Modifier
                    .padding(horizontal = 8.dp)
                HomeContent(
                    data = data,
                    modifier = bannerModifier,
                    lazyPagingItems = lazyPagingItems,
                    toArticleDetails = toArticleDetails
                )
            }
        }
    }
}

@Composable
fun HomeContent(
    data: List<BannerBean>,
    modifier: Modifier = Modifier,
    articleModifier: Modifier = Modifier,
    lazyPagingItems: LazyPagingItems<ArticleModel>,
    toArticleDetails: (ArticleModel) -> Unit
) {
    BannerPager(
        items = data,
        modifier = modifier,
        config = BannerConfig(bannerHeight = 200.dp),
        indicatorGravity = Alignment.BottomEnd
    ) {
        toArticleDetails(
            ArticleModel(
                title = it.title,
                link = it.url
            )
        )
    }
    ArticleListPaging(
        articleModifier,
        lazyPagingItems,
        toArticleDetails
    )
}

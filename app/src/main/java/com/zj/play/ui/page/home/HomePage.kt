@file:OptIn(ExperimentalMaterialApi::class)

package com.zj.play.ui.page.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.zj.banner.BannerPager
import com.zj.banner.utils.ImageLoader
import com.zj.model.*
import com.zj.play.R
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
    HomePageContent(modifier, homeViewModel, isLand, bannerData, lazyPagingItems, {
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
    homeViewModel: HomeViewModel,
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
            val isRefreshing by homeViewModel.isRefreshing.collectAsState()
            val pullRefreshState = rememberPullRefreshState(
                isRefreshing,
                { homeViewModel.refresh() })
            Box(
                Modifier.pullRefresh(pullRefreshState)
            ) {
                if (!isLand) {
                    PortHomeContent(data, toArticleDetails, lazyPagingItems)
                } else {
                    LandHomeContent(data, toArticleDetails, lazyPagingItems)
                }
                PullRefreshIndicator(
                    isRefreshing,
                    pullRefreshState,
                    Modifier.align(Alignment.TopCenter)
                )
            }
        }
    }
}

@Composable
private fun PortHomeContent(
    data: List<BannerBean>,
    toArticleDetails: (ArticleModel) -> Unit,
    lazyPagingItems: LazyPagingItems<ArticleModel>
) {
    Column(modifier = Modifier.fillMaxSize()) {
        BannerPager(
            items = data,
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .wrapContentHeight(),
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
            lazyPagingItems = lazyPagingItems,
            enterArticle = toArticleDetails
        )
    }
}

@Composable
private fun LandHomeContent(
    data: List<BannerBean>,
    toArticleDetails: (ArticleModel) -> Unit,
    lazyPagingItems: LazyPagingItems<ArticleModel>
) {
    Row(modifier = Modifier.fillMaxSize()) {
        //填充数据
        LazyColumn(modifier = Modifier
            .fillMaxHeight()
            .weight(1f)
            .padding(5.dp),
            content = {
                items(data) { item ->
                    Card(
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.padding(PaddingValues(5.dp))
                    ) {
                        ImageLoader(
                            item.data,
                            Modifier
                                .aspectRatio(2f)
                                .clickable {
                                    toArticleDetails(
                                        ArticleModel(
                                            title = item.title,
                                            link = item.url
                                        )
                                    )
                                },
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            })

        ArticleListPaging(
            Modifier
                .weight(1.5f)
                .fillMaxHeight(),
            lazyPagingItems,
            toArticleDetails
        )
    }
}
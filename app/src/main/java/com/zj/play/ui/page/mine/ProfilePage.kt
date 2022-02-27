package com.zj.play.ui.page.mine

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.annotation.ExperimentalCoilApi
import com.zj.banner.utils.ImageLoader
import com.zj.play.Play
import com.zj.play.R
import com.zj.play.logic.model.ArticleModel
import com.zj.play.ui.main.nav.PlayActions
import com.zj.play.ui.page.login.LoginViewModel
import com.zj.play.ui.page.login.LogoutDefault
import com.zj.play.ui.page.login.LogoutFinish
import com.zj.play.ui.page.login.LogoutState
import com.zj.play.ui.view.PlayAppBar
import com.zj.play.ui.view.ShowDialog

@Composable
fun ProfilePage(modifier: Modifier, isLand: Boolean, actions: PlayActions) {
    val viewModel: LoginViewModel = hiltViewModel()
    val logoutState by viewModel.logoutState.observeAsState(LogoutDefault)
    ProfilePageContent(modifier, isLand, actions.toLogin, logoutState, {
        viewModel.logout()
    }) {
        actions.enterArticle(it)
    }
}

@Composable
private fun ProfilePageContent(
    modifier: Modifier,
    isLand: Boolean,
    toLogin: () -> Unit,
    logoutState: LogoutState,
    logout: () -> Unit,
    enterArticle: (ArticleModel) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        PlayAppBar(
            stringResource(id = R.string.mine),
            showBack = false
        )

        if (isLand) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                val headModifier = Modifier.weight(1f)
                HeadItem(headModifier, logoutState, toLogin)
                BlogItem(headModifier, enterArticle, logout)
            }
        } else {
            HeadItem(Modifier, logoutState, toLogin)
            BlogItem(Modifier, enterArticle, logout)
        }
    }
}

@Composable
private fun HeadItem(
    modifier: Modifier = Modifier,
    logoutState: LogoutState,
    toLogin: () -> Unit
) {
    when (logoutState) {
        LogoutDefault -> {
            NameAndPosition(modifier, true, toLogin)
        }
        LogoutFinish -> {
            NameAndPosition(modifier, false, toLogin)
        }
    }
}

@Composable
private fun BlogItem(
    modifier: Modifier = Modifier,
    enterArticle: (ArticleModel) -> Unit,
    logout: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        ProfileProperty(
            imageVector = Icons.Default.Home,
            ArticleModel(
                title = stringResource(R.string.my_book),
                link = "https://zhujiang.blog.csdn.net/article/details/121167705?spm=1001.2014.3001.5502"
            ),
            enterArticle
        )

        ProfileProperty(
            imageVector = Icons.Default.Home,
            ArticleModel(
                title = stringResource(R.string.mine_blog),
                link = "https://zhujiang.blog.csdn.net/"
            ),
            enterArticle
        )

        ProfileProperty(
            imageVector = Icons.Default.AccountBox,
            ArticleModel(
                title = stringResource(R.string.mine_nuggets),
                link = "https://juejin.im/user/5c07e51de51d451de84324d5"
            ),
            enterArticle
        )

        ProfileProperty(
            imageVector = Icons.Default.DateRange,
            ArticleModel(
                title = stringResource(R.string.mine_github),
                link = "https://github.com/zhujiang521"
            ),
            enterArticle
        )

        ProfileProperty(
            imageVector = Icons.Default.Star,
            ArticleModel(
                title = stringResource(R.string.wang_fei),
                link = "https://blog.csdn.net/m0_37667770?type=blog"
            ),
            enterArticle
        )

        ProfileProperty(
            imageVector = Icons.Default.Star,
            ArticleModel(
                title = stringResource(R.string.ren_yu_gang),
                link = "https://blog.csdn.net/singwhatiwanna/"
            ),
            enterArticle
        )

        ProfileProperty(
            imageVector = Icons.Default.Star,
            ArticleModel(
                title = stringResource(R.string.zhang_hong_yang),
                link = "https://blog.csdn.net/lmj623565791"
            ),
            enterArticle
        )

        ProfileProperty(
            imageVector = Icons.Default.Star,
            ArticleModel(
                title = stringResource(R.string.guo_lin),
                link = "https://blog.csdn.net/guolin_blog/"
            ),
            enterArticle
        )

        if (Play.isLogin) {
            val alertDialog = remember { mutableStateOf(false) }

            ShowDialog(
                alertDialog = alertDialog,
                title = stringResource(id = R.string.log_out),
                content = stringResource(id = R.string.sure_log_out),
                cancelString = stringResource(id = R.string.cancel),
                confirmString = stringResource(id = R.string.sure)
            ) {
                logout()
            }
            Button(
                onClick = { alertDialog.value = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
            ) {
                Text(text = stringResource(R.string.log_out))
            }
        }
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
private fun NameAndPosition(modifier: Modifier = Modifier, refresh: Boolean, toLogin: () -> Unit) {
    Column(
        modifier = if (Play.isLogin) {
            modifier
                .fillMaxWidth()
                .padding(top = 10.dp, bottom = 20.dp)
        } else {
            modifier
                .fillMaxWidth()
                .padding(top = 10.dp, bottom = 20.dp)
                .clickable { toLogin() }
        },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        ImageLoader(
            data = R.drawable.img_head,
            Modifier
                .size(65.dp)
                .shadow(1.dp, shape = RoundedCornerShape(10.dp))
        )

        Text(
            text = if (Play.isLogin && refresh) Play.nickName else stringResource(R.string.no_login),
            modifier = Modifier.padding(top = 5.dp),
            style = MaterialTheme.typography.h5
        )
        Text(
            text = if (Play.isLogin && refresh) Play.username else stringResource(R.string.click_login),
            modifier = Modifier.padding(top = 5.dp),
            style = MaterialTheme.typography.subtitle1
        )
    }

}

@Composable
fun ProfileProperty(
    imageVector: ImageVector,
    article: ArticleModel,
    enterArticle: (ArticleModel) -> Unit
) {
    Column(modifier = Modifier
        .clickable {
            enterArticle(article)
        }
    ) {
        Divider()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(imageVector = imageVector, contentDescription = "")
            Text(
                text = article.title,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 5.dp),
                style = MaterialTheme.typography.subtitle2
            )
            Icon(
                Icons.Default.KeyboardArrowRight,
                contentDescription = "",
                modifier = Modifier.wrapContentWidth(Alignment.End),
            )
        }
    }
}
package com.zj.play.ui.page.mine

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zj.play.Play
import com.zj.play.R
import com.zj.play.logic.model.ArticleModel
import com.zj.play.ui.main.PlayActions
import com.zj.play.ui.page.login.LoginViewModel
import com.zj.play.ui.page.login.LogoutDefault
import com.zj.play.ui.page.login.LogoutFinish

@Composable
fun ProfilePage(toLogin: () -> Unit, enterArticle: (ArticleModel) -> Unit) {
    Column(modifier = Modifier.fillMaxSize()) {
        Image(
            modifier = Modifier.fillMaxWidth(),
            painter = painterResource(R.drawable.img_head),
            contentScale = ContentScale.Crop,
            contentDescription = null
        )
        UserInfoFields(
            toLogin,
            enterArticle,
        )
    }
}

@Composable
private fun UserInfoFields(
    toLogin: () -> Unit,
    enterArticle: (ArticleModel) -> Unit
) {
    val viewModel: LoginViewModel = viewModel()
    val logoutState by viewModel.logoutState.observeAsState(LogoutDefault)
    Column {
        Spacer(modifier = Modifier.height(8.dp))

        when (logoutState) {
            LogoutDefault -> {
                NameAndPosition(true, toLogin)
            }
            LogoutFinish -> {
                NameAndPosition(false, toLogin)
            }
        }

        ProfileProperty(
            ArticleModel(
                title = stringResource(R.string.mine_blog),
                link = "https://zhujiang.blog.csdn.net/"
            ),
            enterArticle
        )

        ProfileProperty(
            ArticleModel(
                title = stringResource(R.string.mine_nuggets),
                link = "https://juejin.im/user/5c07e51de51d451de84324d5"
            ),
            enterArticle
        )

        ProfileProperty(
            ArticleModel(
                title = stringResource(R.string.mine_github),
                link = "https://github.com/zhujiang521"
            ),
            enterArticle
        )

        if (Play.isLogin) {
            Button(
                onClick = { viewModel.logout() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
            ) {
                Text(text = stringResource(R.string.log_out))
            }
        }
    }
}

@Composable
private fun NameAndPosition(refresh: Boolean, toLogin: () -> Unit) {
    Column(modifier = if (Play.isLogin) {
        Modifier.padding(horizontal = 16.dp)
    } else {
        Modifier
            .padding(horizontal = 16.dp)
            .clickable { toLogin() }
    }) {
        Text(
            text = if (Play.isLogin && refresh) Play.nickName else stringResource(R.string.no_login),
            modifier = Modifier.height(32.dp),
            style = MaterialTheme.typography.h5
        )
        Text(
            text = if (Play.isLogin && refresh) Play.username else stringResource(R.string.click_login),
            modifier = Modifier
                .padding(bottom = 20.dp)
                .height(24.dp),
            style = MaterialTheme.typography.body1
        )
    }
}

@Composable
fun ProfileProperty(article: ArticleModel, enterArticle: (ArticleModel) -> Unit) {
    Column(modifier = Modifier
        .clickable {
            enterArticle(article)
        }
        .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
    ) {
        Divider()
        Text(
            text = article.title,
            modifier = Modifier.height(24.dp).padding(top = 10.dp),
            style = MaterialTheme.typography.caption
        )
        Text(
            text = article.title,
            modifier = Modifier.height(24.dp),
            style = MaterialTheme.typography.body1
        )
    }
}
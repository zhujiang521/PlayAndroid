package com.zj.play.ui.page.mine

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.zj.play.Play
import com.zj.play.R
import com.zj.play.logic.model.ArticleModel
import com.zj.play.ui.page.login.LogoutDefault
import com.zj.play.ui.page.login.LogoutFinish
import com.zj.play.ui.page.login.LogoutState

@Composable
fun ProfilePage(
    modifier: Modifier = Modifier,
    isLand: Boolean = false,
    toLogin: () -> Unit,
    logoutState: LogoutState,
    logout: () -> Unit,
    enterArticle: (ArticleModel) -> Unit
) {
    if (isLand) {
        Row(modifier = modifier.fillMaxSize()) {
            Image(
                modifier = Modifier.fillMaxHeight().weight(1f),
                painter = painterResource(R.drawable.img_head),
                contentScale = ContentScale.Crop,
                contentDescription = null
            )
            Column(
                modifier = Modifier.fillMaxHeight().weight(1.5f)
                    .verticalScroll(rememberScrollState())
            ) {
                UserInfoFields(
                    toLogin,
                    enterArticle,
                    logoutState,
                    logout
                )
            }
        }
    } else {
        Column(
            modifier = modifier.fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Image(
                modifier = Modifier.fillMaxWidth(),
                painter = painterResource(R.drawable.img_head),
                contentScale = ContentScale.Crop,
                contentDescription = null
            )
            UserInfoFields(
                toLogin,
                enterArticle,
                logoutState,
                logout
            )
        }
    }
}

@Composable
private fun UserInfoFields(
    toLogin: () -> Unit,
    enterArticle: (ArticleModel) -> Unit,
    logoutState: LogoutState,
    logout: () -> Unit
) {
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
                onClick = { logout() },
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
    ) {
        Divider()
        Row(
            modifier = Modifier.fillMaxWidth().height(55.dp).padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = article.title,
                modifier = Modifier.weight(1f),
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
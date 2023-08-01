package com.zj.play.ui.page.mine

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults.buttonColors
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.zj.banner.utils.ImageLoader
import com.zj.model.ArticleModel
import com.zj.play.Play
import com.zj.play.R
import com.zj.play.loginState
import com.zj.play.ui.main.nav.PlayActions
import com.zj.play.ui.page.login.LoginViewModel
import com.zj.play.ui.page.login.LogoutDefault
import com.zj.play.ui.page.login.LogoutFinish
import com.zj.play.ui.page.login.LogoutState
import com.zj.play.ui.view.ShowDialog
import com.zj.play.ui.view.bar.PlayAppBar

@Composable
fun ProfilePage(modifier: Modifier, isLand: Boolean, actions: PlayActions) {
    val viewModel: LoginViewModel = hiltViewModel()
    val logoutState by viewModel.logoutState
    ProfilePageContent(modifier, isLand, actions.toLogin, actions.toTheme, logoutState, {
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
    toTheme: () -> Unit,
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
                HeadItem(Modifier.weight(0.3f), logoutState, toLogin, true)
                BlogItem(Modifier.weight(0.7f), enterArticle, logout, toTheme)
            }
        } else {
            HeadItem(Modifier, logoutState, toLogin, false)
            BlogItem(Modifier, enterArticle, logout, toTheme)
        }
    }
}

@Composable
private fun HeadItem(
    modifier: Modifier = Modifier,
    logoutState: LogoutState,
    toLogin: () -> Unit,
    isLand: Boolean
) {
    when (logoutState) {
        LogoutDefault -> {
            NameAndPosition(modifier, true, toLogin, isLand)
        }

        LogoutFinish -> {
            NameAndPosition(modifier, false, toLogin, isLand)
        }
    }
}

@Composable
private fun BlogItem(
    modifier: Modifier = Modifier,
    enterArticle: (ArticleModel) -> Unit,
    logout: () -> Unit,
    toTheme: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
    ) {
        ProfilePropertyItem(Icons.Default.Favorite, stringResource(id = R.string.theme_change)) {
            toTheme()
        }
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

        ShowLogoutDialog(logout)
    }
}

@Composable
private fun ShowLogoutDialog(logout: () -> Unit) {
    val loginState by loginState
    if (loginState) {
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
            shape = MaterialTheme.shapes.medium,
            colors = buttonColors(
                backgroundColor = Color.Red,
                disabledBackgroundColor = Color.Red.copy(alpha = 0.12f)
            )
        ) {
            Text(text = stringResource(R.string.log_out), color = Color.LightGray)
        }
    }
}

@Composable
private fun NameAndPosition(
    modifier: Modifier = Modifier,
    refresh: Boolean,
    toLogin: () -> Unit,
    isLand: Boolean
) {
    val m = if (isLand) {
        modifier.fillMaxSize()
    } else {
        modifier.fillMaxWidth()
    }
    val loginState by loginState

    Column(
        modifier = if (loginState) {
            m.padding(top = 10.dp, bottom = 20.dp)
        } else {
            m
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
            text = if (loginState && refresh) Play.nickName else stringResource(R.string.no_login),
            modifier = Modifier.padding(top = 5.dp),
            style = MaterialTheme.typography.h6
        )
        Text(
            text = if (loginState && refresh) Play.username else stringResource(R.string.click_login),
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
    ProfilePropertyItem(imageVector, article.title) {
        enterArticle(article)
    }
}

@Composable
fun ProfilePropertyItem(
    imageVector: ImageVector,
    title: String,
    onClick: () -> Unit
) {
    Column(modifier = Modifier
        .clickable {
            onClick()
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
                text = title,
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


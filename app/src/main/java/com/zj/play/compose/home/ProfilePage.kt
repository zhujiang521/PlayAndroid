/*
 * Copyright 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.zj.play.compose.home

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Create
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zj.core.Play
import com.zj.core.util.showToast
import com.zj.model.room.entity.Article
import com.zj.play.R
import com.zj.play.compose.MainActions
import com.zj.play.compose.ThemeViewModel
import com.zj.play.compose.common.AnimatingFabContent
import com.zj.play.main.login.LogoutFinish
import com.zj.play.main.login.LoginViewModel
import com.zj.play.main.login.LogoutDefault
import com.zj.play.compose.common.utils.baselineHeight
import dev.chrisbanes.accompanist.insets.navigationBarsPadding

@Composable
fun ProfilePage(onNavigationEvent: MainActions,themeViewModel: ThemeViewModel) {

    val scrollState = rememberScrollState()
    Column(modifier = Modifier.fillMaxSize()) {
        BoxWithConstraints(modifier = Modifier.weight(1f)) {
            Surface {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState),
                ) {

                    ProfileHeader(
                        scrollState,
                        this@BoxWithConstraints.maxHeight
                    )
                    UserInfoFields(onNavigationEvent.enterArticle, onNavigationEvent.toLogin, this@BoxWithConstraints.maxHeight,
                        themeViewModel)
                }
            }
            ProfileFab(
                themeViewModel = themeViewModel,
                extended = scrollState.value == 0,
                modifier = Modifier.align(Alignment.BottomEnd)
            )
        }
    }
}

@Composable
private fun UserInfoFields(
    enterArticle: (Article) -> Unit,
    toLogin: () -> Unit,
    containerHeight: Dp,
    themeViewModel: ThemeViewModel
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
            Article(title = stringResource(R.string.mine_blog),link = "https://zhujiang.blog.csdn.net/"),
            enterArticle
        )

        ProfileProperty(
            Article(title = stringResource(R.string.mine_nuggets),link = "https://juejin.im/user/5c07e51de51d451de84324d5"),
            enterArticle
        )

        ProfileProperty(
            Article(title = stringResource(R.string.mine_github),link = "https://github.com/zhujiang521"),
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


        // Add a spacer that always shows part (320.dp) of the fields list regardless of the device,
        // in order to always leave some content at the top.
        Spacer(Modifier.height((containerHeight - 320.dp).coerceAtLeast(0.dp)))
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
        Name(
            refresh,
            modifier = Modifier.baselineHeight(32.dp)
        )
        Position(
            refresh,
            modifier = Modifier
                .padding(bottom = 20.dp)
                .baselineHeight(24.dp)
        )
    }
}

@Composable
private fun Name(refresh: Boolean, modifier: Modifier = Modifier) {
    Text(
        text = if (Play.isLogin && refresh) Play.nickName else stringResource(R.string.no_login),
        modifier = modifier,
        style = MaterialTheme.typography.h5
    )
}

@Composable
private fun Position(refresh: Boolean, modifier: Modifier = Modifier) {
    CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
        Text(
            text = if (Play.isLogin && refresh) Play.username else stringResource(R.string.click_login),
            modifier = modifier,
            style = MaterialTheme.typography.body1
        )
    }
}

@Composable
private fun ProfileHeader(
    scrollState: ScrollState,
    containerHeight: Dp
) {
    val offset = (scrollState.value / 2)
    val offsetDp = with(LocalDensity.current) { offset.toDp() }

    Image(
        modifier = Modifier
            .heightIn(max = containerHeight / 2)
            .fillMaxWidth()
            .padding(top = offsetDp / 2),
        painter = painterResource(R.drawable.img_head),
        contentScale = ContentScale.Crop,
        contentDescription = null
    )
}

@Composable
fun ProfileProperty(article: Article, enterArticle: (Article) -> Unit) {
    Column(modifier = Modifier
        .clickable {
            enterArticle(article)
        }
        .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
    ) {
        Divider()
        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
            Text(
                text = article.title,
                modifier = Modifier.baselineHeight(24.dp),
                style = MaterialTheme.typography.caption
            )
        }
        val style = MaterialTheme.typography.body1
        Text(
            text = article.title,
            modifier = Modifier.baselineHeight(24.dp),
            style = style
        )
    }
}

@Composable
fun ProfileFab(themeViewModel: ThemeViewModel,extended: Boolean, modifier: Modifier = Modifier) {
    key(extended) { // Prevent multiple invocations to execute during composition
        FloatingActionButton(
            onClick = { showToast(if (extended) "哈哈哈😄，被你发现小彩蛋了，真棒👍！！！" else "太过分了，这样都要点我😡！！！")
                themeViewModel.onThemeChanged(themeViewModel.theme.value == false)
                      },
            modifier = modifier
                .padding(end = 16.dp, bottom = 70.dp)
                .navigationBarsPadding()
                .height(48.dp)
                .widthIn(min = 48.dp),
            backgroundColor = MaterialTheme.colors.primary,
            contentColor = MaterialTheme.colors.onPrimary
        ) {
            AnimatingFabContent(
                icon = {
                    Icon(
                        imageVector = Icons.Outlined.Create,
                        contentDescription = stringResource(
                            R.string.edit_profile
                        )
                    )
                },
                text = {
                    Text(
                        text = stringResource(
                            R.string.edit_profile
                        ),
                    )
                },
                extended = extended
            )
        }
    }
}
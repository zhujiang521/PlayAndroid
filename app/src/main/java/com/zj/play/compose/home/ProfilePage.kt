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

import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Create
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.zj.core.Play
import com.zj.play.R
import com.zj.play.compose.common.AnimatingFabContent
import com.zj.play.compose.utils.baselineHeight
import dev.chrisbanes.accompanist.insets.navigationBarsPadding

@Composable
fun ProfilePage() {

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
                    UserInfoFields(this@BoxWithConstraints.maxHeight)
                }
            }
            ProfileFab(
                extended = scrollState.value == 0,
                modifier = Modifier.align(Alignment.BottomEnd)
            )
        }
    }
}

@Composable
private fun UserInfoFields(containerHeight: Dp) {
    Column {
        Spacer(modifier = Modifier.height(8.dp))

        NameAndPosition()

        ProfileProperty(stringResource(R.string.mine_points), stringResource(R.string.mine_points))

        ProfileProperty(
            stringResource(R.string.my_collection),
            stringResource(R.string.my_collection)
        )

        ProfileProperty(stringResource(R.string.mine_blog), stringResource(R.string.mine_blog))

        ProfileProperty(
            stringResource(R.string.browsing_history),
            stringResource(R.string.browsing_history)
        )

        ProfileProperty(stringResource(R.string.github), stringResource(R.string.github))

        // Add a spacer that always shows part (320.dp) of the fields list regardless of the device,
        // in order to always leave some content at the top.
        Spacer(Modifier.height((containerHeight - 320.dp).coerceAtLeast(0.dp)))
    }
}

@Composable
private fun NameAndPosition() {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Name(
            modifier = Modifier.baselineHeight(32.dp)
        )
        Position(
            modifier = Modifier
                .padding(bottom = 20.dp)
                .baselineHeight(24.dp)
        )
    }
}

@Composable
private fun Name(modifier: Modifier = Modifier) {
    Text(
        text = if (Play.isLogin) Play.nickName else stringResource(R.string.no_login),
        modifier = modifier,
        style = MaterialTheme.typography.h5
    )
}

@Composable
private fun Position(modifier: Modifier = Modifier) {
    CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
        Text(
            text = if (Play.isLogin) Play.username else stringResource(R.string.click_login),
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
            .padding(top = offsetDp),
        painter = painterResource(R.drawable.ic_head),
        contentScale = ContentScale.Crop,
        contentDescription = null
    )
}

@Composable
fun ProfileProperty(label: String, value: String, isLink: Boolean = false) {
    Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp)) {
        Divider()
        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
            Text(
                text = label,
                modifier = Modifier.baselineHeight(24.dp),
                style = MaterialTheme.typography.caption
            )
        }
        val style = if (isLink) {
            MaterialTheme.typography.body1.copy(color = MaterialTheme.colors.primary)
        } else {
            MaterialTheme.typography.body1
        }
        Text(
            text = value,
            modifier = Modifier.baselineHeight(24.dp),
            style = style
        )
    }
}

@Composable
fun ProfileFab(extended: Boolean, modifier: Modifier = Modifier) {
    key(extended) { // Prevent multiple invocations to execute during composition
        FloatingActionButton(
            onClick = { /* TODO */ },
            modifier = modifier
                .padding(end = 16.dp,bottom = 60.dp)
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
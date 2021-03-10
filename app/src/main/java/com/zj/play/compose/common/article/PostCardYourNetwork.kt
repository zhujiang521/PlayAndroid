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

package com.zj.play.compose.common.article

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.zj.model.room.entity.Article
import com.zj.model.room.entity.BannerBean
import com.zj.play.compose.common.NetworkImage

@Composable
fun PostCardPopular(
    article: BannerBean,
    navigateTo: (Article) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = MaterialTheme.shapes.medium,
        modifier = modifier.size(300.dp, 180.dp)
    ) {
        Column(modifier = Modifier.clickable(onClick = {
            navigateTo(
                Article(
                    title = article.title,
                    link = article.url
                )
            )
        })) {
            NetworkImage(
                url = article.imagePath,
                contentDescription = null,
                modifier = Modifier
                    .size(300.dp, 180.dp)
            )

            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = article.title,
                    style = MaterialTheme.typography.h6,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = article.title,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.body2
                )

                Text(
                    text = "${article.type} - ",
                    style = MaterialTheme.typography.body2
                )
            }
        }
    }
}

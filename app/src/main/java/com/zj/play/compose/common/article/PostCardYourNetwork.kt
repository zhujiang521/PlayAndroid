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

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.blankj.utilcode.util.ImageUtils
import com.zj.model.room.entity.Article
import com.zj.model.room.entity.BannerBean
import com.zj.play.compose.common.NetworkImage

private const val TAG = "PostCardYourNetwork"

@Composable
fun PostCardPopular(
    bannerBean: BannerBean,
    navigateTo: (Article) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = MaterialTheme.shapes.medium,
        modifier = modifier.size(300.dp, 180.dp)
    ) {
        val imgModifier = Modifier
            .size(300.dp, 180.dp)
            .clickable(onClick = {
                navigateTo(
                    Article(
                        title = bannerBean.title,
                        link = bannerBean.url
                    )
                )
            })
        if (bannerBean.filePath == "") {
            Log.e(TAG, "PostCardPopular: 加载网络图片")
            NetworkImage(
                url = bannerBean.imagePath,
                contentDescription = null,
                modifier = imgModifier
            )
        } else {
            Log.e(TAG, "PostCardPopular: 加载本地图片")
            val bitmap = ImageUtils.getBitmap(bannerBean.filePath)
            Image(
                modifier = imgModifier,
                painter = BitmapPainter(bitmap.asImageBitmap()),
                contentDescription = "",
                contentScale = ContentScale.Crop
            )
        }
    }
}

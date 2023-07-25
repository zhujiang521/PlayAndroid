package com.zj.play.widget

import android.graphics.BitmapFactory
import androidx.compose.runtime.Composable
import androidx.core.net.toUri
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.layout.ContentScale

@Composable
fun GlanceImageLoader(
    data: Any?,
    modifier: GlanceModifier = GlanceModifier,
    contentScale: ContentScale = ContentScale.Crop,
) {
    when (data) {
        is String -> {
            val imageProvider = if (data.contains("https://") || data.contains("http://")) {
                getImageProvider(data)
            } else {
                val bitmap = BitmapFactory.decodeFile(data)
                ImageProvider(bitmap)
            }
            Image(
                modifier = modifier,
                provider = imageProvider,
                contentDescription = "",
                contentScale = contentScale
            )
        }

        is Int -> {
            Image(
                modifier = modifier,
                provider = ImageProvider(data),
                contentDescription = "",
                contentScale = contentScale
            )
        }

        else -> {
            throw IllegalArgumentException("参数类型不符合要求，只能是：url、文件路径或者是 drawable id")
        }
    }
}

private fun getImageProvider(path: String): ImageProvider {
    if (path.startsWith("content://")) {
        return androidx.glance.appwidget.ImageProvider(path.toUri())
    }
    val bitmap = BitmapFactory.decodeFile(path)
    return ImageProvider(bitmap)
}
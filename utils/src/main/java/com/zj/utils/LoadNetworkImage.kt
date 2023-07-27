package com.zj.utils

import android.accounts.NetworkErrorException
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import kotlinx.coroutines.suspendCancellableCoroutine
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL


/**
 * 通过 网络图片 url 获取图片 Bitmap
 * @param photoUrl 网络图片 url
 */
suspend fun requestWebPhotoBitmap(photoUrl: String): Bitmap =
    suspendCancellableCoroutine { cancellableContinuation ->
        var connection: HttpURLConnection? = null
        try {
            val bitmapUrl = URL(photoUrl)
            connection = bitmapUrl.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.connectTimeout = 5000
            connection.readTimeout = 5000

            // 判断是否请求成功
            if (connection.responseCode == 200) {
                val inputStream = connection.inputStream
                val imgBitmap = BitmapFactory.decodeStream(inputStream)
                cancellableContinuation.resumeWith(Result.success(imgBitmap))
            } else {
                cancellableContinuation.resumeWith(Result.failure(NetworkErrorException("Network error.")))
            }
        } catch (e: IOException) {
            e.printStackTrace()
            cancellableContinuation.resumeWith(Result.failure(e))
        } finally {
            connection?.disconnect()
        }
    }
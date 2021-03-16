package com.zj.network.down

import android.net.Uri
import android.os.Environment
import android.webkit.MimeTypeMap
import com.zj.network.base.ServiceCreator
import com.zj.network.service.UrlService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream

object Download {

    suspend fun download(url: String, build: IDownloadBuild) = flow {
        val response = ServiceCreator.create(UrlService::class.java).downloadFile(url)
        response.body().let { body ->
            if (body == null) return@let
            val length = body.contentLength()
            val contentType = body.contentType().toString()
            val ios = body.byteStream()
            val info = try {
                downloadBuildToOutputStream(build, contentType)
            } catch (e: Exception) {
                emit(DownloadStatus.DownloadError(e))
                DownloadInfo(null)
                return@flow
            }
            val ops = info.ops
            if (ops == null) {
                emit(DownloadStatus.DownloadError(RuntimeException("下载出错")))
                return@flow
            }
            //下载的长度
            var currentLength = 0
            //写入文件
            val bufferSize = 1024 * 8
            val buffer = ByteArray(bufferSize)
            val bufferedInputStream = BufferedInputStream(ios, bufferSize)
            var readLength: Int
            kotlin.runCatching {
                while (bufferedInputStream.read(buffer, 0, bufferSize)
                        .also { readLength = it } != -1
                ) {
                    ops.write(buffer, 0, readLength)
                    currentLength += readLength
                    emit(
                        DownloadStatus.DownloadProcess(
                            currentLength.toLong(),
                            length,
                            currentLength.toFloat() / length.toFloat()
                        )
                    )
                }
                bufferedInputStream.close()
                ops.close()
                ios.close()
            }

            if (info.uri != null)
                emit(DownloadStatus.DownloadSuccess(info.uri, info.file))
            else emit(DownloadStatus.DownloadSuccess(Uri.fromFile(info.file), info.file))
        }
    }.flowOn(Dispatchers.IO)

    private fun downloadBuildToOutputStream(
        build: IDownloadBuild,
        contentType: String
    ): DownloadInfo {
        val context = build.getContext()
        val uri = build.getUri(contentType)
        return when {
            build.getDownloadFile() != null -> {
                val file = build.getDownloadFile()!!
                DownloadInfo(FileOutputStream(file), file)
            }
            uri != null -> {
                DownloadInfo(context.contentResolver.openOutputStream(uri), uri = uri)
            }
            else -> {
                val name = build.getFileName()
                val fileName =
                    if (!name.isNullOrBlank()) name else "${System.currentTimeMillis()}.${
                        MimeTypeMap.getSingleton()
                            .getExtensionFromMimeType(contentType)
                    }"
                val file =
                    File(
                        "${context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)}",
                        fileName
                    )
                DownloadInfo(FileOutputStream(file), file)
            }
        }
    }


}
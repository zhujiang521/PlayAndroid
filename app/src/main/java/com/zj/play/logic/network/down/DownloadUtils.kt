package com.zj.play.logic.network.down

import android.content.Context
import android.net.Uri
import java.io.File
import java.io.OutputStream

sealed class DownloadStatus {
    class DownloadProcess(val currentLength: Long, val length: Long, val process: Float) :
        DownloadStatus()

    class DownloadError(val t: Throwable) : DownloadStatus()
    class DownloadSuccess(val uri: Uri, val file: File?) : DownloadStatus()
}

abstract class IDownloadBuild {
    open fun getFileName(): String? = null
    open fun getUri(contentType: String): Uri? = null
    open fun getDownloadFile(): File? = null
    abstract fun getContext(): Context
}


class DownloadBuild(private val cxt: Context) : IDownloadBuild() {
    override fun getContext(): Context = cxt
}

class DownloadInfo(val ops: OutputStream?, val file: File? = null, val uri: Uri? = null)
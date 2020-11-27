package com.zj.core.almanac

import android.content.Context
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.core.content.FileProvider
import com.zj.core.util.AndroidVersion
import java.io.File

/**
 * 版权：Zhujiang 个人版权
 *
 * @author zhujiang
 * 创建日期：11/16/20
 * 描述：文件的工具类
 *
 */

object FileStorageUtils {

    /**
     * 获取文件的 Uri
     *
     * @param context /
     * @param file 文件
     */
    fun getFileUri(context: Context, file: File): Uri? {
        return FileProvider.getUriForFile(
                context,
                "com.zj.play.fileprovider",
                file).also {
            Log.e("FileUriUtils", "getFileUri: $it")
        }
    }

    fun getStorageFilePath(context: Context): String? {
        return if (AndroidVersion.hasQ()) {
            if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()
                    || !Environment.isExternalStorageRemovable()) {
                //外部存储可用
                context.externalCacheDir?.path
            } else {
                //外部存储不可用
                context.cacheDir.path
            }
        } else {
            Environment.getExternalStorageDirectory().absolutePath
        }
    }

    fun getStorageFile(context: Context): File? {
        return if (AndroidVersion.hasQ()) {
            if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()
                    || !Environment.isExternalStorageRemovable()) {
                //外部存储可用
                context.externalCacheDir
            } else {
                //外部存储不可用
                context.cacheDir
            }
        } else {
            Environment.getExternalStorageDirectory()
        }
    }

}
package com.zj.core.almanac

import android.app.Activity
import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Rect
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import com.blankj.utilcode.util.ConvertUtils
import com.blankj.utilcode.util.ViewUtils
import com.zj.core.R
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

/**
 * 版权：Zhujiang 个人版权
 *
 * @author zhujiang
 * 创建日期：11/26/20
 * 描述：PlayAndroid
 *
 */
object ScreenShotsUtils {

    private fun getBitmapByView(frameLayout: FrameLayout): Bitmap? {
        var height = 0
        for (i in 0 until frameLayout.childCount) {
            height += frameLayout.getChildAt(i).height
        }
        height += ConvertUtils.dp2px(8f)
        val bitmap = Bitmap.createBitmap(frameLayout.width, height, Bitmap.Config.RGB_565)
        val canvas = Canvas(bitmap)
        frameLayout.draw(canvas)
        val bitmapDes = Bitmap.createBitmap(frameLayout.width, height, Bitmap.Config.RGB_565)
        val canvasDes = Canvas(bitmapDes)
        canvasDes.drawBitmap(bitmap, 0f, 0f, null)
        canvas.save()
        canvas.translate(0f, 0f)
        canvas.clipRect(0, 0, frameLayout.width, height)
        canvas.drawBitmap(bitmap, 0f, 0f, null)
        canvasDes.drawBitmap(bitmap, 0f, 0f, null)
        canvas.restore()
        bitmap.recycle()
        return bitmapDes
    }

    private fun shoot(view: View?, filePath: File): Boolean {
        var res = false
        if (view == null) {
            return res
        }
        if (!filePath.parentFile.exists()) {
            filePath.parentFile.mkdirs()
        }
        var fos: FileOutputStream? = null
        var bitmap: Bitmap? = null
        try {
            fos = FileOutputStream(filePath)
            bitmap = getBitmapByView(view as FrameLayout)
            if (bitmap != null) {
                val frame = Rect()
                (view.context as Activity).window.decorView.getWindowVisibleDisplayFrame(frame)
                Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height)
                    .compress(Bitmap.CompressFormat.PNG, 100, fos)
                view.destroyDrawingCache()
                view.setDrawingCacheEnabled(false)
                res = true
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } finally {
            if (fos != null) {
                try {
                    fos.flush()
                    fos.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            bitmap?.recycle()
        }
        return res
    }

    fun takeScreenShot(activity: Activity, view: View): Uri? {
        val filePath: String = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).absolutePath
        val fileName: String = UUID.randomUUID().toString() + ".png"
        if (TextUtils.isEmpty(filePath) || TextUtils.isEmpty(fileName)) {
            return null
        }
        if (shoot(view, File("$filePath/$fileName"))) {
            val values = ContentValues()
            values.put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
            values.put(MediaStore.Images.Media.DESCRIPTION, "almanac")
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/png")
            values.put(MediaStore.Images.Media.TITLE, fileName)
            values.put(
                MediaStore.Images.Media.RELATIVE_PATH,
                "${Environment.DIRECTORY_PICTURES}/${fileName}"
            )
            val audioCollection = MediaStore.Images.Media.getContentUri(
                MediaStore.VOLUME_EXTERNAL_PRIMARY
            )
            activity.contentResolver.insert(audioCollection, values)
        }
        return FileStorageUtils.getFileUri(activity, File("$filePath/$fileName"))
    }

}
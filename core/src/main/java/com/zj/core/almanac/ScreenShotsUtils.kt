package com.zj.core.almanac

import android.app.Activity
import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.FrameLayout
import com.zj.core.util.AndroidVersion
import com.zj.core.util.dp2px
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
        height += frameLayout.context.dp2px(8f)
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

    fun takeScreenShot(activity: Activity, view: View?): Uri? {
        val fileName: String = UUID.randomUUID().toString() + ".png"
        if (view == null) {
            return null
        }
        return addBitmapToAlbum(activity, getBitmapByView(view as FrameLayout), fileName)
    }

    private fun addBitmapToAlbum(activity: Activity, bitmap: Bitmap?, displayName: String): Uri? {
        val values = ContentValues()
        values.put(MediaStore.MediaColumns.DISPLAY_NAME, displayName)
        values.put(MediaStore.MediaColumns.MIME_TYPE, "image/*")
        if (AndroidVersion.hasQ()) {
            values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DCIM)
        } else {
            values.put(
                MediaStore.MediaColumns.DATA,
                "${Environment.getExternalStorageDirectory().path}/${Environment.DIRECTORY_DCIM}/$displayName"
            )
        }
        val uri =
            activity.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        if (uri != null) {
            val outputStream = activity.contentResolver.openOutputStream(uri)
            if (outputStream != null) {
                bitmap?.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                outputStream.close()
            }
        }
        return uri
    }


}
package com.zj.floating

import android.content.res.Resources
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable

/**
 *
 * 可旋转的进度条位图，继承自 [Drawable] <br></br>
 * 原理：利用 [BitmapShader] 绘制出圆形图案，周围留出空白以便绘制进度条。
 *
 */
class FloatingDrawable : Drawable {
    private var mPaint: Paint? = null
    private val drawable: Drawable
    private var mWidth = 0

    constructor(drawable: Drawable) {
        this.drawable = drawable
        circleBitmapFromDrawable(this.drawable)
    }

    constructor(res: Resources?, bitmap: Bitmap?) {
        drawable = BitmapDrawable(res, bitmap)
        circleBitmapFromDrawable(drawable)
    }

    override fun draw(canvas: Canvas) {
        // 画背景图
        canvas.save()
        canvas.drawCircle(
            (mWidth shr 1).toFloat(),
            (mWidth shr 1).toFloat(),
            (mWidth shr 1).toFloat(),
            mPaint!!
        )
        canvas.restore()
    }

    /**
     * 圆形
     */
    private fun circleBitmap(mBitmap: Bitmap) {
        val bitmapShader = BitmapShader(
            mBitmap, Shader.TileMode.CLAMP,
            Shader.TileMode.CLAMP
        )
        mPaint = Paint()
        mPaint!!.isAntiAlias = true
        mPaint!!.shader = bitmapShader
        mWidth = mBitmap.width.coerceAtMost(mBitmap.height)
    }

    private fun circleBitmapFromDrawable(drawable: Drawable) {
        val mBitmap: Bitmap = if (drawable is ColorDrawable) {
            Bitmap.createBitmap(
                COLORDRAWABLE_DIMENSION,
                COLORDRAWABLE_DIMENSION,
                BITMAP_CONFIG
            )
        } else {
            Bitmap.createBitmap(
                drawable.intrinsicWidth,
                drawable.intrinsicHeight, BITMAP_CONFIG
            )
        }
        val canvas = Canvas(mBitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        circleBitmap(mBitmap)
    }

    override fun getIntrinsicWidth(): Int {
        return mWidth
    }

    override fun getIntrinsicHeight(): Int {
        return mWidth
    }

    override fun setAlpha(alpha: Int) {
        mPaint!!.alpha = alpha
    }

    override fun setColorFilter(cf: ColorFilter?) {
        mPaint!!.colorFilter = cf
    }

    override fun getOpacity(): Int {
        return PixelFormat.TRANSLUCENT
    }

    companion object {
        private const val COLORDRAWABLE_DIMENSION = 2
        private val BITMAP_CONFIG = Bitmap.Config.RGB_565
    }
}
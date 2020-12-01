package com.zj.floating

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.lang.reflect.InvocationTargetException

/**
 *
 * 装载旋转进度按钮位图的按钮，继承自[FloatingActionButton]
 *
 */
class FloatingButton @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FloatingActionButton(
    context!!, attrs, defStyleAttr
) {
    private var coverDrawable: FloatingDrawable? = null
    private var backgroundHint: ColorStateList? = null
    private var progress = 0f
    private var isRotation = false

    /**
     * 利用反射重新定义fab图片的大小，默认充满整个fab
     */
    private fun setMaxImageSize() {
        try {
            val clazz: Class<*> = javaClass.superclass
            val sizeMethod = clazz.getDeclaredMethod("getSizeDimension")
            sizeMethod.isAccessible = true
            var size = sizeMethod.invoke(this) as Int
            size -= 50
            //set fab maxsize
            val field = clazz.getDeclaredField("maxImageSize")
            field.isAccessible = true
            field.setInt(this, size)
            //get fab impl
            val field2 = clazz.getDeclaredField("impl")
            field2.isAccessible = true
            val o = field2[this]
            //set fabimpl maxsize
            val maxMethod = o.javaClass.superclass.getDeclaredMethod(
                "setMaxImageSize",
                Int::class.javaPrimitiveType
            )
            maxMethod.isAccessible = true
            maxMethod.invoke(o, size)
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        }
    }

    /**
     * 对fmb进行配置
     *
     * @param backgroundHint fmb背景颜色
     */
    fun config(backgroundHint: ColorStateList?) {
        this.backgroundHint = backgroundHint
        config()
    }

    private fun config() {
        if (backgroundHint != null) {
            backgroundTintList = backgroundHint
        }
    }

    /**
     * 设置按钮背景
     *
     * @param drawable
     */
    fun setCoverDrawable(drawable: Drawable?) {
        if (drawable == null){
            throw NullPointerException("drawable is not null")
        }
        coverDrawable = FloatingDrawable(drawable)
        config()
        setImageDrawable(coverDrawable)
        postInvalidate()
    }

    fun setCover(bitmap: Bitmap?) {
        coverDrawable = FloatingDrawable(resources, bitmap)
        config()
        setImageDrawable(coverDrawable)
        postInvalidate()
    }

    override fun onSaveInstanceState(): Parcelable {
        super.onSaveInstanceState()
        val bundle = Bundle()
        bundle.putBoolean("rotation", isRotation)
        bundle.putFloat("progress", progress)
        if (coverDrawable != null) {
            bundle.putFloat("rotation_angle", coverDrawable!!.rotation)
        }
        return bundle
    }

    override fun onRestoreInstanceState(state: Parcelable) {
        super.onRestoreInstanceState(state)
        val bundle = state as Bundle
        isRotation = bundle.getBoolean("rotation")
        progress = bundle.getFloat("progress")
        requestLayout()
    }

    init {
        setMaxImageSize()
    }
}
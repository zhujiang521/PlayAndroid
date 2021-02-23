package com.zj.play.main

import android.content.Context
import android.content.res.Configuration
import android.graphics.BitmapFactory
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.zj.floating.FloatingMenu
import com.zj.play.R
import com.zj.play.databinding.LayoutHomeBottomLandTabBinding


class HomeBottomLandTabWidget @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseHomeBottomTabWidget(context, attrs, defStyleAttr),
    View.OnClickListener {

    private var floatingButtons: ArrayList<FloatingActionButton>? = null
    private lateinit var fabMenu: FloatingMenu

    /**
     * 初始化 设置点击事件。
     *
     */
    init { //默认第一个碎片
        val isPort = resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT
        if (!isPort) {
            val view =
                LayoutHomeBottomLandTabBinding.inflate(LayoutInflater.from(context), this, true)
            view.apply {
                floatingButtons = arrayListOf(fabHome, fabRepo, fabProject, fabProfile)
            }
            fabMenu = view.fabMenu
            for (floatingButton in floatingButtons!!) {
                floatingButton.setOnClickListener(this)
            }
        }
    }

    /**
     * 销毁，避免内存泄漏
     */
    override fun destroy() {
        super.destroy()
        if (!floatingButtons.isNullOrEmpty()) {
            floatingButtons?.clear()
            floatingButtons = null
        }
    }

    /**
     * 实现按钮的点击事件
     */
    override fun onClick(v: View) {
        when (v.id) {
            R.id.fabHome -> {
                fragmentManger(0)
            }
            R.id.fabRepo -> {
                fragmentManger(1)
            }
            R.id.fabProject -> {
                fragmentManger(2)
            }
            R.id.fabProfile -> {
                fragmentManger(3)
            }
        }
    }

    /**
     * fragment的切换 实现底部导航栏的切换
     *
     * @param position 序号
     */
    override fun fragmentManger(position: Int) {
        super.fragmentManger(position)
        fabMenu.setCover(
            BitmapFactory.decodeResource(
                context.resources,
                when (position) {
                    0 -> R.drawable.ic_nav_news_actived
                    1 -> R.drawable.ic_nav_tweet_actived
                    2 -> R.drawable.ic_nav_discover_actived
                    3 -> R.drawable.ic_nav_my_pressed
                    else -> R.drawable.ic_nav_news_actived
                }
            )
        )
        if (fabMenu.isExpanded) fabMenu.toggle()
        for (j in floatingButtons!!.indices) {
            floatingButtons!![j].isSelected = position == j
        }
    }

}
package com.zj.play.main

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.zj.core.view.custom.floating.FloatingMenu
import com.zj.play.R
import com.zj.play.databinding.LayoutHomeBottomLandTabBinding


class HomeBottomLandTabWidget @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseHomeBottomTabWidget(context, attrs, defStyleAttr),
    View.OnClickListener {

    private var floatingButtons: ArrayList<FloatingActionButton> = arrayListOf()
    private var fabMenu: FloatingMenu

    /**
     * 初始化 设置点击事件。
     *
     */
    init { //默认第一个碎片
        val view =
            LayoutHomeBottomLandTabBinding.inflate(LayoutInflater.from(context), this, true)
        view.apply {
            floatingButtons = arrayListOf(fabHome, fabRepo, fabProject, fabProfile)
        }
        fabMenu = view.fabMenu
        for (floatingButton in floatingButtons) {
            floatingButton.setOnClickListener(this)
        }
    }

    /**
     * 销毁，避免内存泄漏
     */
    override fun destroy() {
        super.destroy()
        if (floatingButtons.isNotEmpty()) {
            floatingButtons.clear()
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
            when (position) {
                0 -> R.drawable.ic_home_selected
                1 -> R.drawable.ic_project_selected
                2 -> R.drawable.ic_account_selected
                3 -> R.drawable.ic_mine_selected
                else -> R.drawable.ic_project_selected
            }
        )
        if (fabMenu.isExpanded) fabMenu.toggle()
        for (j in floatingButtons.indices) {
            floatingButtons[j].isSelected = position == j
        }
    }

}
package com.zj.play.view.main

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.zj.play.R
import com.zj.play.view.main.FragmentFactory.Companion.getCurrentFragment


class HomeBottomTabWidget @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseHomeBottomTabWidget(context, attrs, defStyleAttr, R.layout.layout_home_bottom_tab),
    View.OnClickListener {

    private var textViews: ArrayList<TextView>? = null

    /**
     * 初始化 设置点击事件。
     *
     * @param view /
     */
    override fun initView(view: View) { //默认第一个碎片
        textViews = arrayListOf(
            view.findViewById(R.id.llHomeATHome),
            view.findViewById(R.id.llHomeATCalendar),
            view.findViewById(R.id.llHomeATObject),
            view.findViewById(R.id.llHomeATMy)
        )
        for (textView in textViews!!) {
            textView.setOnClickListener(this)
        }
    }

    /**
     * 销毁，避免内存泄漏
     */
    override fun destroy() {
        super.destroy()
        if (!textViews.isNullOrEmpty()) {
            textViews!!.clear()
            textViews = null
        }
    }

    /**
     * 实现按钮的点击事件
     */
    override fun onClick(v: View) {
        when (v.id) {
            R.id.llHomeATHome -> fragmentManger(0)
            R.id.llHomeATCalendar -> fragmentManger(1)
            R.id.llHomeATObject -> fragmentManger(2)
            R.id.llHomeATMy -> fragmentManger(3)
        }
    }

    /**
     * fragment的切换 实现底部导航栏的切换
     *
     * @param position 序号
     */
    override fun fragmentManger(position: Int) {
        super.fragmentManger(position)
        for (j in textViews!!.indices) {
            textViews!![j].isSelected = position == j
        }
    }

}
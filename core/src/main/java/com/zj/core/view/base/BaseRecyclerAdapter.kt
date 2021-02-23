package com.zj.core.view.base

import androidx.recyclerview.widget.RecyclerView

/**
 * 版权：Zhujiang 个人版权
 *
 * @author zhujiang
 * 创建日期：2/23/21
 * 描述：PlayAndroid
 *
 */
abstract class BaseRecyclerAdapter<T : RecyclerView.ViewHolder> : RecyclerView.Adapter<T>() {

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

}
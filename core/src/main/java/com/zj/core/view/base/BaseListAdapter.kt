package com.zj.core.view.base

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer

abstract class BaseListAdapter<T : Any>(
    protected val mContext: Context,
    private val layoutId: Int,
    private val dataList: List<T>
) : RecyclerView.Adapter<BaseListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        convert(holder, dataList[position], position)
    }

    abstract fun convert(holder: ViewHolder, data: T, position: Int)

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemCount() = dataList.size

    class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView),
        LayoutContainer

}
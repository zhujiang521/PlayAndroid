package com.zj.core.view.custom

import android.content.Context
import com.zhy.adapter.recyclerview.CommonAdapter

/**
 * 版权：Zhujiang 个人版权
 *
 * @author zhujiang
 * 创建日期：2020/11/10
 *
 * 描述：PlayAndroid
 *
 */
abstract class BaseCommonAdapter<T : Any>(context: Context, layoutId: Int, data: List<T>) :
    CommonAdapter<T>(context, layoutId, data) {

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

}
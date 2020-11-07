package com.zj.play.article

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

/**
 * 版权：联想 版权所有
 *
 * @author zhujiang
 * 创建日期：2020/11/7
 * 描述：PlayAndroid
 *
 */
class ArticleViewModel : ViewModel() {

    fun setCollect(isCollection: Int, pageId: Int, originId: Int, context: Context,collectListener: (Boolean) -> Unit) {
        viewModelScope.launch {
            ArticleRepository.setCollect(isCollection, pageId, originId, context,collectListener)
        }
    }

}
package com.zj.play.article

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 版权：联想 版权所有
 *
 * @author zhujiang
 * 创建日期：2020/11/7
 * 描述：PlayAndroid
 *
 */
@HiltViewModel
class ArticleViewModel @Inject constructor(private val articleRepository: ArticleRepository) :
    ViewModel() {

    fun setCollect(
        isCollection: Int,
        pageId: Int,
        originId: Int,
        collectListener: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
            articleRepository.setCollect(isCollection, pageId, originId, collectListener)
        }
    }

}
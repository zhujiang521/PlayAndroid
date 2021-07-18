package com.zj.play.article.collect

import androidx.lifecycle.LiveData
import com.zj.core.view.base.BaseViewModel
import com.zj.model.model.Collect
import com.zj.model.model.CollectX
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * 版权：Zhujiang 个人版权
 * @author zhujiang
 * 版本：1.5
 * 创建日期：2020/5/19
 * 描述：PlayAndroid
 *
 */
@HiltViewModel
class CollectListViewModel @Inject constructor(private val collectRepository: CollectRepository) :
    BaseViewModel<Collect, CollectX, Int>() {

    override fun getData(page: Int): LiveData<Result<Collect>> {
        return collectRepository.getCollectList(page - 1)
    }

}
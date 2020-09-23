package com.zj.play.view.collect

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.zj.play.model.CollectX
import com.zj.play.network.CollectRepository

/**
 * 版权：渤海新能 版权所有
 * @author zhujiang
 * 版本：1.5
 * 创建日期：2020/5/19
 * 描述：PlayAndroid
 *
 */
class CollectListViewModel : ViewModel() {

    val collectList = ArrayList<CollectX>()

    private val pageLiveData = MutableLiveData<Int>()

    val collectLiveData = Transformations.switchMap(pageLiveData) { page ->
        CollectRepository.getCollectList(page - 1)
    }

    fun getArticleList(page: Int) {
        pageLiveData.value = page
    }


}
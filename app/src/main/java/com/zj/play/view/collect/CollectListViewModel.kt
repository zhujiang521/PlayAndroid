package com.zj.play.view.collect

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.zj.core.view.BaseViewModel
import com.zj.play.model.Collect
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
class CollectListViewModel : BaseViewModel<Collect,CollectX,Int>() {

    override fun getData(page: Int): LiveData<Result<Collect>> {
        return CollectRepository.getCollectList(page - 1)
    }

}
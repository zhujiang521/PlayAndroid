package com.zj.play.article.collect

import androidx.lifecycle.LiveData
import com.zj.core.view.base.BaseViewModel
import com.zj.model.model.Collect
import com.zj.model.model.CollectX
import com.zj.network.repository.CollectRepository

/**
 * 版权：渤海新能 版权所有
 * @author zhujiang
 * 版本：1.5
 * 创建日期：2020/5/19
 * 描述：PlayAndroid
 *
 */
class CollectListViewModel : BaseViewModel<Collect, CollectX, Int>() {

    override fun getData(page: Int): LiveData<Result<Collect>> {
        return CollectRepository.getCollectList(page - 1)
    }

}
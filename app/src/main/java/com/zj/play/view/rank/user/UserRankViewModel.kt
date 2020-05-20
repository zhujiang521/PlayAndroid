package com.zj.play.view.rank.user

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.zj.play.model.Ranks
import com.zj.play.network.RankRepository

/**
 * 版权：渤海新能 版权所有
 * @author zhujiang
 * 版本：1.5
 * 创建日期：2020/5/19
 * 描述：PlayAndroid
 *
 */
class UserRankViewModel : ViewModel() {

    val rankList = ArrayList<Ranks>()

    private val pageLiveData = MutableLiveData<Int>()

    val rankLiveData = Transformations.switchMap(pageLiveData) { page ->
        RankRepository.getUserRank(page)
    }

    fun getRankList(page: Int) {
        pageLiveData.value = page
    }

}
package com.zj.play.view.rank.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.zj.core.view.BaseViewModel
import com.zj.play.model.RankList
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
class UserRankViewModel : BaseViewModel<RankList, Ranks, Int>() {

    override fun getData(page: Int): LiveData<Result<RankList>> {
        return RankRepository.getUserRank(page)
    }

}
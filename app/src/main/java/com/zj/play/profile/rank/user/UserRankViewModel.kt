package com.zj.play.profile.rank.user

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import com.zj.core.view.base.BaseViewModel
import com.zj.model.model.RankList
import com.zj.model.model.Ranks
import com.zj.play.profile.rank.RankRepository

/**
 * 版权：Zhujiang 个人版权
 * @author zhujiang
 * 版本：1.5
 * 创建日期：2020/5/19
 * 描述：PlayAndroid
 *
 */
class UserRankViewModel @ViewModelInject constructor(private val rankRepository: RankRepository) :
    BaseViewModel<RankList, Ranks, Int>() {

    override fun getData(page: Int): LiveData<Result<RankList>> {
        return rankRepository.getUserRank(page)
    }

}
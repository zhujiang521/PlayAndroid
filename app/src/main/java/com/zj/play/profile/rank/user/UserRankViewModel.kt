package com.zj.play.profile.rank.user

import androidx.lifecycle.LiveData
import com.zj.core.view.BaseViewModel
import com.zj.model.model.RankList
import com.zj.model.model.Ranks
import com.zj.network.repository.RankRepository

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
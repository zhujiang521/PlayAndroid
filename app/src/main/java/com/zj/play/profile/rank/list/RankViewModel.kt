package com.zj.play.profile.rank.list

import androidx.lifecycle.LiveData
import com.zj.core.view.base.BaseViewModel
import com.zj.model.model.Rank
import com.zj.model.model.RankData
import com.zj.network.repository.RankRepository

/**
 * 版权：Zhujiang 个人版权
 * @author zhujiang
 * 版本：1.5
 * 创建日期：2020/5/19
 * 描述：PlayAndroid
 *
 */
class RankViewModel : BaseViewModel<RankData, Rank, Int>() {

    override fun getData(page: Int): LiveData<Result<RankData>> {
        return RankRepository.getRankList(page)
    }

}
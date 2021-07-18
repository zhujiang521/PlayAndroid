package com.zj.play.profile.rank.user

import androidx.lifecycle.LiveData
import com.zj.core.view.base.BaseViewModel
import com.zj.model.model.RankList
import com.zj.model.model.Ranks
import com.zj.play.profile.rank.RankRepository
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
class UserRankViewModel @Inject constructor(private val rankRepository: RankRepository) :
    BaseViewModel<RankList, Ranks, Int>() {

    override fun getData(page: Int): LiveData<Result<RankList>> {
        return rankRepository.getUserRank(page)
    }

}
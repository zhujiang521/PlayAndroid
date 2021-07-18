package com.zj.play.profile.rank.list

import androidx.lifecycle.LiveData
import com.zj.core.view.base.BaseViewModel
import com.zj.model.model.Rank
import com.zj.model.model.RankData
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
class RankViewModel @Inject constructor(private val rankRepository: RankRepository) :
    BaseViewModel<RankData, Rank, Int>() {

    override fun getData(page: Int): LiveData<Result<RankData>> {
        return rankRepository.getRankList(page)
    }

}
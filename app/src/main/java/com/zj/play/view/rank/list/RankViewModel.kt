package com.zj.play.view.rank.list

import androidx.lifecycle.LiveData
import com.zj.core.view.BaseViewModel
import com.zj.play.model.Rank
import com.zj.play.model.RankData
import com.zj.play.network.RankRepository

/**
 * 版权：渤海新能 版权所有
 * @author zhujiang
 * 版本：1.5
 * 创建日期：2020/5/19
 * 描述：PlayAndroid
 *
 */
class RankViewModel : BaseViewModel<RankData,Rank,Int>() {

    override fun getData(page: Int): LiveData<Result<RankData>> {
        return RankRepository.getRankList(page)
    }

}
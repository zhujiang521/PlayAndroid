package com.zj.play.official.list

import androidx.lifecycle.LiveData
import com.zj.core.view.base.BaseAndroidViewModel
import com.zj.model.pojo.QueryArticle
import com.zj.model.room.entity.Article
import com.zj.play.official.OfficialRepository
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
class OfficialListViewModel @Inject constructor(
    private val officialRepository: OfficialRepository
) : BaseAndroidViewModel<List<Article>, Article, QueryArticle>() {

    override fun getData(page: QueryArticle): LiveData<Result<List<Article>>> {
        return officialRepository.getWxArticle(page)
    }

}


package com.zj.play.official.list

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import com.zj.core.view.base.BaseAndroidViewModel
import com.zj.model.pojo.QueryArticle
import com.zj.model.room.entity.Article
import com.zj.play.official.OfficialRepository
import dagger.hilt.android.scopes.FragmentScoped

/**
 * 版权：Zhujiang 个人版权
 * @author zhujiang
 * 版本：1.5
 * 创建日期：2020/5/19
 * 描述：PlayAndroid
 *
 */
@FragmentScoped
class OfficialListViewModel @ViewModelInject constructor(
    private val officialRepository: OfficialRepository
) : BaseAndroidViewModel<List<Article>, Article, QueryArticle>() {

    override fun getData(page: QueryArticle): LiveData<Result<List<Article>>> {
        return officialRepository.getWxArticle(page)
    }

}


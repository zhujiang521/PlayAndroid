package com.zj.play.official.list

import android.app.Application
import androidx.lifecycle.LiveData
import com.zj.play.compose.common.BaseAndroidViewModel
import com.zj.model.pojo.QueryArticle
import com.zj.model.room.entity.Article
import com.zj.play.official.OfficialRepository

/**
 * 版权：Zhujiang 个人版权
 * @author zhujiang
 * 版本：1.5
 * 创建日期：2020/5/19
 * 描述：PlayAndroid
 *
 */
class OfficialListViewModel(application: Application) :
    BaseAndroidViewModel<List<Article>, Article, QueryArticle>(application) {

    private val officialRepository = OfficialRepository(application)

    override suspend fun getData(page: QueryArticle){
        return officialRepository.getWxArticle(_state,page)
    }

}


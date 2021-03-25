package com.zj.play.compose.repository

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.zj.model.model.ArticleList
import com.zj.model.model.BaseModel
import com.zj.play.compose.model.QueryArticle
import com.zj.model.room.entity.Article
import com.zj.model.room.entity.OFFICIAL
import com.zj.model.room.entity.ProjectClassify
import com.zj.network.base.PlayAndroidNetwork
import com.zj.play.compose.model.PlayState

/**
 * 版权：Zhujiang 个人版权
 *
 * @author zhujiang
 * 创建日期：2020/9/10
 * 描述：PlayAndroid
 *
 */
class OfficialRepository(application: Application) : ArticleRepository(application) {

    override suspend fun getArticleTree(): BaseModel<List<ProjectClassify>> {
        return PlayAndroidNetwork.getWxArticleTree()
    }

    override suspend fun getFlag(): String {
        return DOWN_OFFICIAL_ARTICLE_TIME
    }

}
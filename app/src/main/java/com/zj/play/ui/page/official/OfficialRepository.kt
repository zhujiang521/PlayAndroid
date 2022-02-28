package com.zj.play.ui.page.official

import android.app.Application
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.zj.model.BaseModel
import com.zj.model.ClassifyModel
import com.zj.model.Query
import com.zj.network.PlayAndroidNetwork
import com.zj.play.logic.paging.OfficialPagingSource
import com.zj.play.logic.repository.BaseArticleRepository

/**
 * 版权：Zhujiang 个人版权
 *
 * @author zhujiang
 * 创建日期：2021/9/10
 * 描述：PlayAndroid
 *
 */
class OfficialRepository(application: Application) : BaseArticleRepository(application) {

    override suspend fun getArticleTree(): BaseModel<List<ClassifyModel>> {
        return PlayAndroidNetwork.getWxArticleTree()
    }

    override fun getPagingData(query: Query) = Pager(
        PagingConfig(
            pageSize = PAGE_SIZE,
            enablePlaceholders = false
        )
    ) {
        OfficialPagingSource(query.cid)
    }.flow

}
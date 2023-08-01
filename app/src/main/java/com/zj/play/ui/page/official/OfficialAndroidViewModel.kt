package com.zj.play.ui.page.official

import android.app.Application
import com.zj.play.logic.repository.BaseArticlePagingRepository
import com.zj.play.logic.viewmodel.BaseAndroidViewModel

/**
 * 版权：Zhujiang 个人版权
 * @author zhujiang
 * 版本：1.5
 * 创建日期：2021/5/17
 * 描述：PlayAndroid
 *
 */
class OfficialAndroidViewModel(application: Application) : BaseAndroidViewModel(application) {

    override val repositoryArticle: BaseArticlePagingRepository
        get() = OfficialRepository(getApplication())

    override suspend fun getData() {
        (repositoryArticle as OfficialRepository).getTree(mutableTreeState)
    }

}
package com.zj.play.article

import android.app.Application
import com.zj.core.Play
import com.zj.core.util.showToast
import com.zj.play.R
import com.zj.play.article.collect.CollectRepositoryPoint
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * 版权：联想 版权所有
 *
 * @author zhujiang
 * 创建日期：2020/11/7
 * 描述：PlayAndroid
 *
 */
class ArticleRepository @Inject constructor(val application: Application) {

    suspend fun setCollect(
        isCollection: Int,
        pageId: Int,
        originId: Int,
        collectListener: (Boolean) -> Unit
    ) {

        if (!Play.isLogin) {
            showToast(application.getString(R.string.not_currently_logged_in))
            return
        }

        if (isCollection == -1 || pageId == -1) {
            showToast(application.getString(R.string.page_is_not_collection))
            return
        }
        val collectRepository = EntryPointAccessors.fromApplication(
            application,
            CollectRepositoryPoint::class.java
        ).collectRepository()
        withContext(Dispatchers.IO) {
            if (isCollection == 1) {
                val cancelCollects =
                    collectRepository.cancelCollects(if (originId != -1) originId else pageId)
                if (cancelCollects.errorCode == 0) {
                    showToast(application.getString(R.string.collection_cancelled_successfully))
                    ArticleBroadCast.sendArticleChangesReceiver(application)
                    collectListener.invoke(false)
                } else {
                    showToast(application.getString(R.string.failed_to_cancel_collection))
                }
            } else {
                val toCollects = collectRepository.toCollects(pageId)
                if (toCollects.errorCode == 0) {
                    showToast(application.getString(R.string.collection_successful))
                    ArticleBroadCast.sendArticleChangesReceiver(application)
                    collectListener.invoke(true)
                } else {
                    showToast(application.getString(R.string.collection_failed))
                }

            }
        }

    }

}
package com.zj.play.article

import android.content.Context
import com.zj.core.Play
import com.zj.core.util.showToast
import com.zj.network.repository.CollectRepository
import com.zj.play.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * 版权：联想 版权所有
 *
 * @author zhujiang
 * 创建日期：2020/11/7
 * 描述：PlayAndroid
 *
 */
object ArticleRepository {

    suspend fun setCollect(
        isCollection: Int,
        pageId: Int,
        originId: Int,
        context: Context,
        collectListener: (Boolean) -> Unit
    ) {

        if (!Play.isLogin) {
            showToast(context.getString(R.string.not_currently_logged_in))
            return
        }

        if (isCollection == -1 || pageId == -1) {
            showToast(context.getString(R.string.page_is_not_collection))
            return
        }

        withContext(Dispatchers.IO) {
            if (isCollection == 1) {
                val cancelCollects =
                    CollectRepository.cancelCollects(if (originId != -1) originId else pageId)
                if (cancelCollects.errorCode == 0) {
                    showToast(context.getString(R.string.collection_cancelled_successfully))
                    ArticleBroadCast.sendArticleChangesReceiver(context)
                    collectListener.invoke(false)
                } else {
                    showToast(context.getString(R.string.failed_to_cancel_collection))
                }
            } else {
                val toCollects = CollectRepository.toCollects(pageId)
                if (toCollects.errorCode == 0) {
                    showToast(context.getString(R.string.collection_successful))
                    ArticleBroadCast.sendArticleChangesReceiver(context)
                    collectListener.invoke(true)
                } else {
                    showToast(context.getString(R.string.collection_failed))
                }

            }
        }

    }


}
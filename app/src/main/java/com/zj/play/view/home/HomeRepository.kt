package com.zj.play.view.home

import com.zj.play.network.PlayAndroidNetwork
import com.zj.play.network.fire
import com.zj.play.room.entity.Article
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

/**
 * 版权：联想 版权所有
 *
 * @author zhujiang
 * 创建日期：2020/9/10
 * 描述：PlayAndroid
 *
 */
object HomeRepository {

    /**
     * 获banner
     */
    fun getBanner() = fire() {
        val bannerResponse = PlayAndroidNetwork.getBanner()
        if (bannerResponse.errorCode == 0) {
            val bannerList = bannerResponse.data
            Result.success(bannerList)
        } else {
            Result.failure(RuntimeException("response status is ${bannerResponse.errorCode}  msg is ${bannerResponse.errorMsg}"))
        }
    }

    /**
     * 首页获取文章列表
     * @param page 页码
     */
    fun getArticleList(page: Int) = fire {
        coroutineScope {
            val articleListDeferred = async { PlayAndroidNetwork.getArticleList(page - 1) }
            val articleList = articleListDeferred.await()
            if (page == 1) {
                val topArticleListDeferred = async { PlayAndroidNetwork.getTopArticleList() }
                val topArticleList = topArticleListDeferred.await()
                if (topArticleList.errorCode == 0 && articleList.errorCode == 0) {
                    val res = arrayListOf<Article>()
                    res.addAll(topArticleList.data)
                    res.addAll(articleList.data.datas)
                    Result.success(res)
                } else {
                    Result.failure(
                        RuntimeException(
                            "response status is ${topArticleList.errorCode}" + "  msg is ${topArticleList.errorMsg}"
                        )
                    )
                }
            } else {
                if (articleList.errorCode == 0) {
                    val res = arrayListOf<Article>()
                    res.addAll(articleList.data.datas)
                    Result.success(res)
                } else {
                    Result.failure(
                        RuntimeException(
                            "response status is ${articleList.errorCode}" + "  msg is ${articleList.errorMsg}"
                        )
                    )
                }
            }
        }
    }

}
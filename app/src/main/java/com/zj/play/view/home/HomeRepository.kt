package com.zj.play.view.home

import android.app.Application
import android.util.Log
import com.blankj.utilcode.util.SPUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target.SIZE_ORIGINAL
import com.zj.core.util.Preference
import com.zj.play.network.PlayAndroidNetwork
import com.zj.play.network.fire
import com.zj.play.room.PlayDatabase
import com.zj.play.room.dao.BannerBeanDao
import com.zj.play.room.entity.Article
import com.zj.play.room.entity.BannerBean
import kotlinx.coroutines.*

/**
 * 版权：联想 版权所有
 *
 * @author zhujiang
 * 创建日期：2020/9/10
 * 描述：PlayAndroid
 *
 */
object HomeRepository {

    private const val ONE_DAY = 1000 * 60 * 60 * 24
    private const val DOWN_IMAGE_TIME = "DownImageTime"

    /**
     * 获取banner
     */
    fun getBanner(application: Application) = fire {
        val spUtils = SPUtils.getInstance()
        val downImageTime by Preference(DOWN_IMAGE_TIME, System.currentTimeMillis())
        val bannerBeanDao = PlayDatabase.getDatabase(application).bannerBeanDao()
        val bannerBeanList = bannerBeanDao.getBannerBeanList()
        if (bannerBeanList.isNotEmpty() && downImageTime > 0 && downImageTime - System.currentTimeMillis() < ONE_DAY) {
            Result.success(bannerBeanList)
        } else {
            val bannerResponse = PlayAndroidNetwork.getBanner()
            if (bannerResponse.errorCode == 0) {
                val bannerList = bannerResponse.data
                spUtils.put(DOWN_IMAGE_TIME, System.currentTimeMillis())
                if (bannerBeanList.isNotEmpty() && bannerBeanList[0].url == bannerList[0].url) {
                    Result.success(bannerBeanList)
                } else {
                    bannerBeanDao.deleteAll()
                    insertBannerList(application, bannerBeanDao, bannerList)
                    Result.success(bannerList)
                }
            } else {
                Result.failure(RuntimeException("response status is ${bannerResponse.errorCode}  msg is ${bannerResponse.errorMsg}"))
            }
        }
    }

    private suspend fun insertBannerList(
        application: Application,
        bannerBeanDao: BannerBeanDao,
        bannerList: List<BannerBean>
    ) {
        bannerList.forEach {
            GlobalScope.launch(Dispatchers.IO) {
                val file = Glide.with(application)
                    .load(it.imagePath)
                    .downloadOnly(SIZE_ORIGINAL, SIZE_ORIGINAL)
                    .get()
                it.filePath = file.absolutePath
                bannerBeanDao.insert(it)
            }
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
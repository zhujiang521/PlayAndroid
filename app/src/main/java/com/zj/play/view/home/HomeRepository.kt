package com.zj.play.view.home

import android.app.Application
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
import com.zj.play.room.entity.HOME
import com.zj.play.room.entity.HOME_TOP
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

    /**
     * 获取banner
     */
    fun getBanner(application: Application, isRefresh: Boolean) = fire {
        val spUtils = SPUtils.getInstance()
        val downImageTime by Preference(DOWN_IMAGE_TIME, System.currentTimeMillis())
        val bannerBeanDao = PlayDatabase.getDatabase(application).bannerBeanDao()
        val bannerBeanList = bannerBeanDao.getBannerBeanList()
        if (bannerBeanList.isNotEmpty() && downImageTime > 0 && downImageTime - System.currentTimeMillis() < ONE_DAY && !isRefresh) {
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
    fun getArticleList(application: Application, query: QueryHomeArticle) = fire {
        coroutineScope {
            val res = arrayListOf<Article>()
            if (query.page == 1) {
                val spUtils = SPUtils.getInstance()
                val downArticleTime by Preference(DOWN_ARTICLE_TIME, System.currentTimeMillis())
                val articleListDao = PlayDatabase.getDatabase(application).browseHistoryDao()
                val articleListHome = articleListDao.getArticleList(HOME)
                val articleListTop = articleListDao.getTopArticleList(HOME_TOP)
                val downTopArticleTime by Preference(
                    DOWN_TOP_ARTICLE_TIME,
                    System.currentTimeMillis()
                )
                if (articleListTop.isNotEmpty() && query.page == 1 && downTopArticleTime > 0 &&
                    downTopArticleTime - System.currentTimeMillis() < FOUR_HOUR && !query.isRefresh
                ) {
                    res.addAll(articleListTop)
                } else {
                    val topArticleListDeferred =
                        async { PlayAndroidNetwork.getTopArticleList() }
                    val topArticleList = topArticleListDeferred.await()
                    if (topArticleList.errorCode == 0) {
                        if (articleListTop.isNotEmpty() && articleListTop[0].link == topArticleList.data[0].link && !query.isRefresh) {
                            res.addAll(articleListTop)
                        } else {
                            res.addAll(topArticleList.data)
                            topArticleList.data.forEach {
                                it.localType = HOME_TOP
                            }
                            spUtils.put(DOWN_TOP_ARTICLE_TIME, System.currentTimeMillis())
                            articleListDao.deleteAll(HOME_TOP)
                            articleListDao.insertList(topArticleList.data)
                        }
                    }
                }
                if (articleListHome.isNotEmpty() && downArticleTime > 0 && downArticleTime - System.currentTimeMillis() < FOUR_HOUR
                    && !query.isRefresh
                ) {
                    res.addAll(articleListHome)
                    Result.success(res)
                } else {
                    val articleListDeferred =
                        async { PlayAndroidNetwork.getArticleList(query.page - 1) }
                    val articleList = articleListDeferred.await()
                    if (articleList.errorCode == 0) {
                        if (articleListHome.isNotEmpty() && articleListHome[0].link == articleList.data.datas[0].link && !query.isRefresh) {
                            res.addAll(articleListHome)
                        } else {
                            res.addAll(articleList.data.datas)
                            articleList.data.datas.forEach {
                                it.localType = HOME
                            }
                            spUtils.put(DOWN_ARTICLE_TIME, System.currentTimeMillis())
                            articleListDao.deleteAll(HOME)
                            articleListDao.insertList(articleList.data.datas)
                        }
                        Result.success(res)
                    } else {
                        Result.failure(
                            RuntimeException(
                                "response status is ${articleList.errorCode}" + "  msg is ${articleList.errorMsg}"
                            )
                        )
                    }
                }
            } else {
                val articleListDeferred =
                    async { PlayAndroidNetwork.getArticleList(query.page - 1) }
                val articleList = articleListDeferred.await()
                if (articleList.errorCode == 0) {
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

const val ONE_DAY = 1000 * 60 * 60 * 24
const val FOUR_HOUR = 1000 * 60 * 60 * 4
const val DOWN_IMAGE_TIME = "DownImageTime"
const val DOWN_TOP_ARTICLE_TIME = "DownTopArticleTime"
const val DOWN_ARTICLE_TIME = "DownArticleTime"
const val DOWN_PROJECT_ARTICLE_TIME = "DownProjectArticleTime"
const val DOWN_OFFICIAL_ARTICLE_TIME = "DownOfficialArticleTime"
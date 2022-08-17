package com.zj.play.home

import android.annotation.SuppressLint
import android.app.Application
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.zj.core.util.DataStoreUtils
import com.zj.core.util.PlayLog
import com.zj.model.pojo.QueryHomeArticle
import com.zj.model.room.PlayDatabase
import com.zj.model.room.dao.BannerBeanDao
import com.zj.model.room.entity.Article
import com.zj.model.room.entity.BannerBean
import com.zj.model.room.entity.HOME
import com.zj.model.room.entity.HOME_TOP
import com.zj.network.base.PlayAndroidNetwork
import com.zj.play.base.liveDataFire
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject


/**
 * 版权：Zhujiang 个人版权
 *
 * @author zhujiang
 * 创建日期：2020/9/10
 * 描述：PlayAndroid
 *
 */
@ActivityRetainedScoped
class HomeRepository @Inject constructor(val application: Application) :
    CoroutineScope by MainScope() {

    private val dataStore = DataStoreUtils.init(application.applicationContext)

    /**
     * 获取banner
     */
    fun getBanner() = liveDataFire {
        val downImageTime = dataStore.readLongData(DOWN_IMAGE_TIME, System.currentTimeMillis())
        val bannerBeanDao = PlayDatabase.getDatabase(application).bannerBeanDao()
        val bannerBeanList = bannerBeanDao.getBannerBeanList()
        if (bannerBeanList.isNotEmpty() && downImageTime > 0 && downImageTime - System.currentTimeMillis() < ONE_DAY) {
            Result.success(bannerBeanList)
        } else {
            val bannerResponse = PlayAndroidNetwork.getBanner()
            if (bannerResponse.errorCode == 0) {
                val bannerList = bannerResponse.data
                dataStore.saveLongData(DOWN_IMAGE_TIME, System.currentTimeMillis())
                if (bannerBeanList.isNotEmpty() && bannerBeanList[0].url == bannerList[0].url) {
                    Result.success(bannerBeanList)
                } else {
                    bannerBeanDao.deleteAll()
                    insertBannerList(this, bannerBeanDao, bannerList)
                    Result.success(bannerList)
                }
            } else {
                Result.failure(RuntimeException("response status is ${bannerResponse.errorCode}  msg is ${bannerResponse.errorMsg}"))
            }
        }
    }

    /**
     * 首页获取文章列表
     * @param query 查询条件
     */
    fun getArticleList(query: QueryHomeArticle) = liveDataFire {
        val res = arrayListOf<Article>()
        if (query.page == 1) {
            buildOnePage(query, res)
        } else {
            val articleList = PlayAndroidNetwork.getArticleList(query.page - 1)
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

    private suspend fun buildOnePage(
        query: QueryHomeArticle,
        res: ArrayList<Article>
    ): Result<ArrayList<Article>> {
        val downArticleTime = dataStore.readLongData(DOWN_ARTICLE_TIME, System.currentTimeMillis())
        val articleListDao = PlayDatabase.getDatabase(application).browseHistoryDao()
        val articleListHome = articleListDao.getArticleList(HOME)
        val articleListTop = articleListDao.getTopArticleList(HOME_TOP)
        val downTopArticleTime =
            dataStore.readLongData(DOWN_TOP_ARTICLE_TIME, System.currentTimeMillis())
        if (articleListTop.isNotEmpty() && downTopArticleTime > 0 &&
            downTopArticleTime - System.currentTimeMillis() < FOUR_HOUR && !query.isRefresh
        ) {
            res.addAll(articleListTop)
        } else {
            val topArticleList = PlayAndroidNetwork.getTopArticleList()
            if (topArticleList.errorCode == 0) {
                if (articleListTop.isNotEmpty() && articleListTop[0].link == topArticleList.data[0].link && !query.isRefresh) {
                    res.addAll(articleListTop)
                } else {
                    res.addAll(topArticleList.data)
                    topArticleList.data.forEach {
                        it.localType = HOME_TOP
                    }
                    dataStore.saveLongData(
                        DOWN_TOP_ARTICLE_TIME,
                        System.currentTimeMillis()
                    )
                    articleListDao.deleteAll(HOME_TOP)
                    articleListDao.insertList(topArticleList.data)
                }
            }
        }
        return if (articleListHome.isNotEmpty() && downArticleTime > 0 && downArticleTime - System.currentTimeMillis() < FOUR_HOUR
            && !query.isRefresh
        ) {
            res.addAll(articleListHome)
            Result.success(res)
        } else {
            val articleList = PlayAndroidNetwork.getArticleList(query.page - 1)
            if (articleList.errorCode == 0) {
                if (articleListHome.isNotEmpty() && articleListHome[0].link == articleList.data.datas[0].link && !query.isRefresh) {
                    res.addAll(articleListHome)
                } else {
                    res.addAll(articleList.data.datas)
                    articleList.data.datas.forEach {
                        it.localType = HOME
                    }
                    dataStore.saveLongData(DOWN_ARTICLE_TIME, System.currentTimeMillis())
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
    }

    companion object {
        /**
         * 保存banner的图片数据
         */
        @SuppressLint("CheckResult")
        private suspend fun insertBannerList(
            homeRepository: HomeRepository, bannerBeanDao: BannerBeanDao,
            bannerList: List<BannerBean>
        ) {
            bannerList.forEach {
                val mRequestManager: RequestManager = Glide.with(homeRepository.application)
                val mRequestBuilder: RequestBuilder<File> = mRequestManager.downloadOnly()
                mRequestBuilder.load(it.imagePath)
                mRequestBuilder.listener(object : RequestListener<File> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<File>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        PlayLog.e("insertBannerList onLoadFailed: ${e?.message}")
                        return false
                    }

                    override fun onResourceReady(
                        resource: File?,
                        model: Any?,
                        target: Target<File>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        try {
                            it.filePath = resource?.absolutePath ?: ""
                            homeRepository.launch {
                                if (it.filePath.isNotEmpty()) {
                                    bannerBeanDao.insert(it)
                                }
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                            PlayLog.e("insertBannerList onResourceReady: ${e.message}")
                        }
                        return false
                    }
                })
                mRequestBuilder.preload()
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
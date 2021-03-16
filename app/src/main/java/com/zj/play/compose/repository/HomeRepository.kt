package com.zj.play.compose.repository

import android.accounts.NetworkErrorException
import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.zj.core.util.DataStoreUtils
import com.zj.model.pojo.QueryHomeArticle
import com.zj.model.room.PlayDatabase
import com.zj.model.room.dao.BannerBeanDao
import com.zj.model.room.entity.Article
import com.zj.model.room.entity.BannerBean
import com.zj.model.room.entity.HOME
import com.zj.model.room.entity.HOME_TOP
import com.zj.network.base.PlayAndroidNetwork
import com.zj.network.down.Download
import com.zj.network.down.DownloadBuild
import com.zj.network.down.DownloadStatus
import com.zj.play.compose.model.PlayError
import com.zj.play.compose.model.PlayLoading
import com.zj.play.compose.model.PlayState
import com.zj.play.compose.model.PlaySuccess
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.*


/**
 * 版权：Zhujiang 个人版权
 *
 * @author zhujiang
 * 创建日期：2020/9/10
 * 描述：PlayAndroid
 *
 */

class HomeRepository constructor(val application: Application) {

    companion object {
        private const val TAG = "HomeRepository"
    }

    /**
     * 获取banner
     */
    suspend fun getBanner(state: MutableLiveData<PlayState>) {
        state.postValue(PlayLoading)
        val dataStore = DataStoreUtils
        var downImageTime = 0L
        dataStore.readLongFlow(DOWN_IMAGE_TIME, System.currentTimeMillis()).first {
            downImageTime = it
            true
        }
        val bannerBeanDao = PlayDatabase.getDatabase(application).bannerBeanDao()
        val bannerBeanList = bannerBeanDao.getBannerBeanList()
        if (bannerBeanList.isNotEmpty() && downImageTime > 0 && downImageTime - System.currentTimeMillis() < ONE_DAY) {
            state.postValue(PlaySuccess(bannerBeanList))
        } else {
            val bannerResponse = PlayAndroidNetwork.getBanner()
            if (bannerResponse.errorCode == 0) {
                val bannerList = bannerResponse.data
                if (bannerBeanList.isNotEmpty() && bannerBeanList[0].url == bannerList[0].url) {
                    state.postValue(PlaySuccess(bannerBeanList))
                } else {
                    bannerBeanDao.deleteAll()
                    insertBannerList(bannerBeanDao, bannerList)
                    state.postValue(PlaySuccess(bannerList))
                }
                dataStore.saveLongData(DOWN_IMAGE_TIME, System.currentTimeMillis())
            } else {
                state.postValue(PlayError(RuntimeException("response status is ${bannerResponse.errorCode}  msg is ${bannerResponse.errorMsg}")))
            }
        }
    }

    private suspend fun insertBannerList(
        bannerBeanDao: BannerBeanDao,
        bannerList: List<BannerBean>
    ) {
        val uiScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
        bannerList.forEach {
            Download.download(it.imagePath, DownloadBuild(application)).collect { value ->
                when (value) {
                    is DownloadStatus.DownloadError -> {
                        //下载错误
                        Log.e(TAG, "emit: error:${value.t}")
                    }
                    is DownloadStatus.DownloadSuccess -> {
                        Log.e(TAG, "insertBannerList: ${value.file?.path}")
                        //下载完成
                        uiScope.launch(Dispatchers.IO) {
                            val banner = bannerBeanDao.loadBanner(it.id)
                            if (banner == null) {
                                it.filePath = value.file?.path ?: ""
                                bannerBeanDao.insert(it)
                            }
                        }
                    }
                    is DownloadStatus.DownloadProcess -> {
                        //下载中
                        //下载进度：it.process
                        //Log.e(TAG, "emit: process${value.process}")
                    }
                }
            }
            Log.e(TAG, "emit: ----------:${it}")
        }
    }


    /**
     * 首页获取文章列表
     * @param query 查询条件
     */
    suspend fun getArticleList(
        state: MutableLiveData<PlayState>,
        value: MutableLiveData<ArrayList<Article>>,
        query: QueryHomeArticle,
        isLoad: Boolean
    ) {
        if (!isLoad) {
            state.postValue(PlayLoading)
        }
        val res: ArrayList<Article>
        if (query.page == 1) {
            res = arrayListOf()
            val dataStore = DataStoreUtils
            var downArticleTime = 0L
            dataStore.readLongFlow(DOWN_ARTICLE_TIME, System.currentTimeMillis()).first {
                downArticleTime = it
                true
            }
            val articleListDao = PlayDatabase.getDatabase(application).browseHistoryDao()
            val articleListHome = articleListDao.getArticleList(HOME)
            val articleListTop = articleListDao.getTopArticleList(HOME_TOP)
            var downTopArticleTime = 0L
            dataStore.readLongFlow(DOWN_TOP_ARTICLE_TIME, System.currentTimeMillis()).first {
                downTopArticleTime = it
                true
            }
            if (articleListTop.isNotEmpty() && downTopArticleTime > 0 &&
                downTopArticleTime - System.currentTimeMillis() < FOUR_HOUR && !query.isRefresh
            ) {
                res.addAll(articleListTop)
                Log.e(TAG, "getArticleList: 111:${res.size}")
            } else {
                val topArticleListDeferred = PlayAndroidNetwork.getTopArticleList()
                if (topArticleListDeferred.errorCode == 0) {
                    if (articleListTop.isNotEmpty() && articleListTop[0].link == topArticleListDeferred.data[0].link && !query.isRefresh) {
                        res.addAll(articleListTop)
                        Log.e(TAG, "getArticleList: 222:${res.size}")
                    } else {
                        res.addAll(topArticleListDeferred.data)
                        Log.e(TAG, "getArticleList: 333:${res.size}")
                        topArticleListDeferred.data.forEach {
                            it.localType = HOME_TOP
                        }
                        dataStore.saveLongData(
                            DOWN_TOP_ARTICLE_TIME,
                            System.currentTimeMillis()
                        )
                        articleListDao.deleteAll(HOME_TOP)
                        articleListDao.insertList(topArticleListDeferred.data)
                    }
                }
            }
            if (articleListHome.isNotEmpty() && downArticleTime > 0 && downArticleTime - System.currentTimeMillis() < FOUR_HOUR
                && !query.isRefresh
            ) {
                res.addAll(articleListHome)
                state.postValue(PlaySuccess<List<Article>>(res))
                value.postValue(res)
                Log.e(TAG, "getArticleList: 444:${res.size}")
            } else {
                val articleListDeferred = PlayAndroidNetwork.getArticleList(query.page - 1)
                if (articleListDeferred.errorCode == 0) {
                    if (articleListHome.isNotEmpty() && articleListHome[0].link == articleListDeferred.data.datas[0].link && !query.isRefresh) {
                        res.addAll(articleListHome)
                    } else {
                        res.addAll(articleListDeferred.data.datas)
                        articleListDeferred.data.datas.forEach {
                            it.localType = HOME
                        }
                        dataStore.saveLongData(DOWN_ARTICLE_TIME, System.currentTimeMillis())
                        articleListDao.deleteAll(HOME)
                        articleListDao.insertList(articleListDeferred.data.datas)
                    }
                    state.postValue(PlaySuccess<List<Article>>(res))
                    value.postValue(res)
                } else {
                    state.postValue(PlayError(NetworkErrorException("网络错误")))
                }
            }
        } else {
            res = value.value ?: arrayListOf()
            val articleListDeferred = PlayAndroidNetwork.getArticleList(query.page - 1)
            if (articleListDeferred.errorCode == 0) {
                res.addAll(articleListDeferred.data.datas)
                state.postValue(PlaySuccess<List<Article>>(res))
                value.postValue(res)
                Log.e(TAG, "getArticleList: 666:${res.size}")
            } else {
                state.postValue(PlayError(NetworkErrorException("网络错误")))
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
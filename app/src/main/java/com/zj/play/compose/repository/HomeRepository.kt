package com.zj.play.compose.repository

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.zj.core.util.DataStoreUtils
import com.zj.model.room.PlayDatabase
import com.zj.model.room.dao.BannerBeanDao
import com.zj.model.room.entity.Article
import com.zj.model.room.entity.BannerBean
import com.zj.model.room.entity.HOME
import com.zj.network.base.PlayAndroidNetwork
import com.zj.network.down.Download
import com.zj.network.down.DownloadBuild
import com.zj.network.down.DownloadStatus
import com.zj.play.compose.mediator.HomeRemoteMediator
import com.zj.play.compose.model.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


/**
 * 版权：Zhujiang 个人版权
 *
 * @author zhujiang
 * 创建日期：2020/9/10
 * 描述：PlayAndroid
 *
 */

class HomePagingRepository(private val application: Application) : BasePagingRepository() {

    companion object {
        private const val TAG = "ArticlePagingRepository"
    }

    @ExperimentalPagingApi
    override fun getPagingData(query: Query): Flow<PagingData<Article>> {
        val database = PlayDatabase.getDatabase(application)
        return Pager(
            config = PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = false),
            remoteMediator = HomeRemoteMediator(database),
            pagingSourceFactory = {
                database.browseHistoryDao().articleByType(HOME)
            }
        ).flow
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
            Log.e(TAG, "getBanner: 000:$bannerBeanList")
        } else {
            val bannerResponse = PlayAndroidNetwork.getBanner()
            if (bannerResponse.errorCode == 0) {
                val bannerList = bannerResponse.data
                if (bannerBeanList.isNotEmpty() && bannerBeanList[0].url == bannerList[0].url) {
                    state.postValue(PlaySuccess(bannerBeanList))
                } else {
                    bannerBeanDao.deleteAll()
                    bannerList.forEach {
                        it.data = it.imagePath
                    }
                    state.postValue(PlaySuccess(bannerList))
                    insertBannerList(bannerBeanDao, bannerList)
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
                                it.data = value.file?.path ?: ""
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

}

const val ONE_DAY = 1000 * 60 * 60 * 24
const val FOUR_HOUR = 1000 * 60 * 60 * 4
const val DOWN_IMAGE_TIME = "DownImageTime"
const val DOWN_TOP_ARTICLE_TIME = "DownTopArticleTime"
const val DOWN_ARTICLE_TIME = "DownArticleTime"
const val DOWN_PROJECT_ARTICLE_TIME = "DownProjectArticleTime"
const val DOWN_OFFICIAL_ARTICLE_TIME = "DownOfficialArticleTime"
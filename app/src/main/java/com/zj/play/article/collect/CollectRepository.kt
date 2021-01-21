package com.zj.play.article.collect

import com.zj.network.base.PlayAndroidNetwork
import com.zj.play.main.login.fires
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 版权：Zhujiang 个人版权
 * @author zhujiang
 * 版本：1.5
 * 创建日期：2020/5/19
 * 描述：PlayAndroid
 *
 */
@Singleton
class CollectRepository @Inject constructor() {

    /**
     * 获取收藏列表
     *
     * @param page 页码
     */
    fun getCollectList(page: Int) = fires { PlayAndroidNetwork.getCollectList(page) }

    suspend fun cancelCollects(id: Int) = PlayAndroidNetwork.cancelCollect(id)
    suspend fun toCollects(id: Int) = PlayAndroidNetwork.toCollect(id)

}

@EntryPoint
@InstallIn(SingletonComponent::class)
interface CollectRepositoryPoint {
    fun collectRepository(): CollectRepository
}
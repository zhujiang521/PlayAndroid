package com.zj.play.profile.share

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.zj.model.room.entity.Article
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * 版权：Zhujiang 个人版权
 * @author zhujiang
 * 版本：1.5
 * 创建日期：2020/5/19
 * 描述：PlayAndroid
 *
 */
@HiltViewModel
class ShareViewModel @Inject constructor(private val shareRepository: ShareRepository) :
    ViewModel() {

    val articleList = ArrayList<Article>()

    private val pageLiveData = MutableLiveData<Int>()

    private val pageAndCidLiveData = MutableLiveData<ShareArticle>()

    val articleLiveData = pageLiveData.switchMap { page ->
        shareRepository.getMyShareList(page)
    }

    val articleAndCidLiveData = pageAndCidLiveData.switchMap { page ->
        shareRepository.getShareList(page.cid, page.page)
    }

    fun getArticleList(page: Int) {
        pageLiveData.value = page
    }

    fun getArticleList(cid: Int, page: Int) {
        pageAndCidLiveData.value = ShareArticle(cid, page)
    }


}

data class ShareArticle(var cid: Int, var page: Int)
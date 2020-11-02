package com.zj.play.profile.share

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.zj.network.repository.ShareRepository
import com.zj.model.room.entity.Article

/**
 * 版权：渤海新能 版权所有
 * @author zhujiang
 * 版本：1.5
 * 创建日期：2020/5/19
 * 描述：PlayAndroid
 *
 */
class ShareViewModel : ViewModel() {

    val articleList = ArrayList<Article>()

    private val pageLiveData = MutableLiveData<Int>()

    private val pageAndCidLiveData = MutableLiveData<ShareArticle>()

    val articleLiveData = Transformations.switchMap(pageLiveData) { page ->
        ShareRepository.getMyShareList(page)
    }

    val articleAndCidLiveData = Transformations.switchMap(pageAndCidLiveData) { page ->
        ShareRepository.getShareList(page.cid, page.page)
    }

    fun getArticleList(page: Int) {
        pageLiveData.value = page
    }

    fun getArticleList(cid: Int, page: Int) {
        pageAndCidLiveData.value = ShareArticle(cid, page)
    }


}

data class ShareArticle(var cid: Int, var page: Int)
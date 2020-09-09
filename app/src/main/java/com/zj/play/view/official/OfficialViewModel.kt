package com.zj.play.view.official

import androidx.lifecycle.*
import com.zj.play.model.BannerBean
import com.zj.play.model.Article
import com.zj.play.network.Repository

/**
 * 版权：渤海新能 版权所有
 * @author zhujiang
 * 版本：1.5
 * 创建日期：2020/5/17
 * 描述：PlayAndroid
 *
 */
class OfficialViewModel : ViewModel() {

    val officialTreeLiveData = Repository.getOfficialTree()

}
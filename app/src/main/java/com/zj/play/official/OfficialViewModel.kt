package com.zj.play.official

import android.app.Application
import androidx.lifecycle.LiveData
import com.zj.core.view.BaseAndroidViewModel
import com.zj.model.room.entity.ProjectClassify

/**
 * 版权：渤海新能 版权所有
 * @author zhujiang
 * 版本：1.5
 * 创建日期：2020/5/17
 * 描述：PlayAndroid
 *
 */
class OfficialViewModel(application: Application) :
    BaseAndroidViewModel<List<ProjectClassify>, Unit, Boolean>(application) {

    var position = 0

    override fun getData(page: Boolean): LiveData<Result<List<ProjectClassify>>> {
        return OfficialRepository(getApplication()).getWxArticleTree(page)
    }
}
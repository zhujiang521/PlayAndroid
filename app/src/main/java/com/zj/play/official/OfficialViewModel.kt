package com.zj.play.official

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import com.zj.core.view.base.BaseAndroidViewModel
import com.zj.model.room.entity.ProjectClassify
import dagger.hilt.android.scopes.ActivityScoped

/**
 * 版权：Zhujiang 个人版权
 * @author zhujiang
 * 版本：1.5
 * 创建日期：2020/5/17
 * 描述：PlayAndroid
 *
 */
@ActivityScoped
class OfficialViewModel @ViewModelInject constructor(
    private val officialRepository: OfficialRepository
) : BaseAndroidViewModel<List<ProjectClassify>, Unit, Boolean>() {

    var position = 0

    override fun getData(page: Boolean): LiveData<Result<List<ProjectClassify>>> {
        return officialRepository.getWxArticleTree(page)
    }
}
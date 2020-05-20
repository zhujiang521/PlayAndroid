package com.zj.play.view.official

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.zj.play.model.ProjectTree
import com.zj.play.network.Repository

/**
 * 版权：渤海新能 版权所有
 * @author zhujiang
 * 版本：1.5
 * 创建日期：2020/5/19
 * 描述：PlayAndroid
 *
 */
class OfficialAccountsViewModel : ViewModel() {
    fun getOfficialTree(): LiveData<Result<List<ProjectTree>>> {
        return Repository.getOfficialTree()
    }
}
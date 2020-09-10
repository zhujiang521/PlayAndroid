package com.zj.play.view.project

import android.content.Context
import androidx.lifecycle.ViewModel
import com.zj.play.room.PlayDatabase

/**
 * 版权：渤海新能 版权所有
 * @author zhujiang
 * 版本：1.5
 * 创建日期：2020/5/17
 * 描述：PlayAndroid
 *
 */
class ProjectViewModel(context: Context) : ViewModel() {

    val projectTreeLiveData =
        ProjectRepository(PlayDatabase.getDatabase(context).projectClassifyDao()).getProjectTree()

}
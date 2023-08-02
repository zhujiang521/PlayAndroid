package com.zj.play

import android.app.Application
import com.zj.utils.DataStoreUtils
import dagger.hilt.android.HiltAndroidApp

/**
 * Application
 *
 * @author jiang zhu on 2019/10/21
 */
@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        DataStoreUtils.init(applicationContext)
    }

}
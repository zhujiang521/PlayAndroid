package com.zj.play

import android.app.Application
import com.zj.play.logic.utils.DataStoreUtils


/**
 * Application
 *
 * @author jiang zhu on 2019/10/21
 */
class App : Application() {

    private var dataStore = DataStoreUtils

    override fun onCreate() {
        super.onCreate()
        DataStoreUtils.init(applicationContext)
        Play.initialize(dataStore)
    }

}
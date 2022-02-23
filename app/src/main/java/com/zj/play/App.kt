package com.zj.play

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.zj.play.logic.utils.DataStoreUtils
import dagger.hilt.android.HiltAndroidApp


/**
 * Application
 *
 * @author jiang zhu on 2019/10/21
 */
@HiltAndroidApp
class App : Application() {

    companion object {
        @SuppressLint("StaticFieldLeak")
        var context: Context? = null
    }

    private var dataStore = DataStoreUtils

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        DataStoreUtils.init(applicationContext)
        Play.initialize(dataStore)
    }

}
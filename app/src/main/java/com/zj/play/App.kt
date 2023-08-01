package com.zj.play

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.zj.utils.DataStoreUtils
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

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        DataStoreUtils.init(applicationContext)
    }

}
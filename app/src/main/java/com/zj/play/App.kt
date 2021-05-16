package com.zj.play

import android.app.Application


/**
 * Application
 *
 * @author jiang zhu on 2019/10/21
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        Play.initialize(applicationContext)
    }

}
package com.zj.play.compose

import android.app.Application
import android.util.Log
import com.tencent.bugly.crashreport.CrashReport
import com.tencent.smtt.sdk.QbSdk
import com.zj.core.Play
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch


/**
 * Application
 *
 * @author jiang zhu on 2019/10/21
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        //instances = this
        Play.initialize(applicationContext)
        initData()
    }

    private fun initData() {
        CoroutineScope(Dispatchers.IO + SupervisorJob()).launch {
            initQbSdk()
            initBugLy()
        }
    }

    private fun initBugLy() {
        // Bugly bug上报
        CrashReport.initCrashReport(applicationContext, "0f4f8e06b4", false)
    }

    private fun initQbSdk() {
        // x5内核初始化接口
        QbSdk.initX5Environment(applicationContext, object : QbSdk.PreInitCallback {
            override fun onViewInitFinished(arg0: Boolean) { //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                Log.e("APP", " onViewInitFinished is $arg0")
            }

            override fun onCoreInitFinished() {
                Log.e("APP", " onCoreInitFinished")
            }
        })
    }

//    companion object {
//        @SuppressLint("StaticFieldLeak")
//        private var instances: App? = null
//
//        fun getInstance(): App {
//            if (instances == null) {
//                synchronized(App::class.java) {
//                    if (instances == null) {
//                        instances = App()
//                    }
//                }
//            }
//            return instances!!
//        }
//    }
}
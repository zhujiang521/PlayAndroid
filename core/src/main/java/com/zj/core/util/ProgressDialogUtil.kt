package com.zj.core.util

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context

import java.lang.ref.WeakReference

/**
 * Created by wzf on 2017/11/28.
 */
class ProgressDialogUtil {

    @Synchronized
    fun progressDialogShow(title: String, msg: String) {

        if (progressDialog != null && progressDialog!!.isShowing) {
            if ((mContext!!.get() as Activity).isFinishing) {
                progressDialog!!.dismiss()
            }
            return
        }
        if (mContext == null || mContext!!.get() !is Activity) {
            return
        }
        progressDialog = ProgressDialog(mContext!!.get())
        progressDialog!!.setTitle(title)
        progressDialog!!.setMessage(msg)
        progressDialog!!.setCanceledOnTouchOutside(false)
        if (progressDialog != null && !(mContext!!.get() as Activity).isFinishing && !progressDialog!!.isShowing) {
            progressDialog!!.show()
        }

    }

    @Synchronized
    fun progressDialogShow(msg: String) {
        progressDialogShow("", msg)
    }

    @Synchronized
    fun progressDialogShow() {
        progressDialogShow("", "正在上传...")
    }

    @Synchronized
    fun progressDialogDismiss() {

        if (progressDialog != null && progressDialog!!.isShowing) {
            progressDialog!!.dismiss()
        }
        progressDialog = null


    }

    companion object {
        private var mProgressDialogUtil: ProgressDialogUtil? = null
        @Volatile
        private var progressDialog: ProgressDialog? = null
        private var mContext: WeakReference<Context>? = null

        @Synchronized
        fun getInstance(contexts: Context): ProgressDialogUtil? {
            mContext = WeakReference(contexts)
            if (mProgressDialogUtil == null) {
                synchronized(ProgressDialogUtil::class.java) {
                    if (mProgressDialogUtil == null) {
                        mProgressDialogUtil = ProgressDialogUtil()
                    }
                }
            }
            return mProgressDialogUtil
        }
    }
}

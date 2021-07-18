package com.zj.core.util

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import com.zj.core.databinding.DialogProgressBinding
import java.lang.ref.WeakReference

/**
 * ProgressDialog Utils
 */
class ProgressDialogUtil {

    @Synchronized
    fun progressDialogShow(msg: String) {
        if (progressDialog != null && progressDialog!!.isShowing) {
            if ((mContext!!.get() as Activity).isFinishing) {
                progressDialog!!.dismiss()
            }
            return
        }
        if (mContext == null || mContext!!.get() !is Activity) {
            return
        }
        progressDialog = Dialog(mContext!!.get()!!)
        val progressView = DialogProgressBinding.inflate(LayoutInflater.from(mContext!!.get()!!))
        progressView.apply {
            dialogMessage.text = msg
        }
        progressDialog?.apply {
            setContentView(progressView.root)
            setCanceledOnTouchOutside(false)
            if (!(mContext!!.get() as Activity).isFinishing && !progressDialog!!.isShowing) {
                show()
            }
        }
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
        private var progressDialog: Dialog? = null
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

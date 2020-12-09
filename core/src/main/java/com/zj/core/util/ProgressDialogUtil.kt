package com.zj.core.util

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.view.View
import com.zj.core.R
import kotlinx.android.synthetic.main.dialog_progress.view.*
import java.lang.ref.WeakReference

/**
 * Created by wzf on 2017/11/28.
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
        val view = View.inflate(mContext!!.get()!!, R.layout.dialog_progress, null)
        view.apply {
            dialogMessage.text = msg
        }
        progressDialog!!.setContentView(view)
        progressDialog!!.setCanceledOnTouchOutside(false)
        if (progressDialog != null && !(mContext!!.get() as Activity).isFinishing && !progressDialog!!.isShowing) {
            progressDialog!!.show()
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

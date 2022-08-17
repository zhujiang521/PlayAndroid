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
        val context = mContext?.get() ?: return
        if (context !is Activity) {
            PlayLog.w(TAG, "progressDialogShow: no activity")
            return
        }
        if (progressDialog != null && progressDialog?.isShowing != false) {
            if (context.isFinishing) {
                progressDialog?.dismiss()
            }
            return
        }

        progressDialog = Dialog(context)
        val progressView = DialogProgressBinding.inflate(LayoutInflater.from(context))
        progressView.apply {
            dialogMessage.text = msg
        }
        progressDialog?.apply {
            setContentView(progressView.root)
            setCanceledOnTouchOutside(false)
            if (!(mContext?.get() as Activity).isFinishing && progressDialog?.isShowing == false) {
                show()
            }
        }
    }

    @Synchronized
    fun progressDialogDismiss() {
        if (progressDialog != null && progressDialog?.isShowing != false) {
            progressDialog?.dismiss()
        }
        progressDialog = null
    }

    companion object {
        private const val TAG = "ProgressDialogUtil"
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

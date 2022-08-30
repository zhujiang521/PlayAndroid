package com.zj.core.util

import android.content.Context
import android.widget.Toast


private var toast: Toast? = null

fun Context?.showToast(
    content: String?
) {
      if (Thread.currentThread().name != "main") return
    if (this == null) return
    if (toast == null) {
        toast = Toast.makeText(
            this,
            content,
            Toast.LENGTH_SHORT
        )
    } else {
        toast?.setText(content)
    }
    toast?.show()
}

fun Context?.showToast(resId: Int) {
    if (this == null) return
    if (toast == null) {
        toast = Toast.makeText(
            this,
            resId,
            Toast.LENGTH_SHORT
        )
    } else {
        toast?.setText(resId)
    }
    toast?.show()
}

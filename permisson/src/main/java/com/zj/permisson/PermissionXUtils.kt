package com.zj.permisson

import android.os.Build
import android.util.Log
import androidx.fragment.app.FragmentActivity
import com.zj.core.util.showLongToast
import java.lang.StringBuilder

/**
 * 版权：渤海新能 版权所有
 * @author zhujiang
 * 版本：1.5
 * 创建日期：2020/4/8
 * 描述：权限申请工具类
 *
 */
object PermissionXUtils {

    fun getPermission(
        activity: FragmentActivity,
        callback: PermissionXUtilsCallback,
        vararg permissions: String
    ) {
        if (Build.VERSION.SDK_INT >= 23) {
            PermissionX.init(activity)
                .permissions(
                    *permissions
                )
                .onExplainRequestReason { deniedList ->
                    showRequestReasonDialog(
                        deniedList,
                        "${activity.resources.getString(R.string.activity_permission)}\n${
                            getDeniedList(
                            deniedList
                        )
                        }",
                        activity.resources.getString(R.string.activity_permission_sure),
                        activity.resources.getString(R.string.activity_permission_cancel)
                    )
                }
                .onForwardToSettings { deniedList ->
                    showForwardToSettingsDialog(
                        deniedList,
                        "${activity.resources.getString(R.string.activity_permission_setting)}\n${
                            getDeniedList(
                            deniedList
                        )
                        }",
                        activity.resources.getString(R.string.activity_permission_sure),
                        activity.resources.getString(R.string.activity_permission_cancel)
                    )
                }
                .request { allGranted, _, deniedList ->
                    if (allGranted) {
                        callback.permissionCallback()
                    } else {
                        showLongToast("已拒绝：${getDeniedList(deniedList)}")
                        Log.e("拒绝权限: ", "拒绝 $deniedList")
                    }
                }
        } else {
            callback.permissionCallback()
        }
    }

    private fun getDeniedList(deniedList: List<String>): String {
        var index = 1
        val result = StringBuilder()
        deniedList.forEach {
            if (it.endsWith("RECORD_AUDIO")) {
                result.append("\n ${index++}、麦克风权限")
            } else if (it.endsWith("CAMERA")) {
                result.append("\n ${index++}、相机权限")
            } else if (it.endsWith("STORAGE") && !result.contains("存储权限")) {
                result.append("\n ${index++}、存储权限")
            } else if (it.endsWith("LOCATION") && !result.contains("位置权限")) {
                result.append("\n ${index++}、位置权限（GPS开关）")
            }
        }
        return result.toString()
    }

}

interface PermissionXUtilsCallback {

    fun permissionCallback()

}
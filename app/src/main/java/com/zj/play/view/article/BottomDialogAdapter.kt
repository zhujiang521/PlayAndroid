package com.zj.play.view.article

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.zhy.adapter.recyclerview.CommonAdapter
import com.zhy.adapter.recyclerview.base.ViewHolder
import com.zj.core.Play
import com.zj.core.util.showToast
import com.zj.play.R
import com.zj.play.network.CollectRepository
import com.zj.play.view.profile.ProfileItem
import java.util.*


class BottomDialogAdapter(
    context: Context,
    layoutId: Int,
    rows: ArrayList<ProfileItem>,
    private val bottomSheetDialog: BottomSheetDialog
) :
    CommonAdapter<ProfileItem>(context, layoutId, rows) {

    override fun convert(holder: ViewHolder, t: ProfileItem, position: Int) {
        val bottomDialogLl = holder.getView<LinearLayout>(R.id.bottomDialogLl)
        val bottomDialogIv = holder.getView<ImageView>(R.id.bottomDialogIv)
        val bottomDialogTv = holder.getView<TextView>(R.id.bottomDialogTv)
        bottomDialogIv.setImageResource(t.imgId)
        bottomDialogTv.text = t.title
        bottomDialogLl.setOnClickListener {
            bottomSheetDialog.dismiss()
            setAction(t.title)
        }
        mDatas[position].title
    }

    private fun setAction(title: String) {
        when (title) {
            "收藏" -> {
                setCollect(true, 1)
            }
            "取消收藏" -> {
                setCollect(false, 2)
            }
            "复制链接" -> {
                copyToClipboard("")
            }
            "浏览器打开" -> {

            }
            "三方分享" -> {

            }
            "刷新" -> {

            }
        }
    }

    private fun setCollect(collect: Boolean, id: Int) {
        if (Play.isLogin()) {
            if (collect) {
                CollectRepository.toCollect(id)
            } else {
                CollectRepository.cancelCollect(id)
            }
        } else {
            showToast("当前未登录")
        }
    }

    private fun copyToClipboard(text: String?) {
        val systemService: ClipboardManager =
            mContext.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        systemService.setPrimaryClip(ClipData.newPlainText("text", text))
    }

}

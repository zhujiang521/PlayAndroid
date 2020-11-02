package com.zj.play.profile.share.add

import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.view.View
import com.blankj.utilcode.util.RegexUtils
import com.zj.core.util.showToast
import com.zj.core.view.BaseActivity
import com.zj.play.R
import com.zj.network.repository.ShareRepository
import kotlinx.android.synthetic.main.activity_add_share.*

class AddShareActivity : BaseActivity(), View.OnClickListener {


    override fun getLayoutId(): Int = R.layout.activity_add_share

    override fun initView() {
        addShareBtnAdd.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.addShareBtnAdd -> {
                addShare()
            }
        }
    }

    private fun addShare() {
        val title = addShareEtTitle.text.toString().trim()
        if (TextUtils.isEmpty(title) || title == "") {
            addShareEtTitle.error = getString(R.string.title_cannot_empty)
            return
        }
        val link = addShareEtLink.text.toString().trim()
        if (TextUtils.isEmpty(link) || link == "") {
            addShareEtLink.error = getString(R.string.link_cannot_empty)
            return
        }
        if (!RegexUtils.isURL(link)) {
            addShareEtLink.error = getString(R.string.link_format_error)
            return
        }
        ShareRepository.shareArticle(title, link).observe(this, {
            if (it.isSuccess) {
                showToast(getString(R.string.share_success))
                finish()
            }
        })
    }

    companion object {
        fun actionStart(context: Context) {
            val intent = Intent(context, AddShareActivity::class.java)
            context.startActivity(intent)
        }
    }

}
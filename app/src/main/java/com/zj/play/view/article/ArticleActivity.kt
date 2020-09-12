package com.zj.play.view.article

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.PixelFormat
import android.text.Html
import android.view.KeyEvent
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.zj.core.view.BaseActivity
import com.zj.play.R
import com.zj.play.view.profile.ProfileItem
import kotlinx.android.synthetic.main.activity_article.*


const val PAGE_NAME = "PAGE_NAME"
const val PAGE_URL = "PAGE_URL"

class ArticleActivity : BaseActivity(), View.OnClickListener {

    override fun getLayoutId(): Int {
        return R.layout.activity_article
    }

    private var pageName = ""
    private var pageUrl = ""

    override fun initData() {
        pageName = intent.getStringExtra(PAGE_NAME) ?: ""
        pageUrl = intent.getStringExtra(PAGE_URL) ?: ""
        articleTxtTitle.text = Html.fromHtml(pageName)
        articleWebView.loadUrl(pageUrl)
    }

    override fun initView() {
        window.setFormat(PixelFormat.TRANSLUCENT)
        articleImgBack.setOnClickListener(this)
        articleImgRight.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.articleImgBack -> {
                if (articleWebView.canGoBack()) {
                    //返回上个页面
                    articleWebView.goBack()
                    return
                } else {
                    finish()
                }
            }
            R.id.articleImgRight -> {
                setBottomDialog()
            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && articleWebView.canGoBack()) {
            articleWebView.goBack() //返回上个页面
            return true
        }
        return super.onKeyDown(keyCode, event) //退出H5界面
    }

    private fun setBottomDialog() {
        val profileItemList = ArrayList<ProfileItem>()
        profileItemList.add(ProfileItem("收藏", R.drawable.ic_favorite_black_24dp))
        profileItemList.add(ProfileItem("复制链接", R.drawable.ic_collections_black_24dp))
        profileItemList.add(ProfileItem("浏览器打开", R.drawable.ic_account_blog_black_24dp))
        profileItemList.add(ProfileItem("三方分享", R.drawable.ic_bug_report_black_24dp))
        profileItemList.add(ProfileItem("刷新", R.drawable.ic_github_black_24dp))
        val bottomSheetDialog = BottomSheetDialog(this)
        val dialogView: View = bottomSheetDialog.layoutInflater.inflate(
            R.layout.dialog_bottom_sheet,
            null
        )
        val bottomDialogRv = dialogView.findViewById<RecyclerView>(R.id.bottomDialogRv)
        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            bottomDialogRv.layoutManager = GridLayoutManager(this, 4)
        }else{
            bottomDialogRv.layoutManager = GridLayoutManager(this, 5)
        }
        bottomDialogRv.adapter =
            BottomDialogAdapter(
                this,
                R.layout.dialog_bottom_item,
                profileItemList,
                bottomSheetDialog
            )
        bottomSheetDialog.setContentView(dialogView) //给布局设置透明背景色
        ((dialogView.parent) as View).setBackgroundColor(Color.TRANSPARENT)
        bottomSheetDialog.show()
    }

    companion object {
        fun actionStart(context: Context, pageName: String, pageUrl: String) {
            val intent = Intent(context, ArticleActivity::class.java).apply {
                putExtra(PAGE_NAME, pageName)
                putExtra(PAGE_URL, pageUrl)
            }
            context.startActivity(intent)
        }
    }

}

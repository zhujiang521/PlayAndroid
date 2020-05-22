package com.zj.play.view.article

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.PixelFormat
import android.text.Html
import android.view.KeyEvent
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.tencent.smtt.sdk.WebSettings
import com.tencent.smtt.sdk.WebViewClient
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
        //initWebView()
    }

    private fun initWebView() {
        //防止用浏览器打开网页
        articleWebView.webViewClient = WebViewClient()
        val webSettings: WebSettings = articleWebView.settings
        //支持缩放，默认为true。
        webSettings.setSupportZoom(false)
        //调整图片至适合webview的大小
        webSettings.useWideViewPort = true
        // 缩放至屏幕的大小
        webSettings.loadWithOverviewMode = true
        //设置默认编码
        webSettings.defaultTextEncodingName = "utf-8"
        //设置自动加载图片
        webSettings.loadsImagesAutomatically = true
        //多窗口
        webSettings.supportMultipleWindows();
        //获取触摸焦点
        articleWebView.requestFocusFromTouch();
        //允许访问文件
        webSettings.allowFileAccess = true;
        //开启javascript
        webSettings.javaScriptEnabled = true;
        //支持通过JS打开新窗口
        webSettings.javaScriptCanOpenWindowsAutomatically = true;
        //提高渲染的优先级
        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        //支持内容重新布局
        webSettings.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN;
        //关闭webview中缓存
        webSettings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK;
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
        bottomDialogRv.layoutManager = GridLayoutManager(this, 4)
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

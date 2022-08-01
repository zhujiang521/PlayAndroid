package com.zj.play.article

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.webkit.*
import android.webkit.WebSettings.FORCE_DARK_OFF
import android.widget.ProgressBar
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import com.zj.play.R

/**
 * [setShowProgress]
 */
class X5WebView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : WebView(context, attrs, defStyleAttr) {

    private var progressBar: ProgressBar? = null

    private fun setShowProgress(showProgress: Boolean) {
        progressBar?.isVisible = showProgress
    }

    init {
        isHorizontalScrollBarEnabled = false //水平不显示小方块
        isVerticalScrollBarEnabled = false //垂直不显示小方块

        progressBar = ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal)
        progressBar?.max = 100
        progressBar?.progressDrawable =
            ResourcesCompat.getDrawable(resources, R.drawable.color_progressbar, null)
        addView(progressBar, LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 6))
        initWebViewSettings()
    }

    //   基本的WebViewSetting
    @SuppressLint("ClickableViewAccessibility", "SetJavaScriptEnabled")
    private fun initWebViewSettings() {
        setBackgroundColor(resources.getColor(R.color.article_share_bg, null))
        webViewClient = client
        webChromeClient = chromeClient
        setDownloadListener(downloadListener)
        isClickable = true
        setOnTouchListener { _: View?, _: MotionEvent? -> false }
        val webSetting = settings
        webSetting.builtInZoomControls = true
        webSetting.javaScriptCanOpenWindowsAutomatically = true
        webSetting.domStorageEnabled = true
        webSetting.allowFileAccess = true
        webSetting.setSupportZoom(true)
        webSetting.useWideViewPort = true
        webSetting.setSupportMultipleWindows(true)
        webSetting.setAppCacheEnabled(true)
        webSetting.setGeolocationEnabled(true)

        if (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES) { //判断如果系统是深色主题
            webSetting.forceDark = WebSettings.FORCE_DARK_ON //强制开启webview深色主题模式
        } else {
            webSetting.forceDark = FORCE_DARK_OFF
        }

        //需要支持多窗体还需要重写WebChromeClient.onCreateWindow
        webSetting.setSupportMultipleWindows(false)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && canGoBack()) {
            goBack() // goBack()表示返回WebView的上一页面
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    private val chromeClient: WebChromeClient = object : WebChromeClient() {
        override fun onReceivedTitle(view: WebView, title: String) {
            if (TextUtils.isEmpty(title)) {
                return
            }
        }

        //监听进度
        override fun onProgressChanged(view: WebView, newProgress: Int) {
            progressBar?.progress = newProgress
            if (progressBar != null && newProgress != 100) {
                //WebView加载没有完成 就显示我们自定义的加载图
                progressBar?.visibility = VISIBLE
            } else if (progressBar != null) {
                //WebView加载完成 就隐藏进度条,显示WebView
                progressBar?.visibility = GONE
            }
        }
    }
    private val client: WebViewClient = object : WebViewClient() {
        //当页面加载完成的时候
        override fun onPageFinished(webView: WebView, url: String) {
            val cookieManager = CookieManager.getInstance()
            cookieManager.setAcceptCookie(true)
            val endCookie = cookieManager.getCookie(url)
            Log.i("TAG", "onPageFinished: endCookie : $endCookie")
            CookieManager.getInstance().flush()
            super.onPageFinished(webView, url)
        }

        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?
        ): Boolean {
            return true
        }

    }

    private val downloadListener =
        DownloadListener { url: String?, _: String?, _: String?, _: String?, _: Long ->
            val uri = Uri.parse(url)
            val intent = Intent(Intent.ACTION_VIEW, uri)
            context.startActivity(intent)
        }
}
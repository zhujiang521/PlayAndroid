package com.zj.play.article

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.view.isVisible
import com.tencent.smtt.sdk.*
import com.zj.core.util.showToast
import com.zj.play.R
import java.util.*

class X5WebView @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : WebView(context, attrs, defStyleAttr) {

    private var progressBar: ProgressBar? = null

    fun setShowProgress(showProgress: Boolean) {
        progressBar?.isVisible = showProgress
    }

    init {
        onCreate()
    }

    fun onCreate() {
        //        x5WebViewExtension.setScrollBarFadingEnabled(false);
        isHorizontalScrollBarEnabled = false //水平不显示小方块
        isVerticalScrollBarEnabled = false //垂直不显示小方块

//      setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);//滚动条在WebView内侧显示
//      setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);//滚动条在WebView外侧显示
        progressBar = ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal)
        progressBar?.max = 100
        progressBar?.progressDrawable = this.resources.getDrawable(R.drawable.color_progressbar)
        addView(progressBar, LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 6))
//        imageView = new ImageView (getContext());
//        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//        //      加载图 根据自己的需求去集成使用
//        imageView.setImageResource(R.mipmap.splash);
//        imageView.setVisibility(VISIBLE);
//        addView(
//            imageView,
//            new LayoutParams (ViewGroup.LayoutParams.MATCH_PARENT,
//            ViewGroup.LayoutParams.MATCH_PARENT
//        ))
        initWebViewSettings()
    }

    fun onStart() {

    }

    fun onStop() {

    }

    //   基本的WebViewSetting
    private fun initWebViewSettings() {
        setBackgroundColor(resources.getColor(android.R.color.white))
        webViewClient = client
        webChromeClient = chromeClient
        setDownloadListener(downloadListener)
        isClickable = true
        setOnTouchListener { _: View?, _: MotionEvent? -> false }
        val webSetting = settings
        webSetting.javaScriptEnabled = true
        webSetting.builtInZoomControls = true
        webSetting.javaScriptCanOpenWindowsAutomatically = true
        webSetting.domStorageEnabled = true
        webSetting.allowFileAccess = true
        webSetting.layoutAlgorithm = WebSettings.LayoutAlgorithm.NARROW_COLUMNS
        webSetting.setSupportZoom(true)
        webSetting.useWideViewPort = true
        webSetting.setSupportMultipleWindows(true)
        webSetting.setAppCacheEnabled(true)
        webSetting.setGeolocationEnabled(true)
        webSetting.setAppCacheMaxSize(Long.MAX_VALUE)
        webSetting.pluginState = WebSettings.PluginState.ON_DEMAND
        webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH)
        //android 默认是可以打开_bank的，是因为它默认设置了WebSettings.setSupportMultipleWindows(false)
        //在false状态下，_bank也会在当前页面打开……
        //而x5浏览器，默认开启了WebSettings.setSupportMultipleWindows(true)，
        // 所以打不开……主动设置成false就可以打开了
        //需要支持多窗体还需要重写WebChromeClient.onCreateWindow
        webSetting.setSupportMultipleWindows(false)
//        webSetting.cacheMode = WebSettings.LOAD_NORMAL;
//        settingsExtension.setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);//extension
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
            progressBar!!.progress = newProgress
            if (progressBar != null && newProgress != 100) {
                //WebView加载没有完成 就显示我们自定义的加载图
                progressBar!!.visibility = VISIBLE
            } else if (progressBar != null) {

                //WebView加载完成 就隐藏进度条,显示WebView
                progressBar?.visibility = GONE
                //imageView.setVisibility(GONE);
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

        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            //返回值是true的时候控制去WebView打开，
            // 为false调用系统浏览器或第三方浏览器
            return if (url.startsWith("http") || url.startsWith("https") || url.startsWith("ftp")) {
                false
            } else {
                try {
                    val intent = Intent()
                    intent.action = Intent.ACTION_VIEW
                    intent.data = Uri.parse(url)
                    view.context.startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    showToast(R.string.no_app)
                }
                true
            }
        }

        override fun onLoadResource(webView: WebView, s: String) {
            super.onLoadResource(webView, s)
            val reUrl = webView.url + ""
            val urlList: MutableList<String> = ArrayList()
            urlList.add(reUrl)
            val newList: MutableList<String> = ArrayList()
            for (cd in urlList) {
                if (!newList.contains(cd)) {
                    newList.add(cd)
                }
            }
        }
    }

    private val downloadListener =
        DownloadListener { url: String?, _: String?, _: String?, _: String?, _: Long ->
            val uri = Uri.parse(url)
            val intent = Intent(Intent.ACTION_VIEW, uri)
            context?.startActivity(intent)
        }
}
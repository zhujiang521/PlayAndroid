package com.zj.play.article

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
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
        setDefaultWebSettings()
    }

    private fun setDefaultWebSettings() {
        webChromeClient = chromeClient
        val webSettings = settings
        //5.0以上开启混合模式加载
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }
        webSettings.loadWithOverviewMode = true
        webSettings.useWideViewPort = true
        //允许js代码
        webSettings.javaScriptEnabled = true
        //允许SessionStorage/LocalStorage存储
        webSettings.domStorageEnabled = true
        //禁用放缩
        webSettings.displayZoomControls = false
        webSettings.builtInZoomControls = false
        //禁用文字缩放
        webSettings.textZoom = 100
        //10M缓存，api 18后，系统自动管理。
        webSettings.setAppCacheMaxSize((10 * 1024 * 1024).toLong())
        //允许缓存，设置缓存位置
        webSettings.setAppCacheEnabled(true)
        webSettings.setAppCachePath(context.getDir("appcache", 0).path)
        //允许WebView使用File协议
        webSettings.allowFileAccess = true
        //不保存密码
        webSettings.savePassword = false
        //设置UA
        webSettings.userAgentString = webSettings.userAgentString + " playAndroid"
        //自动加载图片
        webSettings.loadsImagesAutomatically = true
        if (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES) { //判断如果系统是深色主题
            webSettings.forceDark = WebSettings.FORCE_DARK_ON //强制开启webview深色主题模式
        } else {
            webSettings.forceDark = FORCE_DARK_OFF
        }
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

    private var mTouchByUser = false

    override fun loadUrl(url: String, additionalHttpHeaders: Map<String?, String?>) {
        super.loadUrl(url, additionalHttpHeaders)
        resetAllStateInternal(url)
    }

    override fun loadUrl(url: String) {
        super.loadUrl(url)
        resetAllStateInternal(url)
    }

    override fun postUrl(url: String, postData: ByteArray) {
        super.postUrl(url, postData)
        resetAllStateInternal(url)
    }

    override fun loadData(data: String, mimeType: String?, encoding: String?) {
        super.loadData(data, mimeType, encoding)
        resetAllStateInternal(url)
    }

    override fun loadDataWithBaseURL(
        baseUrl: String?,
        data: String,
        mimeType: String?,
        encoding: String?,
        historyUrl: String?
    ) {
        super.loadDataWithBaseURL(baseUrl, data, mimeType, encoding, historyUrl)
        resetAllStateInternal(url)
    }

    override fun reload() {
        super.reload()
        resetAllStateInternal(url)
    }

    fun isTouchByUser(): Boolean {
        return mTouchByUser
    }

    private fun resetAllStateInternal(url: String?) {
        Log.w(TAG, "resetAllStateInternal: url:$url")
        if (url != null && !TextUtils.isEmpty(url) && url.startsWith("javascript:")) {
            return
        }
        resetAllState()
    }

    // 加载url时重置touch状态
    private fun resetAllState() {
        mTouchByUser = false
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN ->                //用户按下到下一个链接加载之前，置为true
                mTouchByUser = true
        }
        return super.onTouchEvent(event)
    }

    override fun setWebViewClient(client: WebViewClient) {
        Log.w(TAG, "setWebViewClient: $client")
        super.setWebViewClient(object : WebViewClient() {


            @Deprecated("Deprecated in Java")
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                val handleByChild = client.shouldOverrideUrlLoading(view, url)
                return if (handleByChild) {
                    // 开放client接口给上层业务调用，如果返回true，表示业务已处理。
                    true
                } else if (!isTouchByUser()) {
                    // 如果业务没有处理，并且在加载过程中用户没有再次触摸屏幕，认为是301/302事件，直接交由系统处理。
                    super.shouldOverrideUrlLoading(view, url)
                } else {
                    //否则，属于二次加载某个链接的情况，为了解决拼接参数丢失问题，重新调用loadUrl方法添加固有参数。
                    loadUrl(url)
                    true
                }
            }

            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest
            ): Boolean {
                Log.w(TAG, "shouldOverrideUrlLoading: ${request.url}")
                val handleByChild = client.shouldOverrideUrlLoading(view, request)
                return if (handleByChild) {
                    true
                } else if (!isTouchByUser()) {
                    return true
                } else {
                    loadUrl(request.url.toString())
                    true
                }
            }
        })
    }

    companion object {
        private const val TAG = "X5WebView"
    }

}
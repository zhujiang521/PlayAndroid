package com.zj.play.article

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.text.TextUtils
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.webkit.*
import android.webkit.WebSettings.FORCE_DARK_OFF
import android.widget.ProgressBar
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import com.zj.core.util.AndroidVersion
import com.zj.core.util.PlayLog
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
        setDefaultWebSettings()
        isHorizontalScrollBarEnabled = false //水平不显示小方块
        isVerticalScrollBarEnabled = false //垂直不显示小方块
        overScrollMode = OVER_SCROLL_NEVER
        isFocusable = true

        progressBar = ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal)
        progressBar?.max = 100
        progressBar?.progressDrawable =
            ResourcesCompat.getDrawable(resources, R.drawable.color_progressbar, null)
        addView(progressBar, LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 6))
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setDefaultWebSettings() {
        webViewClient = client
        webChromeClient = chromeClient
        val webSettings = settings
        //5.0以上开启混合模式加载
        webSettings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
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
        //允许WebView使用File协议
        webSettings.allowFileAccess = true
        //设置UA
        webSettings.userAgentString = webSettings.userAgentString + " playAndroid"
        //自动加载图片
        webSettings.loadsImagesAutomatically = true
        if (AndroidVersion.hasQ()) {
            if (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES) {
                // 判断如果系统是深色主题,强制开启webView深色主题模式
                webSettings.forceDark = WebSettings.FORCE_DARK_ON
            } else {
                webSettings.forceDark = FORCE_DARK_OFF
            }
        }

    }


    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && canGoBack()) {
            // goBack()表示返回WebView的上一页面
            goBack()
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
        PlayLog.w(TAG, "resetAllStateInternal: url:$url")
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

    private val client = object : WebViewClient() {
        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest
        ): Boolean {
            PlayLog.w(TAG, "shouldOverrideUrlLoading: ${request.url}")
            val handleByChild = shouldOverrideUrlLoading(view, request)
            return if (handleByChild) {
                true
            } else if (!isTouchByUser()) {
                return true
            } else {
                loadUrl(request.url.toString())
                true
            }
        }

        override fun shouldInterceptRequest(
            view: WebView?,
            request: WebResourceRequest?
        ): WebResourceResponse? {
            return super.shouldInterceptRequest(view, request)
        }

    }


    companion object {
        private const val TAG = "X5WebView"
    }

}
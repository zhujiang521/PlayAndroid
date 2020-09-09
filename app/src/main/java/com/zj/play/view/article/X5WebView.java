package com.zj.play.view.article;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.export.external.interfaces.WebResourceResponse;
import com.tencent.smtt.sdk.CookieManager;
import com.tencent.smtt.sdk.CookieSyncManager;
import com.tencent.smtt.sdk.DownloadListener;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.zj.play.R;

import java.util.ArrayList;
import java.util.List;

public class X5WebView extends WebView {

    private ProgressBar progressBar;


    public X5WebView(Context context) {
        super(context);
        initUI();
    }

    public X5WebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initUI();
    }

    public X5WebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initUI();
    }

    public void setShowProgress(boolean showProgress) {
        if (showProgress) {
            progressBar.setVisibility(VISIBLE);
        } else {
            progressBar.setVisibility(GONE);
        }
    }


    private void initUI() {

        //getX5WebViewExtension().setScrollBarFadingEnabled(false);
        setHorizontalScrollBarEnabled(false);//水平不显示小方块
        setVerticalScrollBarEnabled(false); //垂直不显示小方块

//      setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);//滚动条在WebView内侧显示
//      setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);//滚动条在WebView外侧显示


        progressBar = new ProgressBar(getContext(), null, android.R.attr.progressBarStyleHorizontal);
        progressBar.setMax(100);
        progressBar.setProgressDrawable(this.getResources().getDrawable(R.drawable.color_progressbar));

        addView(progressBar, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 6));
//        imageView = new ImageView(getContext());
//        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//        //      加载图 根据自己的需求去集成使用
//        imageView.setImageResource(R.mipmap.splash);
//        imageView.setVisibility(VISIBLE);
//        addView(imageView, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        initWebViewSettings();
    }

    //   基本的WebViewSetting
    public void initWebViewSettings() {
        setBackgroundColor(getResources().getColor(android.R.color.white));
        setWebViewClient(client);
        setWebChromeClient(chromeClient);
        setDownloadListener(downloadListener);
        setClickable(true);
        setOnTouchListener((v, event) -> false);
        WebSettings webSetting = getSettings();
        webSetting.setJavaScriptEnabled(true);
        webSetting.setBuiltInZoomControls(true);
        webSetting.setJavaScriptCanOpenWindowsAutomatically(true);
        webSetting.setDomStorageEnabled(true);
        webSetting.setAllowFileAccess(true);
        webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSetting.setSupportZoom(true);
        webSetting.setUseWideViewPort(true);
        webSetting.setSupportMultipleWindows(true);
        webSetting.setAppCacheEnabled(true);
        webSetting.setGeolocationEnabled(true);
        webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
        webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
        webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH);
        //android 默认是可以打开_bank的，是因为它默认设置了WebSettings.setSupportMultipleWindows(false)
        //在false状态下，_bank也会在当前页面打开……
        //而x5浏览器，默认开启了WebSettings.setSupportMultipleWindows(true)，
        // 所以打不开……主动设置成false就可以打开了
        //需要支持多窗体还需要重写WebChromeClient.onCreateWindow
        webSetting.setSupportMultipleWindows(false);
//        webSetting.setCacheMode(WebSettings.LOAD_NORMAL);
//        getSettingsExtension().setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);//extension

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && this.canGoBack()) {
            this.goBack(); // goBack()表示返回WebView的上一页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private WebChromeClient chromeClient = new WebChromeClient() {
        @Override
        public void onReceivedTitle(WebView view, String title) {
            if (TextUtils.isEmpty(title)) {
                return;
            }
        }

        //监听进度
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            progressBar.setProgress(newProgress);
            if (progressBar != null && newProgress != 100) {

                //Webview加载没有完成 就显示我们自定义的加载图
                progressBar.setVisibility(VISIBLE);

            } else if (progressBar != null) {

                //Webview加载完成 就隐藏进度条,显示Webview
                progressBar.setVisibility(GONE);
                //imageView.setVisibility(GONE);
            }
        }
    };

    private WebViewClient client = new WebViewClient() {

        //当页面加载完成的时候
        @Override
        public void onPageFinished(WebView webView, String url) {
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptCookie(true);
            String endCookie = cookieManager.getCookie(url);
            Log.i("TAG", "onPageFinished: endCookie : " + endCookie);
            CookieManager.getInstance().flush();
            super.onPageFinished(webView, url);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            //返回值是true的时候控制去WebView打开，
            // 为false调用系统浏览器或第三方浏览器
            if (url.startsWith("http") || url.startsWith("https") || url.startsWith("ftp")) {
                return false;
            } else {
                try {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    view.getContext().startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(view.getContext(), "手机还没有安装支持打开此网页的应用！", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        }

        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
            return super.shouldInterceptRequest(view, request);
        }

        @Override
        public void onLoadResource(WebView webView, String s) {
            super.onLoadResource(webView, s);
            String reUrl = webView.getUrl() + "";
//            Log.i(TAG, "onLoadResource: onLoadResource : " + reUrl);
            List<String> urlList = new ArrayList<>();
            urlList.add(reUrl);
            List<String> newList = new ArrayList();
            for (String cd : urlList) {
                if (!newList.contains(cd)) {
                    newList.add(cd);
                }
            }
        }


    };

    public void syncCookie(String url, String cookie) {
        CookieSyncManager.createInstance(getContext());
        if (!TextUtils.isEmpty(url)) {
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptCookie(true);
            cookieManager.removeSessionCookie();// 移除
            cookieManager.removeAllCookie();

            //这里的拼接方式是伪代码
            String[] split = cookie.split(";");
            for (String string : split) {
                //为url设置cookie
                // ajax方式下  cookie后面的分号会丢失
                cookieManager.setCookie(url, string);
            }
            String newCookie = cookieManager.getCookie(url);
            Log.i("TAG", "syncCookie: newCookie == " + newCookie);
            //sdk21之后CookieSyncManager被抛弃了，换成了CookieManager来进行管理。
            CookieManager.getInstance().flush();
        }

    }

    //删除Cookie
    private void removeCookie() {

        CookieSyncManager.createInstance(getContext());
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeSessionCookie();
        cookieManager.removeAllCookie();
        CookieManager.getInstance().flush();

    }

    public String getDoMain(String url) {
        String domain = "";
        int start = url.indexOf(".");
        if (start >= 0) {
            int end = url.indexOf("/", start);
            if (end < 0) {
                domain = url.substring(start);
            } else {
                domain = url.substring(start, end);
            }
        }
        return domain;
    }

    DownloadListener downloadListener = (url, userAgent, contentDisposition, mimetype, contentLength) -> {
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        getContext().startActivity(intent);
    };
}

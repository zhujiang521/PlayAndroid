package com.zj.core.view.base

import android.annotation.SuppressLint
import android.app.Activity
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import com.blankj.utilcode.util.ConvertUtils
import com.zj.core.R
import com.zj.core.util.AndroidVersion
import com.zj.core.util.showToast
import com.zj.core.view.custom.RequestLifecycle
import java.lang.ref.WeakReference


/**
 * 应用程序中所有Activity的基类。
 *
 */
@SuppressLint("Registered")
abstract class BaseActivity : AppCompatActivity(), RequestLifecycle, BaseInit {

    /**
     * Activity中显示加载等待的控件。
     */
    private var loading: ProgressBar? = null

    /**
     * Activity中由于服务器异常导致加载失败显示的布局。
     */
    private var loadErrorView: View? = null

    /**
     * Activity中由于网络异常导致加载失败显示的布局。
     */
    private var badNetworkView: View? = null

    /**
     * Activity中当界面上没有任何内容时展示的布局。
     */
    private var noContentView: View? = null

    private var weakRefActivity: WeakReference<Activity>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        transparentStatusBar()
        setContentView(getLayoutId())
        ActivityCollector.add(WeakReference(this))
        weakRefActivity = WeakReference(this)
        initView()
        initData()
    }

    override fun initData() {}

    override fun onDestroy() {
        super.onDestroy()
        ActivityCollector.remove(weakRefActivity)
    }

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
        setupViews()
    }

    protected open fun setupViews() {
        val view = View.inflate(this, R.layout.layout_lce, null)
        val params = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT
        )
        params.setMargins(
            0,
            ConvertUtils.dp2px(if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT || isSearchPage()) 70f else 55f),
            0,
            0
        )
        addContentView(view, params)
        loading = view.findViewById(R.id.loading)
        noContentView = view.findViewById(R.id.noContentView)
        badNetworkView = view.findViewById(R.id.badNetworkView)
        loadErrorView = view.findViewById(R.id.loadErrorView)
        if (loading == null) {
            Log.e(TAG, "loading is null")
        }
        if (badNetworkView == null) {
            Log.e(TAG, "badNetworkView is null")
        }
        if (loadErrorView == null) {
            Log.e(TAG, "loadErrorView is null")
        }
        loadFinished()
    }

    protected open fun isSearchPage(): Boolean {
        return false
    }

    /**
     * 将状态栏设置成透明。只适配Android 5.0以上系统的手机。
     */
    private fun transparentStatusBar() {
        if (AndroidVersion.hasLollipop()) {
            val decorView = window.decorView
            decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            window.statusBarColor = Color.TRANSPARENT
        } else if (AndroidVersion.hasR()) {
            val controller = window.decorView.windowInsetsController
            window.statusBarColor = Color.TRANSPARENT
            window.setDecorFitsSystemWindows(false)
            if (controller != null) {
                // 隐藏状态栏
                controller.systemBarsBehavior =
                    WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        }
    }

    /**
     * 当Activity中的加载内容服务器返回失败，通过此方法显示提示界面给用户。
     *
     * @param tip
     * 界面中的提示信息
     */
    fun showLoadErrorView(tip: String = getString(R.string.failed_load_data)) {
        loadFinished()
        if (loadErrorView != null) {
            val loadErrorText = loadErrorView?.findViewById<TextView>(R.id.loadErrorText)
            loadErrorText?.text = tip
            loadErrorView?.visibility = View.VISIBLE
            return
        }
    }

    /**
     * 当Activity中的内容因为网络原因无法显示的时候，通过此方法显示提示界面给用户。
     *
     * @param listener
     * 重新加载点击事件回调
     */
    private fun showBadNetworkView(listener: View.OnClickListener) {
        loadFinished()
        if (badNetworkView != null) {
            badNetworkView?.visibility = View.VISIBLE
            badNetworkView?.setOnClickListener(listener)
            return
        }
    }

    /**
     * 设置 LiveData 的状态，根据不同状态显示不同页面
     *
     * @param dataLiveData LiveData
     * @param onDataStatus 数据回调进行使用
     */
    fun <T> setDataStatus(dataLiveData: LiveData<Result<T>>, onDataStatus: (T) -> Unit) {
        dataLiveData.observe(this) {
            if (it.isSuccess) {
                val dataList = it.getOrNull()
                if (dataList != null) {
                    loadFinished()
                    onDataStatus(dataList)
                } else {
                    showLoadErrorView()
                }
            } else {
                showToast(getString(R.string.bad_network_view_tip))
                showBadNetworkView { initData() }
            }
        }
    }

    /**
     * 当Activity中没有任何内容的时候，通过此方法显示提示界面给用户。
     * @param tip
     * 界面中的提示信息
     */
    protected fun showNoContentView(tip: String) {
        loadFinished()
        val noContentText = noContentView?.findViewById<TextView>(R.id.noContentText)
        noContentText?.text = tip
        noContentView?.visibility = View.VISIBLE
    }

    /**
     * 将load error view进行隐藏。
     */
    private fun hideLoadErrorView() {
        loadErrorView?.visibility = View.GONE
    }

    /**
     * 将no content view进行隐藏。
     */
    private fun hideNoContentView() {
        noContentView?.visibility = View.GONE
    }

    /**
     * 将bad network view进行隐藏。
     */
    private fun hideBadNetworkView() {
        badNetworkView?.visibility = View.GONE
    }

    @CallSuper
    override fun startLoading() {
        hideBadNetworkView()
        hideNoContentView()
        hideLoadErrorView()
        loading?.visibility = View.VISIBLE
    }

    @CallSuper
    override fun loadFinished() {
        loading?.visibility = View.GONE
        hideBadNetworkView()
        hideNoContentView()
        hideLoadErrorView()
    }

    @CallSuper
    override fun loadFailed(msg: String?) {
        loading?.visibility = View.GONE
        hideBadNetworkView()
        hideNoContentView()
        hideLoadErrorView()
    }

    companion object {

        private const val TAG = "BaseActivity"
    }
}

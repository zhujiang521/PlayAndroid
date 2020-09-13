package com.zj.core.view

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import com.zj.core.R
import com.zj.core.util.AndroidVersion
import com.zj.core.util.logWarn
import java.lang.ref.WeakReference

/**
 * 应用程序中所有Activity的基类。
 *
 * @author guolin
 * @since 17/2/16
 */
@SuppressLint("Registered")
abstract class BaseActivity : AppCompatActivity(), RequestLifecycle {

    /**
     * 判断当前Activity是否在前台。
     */
    protected var isActive: Boolean = false

    /**
     * 当前Activity的实例。
     */
    protected var activity: Activity? = null

    /**
     * Activity中显示加载等待的控件。
     */
    protected var loading: ProgressBar? = null

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

    var toolbar: Toolbar? = null

    private var progressDialog: ProgressDialog? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        transparentStatusBar()
        setContentView(getLayoutId())
        ActivityCollector.add(WeakReference(this))
        activity = this
        weakRefActivity = WeakReference(this)
        initView()
        initData()
    }

    abstract fun initData()

    abstract fun initView()

    abstract fun getLayoutId(): Int

    override fun onResume() {
        super.onResume()
        isActive = true
    }

    override fun onPause() {
        super.onPause()
        isActive = false
    }

    override fun onDestroy() {
        super.onDestroy()
        activity = null
        ActivityCollector.remove(weakRefActivity)
    }

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
        setupViews()
    }

    protected open fun setupViews() {
        loading = findViewById(R.id.loading)
        noContentView = findViewById(R.id.noContentView)
        badNetworkView = findViewById(R.id.badNetworkView)
        loadErrorView = findViewById(R.id.loadErrorView)
    }


    /**
     * 将状态栏设置成透明。只适配Android 5.0以上系统的手机。
     */
    protected fun transparentStatusBar() {
        if (AndroidVersion.hasLollipop()) {
            val decorView = window.decorView
            decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            window.statusBarColor = Color.TRANSPARENT
        }
    }


    /**
     * 隐藏软键盘。
     */
    fun hideSoftKeyboard() {
        try {
            val view = currentFocus
            if (view != null) {
                val binder = view.windowToken
                val manager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                manager.hideSoftInputFromWindow(binder, InputMethodManager.HIDE_NOT_ALWAYS)
            }
        } catch (e: Exception) {
            logWarn(TAG, e.message, e)
        }

    }

    /**
     * 显示软键盘。
     */
    fun showSoftKeyboard(editText: EditText?) {
        try {
            if (editText != null) {
                editText.requestFocus()
                val manager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                manager.showSoftInput(editText, 0)
            }
        } catch (e: Exception) {
            logWarn(TAG, e.message, e)
        }

    }

    /**
     * 当Activity中的加载内容服务器返回失败，通过此方法显示提示界面给用户。
     *
     * @param tip
     * 界面中的提示信息
     */
    protected fun showLoadErrorView(tip: String = "加载数据失败") {
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
    protected fun showBadNetworkView(listener: View.OnClickListener) {
        loadFinished()
        if (badNetworkView != null) {
            badNetworkView?.visibility = View.VISIBLE
            badNetworkView?.setOnClickListener(listener)
            return
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
    protected fun hideLoadErrorView() {
        loadErrorView?.visibility = View.GONE
    }

    /**
     * 将no content view进行隐藏。
     */
    protected fun hideNoContentView() {
        noContentView?.visibility = View.GONE
    }

    /**
     * 将bad network view进行隐藏。
     */
    protected fun hideBadNetworkView() {
        badNetworkView?.visibility = View.GONE
    }

    fun showProgressDialog(title: String?, message: String) {
        if (progressDialog == null) {
            progressDialog = ProgressDialog(this).apply {
                if (title != null) {
                    setTitle(title)
                }
                setMessage(message)
                setCancelable(false)
            }
        }
        progressDialog?.show()
    }

    fun closeProgressDialog() {
        progressDialog?.let {
            if (it.isShowing) {
                it.dismiss()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
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

package com.zj.core.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewStub
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import com.zj.core.R

/**
 * 应用程序中所有Fragment的基类。
 *
 * @author guolin
 * @since 17/3/20
 */
abstract class BaseFragment : Fragment(), RequestLifecycle {

    /**
     * Fragment中由于服务器异常导致加载失败显示的布局。
     */
    private var loadErrorView: View? = null

    /**
     * Fragment中由于网络异常导致加载失败显示的布局。
     */
    private var badNetworkView: View? = null

    /**
     * Fragment中当界面上没有任何内容时展示的布局。
     */
    private var noContentView: View? = null

    /**
     * Fragment中inflate出来的布局。
     */
    protected var rootView: View? = null

    /**
     * Fragment中显示加载等待的控件。
     */
    protected var loading: ProgressBar? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(getLayoutId(), container, false)
        onCreateView(view)
        // Inflate  the layout for this fragment
        return view
    }

    abstract fun getLayoutId(): Int


    /**
     * 当Fragment中的加载内容服务器返回失败，通过此方法显示提示界面给用户。
     *
     * @param tip
     * 界面中的提示信息
     */
    protected fun showLoadErrorView(tip: String = "加载数据失败") {
        if (loadErrorView != null) {
            loadErrorView?.visibility = View.VISIBLE
            return
        }
        if (rootView != null) {
            val viewStub = rootView?.findViewById<ViewStub>(R.id.loadErrorView)
            if (viewStub != null) {
                loadErrorView = viewStub.inflate()
                val loadErrorText = loadErrorView?.findViewById<TextView>(R.id.loadErrorText)
                loadErrorText?.text = tip
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()
        initData()
    }

    abstract fun initView()

    abstract fun initData()

    /**
     * 当Fragment中的内容因为网络原因无法显示的时候，通过此方法显示提示界面给用户。
     *
     * @param listener
     * 重新加载点击事件回调
     */
    protected fun showBadNetworkView(listener: View.OnClickListener) {
        if (badNetworkView != null) {
            badNetworkView?.visibility = View.VISIBLE
            return
        }
        if (rootView != null) {
            val viewStub = rootView?.findViewById<ViewStub>(R.id.badNetworkView)
            if (viewStub != null) {
                badNetworkView = viewStub.inflate()
                val badNetworkRootView = badNetworkView?.findViewById<View>(R.id.badNetworkRootView)
                badNetworkRootView?.setOnClickListener(listener)
            }
        }
    }

    /**
     * 当Fragment中没有任何内容的时候，通过此方法显示提示界面给用户。
     *
     * @param tip
     * 界面中的提示信息
     */
    protected fun showNoContentView(tip: String) {
        if (noContentView != null) {
            noContentView?.visibility = View.VISIBLE
            return
        }
        if (rootView != null) {
            val viewStub = rootView?.findViewById<ViewStub>(R.id.noContentView)
            if (viewStub != null) {
                noContentView = viewStub.inflate()
                val noContentText = noContentView?.findViewById<TextView>(R.id.noContentText)
                noContentText?.text = tip
            }
        }
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

    /**
     * 在Fragment基类中获取通用的控件，会将传入的View实例原封不动返回。
     * @param view
     * Fragment中inflate出来的View实例。
     * @return  Fragment中inflate出来的View实例原封不动返回。
     */
    fun onCreateView(view: View): View {
        rootView = view
        loading = view.findViewById(R.id.loading)
        return view
    }

    /**
     * 开始加载，将加载等待控件显示。
     */
    @CallSuper
    override fun startLoading() {
        loading?.visibility = View.VISIBLE
        hideBadNetworkView()
        hideNoContentView()
        hideLoadErrorView()
    }

    /**
     * 加载完成，将加载等待控件隐藏。
     */
    @CallSuper
    override fun loadFinished() {
        loading?.visibility = View.GONE
    }

    /**
     * 加载失败，将加载等待控件隐藏。
     */
    @CallSuper
    override fun loadFailed(msg: String?) {
        loading?.visibility = View.GONE
    }
}

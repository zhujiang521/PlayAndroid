package com.zj.core.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.zj.core.R

/**
 * 应用程序中所有Fragment的基类。
 *
 * @author guolin
 * @since 17/3/20
 */
abstract class BaseFragment : Fragment(), RequestLifecycle {

    private val viewModel by lazy { ViewModelProvider(this).get(BaseFragmentViewModel::class.java) }

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
        loadFinished()
        if (viewModel.loadErrorView != null) {
            viewModel.loadErrorView?.visibility = View.VISIBLE
            val loadErrorText =
                viewModel.loadErrorView?.findViewById<TextView>(R.id.loadErrorText)
            loadErrorText?.text = tip
            return
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
        loadFinished()
        if (viewModel.badNetworkView != null) {
            viewModel.badNetworkView?.visibility = View.VISIBLE
            viewModel.badNetworkView?.setOnClickListener(listener)
            return
        }
    }

    /**
     * 当Fragment中没有任何内容的时候，通过此方法显示提示界面给用户。
     *
     * @param tip
     * 界面中的提示信息
     */
    protected fun showNoContentView(tip: String) {
        loadFinished()
        if (viewModel.noContentView != null) {
            viewModel.noContentView?.visibility = View.VISIBLE
            val noContentText = viewModel.noContentView?.findViewById<TextView>(R.id.noContentText)
            noContentText?.text = tip
            return
        }
    }

    /**
     * 将load error view进行隐藏。
     */
    protected fun hideLoadErrorView() {
        viewModel.loadErrorView?.visibility = View.GONE
    }

    /**
     * 将no content view进行隐藏。
     */
    protected fun hideNoContentView() {
        viewModel.noContentView?.visibility = View.GONE
    }

    /**
     * 将bad network view进行隐藏。
     */
    protected fun hideBadNetworkView() {
        viewModel.badNetworkView?.visibility = View.GONE
    }

    /**
     * 在Fragment基类中获取通用的控件，会将传入的View实例原封不动返回。
     * @param view
     * Fragment中inflate出来的View实例。
     * @return  Fragment中inflate出来的View实例原封不动返回。
     */
    private fun onCreateView(view: View): View {
        viewModel.rootView = view
        viewModel.loading = view.findViewById(R.id.loading)
        viewModel.noContentView = view.findViewById(R.id.noContentView)
        viewModel.badNetworkView = view.findViewById(R.id.badNetworkView)
        viewModel.loadErrorView = view.findViewById(R.id.loadErrorView)
        if (viewModel.loading == null) {
            throw NullPointerException("loading is null")
        }
        if (viewModel.badNetworkView == null) {
            throw NullPointerException("badNetworkView is null")
        }
        if (viewModel.loadErrorView == null) {
            throw NullPointerException("loadErrorView is null")
        }
        return view
    }

    /**
     * 开始加载，将加载等待控件显示。
     */
    @CallSuper
    override fun startLoading() {
        viewModel.loading?.visibility = View.VISIBLE
        hideBadNetworkView()
        hideNoContentView()
        hideLoadErrorView()
    }

    /**
     * 加载完成，将加载等待控件隐藏。
     */
    @CallSuper
    override fun loadFinished() {
        viewModel.loading?.visibility = View.GONE
        hideBadNetworkView()
        hideNoContentView()
        hideLoadErrorView()
    }

    /**
     * 加载失败，将加载等待控件隐藏。
     */
    @CallSuper
    override fun loadFailed(msg: String?) {
        viewModel.loading?.visibility = View.GONE
        hideBadNetworkView()
        hideNoContentView()
        hideLoadErrorView()
    }
}

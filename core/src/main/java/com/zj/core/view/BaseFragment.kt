package com.zj.core.view

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import com.blankj.utilcode.util.ConvertUtils
import com.zj.core.R
import com.zj.core.util.showToast

/**
 * 应用程序中所有Fragment的基类。
 */
abstract class BaseFragment : Fragment(), RequestLifecycle, BaseInit {

    /**
     * Fragment中由于服务器异常导致加载失败显示的布局。
     */
    private var loadErrorView: RelativeLayout? = null

    /**
     * Fragment中由于网络异常导致加载失败显示的布局。
     */
    private var badNetworkView: RelativeLayout? = null

    /**
     * Fragment中当界面上没有任何内容时展示的布局。
     */
    private var noContentView: RelativeLayout? = null


    /**
     * Fragment中显示加载等待的控件。
     */
    private var loading: ProgressBar? = null

    protected open fun isHaveHeadMargin(): Boolean {
        return true
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val frameLayout = FrameLayout(context!!)
        val lce = View.inflate(context, R.layout.layout_lce, null)
        val params = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT
        )
        val isPort = resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT
        params.setMargins(
            0,
            if (isHaveHeadMargin()) {
                ConvertUtils.dp2px(if (isPort) 70f else 55f)
            } else 0,
            0,
            0
        )
        lce.layoutParams = params
        val content = inflater.inflate(getLayoutId(), container, false)
        frameLayout.addView(content)
        frameLayout.addView(lce)
        onCreateView(lce)
        return frameLayout
    }


    /**
     * 当Fragment中的加载内容服务器返回失败，通过此方法显示提示界面给用户。
     *
     * @param tip
     * 界面中的提示信息
     */
    private fun showLoadErrorView(tip: String = getString(R.string.failed_load_data)) {
        loadFinished()
        if (loadErrorView != null) {
            loadErrorView?.visibility = View.VISIBLE
            val loadErrorText =
                loadErrorView?.findViewById<TextView>(R.id.loadErrorText)
            loadErrorText?.text = tip
            return
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()
        initData()
    }

    /**
     * 设置 LiveData 的状态，根据不同状态显示不同页面
     *
     * @param dataLiveData LiveData
     * @param onDataStatus 数据回调进行使用
     */
    fun <T> setDataStatus(
        dataLiveData: LiveData<Result<T>>,
        onBadNetwork: () -> Unit = {},
        onDataStatus: (T) -> Unit
    ) {
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
                onBadNetwork.invoke()
            }
        }
    }

    /**
     * 当Fragment中的内容因为网络原因无法显示的时候，通过此方法显示提示界面给用户。
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
     * 当Fragment中没有任何内容的时候，通过此方法显示提示界面给用户。
     *
     * @param tip
     * 界面中的提示信息
     */
    protected fun showNoContentView(tip: String) {
        loadFinished()
        if (noContentView != null) {
            noContentView?.visibility = View.VISIBLE
            val noContentText = noContentView?.findViewById<TextView>(R.id.noContentText)
            noContentText?.text = tip
            return
        }
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

    /**
     * 在Fragment基类中获取通用的控件，会将传入的View实例原封不动返回。
     * @param view
     * Fragment中inflate出来的View实例。
     * @return  Fragment中inflate出来的View实例原封不动返回。
     */
    private fun onCreateView(view: View): View {
        loading = view.findViewById(R.id.loading)
        noContentView = view.findViewById(R.id.noContentView)
        badNetworkView = view.findViewById(R.id.badNetworkView)
        loadErrorView = view.findViewById(R.id.loadErrorView)
        if (loading == null) {
            throw NullPointerException("loading is null")
        }
        if (badNetworkView == null) {
            throw NullPointerException("badNetworkView is null")
        }
        if (loadErrorView == null) {
            throw NullPointerException("loadErrorView is null")
        }
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
        hideBadNetworkView()
        hideNoContentView()
        hideLoadErrorView()
    }

    /**
     * 加载失败，将加载等待控件隐藏。
     */
    @CallSuper
    override fun loadFailed(msg: String?) {
        loading?.visibility = View.GONE
        hideBadNetworkView()
        hideNoContentView()
        hideLoadErrorView()
    }

}

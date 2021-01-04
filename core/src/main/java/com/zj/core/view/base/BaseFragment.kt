package com.zj.core.view.base

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import com.blankj.utilcode.util.ConvertUtils
import com.zj.core.R
import com.zj.core.util.showToast
import com.zj.core.view.base.lce.DefaultLceImpl
import com.zj.core.view.base.lce.ILce

/**
 * 应用程序中所有Fragment的基类。
 */
abstract class BaseFragment : Fragment(), ILce, BaseInit {

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

    private var defaultLce: ILce? = null

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
        defaultLce = DefaultLceImpl(
            loading,
            loadErrorView,
            badNetworkView,
            noContentView
        )
        return view
    }

    @CallSuper
    override fun startLoading() {
        defaultLce?.startLoading()
    }

    @CallSuper
    override fun loadFinished() {
        defaultLce?.loadFinished()
    }

    override fun showLoadErrorView(tip: String) {
        defaultLce?.showLoadErrorView(tip)
    }

    override fun showBadNetworkView(listener: View.OnClickListener) {
        defaultLce?.showBadNetworkView(listener)
    }

    override fun showNoContentView(tip: String) {
        defaultLce?.showNoContentView(tip)
    }

}

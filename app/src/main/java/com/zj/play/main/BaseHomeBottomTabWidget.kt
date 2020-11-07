package com.zj.play.main

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.zj.play.R
import com.zj.play.home.HomePageFragment
import com.zj.play.official.OfficialAccountsFragment
import com.zj.play.profile.ProfileFragment
import com.zj.play.project.ProjectFragment

abstract class BaseHomeBottomTabWidget @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    layoutId: Int
) : LinearLayout(context, attrs, defStyleAttr), View.OnClickListener {

    private var mFragmentManager: FragmentManager? = null
    private var mFragments: ArrayList<Fragment>? = null
    private lateinit var mViewModel: MainViewModel
    private var currentFragment: Fragment? = null

    /**
     * 外部调用初始化，传入必要的参数
     *
     * @param fm
     */
    fun init(fm: FragmentManager?, viewModel: MainViewModel) {
        mFragmentManager = fm
        mViewModel = viewModel
        if (mFragments == null) {
            mFragments = arrayListOf()
            mFragments?.let {
                it.add(getCurrentFragment(0)!!)
                it.add(getCurrentFragment(1)!!)
                it.add(getCurrentFragment(2)!!)
                it.add(getCurrentFragment(3)!!)
            }
        }
        fragmentManger(viewModel.getPage() ?: 0)
    }

    /**
     * 初始化 设置点击事件。
     *
     * @param view /
     */
    @Suppress("LeakingThis")
    abstract fun initView(view: View)

    /**
     * 销毁，避免内存泄漏
     */
    open fun destroy() {
        if (null != mFragmentManager) {
            if (!mFragmentManager!!.isDestroyed)
                mFragmentManager = null
        }
        if (!mFragments.isNullOrEmpty()) {
            mFragments?.clear()
            mFragments = null
        }
    }

    /**
     * fragment的切换 实现底部导航栏的切换
     *
     * @param position 序号
     */
    protected open fun fragmentManger(position: Int) {
        mViewModel.setPage(position)
        val targetFg: Fragment = mFragments!![position]
        val transaction = mFragmentManager!!.beginTransaction()
        if (currentFragment != null) {
            transaction.hide(currentFragment!!)
        }
        if (!targetFg.isAdded) {
            transaction.add(R.id.flHomeFragment, targetFg).commit()
        } else {
            transaction.show(targetFg).commit()
        }
        currentFragment = targetFg
    }

    init {
        initView(View.inflate(context, layoutId, this))
    }

    private val mHomeFragment: HomePageFragment by lazy { HomePageFragment.newInstance() }
    private val mProjectFragment: ProjectFragment by lazy { ProjectFragment.newInstance() }
    private val mObjectListFragment: OfficialAccountsFragment by lazy { OfficialAccountsFragment.newInstance() }
    private val mProfileFragment: ProfileFragment by lazy { ProfileFragment.newInstance() }

    private fun getCurrentFragment(index: Int): Fragment? {
        return when (index) {
            0 -> mHomeFragment
            1 -> mProjectFragment
            2 -> mObjectListFragment
            3 -> mProfileFragment
            else -> null
        }
    }

}
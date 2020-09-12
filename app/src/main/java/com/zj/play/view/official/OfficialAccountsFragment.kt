package com.zj.play.view.official

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.zj.core.view.BaseFragment
import com.zj.core.view.FragmentAdapter
import com.zj.play.R
import com.zj.play.view.official.list.OfficialListFragment
import kotlinx.android.synthetic.main.fragment_official_accounts.*

class OfficialAccountsFragment : BaseFragment(), ViewPager.OnPageChangeListener,
    TabLayout.OnTabSelectedListener {

    private val viewModel by lazy { ViewModelProvider(this).get(OfficialViewModel::class.java) }

    override fun getLayoutId(): Int {
        return R.layout.fragment_official_accounts
    }

    private lateinit var adapter: FragmentAdapter

    override fun initView() {
        adapter = FragmentAdapter(activity?.supportFragmentManager)
        officialViewPager.adapter = adapter
        officialTabLayout.setupWithViewPager(officialViewPager)
        officialViewPager.addOnPageChangeListener(this)
        officialTabLayout.addOnTabSelectedListener(this)
    }

    override fun initData() {
        startLoading()
        viewModel.officialTreeLiveData.observe(this, {
            if (it.isSuccess) {
                loadFinished()
                val projectTree = it.getOrNull()
                if (projectTree != null) {
                    val nameList = mutableListOf<String>()
                    val viewList = mutableListOf<Fragment>()
                    projectTree.forEach { project ->
                        nameList.add(project.name)
                        viewList.add(OfficialListFragment.newInstance(project.id))
                    }
                    adapter.reset(nameList.toTypedArray())
                    adapter.reset(viewList)
                    adapter.notifyDataSetChanged()
                    officialViewPager.currentItem = viewModel.position
                } else {
                    showLoadErrorView()
                }
            } else {
                showBadNetworkView { initData() }
            }
        })
    }

    companion object {
        @JvmStatic
        fun newInstance() = OfficialAccountsFragment()
    }


    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    }

    override fun onPageSelected(position: Int) {
        viewModel.position = position
    }

    override fun onPageScrollStateChanged(state: Int) {
    }

    override fun onTabSelected(tab: TabLayout.Tab?) {
        if (tab != null && tab.position > 0)
            viewModel.position = tab.position
    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {
    }

    override fun onTabReselected(tab: TabLayout.Tab?) {
    }

}

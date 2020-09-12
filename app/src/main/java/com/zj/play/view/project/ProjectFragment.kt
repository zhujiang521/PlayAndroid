package com.zj.play.view.project

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.zj.core.view.BaseFragment
import com.zj.core.view.FragmentAdapter
import com.zj.play.R
import com.zj.play.view.project.list.ProjectListFragment
import kotlinx.android.synthetic.main.fragment_project.*

class ProjectFragment : BaseFragment(), ViewPager.OnPageChangeListener,
    TabLayout.OnTabSelectedListener {

    private val viewModel by lazy { ViewModelProvider(this).get(ProjectViewModel::class.java) }

    override fun getLayoutId(): Int {
        return R.layout.fragment_project
    }

    private lateinit var adapter: FragmentAdapter

    override fun initView() {
        adapter = FragmentAdapter(activity?.supportFragmentManager)
        projectViewPager.adapter = adapter
        projectTabLayout.setupWithViewPager(projectViewPager)
        projectViewPager.addOnPageChangeListener(this)
        projectTabLayout.addOnTabSelectedListener(this)
    }

    override fun initData() {
        startLoading()
        viewModel.projectTreeLiveData.observe(this, {
            if (it.isSuccess) {
                loadFinished()
                val projectTree = it.getOrNull()
                if (projectTree != null) {
                    val nameList = mutableListOf<String>()
                    val viewList = mutableListOf<Fragment>()
                    projectTree.forEach { project ->
                        nameList.add(project.name)
                        viewList.add(ProjectListFragment.newInstance(project.id))
                    }
                    adapter.reset(nameList.toTypedArray())
                    adapter.reset(viewList)
                    adapter.notifyDataSetChanged()
                    projectViewPager.currentItem = viewModel.position
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
        fun newInstance() = ProjectFragment()
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

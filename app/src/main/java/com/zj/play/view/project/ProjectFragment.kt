package com.zj.play.view.project

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.zj.core.view.BaseFragment
import com.zj.core.view.FragmentAdapter
import com.zj.play.R
import com.zj.play.network.Repository
import com.zj.play.view.home.HomePageViewModel
import com.zj.play.view.project.list.ProjectListFragment
import kotlinx.android.synthetic.main.fragment_project.*

class ProjectFragment : BaseFragment() {

    private val viewModel by lazy { ViewModelProvider(this).get(ProjectViewModel::class.java) }

    override fun getLayoutId(): Int {
        return R.layout.fragment_project
    }

    private lateinit var adapter: FragmentAdapter

    override fun initView() {
        adapter = FragmentAdapter(activity?.supportFragmentManager)
        projectViewPager.adapter = adapter
        projectTabLayout.setupWithViewPager(projectViewPager)
    }

    override fun initData() {
        startLoading()
        viewModel.projectTreeLiveData.observe(this, Observer {
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
                } else {
                    showLoadErrorView()
                }
            } else {
                showBadNetworkView(View.OnClickListener { initData() })
            }
        })
    }

    companion object {
        @JvmStatic
        fun newInstance() = ProjectFragment()
    }
}

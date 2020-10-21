package com.zj.play.view.project

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.zj.core.view.FragmentAdapter
import com.zj.play.R
import com.zj.play.room.entity.ProjectClassify
import com.zj.play.view.project.list.ProjectListFragment
import kotlinx.android.synthetic.main.fragment_project.*

class ProjectFragment : BaseTabFragment() {

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
        setDataStatus(viewModel.projectTreeLiveData)
        getProjectTree()
    }

    override fun <T> setData(data: T){
        data as List<ProjectClassify>
        val nameList = mutableListOf<String>()
        val viewList = mutableListOf<Fragment>()
        data.forEach { project ->
            nameList.add(project.name)
            viewList.add(ProjectListFragment.newInstance(project.id))
        }
        adapter.reset(nameList.toTypedArray())
        adapter.reset(viewList)
        adapter.notifyDataSetChanged()
        projectViewPager.currentItem = viewModel.position
    }

    private fun getProjectTree() {
        viewModel.getArticleList(false)
    }

    override fun onTabPageSelected(position: Int) {
        viewModel.position = position
    }

    companion object {
        @JvmStatic
        fun newInstance() = ProjectFragment()
    }

}

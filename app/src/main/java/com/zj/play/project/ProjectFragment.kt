package com.zj.play.project

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.zj.core.view.custom.FragmentAdapter
import com.zj.play.R
import com.zj.play.project.list.ProjectListFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_project.*

@AndroidEntryPoint
class ProjectFragment : BaseTabFragment() {

    private val viewModel by viewModels<ProjectViewModel>()

    override fun getLayoutId(): Int = R.layout.fragment_project

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
        setDataStatus(viewModel.dataLiveData) {
            val nameList = mutableListOf<String>()
            val viewList = mutableListOf<Fragment>()
            it.forEach { project ->
                nameList.add(project.name)
                viewList.add(ProjectListFragment.newInstance(project.id))
            }
            adapter.apply {
                reset(nameList.toTypedArray())
                reset(viewList)
                notifyDataSetChanged()
            }
            projectViewPager.currentItem = viewModel.position
        }
        getProjectTree()
    }

    private fun getProjectTree() {
        viewModel.getDataList(false)
    }

    override fun onTabPageSelected(position: Int) {
        viewModel.position = position
    }

    companion object {
        @JvmStatic
        fun newInstance() = ProjectFragment()
    }

}

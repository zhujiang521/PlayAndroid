package com.zj.play.project

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.zj.core.view.custom.FragmentAdapter
import com.zj.play.databinding.FragmentProjectBinding
import com.zj.play.project.list.ProjectListFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProjectFragment : BaseTabFragment() {

    private val viewModel by viewModels<ProjectViewModel>()

    private lateinit var adapter: FragmentAdapter
    private var binding: FragmentProjectBinding? = null

    override fun getLayoutView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        attachToRoot: Boolean
    ): View {
        binding = FragmentProjectBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun initView() {
        adapter = FragmentAdapter(activity?.supportFragmentManager)
        binding?.apply {
            projectViewPager.adapter = adapter
            projectTabLayout.setupWithViewPager(projectViewPager)
            projectViewPager.addOnPageChangeListener(this@ProjectFragment)
            projectTabLayout.addOnTabSelectedListener(this@ProjectFragment)
        }
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
            binding?.projectViewPager?.currentItem = viewModel.position
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

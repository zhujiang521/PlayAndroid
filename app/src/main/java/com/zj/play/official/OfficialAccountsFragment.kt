package com.zj.play.official

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayoutMediator
import com.zj.core.view.custom.FragmentAdapter
import com.zj.play.databinding.FragmentOfficialAccountsBinding
import com.zj.play.official.list.OfficialListFragment
import com.zj.play.project.BaseTabFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OfficialAccountsFragment : BaseTabFragment() {

    private val viewModel by viewModels<OfficialViewModel>()
    private var binding: FragmentOfficialAccountsBinding? = null

    override fun getLayoutView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        attachToRoot: Boolean
    ): View {
        binding = FragmentOfficialAccountsBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    private lateinit var adapter: FragmentAdapter

    override fun initView() {
        adapter = FragmentAdapter(requireActivity().supportFragmentManager, lifecycle)
        binding?.apply {
            officialViewPager2?.adapter = adapter
            officialTabLayout.setupWithViewPager(officialViewPager)
            officialTabLayout.addOnTabSelectedListener(this@OfficialAccountsFragment)
            TabLayoutMediator(officialTabLayout, officialViewPager2!!) { tab, position ->
                tab.text = adapter.title(position)
            }.attach()
        }
    }

    override fun initData() {
        startLoading()
        setDataStatus(viewModel.dataLiveData) {
            val nameList = mutableListOf<String>()
            val viewList = mutableListOf<Fragment>()
            it.forEach { project ->
                nameList.add(project.name)
                viewList.add(OfficialListFragment.newInstance(project.id))
            }
            adapter.apply {
                reset(nameList.toTypedArray())
                reset(viewList)
                notifyDataSetChanged()
            }
            binding?.officialViewPager?.currentItem = viewModel.position
        }
        getOfficialTree()
    }

    private fun getOfficialTree() {
        viewModel.getDataList(false)
    }

    override fun onTabPageSelected(position: Int) {
        viewModel.position = position
    }

    companion object {
        @JvmStatic
        fun newInstance() = OfficialAccountsFragment()
    }

}

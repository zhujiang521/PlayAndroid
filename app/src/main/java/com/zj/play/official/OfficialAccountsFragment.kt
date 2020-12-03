package com.zj.play.official

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.zj.core.view.custom.FragmentAdapter
import com.zj.play.R
import com.zj.play.official.list.OfficialListFragment
import com.zj.play.project.BaseTabFragment
import kotlinx.android.synthetic.main.fragment_official_accounts.*

class OfficialAccountsFragment : BaseTabFragment() {

    private val viewModel by lazy { ViewModelProvider(this).get(OfficialViewModel::class.java) }

    override fun getLayoutId(): Int = R.layout.fragment_official_accounts

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
            officialViewPager.currentItem = viewModel.position
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

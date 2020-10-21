package com.zj.play.view.main

import androidx.fragment.app.Fragment
import com.zj.play.view.home.HomePageFragment
import com.zj.play.view.official.OfficialAccountsFragment
import com.zj.play.view.profile.ProfileFragment
import com.zj.play.view.project.ProjectFragment

/**
 * 版权：渤海新能 版权所有
 * @author zhujiang
 * 版本：1.5
 * 创建日期：2020-01-15
 * 描述：pwqgc
 *
 */
object FragmentFactory {

    private val mHomeFragment: HomePageFragment by lazy { HomePageFragment.newInstance() }
    private val mProjectFragment: ProjectFragment by lazy { ProjectFragment.newInstance() }
    private val mObjectListFragment: OfficialAccountsFragment by lazy { OfficialAccountsFragment.newInstance() }
    private val mProfileFragment: ProfileFragment by lazy { ProfileFragment.newInstance() }

    fun getCurrentFragment(index: Int): Fragment? {
        return when (index) {
            0 -> mHomeFragment
            1 -> mProjectFragment
            2 -> mObjectListFragment
            3 -> mProfileFragment
            else -> null
        }
    }

}
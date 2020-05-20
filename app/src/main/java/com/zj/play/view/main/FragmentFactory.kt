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
class FragmentFactory {

    companion object {
        private var mHomeFragment: HomePageFragment? = null
        private var mProjectFragment: ProjectFragment? = null
        private var mObjectListFragment: OfficialAccountsFragment? = null
        private var mProfileFragment: ProfileFragment? = null

        private fun getHomeFragment(): HomePageFragment {
            if (mHomeFragment == null) {
                mHomeFragment = HomePageFragment.newInstance()
            }
            return mHomeFragment as HomePageFragment
        }


        private fun getProjectFragment(): ProjectFragment {
            if (mProjectFragment == null) {
                mProjectFragment = ProjectFragment.newInstance()
            }
            return mProjectFragment as ProjectFragment
        }

        private fun getObjectListFragment(): OfficialAccountsFragment {
            if (mObjectListFragment == null) {
                mObjectListFragment = OfficialAccountsFragment.newInstance()
            }
            return mObjectListFragment as OfficialAccountsFragment
        }

        private fun getProfileFragment(): ProfileFragment {
            if (mProfileFragment == null) {
                mProfileFragment = ProfileFragment.newInstance()
            }
            return mProfileFragment as ProfileFragment
        }


        fun getCurrentFragment(index: Int): Fragment? {
            return when (index) {
                0 ->
                    getHomeFragment()
                1 ->
                    getProjectFragment()
                2 ->
                    getObjectListFragment()
                3 ->
                    getProfileFragment()
                else -> null
            }


        }
    }

}
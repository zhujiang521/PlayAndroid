package com.zj.core.view;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.List;

/**
 * 版权：渤海新能 版权所有
 *
 * @author zhujiang
 * 版本：1.5
 * 创建日期：2020-01-18
 * 描述：pwqgc
 */
public class FragmentAdapter extends FragmentPagerAdapter {
    private List<Fragment> mFragment = new ArrayList<>();
    private final FragmentManager mFragmentManager;
    private boolean mUpdateFlag;
    private Fragment mCurFragment;
    private String[] mTitles;

    public FragmentAdapter(FragmentManager fm) {
        super(fm);
        this.mFragmentManager = fm;
    }

    public boolean isUpdateFlag() {
        return mUpdateFlag;
    }

    public void setUpdateFlag(boolean mUpdateFlag) {
        this.mUpdateFlag = mUpdateFlag;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        if (mUpdateFlag) {
            Fragment f = (Fragment) super.instantiateItem(container, position);
            String fragmentTag = f.getTag();
            if (f != getItem(position)) {
                //如果是新建的fragment，f 就和getItem(position)是同一个fragment，否则进入下面
                FragmentTransaction ft = mFragmentManager.beginTransaction();
                //移除旧的fragment
                ft.remove(f);
                //换成新的fragment
                f = getItem(position);
                //添加新fragment时必须用前面获得的tag
                ft.add(container.getId(), f, fragmentTag);
                ft.attach(f);
                ft.commitAllowingStateLoss();
            }
            return f;
        }
        return super.instantiateItem(container, position);
    }

    public void reset(List<Fragment> fragments) {
        mFragment.clear();
        mFragment.addAll(fragments);
    }

    @Override
    public void setPrimaryItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        super.setPrimaryItem(container, position, object);
        if (object instanceof Fragment) {
            mCurFragment = (Fragment) object;
        }
    }

    public Fragment getCurFragment() {
        return mCurFragment;
    }

    public void reset(String[] titles) {
        this.mTitles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragment.get(position);
    }

    @Override
    public int getCount() {
        return mFragment.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }


}

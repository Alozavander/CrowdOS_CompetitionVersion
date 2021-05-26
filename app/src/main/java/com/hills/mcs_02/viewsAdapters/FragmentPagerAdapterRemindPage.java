package com.hills.mcs_02.viewsAdapters;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

public class FragmentPagerAdapterRemindPage extends FragmentPagerAdapter {
    private List<Fragment> mFragments;
    private Context mContext;

    public FragmentPagerAdapterRemindPage(FragmentManager fm, Context context, List<Fragment> fragments) {
        super(fm);
        this.mContext = context;
        this.mFragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }


}

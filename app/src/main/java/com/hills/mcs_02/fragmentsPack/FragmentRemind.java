package com.hills.mcs_02.fragmentsPack;

import com.google.android.material.tabs.TabLayout;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.hills.mcs_02.R;
import com.hills.mcs_02.viewsAdapters.FragmentPagerAdapterRemindPage;

public class FragmentRemind extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final String TAG = "Fragment_remind";
    private String mParam1;
    private String mParam2;
    private Context mContext;
    private TabLayout mTabLayout;
    private List<Fragment> mFragments;
    private ViewPager mViewPager;
    private FragmentPagerAdapterRemindPage mFragmentPagerAdapterRemindPage;

    public FragmentRemind() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_remind, container, false);
        mContext = getActivity().getApplicationContext();

        /** Initialize the TAB list and adapters */
        initTabPager(view);

        return view;
    }

    private void initTabPager(View view) {
        mTabLayout = view.findViewById(R.id.remindpage_tablayout);
        mFragments = new ArrayList<Fragment>();
        mViewPager = view.findViewById(R.id.remindpage_viewpager);
        /** Create a fragment and separate it with a tag */
        mFragments.add(new FragmentRemindPager("doing"));
        mFragments.add(new FragmentRemindPager("done"));
        mFragments.add(new FragmentRemindPager("recommend"));
        mFragmentPagerAdapterRemindPage = new FragmentPagerAdapterRemindPage(getChildFragmentManager(),mContext,mFragments);
        mViewPager.setAdapter(mFragmentPagerAdapterRemindPage);
        mTabLayout.setupWithViewPager(mViewPager);
        String[] tabNames = {getString(R.string.mine_minor2_accepted_processing), getActivity().getString(R.string.mine_minor2_accepted_done),getActivity().getString(R.string.recommend)};
        for (int temp = 0; temp < tabNames.length; temp++) {
            mTabLayout.getTabAt(temp).setText(tabNames[temp]);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override

    public void onDetach() {
        super.onDetach();
        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);
        } catch (NoSuchFieldException exp) {
            throw new RuntimeException(exp);
        } catch (IllegalAccessException exp) {
            throw new RuntimeException(exp);
        }
    }
}

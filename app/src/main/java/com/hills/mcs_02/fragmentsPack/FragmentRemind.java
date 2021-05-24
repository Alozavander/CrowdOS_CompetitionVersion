package com.hills.mcs_02.fragmentsPack;

import com.google.android.material.tabs.TabLayout;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;


import com.hills.mcs_02.R;
import com.hills.mcs_02.viewsAdapters.FragmentPagerAdapter_remindPage;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link FragmentRemind#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentRemind extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private static final String TAG = "Fragment_remind";
    private String mParam1;
    private String mParam2;
    private Context mContext;
    private TabLayout mTabLayout;
    private List<Fragment> mFragments;
    private ViewPager mViewPager;
    private FragmentPagerAdapter_remindPage mFragmentPagerAdapterRemindPage;

    public FragmentRemind() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_home.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentRemind newInstance(String param1, String param2) {
        FragmentRemind fragment = new FragmentRemind();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_remind, container, false);
        mContext = getActivity().getApplicationContext();

        //初始化tab列表及适配器
        initTabPager(view);

        return view;
    }

    private void initTabPager(View view) {
        mTabLayout = view.findViewById(R.id.remindpage_tablayout);
        mFragments = new ArrayList<Fragment>();
        mViewPager = view.findViewById(R.id.remindpage_viewpager);
        //创建fragment并通过tag区别
        mFragments.add(new FragmentRemindPager("doing"));
        mFragments.add(new FragmentRemindPager("done"));
        mFragments.add(new FragmentRemindPager("recommend"));
        mFragmentPagerAdapterRemindPage = new FragmentPagerAdapter_remindPage(getChildFragmentManager(),mContext,mFragments);
        mViewPager.setAdapter(mFragmentPagerAdapterRemindPage);
        //tablayout 与 viewpager的联动,在之后需要重新添加tab
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

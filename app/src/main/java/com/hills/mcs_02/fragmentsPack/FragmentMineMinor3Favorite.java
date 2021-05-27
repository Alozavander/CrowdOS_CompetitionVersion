package com.hills.mcs_02.fragmentsPack;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import com.hills.mcs_02.dataBeans.BeanListViewRemind;
import com.hills.mcs_02.R;
import com.hills.mcs_02.viewsAdapters.AdapterListViewRemind;

public class FragmentMineMinor3Favorite extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private Context mContext;
    private ListView mListView;



    public FragmentMineMinor3Favorite() {
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

        View view = inflater.inflate(R.layout.activity_mine_minor3_favorite,container,false);
        mContext = getActivity().getApplicationContext();

        initList(view);

        return view;
    }

    private void initList(View view){
        List<BeanListViewRemind> list = new ArrayList<>();

        mListView = view.findViewById(R.id.minepage_minor3_lv);
        mListView.setAdapter(new AdapterListViewRemind( mContext,list));

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }
}

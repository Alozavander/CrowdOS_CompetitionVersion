package com.hills.mcs_02.fragmentsPack;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import com.hills.mcs_02.activities.ActivityPublishBasicTask;
import com.hills.mcs_02.activities.ActivityPublishSensorTask;
import com.hills.mcs_02.dataBeans.BeanListViewPublish;
import com.hills.mcs_02.ForTest;
import com.hills.mcs_02.R;
import com.hills.mcs_02.viewsAdapters.AdapterListViewPublish;

public class FragmentPublish extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private Context mContext;
    private ListView mListView;
    private AdapterListViewPublish mAdapterListViewPublish;
    private ForTest mForTest;
    private String username;


    public FragmentPublish() {

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

        View view = inflater.inflate(R.layout.fragment_publish_1, container, false);

        /**  Initialize the task list data */
        initTaskList(view);

        return view;
    }

    private void initTaskList(View view) {
        List<BeanListViewPublish> beanList = new ArrayList<>();

        beanList.add(new BeanListViewPublish(getResources().getString(R.string.fragment_publish_template1), getResources().getString(R.string.fragment_publish_template1)));
        beanList.add(new BeanListViewPublish(getResources().getString(R.string.fragment_publish_template2), getResources().getString(R.string.fragment_publish_template2_minor)));

        mListView = (ListView) view.findViewById(R.id.publishpage_modelList);
        mListView.setAdapter(new AdapterListViewPublish(mContext, beanList));
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                username = getActivity().getSharedPreferences("user", mContext.MODE_PRIVATE).getString("userName", "");
                /**  Check if you are logged  */
                if (username.equals("")) {
                    Toast.makeText(mContext,getResources().getString(R.string.notlogin),Toast.LENGTH_SHORT).show();
                    mForTest.jumpToLoginPage();
                } else {
                    /**  Skip to the base publishing page */
                    switch (position){
                        case 0:
                            Intent intentBasic = new Intent(getContext(), ActivityPublishBasicTask.class);
                            startActivity(intentBasic);
                            break;
                        case 1:
                            Intent intentSensor = new Intent(getContext(), ActivityPublishSensorTask.class);
                            startActivity(intentSensor);
                            break;
                    }
                }
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        try {
            mForTest = (ForTest) context;
        } catch (ClassCastException exp) {
            throw new ClassCastException(context.toString() + " must implement For_TestInterface");
        }
    }
}

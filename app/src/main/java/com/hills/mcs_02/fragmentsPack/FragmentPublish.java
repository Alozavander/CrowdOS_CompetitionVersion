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

import com.hills.mcs_02.activities.ActivityPublishBasicTask;
import com.hills.mcs_02.activities.ActivityPublishSensorTask;
import com.hills.mcs_02.dataBeans.Bean_ListView_publish;
import com.hills.mcs_02.For_test;
import com.hills.mcs_02.R;
import com.hills.mcs_02.viewsAdapters.Adapter_ListView_publish;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link FragmentPublish#newInstance} factory method to
 * create an instance of this fragment.
 */

//“发布”分页面
public class FragmentPublish extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Context mContext;                                                                       //当前上下文全局变量，方便Fragment获取布局文件中各个组件及其他活动
    private ListView mListView;                                                                     //
    private Adapter_ListView_publish mAdapterListViewPublish;                                     //呈现任务模板的ListView
    private For_test mForTest;
    private String username;


    public FragmentPublish() {
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
    public static FragmentPublish newInstance(String param1, String param2) {
        FragmentPublish fragment = new FragmentPublish();
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
        View view = inflater.inflate(R.layout.fragment_publish_1, container, false);

        //初始化任务列表数据
        initTaskList(view);

        return view;
    }

    private void initTaskList(View view) {
        List<Bean_ListView_publish> beanList = new ArrayList<>();

        //for setting
        beanList.add(new Bean_ListView_publish(getResources().getString(R.string.fragment_publish_template1), getResources().getString(R.string.fragment_publish_template1)));
        beanList.add(new Bean_ListView_publish(getResources().getString(R.string.fragment_publish_template2), getResources().getString(R.string.fragment_publish_template2_minor)));
        //beanList.add(new Bean_ListView_publish("自定义任务发布模板", "选择需要的传感器"));

        mListView = (ListView) view.findViewById(R.id.publishpage_modelList);
        mListView.setAdapter(new Adapter_ListView_publish(mContext, beanList));
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                username = getActivity().getSharedPreferences("user", mContext.MODE_PRIVATE).getString("userName", "");
                //检查是否登录
                if (username.equals("")) {
                    Toast.makeText(mContext,getResources().getString(R.string.notlogin),Toast.LENGTH_SHORT).show();
                    /*出现了Fragment无法创建对话框的问题
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setTitle("提示");
                    builder.setMessage("登录后才能发布任务");
                    builder.setPositiveButton("登录", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //跳转到登陆界面
                            mFor_test.jump_to_loginPage();
                        }
                    });
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });*/
                    mForTest.jump_to_loginPage();
                } else {
                    //跳转到基础发布页面
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


    //为Fragment之间通信设置的回调Activity的方法
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        // 保证容器Activity实现了回调接口 否则抛出异常警告
        try {
            mForTest = (For_test) context;
        } catch (ClassCastException exp) {
            throw new ClassCastException(context.toString() + " must implement For_TestInterface");
        }
    }
}

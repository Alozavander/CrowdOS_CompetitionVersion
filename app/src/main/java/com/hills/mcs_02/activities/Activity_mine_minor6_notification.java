package com.hills.mcs_02.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.hills.mcs_02.BaseActivity;
import com.hills.mcs_02.R;

import java.util.ArrayList;
import java.util.List;

public class Activity_mine_minor6_notification extends BaseActivity {
    private String TAG = "Activity_mine_minor7_setting";
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_minor7_setting);
        //初始化列表
        initList();
        initBackBT();
    }

    private void initBackBT() {
        ImageView back_im = findViewById(R.id.minepage_minor7_backarrow);
        back_im.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private void initList() {
        List<String> list = new ArrayList<>();

        //测试所用
        list.add(getResources().getString(R.string.setting_general));
        list.add(getResources().getString(R.string.setting_help_feedback));
        list.add(getResources().getString(R.string.setting_version));

        mListView = findViewById(R.id.minepage_minor7_lv);
        mListView.setAdapter(new ArrayAdapter<String>(this,R.layout.listview_item_minepage_minor7,list));
        //列表监听器暂时不管-添加点击 设置 进入设置选择界面
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("------- setting -------");
                System.out.println("position = " + position);
                if(position == 0) {
                    Intent intent = new Intent(Activity_mine_minor6_notification.this, Activity_mine_setting_general.class);
                    startActivity(intent);
                }
                else Toast.makeText(Activity_mine_minor6_notification.this,getResources().getString(R.string.notYetOpen),Toast.LENGTH_SHORT).show();
            }
        });
    }
}

package com.hills.mcs_02.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.hills.mcs_02.BaseActivity;
import com.hills.mcs_02.R;

import java.util.ArrayList;
import java.util.List;

public class ActivityMineSettingGeneral extends BaseActivity {

    private ListView generalListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_setting_general);

        //初始化列表
        initList();
        initBackBT();
    }


    private void initBackBT() {
        ImageView backIv = findViewById(R.id.general_backarrow);
        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    //通用功能列表
    private void initList() {
        //List<String> generalList = new ArrayList<>();
        List<String> generalList = new ArrayList<>();

        generalList.add(getResources().getString(R.string.setting_general_version));

        generalListView = findViewById(R.id.general_list);
        generalListView.setAdapter(new ArrayAdapter<String>(this,R.layout.listview_item_minepage_minor7,generalList));
        //generalListView.setAdapter(new ArrayAdapter<String>(generalContext,R.layout.fragment_mine_setting_general,generalList));

        //点击通用进入通用功能列表界面
        generalListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("------- setting-general -------");
                System.out.println("position = " + position);
                if (position == 0) {
                    Intent intent = new Intent(ActivityMineSettingGeneral.this,
                        ActivityMineSettingGeneralMultiLanguage.class);
                    startActivity(intent);
                }
            }
        });
    }
}

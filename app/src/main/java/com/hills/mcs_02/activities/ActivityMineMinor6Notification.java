package com.hills.mcs_02.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import com.hills.mcs_02.BaseActivity;
import com.hills.mcs_02.R;

public class ActivityMineMinor6Notification extends BaseActivity {
    private String TAG = "Activity_mine_minor7_setting";
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_minor7_setting);
        initList();
        initBackBT();
    }

    private void initBackBT() {
        ImageView backIv = findViewById(R.id.minepage_minor7_backarrow);
        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    private void initList() {
        List<String> list = new ArrayList<>();
        list.add(getResources().getString(R.string.setting_general));
        list.add(getResources().getString(R.string.setting_help_feedback));
        list.add(getResources().getString(R.string.setting_version));

        mListView = findViewById(R.id.minepage_minor7_lv);
        mListView.setAdapter(new ArrayAdapter<String>(this,R.layout.listview_item_minepage_minor7,list));
        /** The list listener is ignored for now - click Settings to enter the Settings selection screen */
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("------- setting -------");
                System.out.println("position = " + position);
                if(position == 0) {
                    Intent intent = new Intent(ActivityMineMinor6Notification.this, ActivityMineSettingGeneral.class);
                    startActivity(intent);
                }
                else Toast.makeText(ActivityMineMinor6Notification.this,getResources().getString(R.string.notYetOpen),Toast.LENGTH_SHORT).show();
            }
        });
    }
}

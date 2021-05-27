package com.hills.mcs_02.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import com.hills.mcs_02.BaseActivity;
import com.hills.mcs_02.viewsAdapters.AdapterRadioBtnTvMultiLanguage;
import com.hills.mcs_02.R;

public class ActivityMineSettingGeneralMultiLanguage extends BaseActivity {
    private ListView languageListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_setting_general_multilan);

        //初始化列表
        initList();
        initBackBtn();
    }

    private void initBackBtn() {
        ImageView backIv = findViewById(R.id.general_multilan_backarrow);
        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
   /** Language selection list Simplified Chinese*/
    private void initList() {
        List<String> languageList = new ArrayList<>();
        languageList.add("简体中文");
        languageList.add("English");

        languageListView = findViewById(R.id.general_multilan_list);
        languageListView.setAdapter(new AdapterRadioBtnTvMultiLanguage(languageList,
            ActivityMineSettingGeneralMultiLanguage.this));

    }
}

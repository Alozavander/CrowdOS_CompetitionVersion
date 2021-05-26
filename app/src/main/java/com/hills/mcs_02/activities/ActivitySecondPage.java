package com.hills.mcs_02.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.hills.mcs_02.BaseActivity;
import com.hills.mcs_02.InterfacePublishedTaskDetail;
import com.hills.mcs_02.R;

//该类作为二级页面启动为Fragment作基石的Activity
public class ActivitySecondPage extends BaseActivity implements InterfacePublishedTaskDetail {

    private FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2rd_page);

        //初始化当前页面的回退按钮
        ImageView backImage = (ImageView) findViewById(R.id.activity_2rd_backarrow);
        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //从MainActivity传来的Intent
        Intent intent = getIntent();
        //获取其附带的信息
        String pageTag = intent.getStringExtra("pageTag");
        int position = intent.getIntExtra("position", -1);
        mFragmentManager = getSupportFragmentManager();

        //加载对应页面
        initFragment(pageTag, position);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    private void initFragment(String page, int position) {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();

        switch (page) {
            case "Fragment_publish": {
                switch (position) {
                    case 1:
                        break;
                    default:
                        break;

                }
                break;
            }
            default:
                break;
        }
        transaction.commit();
        mFragmentManager.executePendingTransactions();

    }



    @Override
    public void jump_to_TaskDetail_Published_Activity(String toJson) {
        Intent intent = new Intent(ActivitySecondPage.this, ActivityTaskDetailPublished.class);
        intent.putExtra("taskGson",toJson);
        startActivity(intent);
    }
}

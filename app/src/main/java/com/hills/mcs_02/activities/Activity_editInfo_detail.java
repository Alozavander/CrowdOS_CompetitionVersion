package com.hills.mcs_02.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hills.mcs_02.BaseActivity;
import com.hills.mcs_02.R;

import java.io.File;

//昵称、电话、简介的编辑页面，需要在页面finish时返回带结果数据的intent
public class Activity_editInfo_detail extends BaseActivity {
    private ListView mListView;
    private static final String TAG = "activity_enditInfo";
    private EditText textEditView;
    private File userIcon_File;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editinfo_detail);

        //初始化信息
        initInfo();

        initBT();
    }

    private void initInfo() {
        TextView tempTitleTV = findViewById(R.id.minepage_editInfo_detail_title);
        //根据intent中kind的键值改变当前页面的标题
        int kind = getIntent().getIntExtra("kind",-1);
        switch (kind){
            //默认值发出提醒
            case -1 :
                Toast.makeText(Activity_editInfo_detail.this,"Info error!", Toast.LENGTH_SHORT);
                break;
            //0为昵称
            case 0 :
                tempTitleTV.setText(getString(R.string.nickname));
                break;
            //1为phone
            case 1 :
                tempTitleTV.setText(getString(R.string.mobilePhone));
                break;
            //2为introduction
            case 2 :
                tempTitleTV.setText(getString(R.string.introduction));
                break;
        }

        textEditView = findViewById(R.id.minepage_editInfo_detail_editview);
        //将获取到的信息放到编辑框中
        textEditView.setText(getIntent().getStringExtra("text"));
    }


    private void initBT() {
        //将回退图片绑定事件监听器
        findViewById(R.id.minepage_editInfo_detail_backerrow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //返回取消code
                Activity_editInfo_detail.this.setResult(Activity.RESULT_CANCELED);
                finish();
            }
        });

        //给confirm绑定点击事件
        Button confirm_bt = findViewById(R.id.minepage_editInfo_detail_confirm_bt);
        confirm_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = textEditView.getText().toString();
                Intent tempIntent = new Intent();
                tempIntent.putExtra("text",text);
                //将intent加入到此acitivity中的result里，便于结束唤起者获取
                Activity_editInfo_detail.this.setResult(Activity.RESULT_OK,tempIntent);
                finish();
            }
        });
    }
}

package com.hills.mcs_02.activities;

import com.bumptech.glide.Glide;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.hills.mcs_02.BaseActivity;
import com.hills.mcs_02.dataBeans.BeanListViewMineEditInfo;
import com.hills.mcs_02.R;
import com.hills.mcs_02.RequestCodes;
import com.hills.mcs_02.taskSubmit.SelectDialog;
import com.hills.mcs_02.viewsAdapters.AdapterListViewMineEditInfo;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;

public class ActivityEditInfo extends BaseActivity {
    private ListView mListView;
    private static final String TAG = "activity_enditInfo";
    private ImageView userIconIv;
    private File userIconFile;
    private AdapterListViewMineEditInfo infoListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editinfo);
        initIconSelect();
        /** Initialize the list */
        initList();
        /** Initialize the logout button */
        initQuitButton();
        /** Initialize the return button */
        initBackArrow();
        /** Initialization complete button */
        initCompleted();
    }

    private void initBackArrow() {
        findViewById(R.id.minepage_editInfo_backerrow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    private void initIconSelect() {
        userIconIv = findViewById(R.id.minepage_editInfo_userIcon);
    }

    private void initQuitButton() {
        Button quitBtn= findViewById(R.id.minepage_editInfo_quit_bt);
        quitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /** Clear user information */
                SharedPreferences.Editor editor = getSharedPreferences("user",MODE_PRIVATE).edit();
                editor.putString("userID","-1");   /** Set the userID to -1 to initialize */
                editor.remove("userName");
                editor.commit();
                String userID = getSharedPreferences("user",MODE_PRIVATE).getString("userID","");
                Log.i(TAG,"userID :   " + String.valueOf(userID));
                /** Send a broadcast that refreshes Fragment_mine */
                Intent intent = new Intent();
                intent.setAction("action_Fragment_mine_userInfo_quit");
                sendBroadcast(intent);
                finish();
            }
        });
    }

    private void initCompleted() {
        TextView completedTv = findViewById(R.id.minepage_editInfo_completed_tv);
        completedTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /** Edit the commit logic */
            }
        });
    }

    private void initList(){
         List<BeanListViewMineEditInfo>list = new ArrayList<>();

        list.add(new BeanListViewMineEditInfo(this.getResources().getString(R.string.nickname),this.getResources().getString(R.string.notYetOpen)));
        list.add(new BeanListViewMineEditInfo(this.getResources().getString(R.string.mobilePhone),this.getResources().getString(R.string.notYetOpen)));
        list.add(new BeanListViewMineEditInfo(this.getResources().getString(R.string.introduction),this.getResources().getString(R.string.notYetOpen)));

        infoListAdapter = new AdapterListViewMineEditInfo(list,this);

        mListView = findViewById(R.id.minepage_editInfo_lv);
        mListView.setAdapter(infoListAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PictureConfig.CHOOSE_REQUEST && resultCode == Activity.RESULT_OK){
            /** Returns a file that you can retrieve directly */
            LocalMedia media = PictureSelector.obtainMultipleResult(data).get(0);
            if(media.getPath() != null)  userIconFile = new File(media.getPath());
            else Toast.makeText(this,"未找到图片",Toast.LENGTH_SHORT).show();
            Glide.with(this).load(userIconFile).into(userIconIv);
        }
        if(requestCode == RequestCodes.INTENT_RC_EDIT_INFO_NICKNAME && resultCode == Activity.RESULT_OK){
            String nickName = data.getStringExtra("text");
            /** Change the data list information using methods built into the adapter */
            infoListAdapter.textChange(0,nickName);
        }
        if(requestCode == RequestCodes.INTENT_RC_EDIT_INFO_PHONE && resultCode == Activity.RESULT_OK){
            String phone = data.getStringExtra("text");
            infoListAdapter.textChange(1,phone);
        }
        if(requestCode == RequestCodes.INTENT_RC_EDIT_INFO_INTRODUCTION && resultCode == Activity.RESULT_OK){
            String introduction = data.getStringExtra("text");
            infoListAdapter.textChange(2,introduction);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}

package com.hills.mcs_02.activities;

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

import com.bumptech.glide.Glide;
import com.hills.mcs_02.BaseActivity;
import com.hills.mcs_02.R;
import com.hills.mcs_02.RequestCodes;
import com.hills.mcs_02.dataBeans.Bean_ListView_mine_editInfo;
import com.hills.mcs_02.taskSubmit.SelectDialog;
import com.hills.mcs_02.viewsAdapters.AdapterListViewMineEditInfo;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ActivityEditInfo extends BaseActivity {
    private ListView mListView;
    private static final String TAG = "activity_enditInfo";
    private ImageView userIconIV;
    private File userIconFile;
    private AdapterListViewMineEditInfo infoListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editinfo);
        initIconSelect();
        //初始化列表
        initList();
        //初始化退出登录按钮
        initQuitButton();
        //初始化返回按钮
        initBackArrow();
        //初始化“完成”按钮
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
        userIconIV = findViewById(R.id.minepage_editInfo_userIcon);
        /*userIcon_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> choiceList = new ArrayList<String>();
                choiceList.add(getString(R.string.updateUserIcon));
                showDialog(new SelectDialog.SelectDialogListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if(position == 0){
                            PictureSelector.create(Activity_editInfo.this)
                                    .openGallery(PictureMimeType.ofImage()) //打开图库（只显示图片）
                                    .maxSelectNum(1)   //只能选一张图片
                                    .isCamera(false)   //不显示照相机图案
                                    .forResult(PictureConfig.CHOOSE_REQUEST);
                        }
                    }
                }, choiceList);
            }
        });*/
    }


    private SelectDialog showDialog(SelectDialog.SelectDialogListener listener, List<String> names) {
        SelectDialog dialog = new SelectDialog(this, R.style.transparentFrameWindowStyle, listener, names);
        //从activity的isFinishing转换而来，测试点
        if (!this.isFinishing()) {
            dialog.show();
        }
        return dialog;
    }

    private void initQuitButton() {
        Button quitBtn= findViewById(R.id.minepage_editInfo_quit_bt);
        quitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //清除用户信息
                SharedPreferences.Editor editor = getSharedPreferences("user",MODE_PRIVATE).edit();
                editor.putString("userID","-1");   //userID设置为-1初始化
                editor.remove("userName");
                editor.commit();
                String userID = getSharedPreferences("user",MODE_PRIVATE).getString("userID","");
                Log.i(TAG,"userID :   " + String.valueOf(userID));
                //发送刷新Fragment_mine的广播
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
                //编写提交信息编辑提交逻辑


            }
        });
    }

    private void initList(){
         List<Bean_ListView_mine_editInfo>list = new ArrayList<>();

        //测试所用
        list.add(new Bean_ListView_mine_editInfo(this.getResources().getString(R.string.nickname),this.getResources().getString(R.string.notYetOpen)));
        list.add(new Bean_ListView_mine_editInfo(this.getResources().getString(R.string.mobilePhone),this.getResources().getString(R.string.notYetOpen)));
        list.add(new Bean_ListView_mine_editInfo(this.getResources().getString(R.string.introduction),this.getResources().getString(R.string.notYetOpen)));

        infoListAdapter = new AdapterListViewMineEditInfo(list,this);

        mListView = findViewById(R.id.minepage_editInfo_lv);
        mListView.setAdapter(infoListAdapter);
        //列表监听器暂时不管
        /*mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(Activity_editInfo.this, Activity_editInfo_detail.class);
                //加入类别信息，便于下一个Activity显示对应的标题
                intent.putExtra("kind",position);
                //将内容栏中的string传递到下一个activity中
                intent.putExtra("text",list.get(position).getContent());
                //使用request code跳转
                startActivityForResult(intent, RequestCodes.Intent_RC_editInfo_nickName + position);
            }
        });*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //对code进行辨识
        if(requestCode == PictureConfig.CHOOSE_REQUEST && resultCode == Activity.RESULT_OK){
            //返回一个文件，直接获取
            LocalMedia media = PictureSelector.obtainMultipleResult(data).get(0);
            if(media.getPath() != null)  userIconFile = new File(media.getPath());
            else Toast.makeText(this,"未找到图片",Toast.LENGTH_SHORT).show();
            Glide.with(this).load(userIconFile).into(userIconIV);
        }
        if(requestCode == RequestCodes.INTENT_RC_EDITINFO_NICKNAME && resultCode == Activity.RESULT_OK){
            String nickName = data.getStringExtra("text");
            //通过adapter内置的方法改变资料列表信息
            infoListAdapter.textChange(0,nickName);
        }
        if(requestCode == RequestCodes.INTENT_RC_EDITINFO_PHONE && resultCode == Activity.RESULT_OK){
            String phone = data.getStringExtra("text");
            infoListAdapter.textChange(1,phone);
        }
        if(requestCode == RequestCodes.INTENT_RC_EDITINFO_INTRODUCTION && resultCode == Activity.RESULT_OK){
            String introduction = data.getStringExtra("text");
            infoListAdapter.textChange(2,introduction);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}

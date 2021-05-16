package com.hills.mcs_02.activities;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.hills.mcs_02.R;
import com.hills.mcs_02.StringStore;
import com.hills.mcs_02.fragmentsPack.MCS_RecyclerItemClickListener;
import com.hills.mcs_02.saveFile.FileExport;
import com.hills.mcs_02.sensorFunction.SensorSQLiteOpenHelper;
import com.hills.mcs_02.viewsAdapters.Adapter_RecyclerView_setting_sensorData;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Activity_mine_minor5_sensorData extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private Adapter_RecyclerView_setting_sensorData mAdapter;
    private List<String[]> mList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_minor5_sensordata);

        initData();
        initViews();
    }

    private void initData() {
        //menuContent_1:查看感知数据；menuContent_2:删除数据；menuContent_3：开启/关闭感知；menuContent_4：保存文件
        String[] menuContent_1 = new String[2];
        String[] menuContent_2 = new String[2];
        String[] menuContent_3 = new String[2];
        String[] menuContent_4 = new String[2];
        menuContent_1[0] = getString(R.string.setting_sensorData_chuanganqishuju);
        Cursor c = new SensorSQLiteOpenHelper(this).getReadableDatabase().query(StringStore.SensorDataTable_Name,
                new String[]{StringStore.SensorDataTable_SenseType,
                        StringStore.SensorDataTable_SenseTime,
                        StringStore.SensorDataTable_SenseData_1,
                        StringStore.SensorDataTable_SenseData_2,
                        StringStore.SensorDataTable_SenseData_3},
                null, null, null, null, null);
        menuContent_1[1] = getString(R.string.setting_sensorData_dangqianshujushuliang) + "  " + c.getCount();
        c.close();
        menuContent_2[0] = getString(R.string.setting_sensorData_qingliganzhishuju);
        menuContent_2[1] = null; //为null时在adapter中会把对应的次级内容组件隐藏

        menuContent_3[0] = getString(R.string.setting_sensorData_baocunshuju);
        menuContent_3[1] = null; //为null时在adapter中会把对应的次级内容组件隐藏

        menuContent_4[0] = getString(R.string.setting_sensorData_kaiqiguanbiganzhi);
        menuContent_4[1] = null; //为null时在adapter中会把对应的次级内容组件隐藏

        mList = new ArrayList<>();
        mList.add(menuContent_1);
        mList.add(menuContent_2);
        mList.add(menuContent_3);
 /*       mList.add(menuContent_4);*/
    }

    private void initViews() {
        findViewById(R.id.setting_sensorData_back).setOnClickListener(this);
        mRecyclerView = findViewById(R.id.setting_sensorData_rv);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));
        mAdapter = new Adapter_RecyclerView_setting_sensorData(this,mList);
        mAdapter.setRecyclerItemClickListener(new MCS_RecyclerItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //添加跳转事件
                if(position == 0) {
                    startActivity(new Intent(Activity_mine_minor5_sensorData.this,Activity_mine_minor5_sensorData_sensorContent.class));
                }
                else if(position == 1){
                    startActivity(new Intent(Activity_mine_minor5_sensorData.this,Activity_mine_minor5_sensorData_Delete.class));
                }
                else if(position == 2){
                    Cursor c = new SensorSQLiteOpenHelper(Activity_mine_minor5_sensorData.this).getReadableDatabase().query(StringStore.SensorDataTable_Name,
                            new String[]{StringStore.SensorDataTable_SenseType,
                                    StringStore.SensorDataTable_SenseTime,
                                    StringStore.SensorDataTable_SenseData_1,
                                    StringStore.SensorDataTable_SenseData_2,
                                    StringStore.SensorDataTable_SenseData_3},
                            null, null, null, null, null);
                    File saveFile = FileExport.ExportToCSV(c,null, null);
                    Toast.makeText(Activity_mine_minor5_sensorData.this,"Output finishing. The file path is :" + saveFile.getAbsolutePath(),Toast.LENGTH_LONG).show();
                    c.close();
                }
            }
        });
        mRecyclerView.setAdapter(mAdapter);
        mSwipeRefreshLayout = findViewById(R.id.setting_sensorData_swiperefreshLayout);
        mSwipeRefreshLayout.setColorSchemeColors(Color.RED,Color.BLUE, Color.GREEN);
        //绑定列表事件点击
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.setting_sensorData_back:
                finish();
                break;
        }
    }
}

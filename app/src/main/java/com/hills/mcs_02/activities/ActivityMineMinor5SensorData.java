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


import com.hills.mcs_02.fragmentsPack.MCSRecyclerItemClickListener;
import com.hills.mcs_02.R;
import com.hills.mcs_02.StringStore;
import com.hills.mcs_02.saveFile.FileExport;
import com.hills.mcs_02.sensorFunction.SensorSqliteOpenHelper;
import com.hills.mcs_02.viewsAdapters.AdapterRecyclerViewSettingSensorData;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ActivityMineMinor5SensorData extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private AdapterRecyclerViewSettingSensorData mAdapter;
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
        String[] menuContent1 = new String[2];
        String[] menuContent2 = new String[2];
        String[] menuContent3 = new String[2];
        String[] menuContent4 = new String[2];
        menuContent1[0] = getString(R.string.setting_sensorData_chuanganqishuju);
        Cursor c = new SensorSqliteOpenHelper(this).getReadableDatabase().query(StringStore.SENSOR_DATATABLE_NAME,
                new String[]{StringStore.SENSOR_DATATABLE_SENSE_TYPE,
                        StringStore.SENSOR_DATATABLE_SENSE_TIME,
                        StringStore.SENSOR_DATATABLE_SENSE_DATA_1,
                        StringStore.SENSOR_DATATABLE_SENSE_DATA_2,
                        StringStore.SENSOR_DATATABLE_SENSE_DATA_3},
                null, null, null, null, null);
        menuContent1[1] = getString(R.string.setting_sensorData_dangqianshujushuliang) + "  " + c.getCount();
        c.close();
        menuContent2[0] = getString(R.string.setting_sensorData_qingliganzhishuju);
        menuContent2[1] = null; //为null时在adapter中会把对应的次级内容组件隐藏

        menuContent3[0] = getString(R.string.setting_sensorData_baocunshuju);
        menuContent3[1] = null; //为null时在adapter中会把对应的次级内容组件隐藏

        menuContent4[0] = getString(R.string.setting_sensorData_kaiqiguanbiganzhi);
        menuContent4[1] = null; //为null时在adapter中会把对应的次级内容组件隐藏

        mList = new ArrayList<>();
        mList.add(menuContent1);
        mList.add(menuContent2);
        mList.add(menuContent3);
 /*       mList.add(menuContent_4);*/
    }

    private void initViews() {
        findViewById(R.id.setting_sensorData_back).setOnClickListener(this);
        mRecyclerView = findViewById(R.id.setting_sensorData_rv);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));
        mAdapter = new AdapterRecyclerViewSettingSensorData(this,mList);
        mAdapter.setRecyclerItemClickListener(new MCSRecyclerItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //添加跳转事件
                if(position == 0) {
                    startActivity(new Intent(ActivityMineMinor5SensorData.this,ActivityMineMinor5SensorDataSensorContent.class));
                }
                else if(position == 1){
                    startActivity(new Intent(ActivityMineMinor5SensorData.this,ActivityMineMinor5SensorDataDelete.class));
                }
                else if(position == 2){
                    Cursor c = new SensorSqliteOpenHelper(ActivityMineMinor5SensorData.this).getReadableDatabase().query(StringStore.SENSOR_DATATABLE_NAME,
                            new String[]{StringStore.SENSOR_DATATABLE_SENSE_TYPE,
                                    StringStore.SENSOR_DATATABLE_SENSE_TIME,
                                    StringStore.SENSOR_DATATABLE_SENSE_DATA_1,
                                    StringStore.SENSOR_DATATABLE_SENSE_DATA_2,
                                    StringStore.SENSOR_DATATABLE_SENSE_DATA_3},
                            null, null, null, null, null);
                    File saveFile = FileExport.exportToCSV(c,null, null);
                    Toast.makeText(ActivityMineMinor5SensorData.this,"Output finishing. The file path is :" + saveFile.getAbsolutePath(),Toast.LENGTH_LONG).show();
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
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.setting_sensorData_back:
                finish();
                break;
        }
    }
}

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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.hills.mcs_02.fragmentsPack.MCSRecyclerItemClickListener;
import com.hills.mcs_02.R;
import com.hills.mcs_02.StringStore;
import com.hills.mcs_02.saveFile.FileExport;
import com.hills.mcs_02.sensorFunction.SensorSQLiteOpenHelper;
import com.hills.mcs_02.viewsAdapters.AdapterRecyclerViewSettingSensorData;

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
        String[] menuContent1 = new String[2];/** View perceptual data*/
        String[] menuContent2 = new String[2];/** delete data */
        String[] menuContent3 = new String[2];/** Turn awareness on/off */
        String[] menuContent4 = new String[2];/* save file */
        menuContent1[0] = getString(R.string.setting_sensorData_chuanganqishuju);
        Cursor cur = new SensorSQLiteOpenHelper(this).getReadableDatabase().query(StringStore.SENSOR_DATATABLE_NAME,
                new String[]{StringStore.SENSOR_DATATABLE_SENSE_TYPE,
                        StringStore.SENSOR_DATATABLE_SENSE_TIME,
                        StringStore.SENSOR_DATATABLE_SENSE_DATA_1,
                        StringStore.SENSOR_DATATABLE_SENSE_DATA_2,
                        StringStore.SENSOR_DATATABLE_SENSE_DATA_3},
                null, null, null, null, null);
        menuContent1[1] = getString(R.string.setting_sensorData_dangqianshujushuliang) + "  " + cur.getCount();
        cur.close();
        menuContent2[0] = getString(R.string.setting_sensorData_qingliganzhishuju);
        menuContent2[1] = null; /** If NULL, the corresponding secondary content component is hidden in the Adapter */

        menuContent3[0] = getString(R.string.setting_sensorData_baocunshuju);
        menuContent3[1] = null; /** If NULL, the corresponding secondary content component is hidden in the Adapter */

        menuContent4[0] = getString(R.string.setting_sensorData_kaiqiguanbiganzhi);
        menuContent4[1] = null; /** If NULL, the corresponding secondary content component is hidden in the Adapter */

        mList = new ArrayList<>();
        mList.add(menuContent1);
        mList.add(menuContent2);
        mList.add(menuContent3);
    }

    private void initViews() {
        findViewById(R.id.setting_sensorData_back).setOnClickListener(this);
        mRecyclerView = findViewById(R.id.setting_sensorData_rv);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));
        mAdapter = new AdapterRecyclerViewSettingSensorData(this,mList);
        mAdapter.setRecyclerItemClickListener(new MCSRecyclerItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                /** Add a jump event */
                if(position == 0) {
                    startActivity(new Intent(ActivityMineMinor5SensorData.this,ActivityMineMinor5SensorDataSensorContent.class));
                }
                else if(position == 1){
                    startActivity(new Intent(ActivityMineMinor5SensorData.this,ActivityMineMinor5SensorDataDelete.class));
                }
                else if(position == 2){
                    Cursor cur = new SensorSQLiteOpenHelper(ActivityMineMinor5SensorData.this).getReadableDatabase().query(StringStore.SENSOR_DATATABLE_NAME,
                            new String[]{StringStore.SENSOR_DATATABLE_SENSE_TYPE,
                                    StringStore.SENSOR_DATATABLE_SENSE_TIME,
                                    StringStore.SENSOR_DATATABLE_SENSE_DATA_1,
                                    StringStore.SENSOR_DATATABLE_SENSE_DATA_2,
                                    StringStore.SENSOR_DATATABLE_SENSE_DATA_3},
                            null, null, null, null, null);
                    File saveFile = FileExport.exportToCSV(cur,null, null);
                    Toast.makeText(ActivityMineMinor5SensorData.this,"Output finishing. The file path is :" + saveFile.getAbsolutePath(),Toast.LENGTH_LONG).show();
                    cur.close();
                }
            }
        });
        mRecyclerView.setAdapter(mAdapter);
        mSwipeRefreshLayout = findViewById(R.id.setting_sensorData_swiperefreshLayout);
        mSwipeRefreshLayout.setColorSchemeColors(Color.RED,Color.BLUE, Color.GREEN);
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

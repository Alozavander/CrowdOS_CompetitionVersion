package com.hills.mcs_02.activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


import com.hills.mcs_02.fragmentsPack.MCSRecyclerItemClickListener;
import com.hills.mcs_02.R;
import com.hills.mcs_02.SenseDataDisplay.SQLiteDataDisplay;
import com.hills.mcs_02.StringStore;
import com.hills.mcs_02.sensorFunction.SenseHelper;
import com.hills.mcs_02.sensorFunction.SensorSQLiteOpenHelper;
import com.hills.mcs_02.viewsAdapters.AdapterRecyclerViewSettingSensorData;

public class ActivityMineMinor5SensorDataSensorContent extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView mRecyclerView;
    //private SwipeRefreshLayout mSwipeRefreshLayout;
    private AdapterRecyclerViewSettingSensorData mAdapter;
    private List<String[]> mList;
    public String[] mSensorS;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_minor5_sensor_data_sensor_content);

        initData();
        initViews();
    }

    private void initData() {
        /** Get the list of sensors using SenseHelper */
        mSensorS = new SenseHelper(this).getSensorListTypeIntStrings();
        mList = new ArrayList<>();
        for(String s : mSensorS) {
            int sensorType = SenseHelper.sensorType2XmlName(this,s);
            Cursor lCursor = new SensorSQLiteOpenHelper(this).getReadableDatabase().query(StringStore.SENSOR_DATATABLE_NAME,
                    new String[]{StringStore.SENSOR_DATATABLE_SENSE_TYPE,
                            StringStore.SENSOR_DATATABLE_SENSE_TIME,
                            StringStore.SENSOR_DATATABLE_SENSE_DATA_1,
                            StringStore.SENSOR_DATATABLE_SENSE_DATA_2,
                            StringStore.SENSOR_DATATABLE_SENSE_DATA_3},
                    StringStore.SENSOR_DATATABLE_SENSE_TYPE + "=?", new String[]{sensorType+""}, null, null, null);
            String lNum =  getString(R.string.setting_sensorData_dangqianshujushuliang) + "  " + lCursor.getCount();
            mList.add(new String[]{s,lNum});
            lCursor.close();
        }
    }

    private void initViews() {
        findViewById(R.id.setting_sensorData_sensorContent_back).setOnClickListener(this);
        mRecyclerView = findViewById(R.id.setting_sensorData_sensorContent_rv);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));
        mAdapter = new AdapterRecyclerViewSettingSensorData(this,mList);
        mAdapter.setRecyclerItemClickListener(new MCSRecyclerItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                /** Add a jump event*/
                Intent lIntent = new Intent(ActivityMineMinor5SensorDataSensorContent.this, SQLiteDataDisplay.class);
                lIntent.putExtra("sensorName",mSensorS[position]);
                startActivity(lIntent);
            }
        });
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.setting_sensorData_sensorContent_back:
                finish();
                break;
        }
    }

}

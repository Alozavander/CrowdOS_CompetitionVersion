package com.hills.mcs_02.activities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hills.mcs_02.BaseActivity;
import com.hills.mcs_02.R;

import java.io.File;

//这个的位置
import senseInfoClasses.SenseInfo;

//该类作为二级页面启动为Fragment作基石的Activity
public class ActivityTestDataStore extends BaseActivity {

    //测试用的数据库操作类
    private SQLiteDatabase testDb;
    //自己封装的传感器获取信息类
    private SenseInfo mSenseInfo;

    private EditText mEt;
    private TextView mTv1;
    private TextView mTv2;
    private Button dbBtn;
    private Button getDataBtn;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_datastore);
        mContext = this;
        /** Get the SQLiteDatabase object through the static method */
        File file = new File(mContext.getFilesDir().toString() + File.separator  + "test");
        if(!file.exists()){
            file.mkdirs();
        }
        testDb = SQLiteDatabase.openOrCreateDatabase(file.getPath() + File.separator + "task_test_1.db", null);

        /**  Initialize other views */
        dbBtn = (Button) findViewById(R.id.activity_testDB_btn);
        getDataBtn = (Button) findViewById(R.id.activity_test_sensor_btn);
        mEt = (EditText) findViewById(R.id.activity_taskSub_et);
        mTv1 = (TextView) findViewById(R.id.activity_testDB_tv);
        mTv2 = (TextView) findViewById(R.id.activity_test_sensor_tv);

        /**  Initialize the wrapper class for sensor information */
        mSenseInfo = new SenseInfo(this,testDb,file.getPath());

        getDataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sensorName = mEt.getText().toString();
                if (sensorName.equals("GPS")) {
                    double[] location = mSenseInfo.getLocationInfo();
                } else if (sensorName == null || sensorName.equals("")) {
                    Toast.makeText(mContext, getResources().getString(R.string.InputBoxNull), Toast.LENGTH_SHORT).show();
                } else {
                    mSenseInfo.getSensorData(sensorName);
                }
            }
        });

        dbBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        initDb();

    }

    private void initDb() {
        /** Create a table and add a table to it */
        boolean exit = false;
        String sql = "select count(*) as c from sqlite_master where type ='table' and name ='task';";
        Cursor cursor = testDb.rawQuery(sql, null);
        if (cursor.moveToNext()) {
            int count = cursor.getInt(0);
            if (count > 0) {
                exit = true;
            }
        }

        /** create a table that does not exist */
        if(!exit){
            String init = "create table task(taskID integer primary key,taskName text,postTime text)";
            testDb.execSQL(init);
            insertValues();
        }else{
            Toast.makeText(this,getResources().getString(R.string.DataTableHasBeenCreated),Toast.LENGTH_SHORT);
        }

        getSensorList();
    }

    private void insertValues() {
        /** Attempt to insert built-in task data */
        ContentValues valueTask1 = new ContentValues();
        valueTask1.put("taskID", 0001);
        valueTask1.put("taskN", "测试用任务0001");
        valueTask1.put("postTime", "2011.8.12");
        ContentValues valueTask2 = new ContentValues();
        valueTask2.put("taskID", 0002);
        valueTask2.put("taskN", "测试用任务0002");
        valueTask1.put("postTime", "2019.1.2");
        testDb.insert("task", null, valueTask1);
        testDb.insert("task", null, valueTask2);
       /** The table created successfully */
        Toast.makeText(this, "Create DataBase Table Successfully", Toast.LENGTH_LONG).show();
    }

    private void getSensorList() {
        mTv1.setText(mSenseInfo.getSensorListContent());
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        testDb.close();
        mSenseInfo.clean();
    }
}

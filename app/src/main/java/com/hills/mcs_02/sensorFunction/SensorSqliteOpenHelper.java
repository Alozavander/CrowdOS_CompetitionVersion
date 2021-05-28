package com.hills.mcs_02.sensorFunction;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.hills.mcs_02.StringStore;


/*
 *  SQLite打开辅助类
 *  专门为Sensor存储感知数据设立
 *  功能：根据传入的SensorType(依照SenseHelper中对SensorType存储到SP的格式进行解析)分别为当前设备支持的各类传感器创建对应Table
 */
public class SensorSQLiteOpenHelper extends SQLiteOpenHelper {

    public final String senseTime = "senseTime";
    public final String sNumber = "snumber";
    private String types;


    public SensorSQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }


    public SensorSQLiteOpenHelper(Context context) {
        super(context, StringStore.SENSOR_DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table if not exists "
                + StringStore.SENSOR_DATATABLE_NAME + "("
                + StringStore.SENSOR_DATATABLE_ID + " integer primary key autoincrement,"
                + StringStore.SENSOR_DATATABLE_SENSE_TYPE + " integer,"
                + StringStore.SENSOR_DATATABLE_SENSE_TIME + " text,"
                + StringStore.SENSOR_DATATABLE_SENSE_DATA_1 + " text,"
                + StringStore.SENSOR_DATATABLE_SENSE_DATA_2 + " text,"
                + StringStore.SENSOR_DATATABLE_SENSE_DATA_3 + " text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

package com.hills.mcs_02.sensorFunction;

import static android.widget.Toast.LENGTH_LONG;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.hills.mcs_02.StringStore;
import com.hills.mcs_02.saveFile.FileExport;

import java.io.File;

/*
 * Edit by Liao JiaHao
 * Time: 2020.7.9
 * Description : SenseFunction整个功能模块的接口集成类
 * 功能：
 * 1. 开启/关闭SenseService
 * 2. 开启/关闭对应传感器感知
 * 3. 查看感知数据
 * 4. 删除感知数据
 * 5. 保存感知数据——>csv文件(自定义路径)
 *
 */
public class SenseFunction {
    private static final String TAG = "SenseFunction";
    private Context mContext;
    private SensorServiceInterface sensorServiceInterface;
    private boolean isBind;
    private ServiceConnection conn;
    public SenseHelper senseHelper;
    public SQLiteDatabase mReadableDatabase;

    /* 绑定/启动Service服务的Activity的Context */
    public SenseFunction(Context pContext) {
        mContext = pContext;
    }

    /* 开启SenseService
     */
    public void onSenseService() {
        Log.i(TAG, "=======Now Init the sensor Service...===========");
        //初始化传感器感知服务Service
        senseHelper = new SenseHelper(mContext);
        Intent intent = new Intent(mContext, SensorService.class);
        if (conn == null) {
            Log.i(TAG, "===========connection creating...============");
            conn = new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName name, IBinder service) {

                    sensorServiceInterface = (SensorServiceInterface) service;
                    Log.i(TAG, "sensorService_interface connection is done.");
                }

                @Override
                public void onServiceDisconnected(ComponentName name) {
                    Log.i(TAG, "sensorService disconnected.");
                }
            };
        } else {
            Log.i(TAG, "===============sensorService connection exits.================");
        }
        //bind the Service
        isBind = mContext.getApplicationContext().bindService(intent, conn, Context.BIND_AUTO_CREATE);
        Log.i(TAG, "=============SensorService has been bound :" + isBind + "==============");
    }

    /* 关闭SenseService
     * 请在开启service的代码结构中的onDestroy方法加入此方法
     */
    public void offSenseService() {
        if (isBind) {
            isBind = false;
            mContext.getApplicationContext().unbindService(conn);
        }
    }

    /* 开启传感器感知
     * 由sensorType_array指定开启的传感器类型
     */
    public void onSensor(int[] sensorTypeArray) {
        if (isBind) {
            if (sensorServiceInterface != null) {
                sensorServiceInterface.binderSensorOn(sensorTypeArray);
                Log.i(TAG, "SensorService's sensorOn has been remote.");
            } else {
                Toast.makeText(mContext, "sensorService_interface is null. Please init SenseService use On_SenseService method.", LENGTH_LONG).show();
                Log.i(TAG, "sensorService_interface is null. Please init SenseService use On_SenseService method.");
            }
        } else {
            Toast.makeText(mContext, "SensorService has been bound :" + isBind + ". Please  init SenseService use On_SenseService method.", LENGTH_LONG).show();
            Log.i(TAG, "SensorService has been bound :" + isBind + ". Please  init SenseService use On_SenseService method.");
        }
    }

    /* 关闭传感器感知
     * 由sensorType_array指定关闭的传感器类型
     */
    public void offSensor(int[] sensorTypeArray) {
        if (isBind) {
            if (sensorServiceInterface != null) {
                sensorServiceInterface.binderSensorOff(sensorTypeArray);
                Log.i(TAG, "SensorService's sensorOff has been remote.");
            } else {
                Toast.makeText(mContext, "sensorService_interface is null. Please init SenseService use On_SenseService method.", LENGTH_LONG).show();
                Log.i(TAG, "sensorService_interface is null. Please init SenseService use On_SenseService method.");
            }
        } else {
            Toast.makeText(mContext, "SensorService has been bound :" + isBind + ". Please  init SenseService use On_SenseService method.", LENGTH_LONG).show();
            Log.i(TAG, "SensorService has been bound :" + isBind + ". Please  init SenseService use On_SenseService method.");
        }
    }

    /* 查询传感器数据
     * 返回对应的cursor
     * cursor使用完成后应当使用close讲它关闭
     */
    public Cursor querySenseData(int pSensorType) {
        SQLiteDatabase db = new SensorSQLiteOpenHelper(mContext).getReadableDatabase();
        Cursor cur = db.query(StringStore.SENSOR_DATATABLE_NAME,
                new String[]{StringStore.SENSOR_DATATABLE_ID,
                        StringStore.SENSOR_DATATABLE_SENSE_TYPE,
                        StringStore.SENSOR_DATATABLE_SENSE_TIME,
                        StringStore.SENSOR_DATATABLE_SENSE_DATA_1,
                        StringStore.SENSOR_DATATABLE_SENSE_DATA_2,
                        StringStore.SENSOR_DATATABLE_SENSE_DATA_3},
                StringStore.SENSOR_DATATABLE_SENSE_TYPE + "=?", new String[]{pSensorType + ""}, null, null, null);
        return cur;
    }

    /* 删除传感器数据
     * 返回删除的数据数量，-1为出错，如果startTime和endTime为null，则该传感器类型的感知数据全部删除
     */
    public int sqliteDelete(int sensorType, String startTime, String endTime) {
        SQLiteDatabase db = new SensorSQLiteOpenHelper(mContext).getReadableDatabase();
        int lI = -1;
        //四种时间不同的情况
        if (startTime == null && endTime != null) {
            //whereclaus
            String whereClaus = StringStore.SENSOR_DATATABLE_SENSE_TYPE + "=?" + " AND " + StringStore.SENSOR_DATATABLE_SENSE_TIME
                + " < ?";
            lI = db.delete(StringStore.SENSOR_DATATABLE_NAME,
                    whereClaus, new String[]{sensorType + "", endTime});
            Log.e(TAG, "Where Claus is : " + whereClaus);
            Log.e(TAG, "EndTime is : " + endTime);
            Log.e(TAG, "Delete result : " + lI);
        } else if (startTime != null && endTime == null) {
            String whereClaus = StringStore.SENSOR_DATATABLE_SENSE_TYPE + "=?" + " AND " + StringStore.SENSOR_DATATABLE_SENSE_TIME
                + " > ?";
            lI = db.delete(StringStore.SENSOR_DATATABLE_NAME,
                    whereClaus, new String[]{sensorType + "", startTime});
            Log.e(TAG, "Where Claus is : " + whereClaus);
            Log.e(TAG, "StartTime is : " + startTime);
            Log.e(TAG, "Delete result : " + lI);
        } else if (startTime == null && endTime == null) {
            String whereClaus = StringStore.SENSOR_DATATABLE_SENSE_TYPE + "=?";
            lI = db.delete(StringStore.SENSOR_DATATABLE_NAME,
                    whereClaus, new String[]{sensorType + ""});
            Log.e(TAG, "Where Claus is : " + whereClaus);
            Log.e(TAG, "Delete result : " + lI);
        } else {
            String whereClaus = StringStore.SENSOR_DATATABLE_SENSE_TYPE + "=?" + " AND " + StringStore.SENSOR_DATATABLE_SENSE_TIME
                + " > ? AND " + StringStore.SENSOR_DATATABLE_SENSE_TIME + " < ?";
            lI = db.delete(StringStore.SENSOR_DATATABLE_NAME,
                    whereClaus, new String[]{sensorType + "", startTime, endTime});
            Log.e(TAG, "Where Claus is : " + whereClaus);
            Log.e(TAG, "StartTime is : " + startTime);
            Log.e(TAG, "EndTime is : " + endTime);
            Log.e(TAG, "Delete result : " + lI);
        }
        db.close();
        return lI;
    }

    /* 保存成文件
     * 返回保存的File类
     * 文件默认名为senseData.csv，默认路径为sd/SensorDataStore/senseData.csv
     * 自定义路径请检查父文件夹不存在的错误
     */
    public File storeDataToCSV(int sensorType, String fileName, String fileParentPath) {
        File saveFile;
        Cursor cur = new SensorSQLiteOpenHelper(mContext).getReadableDatabase().query(StringStore.SENSOR_DATATABLE_NAME,
                new String[]{StringStore.SENSOR_DATATABLE_SENSE_TYPE,
                        StringStore.SENSOR_DATATABLE_SENSE_TIME,
                        StringStore.SENSOR_DATATABLE_SENSE_DATA_1,
                        StringStore.SENSOR_DATATABLE_SENSE_DATA_2,
                        StringStore.SENSOR_DATATABLE_SENSE_DATA_3},
                StringStore.SENSOR_DATATABLE_SENSE_TYPE + "=?", new String[]{sensorType+""}, null, null, null);
        saveFile = FileExport.exportToCSV(cur, fileName, fileParentPath);
        Toast.makeText(mContext, "Output finishing. The file path is :" + saveFile.getAbsolutePath(), Toast.LENGTH_LONG).show();
        return saveFile;
    }
}

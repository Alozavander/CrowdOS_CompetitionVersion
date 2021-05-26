package senseInfoClasses;

import android.app.Service;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

import java.io.File;

public class ServiceSenseData extends Service {
    public static final String TAG = "Service_senseData" ;
    public static final String PATH_ROOT = Environment.getExternalStorageDirectory() + "/MCS/";

    //测试用的数据库操作类
    private SQLiteDatabase mcsDb;
    //自己封装的传感器获取信息类
    private SenseInfo mSenseInfo;



    @Override
    public void onCreate() {
        String dbPath = getCacheDir().toString() + "sensorDataBaseData" + File.separator + "sensorData.db";
        String logFilePath = PATH_ROOT + "SensorData.log";
        //创建数据库文件存储地址,在cache中查找
        File dbFile = new File(dbPath);
        if(!dbFile.exists()){
            dbFile.mkdirs();
        }
        //初始化或者打开数据库
        mcsDb = SQLiteDatabase.openOrCreateDatabase(dbPath, null);
        Log.e(TAG,"打开了sensorData.db数据库");
        //初始化封装的传感器信息获取类
        mSenseInfo = new SenseInfo(this, mcsDb,logFilePath);
        Log.e(TAG,"已开启传感器感知服务");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }





    @Override
    public void onDestroy() {
        super.onDestroy();
        mcsDb.close();
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}

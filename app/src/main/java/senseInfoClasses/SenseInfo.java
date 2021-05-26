package senseInfoClasses;

import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.Manifest;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class SenseInfo {
    public static final String TAG = "SenseInfo";

    private Context context;
    private double[] locationInfo;
    private float[][] sensorData;
    private List<Sensor> sensorList;
    private SQLiteDatabase db;
    //sense data
    private SensorManager sensorManager;
    private dbSensorListener dBSensorListener;
    //GPS
    private LocationManager locationManager;
    private int GPS_REQUEST_CODE = 1;
    static final String[] LOCATION_GPS = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_PHONE_STATE};
    private static final int READ_PHONE_STATE = 100;//定位权限请求
    private String logFilePath;


    /*
     * @Param:db 封装的直接输入数据的数据库对象，并直接存储感知信息
     */
    public SenseInfo(Context context, SQLiteDatabase db,String path) {
        dBSensorListener = new dbSensorListener();
        this.db = db;
        this.logFilePath = path;
        init(context);
        initDb();
        registerAllWithDb();
    }

    //初始化SensorManager及其他信息,如：当前设备支持的传感器
    private void init(Context context) {
        this.context = context;
        locationInfo = new double[2];
        sensorManager = (SensorManager) context.getSystemService(context.SENSOR_SERVICE);
        sensorList = sensorManager.getSensorList(Sensor.TYPE_ALL);

        //根据手机支持的传感器数量创建数组
        Log.e(TAG, "传感器列表长度为：   " + sensorList.size());
        sensorData = new float[sensorList.size()][6];
    }

    //是否包含特定传感器
    public int checkHaveSensor(String sensorName) {
        int index = -1;
        for (Sensor sensor : sensorList) {
            if (sensor.getName().equals(sensorName)) {
                index = sensorList.indexOf(sensor);
            }
        }
        return index;
    }


    public String getSensorListContent() {
        String sensorS = "本机支持的传感器有：\n" + "GPS\n";
        for (Sensor sensor : sensorList) {
            String name = sensor.getName();
            int type = sensor.getType();
            sensorS += name + "  " + type + "\n";
        }
        return sensorS;
    }

    //通过传感器名字参数获取对应的感知信息的方法
    public float[] getSensorData(String sensorName) {
        float[] data = new float[3];
        //从当前手机支持的传感器列表里匹配用户想要获得的传感器信息
        int index = checkHaveSensor(sensorName);
        if (index > 0) {

            //已变更成直接实时更新数据到目标文本框中
            data[0] = sensorData[index][0];
            data[1] = sensorData[index][1];
            data[2] = sensorData[index][2];

        }
        return data;
    }

    //获取位置信息的方法，调用updateLocation将更新存储的定位数据
    private void getLocation() {
        // 获取位置管理服务
        String serviceName = context.LOCATION_SERVICE;
        locationManager = (LocationManager) context.getSystemService(serviceName);
        // 查找到服务信息
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE); // 高精度
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(true);
        criteria.setPowerRequirement(Criteria.POWER_LOW); // 低功耗
        String provider = locationManager.getBestProvider(criteria, true); // 获取GPS信息
        /**这段代码不需要深究，是locationManager.getLastKnownLocation(provider)自动生成的，不加会出错**/
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = locationManager.getLastKnownLocation(provider); // 通过GPS获取位置
        updateLocation(location);
    }

    //获取到当前位置的经纬度
    private void updateLocation(Location location) {
        if (location != null) {
            locationInfo[0] = location.getLatitude();
            locationInfo[1] = location.getLongitude();
        } else {
            Log.e(TAG, "无法获取到位置信息");
            Toast.makeText(context, "无法获取到位置信息", Toast.LENGTH_SHORT).show();
            locationInfo[0] = -0.1;
            locationInfo[1] = -0.1;
        }
    }

    //所有在Service中进行数据存储的内容都在这里进行
    private final class dbSensorListener implements SensorEventListener {
        @Override
        public void onSensorChanged(SensorEvent event) {
            String data = Arrays.toString(event.values);
            SimpleDateFormat format = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
            String time = format.format(new Date(System.currentTimeMillis()));
            String sensorName = event.sensor.getStringType();

            //将数据插入到数据库中
            ContentValues contentValues = new ContentValues();
            contentValues.put("recordTime", time);
            contentValues.put("sensorName", sensorName);
            contentValues.put("sensorData", data);
            db.insert("table_senseData", null, contentValues);
            Log.e(TAG, "table insert value, sensor: " + event.sensor.getName() + "   sensorData:" + "     " + data);
            //写到日志文件中
            writeLog(time + "  " + sensorName + "  " + data);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    }

    //为保证传感器使用问题，设立撤销方法
    public void clean() {
        sensorManager.unregisterListener(dBSensorListener);
    }



    private void initDb() {
        //创建数据表并且添加是否存在表格的判定
        //,deadLine varchar(64),postName varchar(64),coin integer,taskText text
        boolean exit = false;
        String sql = "select count(*) from sqlite_master where type ='table' and name ='table_senseData';";
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToNext()) {
            int count = cursor.getInt(0);
            if (count > 0) {
                exit = true;
            }
        }
        cursor.close();

        //不存在表才执行创建表的操作
        if (!exit) {
            String init = "create table table_senseData(recordTime text NOT NULL,sensorName text NOT NULL,sensorData text,CONSTRAINT uni_id PRIMARY KEY(recordTime,sensorName))";
            db.execSQL(init);
            Log.e(TAG, "table_senseData数据表格成功创建");
            Toast.makeText(context, "table_senseData数据表格成功创建", Toast.LENGTH_SHORT).show();
        } else {
            Log.e(TAG, "table_senseData数据表格已创建");
            Toast.makeText(context, "table_senseData数据表格已创建", Toast.LENGTH_SHORT).show();
        }
    }



    private void registerAllWithDb() {
        for (Sensor sensor : sensorList) {
            sensorManager.registerListener(dBSensorListener, sensorManager.getDefaultSensor(sensor.getType()), SensorManager.SENSOR_DELAY_NORMAL);
            //sensorManager.registerListener(dBsensorListener, sensorManager.getDefaultSensor(sensor.getType()), senseCD);
            Log.e(TAG, "已注册" + sensor.getName() + "传感器");
        }
    }


    public void writeLog(String text){
        File logFile = new File(logFilePath);
        BufferedWriter buff;
        try{
            buff = new BufferedWriter(new FileWriter(logFile,true));
            buff.append(text);
            buff.newLine();
            buff.close();
        }catch (IOException exp){
            exp.printStackTrace();
        }
    }
   /*  public double[] getGPSInfo() {
        initGPS();

        return locationInfo;
    }

   //测试GPS定位信息的获取
    private void initGPS() {
        openGPSsetting();
    }


    private boolean checkGpsIsOpen() {
        boolean isOpen;
        LocationManager locationManager = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);
        isOpen = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return isOpen;
    }

    private void openGPSsetting() {
        //判定是否开启GPS的设置
        if (checkGpsIsOpen()) {
            Log.e(TAG,"GPS已开启");
            Toast.makeText(context, "GPS已开启", Toast.LENGTH_SHORT).show();
            if (Build.VERSION.SDK_INT >= 23) { //判断是否为android6.0系统版本，如果是，需要动态添加权限
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {// 没有权限，申请权限。
                    ActivityCompat.requestPermissions((Activity) context, LOCATIONGPS, READ_PHONE_STATE);
                } else {
                    getLocation();
                }
            }
        } else {
            Dialog dialog = new AlertDialog.Builder(context).setTitle("打开GPS")
                    .setMessage("前去设置")
                    //  取消选项
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Log.e(TAG,"GPS开启过程关闭");
                            // 关闭dialog
                            dialogInterface.dismiss();
                        }
                    })
                    //  确认选项
                    .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //跳转到手机原生设置页面
                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            //测试关注点
                            ((Activity) context).startActivityForResult(intent, GPS_REQUEST_CODE);
                        }
                    })
                    .setCancelable(false)
                    .create();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){//6.0
                dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
            }else {
                dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
            }
            dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
            dialog.show();
        }
    }*/

    public double[] getLocationInfo() {
        return locationInfo;
    }

}

package com.hills.mcs_02.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hills.mcs_02.BaseActivity;
import com.hills.mcs_02.R;
import com.hills.mcs_02.StringStore;
import com.hills.mcs_02.dataBeans.Task;
import com.hills.mcs_02.networkClasses.interfacesPack.PostRequest_publishTask;
import com.hills.mcs_02.sensorFunction.SenseHelper;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Activity_publish_sensortask_2 extends BaseActivity implements View.OnClickListener, AMapLocationListener {
    private static final String TAG = "Activity_publish_sensortask_2";
    private Task task;
    //为日期选择设立的全局变量
    int mYear, mMonth, mDay;
    String date_tvString;
    Spinner taskKind_spinner;
    int taskKind = -1;
    private TextView longitude_tv;
    private TextView latitude_tv;
    private TextView Deadline_tv;
    private boolean loaction_YN = true; //位置获取是否成功标识
    private AMapLocationClient mapLocationClient;
    private AlertDialog mSensorMultiAlertDialog;
    private boolean[] mBooleans;
    public String[] mSensors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_sensortask_2);

        init();
    }

    private void init() {
        findViewById(R.id.publishpage_sensortaskpublish_2_button).setOnClickListener(this);
        Deadline_tv = findViewById(R.id.publishpage_sensortaskpublish_2_deadline_dp);
        Deadline_tv.setOnClickListener(this);

        mSensors =new SenseHelper(this).getSensorList_TypeInt_Strings();
        mBooleans = new boolean[mSensors.length];

        taskKind_spinner = findViewById(R.id.publishpage_sensortaskpublish_2_taskKind_spinner);
        taskKind_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                taskKind = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                taskKind = 0;
            }
        });

        ImageView back_im = findViewById(R.id.publishpage_sensortaskpublish_2_backarrow);
        back_im.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.publishpage_sensortaskpublish_2_sensor_content).setOnClickListener(this);
        locationInit();
    }

    private void multiChooseDialogCreate() {
        mSensorMultiAlertDialog = new AlertDialog.Builder(this).setMultiChoiceItems(mSensors, mBooleans, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                Log.i(TAG, which + ";" + isChecked);
                mBooleans[which] = isChecked;
            }
        }).setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                TextView temp_tv = findViewById(R.id.publishpage_sensortaskpublish_2_sensor_content);
                temp_tv.setText(getSensorTvText());
            }
        }).create();
        mSensorMultiAlertDialog.show();
    }

    private String getSensorTvText() {
        String chooseSensor = "";
        //遍历取得选择的传感器
        for (int i = 0; i < mBooleans.length; i++) {
            if (mBooleans[i] == true) chooseSensor = chooseSensor + mSensors[i];
            if (i != mBooleans.length - 1) chooseSensor = chooseSensor + " ";
        }
        return  chooseSensor;
    }

    private void locationInit() {
        //初始化经纬度数据
        //longitude_tv = findViewById(R.id.publishpage_basictaskpublish_1_longitude_tv_content);
        //latitude_tv = findViewById(R.id.publishpage_basictaskpublish_1_latitude_tv_content);

        //初始化定位类,这里绑定的定位只是单单此Activity，注意如果调整成全应用内通过，需要编程getApplicationContext
        mapLocationClient = new AMapLocationClient(this);
        mapLocationClient.setLocationListener(this);
        //为mapClient配置参数
        AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        mLocationOption.setOnceLocationLatest(true);
        mapLocationClient.setLocationOption(mLocationOption);
        mapLocationClient.startLocation();//开始定位
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.publishpage_sensortaskpublish_2_button:
                postNetworkRequest();
                break;
            case R.id.publishpage_sensortaskpublish_2_deadline_dp:
                calenderDialogCreat();
                break;
            case R.id.publishpage_sensortaskpublish_2_sensor_content:
                multiChooseDialogCreate();
                break;
        }
    }

    private void calenderDialogCreat() {
        //获取当前日期
        Calendar cal = Calendar.getInstance();
        mYear = cal.get(Calendar.YEAR);
        mMonth = cal.get(Calendar.MONTH);
        mDay = cal.get(Calendar.DAY_OF_MONTH);

        //创建日期选择的对话框，并绑定日期选择的Listener（都是Android内部封装的组件和方法）
        DatePickerDialog dialog = new DatePickerDialog(Activity_publish_sensortask_2.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                mYear = year;
                mMonth = month;
                mDay = dayOfMonth;
                date_tvString = mYear + "." + (mMonth + 1) + "." + mDay;
                Deadline_tv.setText(date_tvString);
            }

        }, mYear, mMonth, mDay);
        //设置最小时限
        DatePicker datePicker = dialog.getDatePicker();
        datePicker.setMinDate(new Date().getTime());

        dialog.show();
    }

    @SuppressLint("LongLogTag")
    private void postNetworkRequest() {
        final Context context = this;
        //收集当前页输入的信息
        String coins_str = ((EditText) findViewById(R.id.publishpage_sensortaskpublish_2_Coins_ev)).getText().toString();
        String taskName = ((EditText) findViewById(R.id.publishpage_sensortaskpublish_2_taskName_ev)).getText().toString();
        String taskMount_str = ((EditText) findViewById(R.id.publishpage_sensortaskpublish_2_taskMount_ev)).getText().toString();
        String deadline = ((TextView) findViewById(R.id.publishpage_sensortaskpublish_2_deadline_dp)).getText().toString();
        String describe = ((EditText) findViewById(R.id.publishpage_sensortaskpublish_2_describe_ev)).getText().toString();
        //添加了传感器输入信息
        String sensorTypesString = getSensorTvText();

        if (coins_str == "" || taskName == "" || taskMount_str == "" || deadline == "" || describe == "" || sensorTypesString == "")
            Toast.makeText(this, getString(R.string.publishTask_nullRemind), Toast.LENGTH_SHORT).show();
        else {
            float coins = Float.parseFloat(coins_str);
            int taskMount = Integer.parseInt(taskMount_str);

            int userId = Integer.parseInt(getSharedPreferences("user", MODE_PRIVATE).getString("userID", ""));
            String userName = getSharedPreferences("user", MODE_PRIVATE).getString("userName", "");
            //String timeNow = (new SimpleDateFormat("yyyy.MM.dd  HH:mm:ss")).format(new Date(System.currentTimeMillis()));
            //获取感知任务指定的传感器类型并转换成Integer[]类型
            int[] sensorTypes = new SenseHelper(this).sensorList_NameStrings2TypeInts(sensorTypesString.split(" "));
            Log.i(TAG,"MARRRRK: sensorTypesLength" + sensorTypes.length);
            String lSensorTypes_String = "";
            for(int i = 0; i < sensorTypes.length; i++) {
                lSensorTypes_String = lSensorTypes_String + sensorTypes[i];
                if(i != sensorTypes.length -1) lSensorTypes_String = lSensorTypes_String + StringStore.Divider_1;
            }

            //建立任务Bean
            try {
                task = new Task(null, taskName, new Date(), new SimpleDateFormat("yyyy.MM.dd").parse(deadline), userId, userName, coins, describe, taskMount, 0, taskKind,lSensorTypes_String);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Log.i(TAG, task.toString());
            Gson gson = new GsonBuilder().setDateFormat("yyyy.MM.dd").create();
            String postTask = gson.toJson(task);


            //发送POST请求
            Retrofit retrofit = new Retrofit.Builder().baseUrl(this.getResources().getString(R.string.base_url)).addConverterFactory(GsonConverterFactory.create()).build();
            //测试使用url
            //Retrofit retrofit = new Retrofit.Builder().baseUrl("http://192.168.43.60:8889/").addConverterFactory(GsonConverterFactory.create()).build();
            RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), postTask);
            PostRequest_publishTask publish = retrofit.create(PostRequest_publishTask.class);
            Call<ResponseBody> call = publish.publishTask(requestBody);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.code() == 200) {
                        Toast.makeText(context, getResources().getString(R.string.publishSuccess), Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(context, getResources().getString(R.string.publishFail), Toast.LENGTH_SHORT).show();
                        Log.i(TAG,"getResources().getString(R.string.publishFail)" + "\n sensorTask publishing response code :" + response.code());
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        }
    }

    @Override
    public void onLocationChanged(AMapLocation pAMapLocation) {
        if (pAMapLocation == null) {
            loaction_YN = false;
            System.out.println("Alert！aMapLocation is null");
        } else {
            //ErrorCode等于0为无错误
            if (pAMapLocation.getErrorCode() == 0) {
                double longitude = pAMapLocation.getLongitude();
                double latitude = pAMapLocation.getLatitude();
                //System.out.println("latitude: " + latitude +" longitude: " + longitude);
                //做小数位数限制 目前为五位数
                DecimalFormat df = new DecimalFormat("#.00000");
                //longitude_tv.setText(df.format(longitude));
                //latitude_tv.setText(df.format(latitude));
            } else {
                loaction_YN = false;
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapLocationClient.stopLocation();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapLocationClient.onDestroy();
    }
}

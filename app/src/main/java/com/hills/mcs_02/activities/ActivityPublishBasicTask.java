package com.hills.mcs_02.activities;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

import android.app.DatePickerDialog;
import android.content.Context;
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

import java.util.Calendar;
import java.util.Date;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.hills.mcs_02.BaseActivity;
import com.hills.mcs_02.dataBeans.Task;
import com.hills.mcs_02.networkClasses.interfacesPack.PostRequestPublishTask;
import com.hills.mcs_02.R;

public class ActivityPublishBasicTask extends BaseActivity implements AMapLocationListener {
    private Task task;
    private String TAG = "Fragment_publish";
    int mYear,mMonth,mDay;   /** Set the global variable for the date selection */
    String dateString;
    Spinner taskKindSpinner;
    int taskKind = -1;
    private TextView longitudeTv;
    private TextView latitudeTv;
    private boolean isLocation = true; /** The location is determined to be successful */
    private AMapLocationClient mapLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_taskpublish_1);

        findViewById(R.id.publishpage_basictaskpublish_1_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postNetworkRequest();
            }
        });
       /**  Initialize the positioning class*/
        mapLocationClient = new AMapLocationClient(this);
        mapLocationClient.setLocationListener(this);
        /**  Configure the parameters for MapClient */
        AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        mLocationOption.setOnceLocationLatest(true);
        mapLocationClient.setLocationOption(mLocationOption);
        mapLocationClient.startLocation();

        /**  Add a bound Listener to the deadline selection button of the publication page */
        final TextView TEXT_VIEW = findViewById(R.id.publishpage_basictaskpublish_1_deadline_dp);
        TEXT_VIEW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //获取当前日期
                Calendar cal = Calendar.getInstance();
                mYear = cal.get(Calendar.YEAR);
                mMonth = cal.get(Calendar.MONTH);
                mDay = cal.get(Calendar.DAY_OF_MONTH);
                /** Create a dialog box for date selection and bind the Listener for date selection */
                DatePickerDialog dialog = new DatePickerDialog(ActivityPublishBasicTask.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        mYear = year;
                        mMonth = month;
                        mDay = dayOfMonth;
                        dateString = mYear + "." + (mMonth+1) + "." + mDay;
                        TEXT_VIEW.setText(dateString);
                    }

                },mYear,mMonth,mDay);
                /** Set a minimum time limit */
                DatePicker datePicker = dialog.getDatePicker();
                datePicker.setMinDate(new Date().getTime());
                dialog.show();
            }
        });
        taskKindSpinner = findViewById(R.id.publishpage_basictaskpublish_1_taskKind_spinner);
        taskKindSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                taskKind = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                taskKind = 0;
            }
        });
        initBackBtn();
    }

        private void postNetworkRequest() {
            final Context CONTEXT = this;
            /** Collect information entered on the current page */
            String coinsStr = ((EditText) findViewById(R.id.publishpage_basictaskpublish_1_Coins_ev)).getText().toString();
            String taskName = ((EditText) findViewById(R.id.publishpage_basictaskpublish_1_taskName_ev)).getText().toString();
            String taskCountStr = ((EditText) findViewById(R.id.publishpage_basictaskpublish_1_taskMount_ev)).getText().toString();
            String deadline = ((TextView) findViewById(R.id.publishpage_basictaskpublish_1_deadline_dp)).getText().toString();
            String describe = ((EditText) findViewById(R.id.publishpage_basictaskpublish_1_describe_ev)).getText().toString();
            if (coinsStr == "" || taskName == "" || taskCountStr == "" || deadline == "" || describe == "")
                Toast.makeText(this, getString(R.string.publishTask_nullRemind), Toast.LENGTH_SHORT).show();
            else {
                float coins = Float.parseFloat(coinsStr);
                int taskCount = Integer.parseInt(taskCountStr);
                int userId = Integer.parseInt(getSharedPreferences("user", MODE_PRIVATE).getString("userID", ""));
                String userName = getSharedPreferences("user", MODE_PRIVATE).getString("userName", "");
                    try {
                        task = new Task(null, taskName, new Date(), new SimpleDateFormat("yyyy.MM.dd").parse(deadline), userId, userName, coins, describe, taskCount, 0, taskKind);
                    } catch (ParseException exp) {
                        exp.printStackTrace();
                    }
                Log.i(TAG, task.toString());
                Gson gson = new GsonBuilder().setDateFormat("yyyy.MM.dd").create();
                String postTask = gson.toJson(task);
                /** Send a POST request */
                Retrofit retrofit = new Retrofit.Builder().baseUrl(this.getResources().getString(R.string.base_url)).addConverterFactory(GsonConverterFactory.create()).build();

                RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), postTask);
                PostRequestPublishTask publish = retrofit.create(PostRequestPublishTask.class);
                Call<ResponseBody> call = publish.publishTask(requestBody);
                Log.i(TAG,"The addTask request time: " + System.currentTimeMillis());
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.code() == 200) {
                            Toast.makeText(CONTEXT, getResources().getString(R.string.publishSuccess), Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(CONTEXT, getResources().getString(R.string.publishFail), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable throwable) {

                    }
                });
            }
        }


    private void initBackBtn() {
        ImageView backIv = findViewById(R.id.publishpage_basictaskpublish_1_backarrow);
        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if(aMapLocation == null){
            isLocation = false;
            System.out.println("Alert！aMapLocation is null");
        }else{
            if(aMapLocation.getErrorCode() == 0){
                double longitude = aMapLocation.getLongitude();
                double latitude = aMapLocation.getLatitude();
                /**  The decimal limit is currently five digits */
                DecimalFormat df = new DecimalFormat("#.00000");
            }else{
                isLocation = false;
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

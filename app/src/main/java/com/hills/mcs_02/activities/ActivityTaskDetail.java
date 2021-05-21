package com.hills.mcs_02.activities;

import com.google.gson.Gson;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.Toast;

import com.hills.mcs_02.dataBeans.Task;
import com.hills.mcs_02.dataBeans.User_Task;
import com.hills.mcs_02.BaseActivity;
import com.hills.mcs_02.networkClasses.interfacesPack.PostRequestUserTaskAdd;
import com.hills.mcs_02.networkClasses.interfacesPack.QueryRequestTaskDetail;
import com.hills.mcs_02.R;
import com.hills.mcs_02.StringStore;
import com.hills.mcs_02.sensorFunction.SenseHelper;
import com.hills.mcs_02.sensorFunction.SensorService;
import com.hills.mcs_02.taskSubmit.Activity_Task_Submit;

import java.io.IOException;
import java.text.SimpleDateFormat;



public class ActivityTaskDetail extends BaseActivity {

    private final String TAG = "activity_task_detail";
    private Task task;
    private TextView userNameTv;
    private TextView postTimeTv;
    private TextView describeTv;
    private TextView taskContentTv;
    private TextView coinsCountTv;
    private TextView deadlineTv;
    private TextView taskNameTv;
    private TextView taskKindTv;
    private Button acceptBtn;
    private Button submitBtn;
    private Scroller mScroller;
    public Intent mToSensorServiceIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        mScroller = new Scroller(this);
        task = new Task();
        userNameTv = findViewById(R.id.activity_taskDetail_userName);
        taskContentTv = findViewById(R.id.activity_taskDetail_content);
        coinsCountTv = findViewById(R.id.activity_taskDetail_coin);
        deadlineTv = findViewById(R.id.activity_taskDetail_deadline);
        postTimeTv = findViewById(R.id.activity_taskDetail_postTime);
        taskNameTv = findViewById(R.id.activity_taskDetail_taskName);
        acceptBtn = (Button) findViewById(R.id.activity_taskDetail_accept);
        taskKindTv = findViewById(R.id.activity_taskDetail_taskKind);
        submitBtn = (Button) findViewById(R.id.activity_taskDetail_submit);
        acceptBtn.setVisibility(View.INVISIBLE);
        submitBtn.setVisibility(View.INVISIBLE);


        initData();
        initBackBtn();
        checkUserTask();
        /*
        accept_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_Task_Detail.this, Activity_Task_Submit.class);
                startActivity(intent);
            }
        });*/


    }


    private void initBackBtn() {
        ImageView backIv = findViewById(R.id.activity_taskDetail_backarrow);
        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initData() {
        String taskGson = getIntent().getStringExtra("taskGson");
        Gson gson = new Gson();
        task = gson.fromJson(taskGson, Task.class);
        userNameTv.setText(task.getUserName());
        taskContentTv.setText(task.getDescribe_task());
        coinsCountTv.setText(task.getCoin().toString());
        deadlineTv.setText(new SimpleDateFormat("yyyy.MM.dd").format(task.getDeadLine()));
        postTimeTv.setText(new SimpleDateFormat("yyyy.MM.dd").format(task.getPostTime()));
        taskNameTv.setText(task.getTaskName());
        if(task.getTaskKind() == null) taskKindTv.setText(R.string.ordinaryTask);
        else {
            switch (task.getTaskKind()){
                case 0:
                    taskKindTv.setText(getString(R.string.home_grid_0));break;
                case 1:
                    taskKindTv.setText(getString(R.string.home_grid_1));break;
                case 2:
                    taskKindTv.setText(getString(R.string.home_grid_2));break;
                case 3:
                    taskKindTv.setText(getString(R.string.home_grid_3));break;
                case 4:
                    taskKindTv.setText(getString(R.string.home_grid_4));break;
            }
        }
    }

    private void checkUserTask() {
        int loginUserId = Integer.parseInt(getSharedPreferences("user", MODE_PRIVATE).getString("userID", "-1"));
        //检查是否登录
        if (loginUserId == -1) {
            Toast.makeText(this, getResources().getString(R.string.login_first), Toast.LENGTH_SHORT);
            Intent intent = new Intent(ActivityTaskDetail.this, ActivityLogin.class);
            startActivity(intent);
        } else {
            User_Task userTask = new User_Task(null, loginUserId, task.getTaskId(), 0, null, null,0);
            Gson gson = new Gson();
            String content = gson.toJson(userTask);
            queryRequest(content);
        }
    }


    public void queryRequest(final String content) {
        //创建Retrofit对象
        Retrofit retrofit = new Retrofit.Builder().baseUrl(getResources().getString(R.string.base_url)).addConverterFactory(GsonConverterFactory.create()).build();
        //Retrofit retrofit = new Retrofit.Builder().baseUrl("http://192.168.43.60:8889/").addConverterFactory(GsonConverterFactory.create()).build();

        //创建网络接口实例
        QueryRequestTaskDetail request = retrofit.create(QueryRequestTaskDetail.class);

        //创建RequestBody
        RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), content);

        //包装发送请求
        Call<ResponseBody> call = request.checkUserTask(requestBody);

        final Context context = this;

        Log.i(TAG,"NowTime S:" + System.currentTimeMillis());
        //异步网络请求
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.i(TAG,"NowTime E:" + System.currentTimeMillis());
                //在此处做未接受任务、已接受任务的两种情况处理，并加入用户登录跳转页
                if (response.code() == 200) {
                    //根据返回内容初始化按钮
                    String status = null;
                    try {
                        status = response.body().string() + "";
                    } catch (IOException exp) {
                        exp.printStackTrace();
                    }

                    Log.i(TAG, "Status: " + status);

                    //这里根据返回的字符判定
                    switch (status) {
                        case "-1":
                            acceptBtn.setVisibility(View.VISIBLE);
                            acceptBtn.setText(getResources().getString(R.string.taskAccept));
                            acceptBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    //判定是否含有要求的传感器，检测即中止
                                    String sensorTypesString = task.getSensorTypes();
                                    boolean canAccept = true;
                                    if(sensorTypesString != null){
                                        String[] tempStrings = sensorTypesString.split(StringStore.Divider_1);
                                        int[] types = new int[tempStrings.length];
                                        for(int temp = 0; temp < tempStrings.length; temp++) types[temp] = Integer.parseInt(
                                            tempStrings[temp]);
                                        if(types.length <= 0) canAccept = true;
                                        else if (types.length == 1 && types[0] == 1) canAccept = true;
                                        else{
                                            SenseHelper lSenseHelper = new SenseHelper(
                                                ActivityTaskDetail.this);
                                            for(int i : types){
                                                if(!lSenseHelper.containSensor(i)) {
                                                    canAccept = false;
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                    //canAccept测试后删除
                                    if(canAccept) {
                                        addUserTaskRequest(content);
                                        mToSensorServiceIntent = new Intent(ActivityTaskDetail.this, SensorService.class);
                                        //需要的字符串的转换
                                        String taskSensor = task.getSensorTypes();
                                        mToSensorServiceIntent.putExtra("task_sensor_need",taskSensor);
                                        startService(mToSensorServiceIntent);
                                    }
                                    else {
                                        //TODO:转换成功string.xml文件中的字符
                                         new AlertDialog.Builder(ActivityTaskDetail.this).setTitle(getString(R.string.failToAcceptTask)).setMessage(getString(R.string.taskSensorNeedRemind))
                                         .setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
                                             @Override
                                             public void onClick(DialogInterface dialog, int which) {
                                                 dialog.dismiss();
                                             }
                                         }).setCancelable(false).show();
                                        //Toast.makeText(Activity_Task_Detail.this,getString(R.string.taskSensorNeedRemind),Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                            break;
                        case "0":
                            acceptBtn.setVisibility(View.INVISIBLE);
                            submitBtn.setVisibility(View.VISIBLE);
                            if(!submitBtn.hasOnClickListeners()){
                                submitBtn.setText(getResources().getString(R.string.submitData));
                                submitBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(ActivityTaskDetail.this, Activity_Task_Submit.class);
                                        //将Task需求的传感器类型转换成字符传递给Task_Submit
                                        if(task.getSensorTypes() == null)   intent.putExtra(getResources().getString(R.string.intent_taskSensorTypes_name), StringStore.SP_StringError); //添加空提示
                                        else {
                                            String sensorTypes = task.getSensorTypes();
                                            if (sensorTypes != null) {
                                                intent.putExtra(getResources().getString(R.string.intent_taskSensorTypes_name), sensorTypes);
                                            } else {
                                                //添加错误提示字符串
                                                intent.putExtra(getResources().getString(R.string.intent_taskSensorTypes_name), StringStore.SP_StringError);
                                            }
                                        }
                                        intent.putExtra(getResources().getString(R.string.intent_taskID_name), task.getTaskId());
                                        startActivity(intent);
                                        finish();
                                    }
                                });
                            }
                            break;
                        case "1":
                            acceptBtn.setVisibility(View.VISIBLE);
                            submitBtn.setVisibility(View.INVISIBLE);
                            acceptBtn.setText(getResources().getString(R.string.taskCompleted));
                            acceptBtn.setEnabled(false);
                            break;
                        default:
                            Log.e(TAG,"user_task返回状态码错误");
                            break;
                    }
                }else{
                    Log.e(TAG,"user_task查询状态码失败");
                    Toast.makeText(context,getResources().getString(R.string.QueryStatusCodeFailed),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {

            }
        });
    }


    public void addUserTaskRequest(final String content) {
        final Context CONTEXT = this;
        //创建Retrofit对象
        Retrofit retrofit = new Retrofit.Builder().baseUrl(getResources().getString(R.string.base_url)).addConverterFactory(GsonConverterFactory.create()).build();
        //Retrofit retrofit = new Retrofit.Builder().baseUrl("http://192.168.43.60:8889/").addConverterFactory(GsonConverterFactory.create()).build();

        //实例化网络接口
        PostRequestUserTaskAdd request = retrofit.create(PostRequestUserTaskAdd.class);

        //初始化requestbody
        RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), content);

        //初始化call
        Call<ResponseBody> call = request.addUserTask(requestBody);

        Log.i(TAG,"NowTime S:" + System.currentTimeMillis());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.i(TAG,"NowTime E:" + System.currentTimeMillis());
                if (response.code() == 200) {
                    Toast.makeText(CONTEXT, getResources().getString(R.string.AcceptTaskSuccessful), Toast.LENGTH_SHORT).show();
                    acceptBtn.setVisibility(View.INVISIBLE);
                    submitBtn.setVisibility(View.VISIBLE);
                    submitBtn.setText(getResources().getString(R.string.submitData));
                    submitBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(ActivityTaskDetail.this, Activity_Task_Submit.class);
                            intent.putExtra(getResources().getString(R.string.intent_taskID_name), task.getTaskId());
                            String sensorType = task.getSensorTypes();
                            //有效化判定
                            if(sensorType != null){
                                intent.putExtra(getResources().getString(R.string.intent_taskSensorTypes_name),sensorType);
                            }else {
                                //TODO：无效化代码
                                //这里直接给予了默认的GPS传感器-1
                                intent.putExtra(getResources().getString(R.string.intent_taskSensorTypes_name),"-1");
                            }
                            startActivity(intent);
                            finish();
                        }
                    });
                } else {
                    Toast.makeText(CONTEXT, getResources().getString(R.string.AcceptTaskFailed), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {

            }
        });

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mToSensorServiceIntent != null) stopService(mToSensorServiceIntent);
    }
}

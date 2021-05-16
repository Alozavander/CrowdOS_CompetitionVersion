package com.hills.mcs_02.taskSubmit;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.google.gson.Gson;
import com.hills.mcs_02.BaseActivity;
import com.hills.mcs_02.R;
import com.hills.mcs_02.RequestCodes;
import com.hills.mcs_02.StringStore;
import com.hills.mcs_02.dataBeans.Familiar_Sensor;
import com.hills.mcs_02.dataBeans.User_Task;
import com.hills.mcs_02.networkClasses.interfacesPack.PostRequest_taskSubmit;
import com.hills.mcs_02.networkClasses.interfacesPack.PostRequest_taskSubmit_FamiliarFiles;
import com.hills.mcs_02.networkClasses.interfacesPack.PostRequest_taskSubmit_files;
import com.hills.mcs_02.sensorFunction.SenseFunction;
import com.hills.mcs_02.taskSubmit.uploadPack.ProgressRequestBody;
import com.hills.mcs_02.taskSubmit.uploadPack.UploadCallbacks;
import com.hills.mcs_02.viewsAdapters.Adapter_RecyclerView_TaskSubmit_Audio;
import com.hills.mcs_02.viewsAdapters.Adapter_RecyclerView_TaskSubmit_SenseData;
import com.hills.mcs_02.viewsAdapters.Adapter_RecyclerView_TaskSubmit_Video;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Activity_Task_Submit extends BaseActivity {
    public static final int IMAGE_ITEM_ADD = -1;
    public static final int REQUEST_CODE_SELECT = 100;
    public static final int REQUEST_CODE_PREVIEW = 101;
    public static final String TAG = "Activity_Task_Submit";
    public static final String dbPath = "data/data/com.hills.mcs_02/cache" + File.separator + "sensorData" + File.separator + "sensorData.db";

    private Scroller mScroller;


    private int maxImgCount = 8;               //允许选择图片最大数
    private ArrayList<File> audioList;         //音频列表
    private Adapter_RecyclerView_TaskSubmit_Audio audio_Adapter;

    private ArrayList<File> videoList;         //视频列表
    private Adapter_RecyclerView_TaskSubmit_Video video_Adapter;

    private ArrayList<File> senseDataList;    //感知数据文件列表
    private Adapter_RecyclerView_TaskSubmit_SenseData senseData_Adapter;

    private RecyclerView mRecyclerView;
    private Adapter_RecyclerView_TaskSubmit_Image image_Adapter;

    private NumberProgressBar mNumberProgressBar;
    private ArrayList<File> imageList;         //视频列表
    private long uploaded_now;            //目前传输总过程中已上传的数据量

    private EditText editText;
    private SQLiteDatabase SQLdb;
    private SensorManager sensorManager;
    //For test to get sensors data;
    private TextView sensorDataShow_tv;
    private BroadcastReceiver receiver;
    private Integer taskID;
    private long totalLength;
    private Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_submit);

        editText = (EditText) findViewById(R.id.activity_taskSub_et);
        mScroller = new Scroller(this);
        sensorManager = (SensorManager) getSystemService(this.SENSOR_SERVICE);
        mNumberProgressBar = findViewById(R.id.activity_taskSub_number_progress_bar);
        taskID = getIntent().getIntExtra(getResources().getString(R.string.intent_taskID_name), -1);
        if (taskID.equals(-1)) {
            Toast.makeText(this, getResources().getString(R.string.Task_Submit_requireTask_error), Toast.LENGTH_SHORT).show();
            finish();
        }

        initBackBT();
        /*
        findViewById(R.id.activity_taskSub_chooseData_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            //点击在文本框中显示数据库存入的感知数据
            public void onClick(View v) {
                String text = "";
                for(Sensor sensor : sensorManager.getSensorList(Sensor.TYPE_ALL)){
                    String sensorName = sensor.getName();
                    String data = "";
                    String sql = "select sensorData from table_senseData where sensorName = '" + sensorName + "'ORDER BY recordTime DESC";
                    Cursor cursor = SQLdb.rawQuery(sql,null);
                    if(cursor.moveToFirst()){
                        data = cursor.getString(0);
                    }
                    cursor.close();
                    text += sensorName + ":  " + data + "\n";
                }
                sensorDataShow_tv.setText(text);
                //请求发送暂时搁置暂时搁置
                //postRequest_Submit();
            }
        });
        */
        findViewById(R.id.activity_taskSub_submit_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageList.size() > 0 || videoList.size() > 0 || audioList.size() > 0 || senseDataList.size() > 0)
                    uploadWithFile();
                else postRequest_Submit();
            }
        });

        initImagePicker();
        initAudioPicker();
        initVideoPicker();
        //For sensor data
        sensorDataShow_tv = (TextView) findViewById(R.id.activity_taskSub_sensorData_tv);
        initSensorDataPicker();


        //数据库相关注销
        //SQLdb = SQLiteDatabase.openOrCreateDatabase(dbPath,null);

        //广播器已截止
        //registBroadcastReceiver();
    }

    //根据Intent传过来的String判定任务需要哪些传感器
    //TODO:
    private void initSensorDataPicker() {
        String sensorTypes_String = getIntent().getStringExtra(getResources().getString(R.string.intent_taskSensorTypes_name));
        if (sensorTypes_String.equals(StringStore.SP_StringError)) concealDataChooseViews();
        else {
            String[] temp_stirngs = sensorTypes_String.split(StringStore.Divider_1);
            int[] sensorTypes = new int[temp_stirngs.length];
            for (int i = 0; i < temp_stirngs.length; i++)
                sensorTypes[i] = Integer.valueOf(temp_stirngs[i]);
            //检测出包含GPS的情况，GPS给的sensor值为-1,暂时隐藏，后续开放
            if (sensorTypes.length == 1 && sensorTypes[0] == -1) concealDataChooseViews();
            else {
                //初始化RECYCLERVIEW和Adapter
                senseDataList = new ArrayList<File>();
                //初始化Audio的RV
                RecyclerView senseData_rv = findViewById(R.id.activity_taskSub_chooseData_rv);
                senseData_rv.setLayoutManager(new LinearLayoutManager(Activity_Task_Submit.this, LinearLayoutManager.VERTICAL, false));
                senseData_Adapter = new Adapter_RecyclerView_TaskSubmit_SenseData(Activity_Task_Submit.this, senseDataList);
                senseData_rv.setAdapter(senseData_Adapter);

                //初始化add的按钮
                //根据传递得到的需求的传感器类型保存得到感知数据文件
                Button data_add = findViewById(R.id.activity_taskSub_chooseData_add);
                data_add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        findViewById(R.id.activity_taskSub_pb_circle).setVisibility(View.VISIBLE);
                        findViewById(R.id.activity_taskSub_pb_circle_text).setVisibility(View.VISIBLE);
                        SenseFunction lSenseHelper = new SenseFunction(Activity_Task_Submit.this);
                        for (int i : sensorTypes) {
                            String timeStamp = System.currentTimeMillis() + "";
                            File temp_File = lSenseHelper.storeDataToCSV(i, i + "_senseType_senseData_" + timeStamp + ".csv", null);
                            senseData_Adapter.AddFooterItem(temp_File);
                        }
                        findViewById(R.id.activity_taskSub_pb_circle).setVisibility(View.GONE);
                        findViewById(R.id.activity_taskSub_pb_circle_text).setVisibility(View.GONE);
                    }
                });
            }
        }

    }

    //隐藏数据选择栏相关的组件
    private void concealDataChooseViews() {
        findViewById(R.id.activity_taskSub_remind_3).setVisibility(View.GONE);
        findViewById(R.id.activity_taskSub_chooseData_rv).setVisibility(View.GONE);
        findViewById(R.id.activity_taskSub_chooseData_add).setVisibility(View.GONE);
    }

    private void initImagePicker() {
        imageList = new ArrayList<File>();
        RecyclerView recyclerView = findViewById(R.id.activity_taskSub_image_rv);
        image_Adapter = new Adapter_RecyclerView_TaskSubmit_Image(Activity_Task_Submit.this, imageList);
        @SuppressLint("WrongConstant") GridLayoutManager manager = new GridLayoutManager(Activity_Task_Submit.this, 3, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(image_Adapter);

        Button image_add = findViewById(R.id.activity_taskSub_image_add);
        image_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> names = new ArrayList<>();
                names.add("相册");
                showDialog(new SelectDialog.SelectDialogListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        switch (position) {
                            case 0:
                                //进入相册并选择
                                PictureSelector.create(Activity_Task_Submit.this)
                                        .openGallery(PictureMimeType.ofImage())
                                        .isCamera(false)// 是否显示拍照按钮 true or false
                                        .forResult(PictureConfig.CHOOSE_REQUEST);
                        }
                    }
                }, names);
            }
        });
    }

    @SuppressLint("WrongConstant")
    private void initAudioPicker() {
        audioList = new ArrayList<File>();
        //初始化Audio的RV
        RecyclerView audio_rv = findViewById(R.id.activity_taskSub_audio_rv);
        audio_rv.setLayoutManager(new LinearLayoutManager(Activity_Task_Submit.this, LinearLayoutManager.VERTICAL, false));
        audio_Adapter = new Adapter_RecyclerView_TaskSubmit_Audio(Activity_Task_Submit.this, audioList);
        audio_rv.setAdapter(audio_Adapter);

        //初始化add的按钮
        Button audio_add = findViewById(R.id.activity_taskSub_audio_add);
        audio_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("audio/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, RequestCodes.Audio_Search_RC);
            }
        });
    }

    @SuppressLint("WrongConstant")
    private void initVideoPicker() {
        videoList = new ArrayList<File>();
        //初始化Audio的RV
        RecyclerView video_rv = findViewById(R.id.activity_taskSub_video_rv);
        video_rv.setLayoutManager(new LinearLayoutManager(Activity_Task_Submit.this, LinearLayoutManager.VERTICAL, false));
        video_Adapter = new Adapter_RecyclerView_TaskSubmit_Video(Activity_Task_Submit.this, videoList);
        video_rv.setAdapter(video_Adapter);

        //初始化add的按钮
        Button video_add = findViewById(R.id.activity_taskSub_video_add);
        video_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("video/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, RequestCodes.Video_Search_RC);
            }
        });
    }

    private SelectDialog showDialog(SelectDialog.SelectDialogListener listener, List<String> names) {
        SelectDialog dialog = new SelectDialog(this, R.style.transparentFrameWindowStyle, listener, names);
        if (!this.isFinishing()) {
            dialog.show();
        }
        return dialog;
    }

    private void initBackBT() {
        ImageView back_im = findViewById(R.id.activity_taskSub_backarrow);
        back_im.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void uploadWithFile() {
        //让进度条可见
        mNumberProgressBar.setVisibility(View.VISIBLE);
        //将所有图片和视频list集成
        //List<File> fileAll_list = new ArrayList<File>();
        //所有file的大小
        //totalLength = 0;

        if (senseDataList != null) {
            if (senseDataList.size() > 0) {
                for (File file : senseDataList) {
                    uploaded_now = 0;
                    //实现上传进度监听，
                    ProgressRequestBody requestBody = new ProgressRequestBody(file, "*/*", new UploadCallbacks() {
                        @Override
                        public void onProgressUpdate(long uploaded) {
                            //设置进度条实时进度
                            uploaded_now += uploaded;
                            mNumberProgressBar.setProgress((int) (100 * uploaded_now / file.length()));
                        }

                        @Override
                        public void onError() {

                        }

                        @Override
                        public void onFinish() {

                        }
                    });
                    //通过已经复写的能够监视进度的requestBody构建MultipartBody类，完成网络上传操作后续就与普通的Retrofit操作类似
                    MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestBody);


                    String subText = editText.getText().toString();

                    if (subText.equals("") || subText == null) {
                        Toast.makeText(this, getResources().getString(R.string.Task_Submit_input_remind), Toast.LENGTH_LONG).show();
                        mNumberProgressBar.setVisibility(View.GONE);
                    } else {
                        //todo: upload the files class type -> 'Familiar_sensor'
                        String lsType = (file.getName().split("_"))[0];
                        Familiar_Sensor lFamiliar_sensor = new Familiar_Sensor(null, Integer.parseInt(getSharedPreferences("user", MODE_PRIVATE).getString("userID", "")), taskID, (float) -9999, (float) -9999, (float) -1, Float.parseFloat(lsType), null);
                        Gson gson = new Gson();
                        String postContent = gson.toJson(lFamiliar_sensor);
                        //创建Retrofit实例
                        Retrofit retrofit = new Retrofit.Builder().baseUrl(getResources().getString(R.string.base_url)).addConverterFactory(GsonConverterFactory.create()).build();
                        //测试用url
                        //Retrofit retrofit = new Retrofit.Builder().baseUrl("http://192.168.43.60:8889/").addConverterFactory(GsonConverterFactory.create()).build();
                        //创建网络接口实例
                        PostRequest_taskSubmit_FamiliarFiles subRequest = retrofit.create(PostRequest_taskSubmit_FamiliarFiles.class);
                        //创建RequestBody
                        RequestBody contentBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), postContent);
                        Call<ResponseBody> call = subRequest.task_submit(contentBody, body);
                        Log.i(TAG, "post Content of sensor data files request body: " + postContent);

                        call.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                Log.i(TAG, "response of sensor data submission code: " + response.code());
                                if (response.code() == 200) {
                                    Toast.makeText(Activity_Task_Submit.this, getResources().getString(R.string.Task_Submit_success_remind), Toast.LENGTH_LONG).show();
                                    mNumberProgressBar.setVisibility(View.GONE);
                                    finish();
                                } else {
                                    Toast.makeText(Activity_Task_Submit.this, getResources().getString(R.string.Task_Submit_fail_remind), Toast.LENGTH_LONG).show();
                                    mNumberProgressBar.setVisibility(View.GONE);
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {

                            }
                        });
                    }
                }
            } else Log.i(TAG, "sensor data files list has no files.");
        } else Log.i(TAG, "sensor data files list is null");

        if (imageList.size() > 0) {
            for (File file : imageList) {
                uploaded_now = 0;
                //实现上传进度监听，
                ProgressRequestBody requestBody = new ProgressRequestBody(file, "image/*", new UploadCallbacks() {
                    @Override
                    public void onProgressUpdate(long uploaded) {
                        //设置进度条实时进度
                        uploaded_now += uploaded;
                        mNumberProgressBar.setProgress((int) (100 * uploaded_now / file.length()));
                    }

                    @Override
                    public void onError() {

                    }

                    @Override
                    public void onFinish() {

                    }
                });
                //通过已经复写的能够监视进度的requestBody构建MultipartBody类，完成网络上传操作后续就与普通的Retrofit操作类似
                MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestBody);


                String subText = editText.getText().toString();

                if (subText.equals("") || subText == null) {
                    Toast.makeText(this, getResources().getString(R.string.Task_Submit_input_remind), Toast.LENGTH_LONG).show();
                    mNumberProgressBar.setVisibility(View.GONE);
                } else {
                    User_Task user_task = new User_Task(null, Integer.parseInt
                            (getSharedPreferences("user", MODE_PRIVATE).getString("userID", "")), taskID, 1, subText, null, 1);
                    Gson gson = new Gson();
                    String postContent = gson.toJson(user_task);
                    //创建Retrofit实例
                    Retrofit retrofit = new Retrofit.Builder().baseUrl(getResources().getString(R.string.base_url)).addConverterFactory(GsonConverterFactory.create()).build();
                    //Retrofit retrofit = new Retrofit.Builder().baseUrl("http://192.168.43.60:8889/").addConverterFactory(GsonConverterFactory.create()).build();
                    //创建网络接口实例
                    PostRequest_taskSubmit_files subRequest = retrofit.create(PostRequest_taskSubmit_files.class);
                    //创建RequestBody
                    RequestBody contentBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), postContent);
                    Call<ResponseBody> call = subRequest.task_submit(contentBody, body);
                    Log.i(TAG, postContent);
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.code() == 200) {          //成功提交任务处理
                                Toast.makeText(Activity_Task_Submit.this, getResources().getString(R.string.Task_Submit_success_remind), Toast.LENGTH_LONG).show();
                                mNumberProgressBar.setVisibility(View.GONE);
                                finish();
                            } else {
                                Toast.makeText(Activity_Task_Submit.this, getResources().getString(R.string.Task_Submit_fail_remind), Toast.LENGTH_LONG).show();
                                mNumberProgressBar.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {

                        }
                    });
                }

            }
        } else Log.i(TAG, "image files list has no files.");

        if (audioList.size() > 0) {
            for (File file : audioList) {
                uploaded_now = 0;
                //实现上传进度监听，
                ProgressRequestBody requestBody = new ProgressRequestBody(file, "audio/*", new UploadCallbacks() {
                    @Override
                    public void onProgressUpdate(long uploaded) {
                        //设置进度条实时进度
                        uploaded_now += uploaded;
                        mNumberProgressBar.setProgress((int) (100 * uploaded_now / file.length()));
                    }

                    @Override
                    public void onError() {

                    }

                    @Override
                    public void onFinish() {

                    }
                });
                //通过已经复写的能够监视进度的requestBody构建MultipartBody类，完成网络上传操作后续就与普通的Retrofit操作类似
                MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestBody);


                String subText = editText.getText().toString();

                if (subText.equals("") || subText == null) {
                    Toast.makeText(this, getResources().getString(R.string.Task_Submit_input_remind), Toast.LENGTH_LONG).show();
                    mNumberProgressBar.setVisibility(View.GONE);
                } else {
                    User_Task user_task = new User_Task(null, Integer.parseInt(getSharedPreferences("user", MODE_PRIVATE).getString("userID", "")), taskID, 1, subText, null, 2);
                    Gson gson = new Gson();
                    String postContent = gson.toJson(user_task);
                    //创建Retrofit实例
                    Retrofit retrofit = new Retrofit.Builder().baseUrl(getResources().getString(R.string.base_url)).addConverterFactory(GsonConverterFactory.create()).build();
                    //Retrofit retrofit = new Retrofit.Builder().baseUrl("http://192.168.43.60:8889/").addConverterFactory(GsonConverterFactory.create()).build();
                    //创建网络接口实例
                    PostRequest_taskSubmit_files subRequest = retrofit.create(PostRequest_taskSubmit_files.class);
                    //创建RequestBody
                    RequestBody contentBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), postContent);
                    Call<ResponseBody> call = subRequest.task_submit(contentBody, body);
                    Log.i(TAG, postContent);

                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.code() == 200) {
                                Toast.makeText(Activity_Task_Submit.this, getResources().getString(R.string.Task_Submit_success_remind), Toast.LENGTH_LONG).show();
                                mNumberProgressBar.setVisibility(View.GONE);
                                finish();
                            } else {
                                Toast.makeText(Activity_Task_Submit.this, getResources().getString(R.string.Task_Submit_fail_remind), Toast.LENGTH_LONG).show();
                                mNumberProgressBar.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {

                        }
                    });
                }
            }
        } else Log.i(TAG, "audio files list has no files.");

        if (videoList.size() > 0) {
            for (File file : videoList) {

                uploaded_now = 0;
                //实现上传进度监听，
                ProgressRequestBody requestBody = new ProgressRequestBody(file, "video/*", new UploadCallbacks() {
                    @Override
                    public void onProgressUpdate(long uploaded) {
                        //设置进度条实时进度
                        uploaded_now += uploaded;
                        mNumberProgressBar.setProgress((int) (100 * uploaded_now / file.length()));
                    }

                    @Override
                    public void onError() {

                    }

                    @Override
                    public void onFinish() {

                    }
                });
                //通过已经复写的能够监视进度的requestBody构建MultipartBody类，完成网络上传操作后续就与普通的Retrofit操作类似
                MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestBody);


                String subText = editText.getText().toString();

                if (subText.equals("") || subText == null) {
                    Toast.makeText(this, getResources().getString(R.string.Task_Submit_input_remind), Toast.LENGTH_LONG).show();
                    mNumberProgressBar.setVisibility(View.GONE);
                } else {
                    User_Task user_task = new User_Task(null, Integer.parseInt(getSharedPreferences("user", MODE_PRIVATE).getString("userID", "")), taskID, 1, subText, null, 3);
                    Gson gson = new Gson();
                    String postContent = gson.toJson(user_task);
                    //创建Retrofit实例
                    Retrofit retrofit = new Retrofit.Builder().baseUrl(getResources().getString(R.string.base_url)).addConverterFactory(GsonConverterFactory.create()).build();
                    //Retrofit retrofit = new Retrofit.Builder().baseUrl("http://192.168.43.60:8889/").addConverterFactory(GsonConverterFactory.create()).build();
                    //创建网络接口实例
                    PostRequest_taskSubmit_files subRequest = retrofit.create(PostRequest_taskSubmit_files.class);
                    //创建RequestBody
                    RequestBody contentBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), postContent);
                    Call<ResponseBody> call = subRequest.task_submit(contentBody, body);
                    Log.i(TAG, postContent);

                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.code() == 200) {
                                Toast.makeText(Activity_Task_Submit.this, getResources().getString(R.string.Task_Submit_success_remind), Toast.LENGTH_LONG).show();
                                mNumberProgressBar.setVisibility(View.GONE);
                                finish();
                            } else {
                                Toast.makeText(Activity_Task_Submit.this, getResources().getString(R.string.Task_Submit_fail_remind), Toast.LENGTH_LONG).show();
                                mNumberProgressBar.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {

                        }
                    });
                }
            }
        } else Log.i(TAG, "video files list has no files.");

        //最后上传文本框内容
        postRequest_Submit();
    }

    private void registBroadcastReceiver() {
        //SDU = Sense Data Update
        IntentFilter intentFilter = new IntentFilter("SDU");
        mContext = this;
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String text = "";
                for (Sensor sensor : sensorManager.getSensorList(Sensor.TYPE_ALL)) {
                    String sensorName = sensor.getName();
                    String data = "";
                    String sql = "select sensorData from table_senseData where sensorName = '" + sensorName + "'ORDER BY recordTime DESC";
                    Cursor cursor = SQLdb.rawQuery(sql, null);
                    if (cursor.moveToFirst()) {
                        data = cursor.getString(0);
                    }
                    cursor.close();
                    text += sensorName + ":  " + data + "\n";
                }
                //Toast.makeText(context,"接受到SDU广播，当前Text：\n" + text,Toast.LENGTH_SHORT).show();
                Log.e(TAG, "接受到SDU广播，当前Text：\n" + text);
                sensorDataShow_tv.setText(text);
            }
        };
        registerReceiver(receiver, intentFilter);
        Log.e(TAG, "注册了广播接收器");
        //Toast.makeText(this,"注册了广播接收器",Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Image Search & Add
        if (requestCode == PictureConfig.CHOOSE_REQUEST && resultCode == Activity.RESULT_OK) {
            List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
            List<File> fileList = new ArrayList<File>();
            if (selectList.size() > 0) {
                for (LocalMedia media : selectList) {
                    File temFile = new File(media.getPath());
                    //限制20MB大小
                    if (temFile.length() / 1024 <= 20480) fileList.add(temFile);
                    else
                        Toast.makeText(Activity_Task_Submit.this, getString(R.string.Task_Submit_add_size_error), Toast.LENGTH_SHORT);
                }
                image_Adapter.AddItemList(fileList);
            } else
                Toast.makeText(Activity_Task_Submit.this, getString(R.string.Task_Submit_add_error), Toast.LENGTH_SHORT);
        }
        //Audio Search & Add
        if (requestCode == RequestCodes.Audio_Search_RC && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            String audio_path = getPath(this, uri);
            if (audio_path != null) {
                File audio = new File(audio_path);
                //通知Audio的Adapter更新
                if (audio.length() / 1024 <= 20480) audio_Adapter.AddFooterItem(audio);
                else
                    Toast.makeText(Activity_Task_Submit.this, getString(R.string.Task_Submit_add_size_error), Toast.LENGTH_SHORT);

            } else
                Toast.makeText(Activity_Task_Submit.this, getString(R.string.Task_Submit_add_error), Toast.LENGTH_SHORT);
        }
        //Video Search & Add
        if (requestCode == RequestCodes.Video_Search_RC && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            String video_path = getPath(this, uri);
            if (video_path != null) {
                File video = new File(video_path);
                if (video.length() / 1024 <= 20480) video_Adapter.AddFooterItem(video);
                else
                    Toast.makeText(Activity_Task_Submit.this, getString(R.string.Task_Submit_add_size_error), Toast.LENGTH_SHORT);
                //通知Audio的Adapter更新
            } else
                Toast.makeText(Activity_Task_Submit.this, getString(R.string.Task_Submit_add_error), Toast.LENGTH_SHORT);
        }

    }


    private void postRequest_Submit() {
        String subText = editText.getText().toString();
        int num_for_random = 0;
        final Context context = this;

        if (subText.equals("") || subText == null) {
            Toast.makeText(this, getResources().getString(R.string.Task_Submit_input_remind), Toast.LENGTH_LONG).show();
        } else {
            User_Task user_task = new User_Task(null, Integer.parseInt(getSharedPreferences("user", MODE_PRIVATE).getString("userID", "")), taskID, 1, subText, null, 0);
            Gson gson = new Gson();
            String postContent = gson.toJson(user_task);
            //创建Retrofit实例
            Retrofit retrofit = new Retrofit.Builder().baseUrl(getResources().getString(R.string.base_url)).addConverterFactory(GsonConverterFactory.create()).build();
            //Retrofit retrofit = new Retrofit.Builder().baseUrl("http://192.168.43.60:8889/").addConverterFactory(GsonConverterFactory.create()).build();
            //创建网络接口实例
            PostRequest_taskSubmit subRequest = retrofit.create(PostRequest_taskSubmit.class);
            //创建RequestBody
            RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), postContent);
            Call<ResponseBody> call = subRequest.task_submit(requestBody);
            Log.i(TAG, postContent);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Log.i(TAG, "Response code of text submission : " + response.code());
                    if (response.code() == 200) {
                        Toast.makeText(context, getResources().getString(R.string.Task_Submit_success_remind), Toast.LENGTH_LONG).show();
                        finish();
                    } else {
                        Toast.makeText(context, getResources().getString(R.string.Task_Submit_fail_remind), Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });

            /*
            //图片上传列表
            List<MultipartBody.Part> picList;
            if (selImageList.size() >= 1) {
                //创建提交内容对应的picList
                picList = new ArrayList<>(selImageList.size());
                for (ImageItem imageItem : selImageList) {
                    //通过ImageItem的path创建文件
                    File file = new File(imageItem.path);
                    RequestBody requestBody = RequestBody.create(MediaType.parse("image/png"), file);
                    //马克,这里名字是否为空判定
                    MultipartBody.Part part = MultipartBody.Part.createFormData("picList", imageItem.name + "_" + num_for_random++, requestBody);
                    picList.add(part);
                }
                call = subRequest.task_Submission(subText, picList);    //根据后台格式修改
            } else {
                call = subRequest.task_Submission(subText, null);       //根据后台格式修改
            }
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if(response.code() == 200){
                        Toast.makeText(context, "提交数据成功", Toast.LENGTH_LONG).show();
                    }
                }
                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });*/
        }
    }


    //以下皆为Uri转换path的功能代码
    public String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }


    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public String getDataColumn(Context context, Uri uri, String selection,
                                String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }
    //Uri转换path的功能代码模块结束


    @Override
    protected void onResume() {
        /* 测试用代码，现在搁置
        //获取支持的传感器列表
        List<Sensor> sensorList = sensorManager.getSensorList(Sensor.TYPE_ALL);
        String sensorS = "本机支持的传感器有：\n";
        for (Sensor sensor : sensorList) {
            String name = sensor.getName();
            int type = sensor.getType();
            sensorS += name + "  " + type + "\n";
        }
        //sensorS_tv.setText(sensorS);
        //获取温度、重力、加速度三个传感器信息
        Sensor sensor_gravity = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        Sensor sensor_acceler = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        Sensor sensor_temper = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        sensorManager.registerListener(sensorListener, sensor_acceler, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(sensorListener, sensor_gravity, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(sensorListener, sensor_temper, SensorManager.SENSOR_DELAY_NORMAL);
        */

        super.onResume();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //暂停使用，如果恢复registBroadcastReceiver()则要重启
        //unregisterReceiver(receiver);

        //数据库相关暂时注销
        //SQLdb.close();
    }
}

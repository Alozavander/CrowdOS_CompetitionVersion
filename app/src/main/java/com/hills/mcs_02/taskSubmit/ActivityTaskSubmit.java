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
import com.hills.mcs_02.dataBeans.FamiliarSensor;
import com.hills.mcs_02.dataBeans.UserTask;
import com.hills.mcs_02.networkClasses.interfacesPack.PostRequestTaskSubmit;
import com.hills.mcs_02.networkClasses.interfacesPack.PostRequestTaskSubmitFamiliarFiles;
import com.hills.mcs_02.networkClasses.interfacesPack.PostRequestTaskSubmitFiles;
import com.hills.mcs_02.sensorFunction.SenseFunction;
import com.hills.mcs_02.taskSubmit.uploadPack.ProgressRequestBody;
import com.hills.mcs_02.taskSubmit.uploadPack.UploadCallback;
import com.hills.mcs_02.viewsAdapters.AdapterRecyclerViewTaskSubmitAudio;
import com.hills.mcs_02.viewsAdapters.AdapterRecyclerViewTaskSubmitSenseData;
import com.hills.mcs_02.viewsAdapters.AdapterRecyclerViewTaskSubmitVideo;
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

public class ActivityTaskSubmit extends BaseActivity {
    public static final int IMAGE_ITEM_ADD = -1;
    public static final int REQUEST_CODE_SELECT = 100;
    public static final int REQUEST_CODE_PREVIEW = 101;
    public static final String TAG = "Activity_Task_Submit";
    public static final String DB_Path = "data/data/com.hills.mcs_02/cache" + File.separator + "sensorData" + File.separator + "sensorData.db";

    private Scroller mScroller;

    /** Allow to select the maximum number of pictures */
    private int maxImgCount = 8;
    private ArrayList<File> audioList;
    /** Audio List */
    private AdapterRecyclerViewTaskSubmitAudio audioAdapter;

    /** Video List */
    private ArrayList<File> videoList;
    private AdapterRecyclerViewTaskSubmitVideo videoAdapter;

    /** Sensing data file list */
    private ArrayList<File> senseDataList;
    private AdapterRecyclerViewTaskSubmitSenseData senseDataAdapter;

    private RecyclerView mRecyclerView;
    private AdapterRecyclerViewTaskSubmitImage imageAdapter;

    private NumberProgressBar mNumberProgressBar;
    /** Image List */
    private ArrayList<File> imageList;
    /** The amount of data that has been uploaded during the current total transfer */
    private long uploadedNow;

    private EditText editText;
    private SQLiteDatabase SqlDb;
    private SensorManager sensorManager;
    private TextView sensorDataShowTv;
    private BroadcastReceiver receiver;
    private Integer taskId;
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
        taskId = getIntent().getIntExtra(getResources().getString(R.string.intent_taskID_name), -1);
        if (taskId.equals(-1)) {
            Toast.makeText(this, getResources().getString(R.string.Task_Submit_requireTask_error), Toast.LENGTH_SHORT).show();
            finish();
        }

        initBackBT();

        findViewById(R.id.activity_taskSub_submit_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imageList.size() > 0 || videoList.size() > 0 || audioList.size() > 0 || senseDataList.size() > 0)
                    uploadWithFile();
                else postRequestSubmit();
            }
        });

        initImagePicker();
        initAudioPicker();
        initVideoPicker();

        sensorDataShowTv = (TextView) findViewById(R.id.activity_taskSub_sensorData_tv);
        initSensorDataPicker();

    }

    /** Determine which sensors are needed for the task based on the String passed by the Intent */
    private void initSensorDataPicker() {
        String sensorTypesString = getIntent().getStringExtra(getResources().getString(R.string.intent_taskSensorTypes_name));
        if (sensorTypesString.equals(StringStore.SP_STRING_ERROR)) concealDataChooseViews();
        else {
            String[] tempStirngs = sensorTypesString.split(StringStore.DIVIDER1);
            int[] sensorTypes = new int[tempStirngs.length];
            for (int temp = 0; temp < tempStirngs.length; temp++)
                sensorTypes[temp] = Integer.valueOf(tempStirngs[temp]);
            /** The sensor value given by GPS is -1 */
            if (sensorTypes.length == 1 && sensorTypes[0] == -1) concealDataChooseViews();
            else {
                /** Initialize the recyclerView and Adapter */
                senseDataList = new ArrayList<File>();
                /** Initialize the recyclerView of Audio */
                RecyclerView senseDataRv = findViewById(R.id.activity_taskSub_chooseData_rv);
                senseDataRv.setLayoutManager(new LinearLayoutManager(ActivityTaskSubmit.this, LinearLayoutManager.VERTICAL, false));
                senseDataAdapter = new AdapterRecyclerViewTaskSubmitSenseData(
                    ActivityTaskSubmit.this, senseDataList);
                senseDataRv.setAdapter(senseDataAdapter);

                /** Initialize the add button */
                Button dataAdd = findViewById(R.id.activity_taskSub_chooseData_add);
                /** Save and get the sensing data file according to the sensor type of the transmitted demand */
                dataAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        findViewById(R.id.activity_taskSub_pb_circle).setVisibility(View.VISIBLE);
                        findViewById(R.id.activity_taskSub_pb_circle_text).setVisibility(View.VISIBLE);
                        SenseFunction lSenseHelper = new SenseFunction(ActivityTaskSubmit.this);
                        for (int temp : sensorTypes) {
                            String timeStamp = System.currentTimeMillis() + "";
                            File tempFile = lSenseHelper.storeDataToCSV(temp, temp + "_senseType_senseData_" + timeStamp + ".csv", null);
                            senseDataAdapter.addFooterItem(tempFile);
                        }
                        findViewById(R.id.activity_taskSub_pb_circle).setVisibility(View.GONE);
                        findViewById(R.id.activity_taskSub_pb_circle_text).setVisibility(View.GONE);
                    }
                });
            }
        }
    }

    /** Hide the view related to sensor data choose */
    private void concealDataChooseViews() {
        findViewById(R.id.activity_taskSub_remind_3).setVisibility(View.GONE);
        findViewById(R.id.activity_taskSub_chooseData_rv).setVisibility(View.GONE);
        findViewById(R.id.activity_taskSub_chooseData_add).setVisibility(View.GONE);
    }

    private void initImagePicker() {
        imageList = new ArrayList<File>();
        RecyclerView recyclerView = findViewById(R.id.activity_taskSub_image_rv);
        imageAdapter = new AdapterRecyclerViewTaskSubmitImage(ActivityTaskSubmit.this, imageList);
        @SuppressLint("WrongConstant") GridLayoutManager manager = new GridLayoutManager(
            ActivityTaskSubmit.this, 3, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(imageAdapter);

        Button imageAdd = findViewById(R.id.activity_taskSub_image_add);
        imageAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<String> names = new ArrayList<>();
                names.add("相册");
                showDialog(new SelectDialog.SelectDialogListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        switch (position) {
                            case 0:
                                /** Enter the album and select the pictures */
                                PictureSelector.create(ActivityTaskSubmit.this)
                                        .openGallery(PictureMimeType.ofImage())
                                        .isCamera(false)
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
        RecyclerView audioRv = findViewById(R.id.activity_taskSub_audio_rv);
        audioRv.setLayoutManager(new LinearLayoutManager(ActivityTaskSubmit.this, LinearLayoutManager.VERTICAL, false));
        audioAdapter = new AdapterRecyclerViewTaskSubmitAudio(ActivityTaskSubmit.this, audioList);
        audioRv.setAdapter(audioAdapter);

        Button audioAdd = findViewById(R.id.activity_taskSub_audio_add);
        audioAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("audio/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, RequestCodes.AUDIO_SEARCH_RC);
            }
        });
    }

    @SuppressLint("WrongConstant")
    private void initVideoPicker() {
        videoList = new ArrayList<File>();
        RecyclerView videoRv = findViewById(R.id.activity_taskSub_video_rv);
        videoRv.setLayoutManager(new LinearLayoutManager(ActivityTaskSubmit.this, LinearLayoutManager.VERTICAL, false));
        videoAdapter = new AdapterRecyclerViewTaskSubmitVideo(ActivityTaskSubmit.this, videoList);
        videoRv.setAdapter(videoAdapter);

        Button videoAdd = findViewById(R.id.activity_taskSub_video_add);
        videoAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("video/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, RequestCodes.VIDEO_SEARCH_RC);
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
        ImageView backIv = findViewById(R.id.activity_taskSub_backarrow);
        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void uploadWithFile() {
        mNumberProgressBar.setVisibility(View.VISIBLE);

        if (senseDataList != null) {
            if (senseDataList.size() > 0) {
                for (File file : senseDataList) {
                    uploadedNow = 0;
                    /**  Realize upload progress monitoring */
                    ProgressRequestBody requestBody = new ProgressRequestBody(file, "*/*", new UploadCallback() {
                        @Override
                        public void onProgressUpdate(long uploaded) {
                            /** Set the  real-time  progress bar */
                            uploadedNow += uploaded;
                            mNumberProgressBar.setProgress((int) (100 * uploadedNow / file.length()));
                        }

                        @Override
                        public void onError() {

                        }
                        @Override
                        public void onFinish() {

                        }
                    });
                    MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestBody);

                    String subText = editText.getText().toString();

                    if (subText.equals("") || subText == null) {
                        Toast.makeText(this, getResources().getString(R.string.Task_Submit_input_remind), Toast.LENGTH_LONG).show();
                        mNumberProgressBar.setVisibility(View.GONE);
                    } else {
                        //todo: upload the files class type -> 'Familiar_sensor'
                        String lsType = (file.getName().split("_"))[0];
                        FamiliarSensor lFamiliarSensor = new FamiliarSensor(null, Integer.parseInt(getSharedPreferences("user", MODE_PRIVATE).getString("userID", "")),
                            taskId, (float) -9999, (float) -9999, (float) -1, Float.parseFloat(lsType), null);
                        Gson gson = new Gson();
                        String postContent = gson.toJson(lFamiliarSensor);

                        /** Create the Retrofit instance */
                        Retrofit retrofit = new Retrofit.Builder().baseUrl(getResources().getString(R.string.base_url)).addConverterFactory(GsonConverterFactory.create()).build();

                        PostRequestTaskSubmitFamiliarFiles subRequest = retrofit.create(
                            PostRequestTaskSubmitFamiliarFiles.class);
                        /** Create the RequestBody */
                        RequestBody contentBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), postContent);
                        Call<ResponseBody> call = subRequest.taskSubmit(contentBody, body);
                        Log.i(TAG, "post Content of sensor data files request body: " + postContent);

                        call.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                Log.i(TAG, "response of sensor data submission code: " + response.code());
                                if (response.code() == 200) {
                                    Toast.makeText(ActivityTaskSubmit.this, getResources().getString(R.string.Task_Submit_success_remind), Toast.LENGTH_LONG).show();
                                    mNumberProgressBar.setVisibility(View.GONE);
                                    finish();
                                } else {
                                    Toast.makeText(ActivityTaskSubmit.this, getResources().getString(R.string.Task_Submit_fail_remind), Toast.LENGTH_LONG).show();
                                    mNumberProgressBar.setVisibility(View.GONE);
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable throwable) {

                            }
                        });
                    }
                }
            } else Log.i(TAG, "sensor data files list has no files.");
        } else Log.i(TAG, "sensor data files list is null");

        /** image upload */
        if (imageList.size() > 0) {
            for (File file : imageList) {
                uploadedNow = 0;
                ProgressRequestBody requestBody = new ProgressRequestBody(file, "image/*", new UploadCallback() {
                    @Override
                    public void onProgressUpdate(long uploaded) {
                        uploadedNow += uploaded;
                        mNumberProgressBar.setProgress((int) (100 * uploadedNow / file.length()));
                    }

                    @Override
                    public void onError() {

                    }

                    @Override
                    public void onFinish() {

                    }
                });
                MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
                String subText = editText.getText().toString();

                if (subText.equals("") || subText == null) {
                    Toast.makeText(this, getResources().getString(R.string.Task_Submit_input_remind), Toast.LENGTH_LONG).show();
                    mNumberProgressBar.setVisibility(View.GONE);
                } else {
                    UserTask userTask = new UserTask(null, Integer.parseInt
                            (getSharedPreferences("user", MODE_PRIVATE).getString("userID", "")),
                        taskId, 1, subText, null, 1);
                    Gson gson = new Gson();
                    String postContent = gson.toJson(userTask);
                    Retrofit retrofit = new Retrofit.Builder().baseUrl(getResources().getString(R.string.base_url)).addConverterFactory(GsonConverterFactory.create()).build();

                    PostRequestTaskSubmitFiles subRequest = retrofit.create(
                        PostRequestTaskSubmitFiles.class);

                    RequestBody contentBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), postContent);
                    Call<ResponseBody> call = subRequest.taskSubmit(contentBody, body);
                    Log.i(TAG, postContent);
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.code() == 200) {          //成功提交任务处理
                                Toast.makeText(ActivityTaskSubmit.this, getResources().getString(R.string.Task_Submit_success_remind), Toast.LENGTH_LONG).show();
                                mNumberProgressBar.setVisibility(View.GONE);
                                finish();
                            } else {
                                Toast.makeText(ActivityTaskSubmit.this, getResources().getString(R.string.Task_Submit_fail_remind), Toast.LENGTH_LONG).show();
                                mNumberProgressBar.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable throwable) {

                        }
                    });
                }

            }
        } else Log.i(TAG, "image files list has no files.");

        /** audio upload */
        if (audioList.size() > 0) {
            for (File file : audioList) {
                uploadedNow = 0;
                ProgressRequestBody requestBody = new ProgressRequestBody(file, "audio/*", new UploadCallback() {
                    @Override
                    public void onProgressUpdate(long uploaded) {
                        uploadedNow += uploaded;
                        mNumberProgressBar.setProgress((int) (100 * uploadedNow / file.length()));
                    }

                    @Override
                    public void onError() {

                    }

                    @Override
                    public void onFinish() {

                    }
                });
                MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestBody);

                String subText = editText.getText().toString();

                if (subText.equals("") || subText == null) {
                    Toast.makeText(this, getResources().getString(R.string.Task_Submit_input_remind), Toast.LENGTH_LONG).show();
                    mNumberProgressBar.setVisibility(View.GONE);
                } else {
                    UserTask userTask = new UserTask(null, Integer.parseInt(getSharedPreferences("user", MODE_PRIVATE).getString("userID", "")),
                        taskId, 1, subText, null, 2);
                    Gson gson = new Gson();
                    String postContent = gson.toJson(userTask);

                    Retrofit retrofit = new Retrofit.Builder().baseUrl(getResources().getString(R.string.base_url)).addConverterFactory(GsonConverterFactory.create()).build();

                    PostRequestTaskSubmitFiles subRequest = retrofit.create(
                        PostRequestTaskSubmitFiles.class);

                    RequestBody contentBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), postContent);
                    Call<ResponseBody> call = subRequest.taskSubmit(contentBody, body);
                    Log.i(TAG, postContent);

                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.code() == 200) {
                                Toast.makeText(ActivityTaskSubmit.this, getResources().getString(R.string.Task_Submit_success_remind), Toast.LENGTH_LONG).show();
                                mNumberProgressBar.setVisibility(View.GONE);
                                finish();
                            } else {
                                Toast.makeText(ActivityTaskSubmit.this, getResources().getString(R.string.Task_Submit_fail_remind), Toast.LENGTH_LONG).show();
                                mNumberProgressBar.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable throwable) {

                        }
                    });
                }
            }
        } else Log.i(TAG, "audio files list has no files.");

        /** video upload */
        if (videoList.size() > 0) {
            for (File file : videoList) {
                uploadedNow = 0;
                ProgressRequestBody requestBody = new ProgressRequestBody(file, "video/*", new UploadCallback() {
                    @Override
                    public void onProgressUpdate(long uploaded) {
                        mNumberProgressBar.setProgress((int) (100 * uploadedNow / file.length()));
                    }

                    @Override
                    public void onError() {

                    }

                    @Override
                    public void onFinish() {

                    }
                });

                MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestBody);

                String subText = editText.getText().toString();

                if (subText.equals("") || subText == null) {
                    Toast.makeText(this, getResources().getString(R.string.Task_Submit_input_remind), Toast.LENGTH_LONG).show();
                    mNumberProgressBar.setVisibility(View.GONE);
                } else {
                    UserTask userTask = new UserTask(null, Integer.parseInt(getSharedPreferences("user", MODE_PRIVATE).getString("userID", "")),
                        taskId, 1, subText, null, 3);
                    Gson gson = new Gson();
                    String postContent = gson.toJson(userTask);

                    Retrofit retrofit = new Retrofit.Builder().baseUrl(getResources().getString(R.string.base_url)).addConverterFactory(GsonConverterFactory.create()).build();
                    PostRequestTaskSubmitFiles subRequest = retrofit.create(
                        PostRequestTaskSubmitFiles.class);
                    //创建RequestBody
                    RequestBody contentBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), postContent);
                    Call<ResponseBody> call = subRequest.taskSubmit(contentBody, body);
                    Log.i(TAG, postContent);

                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.code() == 200) {
                                Toast.makeText(ActivityTaskSubmit.this, getResources().getString(R.string.Task_Submit_success_remind), Toast.LENGTH_LONG).show();
                                mNumberProgressBar.setVisibility(View.GONE);
                                finish();
                            } else {
                                Toast.makeText(ActivityTaskSubmit.this, getResources().getString(R.string.Task_Submit_fail_remind), Toast.LENGTH_LONG).show();
                                mNumberProgressBar.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable throwable) {

                        }
                    });
                }
            }
        } else Log.i(TAG, "video files list has no files.");
        /** Finnaly upload the text */
        postRequestSubmit();
    }

    private void registerBroadcastReceiver() {
        /** SDU = Sense Data Update */
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
                    Cursor cursor = SqlDb.rawQuery(sql, null);
                    if (cursor.moveToFirst()) {
                        data = cursor.getString(0);
                    }
                    cursor.close();
                    text += sensorName + ":  " + data + "\n";
                }
                Log.e(TAG, "接受到SDU广播，当前Text：\n" + text);
                sensorDataShowTv.setText(text);
            }
        };
        registerReceiver(receiver, intentFilter);
        Log.e(TAG, "注册了广播接收器");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /** Image Search & Add */
        if (requestCode == PictureConfig.CHOOSE_REQUEST && resultCode == Activity.RESULT_OK) {
            List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
            List<File> fileList = new ArrayList<File>();
            if (selectList.size() > 0) {
                for (LocalMedia media : selectList) {
                    File temFile = new File(media.getPath());
                    /** 20MB size limit */
                    if (temFile.length() / 1024 <= 20480) fileList.add(temFile);
                    else
                        Toast.makeText(ActivityTaskSubmit.this, getString(R.string.Task_Submit_add_size_error), Toast.LENGTH_SHORT);
                }
                imageAdapter.addItemList(fileList);
            } else
                Toast.makeText(ActivityTaskSubmit.this, getString(R.string.Task_Submit_add_error), Toast.LENGTH_SHORT);
        }
        /** Audio Search & Add */
        if (requestCode == RequestCodes.AUDIO_SEARCH_RC && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            String audioPath = getPath(this, uri);
            if (audioPath != null) {
                File audio = new File(audioPath);
                /** notify the audio adapter to update */
                if (audio.length() / 1024 <= 20480) audioAdapter.addFooterItem(audio);
                else
                    Toast.makeText(ActivityTaskSubmit.this, getString(R.string.Task_Submit_add_size_error), Toast.LENGTH_SHORT);

            } else
                Toast.makeText(ActivityTaskSubmit.this, getString(R.string.Task_Submit_add_error), Toast.LENGTH_SHORT);
        }
        /** Video Search & Add */
        if (requestCode == RequestCodes.VIDEO_SEARCH_RC && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            String videoPath = getPath(this, uri);
            if (videoPath != null) {
                File video = new File(videoPath);
                if (video.length() / 1024 <= 20480) videoAdapter.addFooterItem(video);
                else
                    Toast.makeText(ActivityTaskSubmit.this, getString(R.string.Task_Submit_add_size_error), Toast.LENGTH_SHORT);
            } else
                Toast.makeText(ActivityTaskSubmit.this, getString(R.string.Task_Submit_add_error), Toast.LENGTH_SHORT);
        }

    }

    /** Upload the text */
    private void postRequestSubmit() {
        String subText = editText.getText().toString();
        int numForRandom = 0;
        final Context context = this;

        if (subText.equals("") || subText == null) {
            Toast.makeText(this, getResources().getString(R.string.Task_Submit_input_remind), Toast.LENGTH_LONG).show();
        } else {
            UserTask userTask = new UserTask(null, Integer.parseInt(getSharedPreferences("user", MODE_PRIVATE).getString("userID", "")),
                taskId, 1, subText, null, 0);
            Gson gson = new Gson();
            String postContent = gson.toJson(userTask);

            Retrofit retrofit = new Retrofit.Builder().baseUrl(getResources().getString(R.string.base_url)).addConverterFactory(GsonConverterFactory.create()).build();

            PostRequestTaskSubmit subRequest = retrofit.create(PostRequestTaskSubmit.class);

            RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), postContent);
            Call<ResponseBody> call = subRequest.taskSubmit(requestBody);
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
                public void onFailure(Call<ResponseBody> call, Throwable throwable) {

                }
            });
        }
    }

    /** Uri conversion path function code */
    public String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        /** DocumentProvider */
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            /** ExternalStorageProvider */
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            /** DownloadsProvider */
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            /** MediaProvider */
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
        /** Media Store */
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        /** File Store */
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }


    /** Get the value of the data column for this Uri. This is useful for MediaStore Uris, and other file-based ContentProviders. */
    public String getDataColumn(Context context, Uri uri, String selection,
                                String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int COLUMN_INDEX = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(COLUMN_INDEX);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**  Return the result whether the Uri authority is ExternalStorageProvider. */
    public boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**  Whether the Uri authority is DownloadsProvider. */
    public boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /** Return the reuslt that whether the Uri authority is MediaProvider.*/
    public boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

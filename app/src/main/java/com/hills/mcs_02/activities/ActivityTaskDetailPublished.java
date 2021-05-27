package com.hills.mcs_02.activities;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.hills.mcs_02.BaseActivity;
import com.hills.mcs_02.dataBeans.BeanUserTaskWithUser;
import com.hills.mcs_02.dataBeans.UserTaskWithUser;
import com.hills.mcs_02.dataBeans.Task;
import com.hills.mcs_02.downloadPack.DownloadImageUtils;
import com.hills.mcs_02.networkClasses.interfacesPack.PostRequestPublishedTaskDetail;
import com.hills.mcs_02.R;
import com.hills.mcs_02.viewsAdapters.AdapterRecyclerViewPublishedTaskDetail;

public class ActivityTaskDetailPublished extends BaseActivity {

    private final String TAG = "activity_task_detail_published";
    private Task task;
    private TextView usernameTv;
    private TextView postTimeTv;
    private TextView taskKindTv;
    private TextView taskContentTv;
    private TextView coinCountTv;
    private TextView deadlineTv;
    private TextView taskNameTv;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private AdapterRecyclerViewPublishedTaskDetail recyclerAdapter;
    private List<BeanUserTaskWithUser> mList;
    private Set<Integer> mHashSetTaskId;


    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail_published);

        task = new Task();
        usernameTv = findViewById(R.id.published_taskDetail_userName);
        taskContentTv = findViewById(R.id.published_taskDetail_content);
        coinCountTv = findViewById(R.id.published_taskDetail_coin);
        deadlineTv = findViewById(R.id.published_taskDetail_deadline);
        postTimeTv = findViewById(R.id.published_taskDetail_postTime);
        taskNameTv = findViewById(R.id.published_taskDetail_taskName);
        taskKindTv = findViewById(R.id.published_taskDetail_taskKind);
        mHashSetTaskId = new HashSet<Integer>();

        if (mList == null) {
            mList = new ArrayList<BeanUserTaskWithUser>();
        }

        /**  Initialize Slide Layout and RecyclerView */
        mSwipeRefreshLayout = findViewById(R.id.published_taskDetail_swiperefreshLayout);
        mSwipeRefreshLayout.setColorSchemeColors(Color.RED, Color.BLUE, Color.GREEN);
        mRecyclerView = findViewById(R.id.published_taskDetail_RecyclerView);
        initData();
        initBackBtn();
        recyclerAdapter = new AdapterRecyclerViewPublishedTaskDetail(this, mList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(recyclerAdapter);
        initRefreshListener();
    }

    private void initData() {
        String taskGson = getIntent().getStringExtra("taskGson");
        Gson gson = new Gson();
        task = gson.fromJson(taskGson, Task.class);
        usernameTv.setText(task.getUsername());
        taskContentTv.setText(task.getDescribeTask());
        coinCountTv.setText(task.getCoin().toString());
        taskNameTv.setText(task.getTaskName());
        deadlineTv.setText(new SimpleDateFormat("yyyy.MM.dd").format(task.getDeadline()));
        postTimeTv.setText(new SimpleDateFormat("yyyy.MM.dd").format(task.getPostTime()));
        switch (task.getTaskKind()) {
            case 0:
                taskKindTv.setText(getString(R.string.home_grid_0));
                break;
            case 1:
                taskKindTv.setText(getString(R.string.home_grid_1));
                break;
            case 2:
                taskKindTv.setText(getString(R.string.home_grid_2));
                break;
            case 3:
                taskKindTv.setText(getString(R.string.home_grid_3));
                break;
            case 4:
                taskKindTv.setText(getString(R.string.home_grid_4));
                break;
        }
        firstFresh();
    }

    private void firstFresh() {
        mSwipeRefreshLayout.setRefreshing(true);
        new Handler().postDelayed(new Runnable() {
                                      @Override
                                      public void run() {
                                          postRequest(0);
                                      }
                                  }, 3000
        );

    }


    private void initRefreshListener() {
        initPullRefresh();  /** Pull up to refresh */
    }

    private void initPullRefresh() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                                              @Override
                                              public void run() {
                                                  postRequest(1);
                                              }

                                          }, 3000
                );
            }
        });
    }


    public void postRequest(int tag) {
        final int TEMP_TAG = tag;
        /** Create a Retrofit object */
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.base_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        /** Instantiate the network interface */
        PostRequestPublishedTaskDetail request = retrofit.create(PostRequestPublishedTaskDetail.class);

        /** Initialize the RequestBody */
        Call<ResponseBody> call = request.checkUserTaskWithUsername(task.getTaskId());

        final Context CONTEXT = this;

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    try {
                        /** Get the List of data returned by the response  */
                        List<UserTaskWithUser> responseList = new ArrayList<UserTaskWithUser>();
                       /** Create a List that conforms to the List data format and displays on the page */
                        List<BeanUserTaskWithUser> tempList = new ArrayList<BeanUserTaskWithUser>();
                        Gson gson = new Gson();
                        Type type = new TypeToken<List<UserTaskWithUser>>() {
                        }.getType();
                        String tempContent = response.body().string();
                        Log.i(TAG, "接受的报文内容：" + tempContent);
                        responseList = gson.fromJson(tempContent, type);
                        if (responseList.size() > 0) {
                            /** Convert between two lists */
                            for (UserTaskWithUser userTaskWihUser : responseList) {
                                if (!mHashSetTaskId.contains(userTaskWihUser.getUserTask().getUserTaskId())) {
                                    mHashSetTaskId.add(userTaskWihUser.getUserTask().getUserTaskId());
                                    if (userTaskWihUser.getUserTask().getImage() != null) {
                                        DownloadImageUtils utils = new DownloadImageUtils(getString(R.string.base_url));
                                        tempList.add(new BeanUserTaskWithUser(R.drawable.haimian_usericon, utils.downloadFile(userTaskWihUser.getUserTask().getImage()), userTaskWihUser));
                                    } else {
                                        tempList.add(new BeanUserTaskWithUser(R.drawable.haimian_usericon, userTaskWihUser));
                                        Log.i(TAG, userTaskWihUser.toString());
                                    }
                                }
                            }
                        } else {
                            Toast.makeText(ActivityTaskDetailPublished.this, getResources().getString(R.string.FailToGetData) + responseList.size(), Toast.LENGTH_SHORT).show();
                        }
                        /** The first refresh or subsequent pull-up refresh and pull-down load is determined by the tag */
                        if (TEMP_TAG == 0) mList.addAll(tempList);
                        else if (TEMP_TAG == 1) recyclerAdapter.addHeaderItem(tempList);
                        else recyclerAdapter.addFooterItem(tempList);


                        if (mSwipeRefreshLayout.isRefreshing())
                            mSwipeRefreshLayout.setRefreshing(false);
                    } catch (IOException exp) {
                        exp.printStackTrace();
                    }
                } else {
                    if (mSwipeRefreshLayout.isRefreshing())
                        mSwipeRefreshLayout.setRefreshing(false);
                    Log.e(TAG, "查询任务完成结果失败");
                    Toast.makeText(CONTEXT, getResources().getString(R.string.NoneQueryResult), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {

            }
        });

        loadPicture();
    }

    private void loadPicture() {

    }


    private void initBackBtn() {
        ImageView backIv = findViewById(R.id.published_taskDetail_backarrow);
        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}

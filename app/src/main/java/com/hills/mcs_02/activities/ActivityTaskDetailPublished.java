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


import com.hills.mcs_02.BaseActivity;
import com.hills.mcs_02.dataBeans.Bean_Combine_u_ut;
import com.hills.mcs_02.dataBeans.Combine_u_ut;
import com.hills.mcs_02.dataBeans.Task;
import com.hills.mcs_02.downloadPack.DownloadImageUtils;
import com.hills.mcs_02.networkClasses.interfacesPack.PostRequestPublishedTaskDetail;
import com.hills.mcs_02.R;
import com.hills.mcs_02.viewsAdapters.Adapter_RecyclerView_Published_TaskDetail;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;



public class ActivityTaskDetailPublished extends BaseActivity {

    private final String TAG = "activity_task_detail_published";
    private Task task;
    private TextView userNameTv;
    private TextView postTimeTv;
    private TextView taskKindTv;
    private TextView taskContentTv;
    private TextView coinsCountTv;
    private TextView deadlineTv;
    private TextView taskNameTv;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private Adapter_RecyclerView_Published_TaskDetail recyclerAdapter;
    private List<Bean_Combine_u_ut> mList;
    private Set<Integer> mHashSetTaskId;                                             //用于获取感知任务去重


    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail_published);

        task = new Task();
        userNameTv = findViewById(R.id.published_taskDetail_userName);
        taskContentTv = findViewById(R.id.published_taskDetail_content);
        coinsCountTv = findViewById(R.id.published_taskDetail_coin);
        deadlineTv = findViewById(R.id.published_taskDetail_deadline);
        postTimeTv = findViewById(R.id.published_taskDetail_postTime);
        taskNameTv = findViewById(R.id.published_taskDetail_taskName);
        taskKindTv = findViewById(R.id.published_taskDetail_taskKind);
        mHashSetTaskId = new HashSet<Integer>();

        if (mList == null) {
            mList = new ArrayList<Bean_Combine_u_ut>();
        }

        //初始化滑动布局与recyclerview
        mSwipeRefreshLayout = findViewById(R.id.published_taskDetail_swiperefreshLayout);
        mSwipeRefreshLayout.setColorSchemeColors(Color.RED, Color.BLUE, Color.GREEN);
        mRecyclerView = findViewById(R.id.published_taskDetail_RecyclerView);
        initData();
        initBackBT();
        recyclerAdapter = new Adapter_RecyclerView_Published_TaskDetail(this, mList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(recyclerAdapter);
        initRefreshListener();
    }

    private void initData() {
        String taskGson = getIntent().getStringExtra("taskGson");
        Gson gson = new Gson();
        task = gson.fromJson(taskGson, Task.class);
        userNameTv.setText(task.getUserName());
        taskContentTv.setText(task.getDescribe_task());
        coinsCountTv.setText(task.getCoin().toString());
        taskNameTv.setText(task.getTaskName());
        deadlineTv.setText(new SimpleDateFormat("yyyy.MM.dd").format(task.getDeadLine()));
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
        first_fresh();
    }

    private void first_fresh() {
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
        initPullRefresh();  //上拉刷新
        //initLoadMoreListener();  //下拉加载更多
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


    private void initLoadMoreListener() {

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int lastVisibleItem;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                //判断RecyclerView的状态 是空闲时，同时，是最后一个可见的ITEM时才加载
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == recyclerAdapter.getItemCount()) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            postRequest(2);
                        }
                    }, 3000);

                }

            }

            //更新lastvisibleItem数值
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                //最后一个可见的ITEM
                lastVisibleItem = layoutManager.findLastVisibleItemPosition();
            }
        });
    }

    public void postRequest(int tag) {
        final int TEMP_TAG = tag;
        //创建Retrofit对象
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.base_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //创建网络接口实例
        PostRequestPublishedTaskDetail request = retrofit.create(PostRequestPublishedTaskDetail.class);


        //包装发送请求
        Call<ResponseBody> call = request.checkUserTaskWithUsername(task.getTaskId());

        final Context context = this;

        //异步网络请求
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    try {
                        //取得响应返回的数据List
                        List<Combine_u_ut> responseList = new ArrayList<Combine_u_ut>();
                        //创建符合List数据格式，在页面显示的列表
                        List<Bean_Combine_u_ut> tempList = new ArrayList<Bean_Combine_u_ut>();
                        Gson gson = new Gson();
                        Type type = new TypeToken<List<Combine_u_ut>>() {
                        }.getType();
                        String temp_content = response.body().string();
                        Log.i(TAG, "接受的报文内容：" + temp_content);
                        responseList = gson.fromJson(temp_content, type);
                        if (responseList.size() > 0) {
                            //两个List之间的转换
                            for (Combine_u_ut u_ut : responseList) {
                                //HashSet去重
                                if (!mHashSetTaskId.contains(u_ut.getUt().getUser_taskId())) {
                                    mHashSetTaskId.add(u_ut.getUt().getUser_taskId());
                                    if (u_ut.getUt().getImage() != null) {
                                        //在此处增加图片下载的功能代码
                                        DownloadImageUtils utils = new DownloadImageUtils(getString(R.string.base_url));
                                        tempList.add(new Bean_Combine_u_ut(R.drawable.haimian_usericon, utils.downloadFile(u_ut.getUt().getImage()), u_ut));
                                    } else {
                                        tempList.add(new Bean_Combine_u_ut(R.drawable.haimian_usericon, u_ut));
                                        Log.i(TAG, u_ut.toString());
                                    }
                                }
                            }
                        } else {
                            Toast.makeText(ActivityTaskDetailPublished.this, getResources().getString(R.string.FailToGetData) + responseList.size(), Toast.LENGTH_SHORT).show();
                        }
                        //根据tag判断是第一次刷新还是后续的上拉刷新和下拉加载
                        if (TEMP_TAG == 0) mList.addAll(tempList);
                        else if (TEMP_TAG == 1) recyclerAdapter.AddHeaderItem(tempList);
                        else recyclerAdapter.AddFooterItem(tempList);

                        //刷新完成
                        if (mSwipeRefreshLayout.isRefreshing())
                            mSwipeRefreshLayout.setRefreshing(false);
                    } catch (IOException exp) {
                        exp.printStackTrace();
                    }
                } else {
                    if (mSwipeRefreshLayout.isRefreshing())
                        mSwipeRefreshLayout.setRefreshing(false);
                    Log.e(TAG, "查询任务完成结果失败");
                    Toast.makeText(context, getResources().getString(R.string.NoneQueryResult), Toast.LENGTH_SHORT).show();
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


    private void initBackBT() {
        ImageView backIv = findViewById(R.id.published_taskDetail_backarrow);
        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}

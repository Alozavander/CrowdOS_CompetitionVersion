package com.hills.mcs_02.activities;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import com.hills.mcs_02.BaseActivity;
import com.hills.mcs_02.dataBeans.Bean_ListView_home;
import com.hills.mcs_02.dataBeans.Task;
import com.hills.mcs_02.fragmentsPack.MCSRecyclerItemClickListener;
import com.hills.mcs_02.networkClasses.interfacesPack.GetNewTenRequestHomeTaskList;
import com.hills.mcs_02.networkClasses.interfacesPack.PostRequestGridPageTaskList;
import com.hills.mcs_02.R;
import com.hills.mcs_02.viewsAdapters.Adapter_RecyclerView_home;


import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;



public class ActivityGridPage extends BaseActivity {
    private String TAG = "Activity_gridPage";
    private int pageTag = -1;
    private RecyclerView mRecyclerView;                                              //为首页显示任务列表的RecyclerView
    private List<Bean_ListView_home> mBeanListViewGridPage;                           //为上述ListView准备的数据链表
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private Adapter_RecyclerView_home recyclerAdapter;
    private List<Task> mRequestTaskList;
    private Set<Integer> mHashSetTaskId;                                             //用于获取感知任务去重
    int[] photoPath = {R.drawable.testphoto_1, R.drawable.testphoto_2, R.drawable.testphoto_3, R.drawable.testphoto_4};
    private String pageName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gridpage);
        mHashSetTaskId = new HashSet<Integer>();
        mRequestTaskList = new ArrayList<Task>();

        pageInit();
        initRecyclerView();
    }

    private void pageInit() {
        //通过intent取出不同的title标志
        Intent intent = getIntent();
        pageName = intent.getStringExtra("pageName");
        TextView titleTv = findViewById(R.id.gridpage_title);
        switch (pageName) {
            case "Security": titleTv.setText(getString(R.string.home_grid_0));pageTag = 0;break;
            case "Environment":titleTv.setText(getString(R.string.home_grid_1));pageTag = 1;break;
            case "Daily Life":titleTv.setText(getString(R.string.home_grid_2));pageTag = 2;break;
            case "Business":titleTv.setText(getString(R.string.home_grid_3));pageTag = 3;break;
            case "More":titleTv.setText(getString(R.string.home_grid_4));pageTag = 4;break;
        }
    }


    @SuppressLint("WrongConstant")
    private void initRecyclerView() {
        //第一次初始化任务
        if (mBeanListViewGridPage == null) {
            mBeanListViewGridPage = new ArrayList<Bean_ListView_home>();

            /*/测试使用
            for(int i = 0; i < 10; i++){
                Bean_ListView_home bean_1 = new Bean_ListView_home(R.drawable.cat_usericon, R.drawable.takephoto, R.drawable.star_1, R.drawable.testphoto_1, "猫某", "1小时前", "裂缝识别", "任务描述：该任务是为拍照裂缝……", new Random().nextInt(100) + "元", new Random().nextInt(10) + " 个", "截止时间：2018.12.25");
                mBean_ListView_gridPage.add(bean_1);
            }*/
        }

        //初始化SwipeRefreshLayout
        mSwipeRefreshLayout = findViewById(R.id.gridpage_swiperefreshLayout);
        mSwipeRefreshLayout.setColorSchemeColors(Color.RED, Color.BLUE, Color.GREEN);
        mRecyclerView = findViewById(R.id.gridpage_RecyclerView);
        //进入页面初始化任务列表
        firstListRefresh();
        recyclerAdapter = new Adapter_RecyclerView_home(ActivityGridPage.this, mBeanListViewGridPage);
        //recyclerView没有跟listView一样封装OnItemClickListener，所以只能手动实现，这里是将监听器绑定在了适配器上
        recyclerAdapter.setRecyclerItemClickListener(new MCSRecyclerItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                int loginUserId = Integer.parseInt(ActivityGridPage.this.getSharedPreferences("user", MODE_PRIVATE).getString("userID", "-1"));
                //检查是否登录
                if (loginUserId == -1) {
                    Toast.makeText(ActivityGridPage.this, getResources().getString(R.string.login_first), Toast.LENGTH_SHORT);
                    Intent intent = new Intent(ActivityGridPage.this, ActivityLogin.class);
                    startActivity(intent);
                }else{
                    Gson gson = new Gson();
                    Intent intent = new Intent(ActivityGridPage.this, ActivityTaskDetail.class);
                    intent.putExtra("taskGson",gson.toJson(mBeanListViewGridPage.get(position).getTask()));
                    startActivity(intent);
                }
            }
        });

        mRecyclerView.setLayoutManager(new LinearLayoutManager(ActivityGridPage.this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(recyclerAdapter);

        initRefreshListener();
    }

    //第一次刷新列表
    private void firstListRefresh() {
        mSwipeRefreshLayout.setRefreshing(true);
        new Handler().postDelayed(new Runnable() {
                                      @Override
                                      public void run() {
                                          postRequest(0);
                                      }

                                  }, 3000
        );
    }


    //初始化刷新监听，包含再请求网络数据传输
    private void initRefreshListener() {
        initPullRefresh();  //上拉刷新
        initLoadMoreListener();  //下拉加载更多
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
                            //getNewTenTaskRequest();
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
        //根据tag标记不同判定添加到列表开头（1）还是列表尾部（2）,0为初始刷新使用
        final int tempTag = tag;
        //创建Retrofit对象
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(this.getString(R.string.base_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //创建网络接口实例
        PostRequestGridPageTaskList requestGetTaskList = retrofit.create(
            PostRequestGridPageTaskList.class);
        //包装发送请求
        Call<ResponseBody> call = requestGetTaskList.getCall(pageTag);

        //异步网络请求
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    Gson gson = new GsonBuilder().setDateFormat("yyyy.MM.dd").create();
                    Type type = new TypeToken<List<Task>>() {}.getType();
                    try {
                        //在此附近应该加入内容判定，优化响应逻辑
                        String temp = response.body().string();
                        Log.i(TAG, temp);
                        mRequestTaskList = gson.fromJson(temp, type);
                        //成功获取网络请求内容后，调用内容处理方法
                        //Toast.makeText(Activity_gridPage.this,temp,Toast.LENGTH_SHORT).show();
                        Log.i(TAG, mRequestTaskList.size() + "");
                        List<Bean_ListView_home> tempList = new ArrayList<Bean_ListView_home>();
                        if (mRequestTaskList.size() > 0) {
                            for (Task task : mRequestTaskList) {
                                Log.i(TAG,task.toString());
                                if (!mHashSetTaskId.contains(task.getTaskId())) {
                                    mHashSetTaskId.add(task.getTaskId());
                                    tempList.add(new Bean_ListView_home(R.drawable.cat_usericon, task.getUserName(), photoPath[new Random().nextInt(3)], "普通任务", task));
                                }
                            }
                        } else {
                            Toast.makeText(ActivityGridPage.this, getResources().getString(R.string.FailToGetData) + mRequestTaskList.size(), Toast.LENGTH_SHORT).show();
                        }
                        if (tempTag == 0) mBeanListViewGridPage.addAll(tempList);
                        else if(tempTag == 1)recyclerAdapter.AddHeaderItem(tempList);
                        else recyclerAdapter.AddFooterItem(tempList);


                        //刷新完成
                        if (mSwipeRefreshLayout.isRefreshing())
                            mSwipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(ActivityGridPage.this, getResources().getString(R.string.Refresh) + tempList.size() + getResources().getString(R.string.Now) + mBeanListViewGridPage.size() + getResources().getString(R.string.TaskNum), Toast.LENGTH_SHORT).show();
                    } catch (IOException exp) {
                        exp.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {

            }
        });
    }


    private void getNewTenTaskRequest(){
        //创建Retrofit对象
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(this.getString(R.string.base_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //创建网络接口实例
        GetNewTenRequestHomeTaskList requestGetTaskList = retrofit.create(
            GetNewTenRequestHomeTaskList.class);
        //包装发送请求
        Call<ResponseBody> call = requestGetTaskList.queryNewTenTask(minTaskId());


        //异步网络请求
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    Gson gson = new GsonBuilder().setDateFormat("yyyy.MM.dd").create();
                    Type type = new TypeToken<List<Task>>() {}.getType();
                    try {
                        //在此附近应该加入内容判定，优化响应逻辑
                        String temp = response.body().string();
                        Log.i(TAG, temp);
                        mRequestTaskList = gson.fromJson(temp, type);
                        //成功获取网络请求内容后，调用内容处理方法
                        //Toast.makeText(Activity_gridPage.this,temp,Toast.LENGTH_SHORT).show();
                        Log.i(TAG, mRequestTaskList.size() + "");
                        List<Bean_ListView_home> tempList = new ArrayList<Bean_ListView_home>();
                        if (mRequestTaskList.size() > 0) {
                            for (Task task : mRequestTaskList) {
                                Log.i(TAG,task.toString());
                                if (!mHashSetTaskId.contains(task.getTaskId())) {
                                    mHashSetTaskId.add(task.getTaskId());
                                    tempList.add(new Bean_ListView_home(R.drawable.cat_usericon, task.getUserName(), photoPath[new Random().nextInt(3)], getResources().getString(R.string.ordinaryTask), task));
                                }
                            }
                        } else {
                            Toast.makeText(ActivityGridPage.this, getResources().getString(R.string.taskNoNew), Toast.LENGTH_SHORT).show();
                        }
                        recyclerAdapter.AddFooterItem(tempList);


                        //刷新完成
                        if (mSwipeRefreshLayout.isRefreshing())
                            mSwipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(ActivityGridPage.this, getResources().getString(R.string.Refresh) + " " + tempList.size() + " " + getResources().getString(R.string.Now)  + " " + mBeanListViewGridPage.size()  + " " + getResources().getString(R.string.TaskNum), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    Toast.makeText(ActivityGridPage.this, getResources().getString(R.string.FailToGetData), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {

            }
        });
    }

    private int minTaskId(){
        int min = Integer.MAX_VALUE;
        for(int temp : mHashSetTaskId){
            if(temp < min) min = temp;
        }
        return min;
    }
}

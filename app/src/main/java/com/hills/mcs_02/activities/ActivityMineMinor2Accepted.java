package com.hills.mcs_02.activities;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import com.hills.mcs_02.BaseActivity;
import com.hills.mcs_02.dataBeans.Bean_ListView_remind;
import com.hills.mcs_02.dataBeans.Task;
import com.hills.mcs_02.fragmentsPack.MCSRecyclerItemClickListener;
import com.hills.mcs_02.networkClasses.interfacesPack.PostRequestMineMinor2Accepted;
import com.hills.mcs_02.R;
import com.hills.mcs_02.viewsAdapters.Adapter_RecyclerView_remind;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;



public class ActivityMineMinor2Accepted extends BaseActivity {

    private String TAG="Activity_mine_minor2_accepted";
    private SwipeRefreshLayout mSwipRefreshLayout;
    private RecyclerView mRecyclerView;
    private Adapter_RecyclerView_remind recyclerAdapter;
    private List<Bean_ListView_remind> mBeanListViewRemind;
    private Set<Integer> mHashSetTaskId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_minor2_accepted);

        mHashSetTaskId = new HashSet<Integer>();

        //初始化列表
        initList();

    }

    private void initList(){
        mRecyclerView = findViewById(R.id.minepage_minor2_RecyclerView);
        mSwipRefreshLayout = findViewById(R.id.minepage_minor2_swiperefreshLayout);
        mSwipRefreshLayout.setColorSchemeColors(Color.RED,Color.BLUE,Color.GREEN);

        if(mBeanListViewRemind == null){
            mBeanListViewRemind = new ArrayList<Bean_ListView_remind>();
        }

        //进入页面初始化任务列表
        first_ListRefresh();
        recyclerAdapter = new Adapter_RecyclerView_remind(this,mBeanListViewRemind);
        recyclerAdapter.setRecyclerItemClickListener(new MCSRecyclerItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Gson gson = new Gson();
                Intent intent = new Intent(ActivityMineMinor2Accepted.this, ActivityTaskDetailPublished.class);
                intent.putExtra("taskGson",gson.toJson(mBeanListViewRemind.get(position).getTask()));
                startActivity(intent);
            }
        });

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        mRecyclerView.setAdapter(recyclerAdapter);

        initRefreshListener();

    }

    private void initRefreshListener() {
        initPullRefresh();  //上拉刷新
        initLoadMoreListener();  //下拉加载更多
    }

    //第一次刷新列表
    private void first_ListRefresh(){
        mSwipRefreshLayout.setRefreshing(true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                postRequest(0);
            }
        }, 3000
        );
    }

    private void initPullRefresh() {
        mSwipRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
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

    private void postRequest(int tag){
        //根据tag标记不同判定添加到列表开头（1）还是列表尾部（2）,0为初始刷新使用
        final int tempTag = tag;
        //创建Retrofit
        Retrofit retrofit = new Retrofit.Builder().baseUrl(getResources().getString(R.string.base_url))
                .addConverterFactory(GsonConverterFactory.create()).build();

        //创建网络接口
        PostRequestMineMinor2Accepted postRequest = retrofit.create(PostRequestMineMinor2Accepted.class);
        String userID = getSharedPreferences("user", Context.MODE_PRIVATE).getString("userID","");

        //创建call
        Call<ResponseBody> call = postRequest.queryAccepted(Integer.parseInt(userID));

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200){
                    Gson gson = new GsonBuilder().setDateFormat("yyyy.MM.dd").create();
                    Type type = new TypeToken<List<Task>>(){
                    }.getType();
                    try{
                        String list_string = response.body().string();
                        Log.i(TAG,list_string);
                        //将Gson字符串转化成为List
                        List<Task> task_list = gson.fromJson(list_string,type);
                        List<Bean_ListView_remind> tempList = new ArrayList<Bean_ListView_remind>();
                        //将List<Task>转化成为需要的List<List<Bean_ListView_remind>>
                        if(task_list.size() > 0){
                            System.out.println("收到的任务内容: " + task_list.toString());
                            for (Task task : task_list) {
                                if (task!=null && !mHashSetTaskId.contains(task.getTaskId())) {
                                    mHashSetTaskId.add(task.getTaskId());
                                    Log.i(TAG, task.toString());
                                    tempList.add(new Bean_ListView_remind(R.drawable.haimian_usericon, R.drawable.testphoto_4,  getResources().getString(R.string.ordinaryTask), task));
                                }
                            }
                        } else{
                            Toast.makeText(ActivityMineMinor2Accepted.this, getResources().getString(R.string.FailToGetData), Toast.LENGTH_SHORT).show();
                        }
                        if (tempTag == 0) mBeanListViewRemind.addAll(tempList);
                        else if (tempTag == 1) recyclerAdapter.AddHeaderItem(tempList);
                        else recyclerAdapter.AddFooterItem(tempList);

                        //刷新完成
                        if (mSwipRefreshLayout.isRefreshing())
                            mSwipRefreshLayout.setRefreshing(false);
                        Toast.makeText(ActivityMineMinor2Accepted.this, getResources().getString(R.string.Refresh) + tempList.size() + getResources().getString(R.string.Now) + mBeanListViewRemind.size() + getResources().getString(R.string.TaskNum), Toast.LENGTH_SHORT).show();
                    } catch (IOException e){
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {

            }
        });
    }

}

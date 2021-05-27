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

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import com.hills.mcs_02.BaseActivity;
import com.hills.mcs_02.dataBeans.BeanListViewHome;
import com.hills.mcs_02.dataBeans.Task;
import com.hills.mcs_02.fragmentsPack.MCSRecyclerItemClickListener;
import com.hills.mcs_02.networkClasses.interfacesPack.GetNewTenRequestHomeTaskList;
import com.hills.mcs_02.networkClasses.interfacesPack.PostRequestGridPageTaskList;
import com.hills.mcs_02.R;
import com.hills.mcs_02.viewsAdapters.AdapterRecyclerViewHome;

public class ActivityGridPage extends BaseActivity {
    private String TAG = "Activity_gridPage";
    private int pageTag = -1;
    private RecyclerView mRecyclerView;                                             /** The front page displays a list of tasks. */
    private List<BeanListViewHome> mBeanListViewGridPage;                           /** A data linked list of task lists. */
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private AdapterRecyclerViewHome recyclerAdapter;
    private List<Task> mRequestTaskList;
    private Set<Integer> mHashSetTaskId;                                             /** Avoid perceived task duplication */
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
        /** The Intent fetches the different Title flags. */
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
        /** The first time the task is initialized. */
        if (mBeanListViewGridPage == null) {
            mBeanListViewGridPage = new ArrayList<BeanListViewHome>();
        }

        /** Initialize SwipeRefreshLayout */
        mSwipeRefreshLayout = findViewById(R.id.gridpage_swiperefreshLayout);
        mSwipeRefreshLayout.setColorSchemeColors(Color.RED, Color.BLUE, Color.GREEN);
        mRecyclerView = findViewById(R.id.gridpage_RecyclerView);
        /** Go to the page to initialize the task list. */
        firstListRefresh();
        recyclerAdapter = new AdapterRecyclerViewHome(ActivityGridPage.this, mBeanListViewGridPage);
        recyclerAdapter.setRecyclerItemClickListener(new MCSRecyclerItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                int loginUserId = Integer.parseInt(ActivityGridPage.this.getSharedPreferences("user", MODE_PRIVATE).getString("userID", "-1"));
                /** Check if you are logged in. */
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

    /** Refresh the list for the first time. */
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


    /** Initialize refresh listener, including request for network data transfer. */
    private void initRefreshListener() {
        initPullRefresh();  /** The pull to refresh. */
        initLoadMoreListener();  /** Drop down to load more. */
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

                /** Determine the state of the RecyclerView when it is idle and when it is the last visible item before loading. */
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == recyclerAdapter.getItemCount()) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //getNewTenTaskRequest();
                        }
                    }, 3000);

                }

            }

            /** Update the LastVisibleItem value. */
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                /** The last item visible */
                lastVisibleItem = layoutManager.findLastVisibleItemPosition();
            }
        });
    }

    public void postRequest(int tag) {
        /** Add the tag to the top of the list (1) or the end of the list (2). 0 is used for initial refresh */
        final int TEMP_TAG = tag;
        /** Create a Retrofit object */
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(this.getString(R.string.base_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        /**  Create a network interface instance */
        PostRequestGridPageTaskList requestGetTaskList = retrofit.create(
            PostRequestGridPageTaskList.class);
        /** Wrappers send the request */
        Call<ResponseBody> call = requestGetTaskList.getCall(pageTag);

       /** Asynchronous network request */
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    Gson gson = new GsonBuilder().setDateFormat("yyyy.MM.dd").create();
                    Type type = new TypeToken<List<Task>>() {}.getType();
                    try {
                        String temp = response.body().string();
                        Log.i(TAG, temp);
                        mRequestTaskList = gson.fromJson(temp, type);
                        Log.i(TAG, mRequestTaskList.size() + "");
                        List<BeanListViewHome> tempList = new ArrayList<BeanListViewHome>();
                        if (mRequestTaskList.size() > 0) {
                            for (Task task : mRequestTaskList) {
                                Log.i(TAG,task.toString());
                                if (!mHashSetTaskId.contains(task.getTaskId())) {
                                    mHashSetTaskId.add(task.getTaskId());
                                    tempList.add(new BeanListViewHome(R.drawable.cat_usericon, task.getUsername(), photoPath[new Random().nextInt(3)], "普通任务", task));
                                }
                            }
                        } else {
                            Toast.makeText(ActivityGridPage.this, getResources().getString(R.string.FailToGetData) + mRequestTaskList.size(), Toast.LENGTH_SHORT).show();
                        }
                        if (TEMP_TAG == 0) mBeanListViewGridPage.addAll(tempList);
                        else if(TEMP_TAG == 1)recyclerAdapter.addHeaderItem(tempList);
                        else recyclerAdapter.addFooterItem(tempList);

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

    private int minTaskId(){
        int min = Integer.MAX_VALUE;
        for(int temp : mHashSetTaskId){
            if(temp < min) min = temp;
        }
        return min;
    }
}

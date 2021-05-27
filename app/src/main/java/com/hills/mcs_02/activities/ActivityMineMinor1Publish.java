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
import android.widget.ImageView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.hills.mcs_02.BaseActivity;
import com.hills.mcs_02.dataBeans.BeanListViewRemind;
import com.hills.mcs_02.dataBeans.Task;
import com.hills.mcs_02.fragmentsPack.MCSRecyclerItemClickListener;
import com.hills.mcs_02.networkClasses.interfacesPack.PostRequestMineMinor1Published;
import com.hills.mcs_02.R;
import com.hills.mcs_02.viewsAdapters.AdapterRecyclerViewRemind;

public class ActivityMineMinor1Publish extends BaseActivity {
    private String TAG = "Activity_mine_minor1_published";
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private AdapterRecyclerViewRemind recyclerAdapter;
    private List<BeanListViewRemind> mBeanListViewRemind;                           /** A data linked list of task lists. */
    private Set<Integer> mHashSetTaskId;                                             /** Avoid perceived task duplication */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_minor1_published);

        mHashSetTaskId= new HashSet<Integer>();
        /** Initialize the list */
        initList();
        initBackBT();
    }

    private void initBackBT() {
        ImageView backIv = findViewById(R.id.minepage_editInfo_detail_editview);
        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initList() {
        mRecyclerView = findViewById(R.id.minepage_minor1_RecyclerView);
        mSwipeRefreshLayout = findViewById(R.id.minepage_minor1_swiperefreshLayout);
        mSwipeRefreshLayout.setColorSchemeColors(Color.RED, Color.BLUE, Color.GREEN);

        if (mBeanListViewRemind == null) {
            mBeanListViewRemind = new ArrayList<BeanListViewRemind>();
        }

        /** Go to the page to initialize the task list */
        firstListRefresh();
        recyclerAdapter = new AdapterRecyclerViewRemind(this, mBeanListViewRemind);
        recyclerAdapter.setRecyclerItemClickListener(new MCSRecyclerItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Gson gson = new Gson();
                Intent intent = new Intent(ActivityMineMinor1Publish.this, ActivityTaskDetailPublished.class);
                intent.putExtra("taskGson",gson.toJson(mBeanListViewRemind.get(position).getTask()));
                startActivity(intent);
            }
        });

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        mRecyclerView.setAdapter(recyclerAdapter);

        initRefreshListener();

    }

    private void initRefreshListener() {
        initPullRefresh();  /** Pull up to refresh */
    }
    /** Refresh the list for the first time*/
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

    private void postRequest(int tag) {
        /** Add the tag to the top of the list (1) or the end of the list (2). 0 is used for initial refresh */
        final int tempTag = tag;
        /** Create a Retrofit object */
        Retrofit retrofit = new Retrofit.Builder().baseUrl(getResources().getString(R.string.base_url)).addConverterFactory(GsonConverterFactory.create()).build();
        /**  Create a network interface instance */
        PostRequestMineMinor1Published postRequest = retrofit.create(PostRequestMineMinor1Published.class);
        String userId = getSharedPreferences("user", Context.MODE_PRIVATE).getString("userID", "");
        Call<ResponseBody> call = postRequest.queryPublished(Integer.parseInt(userId));
        call.enqueue(new Callback<ResponseBody>() {


            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    Gson gson = new GsonBuilder().setDateFormat("yyyy.MM.dd").create();
                    Type type = new TypeToken<List<Task>>() {
                    }.getType();
                    try {
                        String listString = response.body().string();
                        Log.i(TAG, listString);
                        /** Convert the Gson string to a List */
                        List<Task> taskList = gson.fromJson(listString, type);
                        List<BeanListViewRemind> tempList = new ArrayList<BeanListViewRemind>();
                        if (taskList.size() > 0) {
                            for (Task task : taskList) {
                                if (task!=null && !mHashSetTaskId.contains(task.getTaskId())) {
                                    mHashSetTaskId.add(task.getTaskId());
                                    Log.i(TAG, task.toString());
                                    tempList.add(new BeanListViewRemind(R.drawable.haimian_usericon, R.drawable.testphoto_4,  getResources().getString(R.string.ordinaryTask), task));
                                }
                            }
                        } else {
                            Toast.makeText(ActivityMineMinor1Publish.this, getResources().getString(R.string.FailToGetData), Toast.LENGTH_SHORT).show();
                        }
                        if (tempTag == 0) mBeanListViewRemind.addAll(tempList);
                        else if (tempTag == 1) recyclerAdapter.addHeaderItem(tempList);
                        else recyclerAdapter.addFooterItem(tempList);

                        if (mSwipeRefreshLayout.isRefreshing())
                            mSwipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(ActivityMineMinor1Publish.this, getResources().getString(R.string.Refresh) + tempList.size() + getResources().getString(R.string.Now) + mBeanListViewRemind.size() + getResources().getString(R.string.TaskNum), Toast.LENGTH_SHORT).show();

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
}

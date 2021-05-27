package com.hills.mcs_02.fragmentsPack;

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
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.hills.mcs_02.dataBeans.BeanListViewRemind;
import com.hills.mcs_02.dataBeans.Task;
import com.hills.mcs_02.networkClasses.interfacesPack.PostRequestRemindDoing;
import com.hills.mcs_02.networkClasses.interfacesPack.PostRequestRemindDone;
import com.hills.mcs_02.networkClasses.interfacesPack.PostRequestRemindRecommend;
import com.hills.mcs_02.R;
import com.hills.mcs_02.viewsAdapters.AdapterRecyclerViewRemind;

public class FragmentRemindPager extends Fragment {
    private String pageTag = "Fragment_remind_pager";
    private String tag;
    private Context mContext;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private AdapterRecyclerViewRemind mRecyclerAdapter;
    private List<BeanListViewRemind> mBeanListViewRemind;                           //为上述ListView准备的数据链表
    private Set<Integer> mHashSetTaskId;                                             //用于获取的发布任务去重

    public FragmentRemindPager() {

    }

    public FragmentRemindPager(String tag) {
        this.tag = tag;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_remindpage_pager,container,false);

        /** Initialize various views and global variables */
        mSwipeRefreshLayout = view.findViewById(R.id.remindpage_pager_swiperefreshLayout);
        mRecyclerView = view.findViewById(R.id.remindpage_pager_RecyclerView);
        mSwipeRefreshLayout.setColorSchemeColors(Color.RED, Color.BLUE, Color.GREEN);
        mHashSetTaskId = new HashSet<Integer>();
        if(mBeanListViewRemind == null) mBeanListViewRemind = new ArrayList<BeanListViewRemind>();

        /** Initialize the list */
        firstListRefresh();

        mRecyclerAdapter = new AdapterRecyclerViewRemind(mContext, mBeanListViewRemind);
        mRecyclerAdapter.setRecyclerItemClickListener(new MCSRecyclerItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
            }
        });

        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false));
        mRecyclerView.setAdapter(mRecyclerAdapter);

        initRefreshListener();

        return view;
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

   /** Refresh the list for the first time */
    private void firstListRefresh() {
        mSwipeRefreshLayout.setRefreshing(true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                /** Load the list */
                postRequest(0);
            }
        },3000);
    }


    private void postRequest(int label) {
        /** Retrieves data from different network interfaces based on different tags */
        switch (tag){
            case "doing" :
                doingRequest(label);
                break;
            case "done" :
                doneRequest(label);
                break;
            case "recommend" :
                recommendRequest(label);
                break;
        }

    }

    private void doingRequest(int label) {

        Retrofit retrofit = new Retrofit.Builder().baseUrl(getResources().getString(R.string.base_url)).addConverterFactory(GsonConverterFactory.create()).build();
        String userId = mContext.getSharedPreferences("user", Context.MODE_PRIVATE).getString("userID", "");
        final int TEMP_LABEL = label;
        PostRequestRemindDoing postRequest = retrofit.create(PostRequestRemindDoing.class);

        Call<ResponseBody> call = postRequest.queryDoing(Integer.parseInt(userId));

        call.enqueue(new Callback<ResponseBody>() {


            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    Gson gson = new GsonBuilder().setDateFormat("yyyy.MM.dd").create();
                    Type type = new TypeToken<List<Task>>() {
                    }.getType();
                    try {
                        String listString = response.body().string();
                        Log.i(pageTag, listString);
                        List<Task> taskList = gson.fromJson(listString, type);
                        List<BeanListViewRemind> tempList = new ArrayList<BeanListViewRemind>();
                        if (taskList.size() > 0) {
                            System.out.println("收到的任务内容: " + taskList.toString());
                            for (Task task : taskList) {
                                if (task!=null && !mHashSetTaskId.contains(task.getTaskId())) {
                                    mHashSetTaskId.add(task.getTaskId());
                                    Log.i(pageTag, task.toString());
                                    tempList.add(new BeanListViewRemind(R.drawable.haimian_usericon, R.drawable.testphoto_4,  getResources().getString(R.string.ordinaryTask), task));
                                }
                            }
                        } else {
                            Toast.makeText(mContext, getResources().getString(R.string.FailToGetData), Toast.LENGTH_SHORT).show();
                        }
                        if (TEMP_LABEL == 0) mBeanListViewRemind.addAll(tempList);
                        else if (TEMP_LABEL == 1) mRecyclerAdapter.addHeaderItem(tempList);
                        else mRecyclerAdapter.addFooterItem(tempList);

                        if (mSwipeRefreshLayout.isRefreshing())
                            mSwipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(mContext, getResources().getString(R.string.Refresh) + tempList.size() + getResources().getString(R.string.Now) + mBeanListViewRemind
                            .size() + getResources().getString(R.string.TaskNum), Toast.LENGTH_SHORT).show();

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

    private void doneRequest(int label) {

        Retrofit retrofit = new Retrofit.Builder().baseUrl(getResources().getString(R.string.base_url)).addConverterFactory(GsonConverterFactory.create()).build();
        String userId = mContext.getSharedPreferences("user", Context.MODE_PRIVATE).getString("userID", "");
        final int TEMP_LABEL = label;
        PostRequestRemindDone postRequest = retrofit.create(PostRequestRemindDone.class);

        Call<ResponseBody> call = postRequest.queryDone(Integer.parseInt(userId));

        call.enqueue(new Callback<ResponseBody>() {


            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    Gson gson = new GsonBuilder().setDateFormat("yyyy.MM.dd").create();
                    Type type = new TypeToken<List<Task>>() {
                    }.getType();
                    try {
                        String listString = response.body().string();
                        Log.i(pageTag, listString);
                        List<Task> taskList = gson.fromJson(listString, type);
                        List<BeanListViewRemind> tempList = new ArrayList<BeanListViewRemind>();
                        if (taskList.size() > 0) {
                            System.out.println(taskList);
                            for (Task task : taskList) {
                                if (task!=null && !mHashSetTaskId.contains(task.getTaskId())) {
                                    mHashSetTaskId.add(task.getTaskId());
                                    Log.i(pageTag, task.toString());
                                    tempList.add(new BeanListViewRemind(R.drawable.haimian_usericon, R.drawable.testphoto_4,  getResources().getString(R.string.ordinaryTask), task));
                                }
                            }
                        } else {
                            Toast.makeText(mContext, getResources().getString(R.string.FailToGetData), Toast.LENGTH_SHORT).show();
                        }
                        if (TEMP_LABEL == 0) mBeanListViewRemind.addAll(tempList);
                        else if (TEMP_LABEL == 1) mRecyclerAdapter.addHeaderItem(tempList);
                        else mRecyclerAdapter.addFooterItem(tempList);

                        if (mSwipeRefreshLayout.isRefreshing())
                            mSwipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(mContext, getResources().getString(R.string.Refresh) + tempList.size() + getResources().getString(R.string.Now) + mBeanListViewRemind
                            .size() + getResources().getString(R.string.TaskNum), Toast.LENGTH_SHORT).show();

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

    private void recommendRequest(int label) {

        Retrofit retrofit = new Retrofit.Builder().baseUrl(getResources().getString(R.string.base_url)).addConverterFactory(GsonConverterFactory.create()).build();
        String userId = mContext.getSharedPreferences("user", Context.MODE_PRIVATE).getString("userID", "");
        final int TEMP_LABEL = label;

        PostRequestRemindRecommend postRequest = retrofit.create(PostRequestRemindRecommend.class);

        Call<ResponseBody> call = postRequest.queryRecommend(Integer.parseInt(userId));

        call.enqueue(new Callback<ResponseBody>() {


            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    Gson gson = new GsonBuilder().setDateFormat("yyyy.MM.dd").create();
                    Type type = new TypeToken<List<Task>>() {
                    }.getType();
                    try {
                        String listString = response.body().string();
                        Log.i(pageTag, listString);
                        List<Task> taskList = gson.fromJson(listString, type);
                        List<BeanListViewRemind> tempList = new ArrayList<BeanListViewRemind>();
                        if (taskList.size() > 0) {
                            for (Task task : taskList) {
                                if(task == null) continue;
                                if (task!=null && !mHashSetTaskId.contains(task.getTaskId())) {
                                    mHashSetTaskId.add(task.getTaskId());
                                    Log.i(pageTag, task.toString());
                                    tempList.add(new BeanListViewRemind(R.drawable.haimian_usericon, R.drawable.testphoto_4,  getResources().getString(R.string.ordinaryTask), task));
                                }
                            }
                        } else {
                            Toast.makeText(mContext, getResources().getString(R.string.FailToGetData), Toast.LENGTH_SHORT).show();
                        }
                        if (TEMP_LABEL == 0) mBeanListViewRemind.addAll(tempList);
                        else if (TEMP_LABEL == 1) mRecyclerAdapter.addHeaderItem(tempList);
                        else mRecyclerAdapter.addFooterItem(tempList);

                        if (mSwipeRefreshLayout.isRefreshing())
                            mSwipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(mContext, getResources().getString(R.string.Refresh) + tempList.size() + getResources().getString(R.string.Now) + mBeanListViewRemind
                            .size() + getResources().getString(R.string.TaskNum), Toast.LENGTH_SHORT).show();

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



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }
}

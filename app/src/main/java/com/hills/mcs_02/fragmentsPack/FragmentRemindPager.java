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


import com.hills.mcs_02.dataBeans.Bean_ListView_remind;
import com.hills.mcs_02.dataBeans.Task;
import com.hills.mcs_02.networkClasses.interfacesPack.PostRequestRemindDoing;
import com.hills.mcs_02.networkClasses.interfacesPack.PostRequestRemindDone;
import com.hills.mcs_02.networkClasses.interfacesPack.PostRequestRemindRecommend;
import com.hills.mcs_02.R;
import com.hills.mcs_02.viewsAdapters.AdapterRecyclerViewRemind;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class FragmentRemindPager extends Fragment {
    private String page_TAG = "Fragment_remind_pager";
    private String tag;
    private Context mContext;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private AdapterRecyclerViewRemind mRecyclerAdapter;
    private List<Bean_ListView_remind> mBeanListViewRemind;                           //为上述ListView准备的数据链表
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

        //初始化各类view和全局变量
        mSwipeRefreshLayout = view.findViewById(R.id.remindpage_pager_swiperefreshLayout);
        mRecyclerView = view.findViewById(R.id.remindpage_pager_RecyclerView);
        mSwipeRefreshLayout.setColorSchemeColors(Color.RED, Color.BLUE, Color.GREEN);
        mHashSetTaskId = new HashSet<Integer>();
        if(mBeanListViewRemind == null) mBeanListViewRemind = new ArrayList<Bean_ListView_remind>();

        //初始化列表
        firstListRefresh();

        mRecyclerAdapter = new AdapterRecyclerViewRemind(mContext, mBeanListViewRemind);
        mRecyclerAdapter.setRecyclerItemClickListener(new MCSRecyclerItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                /*Gson gson = new Gson();
                Intent intent = new Intent(getActivity(), Activity_Task_Detail_Published.class);
                intent.putExtra("taskGson",gson.toJson(mBean_ListView_remind.get(position).getTask()));
                startActivity(intent);*/
            }
        });

        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false));
        mRecyclerView.setAdapter(mRecyclerAdapter);

        initRefreshListener();

        return view;
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
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == mRecyclerAdapter.getItemCount()) {
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




    //第一次刷新列表
    private void firstListRefresh() {
        mSwipeRefreshLayout.setRefreshing(true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //加载列表
                postRequest(0);
            }
        },3000);
    }


    private void postRequest(int label) {
        //根据不同tag到不同的网络接口获取数据
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
        //创建Retrofit
        Retrofit retrofit = new Retrofit.Builder().baseUrl(getResources().getString(R.string.base_url)).addConverterFactory(GsonConverterFactory.create()).build();
        String userid = mContext.getSharedPreferences("user", Context.MODE_PRIVATE).getString("userID", "");
        final int tempLabel = label;
        //创建网络接口
        PostRequestRemindDoing postRequest = retrofit.create(PostRequestRemindDoing.class);

        //创建call
        Call<ResponseBody> call = postRequest.queryDoing(Integer.parseInt(userid));

        call.enqueue(new Callback<ResponseBody>() {


            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    Gson gson = new GsonBuilder().setDateFormat("yyyy.MM.dd").create();
                    Type type = new TypeToken<List<Task>>() {
                    }.getType();
                    try {
                        String listString = response.body().string();
                        Log.i(page_TAG, listString);
                        //将Gson字符串转换成为List
                        List<Task> taskList = gson.fromJson(listString, type);
                        List<Bean_ListView_remind> tempList = new ArrayList<Bean_ListView_remind>();
                        //将List<Task>转化成为需要的List<List<Bean_ListView_remind>>
                        if (taskList.size() > 0) {
                            System.out.println("收到的任务内容: " + taskList.toString());
                            for (Task task : taskList) {
                                if (task!=null && !mHashSetTaskId.contains(task.getTaskId())) {
                                    mHashSetTaskId.add(task.getTaskId());
                                    Log.i(page_TAG, task.toString());
                                    tempList.add(new Bean_ListView_remind(R.drawable.haimian_usericon, R.drawable.testphoto_4,  getResources().getString(R.string.ordinaryTask), task));
                                }
                            }
                        } else {
                            Toast.makeText(mContext, getResources().getString(R.string.FailToGetData), Toast.LENGTH_SHORT).show();
                        }
                        if (tempLabel == 0) mBeanListViewRemind.addAll(tempList);
                        else if (tempLabel == 1) mRecyclerAdapter.addHeaderItem(tempList);
                        else mRecyclerAdapter.addFooterItem(tempList);

                        //刷新完成
                        if (mSwipeRefreshLayout.isRefreshing())
                            mSwipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(mContext, getResources().getString(R.string.Refresh) + tempList.size() + getResources().getString(R.string.Now) + mBeanListViewRemind
                            .size() + getResources().getString(R.string.TaskNum), Toast.LENGTH_SHORT).show();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {

            }
        });
    }

    private void doneRequest(int label) {
        //创建Retrofit
        Retrofit retrofit = new Retrofit.Builder().baseUrl(getResources().getString(R.string.base_url)).addConverterFactory(GsonConverterFactory.create()).build();
        String userid = mContext.getSharedPreferences("user", Context.MODE_PRIVATE).getString("userID", "");
        final int tempLabel = label;
        //创建网络接口
        PostRequestRemindDone postRequest = retrofit.create(PostRequestRemindDone.class);

        //创建call
        Call<ResponseBody> call = postRequest.queryDone(Integer.parseInt(userid));

        call.enqueue(new Callback<ResponseBody>() {


            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    Gson gson = new GsonBuilder().setDateFormat("yyyy.MM.dd").create();
                    Type type = new TypeToken<List<Task>>() {
                    }.getType();
                    try {
                        String listString = response.body().string();
                        Log.i(page_TAG, listString);
                        //将Gson字符串转换成为List
                        List<Task> taskList = gson.fromJson(listString, type);
                        List<Bean_ListView_remind> tempList = new ArrayList<Bean_ListView_remind>();
                        //将List<Task>转化成为需要的List<List<Bean_ListView_remind>>
                        if (taskList.size() > 0) {
                            System.out.println(taskList);
                            for (Task task : taskList) {
                                if (task!=null && !mHashSetTaskId.contains(task.getTaskId())) {
                                    mHashSetTaskId.add(task.getTaskId());
                                    Log.i(page_TAG, task.toString());
                                    tempList.add(new Bean_ListView_remind(R.drawable.haimian_usericon, R.drawable.testphoto_4,  getResources().getString(R.string.ordinaryTask), task));
                                }
                            }
                        } else {
                            Toast.makeText(mContext, getResources().getString(R.string.FailToGetData), Toast.LENGTH_SHORT).show();
                        }
                        if (tempLabel == 0) mBeanListViewRemind.addAll(tempList);
                        else if (tempLabel == 1) mRecyclerAdapter.addHeaderItem(tempList);
                        else mRecyclerAdapter.addFooterItem(tempList);

                        //刷新完成
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
        //创建Retrofit
        Retrofit retrofit = new Retrofit.Builder().baseUrl(getResources().getString(R.string.base_url)).addConverterFactory(GsonConverterFactory.create()).build();
        String userid = mContext.getSharedPreferences("user", Context.MODE_PRIVATE).getString("userID", "");
        final int tempLabel = label;
        //创建网络接口
        PostRequestRemindRecommend postRequest = retrofit.create(PostRequestRemindRecommend.class);

        //创建call
        Call<ResponseBody> call = postRequest.queryRecommend(Integer.parseInt(userid));

        call.enqueue(new Callback<ResponseBody>() {


            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    Gson gson = new GsonBuilder().setDateFormat("yyyy.MM.dd").create();
                    Type type = new TypeToken<List<Task>>() {
                    }.getType();
                    try {
                        String list_string = response.body().string();
                        Log.i(page_TAG, list_string);
                        //将Gson字符串转换成为List
                        List<Task> taskList = gson.fromJson(list_string, type);
                        List<Bean_ListView_remind> tempList = new ArrayList<Bean_ListView_remind>();
                        //将List<Task>转化成为需要的List<List<Bean_ListView_remind>>
                        if (taskList.size() > 0) {
                            for (Task task : taskList) {
                                if(task == null) continue;
                                if (task!=null && !mHashSetTaskId.contains(task.getTaskId())) {
                                    mHashSetTaskId.add(task.getTaskId());
                                    Log.i(page_TAG, task.toString());
                                    tempList.add(new Bean_ListView_remind(R.drawable.haimian_usericon, R.drawable.testphoto_4,  getResources().getString(R.string.ordinaryTask), task));
                                }
                            }
                        } else {
                            Toast.makeText(mContext, getResources().getString(R.string.FailToGetData), Toast.LENGTH_SHORT).show();
                        }
                        if (tempLabel == 0) mBeanListViewRemind.addAll(tempList);
                        else if (tempLabel == 1) mRecyclerAdapter.addHeaderItem(tempList);
                        else mRecyclerAdapter.addFooterItem(tempList);

                        //刷新完成
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

package com.hills.mcs_02.fragmentsPack;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.hills.mcs_02.For_test;
import com.hills.mcs_02.R;
import com.hills.mcs_02.activities.Activity_gridPage;
import com.hills.mcs_02.activities.Activity_login;
import com.hills.mcs_02.dataBeans.Bean_ListView_home;
import com.hills.mcs_02.dataBeans.Task;
import com.hills.mcs_02.networkClasses.interfacesPack.GetNewTen_Request_home_taskList;
import com.hills.mcs_02.networkClasses.interfacesPack.GetRequest_home_taskList;
import com.hills.mcs_02.viewsAdapters.Adapter_PagerView_home;
import com.hills.mcs_02.viewsAdapters.Adapter_RecyclerView_home;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link Fragment_home#newInstance} factory method to
 * create an instance of this fragment.
 */
//首页Fragment类
public class Fragment_home extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String Fragment_home_func = "Fragment_home_func";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Context mContext;
    private ViewPager mViewPager;
    private List<ImageView> viewList;                                                //图片轮转用到的List
    private Adapter_PagerView_home mAdapterPagerView_home_list;
    private GridView mGridView;
    //显示“全部、市政民生、校园生活、商业活动、更多”五个入口的View
    private List<Map<String, Object>> grid_item_list;                                //为上述GridView准备的数据链表
    private SimpleAdapter grid_adapter;                                              //为GridView准备的数据适配器
    private RecyclerView mRecyclerView;                                              //为首页显示任务列表的RecyclerView
    private List<Bean_ListView_home> mBean_ListView_homes;                           //为上述ListView准备的数据链表
    private SearchView mSearchView;                                                  //搜索框的绑定
    private For_test mFor_test;                                                      //暂时的接口设置
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private Adapter_RecyclerView_home recyclerAdapter;
    private List<Task> mRequest_TaskList;
    private Set<Integer> mHashSet_TaskID;                                             //用于获取感知任务去重
    private String TAG = "fragment_home";

    int[] photoPath = {R.drawable.testphoto_1, R.drawable.testphoto_2, R.drawable.testphoto_3, R.drawable.testphoto_4};

    public Fragment_home() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_home.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_home newInstance(String param1, String param2) {
        Fragment_home fragment = new Fragment_home();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        mViewPager = (ViewPager) view.findViewById(R.id.homepage_viewPager_pageRolling);
        mGridView = (GridView) view.findViewById(R.id.homepage_GridView);
        mSearchView = (SearchView) view.findViewById(R.id.Search_home);
        viewList = new ArrayList<>();
        grid_item_list = new ArrayList<Map<String, Object>>();
        mHashSet_TaskID = new HashSet<Integer>();
        mRequest_TaskList = new ArrayList<Task>();

        flash(view);
        initGridView();
        initSearchView();


        return view;
    }

    //刷新页面
    public void flash(View view) {
        //自定义方法，为初始化首页上的各类组件
        initPageRolling();
        initRecyclerView(view);
    }

    public void getRequest(int tag) {
        //根据tag标记不同判定添加到列表开头（1）还是列表尾部（2）,0为初始刷新使用
        final int tempTag = tag;
        //创建Retrofit对象
        Retrofit retrofit = new Retrofit.Builder().baseUrl(this.getString(R.string.base_url)).addConverterFactory(GsonConverterFactory.create()).build();
        //测试用url
        //Retrofit retrofit = new Retrofit.Builder().baseUrl("http://192.168.43.213:8889/").addConverterFactory(GsonConverterFactory.create()).build();

        //创建网络接口实例
        GetRequest_home_taskList request_getTaskList = retrofit.create(GetRequest_home_taskList.class);
        //包装发送请求
        Call<ResponseBody> call = request_getTaskList.getCall();

        Log.i("TIMMMMMMMME!","NowTime:" + System.currentTimeMillis());
        //异步网络请求

        Date d =new Date();

        Log.i(TAG,"The getTen request time: " +         d.getTime());
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
                        mRequest_TaskList = gson.fromJson(temp, type);
                        //成功获取网络请求内容后，调用内容处理方法
                        //Toast.makeText(mContext,temp,Toast.LENGTH_SHORT).show();
                        Log.i(TAG, mRequest_TaskList.size() + "");
                        List<Bean_ListView_home> tempList = new ArrayList<Bean_ListView_home>();
                        if (mRequest_TaskList.size() > 0) {
                            for (Task task : mRequest_TaskList) {
                                Log.i(TAG,task.toString());
                                if (!mHashSet_TaskID.contains(task.getTaskId())) {
                                    mHashSet_TaskID.add(task.getTaskId());
                                    tempList.add(new Bean_ListView_home(R.drawable.cat_usericon, task.getUserName(), photoPath[new Random().nextInt(3)], "普通任务", task));
                                }
                            }
                        } else {
                            Toast.makeText(mContext, getResources().getString(R.string.FailToGetData) + mRequest_TaskList.size(), Toast.LENGTH_SHORT).show();
                        }
                        if (tempTag == 0) mBean_ListView_homes.addAll(tempList);
                        else if(tempTag == 1)recyclerAdapter.AddHeaderItem(tempList);
                        else recyclerAdapter.AddFooterItem(tempList);


                        //刷新完成
                        if (mSwipeRefreshLayout.isRefreshing())
                            mSwipeRefreshLayout.setRefreshing(false);
                        Log.i("TIMMMMMMMME!","NowTime:" + System.currentTimeMillis());
                        Toast.makeText(mContext, getResources().getString(R.string.Refresh) + tempList.size() + getResources().getString(R.string.Now) + mBean_ListView_homes.size() + getResources().getString(R.string.TaskNum), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }


    private void initPageRolling() {
        //Test，当前模拟填充的数据
        ImageView imageView_1 = new ImageView(mContext);
        imageView_1.setImageResource(R.drawable.rollingimgae_1);
        imageView_1.setScaleType(ImageView.ScaleType.CENTER_CROP);
        viewList.add(imageView_1);
        //Test
        ImageView imageView_2 = new ImageView(mContext);
        imageView_2.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView_2.setImageResource(R.drawable.rollingimgae_2);
        viewList.add(imageView_2);
        //Test
        ImageView imageView_3 = new ImageView(mContext);
        imageView_3.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView_3.setImageResource(R.drawable.rollingimgae_3);
        viewList.add(imageView_3);


        mAdapterPagerView_home_list = new Adapter_PagerView_home(viewList, mContext);
        mViewPager.setAdapter(mAdapterPagerView_home_list);
        mViewPager.setCurrentItem(0);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {


            }

            @Override
            public void onPageSelected(int i) {


            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    private void initGridView() {
        int iconID[] = {R.drawable.home_logo_1, R.drawable.home_logo_2, R.drawable.home_logo_3_1, R.drawable.home_logo_4, R.drawable.home_logo_5};
        String textS[] = {getResources().getString(R.string.home_grid_0), getResources().getString(R.string.home_grid_1), getResources().getString(R.string.home_grid_2), getResources().getString(R.string.home_grid_3), getResources().getString(R.string.home_grid_4)};
        for (int i = 0; i < textS.length; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("gridItem_img", iconID[i]);
            map.put("gridItem_text", textS[i]);
            grid_item_list.add(map);
        }

        String[] from = {"gridItem_img", "gridItem_text"};
        int[] to = {R.id.gridItem_img, R.id.gridIem_text};


        grid_adapter = new SimpleAdapter(getActivity(), grid_item_list, R.layout.gridview_item_home, from, to);
        mGridView.setAdapter(grid_adapter);
        //gridItem点击相应事件待写
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch(position){
                    case 0:
                        Intent intent_0 = new Intent(getActivity(), Activity_gridPage.class);
                        intent_0.putExtra("pageName","Security");
                        startActivity(intent_0);
                        break;
                    case 1:
                        Intent intent_1 = new Intent(getActivity(), Activity_gridPage.class);
                        intent_1.putExtra("pageName","Environment");
                        startActivity(intent_1);
                        break;
                    case 2:
                        Intent intent_2 = new Intent(getActivity(), Activity_gridPage.class);
                        intent_2.putExtra("pageName","Daily Life");
                        startActivity(intent_2);
                        break;
                    case 3:
                        Intent intent_3 = new Intent(getActivity(), Activity_gridPage.class);
                        intent_3.putExtra("pageName","Business");
                        startActivity(intent_3);
                        break;
                    case 4:
                        Intent intent_4 = new Intent(getActivity(), Activity_gridPage.class);
                        intent_4.putExtra("pageName","More");
                        startActivity(intent_4);
                        break;
                }
            }
        });
    }


    @SuppressLint("WrongConstant")
    private void initRecyclerView(View view) {

        //第一次初始化任务
        if (mBean_ListView_homes == null) {
            mBean_ListView_homes = new ArrayList<Bean_ListView_home>();

            /*/测试使用
            for(int i = 0; i < 10; i++){
                Bean_ListView_home bean_1 = new Bean_ListView_home(R.drawable.cat_usericon, R.drawable.takephoto, R.drawable.star_1, R.drawable.testphoto_1, "猫某", "1小时前", "裂缝识别", "任务描述：该任务是为拍照裂缝……", new Random().nextInt(100) + "元", new Random().nextInt(10) + " 个", "截止时间：2018.12.25");
                mBean_ListView_homes.add(bean_1);
            }*/
        }

        //初始化SwipeRefreshLayout
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.homepage_swiperefreshLayout);
        mSwipeRefreshLayout.setColorSchemeColors(Color.RED, Color.BLUE, Color.GREEN);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.homepage_RecyclerView);
        //进入页面初始化任务列表
        first_ListRefresh();
        recyclerAdapter = new Adapter_RecyclerView_home(mContext, mBean_ListView_homes);
        //recyclerView没有跟listView一样封装OnItemClickListener，所以只能手动实现，这里是将监听器绑定在了适配器上
        recyclerAdapter.setRecyclerItemClickListener(new MCS_RecyclerItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                int login_userID = Integer.parseInt(mContext.getSharedPreferences("user", MODE_PRIVATE).getString("userID", "-1"));
                //检查是否登录
                if (login_userID == -1) {
                    Toast.makeText(mContext, getResources().getString(R.string.login_first), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(mContext, Activity_login.class);
                    startActivity(intent);
                }else{
                    Gson gson = new Gson();
                    mFor_test.jump_to_TaskDetailActivity(gson.toJson(mBean_ListView_homes.get(position).getTask()));
                }
            }
        });

        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(recyclerAdapter);

        initRefreshListener();


    }

    //第一次刷新列表
    private void first_ListRefresh() {
        mSwipeRefreshLayout.setRefreshing(true);
        new Handler().postDelayed(new Runnable() {
                                      @Override
                                      public void run() {
                                          getRequest(0);
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
                                                  getRequest(1);
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
                            getNewTenTaskRequest();
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




    public void getNewTenTaskRequest() {

        //创建Retrofit对象
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(this.getString(R.string.base_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //创建网络接口实例
        GetNewTen_Request_home_taskList request_getTaskList = retrofit.create(GetNewTen_Request_home_taskList.class);
        //包装发送请求
        Call<ResponseBody> call = request_getTaskList.query_NewTenTask(minTaskId());


        Log.i(TAG,"The request info time start: " + System.currentTimeMillis());
        //异步网络请求
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.i(TAG,"The request info time end: " + System.currentTimeMillis());
                if (response.code() == 200) {
                    Gson gson = new GsonBuilder().setDateFormat("yyyy.MM.dd").create();
                    Type type = new TypeToken<List<Task>>() {}.getType();
                    try {
                        //在此附近应该加入内容判定，优化响应逻辑
                        String temp = response.body().string();
                        Log.i(TAG, temp);
                        mRequest_TaskList = gson.fromJson(temp, type);
                        //成功获取网络请求内容后，调用内容处理方法
                        //Toast.makeText(mContext,temp,Toast.LENGTH_SHORT).show();
                        Log.i(TAG, mRequest_TaskList.size() + "");
                        List<Bean_ListView_home> tempList = new ArrayList<Bean_ListView_home>();
                        if (mRequest_TaskList.size() > 0) {
                            for (Task task : mRequest_TaskList) {
                                Log.i(TAG,task.toString());
                                if (!mHashSet_TaskID.contains(task.getTaskId())) {
                                    mHashSet_TaskID.add(task.getTaskId());
                                    tempList.add(new Bean_ListView_home(R.drawable.cat_usericon, task.getUserName(), photoPath[new Random().nextInt(3)], getResources().getString(R.string.ordinaryTask), task));
                                }
                            }
                        } else {
                            Toast.makeText(mContext, getResources().getString(R.string.taskNoNew), Toast.LENGTH_SHORT).show();
                        }
                        recyclerAdapter.AddFooterItem(tempList);


                        //刷新完成
                        if (mSwipeRefreshLayout.isRefreshing())
                            mSwipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(mContext, getResources().getString(R.string.Refresh) + " " + tempList.size() + " " + getResources().getString(R.string.Now)  + " " + mBean_ListView_homes.size()  + " " + getResources().getString(R.string.TaskNum), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    Toast.makeText(mContext, getResources().getString(R.string.FailToGetData), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private int minTaskId(){
        int min = Integer.MAX_VALUE;
        for(int i : mHashSet_TaskID){
            if(i < min) min = i;
        }
        return min;
    }

    private void initSearchView() {
        mSearchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFor_test.jump_to_SearchActivity();
            }
        });
    }


    //为Fragment之间通信设置的回调Activity的方法
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        // 保证容器Activity实现了回调接口 否则抛出异常警告
        try {
            mFor_test = (For_test) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement For_TestInterface");
        }
    }
}

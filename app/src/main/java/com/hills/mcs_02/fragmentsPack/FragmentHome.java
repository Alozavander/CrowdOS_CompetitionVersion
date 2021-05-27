package com.hills.mcs_02.fragmentsPack;

import static android.content.Context.MODE_PRIVATE;

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

import com.hills.mcs_02.activities.ActivityGridPage;
import com.hills.mcs_02.activities.ActivityLogin;
import com.hills.mcs_02.dataBeans.BeanListViewHome;
import com.hills.mcs_02.dataBeans.Task;
import com.hills.mcs_02.ForTest;
import com.hills.mcs_02.networkClasses.interfacesPack.GetNewTenRequestHomeTaskList;
import com.hills.mcs_02.networkClasses.interfacesPack.GetRequestHomeTaskList;
import com.hills.mcs_02.R;
import com.hills.mcs_02.viewsAdapters.AdapterPagerViewHome;
import com.hills.mcs_02.viewsAdapters.AdapterRecyclerViewHome;

public class FragmentHome extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String FRAGMENT_HOME_FUNC = "Fragment_home_func";
    private String mParam1;
    private String mParam2;
    private Context mContext;
    private ViewPager mViewPager;
    private List<ImageView> viewList;
    private AdapterPagerViewHome mAdapterPagerViewHomeList;             /** Rotate the List of images */
    private GridView mGridView;
    private List<Map<String, Object>> gridItemList;
    private SimpleAdapter gridAdapter;
    private RecyclerView mRecyclerView;
    private List<BeanListViewHome> mBeanListViewHomes;
    private SearchView mSearchView;                                           /** Search box binding */
    private ForTest mForTest;                                                 /** Temporal interface setup */
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private AdapterRecyclerViewHome recyclerAdapter;
    private List<Task> mRequestTaskList;
    private Set<Integer> mHashSetTaskId;
    private String TAG = "fragment_home";

    int[] photoPath = {R.drawable.testphoto_1, R.drawable.testphoto_2, R.drawable.testphoto_3, R.drawable.testphoto_4};

    public FragmentHome() {
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
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        mViewPager = (ViewPager) view.findViewById(R.id.homepage_viewPager_pageRolling);
        mGridView = (GridView) view.findViewById(R.id.homepage_GridView);
        mSearchView = (SearchView) view.findViewById(R.id.Search_home);
        viewList = new ArrayList<>();
        gridItemList = new ArrayList<Map<String, Object>>();
        mHashSetTaskId = new HashSet<Integer>();
        mRequestTaskList = new ArrayList<Task>();

        flash(view);
        initGridView();
        initSearchView();


        return view;
    }

    /** Refresh the page*/
    public void flash(View view) {
        initPageRolling();
        initRecyclerView(view);
    }

    public void getRequest(int tag) {
        final int TEMP_TAG = tag;
        Retrofit retrofit = new Retrofit.Builder().baseUrl(this.getString(R.string.base_url)).addConverterFactory(GsonConverterFactory.create()).build();
        GetRequestHomeTaskList requestGetTaskList = retrofit.create(GetRequestHomeTaskList.class);
        Call<ResponseBody> call = requestGetTaskList.getCall();
        Log.i("TIMMMMMMMME!","NowTime:" + System.currentTimeMillis());
        Date d =new Date();
        Log.i(TAG,"The getTen request time: " + d.getTime());
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
                            Toast.makeText(mContext, getResources().getString(R.string.FailToGetData) + mRequestTaskList
                                .size(), Toast.LENGTH_SHORT).show();
                        }
                        if (TEMP_TAG == 0) mBeanListViewHomes.addAll(tempList);
                        else if(TEMP_TAG == 1)recyclerAdapter.addHeaderItem(tempList);
                        else recyclerAdapter.addFooterItem(tempList);
                        if (mSwipeRefreshLayout.isRefreshing())
                            mSwipeRefreshLayout.setRefreshing(false);
                        Log.i("TIMMMMMMMME!","NowTime:" + System.currentTimeMillis());
                        Toast.makeText(mContext, getResources().getString(R.string.Refresh) + tempList.size() + getResources().getString(R.string.Now) + mBeanListViewHomes
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


    private void initPageRolling() {

        ImageView imageView1 = new ImageView(mContext);
        imageView1.setImageResource(R.drawable.rollingimgae_1);
        imageView1.setScaleType(ImageView.ScaleType.CENTER_CROP);
        viewList.add(imageView1);

        ImageView imageView2 = new ImageView(mContext);
        imageView2.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView2.setImageResource(R.drawable.rollingimgae_2);
        viewList.add(imageView2);

        ImageView imageView3 = new ImageView(mContext);
        imageView3.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView3.setImageResource(R.drawable.rollingimgae_3);
        viewList.add(imageView3);


        mAdapterPagerViewHomeList = new AdapterPagerViewHome(viewList, mContext);
        mViewPager.setAdapter(mAdapterPagerViewHomeList);
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
        int iconId[] = {R.drawable.home_logo_1, R.drawable.home_logo_2, R.drawable.home_logo_3_1, R.drawable.home_logo_4, R.drawable.home_logo_5};
        String textS[] = {getResources().getString(R.string.home_grid_0), getResources().getString(R.string.home_grid_1), getResources().getString(R.string.home_grid_2), getResources().getString(R.string.home_grid_3), getResources().getString(R.string.home_grid_4)};
        for (int temp = 0; temp < textS.length; temp++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("gridItem_img", iconId[temp]);
            map.put("gridItem_text", textS[temp]);
            gridItemList.add(map);
        }

        String[] from = {"gridItem_img", "gridItem_text"};
        int[] to = {R.id.gridItem_img, R.id.gridIem_text};


        gridAdapter = new SimpleAdapter(getActivity(), gridItemList, R.layout.gridview_item_home, from, to);
        mGridView.setAdapter(gridAdapter);
        /**  gridItem clicks on the corresponding event to write */
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch(position){
                    case 0:
                        Intent intentSecurity = new Intent(getActivity(), ActivityGridPage.class);
                        intentSecurity.putExtra("pageName","Security");
                        startActivity(intentSecurity);
                        break;
                    case 1:
                        Intent intentEnvironment = new Intent(getActivity(), ActivityGridPage.class);
                        intentEnvironment.putExtra("pageName","Environment");
                        startActivity(intentEnvironment);
                        break;
                    case 2:
                        Intent intentDailyLife = new Intent(getActivity(), ActivityGridPage.class);
                        intentDailyLife.putExtra("pageName","Daily Life");
                        startActivity(intentDailyLife);
                        break;
                    case 3:
                        Intent intentBusiness = new Intent(getActivity(), ActivityGridPage.class);
                        intentBusiness.putExtra("pageName","Business");
                        startActivity(intentBusiness);
                        break;
                    case 4:
                        Intent intentMore = new Intent(getActivity(), ActivityGridPage.class);
                        intentMore.putExtra("pageName","More");
                        startActivity(intentMore);
                        break;
                }
            }
        });
    }


    @SuppressLint("WrongConstant")
    private void initRecyclerView(View view) {

        /** Initialize the task for the first time */
        if (mBeanListViewHomes == null) {
            mBeanListViewHomes = new ArrayList<BeanListViewHome>();
        }
       /** Initialize SwipeRefreshLayout */
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.homepage_swiperefreshLayout);
        mSwipeRefreshLayout.setColorSchemeColors(Color.RED, Color.BLUE, Color.GREEN);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.homepage_RecyclerView);
        /**  Go to the page to initialize the task list */
        firstListRefresh();
        recyclerAdapter = new AdapterRecyclerViewHome(mContext, mBeanListViewHomes);
        /** Listeners are bound to adapters */
        recyclerAdapter.setRecyclerItemClickListener(new MCSRecyclerItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                int loginUserId = Integer.parseInt(mContext.getSharedPreferences("user", MODE_PRIVATE).getString("userID", "-1"));
                /** Check if you are logged in */
                if (loginUserId == -1) {
                    Toast.makeText(mContext, getResources().getString(R.string.login_first), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(mContext, ActivityLogin.class);
                    startActivity(intent);
                }else{
                    Gson gson = new Gson();
                    mForTest.jumpToTaskDetailActivity(gson.toJson(
                        mBeanListViewHomes.get(position).getTask()));
                }
            }
        });

        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(recyclerAdapter);

        initRefreshListener();


    }

     /** Refresh the list for the first time */
    private void firstListRefresh() {
        mSwipeRefreshLayout.setRefreshing(true);
        new Handler().postDelayed(new Runnable() {
                                      @Override
                                      public void run() {
                                          getRequest(0);
                                      }

                                  }, 3000
        );
    }

    /** Initialize refresh listening, including rerequest for network data transfer */
    private void initRefreshListener() {
        initPullRefresh();
        initLoadMoreListener();
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

                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == recyclerAdapter.getItemCount()) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getNewTenTaskRequest();
                        }
                    }, 3000);

                }

            }
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                lastVisibleItem = layoutManager.findLastVisibleItemPosition();
            }
        });
    }




    public void getNewTenTaskRequest() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(this.getString(R.string.base_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        GetNewTenRequestHomeTaskList requestGetTaskList = retrofit.create(
            GetNewTenRequestHomeTaskList.class);

        Call<ResponseBody> call = requestGetTaskList.queryNewTenTask(minTaskId());

        Log.i(TAG,"The request info time start: " + System.currentTimeMillis());

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.i(TAG,"The request info time end: " + System.currentTimeMillis());
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
                                    tempList.add(new BeanListViewHome(R.drawable.cat_usericon, task.getUsername(), photoPath[new Random().nextInt(3)], getResources().getString(R.string.ordinaryTask), task));
                                }
                            }
                        } else {
                            Toast.makeText(mContext, getResources().getString(R.string.taskNoNew), Toast.LENGTH_SHORT).show();
                        }
                        recyclerAdapter.addFooterItem(tempList);


                        if (mSwipeRefreshLayout.isRefreshing())
                            mSwipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(mContext, getResources().getString(R.string.Refresh) + " " + tempList.size() + " " + getResources().getString(R.string.Now)  + " " + mBeanListViewHomes
                            .size()  + " " + getResources().getString(R.string.TaskNum), Toast.LENGTH_SHORT).show();
                    } catch (IOException exp) {
                        exp.printStackTrace();
                    }
                }else{
                    Toast.makeText(mContext, getResources().getString(R.string.FailToGetData), Toast.LENGTH_SHORT).show();
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

    private void initSearchView() {
        mSearchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mForTest.jumpToSearchActivity();
            }
        });
    }
    
    /** Set the callback Activity method for Fragments */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        try {
            mForTest = (ForTest) context;
        } catch (ClassCastException exp) {
            throw new ClassCastException(context.toString() + " must implement For_TestInterface");
        }
    }
}

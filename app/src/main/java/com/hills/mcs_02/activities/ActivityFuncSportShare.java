package com.hills.mcs_02.activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hills.mcs_02.func_sportsShare.StepService;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.hills.mcs_02.BaseActivity;
import com.hills.mcs_02.func_sportsShare.beans.FuncSportShareBaseBean;
import com.hills.mcs_02.func_sportsShare.beans.FuncSportShareStepShareListBean;
import com.hills.mcs_02.func_sportsShare.FuncSportShareAdapter;
import com.hills.mcs_02.func_sportsShare.UpdateUiCallBack;
import com.hills.mcs_02.R;

/** This class acts as a secondary page to start the Activity that serves as the cornerstone for the Fragment. */
public class ActivityFuncSportShare extends BaseActivity {
    private static final String TAG = "func_sportShare";

    private RecyclerView infoRv;
    private SwipeRefreshLayout swipeRefreshLayout;
    private FuncSportShareAdapter recyclerAdapter;
    private List<FuncSportShareBaseBean> beanList;


    private TextView stepCountTv;
    private Handler handler;
    private Boolean isBind;
    private StepService stepService;
    /** The IBinder Service can get instances of the service to call methods or data of the service. */
    private ServiceConnection serviceConnection;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_func_sportshare);

        Log.i(TAG,"计步服务即将开启");
        //Toast.makeText(this,"计步服务即将开启",Toast.LENGTH_SHORT).show();
        initService();
        Log.i(TAG,"计步服务已开启");
        //Toast.makeText(this,"计步服务已开启",Toast.LENGTH_SHORT).show();
        Log.i(TAG,"即将初始化运动分享列表");
        //Toast.makeText(this,"即将初始化运动分享列表",Toast.LENGTH_SHORT).show();
        initInfoList();
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(isBind){
            this.unbindService(serviceConnection);
        }
    }


    public void initService(){
        stepCountTv = findViewById(R.id.fragment_home_func_sportsShare_stepCount);
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 1) {
                    stepCountTv.setText(msg.arg1 + "");
                }
            }
        };
        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                StepService.LcBinder lcBinder = (StepService.LcBinder) service;
                stepService = lcBinder.getService();
                stepService.registerCallback(new UpdateUiCallBack() {
                    @Override
                    public void updateUi(int stepCount) {
                        /** The stepCount data is currently received, which is the latest step number. */
                        Message message = Message.obtain();
                        message.what = 1;
                        message.arg1 = stepCount;
                        handler.sendMessage(message);
                        Log.i(TAG,"updateUi当前步数"+stepCount);
                    }
                });
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };

        Log.i(TAG,"准备开启计步服务");
        Intent intent = new Intent(this, StepService.class);
        Log.i(TAG,"即将绑计步服务");
        isBind =  bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        Log.i(TAG,"当前服务绑定情况isBind参数为：" + isBind);
        startService(intent); /** Bind and start a service */

    }

    public void initInfoList(){
        Log.i(TAG,"即将初始化分享列表组件");
        infoRv = findViewById(R.id.activity_func_sport_rv);
        swipeRefreshLayout = findViewById(R.id.activity_func_sport_swiperefreshLayout);
        swipeRefreshLayout.setColorSchemeColors(Color.RED,Color.BLUE,Color.GREEN);
        Log.i(TAG,"初始化分享数据List");
        initBeanList();
        recyclerAdapter = new FuncSportShareAdapter(beanList,this);

        infoRv.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        infoRv.setAdapter(recyclerAdapter);
        infoRv.addItemDecoration(new ItemDecoration(10,20));

        initRefreshListener();
        Log.i(TAG,"初始化完成");
        Toast.makeText(this,"初始化完成",Toast.LENGTH_SHORT).show();
    }

    private void initBeanList(){
        beanList = new ArrayList<FuncSportShareBaseBean>();
        if(beanList.size() <= 0){
            for(int temp = 0; temp < 10; temp++){
                beanList.add(new FuncSportShareStepShareListBean(R.drawable.cat_usericon + "",
                    "User" + new Random().nextInt(1200),
                    "2019.1.3.15:55",
                    new Random().nextInt(30000) + ""));
            }
        }
    }


    /** Initialize refresh listener, including request for network data transfer. */
    private void initRefreshListener(){
        initPullRefresh();  /** The pull to refresh. */
        initLoadMoreListener(); /** Drop down to load more. */
    }

    private void initPullRefresh() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        List<FuncSportShareBaseBean> headData = new ArrayList<FuncSportShareBaseBean>();
                        for (int temp = 0; temp <5 ; temp++) {
                            headData.add(new FuncSportShareStepShareListBean(R.drawable.cat_usericon + "","User" + new Random().nextInt(1200),"2019.1.3.15:55",new Random().nextInt(30000) + ""));
                        }
                        recyclerAdapter.addHeaderItem(headData);

                        /** Refresh to complete. */
                        swipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(ActivityFuncSportShare.this, "更新了 "+headData.size()+" 条目数据,当前列表有" + beanList.size() + "条数据", Toast.LENGTH_SHORT).show();
                    }

                }, 3000);

            }
        });
    }

    private void initLoadMoreListener() {

        infoRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int lastVisibleItem ;
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                /** Determine the state of the RecyclerView when it is idle and when it is the last visible item before loading. */
                if(newState == RecyclerView.SCROLL_STATE_IDLE &&  lastVisibleItem + 1== recyclerAdapter.getItemCount()){
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            List<FuncSportShareBaseBean> footerData = new ArrayList<FuncSportShareBaseBean>();
                            for (int temp = 0; temp <5 ; temp++) {
                                footerData.add(new FuncSportShareStepShareListBean(R.drawable.cat_usericon + "","User" + new Random().nextInt(1200),"2019.1.3.15:55",new Random().nextInt(30000) + ""));
                            }
                            recyclerAdapter.addFooterItem(footerData);
                            Toast.makeText(ActivityFuncSportShare.this, "更新了 "+footerData.size()+" 条目数据,当前列表有" + beanList.size() + "条数据", Toast.LENGTH_SHORT).show();
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
                lastVisibleItem=layoutManager.findLastVisibleItemPosition();
            }
        });
    }

    /** Class that sets the spacing of the RecyclerView. */
    class ItemDecoration extends RecyclerView.ItemDecoration{
        private int leftAndRightMargin;
        private int btmMargin;

        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.left = leftAndRightMargin;
            outRect.right = leftAndRightMargin;
            outRect.bottom = btmMargin;

            /** If it is the first in the list, it also set the edge. */
            if (parent.getChildAdapterPosition(view) == 0) {
                outRect.top = btmMargin;
            }
        }

        public ItemDecoration(int leftAndRightMargin,int btmMargin) {
            this.leftAndRightMargin = leftAndRightMargin;
            this.btmMargin = btmMargin;
        }

    }





}

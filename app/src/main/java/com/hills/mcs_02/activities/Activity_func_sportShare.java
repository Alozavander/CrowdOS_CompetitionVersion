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
import androidx.annotation.NonNull;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.hills.mcs_02.BaseActivity;
import com.hills.mcs_02.R;
import com.hills.mcs_02.func_sportsShare.Func_sportShare_Adapter;
import com.hills.mcs_02.func_sportsShare.StepService;
import com.hills.mcs_02.func_sportsShare.UpdateUiCallBack;
import com.hills.mcs_02.func_sportsShare.beans.func_sportShare_BaseBean;
import com.hills.mcs_02.func_sportsShare.beans.func_sportShare_stepShareListBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

//该类作为二级页面启动为Fragment作基石的Activity
public class Activity_func_sportShare extends BaseActivity {
    private static final String TAG = "func_sportShare";

    private RecyclerView info_rv;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Func_sportShare_Adapter recyclerAdapter;
    private List<func_sportShare_BaseBean> beanList;


    private TextView stepCount_tv;
    private Handler handler;
    private Boolean isBind;
    private StepService stepService;
    //和绷定服务数据交换的桥梁，可以通过IBinder service获取服务的实例来调用服务的方法或者数据
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
        initInfo_List();
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
        stepCount_tv = findViewById(R.id.fragment_home_func_sportsShare_stepCount);
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 1) {
                    stepCount_tv.setText(msg.arg1 + "");
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
                        //当前接收到stepCount数据，就是最新的步数
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
        startService(intent); //绷定并且开启一个服务

    }

    public void initInfo_List(){
        Log.i(TAG,"即将初始化分享列表组件");
        //Toast.makeText(this,"即将初始化分享列表组件",Toast.LENGTH_SHORT).show();
        info_rv = findViewById(R.id.activity_func_sport_rv);
        swipeRefreshLayout = findViewById(R.id.activity_func_sport_swiperefreshLayout);
        swipeRefreshLayout.setColorSchemeColors(Color.RED,Color.BLUE,Color.GREEN);
        Log.i(TAG,"初始化分享数据List");
        //Toast.makeText(this,"初始化分享数据List",Toast.LENGTH_SHORT).show();
        initBeanList();
        recyclerAdapter = new Func_sportShare_Adapter(beanList,this);

        info_rv.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        info_rv.setAdapter(recyclerAdapter);
        info_rv.addItemDecoration(new ItemDecroration(10,20));

        initRefreshListener();
        Log.i(TAG,"初始化完成");
        Toast.makeText(this,"初始化完成",Toast.LENGTH_SHORT).show();
    }

    private void initBeanList(){
        beanList = new ArrayList<func_sportShare_BaseBean>();
        //网络获取预留
        //networkRequestForSportShareInfo();
        //测试用数据
        if(beanList.size() <= 0){
            //beanList.add(new func_sportShare_stepCounter());
            for(int i = 0; i < 10; i++){
                beanList.add(new func_sportShare_stepShareListBean(R.drawable.cat_usericon + "","User" + new Random().nextInt(1200),"2019.1.3.15:55",new Random().nextInt(30000) + ""));
            }
        }
    }

    private List<func_sportShare_BaseBean> networkRequestForSportShareInfo(){
        List<func_sportShare_BaseBean> list = new ArrayList<func_sportShare_BaseBean>();

        /*网络请求更新运动分享数据方法，考虑为更新数组*/
        return beanList;
    }

    //初始化刷新监听，包含再请求网络数据传输
    private void initRefreshListener(){
        initPullRefresh();  //上拉刷新
        initLoadMoreListener();  //下拉加载更多
    }

    private void initPullRefresh() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        List<func_sportShare_BaseBean> headDatas = new ArrayList<func_sportShare_BaseBean>();
                        for (int i = 0; i <5 ; i++) {
                            headDatas.add(new func_sportShare_stepShareListBean(R.drawable.cat_usericon + "","User" + new Random().nextInt(1200),"2019.1.3.15:55",new Random().nextInt(30000) + ""));
                        }
                        recyclerAdapter.AddHeaderItem(headDatas);

                        //刷新完成
                        swipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(Activity_func_sportShare.this, "更新了 "+headDatas.size()+" 条目数据,当前列表有" + beanList.size() + "条数据", Toast.LENGTH_SHORT).show();
                    }

                }, 3000);

            }
        });
    }

    private void initLoadMoreListener() {

        info_rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int lastVisibleItem ;
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                //判断RecyclerView的状态 是空闲时，同时，是最后一个可见的ITEM时才加载
                if(newState == RecyclerView.SCROLL_STATE_IDLE &&  lastVisibleItem + 1== recyclerAdapter.getItemCount()){
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            List<func_sportShare_BaseBean> footerDatas = new ArrayList<func_sportShare_BaseBean>();
                            for (int i = 0; i <5 ; i++) {
                                footerDatas.add(new func_sportShare_stepShareListBean(R.drawable.cat_usericon + "","User" + new Random().nextInt(1200),"2019.1.3.15:55",new Random().nextInt(30000) + ""));
                            }
                            recyclerAdapter.AddFooterItem(footerDatas);
                            Toast.makeText(Activity_func_sportShare.this, "更新了 "+footerDatas.size()+" 条目数据,当前列表有" + beanList.size() + "条数据", Toast.LENGTH_SHORT).show();
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
                lastVisibleItem=layoutManager.findLastVisibleItemPosition();
            }
        });
    }

    //给RecyclerView设置间距的类
    class ItemDecroration extends RecyclerView.ItemDecoration{
        private int leftAndRightMargin;
        private int btmMargin;

        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.left = leftAndRightMargin;
            outRect.right = leftAndRightMargin;
            outRect.bottom = btmMargin;

            //如果是列表中的第一个，也设置边缘
            if (parent.getChildAdapterPosition(view) == 0) {
                outRect.top = btmMargin;
            }
        }

        public ItemDecroration(int leftAndRightMargin,int btmMargin) {
            this.leftAndRightMargin = leftAndRightMargin;
            this.btmMargin = btmMargin;
        }

    }





}

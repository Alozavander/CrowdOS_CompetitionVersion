package com.hills.mcs_02.activities;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;

import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.hills.mcs_02.BaseActivity;
import com.hills.mcs_02.func_foodShare.FuncFoodShareAdapter;
import com.hills.mcs_02.func_foodShare.beans.FuncFoodShareFoodShareListBean;
import com.hills.mcs_02.R;

/** This class acts as a secondary page to start the Activity that serves as the cornerstone for the Fragment. */
public class ActivityFuncFoodShare extends BaseActivity {
    private static final String TAG = "func_foodShare";

    private RecyclerView infoRv;
    private SwipeRefreshLayout swipeRefreshLayout;
    private FuncFoodShareAdapter recyclerAdapter;
    private List<FuncFoodShareFoodShareListBean> beanList;
    private ImageView cameraIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_func_foodshare);

        bindCamera();


        Log.i(TAG, "即将初始化美食分享列表");
        initInfoList();
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void bindCamera(){
        cameraIv = findViewById(R.id.activity_func_foodShare_camera);
        cameraIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }

    public void initInfoList() {
        Log.i(TAG, "即将初始化分享列表组件");
        infoRv = findViewById(R.id.activity_func_food_rv);
        swipeRefreshLayout = findViewById(R.id.activity_func_food_swiperefreshLayout);
        swipeRefreshLayout.setColorSchemeColors(Color.RED, Color.BLUE, Color.GREEN);
        Log.i(TAG, "初始化分享数据List");
        initBeanList();
        recyclerAdapter = new FuncFoodShareAdapter(beanList);

        /** Install waterfall flow layout. */
        infoRv.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        infoRv.setAdapter(recyclerAdapter);
        infoRv.addItemDecoration(new ItemDecoration(this, 5));
        initRefreshListener();
        Log.i(TAG, "初始化完成");
    }

    private void initBeanList() {
        beanList = new ArrayList<FuncFoodShareFoodShareListBean>();
        if (beanList.size() <= 0) {
            for (int temp = 0; temp < 10; temp++) {
                if(temp % 2 == 0){
                    beanList.add(new FuncFoodShareFoodShareListBean(R.drawable.dog_usericon + "", "User" + new Random().nextInt(1200), "曲江这家创意菜很不错呢，晚上是酒吧，中午是饭店哦，推荐兵马俑巧克力~", R.drawable.testphoto_2 + "", "2019.1.3.15:55"));
                }else{
                    beanList.add(new FuncFoodShareFoodShareListBean(R.drawable.dog_usericon + "", "User" + new Random().nextInt(1200), "图片分享", R.drawable.testphoto_4 + "", "2019.3.3.15:55"));
                }
            }
        }
    }


    /** Initialize refresh listener, including request for network data transfer. */
    private void initRefreshListener() {
        initPullRefresh();/** The pull to refresh. */
        initLoadMoreListener(); /** Drop down to load more. */
    }

    private void initPullRefresh() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        List<FuncFoodShareFoodShareListBean> headDatas = new ArrayList<FuncFoodShareFoodShareListBean>();
                        for (int temp = 0; temp < 5; temp++) {
                            if(temp % 2 == 0){
                                beanList.add(new FuncFoodShareFoodShareListBean(R.drawable.dog_usericon + "", "User" + new Random().nextInt(1200), "曲江这家创意菜很不错呢，晚上是酒吧，中午是饭店哦，推荐兵马俑巧克力~", R.drawable.testphoto_2 + "", "2019.1.3.15:55"));
                            }else{
                                beanList.add(new FuncFoodShareFoodShareListBean(R.drawable.dog_usericon + "", "User" + new Random().nextInt(1200), "图片分享", R.drawable.testphoto_4 + "", "2019.3.3.15:55"));
                            }
                        }
                        recyclerAdapter.addHeaderItem(headDatas);

                       /** Refresh to complete. */
                        swipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(ActivityFuncFoodShare.this, getResources().getString(R.string.Refresh) + headDatas.size() + getResources().getString(R.string.Now) + beanList.size() + getResources().getString(R.string.TaskNum), Toast.LENGTH_SHORT).show();
                    }

                }, 3000);

            }
        });
    }

    private void initLoadMoreListener() {

        infoRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int lastVisibleItem;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                /** Determine the state of the RecyclerView when it is idle and when it is the last visible item before loading. */
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == recyclerAdapter.getItemCount()) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            List<FuncFoodShareFoodShareListBean> footerDatas = new ArrayList<FuncFoodShareFoodShareListBean>();
                            for (int temp = 0; temp < 5; temp++) {
                                if(temp % 2 == 0){
                                    beanList.add(new FuncFoodShareFoodShareListBean(R.drawable.dog_usericon + "", "User" + new Random().nextInt(1200), "曲江这家创意菜很不错呢，晚上是酒吧，中午是饭店哦，推荐兵马俑巧克力~", R.drawable.testphoto_2 + "", "2019.1.3.15:55"));
                                }else{
                                    beanList.add(new FuncFoodShareFoodShareListBean(R.drawable.dog_usericon + "", "User" + new Random().nextInt(1200), "图片分享", R.drawable.testphoto_4 + "", "2019.3.3.15:55"));
                                }
                            }
                            recyclerAdapter.addFooterItem(footerDatas);
                            Toast.makeText(ActivityFuncFoodShare.this, getResources().getString(R.string.Refresh) + footerDatas.size() + getResources().getString(R.string.Now) + beanList.size() + getResources().getString(R.string.TaskNum), Toast.LENGTH_SHORT).show();
                        }
                    }, 3000);

                }

            }

            /** Update the LastVisibleItem value. */
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                StaggeredGridLayoutManager layoutManager = (StaggeredGridLayoutManager) recyclerView.getLayoutManager();
                /** The last item visible */
                lastVisibleItem = findMax(layoutManager.findLastVisibleItemPositions(new int[layoutManager.getSpanCount()]));
            }

            private int findMax(int[] lastPositions) {
                int max = lastPositions[0];
                for (int value : lastPositions) {
                    if (value > max) {
                        max = value;
                    }
                }
                return max;
            }

        });
    }


    /** Class that sets the spacing of the RecyclerView. */
    class ItemDecoration extends RecyclerView.ItemDecoration {
        private Context context;
        private int interval;

        //interval
        public ItemDecoration(Context context, int interval) {
            this.context = context;
            this.interval = interval;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            StaggeredGridLayoutManager.LayoutParams params = (StaggeredGridLayoutManager.LayoutParams) view.getLayoutParams();
           /** Gets the index of item in span. */
            int spanIndex = params.getSpanIndex();
            int interval = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    this.interval, context.getResources().getDisplayMetrics());
            /** interval */
            if (spanIndex % 2 == 0) {
                outRect.left = 0;
            } else {
                /** set Item left interval to 5dp */
                outRect.left = interval;
            }
            /** Below the interval */
            outRect.bottom = interval;
        }
    }

}

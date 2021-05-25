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

//该类作为二级页面启动为Fragment作基石的Activity
public class ActivityFuncFoodShare extends BaseActivity {
    private static final String TAG = "func_foodShare";

    private RecyclerView infoRv;
    private SwipeRefreshLayout swipeRefreshLayout;
    private FuncFoodShareAdapter recyclerAdapter;
    private List<FuncFoodShareFoodShareListBean> beanList;
    private ImageView cameraIM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_func_foodshare);

        bindCamera();


        Log.i(TAG, "即将初始化美食分享列表");
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
    }

    public void bindCamera(){
        cameraIM = findViewById(R.id.activity_func_foodShare_camera);
        cameraIM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(Activity_func_FoodShare.this,Activity_func_FoodShare_publish.class);
                //startActivity(intent);
            }
        });
    }

    public void initInfoList() {
        Log.i(TAG, "即将初始化分享列表组件");
        //Toast.makeText(this,"即将初始化分享列表组件",Toast.LENGTH_SHORT).show();
        infoRv = findViewById(R.id.activity_func_food_rv);
        swipeRefreshLayout = findViewById(R.id.activity_func_food_swiperefreshLayout);
        swipeRefreshLayout.setColorSchemeColors(Color.RED, Color.BLUE, Color.GREEN);
        Log.i(TAG, "初始化分享数据List");
        //Toast.makeText(this,"初始化分享数据List",Toast.LENGTH_SHORT).show();
        initBeanList();
        recyclerAdapter = new FuncFoodShareAdapter(beanList);

        //安装瀑布流布局
        infoRv.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        infoRv.setAdapter(recyclerAdapter);
        infoRv.addItemDecoration(new ItemDecoration(this, 5));
        ;

        initRefreshListener();
        Log.i(TAG, "初始化完成");
    }

    private void initBeanList() {
        beanList = new ArrayList<FuncFoodShareFoodShareListBean>();
        //网络获取预留
        //networkRequestForfoodShareInfo();
        //测试用数据
        if (beanList.size() <= 0) {
            for (int i = 0; i < 10; i++) {
                if(i % 2 == 0){
                    beanList.add(new FuncFoodShareFoodShareListBean(R.drawable.dog_usericon + "", "User" + new Random().nextInt(1200), "曲江这家创意菜很不错呢，晚上是酒吧，中午是饭店哦，推荐兵马俑巧克力~", R.drawable.testphoto_2 + "", "2019.1.3.15:55"));
                }else{
                    beanList.add(new FuncFoodShareFoodShareListBean(R.drawable.dog_usericon + "", "User" + new Random().nextInt(1200), "图片分享", R.drawable.testphoto_4 + "", "2019.3.3.15:55"));
                }
            }
        }
    }

    private List<FuncFoodShareFoodShareListBean> networkRequestForfoodShareInfo() {
        List<FuncFoodShareFoodShareListBean> list = new ArrayList<FuncFoodShareFoodShareListBean>();

        /*网络请求更新运动分享数据方法，考虑为更新数组*/
        return beanList;
    }

    //初始化刷新监听，包含再请求网络数据传输
    private void initRefreshListener() {
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
                        List<FuncFoodShareFoodShareListBean> headDatas = new ArrayList<FuncFoodShareFoodShareListBean>();
                        for (int i = 0; i < 5; i++) {
                            if(i % 2 == 0){
                                beanList.add(new FuncFoodShareFoodShareListBean(R.drawable.dog_usericon + "", "User" + new Random().nextInt(1200), "曲江这家创意菜很不错呢，晚上是酒吧，中午是饭店哦，推荐兵马俑巧克力~", R.drawable.testphoto_2 + "", "2019.1.3.15:55"));
                            }else{
                                beanList.add(new FuncFoodShareFoodShareListBean(R.drawable.dog_usericon + "", "User" + new Random().nextInt(1200), "图片分享", R.drawable.testphoto_4 + "", "2019.3.3.15:55"));
                            }
                        }
                        recyclerAdapter.addHeaderItem(headDatas);

                        //刷新完成
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

                //判断RecyclerView的状态 是空闲时，同时，是最后一个可见的ITEM时才加载
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == recyclerAdapter.getItemCount()) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            List<FuncFoodShareFoodShareListBean> footerDatas = new ArrayList<FuncFoodShareFoodShareListBean>();
                            for (int i = 0; i < 5; i++) {
                                if(i % 2 == 0){
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

            //更新lastvisibleItem数值
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                StaggeredGridLayoutManager layoutManager = (StaggeredGridLayoutManager) recyclerView.getLayoutManager();
                //最后一个可见的ITEM
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


    //给RecyclerView设置间距的类
    class ItemDecoration extends RecyclerView.ItemDecoration {
        private Context context;
        private int interval;

        //interval 间隔
        public ItemDecoration(Context context, int interval) {
            this.context = context;
            this.interval = interval;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
//        int position = parent.getChildAdapterPosition(view);
            StaggeredGridLayoutManager.LayoutParams params = (StaggeredGridLayoutManager.LayoutParams) view.getLayoutParams();
            // 获取item在span中的下标
            int spanIndex = params.getSpanIndex();
            int interval = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    this.interval, context.getResources().getDisplayMetrics());
            // 中间间隔
            if (spanIndex % 2 == 0) {
                outRect.left = 0;
            } else {
                // item为奇数位，设置其左间隔为5dp
                outRect.left = interval;
            }
            // 下方间隔
            outRect.bottom = interval;
        }
    }

}

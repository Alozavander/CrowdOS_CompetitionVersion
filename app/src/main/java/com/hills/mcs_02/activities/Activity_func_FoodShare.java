package com.hills.mcs_02.activities;


import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.hills.mcs_02.BaseActivity;
import com.hills.mcs_02.R;
import com.hills.mcs_02.func_foodShare.Func_foodShareAdapter;
import com.hills.mcs_02.func_foodShare.beans.func_foodShare_foodShareListBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

//该类作为二级页面启动为Fragment作基石的Activity
public class Activity_func_FoodShare extends BaseActivity {
    private static final String TAG = "func_foodShare";

    private RecyclerView info_rv;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Func_foodShareAdapter recyclerAdapter;
    private List<func_foodShare_foodShareListBean> beanList;
    private ImageView cameara_im;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_func_foodshare);

        bindCamera();


        Log.i(TAG, "即将初始化美食分享列表");
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
    }

    public void bindCamera(){
        cameara_im = findViewById(R.id.activity_func_foodShare_camera);
        cameara_im.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(Activity_func_FoodShare.this,Activity_func_FoodShare_publish.class);
                //startActivity(intent);
            }
        });
    }

    public void initInfo_List() {
        Log.i(TAG, "即将初始化分享列表组件");
        //Toast.makeText(this,"即将初始化分享列表组件",Toast.LENGTH_SHORT).show();
        info_rv = findViewById(R.id.activity_func_food_rv);
        swipeRefreshLayout = findViewById(R.id.activity_func_food_swiperefreshLayout);
        swipeRefreshLayout.setColorSchemeColors(Color.RED, Color.BLUE, Color.GREEN);
        Log.i(TAG, "初始化分享数据List");
        //Toast.makeText(this,"初始化分享数据List",Toast.LENGTH_SHORT).show();
        initBeanList();
        recyclerAdapter = new Func_foodShareAdapter(beanList);

        //安装瀑布流布局
        info_rv.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        info_rv.setAdapter(recyclerAdapter);
        info_rv.addItemDecoration(new ItemDecoration(this, 5));
        ;

        initRefreshListener();
        Log.i(TAG, "初始化完成");
    }

    private void initBeanList() {
        beanList = new ArrayList<func_foodShare_foodShareListBean>();
        //网络获取预留
        //networkRequestForfoodShareInfo();
        //测试用数据
        if (beanList.size() <= 0) {
            for (int i = 0; i < 10; i++) {
                if(i % 2 == 0){
                    beanList.add(new func_foodShare_foodShareListBean(R.drawable.dog_usericon + "", "User" + new Random().nextInt(1200), "曲江这家创意菜很不错呢，晚上是酒吧，中午是饭店哦，推荐兵马俑巧克力~", R.drawable.testphoto_2 + "", "2019.1.3.15:55"));
                }else{
                    beanList.add(new func_foodShare_foodShareListBean(R.drawable.dog_usericon + "", "User" + new Random().nextInt(1200), "图片分享", R.drawable.testphoto_4 + "", "2019.3.3.15:55"));
                }
            }
        }
    }

    private List<func_foodShare_foodShareListBean> networkRequestForfoodShareInfo() {
        List<func_foodShare_foodShareListBean> list = new ArrayList<func_foodShare_foodShareListBean>();

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
                        List<func_foodShare_foodShareListBean> headDatas = new ArrayList<func_foodShare_foodShareListBean>();
                        for (int i = 0; i < 5; i++) {
                            if(i % 2 == 0){
                                beanList.add(new func_foodShare_foodShareListBean(R.drawable.dog_usericon + "", "User" + new Random().nextInt(1200), "曲江这家创意菜很不错呢，晚上是酒吧，中午是饭店哦，推荐兵马俑巧克力~", R.drawable.testphoto_2 + "", "2019.1.3.15:55"));
                            }else{
                                beanList.add(new func_foodShare_foodShareListBean(R.drawable.dog_usericon + "", "User" + new Random().nextInt(1200), "图片分享", R.drawable.testphoto_4 + "", "2019.3.3.15:55"));
                            }
                        }
                        recyclerAdapter.AddHeaderItem(headDatas);

                        //刷新完成
                        swipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(Activity_func_FoodShare.this, getResources().getString(R.string.Refresh) + headDatas.size() + getResources().getString(R.string.Now) + beanList.size() + getResources().getString(R.string.TaskNum), Toast.LENGTH_SHORT).show();
                    }

                }, 3000);

            }
        });
    }

    private void initLoadMoreListener() {

        info_rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int lastVisibleItem;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                //判断RecyclerView的状态 是空闲时，同时，是最后一个可见的ITEM时才加载
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == recyclerAdapter.getItemCount()) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            List<func_foodShare_foodShareListBean> footerDatas = new ArrayList<func_foodShare_foodShareListBean>();
                            for (int i = 0; i < 5; i++) {
                                if(i % 2 == 0){
                                    beanList.add(new func_foodShare_foodShareListBean(R.drawable.dog_usericon + "", "User" + new Random().nextInt(1200), "曲江这家创意菜很不错呢，晚上是酒吧，中午是饭店哦，推荐兵马俑巧克力~", R.drawable.testphoto_2 + "", "2019.1.3.15:55"));
                                }else{
                                    beanList.add(new func_foodShare_foodShareListBean(R.drawable.dog_usericon + "", "User" + new Random().nextInt(1200), "图片分享", R.drawable.testphoto_4 + "", "2019.3.3.15:55"));
                                }
                            }
                            recyclerAdapter.AddFooterItem(footerDatas);
                            Toast.makeText(Activity_func_FoodShare.this, getResources().getString(R.string.Refresh) + footerDatas.size() + getResources().getString(R.string.Now) + beanList.size() + getResources().getString(R.string.TaskNum), Toast.LENGTH_SHORT).show();
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

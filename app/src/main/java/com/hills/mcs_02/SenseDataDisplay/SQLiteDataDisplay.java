package com.hills.mcs_02.SenseDataDisplay;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.hills.mcs_02.R;
import com.hills.mcs_02.StringStore;
import com.hills.mcs_02.sensorFunction.SenseHelper;
import com.hills.mcs_02.sensorFunction.SensorSQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class SQLiteDataDisplay extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private Adapter_RecyclerView_SqliteDataDisplay mDataDisplay_adapter;
    private List<SQLiteData_bean> mList;
    private String mSensorType;
    private String TAG = "SQLiteDataDisplay";
    private int mListLoadRecord;        //记录当前列表加载数量

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sqlite_data_display);

        //获取Sensor的名字
        String sensorName = getIntent().getStringExtra("sensorName");
        mSensorType = SenseHelper.sensorXmlName2Type(this, sensorName) + "";
        Log.i(TAG, "The SQLiteDataDisplay sensor type is :" + mSensorType);

        //修改布局的title名字
        ((TextView) findViewById(R.id.sqlitedata_title)).setText(sensorName);

        initDataList();
        initViews();
        initRefreshListener();
    }

    //
    private void initDataList() {
        mList = new ArrayList<>();
        Cursor c = getSQLiteCursor();

        int len = 20 > c.getCount() ? c.getCount() : 20;
        if (c.getCount() > 0) {
            for (int i = 0; i < len; i++) {
                c.moveToPosition(i);
                SQLiteData_bean bean = new SQLiteData_bean();
                bean.setSenseData_id(c.getInt(0));
                bean.setSensorType(c.getInt(1));
                bean.setSenseTime(c.getString(2));
                bean.setSenseValue_1(c.getString(3));
                bean.setSenseValue_2(c.getString(4));
                bean.setSenseValue_3(c.getString(5));
                mList.add(bean);
                mListLoadRecord = i;
            }

        } else {
            Log.i(TAG, "There is no results.");
            Toast.makeText(this, "There is no results.", Toast.LENGTH_SHORT).show();
        }
        c.close();
    }

    private Cursor getSQLiteCursor() {
        SQLiteDatabase db = new SensorSQLiteOpenHelper(this).getReadableDatabase();
        Cursor c = db.query(StringStore.SensorDataTable_Name,
                new String[]{StringStore.SensorDataTable_id,
                        StringStore.SensorDataTable_SenseType,
                        StringStore.SensorDataTable_SenseTime,
                        StringStore.SensorDataTable_SenseData_1,
                        StringStore.SensorDataTable_SenseData_2,
                        StringStore.SensorDataTable_SenseData_3},
                StringStore.SensorDataTable_SenseType + "=?", new String[]{mSensorType}, null, null, null);
        return c;
    }

    private void initRefreshListener() {
        initPullRefresh();
        initLoadMoreListener();  //下拉加载更多
    }

    private void initPullRefresh() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(
                        new Runnable() {
                            @Override
                            public void run() {
                                mSwipeRefreshLayout.setRefreshing(false);
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
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == mDataDisplay_adapter.getItemCount()) {
                    mSwipeRefreshLayout.setRefreshing(true);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            LoadMoreData();
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                    }, 1000);

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

    private void LoadMoreData() {
        ArrayList<SQLiteData_bean> lBeans = new ArrayList<>();
        Cursor c = getSQLiteCursor();
        if (mListLoadRecord == c.getCount()) {
            Toast.makeText(SQLiteDataDisplay.this, getString(R.string.no_more_sqlitedata), Toast.LENGTH_SHORT).show();
        } else {
            int nextLen = mListLoadRecord + 20;
            nextLen = nextLen + 1 >= c.getCount() ? c.getCount() : nextLen;
            if (c.getCount() > 0) {
                int tem_max = -1;
                for (int i = mListLoadRecord; i < nextLen; i++) {
                    c.moveToPosition(i);
                    SQLiteData_bean bean = new SQLiteData_bean();
                    bean.setSenseData_id(c.getInt(0));
                    bean.setSensorType(c.getInt(1));
                    bean.setSenseTime(c.getString(2));
                    bean.setSenseValue_1(c.getString(3));
                    bean.setSenseValue_2(c.getString(4));
                    bean.setSenseValue_3(c.getString(5));
                    lBeans.add(bean);
                    tem_max = i;
                }
                mListLoadRecord = tem_max;
                mDataDisplay_adapter.AddFooterItem(lBeans);
            }
        }

    }


    private void initViews() {
        mRecyclerView = findViewById(R.id.sqlitedata_rv);
        mSwipeRefreshLayout = findViewById(R.id.sqlitedata_swiperefreshLayout);
        mSwipeRefreshLayout.setColorSchemeColors(Color.RED, Color.BLUE, Color.GREEN);
        mDataDisplay_adapter = new Adapter_RecyclerView_SqliteDataDisplay(mList, SQLiteDataDisplay.this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        mRecyclerView.setAdapter(mDataDisplay_adapter);

        findViewById(R.id.sqlitedata_back).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sqlitedata_back:
                finish();
        }
    }
}

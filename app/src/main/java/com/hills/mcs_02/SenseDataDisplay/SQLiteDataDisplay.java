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
    private AdapterRecyclerViewSqliteDataDisplay mDataDisplayAdapter;
    private List<SQLiteDataBean> mList;
    private String mSensorType;
    private String TAG = "SQLiteDataDisplay";
    private int mListLoadRecord;        /** Record the current List item number */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sqlite_data_display);

        /** Get the sensor name */
        String sensorName = getIntent().getStringExtra("sensorName");
        mSensorType = SenseHelper.sensorType2XmlName(this, sensorName) + "";
        Log.i(TAG, "The SQLiteDataDisplay sensor type is :" + mSensorType);

        /** Change the title of the layout */
        ((TextView) findViewById(R.id.sqlitedata_title)).setText(sensorName);

        initDataList();
        initViews();
        initRefreshListener();
    }

    private void initDataList() {
        mList = new ArrayList<>();
        Cursor c = getSqliteCursor();

        int len = 20 > c.getCount() ? c.getCount() : 20;
        if (c.getCount() > 0) {
            for (int temp = 0; temp < len; temp++) {
                c.moveToPosition(temp);
                SQLiteDataBean bean = new SQLiteDataBean();
                bean.setSenseDataId(c.getInt(0));
                bean.setSensorType(c.getInt(1));
                bean.setSenseTime(c.getString(2));
                bean.setSenseValue1(c.getString(3));
                bean.setSenseValue2(c.getString(4));
                bean.setSenseValue3(c.getString(5));
                mList.add(bean);
                mListLoadRecord = temp;
            }

        } else {
            Log.i(TAG, "There is no results.");
            Toast.makeText(this, "There is no results.", Toast.LENGTH_SHORT).show();
        }
        c.close();
    }

    private Cursor getSqliteCursor() {
        SQLiteDatabase db = new SensorSQLiteOpenHelper(this).getReadableDatabase();
        Cursor cur = db.query(StringStore.SENSOR_DATATABLE_NAME,
                new String[]{StringStore.SENSOR_DATATABLE_ID,
                        StringStore.SENSOR_DATATABLE_SENSE_TYPE,
                        StringStore.SENSOR_DATATABLE_SENSE_TIME,
                        StringStore.SENSOR_DATATABLE_SENSE_DATA_1,
                        StringStore.SENSOR_DATATABLE_SENSE_DATA_2,
                        StringStore.SENSOR_DATATABLE_SENSE_DATA_3},
                StringStore.SENSOR_DATATABLE_SENSE_TYPE + "=?", new String[]{mSensorType}, null, null, StringStore.SENSOR_DATATABLE_SENSE_TIME
                + " DESC");
        return cur;
    }

    private void initRefreshListener() {
        initPullRefresh();
        initLoadMoreListener();
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
                /** Judge the status of RecyclerView when it is idle, and at the same time, load only when it is the last visible ITEM  */
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == mDataDisplayAdapter
                    .getItemCount()) {
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

            /** Update the value of last visible Item */
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                lastVisibleItem = layoutManager.findLastVisibleItemPosition();
            }
        });
    }

    private void LoadMoreData() {
        ArrayList<SQLiteDataBean> lBeans = new ArrayList<>();
        Cursor cur = getSqliteCursor();
        if (mListLoadRecord == cur.getCount()) {
            Toast.makeText(SQLiteDataDisplay.this, getString(R.string.no_more_sqlitedata), Toast.LENGTH_SHORT).show();
        } else {
            int nextLen = mListLoadRecord + 20;
            nextLen = nextLen + 1 >= cur.getCount() ? cur.getCount() : nextLen;
            if (cur.getCount() > 0) {
                int temMax = -1;
                for (int i = mListLoadRecord; i < nextLen; i++) {
                    cur.moveToPosition(i);
                    SQLiteDataBean bean = new SQLiteDataBean();
                    bean.setSenseDataId(cur.getInt(0));
                    bean.setSensorType(cur.getInt(1));
                    bean.setSenseTime(cur.getString(2));
                    bean.setSenseValue1(cur.getString(3));
                    bean.setSenseValue2(cur.getString(4));
                    bean.setSenseValue3(cur.getString(5));
                    lBeans.add(bean);
                    temMax = i;
                }
                mListLoadRecord = temMax;
                mDataDisplayAdapter.addFooterItem(lBeans);
            }
        }

    }


    private void initViews() {
        mRecyclerView = findViewById(R.id.sqlitedata_rv);
        mSwipeRefreshLayout = findViewById(R.id.sqlitedata_swiperefreshLayout);
        mSwipeRefreshLayout.setColorSchemeColors(Color.RED, Color.BLUE, Color.GREEN);
        mDataDisplayAdapter = new AdapterRecyclerViewSqliteDataDisplay(mList, SQLiteDataDisplay.this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        mRecyclerView.setAdapter(mDataDisplayAdapter);

        findViewById(R.id.sqlitedata_back).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sqlitedata_back:
                finish();
        }
    }
}

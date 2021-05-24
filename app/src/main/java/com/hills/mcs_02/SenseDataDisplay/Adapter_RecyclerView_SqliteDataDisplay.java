package com.hills.mcs_02.SenseDataDisplay;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hills.mcs_02.R;
import com.hills.mcs_02.fragmentsPack.MCSRecyclerItemClickListener;

import java.util.List;

public class Adapter_RecyclerView_SqliteDataDisplay extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private final static String TAG = "Adapter_RecyclerView_SqliteDataDisplay";
    private List<SQLiteData_bean> mList;
    private LayoutInflater mInflater;
    private MCSRecyclerItemClickListener mListener;

    public Adapter_RecyclerView_SqliteDataDisplay(List<SQLiteData_bean> pList, Context pContext) {
        mList = pList;
        mInflater = LayoutInflater.from(pContext);
    }

    //根据viewType的不同返回对应的viewHolder，为实现多样化列表，这里用不到，直接返回一种viewholder
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType ==1){
            Log.i(TAG,"viewType为1，返回viewholder:sqlitedataDisplay_viewHolder");
            View view = mInflater.inflate(R.layout.sqlitedate_rv_item,parent,false);
            return new sqlitedataDisplay_viewHolder(view);
        }else {
            Log.i(TAG,"viewType返回值出错");
            return null;
        }

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof sqlitedataDisplay_viewHolder) {
            sqlitedataDisplay_viewHolder lHolder = (sqlitedataDisplay_viewHolder) holder;
            SQLiteData_bean lBean = (SQLiteData_bean) mList.get(position);

            lHolder.senseType_tv.setText(lBean.getSensorType()+"");
            lHolder.senseTime_tv.setText(lBean.getSenseTime());
            lHolder.senseValue_1_tv.setText(lBean.getSenseValue_1());
            if (lBean.getSenseValue_2() == null) lHolder.senseValue_2_tv.setVisibility(View.GONE);
            else lHolder.senseValue_2_tv.setText(lBean.getSenseValue_2());
            if (lBean.getSenseValue_3() == null) lHolder.senseValue_3_tv.setVisibility(View.GONE);
            else lHolder.senseValue_3_tv.setText(lBean.getSenseValue_3());
        }else{
            Log.i(TAG,"instance 错误");
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(mList.size() <= 0){
            return -1;
        }
        else {
            return 1;
        }
    }

    class sqlitedataDisplay_viewHolder extends RecyclerView.ViewHolder {
        private TextView senseTime_tv;
        private TextView senseType_tv;
        private TextView senseValue_1_tv;
        private TextView senseValue_2_tv;
        private TextView senseValue_3_tv;

        public sqlitedataDisplay_viewHolder(@NonNull View itemView) {
            super(itemView);
            senseType_tv = itemView.findViewById(R.id.sqlitedata_display_rvitem_sensorName_tv);
            senseTime_tv = itemView.findViewById(R.id.sqlitedata_display_rvitem_senseTime_tv);
            senseValue_1_tv = itemView.findViewById(R.id.sqlitedata_display_rvitem_senseValue_1_tv);
            senseValue_2_tv = itemView.findViewById(R.id.sqlitedata_display_rvitem_senseValue_2_tv);
            senseValue_3_tv = itemView.findViewById(R.id.sqlitedata_display_rvitem_senseValue_3_tv);
        }
    }

    public void AddHeaderItem(List<SQLiteData_bean> items) {
        mList.addAll(0, items);
        notifyDataSetChanged();
    }

    public void AddFooterItem(List<SQLiteData_bean> items) {
        mList.addAll(items);
        notifyDataSetChanged();
    }
}

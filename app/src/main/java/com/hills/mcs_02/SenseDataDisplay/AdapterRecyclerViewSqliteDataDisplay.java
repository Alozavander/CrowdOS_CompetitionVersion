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

import com.hills.mcs_02.fragmentsPack.MCSRecyclerItemClickListener;
import com.hills.mcs_02.R;

import java.util.List;

public class AdapterRecyclerViewSqliteDataDisplay extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private final static String TAG = "Adapter_RecyclerView_SqliteDataDisplay";
    private List<SqliteDataBean> mList;
    private LayoutInflater mInflater;
    private MCSRecyclerItemClickListener mListener;

    public AdapterRecyclerViewSqliteDataDisplay(List<SqliteDataBean> pList, Context pContext) {
        mList = pList;
        mInflater = LayoutInflater.from(pContext);
    }

    //根据viewType的不同返回对应的viewHolder，为实现多样化列表，这里用不到，直接返回一种viewholder
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == 1){
            Log.i(TAG,"viewType为1，返回viewholder:sqlitedataDisplay_viewHolder");
            View view = mInflater.inflate(R.layout.sqlitedate_rv_item,parent,false);
            return new sqliteDataDisplayViewHolder(view);
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
        if(holder instanceof sqliteDataDisplayViewHolder) {
            sqliteDataDisplayViewHolder lHolder = (sqliteDataDisplayViewHolder) holder;
            SqliteDataBean lBean = (SqliteDataBean) mList.get(position);

            lHolder.senseTypeTv.setText(lBean.getSensorType()+"");
            lHolder.senseTimeTv.setText(lBean.getSenseTime());
            lHolder.senseValue1Tv.setText(lBean.getSenseValue1());
            if (lBean.getSenseValue2() == null) lHolder.senseValue2Tv.setVisibility(View.GONE);
            else lHolder.senseValue2Tv.setText(lBean.getSenseValue2());
            if (lBean.getSenseValue3() == null) lHolder.senseValue3Tv.setVisibility(View.GONE);
            else lHolder.senseValue3Tv.setText(lBean.getSenseValue3());
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

    class sqliteDataDisplayViewHolder extends RecyclerView.ViewHolder {
        private TextView senseTimeTv;
        private TextView senseTypeTv;
        private TextView senseValue1Tv;
        private TextView senseValue2Tv;
        private TextView senseValue3Tv;

        public sqliteDataDisplayViewHolder(@NonNull View itemView) {
            super(itemView);
            senseTypeTv = itemView.findViewById(R.id.sqlitedata_display_rvitem_sensorName_tv);
            senseTimeTv = itemView.findViewById(R.id.sqlitedata_display_rvitem_senseTime_tv);
            senseValue1Tv = itemView.findViewById(R.id.sqlitedata_display_rvitem_senseValue_1_tv);
            senseValue2Tv = itemView.findViewById(R.id.sqlitedata_display_rvitem_senseValue_2_tv);
            senseValue3Tv = itemView.findViewById(R.id.sqlitedata_display_rvitem_senseValue_3_tv);
        }
    }

    public void addHeaderItem(List<SqliteDataBean> items) {
        mList.addAll(0, items);
        notifyDataSetChanged();
    }

    public void addFooterItem(List<SqliteDataBean> items) {
        mList.addAll(items);
        notifyDataSetChanged();
    }
}

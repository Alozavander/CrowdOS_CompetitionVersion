package com.hills.mcs_02.viewsAdapters;

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

public class Adapter_RecyclerView_setting_sensorData extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final static String TAG = "Adapter_RecyclerView_setting_sensorData";
    private List<String[]> mList;
    private LayoutInflater mInflater;
    private MCSRecyclerItemClickListener mListener;

    public Adapter_RecyclerView_setting_sensorData() {
        super();
    }

    public Adapter_RecyclerView_setting_sensorData(Context context, List<String[]> list) {
        mList = list;
        mInflater = LayoutInflater.from(context);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    //根据返回的viewType值创建不同的viewholder，对应不同的item布局,viewType的值是从getItemViewType()方法设置的
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view;
        if(viewType == 1){
            view = mInflater.inflate(R.layout.recyclerview_item_setting_sensordata_list,viewGroup,false);
            return new Setting_SensorData_ViewHolder(view,mListener);
        }else{
            Log.i(TAG,"viewType返回值出错");
            //Toast.makeText(mConetxt,"viewType返回值出错",Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    //返回Item的viewType
    @Override
    public int getItemViewType(int position) {
        if(mList.size() <= 0){
            return -1;
        }
        else {
            return 1;
        }
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int position) {

        if(viewHolder instanceof Setting_SensorData_ViewHolder) {
            Setting_SensorData_ViewHolder holder = (Setting_SensorData_ViewHolder) viewHolder;
            String[] lStrings =  mList.get(position);

            holder.mainName_tv.setText(lStrings[0]);
            if(lStrings[1] != null) holder.juniorContent_tv.setText(lStrings[1]);
            else holder.juniorContent_tv.setVisibility(View.GONE);

        }else{
            Log.i(TAG,"instance 错误");
            //Toast.makeText(mConetxt,"instance 错误",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    // ViewHolder用于缓存控件，三个属性分别对应item布局文件的三个控件
    class Setting_SensorData_ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mainName_tv;
        private TextView juniorContent_tv;
        private MCSRecyclerItemClickListener m_MCS_recyclerItemClickListener;


        public Setting_SensorData_ViewHolder(@NonNull View itemView, MCSRecyclerItemClickListener listener) {
            super(itemView);
            //对viewHolder的属性进行赋值
            mainName_tv = itemView.findViewById(R.id.activity_setting_sensorData_mainName);
            juniorContent_tv = itemView.findViewById(R.id.activity_setting_sensorData_juniorContent);

            //设置回调接口
            this.m_MCS_recyclerItemClickListener = listener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(m_MCS_recyclerItemClickListener != null){
                m_MCS_recyclerItemClickListener.onItemClick(v,getLayoutPosition());
            }
        }
    }

    //设置接口
    public void setRecyclerItemClickListener(MCSRecyclerItemClickListener listener) {
        this.mListener = listener;
    }

    public void AddHeaderItem(List<String[]> items){
        mList.addAll(0,items);
        notifyDataSetChanged();
    }

    public void AddFooterItem(List<String[]> items){
        mList.addAll(items);
        notifyDataSetChanged();
    }


}

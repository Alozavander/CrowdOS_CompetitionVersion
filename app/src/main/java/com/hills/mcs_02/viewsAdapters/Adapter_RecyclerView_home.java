package com.hills.mcs_02.viewsAdapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hills.mcs_02.R;
import com.hills.mcs_02.dataBeans.Bean_ListView_home;
import com.hills.mcs_02.fragmentsPack.MCSRecyclerItemClickListener;

import java.util.List;

public class Adapter_RecyclerView_home extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final static String TAG = "Adapter_RecyclerView_home";
    private List<Bean_ListView_home> mBean_ListView_homes;
    private Context mContext;
    private LayoutInflater mInflater;//布局装载器对象
    private MCSRecyclerItemClickListener mListener;
    //private Context mContext;

    public Adapter_RecyclerView_home(Context context, List<Bean_ListView_home> list) {
        mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.mBean_ListView_homes = list;
        //mContext = context;
    }




    //索引对应数据项的ID
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
            view = mInflater.inflate(R.layout.listview_item_homepage,viewGroup,false);
            return new Home_ViewHolder(view,mListener);
        }else{
            Log.i(TAG,"viewType返回值出错");
            //Toast.makeText(mConetxt,"viewType返回值出错",Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    //返回Item的viewType
    @Override
    public int getItemViewType(int position) {
        if(mBean_ListView_homes.size() <= 0){
            return -1;
        }
        else {
            return 1;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int position) {

        if(viewHolder instanceof Home_ViewHolder){
            Home_ViewHolder holder = (Home_ViewHolder) viewHolder;
            Bean_ListView_home bean = (Bean_ListView_home)mBean_ListView_homes.get(position);

            holder.coinsCount_tv.setText(bean.getCoinsCount());
            holder.deadline_tv.setText(mContext.getString(R.string.Task_Detail_deadline) + "  " + bean.getDeadline());
            holder.taskName_tv.setText(bean.getTask().getTaskName());
            if(bean.getTask().getTaskKind() == null) holder.taskKind_tv.setText(mContext.getString(R.string.ordinaryTask));
            else {
                switch (bean.getTask().getTaskKind()){
                    case 0: holder.taskKind_tv.setText(mContext.getString(R.string.home_grid_0));break;
                    case 1:holder.taskKind_tv.setText(mContext.getString(R.string.home_grid_1));break;
                    case 2:holder.taskKind_tv.setText(mContext.getString(R.string.home_grid_2));break;
                    case 3:holder.taskKind_tv.setText(mContext.getString(R.string.home_grid_3));break;
                    case 4:holder.taskKind_tv.setText(mContext.getString(R.string.ordinaryTask));break;
                }
            }
            holder.taskContent_tv.setText(bean.getTaskContent());
            holder.time_tv.setText(bean.getPostTime());
            holder.photo_iv.setImageResource(bean.getPhoto());
            //测试使用，实际应使用网络加载路径
            holder.userIcon_iv.setImageResource(bean.getUserIcon());
            holder.userID_tv.setText(bean.getUserID());
            holder.taskCount_tv.setText(bean.getTaskCount() + "");
        }else{
            Log.i(TAG,"instance 错误");
            //Toast.makeText(mConetxt,"instance 错误",Toast.LENGTH_SHORT).show();
        }
    }



    @Override
    public int getItemCount() {
        //Toast.makeText(mContext,"当前列表长度为：" + mBean_ListView_homes.size(),Toast.LENGTH_SHORT).show();
        return mBean_ListView_homes.size();
    }

    // ViewHolder用于缓存控件，三个属性分别对应item布局文件的三个控件
    class Home_ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView userIcon_iv;
        //private ImageView categoryIcon_iv;
        // private ImageView starIcon_iv;
        private TextView taskName_tv;
        private ImageView photo_iv;
        private TextView userID_tv;
        private TextView time_tv;
        private TextView taskKind_tv;
        private TextView taskContent_tv;
        private TextView taskCount_tv;
        private TextView coinsCount_tv;
        private TextView deadline_tv;
        private MCSRecyclerItemClickListener m_MCS_recyclerItemClickListener;


        public Home_ViewHolder(@NonNull View itemView, MCSRecyclerItemClickListener listener) {
            super(itemView);
            //对viewHolder的属性进行赋值
            taskName_tv = itemView.findViewById(R.id.listview_TaskName);
            userIcon_iv = (ImageView) itemView.findViewById(R.id.listview_userIcon);
            time_tv = (TextView) itemView.findViewById(R.id.listview_Time);
            taskKind_tv = (TextView) itemView.findViewById(R.id.listview_taskKind);
            taskContent_tv = (TextView) itemView.findViewById(R.id.listview_TaskContent);
            taskCount_tv = (TextView)itemView.findViewById(R.id.listview_TaskCount);
            coinsCount_tv = (TextView) itemView.findViewById(R.id.listview_CoinsCount);
            deadline_tv = (TextView) itemView.findViewById(R.id.listview_DeadlineText);
            userID_tv = (TextView)itemView.findViewById(R.id.listview_userID);
            photo_iv = itemView.findViewById(R.id.listview_Photo);

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

    public void AddHeaderItem(List<Bean_ListView_home> items){
        mBean_ListView_homes.addAll(0,items);
        notifyDataSetChanged();
    }

    public void AddFooterItem(List<Bean_ListView_home> items){
        mBean_ListView_homes.addAll(items);
        notifyDataSetChanged();
    }

}

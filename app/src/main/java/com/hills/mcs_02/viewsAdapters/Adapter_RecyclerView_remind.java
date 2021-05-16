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
import com.hills.mcs_02.dataBeans.Bean_ListView_remind;
import com.hills.mcs_02.dataBeans.Task;
import com.hills.mcs_02.fragmentsPack.MCS_RecyclerItemClickListener;

import java.util.List;

public class Adapter_RecyclerView_remind extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final static String TAG = "Adapter_RecyclerView_remind";
    private List<Bean_ListView_remind> mBean_listView_remindList;
    private LayoutInflater mInflater;
    private MCS_RecyclerItemClickListener mListener;

    public Adapter_RecyclerView_remind() {
        super();
    }

    public Adapter_RecyclerView_remind( Context context,List<Bean_ListView_remind> bean_listView_remindList) {
        mBean_listView_remindList = bean_listView_remindList;
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
            view = mInflater.inflate(R.layout.listview_item_remindpage,viewGroup,false);
            return new Remind_ViewHolder(view,mListener);
        }else{
            Log.i(TAG,"viewType返回值出错");
            //Toast.makeText(mConetxt,"viewType返回值出错",Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    //返回Item的viewType
    @Override
    public int getItemViewType(int position) {
        if(mBean_listView_remindList.size() <= 0){
            return -1;
        }
        else {
            return 1;
        }
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int position) {

        if(viewHolder instanceof Remind_ViewHolder) {
            Remind_ViewHolder holder = (Remind_ViewHolder) viewHolder;
            Bean_ListView_remind bean_listView_remind = (Bean_ListView_remind) mBean_listView_remindList.get(position);

            holder.userIcon_iv.setImageResource(bean_listView_remind.getUserIcon());
            holder.picture_iv.setImageResource(bean_listView_remind.getPicture());

            Task task = bean_listView_remind.getTask();
            holder.userID_tv.setText(task.getUserName());
            holder.leftTime_tv.setText(bean_listView_remind.getDeadline());
            holder.describe_tv.setText(bean_listView_remind.getKind());
            holder.taskName_tv.setText(bean_listView_remind.getTask().getTaskName());

            if(task.getDescribe_task().length() > 20) holder.taskContent_tv.setText(task.getDescribe_task().substring(0,19) + "...");
            else holder.taskContent_tv.setText(task.getDescribe_task());
            holder.coinsCount_tv.setText(task.getCoin() + "");
            holder.taskCount_tv.setText(task.getTotalNum() + "");

        }else{
            Log.i(TAG,"instance 错误");
            //Toast.makeText(mConetxt,"instance 错误",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return mBean_listView_remindList.size();
    }


    // ViewHolder用于缓存控件，三个属性分别对应item布局文件的三个控件
    class Remind_ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView userIcon_iv;
        private ImageView picture_iv;
        private TextView userID_tv;
        private TextView leftTime_tv;
        private TextView describe_tv;
        private TextView taskContent_tv;
        private TextView coinsCount_tv;
        private TextView taskCount_tv;
        private TextView taskName_tv;
        private MCS_RecyclerItemClickListener m_MCS_recyclerItemClickListener;


        public Remind_ViewHolder(@NonNull View itemView, MCS_RecyclerItemClickListener listener) {
            super(itemView);
            //对viewHolder的属性进行赋值
            userIcon_iv = (ImageView) itemView.findViewById(R.id.remindpage_tasklv_userIcon);
            picture_iv = (ImageView) itemView.findViewById(R.id.remindpage_tasklv_pic);
            userID_tv = (TextView) itemView.findViewById(R.id.remindpage_tasklv_userID);
            leftTime_tv = (TextView)itemView.findViewById(R.id.remindpage_tasklv_deadline);
            describe_tv = (TextView)itemView.findViewById(R.id.remindpage_tasklv_Describe);
            taskContent_tv = (TextView)itemView.findViewById(R.id.remindpage_tasklv_TaskContent);
            coinsCount_tv = (TextView)itemView.findViewById(R.id.remindpage_tasklv_CoinsCount);
            taskCount_tv = (TextView)itemView.findViewById(R.id.remindpage_tasklv_TaskCount);
            taskName_tv = itemView.findViewById(R.id.remindpage_tasklv_taskName);


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
    public void setRecyclerItemClickListener(MCS_RecyclerItemClickListener listener) {
        this.mListener = listener;
    }

    public void AddHeaderItem(List<Bean_ListView_remind> items){
        mBean_listView_remindList.addAll(0,items);
        notifyDataSetChanged();
    }

    public void AddFooterItem(List<Bean_ListView_remind> items){
        mBean_listView_remindList.addAll(items);
        notifyDataSetChanged();
    }


}

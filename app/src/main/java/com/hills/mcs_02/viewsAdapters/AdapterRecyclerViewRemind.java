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

import java.util.List;

import com.hills.mcs_02.dataBeans.BeanListViewRemind;
import com.hills.mcs_02.dataBeans.Task;
import com.hills.mcs_02.fragmentsPack.MCSRecyclerItemClickListener;
import com.hills.mcs_02.R;

public class AdapterRecyclerViewRemind extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final static String TAG = "AdapterRecyclerRemind";
    private List<BeanListViewRemind> mBeanListViewRemindList;
    private LayoutInflater mInflater;
    private MCSRecyclerItemClickListener mListener;

    public AdapterRecyclerViewRemind() {
        super();
    }

    public AdapterRecyclerViewRemind( Context context,List<BeanListViewRemind> beanListViewRemindList) {
        mBeanListViewRemindList = beanListViewRemindList;
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
            return new remindViewHolder(view,mListener);
        }else{
            Log.i(TAG,"viewType返回值出错");
            //Toast.makeText(mConetxt,"viewType返回值出错",Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    //返回Item的viewType
    @Override
    public int getItemViewType(int position) {
        if(mBeanListViewRemindList.size() <= 0){
            return -1;
        }
        else {
            return 1;
        }
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int position) {

        if(viewHolder instanceof remindViewHolder) {
            remindViewHolder holder = (remindViewHolder) viewHolder;
            BeanListViewRemind beanListViewRemind = (BeanListViewRemind) mBeanListViewRemindList
                .get(position);

            holder.userIconIv.setImageResource(beanListViewRemind.getUserIcon());
            holder.pictureIv.setImageResource(beanListViewRemind.getPicture());

            Task task = beanListViewRemind.getTask();
            holder.userIdTv.setText(task.getUsername());
            holder.leftTimeTv.setText(beanListViewRemind.getDeadline());
            holder.describeTv.setText(beanListViewRemind.getKind());
            holder.taskNameTv.setText(beanListViewRemind.getTask().getTaskName());

            if(task.getDescribeTask().length() > 20) holder.taskContentTv
                .setText(task.getDescribeTask().substring(0,19) + "...");
            else holder.taskContentTv.setText(task.getDescribeTask());
            holder.coinsCountTv.setText(task.getCoin() + "");
            holder.taskCountTv.setText(task.getTotalNum() + "");

        }else{
            Log.i(TAG,"instance 错误");
            //Toast.makeText(mConetxt,"instance 错误",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return mBeanListViewRemindList.size();
    }


    // ViewHolder用于缓存控件，三个属性分别对应item布局文件的三个控件
    class remindViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView userIconIv;
        private ImageView pictureIv;
        private TextView userIdTv;
        private TextView leftTimeTv;
        private TextView describeTv;
        private TextView taskContentTv;
        private TextView coinsCountTv;
        private TextView taskCountTv;
        private TextView taskNameTv;
        private MCSRecyclerItemClickListener mRecyclerItemClickListener;


        public remindViewHolder(@NonNull View itemView, MCSRecyclerItemClickListener listener) {
            super(itemView);
            //对viewHolder的属性进行赋值
            userIconIv = (ImageView) itemView.findViewById(R.id.remindpage_tasklv_userIcon);
            pictureIv = (ImageView) itemView.findViewById(R.id.remindpage_tasklv_pic);
            userIdTv = (TextView) itemView.findViewById(R.id.remindpage_tasklv_userID);
            leftTimeTv = (TextView)itemView.findViewById(R.id.remindpage_tasklv_deadline);
            describeTv = (TextView)itemView.findViewById(R.id.remindpage_tasklv_Describe);
            taskContentTv = (TextView)itemView.findViewById(R.id.remindpage_tasklv_TaskContent);
            coinsCountTv = (TextView)itemView.findViewById(R.id.remindpage_tasklv_CoinsCount);
            taskCountTv = (TextView)itemView.findViewById(R.id.remindpage_tasklv_TaskCount);
            taskNameTv = itemView.findViewById(R.id.remindpage_tasklv_taskName);


            //设置回调接口
            this.mRecyclerItemClickListener = listener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(mRecyclerItemClickListener != null){
                mRecyclerItemClickListener.onItemClick(view,getLayoutPosition());
            }
        }
    }

    //设置接口
    public void setRecyclerItemClickListener(MCSRecyclerItemClickListener listener) {
        this.mListener = listener;
    }

    public void addHeaderItem(List<BeanListViewRemind> items){
        mBeanListViewRemindList.addAll(0,items);
        notifyDataSetChanged();
    }

    public void addFooterItem(List<BeanListViewRemind> items){
        mBeanListViewRemindList.addAll(items);
        notifyDataSetChanged();
    }


}

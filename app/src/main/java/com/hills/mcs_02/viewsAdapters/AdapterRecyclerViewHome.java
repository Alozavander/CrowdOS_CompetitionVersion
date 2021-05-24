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

import com.hills.mcs_02.dataBeans.Bean_ListView_home;
import com.hills.mcs_02.fragmentsPack.MCSRecyclerItemClickListener;
import com.hills.mcs_02.R;

import java.util.List;

public class AdapterRecyclerViewHome extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final static String TAG = "Adapter_RecyclerView_home";
    private List<Bean_ListView_home> mBeanListViewHome;
    private Context mContext;
    private LayoutInflater mInflater;//布局装载器对象
    private MCSRecyclerItemClickListener mListener;
    //private Context mContext;

    public AdapterRecyclerViewHome(Context context, List<Bean_ListView_home> list) {
        mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.mBeanListViewHome = list;
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
            return new homeViewHolder(view,mListener);
        }else{
            Log.i(TAG,"viewType返回值出错");
            //Toast.makeText(mConetxt,"viewType返回值出错",Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    //返回Item的viewType
    @Override
    public int getItemViewType(int position) {
        if(mBeanListViewHome.size() <= 0){
            return -1;
        }
        else {
            return 1;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int position) {

        if(viewHolder instanceof homeViewHolder){
            homeViewHolder holder = (homeViewHolder) viewHolder;
            Bean_ListView_home bean = (Bean_ListView_home) mBeanListViewHome.get(position);

            holder.coinCountTv.setText(bean.getCoinsCount());
            holder.deadlineTv.setText(mContext.getString(R.string.Task_Detail_deadline) + "  " + bean.getDeadline());
            holder.taskNameTv.setText(bean.getTask().getTaskName());
            if(bean.getTask().getTaskKind() == null) holder.taskKindTv.setText(mContext.getString(R.string.ordinaryTask));
            else {
                switch (bean.getTask().getTaskKind()){
                    case 0: holder.taskKindTv.setText(mContext.getString(R.string.home_grid_0));break;
                    case 1:holder.taskKindTv.setText(mContext.getString(R.string.home_grid_1));break;
                    case 2:holder.taskKindTv.setText(mContext.getString(R.string.home_grid_2));break;
                    case 3:holder.taskKindTv.setText(mContext.getString(R.string.home_grid_3));break;
                    case 4:holder.taskKindTv.setText(mContext.getString(R.string.ordinaryTask));break;
                }
            }
            holder.taskContentTv.setText(bean.getTaskContent());
            holder.timeTv.setText(bean.getPostTime());
            holder.photoIv.setImageResource(bean.getPhoto());
            //测试使用，实际应使用网络加载路径
            holder.userIconIv.setImageResource(bean.getUserIcon());
            holder.userIdTv.setText(bean.getUserID());
            holder.taskCountTv.setText(bean.getTaskCount() + "");
        }else{
            Log.i(TAG,"instance 错误");
            //Toast.makeText(mConetxt,"instance 错误",Toast.LENGTH_SHORT).show();
        }
    }



    @Override
    public int getItemCount() {
        //Toast.makeText(mContext,"当前列表长度为：" + mBean_ListView_homes.size(),Toast.LENGTH_SHORT).show();
        return mBeanListViewHome.size();
    }

    // ViewHolder用于缓存控件，三个属性分别对应item布局文件的三个控件
    class homeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView userIconIv;
        //private ImageView categoryIcon_iv;
        // private ImageView starIcon_iv;
        private TextView taskNameTv;
        private ImageView photoIv;
        private TextView userIdTv;
        private TextView timeTv;
        private TextView taskKindTv;
        private TextView taskContentTv;
        private TextView taskCountTv;
        private TextView coinCountTv;
        private TextView deadlineTv;
        private MCSRecyclerItemClickListener mRecyclerItemClickListener;


        public homeViewHolder(@NonNull View itemView, MCSRecyclerItemClickListener listener) {
            super(itemView);
            //对viewHolder的属性进行赋值
            taskNameTv = itemView.findViewById(R.id.listview_TaskName);
            userIconIv = (ImageView) itemView.findViewById(R.id.listview_userIcon);
            timeTv = (TextView) itemView.findViewById(R.id.listview_Time);
            taskKindTv = (TextView) itemView.findViewById(R.id.listview_taskKind);
            taskContentTv = (TextView) itemView.findViewById(R.id.listview_TaskContent);
            taskCountTv = (TextView)itemView.findViewById(R.id.listview_TaskCount);
            coinCountTv = (TextView) itemView.findViewById(R.id.listview_CoinsCount);
            deadlineTv = (TextView) itemView.findViewById(R.id.listview_DeadlineText);
            userIdTv = (TextView)itemView.findViewById(R.id.listview_userID);
            photoIv = itemView.findViewById(R.id.listview_Photo);

            //设置回调接口
            this.mRecyclerItemClickListener = listener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(mRecyclerItemClickListener != null){
                mRecyclerItemClickListener.onItemClick(v,getLayoutPosition());
            }
        }
    }

    //设置接口
    public void setRecyclerItemClickListener(MCSRecyclerItemClickListener listener) {
        this.mListener = listener;
    }

    public void addHeaderItem(List<Bean_ListView_home> items){
        mBeanListViewHome.addAll(0,items);
        notifyDataSetChanged();
    }

    public void addFooterItem(List<Bean_ListView_home> items){
        mBeanListViewHome.addAll(items);
        notifyDataSetChanged();
    }

}

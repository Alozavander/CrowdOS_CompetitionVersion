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
import com.hills.mcs_02.dataBeans.BeanListViewHome;
import com.hills.mcs_02.fragmentsPack.MCSRecyclerItemClickListener;

import java.util.List;


public class AdapterRecyclerViewHome extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final static String TAG = "Adapter_RecyclerView_home";
    private List<BeanListViewHome> mBeanListViewHome;
    private Context mContext;
    private LayoutInflater mInflater;
    private MCSRecyclerItemClickListener mListener;

    public AdapterRecyclerViewHome(Context context, List<BeanListViewHome> list) {
        mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.mBeanListViewHome = list;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /** Create different viewHolders according to the returned viewType value, corresponding to different item layouts */
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view;
        if(viewType == 1){
            view = mInflater.inflate(R.layout.listview_item_homepage,viewGroup,false);
            return new homeViewHolder(view,mListener);
        }else{
            Log.i(TAG,"viewType返回值出错");
            return null;
        }
    }

    /** Return viewType of view */
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
            BeanListViewHome bean = (BeanListViewHome) mBeanListViewHome.get(position);

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
            holder.userIconIv.setImageResource(bean.getUserIcon());
            holder.userIdTv.setText(bean.getUserId());
            holder.taskCountTv.setText(bean.getTaskCount() + "");
        }else{
            Log.i(TAG,"instance 错误");
        }
    }



    @Override
    public int getItemCount() {
        return mBeanListViewHome.size();
    }

    /** ViewHolder is used to cache controls, and the three properties correspond to the three controls of the item layout file. */
    class homeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView userIconIv;
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
            /** Assign values to the attributes of viewHolder */
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

            /** Set callback interface */
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

    /** Set callback interface */
    public void setRecyclerItemClickListener(MCSRecyclerItemClickListener listener) {
        this.mListener = listener;
    }

    public void addHeaderItem(List<BeanListViewHome> items){
        mBeanListViewHome.addAll(0,items);
        notifyDataSetChanged();
    }

    public void addFooterItem(List<BeanListViewHome> items){
        mBeanListViewHome.addAll(items);
        notifyDataSetChanged();
    }
}

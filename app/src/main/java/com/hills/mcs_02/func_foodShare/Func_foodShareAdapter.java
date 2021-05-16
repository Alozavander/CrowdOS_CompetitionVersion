package com.hills.mcs_02.func_foodShare;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.hills.mcs_02.R;
import com.hills.mcs_02.func_foodShare.beans.func_foodShare_foodShareListBean;

import java.util.List;

public class Func_foodShareAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final String TAG = "func_foodShare";
    private final int FOOD_SHARE_VIEW = 1001;

    private List<func_foodShare_foodShareListBean> beanList;
    private AdapterView.OnItemClickListener listener;


    public Func_foodShareAdapter(List<func_foodShare_foodShareListBean> list) {
        super();
        this.beanList = list;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if(viewHolder instanceof FoodShareListViewHolder){
            FoodShareListViewHolder holder = (FoodShareListViewHolder) viewHolder;
            func_foodShare_foodShareListBean bean = beanList.get(position);
            Log.i(TAG,"SportsShareListViewHolde用例出现，当前bean的内容为：  " + bean.toString());
            holder.foodDescription_tv.setText(bean.getFoodDescription());
            holder.publishTime_tv.setText(bean.getPublishTime());
            holder.userName_tv.setText(bean.getUserName());
            //测试使用，实际应使用网络加载路径
            holder.userIcon_im.setImageResource(Integer.parseInt(bean.getUserIcon_path()));
            holder.food_im.setImageResource(Integer.parseInt(bean.getFoodImage_path()));
        }else {
            Log.i(TAG,"instance 错误");
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(beanList.size() <= 0){
            return -1;
        } else {
            return FOOD_SHARE_VIEW;
        }
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view;
        //Toast.makeText(mConetxt,"开始初始化Item布局",Toast.LENGTH_SHORT).show();
        if(viewType == FOOD_SHARE_VIEW){
            view =  LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recyclerview_item_func_foodshare_foodsharelist,viewGroup,false);
            return new FoodShareListViewHolder(view);
        }else{
            return null;
        }
    }



    @Override
    public int getItemCount() {
        return beanList.size();
    }

    class FoodShareListViewHolder extends RecyclerView.ViewHolder{
        ImageView userIcon_im;
        TextView userName_tv;
        TextView foodDescription_tv;
        ImageView food_im;
        TextView publishTime_tv;

        public FoodShareListViewHolder(@NonNull View itemView) {
            super(itemView);
            this.userIcon_im = itemView.findViewById(R.id.activity_func_foodShare_foodsl_userIcon);
            this.userName_tv = itemView.findViewById(R.id.activity_func_foodShare_foodsl_userName);
            this.foodDescription_tv = itemView.findViewById(R.id.activity_func_foodShare_foodsl_foodDescription);
            this.food_im =itemView.findViewById(R.id.activity_func_foodShare_foodsl_food);
            this.publishTime_tv = itemView.findViewById(R.id.activity_func_foodShare_foodsl_publishTime);
        }
    }

    public void AddHeaderItem(List<func_foodShare_foodShareListBean> items){
        //for test
        beanList.addAll(0,items);
        notifyItemRangeChanged(0,beanList.size());
    }

    public void AddFooterItem(List<func_foodShare_foodShareListBean> items){
        int preSize = beanList.size();
        beanList.addAll(items);
        notifyItemRangeChanged(preSize,beanList.size());
    }
}

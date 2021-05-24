package com.hills.mcs_02.func_foodShare;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.hills.mcs_02.func_foodShare.beans.FuncFoodShareFoodShareListBean;
import com.hills.mcs_02.R;

import java.util.List;

public class FuncFoodShareAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final String TAG = "func_foodShare";
    private final int FOOD_SHARE_VIEW = 1001;

    private List<FuncFoodShareFoodShareListBean> beanList;
    private AdapterView.OnItemClickListener listener;


    public FuncFoodShareAdapter(List<FuncFoodShareFoodShareListBean> list) {
        super();
        this.beanList = list;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if(viewHolder instanceof FoodShareListViewHolder){
            FoodShareListViewHolder holder = (FoodShareListViewHolder) viewHolder;
            FuncFoodShareFoodShareListBean bean = beanList.get(position);
            Log.i(TAG,"SportsShareListViewHolde用例出现，当前bean的内容为：  " + bean.toString());
            holder.foodDescriptionTv.setText(bean.getFoodDescription());
            holder.publishTimeTv.setText(bean.getPublishTime());
            holder.usernameTv.setText(bean.getUsername());
            //测试使用，实际应使用网络加载路径
            holder.userIconIm.setImageResource(Integer.parseInt(bean.getUserIconPath()));
            holder.foodIm.setImageResource(Integer.parseInt(bean.getFoodImagePath()));
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
        ImageView userIconIm;
        TextView usernameTv;
        TextView foodDescriptionTv;
        ImageView foodIm;
        TextView publishTimeTv;

        public FoodShareListViewHolder(@NonNull View itemView) {
            super(itemView);
            this.userIconIm = itemView.findViewById(R.id.activity_func_foodShare_foodsl_userIcon);
            this.usernameTv = itemView.findViewById(R.id.activity_func_foodShare_foodsl_userName);
            this.foodDescriptionTv = itemView.findViewById(R.id.activity_func_foodShare_foodsl_foodDescription);
            this.foodIm =itemView.findViewById(R.id.activity_func_foodShare_foodsl_food);
            this.publishTimeTv = itemView.findViewById(R.id.activity_func_foodShare_foodsl_publishTime);
        }
    }

    public void addHeaderItem(List<FuncFoodShareFoodShareListBean> items){
        //for test
        beanList.addAll(0,items);
        notifyItemRangeChanged(0,beanList.size());
    }

    public void addFooterItem(List<FuncFoodShareFoodShareListBean> items){
        int preSize = beanList.size();
        beanList.addAll(items);
        notifyItemRangeChanged(preSize,beanList.size());
    }
}

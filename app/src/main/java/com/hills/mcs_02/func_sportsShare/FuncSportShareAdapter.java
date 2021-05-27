package com.hills.mcs_02.func_sportsShare;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import com.hills.mcs_02.func_sportsShare.beans.FuncSportShareBaseBean;
import com.hills.mcs_02.func_sportsShare.beans.FuncSportShareStepShareListBean;
import com.hills.mcs_02.R;

public class FuncSportShareAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final String TAG = "func_sportShare";
    private final int STEP_COUNTER_VIEW = 1000;
    private final int SPORT_SHARE_VIEW = 1001;
    private final int EMPTY_VIEW = 1002;                   //progressbar


    private List<FuncSportShareBaseBean> beanList;
    private AdapterView.OnItemClickListener listener;
    private LayoutInflater mInflater;

    public FuncSportShareAdapter(List<FuncSportShareBaseBean> list,Context context) {
        this.beanList = list;
        mInflater = LayoutInflater.from(context);
    }

   /**
    * Create a different ViewHolder based on the returned ViewType value for the item layout.
    *  The ViewType value is set from the getItemViewType() method
    *  */
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view;
        if(viewType == STEP_COUNTER_VIEW){
            view =  LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recyclerview_item_func_sportshare_stepcounter,viewGroup,false);
            return new stepCounterViewHolder(view);
        }else if(viewType == SPORT_SHARE_VIEW){
            view =LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recyclerview_item_func_sportshare_sportsharelist,viewGroup,false);
            return new sportsShareListViewHolder(view);
        }else{
            Log.i(TAG,"viewType返回值出错");
            return null;
        }
    }

  /** Returns the viewType of the Item */
    @Override
    public int getItemViewType(int position) {
        if(beanList.size() <= 0){
            return -1;
        }
        else {
            return SPORT_SHARE_VIEW;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int position) {

        if(viewHolder instanceof stepCounterViewHolder){
            stepCounterViewHolder viewHolder1 = (stepCounterViewHolder) viewHolder;
        }else if(viewHolder instanceof sportsShareListViewHolder){
            sportsShareListViewHolder viewHolder1 = (sportsShareListViewHolder) viewHolder;
            FuncSportShareStepShareListBean bean = (FuncSportShareStepShareListBean)beanList.get(position);
            Log.i(TAG,"SportsShareListViewHolde用例出现，当前bean的内容为：  " + bean.getStepAmount() + "  " + bean.getUploadTime() + "  " + bean.getUserIconPath() + "  " + bean.getUsername() + "  " + bean.getViewType());
            viewHolder1.uploadTimeTv.setText(bean.getUploadTime());
            viewHolder1.usernameTv.setText(bean.getUsername());
            viewHolder1.userIconIv.setImageResource(Integer.parseInt(bean.getUserIconPath()));
        }else{
            Log.i(TAG,"instance 错误");
        }
    }

    @Override
    public int getItemCount() {
        return beanList.size();
    }


    class stepCounterViewHolder extends RecyclerView.ViewHolder{
        ImageView userIconIv;
        TextView stepCounterTv;

        public stepCounterViewHolder(@NonNull View itemView) {
            super(itemView);
            userIconIv = itemView.findViewById(R.id.activity_func_sportShare_stepCounter_userIcon);
            stepCounterTv = itemView.findViewById(R.id.activity_func_sportShare_stepCounter_stepCount);
        }
    }

    class sportsShareListViewHolder extends RecyclerView.ViewHolder{
        ImageView userIconIv;
        TextView usernameTv;
        TextView uploadTimeTv;
        TextView stepAmountTv;

        public sportsShareListViewHolder(@NonNull View itemView) {
            super(itemView);
            userIconIv = itemView.findViewById(R.id.activity_func_sportShare_sportsl_userIcon);
            usernameTv = itemView.findViewById(R.id.activity_func_sportShare_sportsl_userName);
            uploadTimeTv = itemView.findViewById(R.id.activity_func_sportShare_sportsl_time);
            stepAmountTv = itemView.findViewById(R.id.activity_func_sportShare_sportsl_mount);
        }
    }

    public void addHeaderItem(List<FuncSportShareBaseBean> items){
        beanList.addAll(0,items);
        notifyDataSetChanged();
    }

    public void addFooterItem(List<FuncSportShareBaseBean> items){
        beanList.addAll(items);
        notifyDataSetChanged();
    }

}

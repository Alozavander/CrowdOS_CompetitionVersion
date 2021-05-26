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
    private final int EMPTY_VIEW = 1002;                   //proressbar


    private List<FuncSportShareBaseBean> beanList;
    private AdapterView.OnItemClickListener listener;
    private LayoutInflater mInflater;

    public FuncSportShareAdapter(List<FuncSportShareBaseBean> list,Context context) {
        this.beanList = list;
        mInflater = LayoutInflater.from(context);
        //Toast.makeText(mConetxt,"成功创建RVAdapter，当前List长度为：" + beanList.size(),Toast.LENGTH_SHORT).show();
    }

    //根据返回的viewType值创建不同的viewholder，对应不同的item布局,viewType的值是从getItemViewType()方法设置的
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view;
        //Toast.makeText(mConetxt,"开始初始化Item布局",Toast.LENGTH_SHORT).show();
        if(viewType == STEP_COUNTER_VIEW){
            view =  LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recyclerview_item_func_sportshare_stepcounter,viewGroup,false);
            return new stepCounterViewHolder(view);
        }else if(viewType == SPORT_SHARE_VIEW){
            view =LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recyclerview_item_func_sportshare_sportsharelist,viewGroup,false);
            //Toast.makeText(mConetxt,"viewType返回值为SPORT_SHARE_VIEW",Toast.LENGTH_SHORT).show();
            return new sportsShareListViewHolder(view);
        }/*else if(viewType == EMPTY_VIEW){
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recyclerview_item_func_sportshare_emptyview,viewGroup,false);
            return new ProgressViewHolder(view);
        }*/else{
            Log.i(TAG,"viewType返回值出错");
            //Toast.makeText(mConetxt,"viewType返回值出错",Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    //返回Item的viewType
    @Override
    public int getItemViewType(int position) {
        if(beanList.size() <= 0){
            return -1;
        }/*else if (beanList.get(position) == null){
            return EMPTY_VIEW;
        }*/
        else {
            return SPORT_SHARE_VIEW;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int position) {

        if/*(viewHolder instanceof ProgressViewHolder){
            ProgressViewHolder viewHolder1 = (ProgressViewHolder) viewHolder;
            ((ProgressViewHolder) viewHolder).progressBar.setIndeterminate(true);
        }else if*/(viewHolder instanceof stepCounterViewHolder){
            stepCounterViewHolder viewHolder1 = (stepCounterViewHolder) viewHolder;
        }else if(viewHolder instanceof sportsShareListViewHolder){
            sportsShareListViewHolder viewHolder1 = (sportsShareListViewHolder) viewHolder;
            FuncSportShareStepShareListBean bean = (FuncSportShareStepShareListBean)beanList.get(position);
            Log.i(TAG,"SportsShareListViewHolde用例出现，当前bean的内容为：  " + bean.getStepAmount() + "  " + bean.getUploadTime() + "  " + bean.getUserIconPath() + "  " + bean.getUsername() + "  " + bean.getViewType());
            //Toast.makeText(mConetxt,"SportsShareListViewHolde用例出现，当前bean的内容为：  " + bean.getStepAmout() + "  " + bean.getUpLoadTime() + "  " + bean.getUserIcon_path() + "  " + bean.getUserName() + "  " + bean.getViewType(),Toast.LENGTH_SHORT).show();
            viewHolder1.stepAmountTv.setText(bean.getStepAmount());
            viewHolder1.uploadTimeTv.setText(bean.getUploadTime());
            viewHolder1.usernameTv.setText(bean.getUsername());
            //测试使用，实际应使用网络加载路径
            viewHolder1.userIconIv.setImageResource(Integer.parseInt(bean.getUserIconPath()));
        }else{
            Log.i(TAG,"instance 错误");
            //Toast.makeText(mConetxt,"instance 错误",Toast.LENGTH_SHORT).show();
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

        public TextView getStepCounterTv(){
            return this.stepCounterTv;
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

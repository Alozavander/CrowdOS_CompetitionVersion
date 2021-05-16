package com.hills.mcs_02.func_sportsShare;

import android.content.Context;
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
import com.hills.mcs_02.func_sportsShare.beans.func_sportShare_BaseBean;
import com.hills.mcs_02.func_sportsShare.beans.func_sportShare_stepShareListBean;

import java.util.List;

public class Func_sportShare_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final String TAG = "func_sportShare";
    private final int STEP_COUNTER_VIEW = 1000;
    private final int SPORT_SHARE_VIEW = 1001;
    private final int EMPTY_VIEW = 1002;                   //proressbar


    private List<func_sportShare_BaseBean> beanList;
    private AdapterView.OnItemClickListener listener;
    private LayoutInflater mInflater;

    public Func_sportShare_Adapter(List<func_sportShare_BaseBean> list,Context context) {
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
            return new StepCounterViewHolder(view);
        }else if(viewType == SPORT_SHARE_VIEW){
            view =LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recyclerview_item_func_sportshare_sportsharelist,viewGroup,false);
            //Toast.makeText(mConetxt,"viewType返回值为SPORT_SHARE_VIEW",Toast.LENGTH_SHORT).show();
            return new SportsShareListViewHolder(view);
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
        }else if*/(viewHolder instanceof StepCounterViewHolder){
            StepCounterViewHolder viewHolder1 = (StepCounterViewHolder) viewHolder;
        }else if(viewHolder instanceof SportsShareListViewHolder){
            SportsShareListViewHolder viewHolder1 = (SportsShareListViewHolder) viewHolder;
            func_sportShare_stepShareListBean bean = (func_sportShare_stepShareListBean)beanList.get(position);
            Log.i(TAG,"SportsShareListViewHolde用例出现，当前bean的内容为：  " + bean.getStepAmout() + "  " + bean.getUpLoadTime() + "  " + bean.getUserIcon_path() + "  " + bean.getUserName() + "  " + bean.getViewType());
            //Toast.makeText(mConetxt,"SportsShareListViewHolde用例出现，当前bean的内容为：  " + bean.getStepAmout() + "  " + bean.getUpLoadTime() + "  " + bean.getUserIcon_path() + "  " + bean.getUserName() + "  " + bean.getViewType(),Toast.LENGTH_SHORT).show();
            viewHolder1.stepAmount_tv.setText(bean.getStepAmout());
            viewHolder1.upLoadTime_tv.setText(bean.getUpLoadTime());
            viewHolder1.userName_tv.setText(bean.getUserName());
            //测试使用，实际应使用网络加载路径
            viewHolder1.userIcon_im.setImageResource(Integer.parseInt(bean.getUserIcon_path()));
        }else{
            Log.i(TAG,"instance 错误");
            //Toast.makeText(mConetxt,"instance 错误",Toast.LENGTH_SHORT).show();
        }
    }



    @Override
    public int getItemCount() {
        return beanList.size();
    }


    class StepCounterViewHolder extends RecyclerView.ViewHolder{
        ImageView userIcon_im;
        TextView stepCounter_tv;

        public StepCounterViewHolder(@NonNull View itemView) {
            super(itemView);
            userIcon_im = itemView.findViewById(R.id.activity_func_sportShare_stepCounter_userIcon);
            stepCounter_tv = itemView.findViewById(R.id.activity_func_sportShare_stepCounter_stepCount);
        }

        public TextView getStepCounter_tv(){
            return this.stepCounter_tv;
        }
    }

    class SportsShareListViewHolder extends RecyclerView.ViewHolder{
        ImageView userIcon_im;
        TextView userName_tv;
        TextView upLoadTime_tv;
        TextView stepAmount_tv;

        public SportsShareListViewHolder(@NonNull View itemView) {
            super(itemView);
            userIcon_im = itemView.findViewById(R.id.activity_func_sportShare_sportsl_userIcon);
            userName_tv = itemView.findViewById(R.id.activity_func_sportShare_sportsl_userName);
            upLoadTime_tv = itemView.findViewById(R.id.activity_func_sportShare_sportsl_time);
            stepAmount_tv = itemView.findViewById(R.id.activity_func_sportShare_sportsl_mount);
        }
    }

    public void AddHeaderItem(List<func_sportShare_BaseBean> items){
        beanList.addAll(0,items);
        notifyDataSetChanged();
    }

    public void AddFooterItem(List<func_sportShare_BaseBean> items){
        beanList.addAll(items);
        notifyDataSetChanged();
    }

}

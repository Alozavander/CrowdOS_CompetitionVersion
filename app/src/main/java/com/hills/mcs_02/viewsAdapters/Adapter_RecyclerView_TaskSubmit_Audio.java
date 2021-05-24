package com.hills.mcs_02.viewsAdapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hills.mcs_02.R;
import com.hills.mcs_02.fragmentsPack.MCSRecyclerItemClickListener;

import java.io.File;
import java.util.List;

public class Adapter_RecyclerView_TaskSubmit_Audio extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final static String TAG = "Adapter_RecyclerView_TaskSubmit_Audio";
    private List<File> mAudioFile_list;
    private LayoutInflater mInflater;
    private MCSRecyclerItemClickListener mListener;

    public Adapter_RecyclerView_TaskSubmit_Audio() {
        super();
    }

    public Adapter_RecyclerView_TaskSubmit_Audio(Context context, List<File> audio_list) {
        mAudioFile_list = audio_list;
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
            view = mInflater.inflate(R.layout.recyclerview_item_tasksubmit_audio,viewGroup,false);
            return new Audio_RV_ViewHolder(view,mListener);
        }else{
            Log.i(TAG,"viewType返回值出错");
            //Toast.makeText(mConetxt,"viewType返回值出错",Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    //返回Item的viewType
    @Override
    public int getItemViewType(int position) {
        if(mAudioFile_list.size() <= 0){
            return -1;
        }
        else {
            return 1;
        }
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int position) {

        if(viewHolder instanceof Audio_RV_ViewHolder) {
            Audio_RV_ViewHolder holder = (Audio_RV_ViewHolder) viewHolder;
            final String audioName =  mAudioFile_list.get(position).getName();

            holder.audioName_tv.setText(audioName);
            holder.delete_iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //这样写存在的问题探究
                    DeleteItem(mAudioFile_list.get(position));
                }
            });

        }else{
            Log.i(TAG,"instance 错误");
            //Toast.makeText(mConetxt,"instance 错误",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return mAudioFile_list.size();
    }


    // ViewHolder用于缓存控件，三个属性分别对应item布局文件的三个控件
    class Audio_RV_ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView delete_iv;
        private TextView audioName_tv;

        private MCSRecyclerItemClickListener m_MCS_recyclerItemClickListener;


        public Audio_RV_ViewHolder(@NonNull View itemView, MCSRecyclerItemClickListener listener) {
            super(itemView);
            //对viewHolder的属性进行赋值
            delete_iv = (ImageView) itemView.findViewById(R.id.taskSubmit_audio_rvitem_delete);
            audioName_tv = (TextView) itemView.findViewById(R.id.taskSubmit_audio_name_rvitem);



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

    public void AddHeaderItem(File item){
        mAudioFile_list.add(0,item);
        notifyDataSetChanged();
    }

    public void AddFooterItem(File item){
        mAudioFile_list.add(item);
        notifyDataSetChanged();
    }

    public void DeleteItem(File item){
        mAudioFile_list.remove(item);
        notifyDataSetChanged();
    }

}

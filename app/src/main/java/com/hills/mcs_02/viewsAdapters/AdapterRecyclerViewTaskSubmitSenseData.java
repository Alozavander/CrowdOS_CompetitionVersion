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

import java.io.File;
import java.util.List;

import com.hills.mcs_02.fragmentsPack.MCSRecyclerItemClickListener;
import com.hills.mcs_02.R;

public class AdapterRecyclerViewTaskSubmitSenseData extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final static String TAG = "Adapter_RecyclerView_TaskSubmit_SenseData";
    private List<File> mSenseDataFileList;
    private LayoutInflater mInflater;
    private MCSRecyclerItemClickListener mListener;

    public AdapterRecyclerViewTaskSubmitSenseData() {
        super();
    }

    public AdapterRecyclerViewTaskSubmitSenseData(Context context, List<File> audioList) {
        mSenseDataFileList = audioList;
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
            return new audioRvViewHolder(view,mListener);
        }else{
            Log.i(TAG,"viewType返回值出错");
            //Toast.makeText(mConetxt,"viewType返回值出错",Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    //返回Item的viewType
    @Override
    public int getItemViewType(int position) {
        if(mSenseDataFileList.size() <= 0){
            return -1;
        }
        else {
            return 1;
        }
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int position) {

        if(viewHolder instanceof audioRvViewHolder) {
            audioRvViewHolder holder = (audioRvViewHolder) viewHolder;
            final String audioName =  mSenseDataFileList.get(position).getName();

            holder.audioNameTv.setText(audioName);
            holder.deleteIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //这样写存在的问题探究
                    deleteItem(mSenseDataFileList.get(position));
                }
            });

        }else{
            Log.i(TAG,"instance 错误");
            //Toast.makeText(mConetxt,"instance 错误",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return mSenseDataFileList.size();
    }


    // ViewHolder用于缓存控件，三个属性分别对应item布局文件的三个控件
    class audioRvViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView deleteIv;
        private TextView audioNameTv;

        private MCSRecyclerItemClickListener mRecyclerItemClickListener;


        public audioRvViewHolder(@NonNull View itemView, MCSRecyclerItemClickListener listener) {
            super(itemView);
            //对viewHolder的属性进行赋值
            deleteIv = (ImageView) itemView.findViewById(R.id.taskSubmit_audio_rvitem_delete);
            audioNameTv = (TextView) itemView.findViewById(R.id.taskSubmit_audio_name_rvitem);



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

    public void addHeaderItem(File item){
        mSenseDataFileList.add(0,item);
        notifyDataSetChanged();
    }

    public void addFooterItem(File item){
        mSenseDataFileList.add(item);
        notifyDataSetChanged();
    }

    public void deleteItem(File item){
        mSenseDataFileList.remove(item);
        notifyDataSetChanged();
    }

}

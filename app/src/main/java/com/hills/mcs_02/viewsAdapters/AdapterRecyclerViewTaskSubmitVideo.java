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
import com.hills.mcs_02.fragmentsPack.MCSRecyclerItemClickListener;

import java.io.File;
import java.util.List;

public class AdapterRecyclerViewTaskSubmitVideo extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final static String TAG = "Adapter_RecyclerView_TaskSubmit_Video";
    private List<File> mVideoFileList;
    private LayoutInflater mInflater;
    private MCSRecyclerItemClickListener mListener;

    public AdapterRecyclerViewTaskSubmitVideo() {
        super();
    }

    public AdapterRecyclerViewTaskSubmitVideo(Context context, List<File> audioList) {
        mVideoFileList = audioList;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view;
        if(viewType == 1){
            view = mInflater.inflate(R.layout.recyclerview_item_tasksubmit_audio,viewGroup,false);
            return new audioRvViewHolder(view,mListener);
        }else{
            Log.i(TAG,"viewType返回值出错");
            return null;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(mVideoFileList.size() <= 0){
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
            final String audioName =  mVideoFileList.get(position).getName();

            holder.audioNameTv.setText(audioName);
            holder.deleteIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteItem(mVideoFileList.get(position));
                }
            });
        }else{
            Log.i(TAG,"instance 错误");
            //Toast.makeText(mConetxt,"instance 错误",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return mVideoFileList.size();
    }

    class audioRvViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView deleteIv;
        private TextView audioNameTv;

        private MCSRecyclerItemClickListener mRecyclerItemClickListener;

        public audioRvViewHolder(@NonNull View itemView, MCSRecyclerItemClickListener listener) {
            super(itemView);

            deleteIv = (ImageView) itemView.findViewById(R.id.taskSubmit_audio_rvitem_delete);
            audioNameTv = (TextView) itemView.findViewById(R.id.taskSubmit_audio_name_rvitem);

            /** Setting the callback Interface */
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

    public void setRecyclerItemClickListener(MCSRecyclerItemClickListener listener) {
        this.mListener = listener;
    }

    public void addHeaderItem(File item){
        mVideoFileList.add(0,item);
        notifyDataSetChanged();
    }

    public void addFooterItem(File item){
        mVideoFileList.add(item);
        notifyDataSetChanged();
    }

    public void deleteItem(File item){
        mVideoFileList.remove(item);
        notifyDataSetChanged();
    }
}

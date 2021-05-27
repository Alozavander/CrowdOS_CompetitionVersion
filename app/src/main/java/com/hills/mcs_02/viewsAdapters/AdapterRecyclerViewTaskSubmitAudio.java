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


public class AdapterRecyclerViewTaskSubmitAudio extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final static String TAG = "Adapter_RecyclerView_TaskSubmit_Audio";
    private List<File> mAudioFileList;
    private LayoutInflater mInflater;
    private MCSRecyclerItemClickListener mListener;

    public AdapterRecyclerViewTaskSubmitAudio() {
        super();
    }

    public AdapterRecyclerViewTaskSubmitAudio(Context context, List<File> audioList) {
        mAudioFileList = audioList;
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
        if(mAudioFileList.size() <= 0){
            return -1;
        }
        else {
            return 1;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int POSITIION) {
        if(viewHolder instanceof audioRvViewHolder) {
            audioRvViewHolder holder = (audioRvViewHolder) viewHolder;
            final String audioName =  mAudioFileList.get(POSITIION).getName();

            holder.audioNameTv.setText(audioName);
            holder.deleteIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    /** Delete the item */
                    DeleteItem(mAudioFileList.get(POSITIION));
                }
            });

        }else{
            Log.i(TAG,"instance 错误");
        }
    }

    @Override
    public int getItemCount() {
        return mAudioFileList.size();
    }

    class audioRvViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView deleteIv;
        private TextView audioNameTv;

        private MCSRecyclerItemClickListener mRecyclerItemClickListener;

        public audioRvViewHolder(@NonNull View itemView, MCSRecyclerItemClickListener listener) {
            super(itemView);

            deleteIv = (ImageView) itemView.findViewById(R.id.taskSubmit_audio_rvitem_delete);
            audioNameTv = (TextView) itemView.findViewById(R.id.taskSubmit_audio_name_rvitem);

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
        mAudioFileList.add(0,item);
        notifyDataSetChanged();
    }

    public void addFooterItem(File item){
        mAudioFileList.add(item);
        notifyDataSetChanged();
    }

    public void DeleteItem(File item){
        mAudioFileList.remove(item);
        notifyDataSetChanged();
    }
}

package com.hills.mcs_02.taskSubmit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hills.mcs_02.R;
import com.hills.mcs_02.fragmentsPack.MCSRecyclerItemClickListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

class AdapterRecyclerViewTaskSubmitImage extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int TYPE_ADD = 1;
    public static final int TYPE_SHOW = 2;
    private final static String TAG = "Adapter_RecyclerView_TaskSubmit_Image";
    private List<File> mImageFileList;
    private LayoutInflater mInflater;
    private MCSRecyclerItemClickListener mListener;
    private int maxNumber = 9;
    private Context mContext;


    public AdapterRecyclerViewTaskSubmitImage(Context context, List<File> imageList) {
        mContext = context;
        mImageFileList = imageList;
        mInflater = LayoutInflater.from(context);
    }

    public AdapterRecyclerViewTaskSubmitImage(Context context) {
        mImageFileList = new ArrayList<File>();
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_item_tasksubmit_image, viewGroup, false);
        return new imageRvViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int position) {
        /** If the type is add and the current position is the largest display position, the add icon will be loaded and the symbol will be deleted */
        if (getItemViewType(position) == TYPE_SHOW){
            imageRvViewHolder holder = (imageRvViewHolder)viewHolder;
            holder.deleteIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteItem(mImageFileList.get(position));
                }
            });
            Glide.with(mContext).load(mImageFileList.get(position)).into(holder.imageIv);
        }else{
            /** Hiden all the views */
            imageRvViewHolder holder = (imageRvViewHolder)viewHolder;
            holder.deleteIv.setVisibility(View.GONE);
            holder.imageIv.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mImageFileList.size();
    }

    @Override
    public int getItemViewType(int position) {
        /** Determine the location shows a picture or an ADD icon */
        if (position > mImageFileList.size() - 1) {
            return TYPE_ADD;
        } else {
            return TYPE_SHOW;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private boolean isShowAddItem(int position) {
        int size = mImageFileList.size();
        return position == size;
    }

    public void setMaxNumber(int maxNumber) {
        this.maxNumber = maxNumber;
    }

    public void setImageFileList(List<File> imageFileList) {
        mImageFileList = imageFileList;
    }

    public void setRecyclerItemClickListener(MCSRecyclerItemClickListener listener) {
        this.mListener = listener;
    }

    class imageRvViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView deleteIv;
        private ImageView imageIv;

        private MCSRecyclerItemClickListener mRecyclerItemClickListener;


        public imageRvViewHolder(@NonNull View itemView, MCSRecyclerItemClickListener listener) {
            super(itemView);
            deleteIv = (ImageView) itemView.findViewById(R.id.taskSubmit_image_rvitem_delete);
            imageIv = (ImageView) itemView.findViewById(R.id.taskSubmit_image_rvitem_pic);

            /** Setting the callback Listener */
            this.mRecyclerItemClickListener = listener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mRecyclerItemClickListener != null) {
                mRecyclerItemClickListener.onItemClick(view, getLayoutPosition());
            }
        }
    }
    
    public void addItem(File item){
        mImageFileList.add(item);
        notifyDataSetChanged();
    }

    public void addItemList(List<File> itemList){
        mImageFileList.addAll(itemList);
        notifyDataSetChanged();
    }

    public void deleteItem(File item){
        mImageFileList.remove(item);
        notifyDataSetChanged();
    }
}

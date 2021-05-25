package com.hills.mcs_02.taskSubmit;

import com.bumptech.glide.Glide;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.hills.mcs_02.fragmentsPack.MCSRecyclerItemClickListener;
import com.hills.mcs_02.R;

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
        //如果type为add并且当前位置是最大显示的位置，则加载add的图标，并将删除符号隐藏
        if /*(getItemViewType(position) == TYPE_ADD && position < maxNumber) {
            Image_RV_ViewHolder holder = (Image_RV_ViewHolder)viewHolder;
            holder.delete_iv.setVisibility(View.INVISIBLE);
            holder.image_iv.setImageResource(R.drawable.selector_image_add);
        }else if*/(getItemViewType(position) == TYPE_SHOW){
            imageRvViewHolder holder = (imageRvViewHolder)viewHolder;
            holder.deleteIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //这样写存在的问题探究
                    deleteItem(mImageFileList.get(position));
                    //Toast.makeText(mContext,mImageFile_list.size() + "",Toast.LENGTH_SHORT).show();
                }
            });
            //使用Glide给ImageView加载图片
            Glide.with(mContext).load(mImageFileList.get(position)).into(holder.imageIv);
        }else{
            //其余情况便隐藏
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
        //该位置是否显示图片，还是ADD图标
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

    //设置接口
    public void setRecyclerItemClickListener(MCSRecyclerItemClickListener listener) {
        this.mListener = listener;
    }

    // ViewHolder用于缓存控件，三个属性分别对应item布局文件的三个控件
    class imageRvViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView deleteIv;
        private ImageView imageIv;

        private MCSRecyclerItemClickListener mRecyclerItemClickListener;


        public imageRvViewHolder(@NonNull View itemView, MCSRecyclerItemClickListener listener) {
            super(itemView);
            //对viewHolder的属性进行赋值
            deleteIv = (ImageView) itemView.findViewById(R.id.taskSubmit_image_rvitem_delete);
            imageIv = (ImageView) itemView.findViewById(R.id.taskSubmit_image_rvitem_pic);


            //设置回调接口
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

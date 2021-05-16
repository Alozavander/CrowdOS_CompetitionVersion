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
import com.hills.mcs_02.fragmentsPack.MCS_RecyclerItemClickListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

class Adapter_RecyclerView_TaskSubmit_Image extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int TYPE_ADD = 1;
    public static final int TYPE_SHOW = 2;
    private final static String TAG = "Adapter_RecyclerView_TaskSubmit_Image";
    private List<File> mImageFile_list;
    private LayoutInflater mInflater;
    private MCS_RecyclerItemClickListener mListener;
    private int maxNumber = 9;
    private Context mContext;


    public Adapter_RecyclerView_TaskSubmit_Image(Context context, List<File> image_list) {
        mContext = context;
        mImageFile_list = image_list;
        mInflater = LayoutInflater.from(context);
    }

    public Adapter_RecyclerView_TaskSubmit_Image(Context context) {
        mImageFile_list = new ArrayList<File>();
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_item_tasksubmit_image, viewGroup, false);
        return new Image_RV_ViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int position) {
        //如果type为add并且当前位置是最大显示的位置，则加载add的图标，并将删除符号隐藏
        if /*(getItemViewType(position) == TYPE_ADD && position < maxNumber) {
            Image_RV_ViewHolder holder = (Image_RV_ViewHolder)viewHolder;
            holder.delete_iv.setVisibility(View.INVISIBLE);
            holder.image_iv.setImageResource(R.drawable.selector_image_add);
        }else if*/(getItemViewType(position) == TYPE_SHOW){
            Image_RV_ViewHolder holder = (Image_RV_ViewHolder)viewHolder;
            holder.delete_iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //这样写存在的问题探究
                    DeleteItem(mImageFile_list.get(position));
                    //Toast.makeText(mContext,mImageFile_list.size() + "",Toast.LENGTH_SHORT).show();
                }
            });
            //使用Glide给ImageView加载图片
            Glide.with(mContext).load(mImageFile_list.get(position)).into(holder.image_iv);
        }else{
            //其余情况便隐藏
            Image_RV_ViewHolder holder = (Image_RV_ViewHolder)viewHolder;
            holder.delete_iv.setVisibility(View.GONE);
            holder.image_iv.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mImageFile_list.size();
    }

    @Override
    public int getItemViewType(int position) {
        //该位置是否显示图片，还是ADD图标
        if (position > mImageFile_list.size() - 1) {
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
        int size = mImageFile_list.size();
        return position == size;
    }

    public void setMaxNumber(int maxNumber) {
        this.maxNumber = maxNumber;
    }

    public void setImageFile_list(List<File> imageFile_list) {
        mImageFile_list = imageFile_list;
    }

    //设置接口
    public void setRecyclerItemClickListener(MCS_RecyclerItemClickListener listener) {
        this.mListener = listener;
    }

    // ViewHolder用于缓存控件，三个属性分别对应item布局文件的三个控件
    class Image_RV_ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView delete_iv;
        private ImageView image_iv;

        private MCS_RecyclerItemClickListener m_MCS_recyclerItemClickListener;


        public Image_RV_ViewHolder(@NonNull View itemView, MCS_RecyclerItemClickListener listener) {
            super(itemView);
            //对viewHolder的属性进行赋值
            delete_iv = (ImageView) itemView.findViewById(R.id.taskSubmit_image_rvitem_delete);
            image_iv = (ImageView) itemView.findViewById(R.id.taskSubmit_image_rvitem_pic);


            //设置回调接口
            this.m_MCS_recyclerItemClickListener = listener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (m_MCS_recyclerItemClickListener != null) {
                m_MCS_recyclerItemClickListener.onItemClick(v, getLayoutPosition());
            }
        }
    }



    public void AddItem(File item){
        mImageFile_list.add(item);
        notifyDataSetChanged();
    }

    public void AddItemList(List<File> itemList){
        mImageFile_list.addAll(itemList);
        notifyDataSetChanged();
    }

    public void DeleteItem(File item){
        mImageFile_list.remove(item);
        notifyDataSetChanged();
    }
}

package com.hills.mcs_02.viewsAdapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hills.mcs_02.R;
import com.hills.mcs_02.dataBeans.Bean_Combine_u_ut;
import com.hills.mcs_02.fragmentsPack.MCS_RecyclerItemClickListener;

import java.io.File;
import java.util.List;

public class Adapter_RecyclerView_Published_TaskDetail extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final static String TAG = "Adapter_RecyclerView_remind";
    private List<Bean_Combine_u_ut> mBeanCombine_uuts;
    private LayoutInflater mInflater;
    private Context mContext;
    private MCS_RecyclerItemClickListener mListener;

    public Adapter_RecyclerView_Published_TaskDetail() {
        super();
    }

    public Adapter_RecyclerView_Published_TaskDetail(Context context, List<Bean_Combine_u_ut> list) {
        mBeanCombine_uuts = list;
        mContext = context;
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

        if (viewType == 1) {
            view = mInflater.inflate(R.layout.listview_item_published_taskdetail, viewGroup, false);
            return new PublishedTask_Detail_ViewHolder(view, mListener);
        } else {
            Log.i(TAG, "viewType返回值出错");
            //Toast.makeText(mConetxt,"viewType返回值出错",Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    //返回Item的viewType
    @Override
    public int getItemViewType(int position) {
        if (mBeanCombine_uuts.size() <= 0) {
            return -1;
        } else {
            return 1;
        }
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int position) {

        if (viewHolder instanceof PublishedTask_Detail_ViewHolder) {
            PublishedTask_Detail_ViewHolder holder = (PublishedTask_Detail_ViewHolder) viewHolder;
            Bean_Combine_u_ut beanCombine_uut = (Bean_Combine_u_ut) mBeanCombine_uuts.get(position);

            holder.userIcon_iv.setImageResource(beanCombine_uut.getUserIcon());
            holder.userName_tv.setText(beanCombine_uut.getUser().getUserName());

            //图片加载
            File pic = beanCombine_uut.getPic();
            if (pic == null) holder.imageView_1.setVisibility(View.GONE);
            else Glide.with(mContext).load(pic).centerCrop().into(holder.imageView_1);   //使用Glide加载图片，假如缩放
            //使得图片能够点击放大预览
            holder.imageView_1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*List<IThumbViewInfo> tempList = new ArrayList<IThumbViewInfo>();
                    tempList.add(new IThumbViewInfo());
                    GPreviewBuilder.from((Activity)mContext)
                            .to(ImagePreviewActivity.class)
                            .setData()*/
                }
            });


            holder.moreData_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //跳转到主题网站
                    String url = mContext.getString(R.string.webUrl);
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    mContext.startActivity(browserIntent);
                }
            });

            //if(task.getDescribe_task().length() > 20) holder.taskContent_tv.setText(task.getDescribe_task().substring(0,19) + "...");
            //else
            //放置任务完成者上传的任务数据
            String content = beanCombine_uut.getUt().getContent();
            if (content == null) content = "该用户尚未上传数据";
            holder.taskContent_tv.setText(content);


        } else {
            Log.i(TAG, "instance 错误");
            //Toast.makeText(mConetxt,"instance 错误",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return mBeanCombine_uuts.size();
    }


    // ViewHolder用于缓存控件，三个属性分别对应item布局文件的三个控件
    class PublishedTask_Detail_ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView userIcon_iv;
        private TextView userName_tv;
        private TextView taskContent_tv;
        private ImageView imageView_1;
        private TextView moreData_tv;
        private MCS_RecyclerItemClickListener m_MCS_recyclerItemClickListener;


        public PublishedTask_Detail_ViewHolder(@NonNull View itemView, MCS_RecyclerItemClickListener listener) {
            super(itemView);
            //对viewHolder的属性进行赋值
            userIcon_iv = (ImageView) itemView.findViewById(R.id.published_taskDetail_tasklv_userIcon);
            userName_tv = (TextView) itemView.findViewById(R.id.published_taskDetail_tasklv_userName);
            taskContent_tv = (TextView) itemView.findViewById(R.id.published_taskDetail_tasklv_TaskContent);
            imageView_1 = itemView.findViewById(R.id.published_taskDetail_tasklv_image1);
            moreData_tv = itemView.findViewById(R.id.published_taskDetail_moreData);
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

    //设置接口
    public void setRecyclerItemClickListener(MCS_RecyclerItemClickListener listener) {
        this.mListener = listener;
    }

    public void AddHeaderItem(List<Bean_Combine_u_ut> items) {
        mBeanCombine_uuts.addAll(0, items);
        notifyDataSetChanged();
    }

    public void AddFooterItem(List<Bean_Combine_u_ut> items) {
        mBeanCombine_uuts.addAll(items);
        notifyDataSetChanged();
    }


}

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
import com.hills.mcs_02.dataBeans.BeanUserTaskWithUser;
import com.hills.mcs_02.fragmentsPack.MCSRecyclerItemClickListener;

import java.io.File;
import java.util.List;

public class AdapterRecyclerViewPublishedTaskDetail extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final static String TAG = "Adapter_RecyclerView_remind";
    private List<BeanUserTaskWithUser> mBeanUserTaskWithUser;
    private LayoutInflater mInflater;
    private Context mContext;
    private MCSRecyclerItemClickListener mListener;

    public AdapterRecyclerViewPublishedTaskDetail(Context context, List<BeanUserTaskWithUser> list) {
        mBeanUserTaskWithUser = list;
        mContext = context;
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
        if (viewType == 1) {
            view = mInflater.inflate(R.layout.listview_item_published_taskdetail, viewGroup, false);
            return new publishedTaskDetailViewHolder(view, mListener);
        } else {
            Log.i(TAG, "viewType返回值出错");
            //Toast.makeText(mConetxt,"viewType返回值出错",Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mBeanUserTaskWithUser.size() <= 0) {
            return -1;
        } else {
            return 1;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int POSITION) {
        if (viewHolder instanceof publishedTaskDetailViewHolder) {
            publishedTaskDetailViewHolder holder = (publishedTaskDetailViewHolder) viewHolder;
            BeanUserTaskWithUser beanCombine_uut = (BeanUserTaskWithUser) mBeanUserTaskWithUser.get(POSITION);

            holder.userIconIv.setImageResource(beanCombine_uut.getUserIcon());
            holder.usernameTv.setText(beanCombine_uut.getUser().getUsername());

            /** Load the picture */
            File pic = beanCombine_uut.getPic();
            if (pic == null) holder.imageView.setVisibility(View.GONE);
            else Glide.with(mContext).load(pic).centerCrop().into(holder.imageView);   /** Use Glide to load pictures, if zoom */
            /** Make the picture clickable to enlarge and preview */
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    /*List<IThumbViewInfo> tempList = new ArrayList<IThumbViewInfo>();
                    tempList.add(new IThumbViewInfo());
                    GPreviewBuilder.from((Activity)mContext)
                            .to(ImagePreviewActivity.class)
                            .setData()*/
                }
            });

            holder.moreDataTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    /** Jumpt to the CrowdOS web */
                    String url = mContext.getString(R.string.webUrl);
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    mContext.startActivity(browserIntent);
                }
            });

            /** Place the task data uploaded by the task completer */
            String content = beanCombine_uut.getUserTask().getContent();
            if (content == null) content = "该用户尚未上传数据";
            holder.taskContentTv.setText(content);

        } else {
            Log.i(TAG, "instance 错误");
            //Toast.makeText(mConetxt,"instance 错误",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return mBeanUserTaskWithUser.size();
    }

    class publishedTaskDetailViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView userIconIv;
        private TextView usernameTv;
        private TextView taskContentTv;
        private ImageView imageView;
        private TextView moreDataTv;
        private MCSRecyclerItemClickListener mRecyclerItemClickListener;

        public publishedTaskDetailViewHolder(@NonNull View itemView, MCSRecyclerItemClickListener listener) {
            super(itemView);
            userIconIv = (ImageView) itemView.findViewById(R.id.published_taskDetail_tasklv_userIcon);
            usernameTv = (TextView) itemView.findViewById(R.id.published_taskDetail_tasklv_userName);
            taskContentTv = (TextView) itemView.findViewById(R.id.published_taskDetail_tasklv_TaskContent);
            imageView = itemView.findViewById(R.id.published_taskDetail_tasklv_image1);
            moreDataTv = itemView.findViewById(R.id.published_taskDetail_moreData);

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

    public void setRecyclerItemClickListener(MCSRecyclerItemClickListener listener) {
        this.mListener = listener;
    }

    public void addHeaderItem(List<BeanUserTaskWithUser> items) {
        mBeanUserTaskWithUser.addAll(0, items);
        notifyDataSetChanged();
    }

    public void addFooterItem(List<BeanUserTaskWithUser> items) {
        mBeanUserTaskWithUser.addAll(items);
        notifyDataSetChanged();
    }
}

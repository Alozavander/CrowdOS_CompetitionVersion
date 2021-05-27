package com.hills.mcs_02.viewsAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hills.mcs_02.R;
import com.hills.mcs_02.dataBeans.BeanListViewRemind;
import com.hills.mcs_02.dataBeans.Task;
import com.hills.mcs_02.fragmentsPack.MCSRecyclerItemClickListener;

import java.util.List;

public class AdapterListViewRemind extends BaseAdapter {
    private List<BeanListViewRemind> mBeanListViewRemindList;
    private LayoutInflater mInflater;
    private MCSRecyclerItemClickListener mListener;

    public AdapterListViewRemind() {
        super();
    }

    public AdapterListViewRemind( Context context,List<BeanListViewRemind> beanListViewRemindList) {
        mBeanListViewRemindList = beanListViewRemindList;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mBeanListViewRemindList.size();
    }

    @Override
    public Object getItem(int position) {
        return mBeanListViewRemindList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.listview_item_remindpage,null);

            viewHolder.userIconIv = (ImageView) convertView.findViewById(R.id.remindpage_tasklv_userIcon);
            viewHolder.pictureIv = (ImageView) convertView.findViewById(R.id.remindpage_tasklv_pic);
            viewHolder.userIdTv = (TextView) convertView.findViewById(R.id.remindpage_tasklv_userID);
            viewHolder.leftTimeTv = (TextView)convertView.findViewById(R.id.remindpage_tasklv_deadline);
            viewHolder.describeTv = (TextView)convertView.findViewById(R.id.remindpage_tasklv_Describe);
            viewHolder.taskContentTv = (TextView)convertView.findViewById(R.id.remindpage_tasklv_TaskContent);
            viewHolder.coinsCountTv = (TextView)convertView.findViewById(R.id.remindpage_tasklv_CoinsCount);
            viewHolder.taskCountTv = (TextView)convertView.findViewById(R.id.remindpage_tasklv_TaskCount);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        BeanListViewRemind beanListViewRemind = (BeanListViewRemind) mBeanListViewRemindList.get(position);

        viewHolder.userIconIv.setImageResource(beanListViewRemind.getUserIcon());
        viewHolder.pictureIv.setImageResource(beanListViewRemind.getPicture());

        Task task = beanListViewRemind.getTask();
        viewHolder.userIdTv.setText(task.getUsername());
        viewHolder.leftTimeTv.setText(beanListViewRemind.getDeadline());
        viewHolder.describeTv.setText(beanListViewRemind.getKind());
        viewHolder.taskContentTv.setText(task.getDescribeTask().substring(0,19) + "...");
        viewHolder.coinsCountTv.setText(task.getCoin() + "");
        viewHolder.taskCountTv.setText(task.getTotalNum() + "");

        return convertView;
    }

    class ViewHolder{
        private ImageView userIconIv;
        private ImageView pictureIv;
        private TextView userIdTv;
        private TextView leftTimeTv;
        private TextView describeTv;
        private TextView taskContentTv;
        private TextView coinsCountTv;
        private TextView taskCountTv;
    }
}

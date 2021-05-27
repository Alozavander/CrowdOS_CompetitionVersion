package com.hills.mcs_02.viewsAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hills.mcs_02.R;
import com.hills.mcs_02.dataBeans.BeanListViewMineMinor2Accepted;

import java.util.List;

public class AdapterListViewMinMinor2Accepted extends BaseAdapter {
    private List<BeanListViewMineMinor2Accepted> mBeanListViewMineMinor2Accepted;
    private LayoutInflater mInflater;
    

    public AdapterListViewMinMinor2Accepted(Context context, List<BeanListViewMineMinor2Accepted> list) {
        mInflater = LayoutInflater.from(context);
        mBeanListViewMineMinor2Accepted = list;
    }

    @Override
    public int getCount() {
        return mBeanListViewMineMinor2Accepted.size();
    }

    @Override
    public Object getItem(int position) {
        return mBeanListViewMineMinor2Accepted.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        /** Judge whether the convertView is initiated or not */
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.listview_item_minepage_minor2,null);

            viewHolder.picIv = (ImageView) convertView.findViewById(R.id.minepage_minor2_lvItem_pic);
            viewHolder.taskIdTv = (TextView) convertView.findViewById(R.id.minepage_minor2_lvItem_taskID);
            viewHolder.taskStateTv = (TextView) convertView.findViewById(R.id.minepage_minor2_lvItem_taskState);
            viewHolder.describeTv = (TextView) convertView.findViewById(R.id.minepage_minor2_lvItem_Describe);
            viewHolder.taskContentTv = (TextView) convertView.findViewById(R.id.minepage_minor2_lvItem_TaskContent);
            viewHolder.coinCountTv = (TextView) convertView.findViewById(R.id.minepage_minor2_lvItem_CoinsCount);
            viewHolder.taskCountTv = (TextView) convertView.findViewById(R.id.minepage_minor2_lvItem_TaskCount);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        BeanListViewMineMinor2Accepted bean = mBeanListViewMineMinor2Accepted.get(position);
        viewHolder.picIv.setImageResource(bean.getPic());
        viewHolder.taskIdTv.setText(bean.getTaskId());
        viewHolder.taskStateTv.setText(bean.getTaskState());
        viewHolder.describeTv.setText(bean.getDescribe());
        viewHolder.taskContentTv.setText(bean.getTaskContent());
        viewHolder.coinCountTv.setText(bean.getCoinCount());
        viewHolder.taskCountTv.setText(bean.getTaskCount());

        return convertView;
    }

    class ViewHolder{
        private ImageView picIv;
        private TextView taskIdTv;
        private TextView taskStateTv;
        private TextView describeTv;
        private TextView taskContentTv;
        private TextView coinCountTv;
        private TextView taskCountTv;

    }
}

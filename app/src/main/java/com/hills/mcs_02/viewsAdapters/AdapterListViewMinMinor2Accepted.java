package com.hills.mcs_02.viewsAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hills.mcs_02.dataBeans.Bean_ListView_mine_minor2_accepted;
import com.hills.mcs_02.R;


import java.util.List;

public class AdapterListViewMinMinor2Accepted extends BaseAdapter {
    private List<Bean_ListView_mine_minor2_accepted> mBeanListViewMineMinor2Accepted;
    private LayoutInflater mInflater;
    

    public AdapterListViewMinMinor2Accepted(Context context, List<Bean_ListView_mine_minor2_accepted> list) {
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
        //如果view未被实例化过，缓存池中没有对应的缓存
        if (convertView == null) {
            viewHolder = new ViewHolder();
            // 由于我们只需要将XML转化为View，并不涉及到具体的布局，所以第二个参数通常设置为null
            convertView = mInflater.inflate(R.layout.listview_item_minepage_minor2,null);

            viewHolder.picIv = (ImageView) convertView.findViewById(R.id.minepage_minor2_lvItem_pic);
            viewHolder.taskIdTv = (TextView) convertView.findViewById(R.id.minepage_minor2_lvItem_taskID);
            viewHolder.taskStateTv = (TextView) convertView.findViewById(R.id.minepage_minor2_lvItem_taskState);
            viewHolder.describeTv = (TextView) convertView.findViewById(R.id.minepage_minor2_lvItem_Describe);
            viewHolder.taskContentTv = (TextView) convertView.findViewById(R.id.minepage_minor2_lvItem_TaskContent);
            viewHolder.coinCountTv = (TextView) convertView.findViewById(R.id.minepage_minor2_lvItem_CoinsCount);
            viewHolder.taskCountTv = (TextView) convertView.findViewById(R.id.minepage_minor2_lvItem_TaskCount);


            //通过setTag将convertView与viewHolder关联
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Bean_ListView_mine_minor2_accepted bean = mBeanListViewMineMinor2Accepted.get(position);
        viewHolder.picIv.setImageResource(bean.getPic());
        viewHolder.taskIdTv.setText(bean.getTaskID());
        viewHolder.taskStateTv.setText(bean.getTaksState());
        viewHolder.describeTv.setText(bean.getDescribe());
        viewHolder.taskContentTv.setText(bean.getTaskContent());
        viewHolder.coinCountTv.setText(bean.getCoinsCount());
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

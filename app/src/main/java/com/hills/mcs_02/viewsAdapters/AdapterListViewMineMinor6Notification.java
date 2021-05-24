package com.hills.mcs_02.viewsAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hills.mcs_02.dataBeans.Bean_ListView_mine_minor6_notification;
import com.hills.mcs_02.R;

import java.util.List;

public class AdapterListViewMineMinor6Notification extends BaseAdapter {
    private List<Bean_ListView_mine_minor6_notification> mBeanListViewMineMinor6Notifications;
    private LayoutInflater mInflater;

    public AdapterListViewMineMinor6Notification() {
        super();
    }

    public AdapterListViewMineMinor6Notification(List<Bean_ListView_mine_minor6_notification> list, Context context) {
        this.mBeanListViewMineMinor6Notifications = list;
        this.mInflater = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return mBeanListViewMineMinor6Notifications.size();
    }

    @Override
    public Object getItem(int position) {
        return mBeanListViewMineMinor6Notifications.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.listview_item_minepage_minor6,null);

            viewHolder.iconIv = (ImageView) convertView.findViewById(R.id.minepage_minor6_Icon);
            viewHolder.idTv = (TextView) convertView.findViewById(R.id.minepage_minor6_ID);
            viewHolder.timeTv = (TextView) convertView.findViewById(R.id.minepage_minor6_Time);
            viewHolder.contentTv = (TextView) convertView.findViewById(R.id.minepage_minor6_Content);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }

        Bean_ListView_mine_minor6_notification bean = mBeanListViewMineMinor6Notifications.get(position);
        viewHolder.iconIv.setImageResource(bean.getIcon());
        viewHolder.idTv.setText(bean.getID());
        viewHolder.timeTv.setText(bean.getTime());
        viewHolder.contentTv.setText(bean.getContent());

        return convertView;
    }

    class ViewHolder{
        private ImageView iconIv;
        private TextView idTv;
        private TextView timeTv;
        private TextView contentTv;
    }
}

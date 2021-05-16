package com.hills.mcs_02.viewsAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hills.mcs_02.R;
import com.hills.mcs_02.dataBeans.Bean_ListView_mine_minor6_notification;

import java.util.List;

public class Adapter_ListView_mine_minor6_notification extends BaseAdapter {
    private List<Bean_ListView_mine_minor6_notification> mBean_listView_mine_minor6_notifications;
    private LayoutInflater mInflater;

    public Adapter_ListView_mine_minor6_notification() {
        super();
    }

    public Adapter_ListView_mine_minor6_notification(List<Bean_ListView_mine_minor6_notification> list, Context context) {
        this.mBean_listView_mine_minor6_notifications = list;
        this.mInflater = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return mBean_listView_mine_minor6_notifications.size();
    }

    @Override
    public Object getItem(int position) {
        return mBean_listView_mine_minor6_notifications.get(position);
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

            viewHolder.Icon_iv = (ImageView) convertView.findViewById(R.id.minepage_minor6_Icon);
            viewHolder.ID_tv = (TextView) convertView.findViewById(R.id.minepage_minor6_ID);
            viewHolder.Time_tv = (TextView) convertView.findViewById(R.id.minepage_minor6_Time);
            viewHolder.Content_tv = (TextView) convertView.findViewById(R.id.minepage_minor6_Content);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }

        Bean_ListView_mine_minor6_notification bean = mBean_listView_mine_minor6_notifications.get(position);
        viewHolder.Icon_iv.setImageResource(bean.getIcon());
        viewHolder.ID_tv.setText(bean.getID());
        viewHolder.Time_tv.setText(bean.getTime());
        viewHolder.Content_tv.setText(bean.getContent());

        return convertView;
    }

    class ViewHolder{
        private ImageView Icon_iv;
        private TextView ID_tv;
        private TextView Time_tv;
        private TextView Content_tv;
    }
}

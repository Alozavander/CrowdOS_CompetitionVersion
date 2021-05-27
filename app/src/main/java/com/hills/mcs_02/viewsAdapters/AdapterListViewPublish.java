package com.hills.mcs_02.viewsAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hills.mcs_02.R;
import com.hills.mcs_02.dataBeans.BeanListViewPublish;

import java.util.List;

public class AdapterListViewPublish extends BaseAdapter {
    private List<BeanListViewPublish> mBeanListViewPublish;
    private LayoutInflater mInflater;


    public AdapterListViewPublish(Context context, List<BeanListViewPublish> beanListViewPublish) {
        this.mBeanListViewPublish = beanListViewPublish;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mBeanListViewPublish.size();
    }

    @Override
    public Object getItem(int position) {
       return mBeanListViewPublish.get(position);
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
            convertView = mInflater.inflate(R.layout.listview_item_pulishpage,null);

            viewHolder.titleTv = (TextView) convertView.findViewById(R.id.pulishpage_modelTitle);
            viewHolder.sensorsTv = (TextView) convertView.findViewById(R.id.publishpage_sensorsUse);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        BeanListViewPublish beanListViewPublish = (BeanListViewPublish) mBeanListViewPublish.get(position);

        viewHolder.titleTv.setText(beanListViewPublish.getTitle());
        viewHolder.sensorsTv.setText(beanListViewPublish.getSensors());

        return convertView;
    }


    /** Inner class */
    class ViewHolder{
        private TextView titleTv;
        private TextView sensorsTv;
    }
}

package com.hills.mcs_02.viewsAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hills.mcs_02.R;
import com.hills.mcs_02.dataBeans.BeanListViewMine;

import java.util.List;


public class AdapterListViewMine extends BaseAdapter {
    private List<BeanListViewMine> mBeanListView;
    private LayoutInflater mInflater;

    public AdapterListViewMine(List<BeanListViewMine> beanListView, Context context) {
        mBeanListView = beanListView;
        mInflater = LayoutInflater.from(context);
    }

    /** Display the number of the data */
    @Override
    public int getCount() {
        return mBeanListView.size();
    }

    /** Index of the corresponding data item */
    @Override
    public Object getItem(int position) {
        return mBeanListView.get(position);
    }

    /** Index of the corresponding data item ID */
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        viewHolder viewHolder;
        /** Judge the view have been initiated or not */
        if (convertView == null) {
            viewHolder = new viewHolder();
            /** We don't nee convert the XML file to View, so the second parmeter is null */
            convertView = mInflater.inflate(R.layout.listview_item_minepage,null);

            viewHolder.iconIv = (ImageView) convertView.findViewById(R.id.homepage_lvItem_icon);
            viewHolder.titleTv = (TextView) convertView.findViewById(R.id.homepage_lvItem_title);

            /** Connect the viewHolder and convertView with setTag */
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (AdapterListViewMine.viewHolder) convertView.getTag();
        }

        BeanListViewMine beanListView = (BeanListViewMine) mBeanListView.get(position);

        viewHolder.iconIv.setImageResource(beanListView.getIcon());
        viewHolder.titleTv.setText(beanListView.getTitle());

        return convertView;
    }


    /** Inner Class */
    class viewHolder {
        private ImageView iconIv;
        private TextView titleTv;
    }

}

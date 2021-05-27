package com.hills.mcs_02.viewsAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hills.mcs_02.R;
import com.hills.mcs_02.dataBeans.BeanListViewMineEditInfo;

import java.util.List;

public class AdapterListViewMineEditInfo extends BaseAdapter {
    private List<BeanListViewMineEditInfo> mBeanListViewMineEditInfos;
    private LayoutInflater mInflater;

    public AdapterListViewMineEditInfo() {
        super();
    }

    public AdapterListViewMineEditInfo(List<BeanListViewMineEditInfo> beanListViewMineEditInfos, Context context) {
        mBeanListViewMineEditInfos = beanListViewMineEditInfos;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mBeanListViewMineEditInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return mBeanListViewMineEditInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null){
            convertView = mInflater.inflate(R.layout.listview_item_minepage_editinfo,null);

            viewHolder = new ViewHolder();
            viewHolder.titleTv = (TextView) convertView.findViewById(R.id.minepage_editInfo_lvItem_title);
            viewHolder.contentTv = (TextView) convertView.findViewById(R.id.minepage_editInfo_lvItem_content);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        BeanListViewMineEditInfo bean = mBeanListViewMineEditInfos.get(position);
        viewHolder.contentTv.setText(bean.getContent());
        viewHolder.titleTv.setText(bean.getTitle());

        return convertView;
    }

    class ViewHolder{
        TextView titleTv;
        TextView contentTv;
    }

    /** Change the data of the listview */
    public void textChange(int position, String text){
        mBeanListViewMineEditInfos.get(position).setContent(text);
        notifyDataSetChanged();
    }
}

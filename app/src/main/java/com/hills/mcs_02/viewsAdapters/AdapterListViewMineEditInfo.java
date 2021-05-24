package com.hills.mcs_02.viewsAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hills.mcs_02.R;
import com.hills.mcs_02.dataBeans.Bean_ListView_mine_editInfo;

import java.util.List;

public class AdapterListViewMineEditInfo extends BaseAdapter {
    private List<Bean_ListView_mine_editInfo> mBeanListViewMineEditInfos;
    private LayoutInflater mInflater;

    public AdapterListViewMineEditInfo() {
        super();
    }

    public AdapterListViewMineEditInfo(List<Bean_ListView_mine_editInfo> beanListViewMineEditInfos, Context context) {
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

        Bean_ListView_mine_editInfo bean = mBeanListViewMineEditInfos.get(position);
        viewHolder.contentTv.setText(bean.getContent());
        viewHolder.titleTv.setText(bean.getTitle());

        return convertView;
    }

    class ViewHolder{
        TextView titleTv;
        TextView contentTv;
    }

    //此方法用于改变列表中的数据
    public void textChange(int position, String text){
        mBeanListViewMineEditInfos.get(position).setContent(text);
        notifyDataSetChanged();
    }
}

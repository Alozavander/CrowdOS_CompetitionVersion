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

public class Adapter_ListView_mine_editInfo extends BaseAdapter {
    private List<Bean_ListView_mine_editInfo> mBean_listView_mine_editInfos;
    private LayoutInflater mInflater;

    public Adapter_ListView_mine_editInfo() {
        super();
    }

    public Adapter_ListView_mine_editInfo(List<Bean_ListView_mine_editInfo> bean_listView_mine_editInfos, Context context) {
        mBean_listView_mine_editInfos = bean_listView_mine_editInfos;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mBean_listView_mine_editInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return mBean_listView_mine_editInfos.get(position);
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
            viewHolder.title_tv = (TextView) convertView.findViewById(R.id.minepage_editInfo_lvItem_title);
            viewHolder.content_tv = (TextView) convertView.findViewById(R.id.minepage_editInfo_lvItem_content);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Bean_ListView_mine_editInfo bean = mBean_listView_mine_editInfos.get(position);
        viewHolder.content_tv.setText(bean.getContent());
        viewHolder.title_tv.setText(bean.getTitle());

        return convertView;
    }

    class ViewHolder{
        TextView title_tv;
        TextView content_tv;
    }

    //此方法用于改变列表中的数据
    public void textChange(int position, String text){
        mBean_listView_mine_editInfos.get(position).setContent(text);
        notifyDataSetChanged();
    }
}

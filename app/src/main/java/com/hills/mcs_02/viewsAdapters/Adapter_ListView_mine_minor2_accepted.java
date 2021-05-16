package com.hills.mcs_02.viewsAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hills.mcs_02.R;
import com.hills.mcs_02.dataBeans.Bean_ListView_mine_minor2_accepted;

import java.util.List;

public class Adapter_ListView_mine_minor2_accepted extends BaseAdapter {
    private List<Bean_ListView_mine_minor2_accepted> mBean_listView_mine_minor2_accepteds;
    private LayoutInflater mInflater;
    

    public Adapter_ListView_mine_minor2_accepted(Context context, List<Bean_ListView_mine_minor2_accepted> list) {
        mInflater = LayoutInflater.from(context);
        mBean_listView_mine_minor2_accepteds = list;
    }

    @Override
    public int getCount() {
        return mBean_listView_mine_minor2_accepteds.size();
    }

    @Override
    public Object getItem(int position) {
        return mBean_listView_mine_minor2_accepteds.get(position);
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

            viewHolder.pic_iv = (ImageView) convertView.findViewById(R.id.minepage_minor2_lvItem_pic);
            viewHolder.taskID_tv = (TextView) convertView.findViewById(R.id.minepage_minor2_lvItem_taskID);
            viewHolder.taskState_tv = (TextView) convertView.findViewById(R.id.minepage_minor2_lvItem_taskState);
            viewHolder.describe_tv = (TextView) convertView.findViewById(R.id.minepage_minor2_lvItem_Describe);
            viewHolder.taskContent_tv = (TextView) convertView.findViewById(R.id.minepage_minor2_lvItem_TaskContent);
            viewHolder.coinsCount_tv = (TextView) convertView.findViewById(R.id.minepage_minor2_lvItem_CoinsCount);
            viewHolder.taskCount_tv = (TextView) convertView.findViewById(R.id.minepage_minor2_lvItem_TaskCount);


            //通过setTag将convertView与viewHolder关联
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Bean_ListView_mine_minor2_accepted bean = mBean_listView_mine_minor2_accepteds.get(position);
        viewHolder.pic_iv.setImageResource(bean.getPic());
        viewHolder.taskID_tv.setText(bean.getTaskID());
        viewHolder.taskState_tv.setText(bean.getTaksState());
        viewHolder.describe_tv.setText(bean.getDescribe());
        viewHolder.taskContent_tv.setText(bean.getTaskContent());
        viewHolder.coinsCount_tv.setText(bean.getCoinsCount());
        viewHolder.taskCount_tv.setText(bean.getTaskCount());

        return convertView;
    }


    class ViewHolder{
        private ImageView pic_iv;
        private TextView taskID_tv;
        private TextView taskState_tv;
        private TextView describe_tv;
        private TextView taskContent_tv;
        private TextView coinsCount_tv;
        private TextView taskCount_tv;

    }
}

package com.hills.mcs_02.viewsAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import com.hills.mcs_02.dataBeans.BeanListViewMine;
import com.hills.mcs_02.R;


public class AdapterListViewMine extends BaseAdapter {
    private List<BeanListViewMine> mBeanListView;
    private LayoutInflater mInflater;//布局装载器对象

    public AdapterListViewMine(List<BeanListViewMine> beanListView, Context context) {
        mBeanListView = beanListView;
        mInflater = LayoutInflater.from(context);
    }

    //显示数据数量
    @Override
    public int getCount() {
        return mBeanListView.size();
    }

    //索引对应的数据项
    @Override
    public Object getItem(int position) {
        return mBeanListView.get(position);
    }

    //索引对应数据项的ID
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        viewHolder viewHolder;
        //如果view未被实例化过，缓存池中没有对应的缓存
        if (convertView == null) {
            viewHolder = new viewHolder();
            // 由于我们只需要将XML转化为View，并不涉及到具体的布局，所以第二个参数通常设置为null
            convertView = mInflater.inflate(R.layout.listview_item_minepage,null);

            viewHolder.iconIv = (ImageView) convertView.findViewById(R.id.homepage_lvItem_icon);
            viewHolder.titleTv = (TextView) convertView.findViewById(R.id.homepage_lvItem_title);

            //通过setTag将convertView与viewHolder关联
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (AdapterListViewMine.viewHolder) convertView.getTag();
        }

        BeanListViewMine beanListView = (BeanListViewMine) mBeanListView.get(position);

        viewHolder.iconIv.setImageResource(beanListView.getIcon());
        viewHolder.titleTv.setText(beanListView.getTitle());

        return convertView;
    }


    //为缓冲机制设立的内部类
    class viewHolder {
        private ImageView iconIv;
        private TextView titleTv;
    }

}

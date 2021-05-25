package com.hills.mcs_02.viewsAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import com.hills.mcs_02.dataBeans.BeanListViewPublish;
import com.hills.mcs_02.R;

public class AdapterListViewPublish extends BaseAdapter {
    private List<BeanListViewPublish> mBeanListViewPublish;
    private LayoutInflater mInflater;//布局装载器对象


    public AdapterListViewPublish(Context context, List<BeanListViewPublish> beanListViewPublish) {
        this.mBeanListViewPublish = beanListViewPublish;
        this.mInflater = LayoutInflater.from(context);
    }

    //显示数据数量
    @Override
    public int getCount() {
        return mBeanListViewPublish.size();
    }

    //索引对应的数据项
    @Override
    public Object getItem(int position) {
       return mBeanListViewPublish.get(position);
    }

    //索引对应数据项的ID
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
            convertView = mInflater.inflate(R.layout.listview_item_pulishpage,null);

            viewHolder.titleTv = (TextView) convertView.findViewById(R.id.pulishpage_modelTitle);
            viewHolder.sensorsTv = (TextView) convertView.findViewById(R.id.publishpage_sensorsUse);

            //通过setTag将convertView与viewHolder关联
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        BeanListViewPublish beanListViewPublish = (BeanListViewPublish) mBeanListViewPublish.get(position);

        viewHolder.titleTv.setText(beanListViewPublish.getTitle());
        viewHolder.sensorsTv.setText(beanListViewPublish.getSensors());

        return convertView;
    }


    //为缓冲机制设立的内部类
    class ViewHolder{
        private TextView titleTv;
        private TextView sensorsTv;
    }
}

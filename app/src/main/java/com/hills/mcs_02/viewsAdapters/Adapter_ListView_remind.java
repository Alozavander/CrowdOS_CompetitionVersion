package com.hills.mcs_02.viewsAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hills.mcs_02.R;
import com.hills.mcs_02.dataBeans.Bean_ListView_remind;
import com.hills.mcs_02.dataBeans.Task;
import com.hills.mcs_02.fragmentsPack.MCSRecyclerItemClickListener;

import java.util.List;

public class Adapter_ListView_remind extends BaseAdapter {
    private List<Bean_ListView_remind> mBean_listView_remindList;
    private LayoutInflater mInflater;
    private MCSRecyclerItemClickListener mListener;

    public Adapter_ListView_remind() {
        super();
    }

    public Adapter_ListView_remind( Context context,List<Bean_ListView_remind> bean_listView_remindList) {
        mBean_listView_remindList = bean_listView_remindList;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mBean_listView_remindList.size();
    }

    @Override
    public Object getItem(int position) {
        return mBean_listView_remindList.get(position);
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
            convertView = mInflater.inflate(R.layout.listview_item_remindpage,null);

            viewHolder.userIcon_iv = (ImageView) convertView.findViewById(R.id.remindpage_tasklv_userIcon);
            viewHolder.picture_iv = (ImageView) convertView.findViewById(R.id.remindpage_tasklv_pic);
            viewHolder.userID_tv = (TextView) convertView.findViewById(R.id.remindpage_tasklv_userID);
            viewHolder.leftTime_tv = (TextView)convertView.findViewById(R.id.remindpage_tasklv_deadline);
            viewHolder.describe_tv = (TextView)convertView.findViewById(R.id.remindpage_tasklv_Describe);
            viewHolder.taskContent_tv = (TextView)convertView.findViewById(R.id.remindpage_tasklv_TaskContent);
            viewHolder.coinsCount_tv = (TextView)convertView.findViewById(R.id.remindpage_tasklv_CoinsCount);
            viewHolder.taskCount_tv = (TextView)convertView.findViewById(R.id.remindpage_tasklv_TaskCount);

            //通过setTag将convertView与viewHolder关联
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Bean_ListView_remind bean_listView_remind = (Bean_ListView_remind) mBean_listView_remindList.get(position);

        viewHolder.userIcon_iv.setImageResource(bean_listView_remind.getUserIcon());
        viewHolder.picture_iv.setImageResource(bean_listView_remind.getPicture());

        Task task = bean_listView_remind.getTask();
        viewHolder.userID_tv.setText(task.getUserName());
        viewHolder.leftTime_tv.setText(bean_listView_remind.getDeadline());
        viewHolder.describe_tv.setText(bean_listView_remind.getKind());
        viewHolder.taskContent_tv.setText(task.getDescribe_task().substring(0,19) + "...");
        viewHolder.coinsCount_tv.setText(task.getCoin() + "");
        viewHolder.taskCount_tv.setText(task.getTotalNum() + "");


        return convertView;
    }

    class ViewHolder{
        private ImageView userIcon_iv;
        private ImageView picture_iv;
        private TextView userID_tv;
        private TextView leftTime_tv;
        private TextView describe_tv;
        private TextView taskContent_tv;
        private TextView coinsCount_tv;
        private TextView taskCount_tv;
    }
}

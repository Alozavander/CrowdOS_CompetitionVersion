package com.hills.mcs_02.viewsAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hills.mcs_02.dataBeans.Bean_ListView_remind;
import com.hills.mcs_02.dataBeans.Task;
import com.hills.mcs_02.fragmentsPack.MCSRecyclerItemClickListener;
import com.hills.mcs_02.R;

import java.util.List;

public class AdapterListViewRemind extends BaseAdapter {
    private List<Bean_ListView_remind> mBeanListViewRemindList;
    private LayoutInflater mInflater;
    private MCSRecyclerItemClickListener mListener;

    public AdapterListViewRemind() {
        super();
    }

    public AdapterListViewRemind( Context context,List<Bean_ListView_remind> beanListViewRemindList) {
        mBeanListViewRemindList = beanListViewRemindList;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mBeanListViewRemindList.size();
    }

    @Override
    public Object getItem(int position) {
        return mBeanListViewRemindList.get(position);
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

            viewHolder.userIconIv = (ImageView) convertView.findViewById(R.id.remindpage_tasklv_userIcon);
            viewHolder.pictureIv = (ImageView) convertView.findViewById(R.id.remindpage_tasklv_pic);
            viewHolder.userIdTv = (TextView) convertView.findViewById(R.id.remindpage_tasklv_userID);
            viewHolder.leftTimeTv = (TextView)convertView.findViewById(R.id.remindpage_tasklv_deadline);
            viewHolder.describeTv = (TextView)convertView.findViewById(R.id.remindpage_tasklv_Describe);
            viewHolder.taskContentTv = (TextView)convertView.findViewById(R.id.remindpage_tasklv_TaskContent);
            viewHolder.coinsCountTv = (TextView)convertView.findViewById(R.id.remindpage_tasklv_CoinsCount);
            viewHolder.taskCountTv = (TextView)convertView.findViewById(R.id.remindpage_tasklv_TaskCount);

            //通过setTag将convertView与viewHolder关联
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Bean_ListView_remind beanListViewRemind = (Bean_ListView_remind) mBeanListViewRemindList.get(position);

        viewHolder.userIconIv.setImageResource(beanListViewRemind.getUserIcon());
        viewHolder.pictureIv.setImageResource(beanListViewRemind.getPicture());

        Task task = beanListViewRemind.getTask();
        viewHolder.userIdTv.setText(task.getUserName());
        viewHolder.leftTimeTv.setText(beanListViewRemind.getDeadline());
        viewHolder.describeTv.setText(beanListViewRemind.getKind());
        viewHolder.taskContentTv.setText(task.getDescribe_task().substring(0,19) + "...");
        viewHolder.coinsCountTv.setText(task.getCoin() + "");
        viewHolder.taskCountTv.setText(task.getTotalNum() + "");


        return convertView;
    }

    class ViewHolder{
        private ImageView userIconIv;
        private ImageView pictureIv;
        private TextView userIdTv;
        private TextView leftTimeTv;
        private TextView describeTv;
        private TextView taskContentTv;
        private TextView coinsCountTv;
        private TextView taskCountTv;
    }
}

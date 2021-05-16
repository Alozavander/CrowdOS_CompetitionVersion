package com.hills.mcs_02.viewsAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hills.mcs_02.R;
import com.hills.mcs_02.dataBeans.Bean_ListView_mine_minor4_wallet;

import java.util.List;

public class Adapter_ListView_mine_minor4_wallet extends BaseAdapter {
    private List<Bean_ListView_mine_minor4_wallet> mBean_listView_mine_minor4_wallets;
    private LayoutInflater mInflater;


    public Adapter_ListView_mine_minor4_wallet() {
        super();
    }

    public Adapter_ListView_mine_minor4_wallet(List<Bean_ListView_mine_minor4_wallet> bean_listView_mine_minor4_wallets, Context context) {
        mBean_listView_mine_minor4_wallets = bean_listView_mine_minor4_wallets;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mBean_listView_mine_minor4_wallets.size();
    }

    @Override
    public Object getItem(int position) {
        return mBean_listView_mine_minor4_wallets.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.listview_item_minepage_minor4,null);

            viewHolder.userIcon_iv = (ImageView)convertView.findViewById(R.id.minepage_minor4_walletlv_userIcon);
            viewHolder.title_tv = (TextView)convertView.findViewById(R.id.minepage_minor4_walletlv_title);
            viewHolder.mount_tv = (TextView)convertView.findViewById(R.id.minepage_minor4_walletlv_mount);
            viewHolder.time_tv = (TextView)convertView.findViewById(R.id.minepage_minor4_walletlv_time);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Bean_ListView_mine_minor4_wallet bean = mBean_listView_mine_minor4_wallets.get(position);
        viewHolder.userIcon_iv.setImageResource(bean.getUserIcon());
        viewHolder.title_tv.setText(bean.getTitle());
        viewHolder.time_tv.setText(bean.getTime());
        viewHolder.mount_tv.setText(bean.getMount());

        return convertView;
    }

    class ViewHolder{
        private ImageView userIcon_iv;
        private TextView title_tv;
        private TextView mount_tv;
        private TextView time_tv;
    }
}

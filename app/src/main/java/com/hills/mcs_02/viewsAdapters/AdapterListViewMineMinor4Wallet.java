package com.hills.mcs_02.viewsAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hills.mcs_02.R;
import com.hills.mcs_02.dataBeans.BeanListViewMineMinor4Wallet;

import java.util.List;


public class AdapterListViewMineMinor4Wallet extends BaseAdapter {
    private List<BeanListViewMineMinor4Wallet> mBeanListViewMineMinor4Wallet;
    private LayoutInflater mInflater;

    public AdapterListViewMineMinor4Wallet() {
        super();
    }

    public AdapterListViewMineMinor4Wallet(List<BeanListViewMineMinor4Wallet> bean_listView_mine_minor4_wallets, Context context) {
        mBeanListViewMineMinor4Wallet = bean_listView_mine_minor4_wallets;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mBeanListViewMineMinor4Wallet.size();
    }

    @Override
    public Object getItem(int position) {
        return mBeanListViewMineMinor4Wallet.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        viewHolder viewHolder;
        if(convertView == null){
            viewHolder = new viewHolder();
            convertView = mInflater.inflate(R.layout.listview_item_minepage_minor4,null);

            viewHolder.userIconIv = (ImageView)convertView.findViewById(R.id.minepage_minor4_walletlv_userIcon);
            viewHolder.titleTv = (TextView)convertView.findViewById(R.id.minepage_minor4_walletlv_title);
            viewHolder.mountTv = (TextView)convertView.findViewById(R.id.minepage_minor4_walletlv_mount);
            viewHolder.timeTv = (TextView)convertView.findViewById(R.id.minepage_minor4_walletlv_time);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (AdapterListViewMineMinor4Wallet.viewHolder) convertView.getTag();
        }

        BeanListViewMineMinor4Wallet bean = mBeanListViewMineMinor4Wallet.get(position);
        viewHolder.userIconIv.setImageResource(bean.getUserIcon());
        viewHolder.titleTv.setText(bean.getTitle());
        viewHolder.timeTv.setText(bean.getTime());
        viewHolder.mountTv.setText(bean.getMount());

        return convertView;
    }

    class viewHolder {
        private ImageView userIconIv;
        private TextView titleTv;
        private TextView mountTv;
        private TextView timeTv;
    }
}

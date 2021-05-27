package com.hills.mcs_02.viewsAdapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.hills.mcs_02.R;
import com.hills.mcs_02.dataBeans.BeanRecyclerViewMineMinor4Wallet;
import com.hills.mcs_02.viewsAdapters.AdapterRecyclerViewMineMinor4Wallet.viewHolder;

import java.util.List;

public class AdapterRecyclerViewMineMinor4Wallet extends RecyclerView.Adapter<viewHolder> {

    private List<BeanRecyclerViewMineMinor4Wallet> mBeanRecyclerViewMineMinor4WalletList;

    static class viewHolder extends RecyclerView.ViewHolder{
        ImageView userCoinImage;
        TextView username;
        TextView userCoin;

        public viewHolder(View view){
            super(view);
            userCoinImage = (ImageView) view.findViewById(R.id.user_coins_image);
            username = (TextView) view.findViewById(R.id.user_coins_userName);
            userCoin = (TextView) view.findViewById(R.id.user_coins_userCoins);
        }
    }

    public AdapterRecyclerViewMineMinor4Wallet(List<BeanRecyclerViewMineMinor4Wallet> beanRecyclerViewmineminor4wallet_list){
        mBeanRecyclerViewMineMinor4WalletList = beanRecyclerViewmineminor4wallet_list;
    }

    @Override
    public viewHolder onCreateViewHolder( ViewGroup viewGroup, int temp) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recyclerview_item_user_coins,viewGroup,false);
        viewHolder holder = new viewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(viewHolder holder, int position) {
        BeanRecyclerViewMineMinor4Wallet user_coins = mBeanRecyclerViewMineMinor4WalletList.get(position);
        holder.userCoinImage.setImageResource(user_coins.getUserIcon());
        holder.username.setText(user_coins.getUsername());
        holder.userCoin.setText(Integer.toString(user_coins.getUserCoin()));
    }

    @Override
    public int getItemCount() {
        return mBeanRecyclerViewMineMinor4WalletList.size();
    }
}

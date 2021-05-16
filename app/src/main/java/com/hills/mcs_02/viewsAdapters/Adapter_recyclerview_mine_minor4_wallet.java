package com.hills.mcs_02.viewsAdapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.hills.mcs_02.R;
import com.hills.mcs_02.dataBeans.Bean_RecyclerView_mine_minor4_wallet;

import java.util.List;

public class Adapter_recyclerview_mine_minor4_wallet extends RecyclerView.Adapter<Adapter_recyclerview_mine_minor4_wallet.ViewHolder> {

    private List<Bean_RecyclerView_mine_minor4_wallet> mBeanRecyclerViewmineminor4wallet_list;

    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView user_coins_image;
        TextView userName;
        TextView userCoins;

        public ViewHolder(View view){
            super(view);
            user_coins_image = (ImageView) view.findViewById(R.id.user_coins_image);
            userName = (TextView) view.findViewById(R.id.user_coins_userName);
            userCoins = (TextView) view.findViewById(R.id.user_coins_userCoins);
        }
    }

    public Adapter_recyclerview_mine_minor4_wallet(List<Bean_RecyclerView_mine_minor4_wallet> beanRecyclerViewmineminor4wallet_list){
        mBeanRecyclerViewmineminor4wallet_list = beanRecyclerViewmineminor4wallet_list;
    }


    @Override
    public ViewHolder onCreateViewHolder( ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recyclerview_item_user_coins,viewGroup,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Bean_RecyclerView_mine_minor4_wallet user_coins = mBeanRecyclerViewmineminor4wallet_list.get(position);
        holder.user_coins_image.setImageResource(user_coins.getUserIcon());
        holder.userName.setText(user_coins.getUserName());
        holder.userCoins.setText(Integer.toString(user_coins.getUserCoins()));
    }

    @Override
    public int getItemCount() {
        return mBeanRecyclerViewmineminor4wallet_list.size();
    }
}

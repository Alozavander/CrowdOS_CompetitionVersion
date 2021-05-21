package com.hills.mcs_02.activities;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.hills.mcs_02.BaseActivity;
import com.hills.mcs_02.dataBeans.Bean_RecyclerView_mine_minor4_wallet;
import com.hills.mcs_02.dataBeans.User;
import com.hills.mcs_02.networkClasses.interfacesPack.GetRequestUserCoinsRankList;
import com.hills.mcs_02.networkClasses.interfacesPack.PostRequestUserCoins;
import com.hills.mcs_02.R;
import com.hills.mcs_02.viewsAdapters.Adapter_recyclerview_mine_minor4_wallet;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;



public class ActivityMineMinor4Wallet extends BaseActivity {

    private List<Bean_RecyclerView_mine_minor4_wallet> userCoinsList = new ArrayList<>();
    private Context mContext;
    private RecyclerView mRecyclerView;
    private String TAG = "myWallet";
    private User user;
    private List<User> mRequestUserCoinsRankList;
    private TextView coinsNum;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_minor4_mywallet);
        mContext = ActivityMineMinor4Wallet.this;
        user = new User();
        mRequestUserCoinsRankList = new ArrayList<User>();

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_coins_ranking);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        coinsNum = (TextView) findViewById(R.id.minepage_minor4_mountNumber);

        getMyCoinsRequest();
        getUserCoinsRankRequest();

    }

    public void getMyCoinsRequest() {

        String userID = getSharedPreferences("user", Context.MODE_PRIVATE).getString("userID", "");

        //创建Retrofit对象
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(this.getString(R.string.base_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        //创建网络接口实例
        PostRequestUserCoins requestUserCoins = retrofit.create(PostRequestUserCoins.class);
        //包装发送请求
        Call<ResponseBody> call = requestUserCoins.userCoins(Integer.parseInt(userID));

        //异步网络请求
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.code() == 200){
                    Gson gson = new GsonBuilder().setDateFormat("yyyy.MM.dd").create();
                    try{
                        //在此附近应该加入内容判定，优化响应逻辑
                        String temp = response.body().string();
                        Log.i(TAG, temp);
                        user = gson.fromJson(temp, User.class);
                        coinsNum.setText(user.getCoins() + "");
                        Log.i("Test","UserInfo:" + user.toString());
                        //成功获取网络请求内容后，调用内容处理方法
                    }catch (IOException exp) {
                        exp.printStackTrace();
                    }
                }else{
                    Toast.makeText(ActivityMineMinor4Wallet.this, "UserInfo:" + user.toString(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
            }
        });
    }

    //获取积分排名列表
    private void getUserCoinsRankRequest(){
        //创建Retrofit对象
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(this.getString(R.string.base_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        //创建网络接口实例
        GetRequestUserCoinsRankList requestUserCoinsRankList = retrofit.create(
            GetRequestUserCoinsRankList.class);
        //包装发送请求
        Call<ResponseBody> call = requestUserCoinsRankList.getCall();

        //异步网络请求
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200){
                    Gson gson = new GsonBuilder().setDateFormat("yyyy.MM.dd").create();
                    Type type = new TypeToken<List<User>>(){}.getType();
                    try {
                        String temp = response.body().string();
                        mRequestUserCoinsRankList = gson.fromJson(temp, type);
                        if (mRequestUserCoinsRankList.size() > 0) {
                            for (User user : mRequestUserCoinsRankList) {
                                Bean_RecyclerView_mine_minor4_wallet rank_item = new Bean_RecyclerView_mine_minor4_wallet();
                                rank_item.setUserCoins(user.getCoins());
                                rank_item.setUserIcon(R.drawable.haimian_usericon);
                                rank_item.setUserId(user.getUserId());
                                rank_item.setUserName(user.getUserName());
                                userCoinsList.add(rank_item);
                            }
                        } else {
                            Toast.makeText(mContext, getResources().getString(R.string.FailToGetData) + mRequestUserCoinsRankList.size(), Toast.LENGTH_SHORT).show();
                        }
                        Adapter_recyclerview_mine_minor4_wallet adapter = new Adapter_recyclerview_mine_minor4_wallet(userCoinsList);
                        mRecyclerView.setAdapter(adapter);
                    }catch (IOException exp){
                        exp.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {

            }
        });
    }

}

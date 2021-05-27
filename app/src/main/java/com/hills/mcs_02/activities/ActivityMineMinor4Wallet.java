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

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.hills.mcs_02.BaseActivity;
import com.hills.mcs_02.dataBeans.BeanRecyclerViewMineMinor4Wallet;
import com.hills.mcs_02.dataBeans.User;
import com.hills.mcs_02.networkClasses.interfacesPack.GetRequestUserCoinsRankList;
import com.hills.mcs_02.networkClasses.interfacesPack.PostRequestUserCoin;
import com.hills.mcs_02.R;
import com.hills.mcs_02.viewsAdapters.AdapterRecyclerViewMineMinor4Wallet;

public class ActivityMineMinor4Wallet extends BaseActivity {

    private List<BeanRecyclerViewMineMinor4Wallet> userCoinsList = new ArrayList<>();
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

        String userId = getSharedPreferences("user", Context.MODE_PRIVATE).getString("userID", "");

        /** Create a Retrofit object */
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(this.getString(R.string.base_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        /** Create a network interface instance  */
        PostRequestUserCoin requestUserCoins = retrofit.create(PostRequestUserCoin.class);
        /** Wrap the send request */
        Call<ResponseBody> call = requestUserCoins.userCoins(Integer.parseInt(userId));
        /** Asynchronous network requests*/
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.code() == 200){
                    Gson gson = new GsonBuilder().setDateFormat("yyyy.MM.dd").create();
                    try{
                        String temp = response.body().string();
                        Log.i(TAG, temp);
                        user = gson.fromJson(temp, User.class);
                        coinsNum.setText(user.getCoin() + "");
                        Log.i("Test","UserInfo:" + user.toString());
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

    /** Get a ranking list of points */
    private void getUserCoinsRankRequest(){
        /** Create a Retrofit object */
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(this.getString(R.string.base_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        /** Create a network interface instance  */
        GetRequestUserCoinsRankList requestUserCoinsRankList = retrofit.create(
            GetRequestUserCoinsRankList.class);
        /** Wrap the send request */
        Call<ResponseBody> call = requestUserCoinsRankList.getCall();

        /** Asynchronous network requests*/
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
                                BeanRecyclerViewMineMinor4Wallet rankItem = new BeanRecyclerViewMineMinor4Wallet();
                                rankItem.setUserCoin(user.getCoin());
                                rankItem.setUserIcon(R.drawable.haimian_usericon);
                                rankItem.setUserId(user.getUserId());
                                rankItem.setUsername(user.getUsername());
                                userCoinsList.add(rankItem);
                            }
                        } else {
                            Toast.makeText(mContext, getResources().getString(R.string.FailToGetData) + mRequestUserCoinsRankList.size(), Toast.LENGTH_SHORT).show();
                        }
                        AdapterRecyclerViewMineMinor4Wallet adapter = new AdapterRecyclerViewMineMinor4Wallet(userCoinsList);
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

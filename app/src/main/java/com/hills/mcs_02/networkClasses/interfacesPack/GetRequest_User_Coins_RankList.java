package com.hills.mcs_02.networkClasses.interfacesPack;


import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;

//本接口是为了获取用户积分排名
public interface GetRequest_User_Coins_RankList {
    //Retrofit 网络请求接口，GET里的为服务器的URL
    @GET("/user/getUserRank") Call<ResponseBody> getCall();
}

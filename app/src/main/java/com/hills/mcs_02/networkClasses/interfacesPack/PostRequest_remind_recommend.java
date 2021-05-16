package com.hills.mcs_02.networkClasses.interfacesPack;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface PostRequest_remind_recommend {
    @Headers({"Content-Type: application/json","Accept: application/json"})//需要添加头
    @POST("/task/getUserAllRecommendFromUT/{userId}")                                                //Post目标地址
    Call<ResponseBody> query_recommend(@Path("userId") Integer userId);
}

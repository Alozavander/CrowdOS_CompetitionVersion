package com.hills.mcs_02.networkClasses.interfacesPack;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface PostRequest_mine_minor7_update {
    @Headers({"Content-Type: application/json","Accept: application/json"})//需要添加头
    @POST("/version_updating/checkVersion/{versionCode}")                  //Post目标地址
    Call<ResponseBody> query_published(@Path("versionCode") int versionCode);
}



package com.hills.mcs_02.networkClasses.interfacesPack;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface PostRequest_user_task_add {
    //Retrofit 网络请求接口
    @Headers({"Content-Type: application/json","Accept: application/json"})//需要添加头
    @POST("/user_task/acceptUser_Task")
    Call<ResponseBody> add_user_task(@Body RequestBody user_task);
}

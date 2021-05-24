package com.hills.mcs_02.networkClasses.interfacesPack;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface PostRequestEmailRegisterAddressCheck {

    /*@POST("user")
    Call<Bean_UserAccount> getU(@Header("Authorization") String auth_String);*/

    @Headers({"Content-Type: application/json","Accept: application/json"})//需要添加头
    @POST("user/addUser")                                              //Post目标地址
    Call<ResponseBody> userRegister(@Body RequestBody userInfo);
}

package com.hills.mcs_02.networkClasses.interfacesPack;

import retrofit2.Call;
import retrofit2.http.GET;

import com.hills.mcs_02.dataBeans.Task;

public interface GetRequestHomeTaskDetail {
    //Retrofit 网络请求接口，GET里的为服务器的URL
    @GET("xxxxxx")
    Call<Task> getCall();
}

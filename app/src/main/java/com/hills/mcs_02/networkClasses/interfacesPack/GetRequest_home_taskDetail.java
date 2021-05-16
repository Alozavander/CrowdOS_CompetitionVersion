package com.hills.mcs_02.networkClasses.interfacesPack;

import com.hills.mcs_02.dataBeans.Task;

import retrofit2.Call;
import retrofit2.http.GET;

public interface GetRequest_home_taskDetail {
    //Retrofit 网络请求接口，GET里的为服务器的URL
    @GET("xxxxxx")
    Call<Task> getCall();
}

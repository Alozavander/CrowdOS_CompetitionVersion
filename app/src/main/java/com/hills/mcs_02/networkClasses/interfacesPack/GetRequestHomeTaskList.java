package com.hills.mcs_02.networkClasses.interfacesPack;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;

//本接口是为了首页获取任务列表
public interface GetRequestHomeTaskList {
    //Retrofit 网络请求接口，GET里的为服务器的URL
    //@GET("xxxxxx") Call<List<Bean_ListView_home>> getCall();
    @GET("task/getTen") Call<ResponseBody> getCall();
}

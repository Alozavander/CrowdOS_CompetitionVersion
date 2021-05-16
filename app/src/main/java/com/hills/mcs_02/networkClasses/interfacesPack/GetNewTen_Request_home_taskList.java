package com.hills.mcs_02.networkClasses.interfacesPack;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Path;

//本接口是为了首页获取任务列表
public interface GetNewTen_Request_home_taskList {
    //Retrofit 网络请求接口，GET里的为服务器的URL
    //@GET("xxxxxx") Call<List<Bean_ListView_home>> getCall();
    @POST("/task/getNewTen/{mintaskId}")                                                //Post目标地址
    Call<ResponseBody> query_NewTenTask(@Path("mintaskId") Integer userId);
}

package com.hills.mcs_02.networkClasses.interfacesPack;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;

//本接口是为了地图获取任务的经纬度
public interface GetRequest_map_taskLoc{
	//Retrofit网络请求接口，GET里面为服务器的URL
	@GET("task/getLoc") Call<ResponseBody> getCall();
}
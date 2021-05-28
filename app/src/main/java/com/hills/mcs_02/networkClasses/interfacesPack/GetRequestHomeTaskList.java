package com.hills.mcs_02.networkClasses.interfacesPack;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;

/** This interface is for the first page to get a list of tasks */
public interface GetRequestHomeTaskList {
    @GET("task/getTen") Call<ResponseBody> getCall();
}

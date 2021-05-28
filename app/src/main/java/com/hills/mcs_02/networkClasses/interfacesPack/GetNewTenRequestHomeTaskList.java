package com.hills.mcs_02.networkClasses.interfacesPack;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Path;
import retrofit2.http.POST;

/** This interface is for the first page to get a list of tasks */
public interface GetNewTenRequestHomeTaskList {
    @POST("/task/getNewTen/{mintaskId}")
    Call<ResponseBody> queryNewTenTask(@Path("mintaskId") Integer userId);
}

package com.hills.mcs_02.networkClasses.interfacesPack;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface PostRequestPublishedTaskDetail {
    //Retrofit 网络请求接口
    @Headers({"Content-Type: application/json","Accept: application/json"})//需要添加头
    @POST("/user_task/getTaskIdToUserName/{taskId}")
    //checkCombine_U_UserTask
    Call<ResponseBody> checkUserTaskWithUsername(@Path("taskId") Integer taskId);
}

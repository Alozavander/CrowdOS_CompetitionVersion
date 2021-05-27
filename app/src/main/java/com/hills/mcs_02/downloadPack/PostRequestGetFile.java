package com.hills.mcs_02.downloadPack;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Streaming;

//本接口是为了首页获取任务列表
public interface PostRequestGetFile {
    @Streaming
    @POST("/version_updating/downVersionFromServer/{image}")
    Call<ResponseBody> getFile(@Path("image") String image);
}

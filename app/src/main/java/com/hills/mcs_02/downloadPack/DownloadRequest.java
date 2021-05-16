package com.hills.mcs_02.downloadPack;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Streaming;

public interface DownloadRequest {
    /**
     * @param fileUrl
     * @return
     */
    @Streaming //大文件时要加不然会OOM
    @POST("/user_task/downImageFromImage/{image}")
    Call<ResponseBody> downloadFile(@Path("image") String fileUrl);



}

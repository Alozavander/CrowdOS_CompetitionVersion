package com.hills.mcs_02.networkClasses.interfacesPack;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface PostRequestTaskSubmitFamiliarFiles {
   /*
    @Multipart
    @POST("XXXX")
    Call<ResponseBody> task_Submission(@Part("subText") String subText, @Part List<MultipartBody.Part> picList);*/

    @Multipart
    @POST("/sensor/uploadSensorFiles")                                                //Post目标地址
    Call<ResponseBody> taskSubmit(@Part("familiar_sensor") RequestBody familiarSensorInfo, @Part MultipartBody.Part file);
}

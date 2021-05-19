package com.hills.mcs_02.networkClasses.interfacesPack;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface PostRequest_SensorDetail_UploadService {
    @Headers({"Content-Type: application/json","Accept: application/json"})     //添加头
    @POST("/uploadSensorFileMessageDetail")                                                            //Post目标地址
    Call<ResponseBody> uploadSensorMessage(@Part("sensor_detail") RequestBody sensor_detail, @Part MultipartBody.Part file);
}

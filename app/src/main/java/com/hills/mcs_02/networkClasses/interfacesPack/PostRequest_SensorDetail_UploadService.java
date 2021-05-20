package com.hills.mcs_02.networkClasses.interfacesPack;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface PostRequest_SensorDetail_UploadService {
    @Multipart
    @POST("/uploadSensorFileMessageDetail")                                                            //Post目标地址
    Call<ResponseBody> uploadSensorMessage(@Part("sensor_detail") RequestBody sensor_detail, @Part MultipartBody.Part file);
}

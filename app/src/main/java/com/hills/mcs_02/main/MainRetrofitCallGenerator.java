package com.hills.mcs_02.main;

import com.google.gson.Gson;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.content.Context;
import android.util.Log;

import com.hills.mcs_02.BuildConfig;
import com.hills.mcs_02.dataBeans.Liveness;
import com.hills.mcs_02.networkClasses.interfacesPack.PostRequestLivenessLogin;
import com.hills.mcs_02.networkClasses.interfacesPack.PostRequestMineMinor7Update;


public class MainRetrofitCallGenerator {
    public static Call<ResponseBody> getLivenessCall(Context pContext,int userId,String url){
        Liveness lLiveness = new Liveness(null, userId, null, null, null, null, null, null, null, null);
        Gson lGson = new Gson();
        String content = lGson.toJson(lLiveness);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create()).build();
        PostRequestLivenessLogin lLivenessLogin = retrofit.create(PostRequestLivenessLogin.class);
        RequestBody contentBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), content);
        Call<ResponseBody> call = lLivenessLogin.livenessLogin(contentBody);
        return call;
    }

    public static Call<ResponseBody> getCheckVersionCall(Context pContext,String url){
        Retrofit retrofit = new Retrofit.Builder().baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create()).build();
        PostRequestMineMinor7Update request = retrofit.create(PostRequestMineMinor7Update.class);
        int versionCode = BuildConfig.VERSION_CODE;
        Log.i("MainRetrofitCallGenerator", "VersionCode:" + versionCode);
        Call<ResponseBody> call = request.queryPublished(versionCode);
        return call;
    }
}

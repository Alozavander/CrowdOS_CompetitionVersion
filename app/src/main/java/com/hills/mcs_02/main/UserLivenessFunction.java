package com.hills.mcs_02.main;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.hills.mcs_02.dataBeans.Liveness;
import com.hills.mcs_02.networkClasses.interfacesPack.PostRequest_LivenessLogout;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserLivenessFunction {
    private Context mContext;

    public UserLivenessFunction(Context pContext) {
        mContext = pContext;
    }

    //服务器活跃度检测功能向方法
    public void userLogout(int userID,String url) {
        //检查是否登录
        if (userID != -1) {
            Liveness lLiveness = new Liveness(null, userID, null, null, null, null, null, null, null, null);
            Gson lGson = new Gson();
            String content = lGson.toJson(lLiveness);
            //创建Retrofit实例
            Retrofit retrofit = new Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create()).build();
            PostRequest_LivenessLogout lLivenessLogout = retrofit.create(PostRequest_LivenessLogout.class);
            RequestBody contentBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), content);
            Call<ResponseBody> call = lLivenessLogout.livenessLogout(contentBody);
            Log.i("UserLivenessFunction", content);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.code() == 200) {
                        Log.i("UserLivenessFunction", "LivenessLogout success.");
                    }
                }
                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                }
            });
        }
    }
}

package com.hills.mcs_02.main;

import com.google.gson.Gson;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

import android.content.Context;
import android.util.Log;

import com.hills.mcs_02.dataBeans.Liveness;
import com.hills.mcs_02.networkClasses.interfacesPack.PostRequestLivenessLogout;

public class UserLivenessFunction {
    private Context mContext;

    public UserLivenessFunction(Context pContext) {
        mContext = pContext;
    }

    /** server activity detection function direction method */
    public void userLogout(int userId,String url) {
      /**  Check if you are logged in */
        if (userId != -1) {
            Liveness lLiveness = new Liveness(null, userId, null, null, null, null, null, null, null, null);
            Gson lGson = new Gson();
            String content = lGson.toJson(lLiveness);
            Retrofit retrofit = new Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create()).build();
            PostRequestLivenessLogout lLivenessLogout = retrofit.create(PostRequestLivenessLogout.class);
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
                public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                }
            });
        }
    }
}

package com.hills.mcs_02.activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hills.mcs_02.BaseActivity;
import com.hills.mcs_02.R;
import com.hills.mcs_02.account.RegexVerify;
import com.hills.mcs_02.dataBeans.Bean_UserAccount;
import com.hills.mcs_02.dataBeans.User;
import com.hills.mcs_02.networkClasses.interfacesPack.PostRequest_userRegiste;

import java.io.IOException;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Activity_registe extends BaseActivity implements View.OnClickListener{
    private EditText registe_username_et;
    private EditText registe_pwd_et;
    private EditText registe_pwd_confirm_et;
    private ImageView back_iv;
    private Button register_bt;
    private Toast mToast;
    private Bean_UserAccount userAccount;
    private String TAG = "LoginPage";
    private RegexVerify regexVerify;
    private TextView username_error_tv;
    private TextView pwd_error_tv;
    private boolean[] verfy_booleans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registe);

        initAll();

       //

        //Toast.makeText(Activity_2rdPage.this,"pageTag: " + pageTag + ", position: " + position , Toast.LENGTH_SHORT).show();


    }

    private void initAll(){

        //初始化当前页面的回退按钮，这里用了两种不同的方法绑定点击事件
        back_iv = (ImageView)findViewById(R.id.activity_registe_back);
        back_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //初始化登陆界面的用户名和密码的EditText
        registe_username_et = (EditText)findViewById(R.id.activity_registe_username);
        registe_pwd_et = (EditText)findViewById(R.id.activity_registe_password);
        registe_pwd_confirm_et = findViewById(R.id.activity_registe_password_confirm);
        username_error_tv = findViewById(R.id.activity_registe_username_error_tv);
        pwd_error_tv = findViewById(R.id.activity_registe_pwd_error_tv);
        regexVerify = new RegexVerify();
        verfy_booleans = new boolean[]{false, false, false};

        //监听账户是否设置为正确格式
        registe_username_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(! regexVerify.registerUsernameVerfy(s.toString())){
                    username_error_tv.setVisibility(View.VISIBLE);
                    username_error_tv.setText(getResources().getString(R.string.format_reminder_name));
                    verfy_booleans[0] = false;
                }else{
                    username_error_tv.setVisibility(View.INVISIBLE);
                    verfy_booleans[0] = true;
                }
                checkEnbleRegiste();
            }
        });

        //监听密码是否设置为正确格式
        registe_pwd_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(! regexVerify.registerUsernameVerfy(s.toString())){
                    pwd_error_tv.setVisibility(View.VISIBLE);
                    pwd_error_tv.setText(getResources().getString(R.string.format_reminder_name));
                    verfy_booleans[1] = false;
                }else{
                    pwd_error_tv.setVisibility(View.INVISIBLE);
                    verfy_booleans[1] = true;
                }
                checkEnbleRegiste();
            }
        });

        //监听是否两次输入的密码相等
        registe_pwd_confirm_et.addTextChangedListener(new TextWatcher() {
            String first_pwd;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                first_pwd = registe_pwd_et.getText().toString();
                TextView error_tv = findViewById(R.id.activity_registe_password_confirm_error_tv);
                if(! s.toString().equals(first_pwd)) {
                    error_tv.setText(getResources().getString(R.string.differentPassword));
                    error_tv.setVisibility(View.VISIBLE);
                    verfy_booleans[2] = false;
                }else{
                    error_tv.setVisibility(View.INVISIBLE);
                    verfy_booleans[2] = true;
                }
                checkEnbleRegiste();
            }
        });

        //初始化注册按钮
        register_bt = (Button)findViewById(R.id.activity_registe_bt);

        //点击事件绑定
        registe_pwd_et.setOnClickListener(this);
        registe_username_et.setOnClickListener(this);
        register_bt.setOnClickListener(this);
        register_bt.setEnabled(false);
    }

    //检测是否能够注册
    public void checkEnbleRegiste(){
        if(verfy_booleans[0] && verfy_booleans[1] && verfy_booleans[2]){
            register_bt.setEnabled(true);
        }else register_bt.setEnabled(false);
    }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.activity_registe_bt:
                //密码正则验证并注册
                if(! regexVerify.registerUsernameVerfy(registe_username_et.getText().toString()) && regexVerify.pwdVerify(registe_pwd_et.getText().toString())){
                    username_error_tv.setVisibility(View.VISIBLE);
                    username_error_tv.setText(getResources().getString(R.string.format_reminder_name));
                    pwd_error_tv.setVisibility(View.INVISIBLE);
                }
                else if(! regexVerify.pwdVerify(registe_pwd_et.getText().toString()) && regexVerify.registerUsernameVerfy(registe_username_et.getText().toString())) {
                    pwd_error_tv.setVisibility(View.VISIBLE);
                    pwd_error_tv.setText(getResources().getString(R.string.format_reminder_pwd));
                    username_error_tv.setVisibility(View.INVISIBLE);
                }else if(! regexVerify.pwdVerify(registe_pwd_et.getText().toString()) && !regexVerify.registerUsernameVerfy(registe_username_et.getText().toString())) {
                    pwd_error_tv.setVisibility(View.VISIBLE);
                    pwd_error_tv.setText(getResources().getString(R.string.format_reminder_pwd));
                    username_error_tv.setVisibility(View.VISIBLE);
                    username_error_tv.setText(getResources().getString(R.string.format_reminder_name));
                }else {
                    username_error_tv.setVisibility(View.INVISIBLE);
                    pwd_error_tv.setVisibility(View.INVISIBLE);
                    RegisteRequest();
                }
                break;
        }
    }


    //登录
    private void RegisteRequest() {
        User user = new User(null, registe_username_et.getText() + "", registe_pwd_et.getText() + "","null",1000);
        Log.i(TAG,"Registe UserInfo:" + user.toString());
        /**String userInfo = registe_username_et.getText() + ":" + registe_pwd_et.getText();
        final String auth_info = "Basic " + Base64.encodeToString(userInfo.getBytes(), Base64.NO_WRAP);

        //创建Retrofit对象
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl((BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //创建网络接口实例
        PostRequest_userAuth postRequest_userAuth = retrofit.create(PostRequest_userAuth.class);

        //包装发送请求
        Call<Bean_UserAccount> call = postRequest_userAuth.getU(auth_info);*/

        final Gson gson = new Gson();
        String postContent = gson.toJson(user);
        Log.i(TAG,"Registe PostContent:" + postContent);



        //发送POST请求
        Retrofit retrofit = new Retrofit.Builder().baseUrl(this.getResources().getString(R.string.base_url)).addConverterFactory(GsonConverterFactory.create()).build();
        RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),postContent);
        PostRequest_userRegiste login = retrofit.create(PostRequest_userRegiste.class);
        Call<ResponseBody> call = login.userRegiste(requestBody);


        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.code() == 200) {
                    //注册成功后应该自动跳转到登录界面
                    try {
                        //Log.i(TAG,"body: " + response.body());
                        //Log.i(TAG,"string: " + response.body().string() + "");
                        User user = new Gson().fromJson(response.body().string() + "",User.class);
                        Log.i(TAG,"UserInfo: " + user);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(Activity_registe.this, getResources().getString(R.string.registeSucceed), Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    //在此处应该根据不同失败情况添加不同的情况判定
                    Log.i(TAG,"Response Code:" + response.code() + response.message());
                    Toast.makeText(Activity_registe.this, getResources().getString(R.string.registeFailed), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();
    }
}

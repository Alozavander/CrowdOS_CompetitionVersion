package com.hills.mcs_02.activities;

import com.google.gson.Gson;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;


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

import com.hills.mcs_02.account.RegexVerify;
import com.hills.mcs_02.BaseActivity;
import com.hills.mcs_02.dataBeans.Bean_UserAccount;
import com.hills.mcs_02.dataBeans.User;
import com.hills.mcs_02.networkClasses.interfacesPack.PostRequestUserRegister;
import com.hills.mcs_02.R;


import java.io.IOException;



public class ActivityRegister extends BaseActivity implements View.OnClickListener{
    private EditText registerUsernameEt;
    private EditText registerPwdEt;
    private EditText registePwdConfirmEt;
    private ImageView backIv;
    private Button registerBtn;
    private Toast mToast;
    private Bean_UserAccount userAccount;
    private String TAG = "LoginPage";
    private RegexVerify regexVerify;
    private TextView usernameErrorTv;
    private TextView pwdErrorTv;
    private boolean[] verifyBooleans;

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
        backIv = (ImageView)findViewById(R.id.activity_registe_back);
        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //初始化登陆界面的用户名和密码的EditText
        registerUsernameEt = (EditText)findViewById(R.id.activity_registe_username);
        registerPwdEt = (EditText)findViewById(R.id.activity_registe_password);
        registePwdConfirmEt = findViewById(R.id.activity_registe_password_confirm);
        usernameErrorTv = findViewById(R.id.activity_registe_username_error_tv);
        pwdErrorTv = findViewById(R.id.activity_registe_pwd_error_tv);
        regexVerify = new RegexVerify();
        verifyBooleans = new boolean[]{false, false, false};

        //监听账户是否设置为正确格式
        registerUsernameEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence seq, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence seq, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable edit) {
                if(! regexVerify.registerUsernameVerfy(edit.toString())){
                    usernameErrorTv.setVisibility(View.VISIBLE);
                    usernameErrorTv.setText(getResources().getString(R.string.format_reminder_name));
                    verifyBooleans[0] = false;
                }else{
                    usernameErrorTv.setVisibility(View.INVISIBLE);
                    verifyBooleans[0] = true;
                }
                checkEnbleRegister();
            }
        });

        //监听密码是否设置为正确格式
        registerPwdEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence seq, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence seq, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable edit) {
                if(! regexVerify.registerUsernameVerfy(edit.toString())){
                    pwdErrorTv.setVisibility(View.VISIBLE);
                    pwdErrorTv.setText(getResources().getString(R.string.format_reminder_name));
                    verifyBooleans[1] = false;
                }else{
                    pwdErrorTv.setVisibility(View.INVISIBLE);
                    verifyBooleans[1] = true;
                }
                checkEnbleRegister();
            }
        });

        //监听是否两次输入的密码相等
        registePwdConfirmEt.addTextChangedListener(new TextWatcher() {
            String firstPwd;
            @Override
            public void beforeTextChanged(CharSequence seq, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence seq, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                firstPwd = registerPwdEt.getText().toString();
                TextView errorTv = findViewById(R.id.activity_registe_password_confirm_error_tv);
                if(! s.toString().equals(firstPwd)) {
                    errorTv.setText(getResources().getString(R.string.differentPassword));
                    errorTv.setVisibility(View.VISIBLE);
                    verifyBooleans[2] = false;
                }else{
                    errorTv.setVisibility(View.INVISIBLE);
                    verifyBooleans[2] = true;
                }
                checkEnbleRegister();
            }
        });

        //初始化注册按钮
        registerBtn = (Button)findViewById(R.id.activity_registe_bt);

        //点击事件绑定
        registerPwdEt.setOnClickListener(this);
        registerUsernameEt.setOnClickListener(this);
        registerBtn.setOnClickListener(this);
        registerBtn.setEnabled(false);
    }

    //检测是否能够注册
    public void checkEnbleRegister(){
        if(verifyBooleans[0] && verifyBooleans[1] && verifyBooleans[2]){
            registerBtn.setEnabled(true);
        }else registerBtn.setEnabled(false);
    }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.activity_registe_bt:
                //密码正则验证并注册
                if(! regexVerify.registerUsernameVerfy(registerUsernameEt.getText().toString()) && regexVerify.pwdVerify(
                    registerPwdEt.getText().toString())){
                    usernameErrorTv.setVisibility(View.VISIBLE);
                    usernameErrorTv.setText(getResources().getString(R.string.format_reminder_name));
                    pwdErrorTv.setVisibility(View.INVISIBLE);
                }
                else if(! regexVerify.pwdVerify(registerPwdEt.getText().toString()) && regexVerify.registerUsernameVerfy(
                    registerUsernameEt.getText().toString())) {
                    pwdErrorTv.setVisibility(View.VISIBLE);
                    pwdErrorTv.setText(getResources().getString(R.string.format_reminder_pwd));
                    usernameErrorTv.setVisibility(View.INVISIBLE);
                }else if(! regexVerify.pwdVerify(registerPwdEt.getText().toString()) && !regexVerify.registerUsernameVerfy(
                    registerUsernameEt.getText().toString())) {
                    pwdErrorTv.setVisibility(View.VISIBLE);
                    pwdErrorTv.setText(getResources().getString(R.string.format_reminder_pwd));
                    usernameErrorTv.setVisibility(View.VISIBLE);
                    usernameErrorTv.setText(getResources().getString(R.string.format_reminder_name));
                }else {
                    usernameErrorTv.setVisibility(View.INVISIBLE);
                    pwdErrorTv.setVisibility(View.INVISIBLE);
                    RegisterRequest();
                }
                break;
        }
    }


    //登录
    private void RegisterRequest() {
        User user = new User(null, registerUsernameEt.getText() + "", registerPwdEt.getText() + "","null",1000);
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
        PostRequestUserRegister login = retrofit.create(PostRequestUserRegister.class);
        Call<ResponseBody> call = login.userRegister(requestBody);


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
                    } catch (IOException exp) {
                        exp.printStackTrace();
                    }
                    Toast.makeText(ActivityRegister.this, getResources().getString(R.string.registeSucceed), Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    //在此处应该根据不同失败情况添加不同的情况判定
                    Log.i(TAG,"Response Code:" + response.code() + response.message());
                    Toast.makeText(ActivityRegister.this, getResources().getString(R.string.registeFailed), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {

            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();
    }
}

package com.hills.mcs_02.activities;

import com.google.gson.Gson;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;



import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
<<<<<<< HEAD:app/src/main/java/com/hills/mcs_02/activities/Activity_login.java
import android.os.Build;
import android.os.Bundle;
=======
>>>>>>> 2e6c2ad0068725ce46b2949ae0acaa989950454e:app/src/main/java/com/hills/mcs_02/activities/ActivityLogin.java
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.os.Bundle;
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
import com.hills.mcs_02.EmailRegiste.Activity_EmailRegister;
import com.hills.mcs_02.networkClasses.interfacesPack.PostRequest_userAuth;
<<<<<<< HEAD:app/src/main/java/com/hills/mcs_02/activities/Activity_login.java
import com.hills.mcs_02.sensorFunction.SenseDataUploadService;
=======
import com.hills.mcs_02.R;


>>>>>>> 2e6c2ad0068725ce46b2949ae0acaa989950454e:app/src/main/java/com/hills/mcs_02/activities/ActivityLogin.java


public class ActivityLogin extends BaseActivity implements View.OnClickListener, View.OnFocusChangeListener,TextWatcher{
    private EditText loginUsernameEt;
    private EditText loginPwdEt;
    private ImageView backIv;
    private Button submitBtn;
    private Button registerBtn;
    private Toast mToast;
    private Bean_UserAccount userAccount;
    private String TAG = "LoginPage";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initAll();

       //

        //Toast.makeText(Activity_2rdPage.this,"pageTag: " + pageTag + ", position: " + position , Toast.LENGTH_SHORT).show();


    }

    private void initAll(){

        //初始化当前页面的回退按钮，这里用了两种不同的方法绑定点击事件
        backIv = (ImageView)findViewById(R.id.minepage_login_backarrow);
        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //初始化登陆界面的用户名和密码的EditText
        loginUsernameEt = (EditText)findViewById(R.id.minepage_login_et_username);
        loginPwdEt = (EditText)findViewById(R.id.minepage_login_et_pwd);

        //初始化提交、注册按钮
        submitBtn = (Button)findViewById(R.id.bt_login_submit);
        submitBtn.setOnClickListener(this);

        //初始化QQ、微信登录接口
        ImageView qqIv = findViewById(R.id.login_qq_iv);
        ImageView wechatIv = findViewById(R.id.login_wechat_iv);
        final Context context = this;
        qqIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context,getResources().getString(R.string.notYetOpen),Toast.LENGTH_SHORT).show();
            }
        });
        wechatIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context,getResources().getString(R.string.notYetOpen),Toast.LENGTH_SHORT).show();
            }
        });
       /* 测试用代码
       submit_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = new User(11,login_username_et.getText() + "",login_pwd_et.getText() + "","null");
                SharedPreferences user_SP = getSharedPreferences("user", MODE_PRIVATE);
                SharedPreferences.Editor editor = user_SP.edit();
                editor.putString("userID", user.getId() + "");
                editor.putString("userName", user.getUserName());
                editor.commit();
                //发送刷新Fragment_mine的广播
                Intent intent = new Intent();
                intent.setAction("action_Fragment_mine_userInfo_fresh");
                sendBroadcast(intent);
                finish();
            }
        });*/
        registerBtn = (Button)findViewById(R.id.bt_login_register);
        registerBtn.setOnClickListener(this);

        //点击事件绑定
        loginPwdEt.setOnClickListener(this);
        loginUsernameEt.setOnClickListener(this);
    }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.minepage_login_et_username:
                loginPwdEt.clearFocus();
                loginUsernameEt.setFocusableInTouchMode(true);
                loginUsernameEt.requestFocus();
                break;
            case R.id.minepage_login_et_pwd:
                loginUsernameEt.clearFocus();
                loginPwdEt.setFocusableInTouchMode(true);
                loginPwdEt.requestFocus();
                break;
            case R.id.bt_login_submit:
                if(loginPwdEt.getText().toString() == null || loginUsernameEt.getText().toString() == null) Toast.makeText(this,"用户名或密码不能为空",Toast.LENGTH_SHORT).show();
                else{
                    RegexVerify regexVerify = new RegexVerify();
                //密码正则验证并登录
                if(regexVerify.pwdVerify(loginPwdEt.getText().toString())) loginRequest();
                else {
                    TextView pwdTv = findViewById(R.id.minepage_login_pwd_error_tv);
                    pwdTv.setVisibility(View.VISIBLE);
                    pwdTv.setText("密码为字母、数字的组合，6-18位");
                    }
                }
                break;
            case R.id.bt_login_register:
                //注册
                startActivity(new Intent(ActivityLogin.this, Activity_EmailRegister.class));
                break;
            case R.id.tv_login_forget_pwd:
                //找回密码
                startActivity(new Intent(ActivityLogin.this, ActivityPwdFind.class));
                break;
        }
    }

    //用户名密码焦点改变
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        int id = v.getId();

        if (id == R.id.minepage_login_et_username) {
            if (hasFocus) {
                loginUsernameEt.setActivated(true);
                loginPwdEt.setActivated(false);
            }
        } else {
            if (hasFocus) {
                loginPwdEt.setActivated(true);
                loginUsernameEt.setActivated(false);
            }
        }
    }


    @Override
    public void beforeTextChanged(CharSequence seq, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence seq, int start, int before, int count) {

    }

    //用户名密码输入事件
    @Override
    public void afterTextChanged(Editable edit) {
        String username = loginUsernameEt.getText().toString().trim();
        String pwd = loginPwdEt.getText().toString().trim();


        /****
        //是否显示清除按钮
        if (username.length() > 0) {
            mIvLoginUsernameDel.setVisibility(View.VISIBLE);
        } else {
            mIvLoginUsernameDel.setVisibility(View.INVISIBLE);
        }
        if (pwd.length() > 0) {
            mIvLoginPwdDel.setVisibility(View.VISIBLE);
        } else {
            mIvLoginPwdDel.setVisibility(View.INVISIBLE);
        }***/

        //登录按钮是否可用
        if (!TextUtils.isEmpty(pwd) && !TextUtils.isEmpty(username)) {
            submitBtn.setBackgroundResource(R.drawable.bg_login_submit);
            submitBtn.setTextColor(getResources().getColor(R.color.white,null));
        } else {
            submitBtn.setBackgroundResource(R.drawable.bg_login_submit_lock);
            submitBtn.setTextColor(getResources().getColor(R.color.account_lock_font_color,null));
        }
    }

    /**
     * 显示Toast
     *
     * @param msg 提示信息内容
     */
    private void showToast(int msg) {
        if (null != mToast) {
            mToast.setText(msg);
        } else {
            mToast = Toast.makeText(ActivityLogin.this, msg, Toast.LENGTH_SHORT);
        }

        mToast.show();
    }


    //登录
    private void loginRequest() {
        //Base64转码用户信息，用于验证
        User user = new User(null,loginUsernameEt.getText() + "",loginPwdEt.getText() + "","null",1000);
        Log.i(TAG,user.toString());
        /**String userInfo = login_username_et.getText() + ":" + login_pwd_et.getText();
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
        Log.i(TAG,postContent);


        //发送POST请求
        Retrofit retrofit = new Retrofit.Builder().baseUrl(this.getResources().getString(R.string.base_url)).addConverterFactory(GsonConverterFactory.create()).build();
        //For test
        //Retrofit retrofit = new Retrofit.Builder().baseUrl("http://192.168.43.75:8889/").addConverterFactory(GsonConverterFactory.create()).build();
        RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),postContent);
        PostRequest_userAuth login = retrofit.create(PostRequest_userAuth.class);
        Call<ResponseBody> call = login.userLogin(requestBody);


        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                User user = null;
                if(response.code() == 200) {
                        //Log.i(TAG,"body: " + response.body());
                        //Log.i(TAG,"string: " + response.body().string() + "");
                    try {
                        response.body();
                        String temp = response.body().string() + "";
                        Log.i(TAG,"UserInfo: " + temp);
                       // Log.i(TAG,"UserInfo: " + temp);
                        user = new Gson().fromJson(temp ,User.class);
                    } catch (Exception exp) {
                        exp.printStackTrace();
                    }if(user != null) {
                        Log.i(TAG,"UserInfo: " + user);
                        Toast.makeText(ActivityLogin.this, getResources().getString(R.string.LoginSucceed), Toast.LENGTH_SHORT).show();
                        //写入一些信息方便其他子页面能够使用
                        SharedPreferences userSP = getSharedPreferences("user", MODE_PRIVATE);
                        SharedPreferences.Editor editor = userSP.edit();
                        editor.putString("userID", user.getUserId() + "");
                        Log.i(TAG, user.getUserId() + "");
                        editor.putString("userName", user.getUserName());
                        editor.commit();
                        //开启手机数据的service
                        Intent lIntent = new Intent(Activity_login.this, SenseDataUploadService.class);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            startForegroundService(lIntent);
                        } else {
                            startService(lIntent);
                        }
                        //发送刷新Fragment_mine的广播
                        Intent intent = new Intent();
                        intent.setAction("action_Fragment_mine_userInfo_login");
                        sendBroadcast(intent);
                        finish();
                    }else{
                        Toast.makeText(ActivityLogin.this, getResources().getString(R.string.LoginFailed), Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Log.i(TAG,response.code() + response.message());
                    Toast.makeText(ActivityLogin.this, getResources().getString(R.string.LoginFailed), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {

            }
        });


        /*//异步网络请求
        call.enqueue(new Callback<Bean_UserAccount>() {
            @Override
            public void onResponse(Call<Bean_UserAccount> call, Response<Bean_UserAccount> response) {
                 userAccount = response.body();
            }

            @Override
            public void onFailure(Call<Bean_UserAccount> call, Throwable throwable) {

            }
        });

        if(userAccount.getState().equals("0")){
            Toast toast = Toast.makeText(this,"用户名或密码错误",Toast.LENGTH_LONG);
        }else{
            //写入一些信息方便其他子页面能够使用
            SharedPreferences user_SP = getSharedPreferences("user",MODE_PRIVATE);
            SharedPreferences.Editor editor = user_SP.edit();
            editor.putString("userID",userAccount.getId());
            editor.putString("userName", userAccount.getName());
            editor.commit();
            //发送刷新Fragment_mine的广播
            Intent intent = new Intent();
            intent.setAction("action_Fragment_mine_userInfo_fresh");
            sendBroadcast(intent);
            //结束当前Activity
            finish();
        }*/
    }


    @Override
    protected void onStart() {
        super.onStart();
    }
}

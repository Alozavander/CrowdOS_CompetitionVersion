package com.hills.mcs_02.activities;

import com.google.gson.Gson;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
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
import com.hills.mcs_02.dataBeans.BeanUserAccount;
import com.hills.mcs_02.dataBeans.User;
import com.hills.mcs_02.EmailRegiste.ActivityEmailRegister;
import com.hills.mcs_02.networkClasses.interfacesPack.PostRequestUserAuth;
import com.hills.mcs_02.R;
import com.hills.mcs_02.sensorFunction.SenseDataUploadService;

public class ActivityLogin extends BaseActivity implements View.OnClickListener, View.OnFocusChangeListener,TextWatcher{
    private EditText loginUsernameEt;
    private EditText loginPwdEt;
    private ImageView backIv;
    private Button submitBtn;
    private Button registerBtn;
    private Toast mToast;
    private BeanUserAccount userAccount;
    private String TAG = "LoginPage";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initAll();

    }

    private void initAll(){

        /** Initializes the back button for the current page. There are two different ways to bind the click event */
        backIv = (ImageView)findViewById(R.id.minepage_login_backarrow);
        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        /** Initialize the editText for the login interface's username and password */
        loginUsernameEt = (EditText)findViewById(R.id.minepage_login_et_username);
        loginPwdEt = (EditText)findViewById(R.id.minepage_login_et_pwd);

        /** Initialize the Submit/Register button */
        submitBtn = (Button)findViewById(R.id.bt_login_submit);
        submitBtn.setOnClickListener(this);

        /** Initialize QQ, WeChat login interface */
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
        registerBtn = (Button)findViewById(R.id.bt_login_register);
        registerBtn.setOnClickListener(this);

        /** Click on the event binding */
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
                /** The password is validated and logged in */
                if(regexVerify.pwdVerify(loginPwdEt.getText().toString())) loginRequest();
                else {
                    TextView pwdTv = findViewById(R.id.minepage_login_pwd_error_tv);
                    pwdTv.setVisibility(View.VISIBLE);
                    pwdTv.setText("密码为字母、数字的组合，6-18位");
                    }
                }
                break;
            case R.id.bt_login_register:
                /**  register */
                startActivity(new Intent(ActivityLogin.this, ActivityEmailRegister.class));
                break;
            case R.id.tv_login_forget_pwd:
                /** retrieve password */
                startActivity(new Intent(ActivityLogin.this, ActivityPwdFind.class));
                break;
        }
    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        int id = view.getId();

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

    /**  Enter the username and password event */
    @Override
    public void afterTextChanged(Editable edit) {
        String username = loginUsernameEt.getText().toString().trim();
        String pwd = loginPwdEt.getText().toString().trim();

        /** Whether the login button is available */
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

    private void loginRequest() {
        User user = new User(null,loginUsernameEt.getText() + "",loginPwdEt.getText() + "","null",1000);
        Log.i(TAG,user.toString());

        final Gson gson = new Gson();
        String postContent = gson.toJson(user);
        Log.i(TAG,postContent);
        /**  Send a POST request */
        Retrofit retrofit = new Retrofit.Builder().baseUrl(this.getResources().getString(R.string.base_url)).addConverterFactory(GsonConverterFactory.create()).build();
        RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),postContent);
        PostRequestUserAuth login = retrofit.create(PostRequestUserAuth.class);
        Call<ResponseBody> call = login.userLogin(requestBody);


        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                User user = null;
                if(response.code() == 200) {
                    try {
                        response.body();
                        String temp = response.body().string() + "";
                        Log.i(TAG,"UserInfo: " + temp);
                        user = new Gson().fromJson(temp ,User.class);
                    } catch (Exception exp) {
                        exp.printStackTrace();
                    }if(user != null) {
                        Log.i(TAG,"UserInfo: " + user);
                        Toast.makeText(ActivityLogin.this, getResources().getString(R.string.LoginSucceed), Toast.LENGTH_SHORT).show();
                        /** Write some information so that other child pages can use it */
                        SharedPreferences userSP = getSharedPreferences("user", MODE_PRIVATE);
                        SharedPreferences.Editor editor = userSP.edit();
                        editor.putString("userID", user.getUserId() + "");
                        Log.i(TAG, user.getUserId() + "");
                        editor.putString("userName", user.getUsername());
                        editor.commit();
                        /** Start the phone data service */
                        Intent lIntent = new Intent(ActivityLogin .this, SenseDataUploadService.class);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            startForegroundService(lIntent);
                        } else {
                            startService(lIntent);
                        }
                        /** Send a broadcast that refreshes Fragment_mine */
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
    }


    @Override
    protected void onStart() {
        super.onStart();
    }
}

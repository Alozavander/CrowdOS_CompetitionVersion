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

import java.io.IOException;

import com.hills.mcs_02.account.RegexVerify;
import com.hills.mcs_02.BaseActivity;
import com.hills.mcs_02.dataBeans.BeanUserAccount;
import com.hills.mcs_02.dataBeans.User;
import com.hills.mcs_02.networkClasses.interfacesPack.PostRequestUserRegister;
import com.hills.mcs_02.R;

public class ActivityRegister extends BaseActivity implements View.OnClickListener{
    private EditText registerUsernameEt;
    private EditText registerPwdEt;
    private EditText registerPwdConfirmEt;
    private ImageView backIv;
    private Button registerBtn;
    private Toast mToast;
    private BeanUserAccount userAccount;
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

    }

    private void initAll(){

        /** Initializes the back button for the current page. There are two different ways to bind the click event */
        backIv = (ImageView)findViewById(R.id.activity_registe_back);
        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        /**  Initialize the editText for the login interface's username and password */
        registerUsernameEt = (EditText)findViewById(R.id.activity_registe_username);
        registerPwdEt = (EditText)findViewById(R.id.activity_registe_password);
        registerPwdConfirmEt = findViewById(R.id.activity_registe_password_confirm);
        usernameErrorTv = findViewById(R.id.activity_registe_username_error_tv);
        pwdErrorTv = findViewById(R.id.activity_registe_pwd_error_tv);
        regexVerify = new RegexVerify();
        verifyBooleans = new boolean[]{false, false, false};

        /** Listens to see if the account is formatted correctly */
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
                checkEnableRegister();
            }
        });

       /** Listens to see if the password is formatted correctly */
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
                checkEnableRegister();
            }
        });

        /**  Check if two passwords entered are the same */
        registerPwdConfirmEt.addTextChangedListener(new TextWatcher() {
            String firstPwd;
            @Override
            public void beforeTextChanged(CharSequence seq, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence seq, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable edit) {
                firstPwd = registerPwdEt.getText().toString();
                TextView errorTv = findViewById(R.id.activity_registe_password_confirm_error_tv);
                if(! edit.toString().equals(firstPwd)) {
                    errorTv.setText(getResources().getString(R.string.differentPassword));
                    errorTv.setVisibility(View.VISIBLE);
                    verifyBooleans[2] = false;
                }else{
                    errorTv.setVisibility(View.INVISIBLE);
                    verifyBooleans[2] = true;
                }
                checkEnableRegister();
            }
        });

        /** Initialize the registration button */
        registerBtn = (Button)findViewById(R.id.activity_registe_bt);

        /** Click on the event binding */
        registerPwdEt.setOnClickListener(this);
        registerUsernameEt.setOnClickListener(this);
        registerBtn.setOnClickListener(this);
        registerBtn.setEnabled(false);
    }

   /** Check if you can register */
    public void checkEnableRegister(){
        if(verifyBooleans[0] && verifyBooleans[1] && verifyBooleans[2]){
            registerBtn.setEnabled(true);
        }else registerBtn.setEnabled(false);
    }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.activity_registe_bt:
                /**  Password validation and registration */
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

    private void RegisterRequest() {
        User user = new User(null, registerUsernameEt.getText() + "", registerPwdEt.getText() + "","null",1000);
        Log.i(TAG,"Registe UserInfo:" + user.toString());

        final Gson gson = new Gson();
        String postContent = gson.toJson(user);
        Log.i(TAG,"Registe PostContent:" + postContent);

        Retrofit retrofit = new Retrofit.Builder().baseUrl(this.getResources().getString(R.string.base_url)).addConverterFactory(GsonConverterFactory.create()).build();
        RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),postContent);
        PostRequestUserRegister login = retrofit.create(PostRequestUserRegister.class);
        Call<ResponseBody> call = login.userRegister(requestBody);


        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.code() == 200) {
                    /** If you register successfully, you will be automatically redirected to the login screen */
                    try {
                        User user = new Gson().fromJson(response.body().string() + "",User.class);
                        Log.i(TAG,"UserInfo: " + user);
                    } catch (IOException exp) {
                        exp.printStackTrace();
                    }
                    Toast.makeText(ActivityRegister.this, getResources().getString(R.string.registeSucceed), Toast.LENGTH_SHORT).show();
                    finish();
                }else{

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

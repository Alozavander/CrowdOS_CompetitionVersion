package com.hills.mcs_02.EmailRegiste;

import com.google.gson.Gson;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.hills.mcs_02.dataBeans.User;
import com.hills.mcs_02.networkClasses.interfacesPack.PostRequestEmailRegisterAddressCheck;
import com.hills.mcs_02.R;

public class ActivityEmailRegister extends AppCompatActivity implements View.OnClickListener {
    private CountDownTimer mCountDownTimer;
    private Button mSendVerifyCodeBtn;
    private Button mNextBtn;
    private RegisterCodeEmail mRegisterCodeEmail;
    private String mEmailRecipient;
    private String mVerifyCode;
    private final static String REGISTER_ERROR_EMAIL_EXIST = "EXIST_ERROR";                     //已存在邮箱错误代码
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emaile_register);
        mContext = this;
        initView();
    }

    private void initView() {
        findViewById(R.id.activity_email_register_back).setOnClickListener(this);
        mSendVerifyCodeBtn = findViewById(R.id.activity_email_registe_verifynumber_send_bt);
        mSendVerifyCodeBtn.setOnClickListener(this);
        mNextBtn = findViewById(R.id.activity_email_registe_next_bt);
        mNextBtn.setOnClickListener(this);
        ((EditText) findViewById(R.id.activity_emaile_registe_email_input_et)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence seq, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence seq, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable edit) {
                /** Check the input email address */
                if (edit.toString().matches("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$")) {
                    mSendVerifyCodeBtn.setEnabled(true);
                    mEmailRecipient = edit.toString();
                } else {
                    mSendVerifyCodeBtn.setEnabled(false);
                    Toast.makeText(ActivityEmailRegister.this, "请输入正确格式的邮箱地址", Toast.LENGTH_LONG).show();
                }
            }
        });
        ((EditText) findViewById(R.id.activity_email_registe_et)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence seq, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence seq, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable edit) {
                mVerifyCode = edit.toString();
                mNextBtn.setEnabled(true);
            }
        });
        mCountDownTimer = new CountDownTimer(1000 * 60, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mSendVerifyCodeBtn.setText(millisUntilFinished / 1000 + "s后重新发送");
            }

            @Override
            public void onFinish() {
                mSendVerifyCodeBtn.setEnabled(true);
                mSendVerifyCodeBtn.setText("发送验证码");
            }
        };
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_email_registe_verifynumber_send_bt:
                mSendVerifyCodeBtn.setEnabled(false);
                mCountDownTimer.start();
                mRegisterCodeEmail = new RegisterCodeEmail();
                mRegisterCodeEmail.sendEmail(mEmailRecipient);
                Toast.makeText(ActivityEmailRegister.this, "验证码已发送", Toast.LENGTH_SHORT).show();
                break;
            case R.id.activity_email_registe_next_bt:
                if (mRegisterCodeEmail.verify(mVerifyCode)) {
                    emailRegisterRequest();
                } else
                    Toast.makeText(ActivityEmailRegister.this, "验证码错误，请重新输入或重新发送验证码", Toast.LENGTH_SHORT).show();
                break;
            case R.id.activity_email_register_back:
                finish();
                break;
        }
    }

    private void emailRegisterRequest() {
        String usernameMail = ((EditText)findViewById(R.id.activity_emaile_registe_email_input_et)).getText().toString();
        User lUser = new User();
        lUser.setUsername(usernameMail);
        Gson lGson = new Gson();
        String requestContent = lGson.toJson(lUser);
        RequestBody lRequestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),requestContent);

        Retrofit lRetrofit = new Retrofit.Builder().baseUrl(getString(R.string.base_url)).addConverterFactory(GsonConverterFactory.create()).build();

        PostRequestEmailRegisterAddressCheck lPostRequestEmailRegisterAddressCheck = lRetrofit.create(
            PostRequestEmailRegisterAddressCheck.class);
        Call<ResponseBody> call = lPostRequestEmailRegisterAddressCheck.userRegister(lRequestBody);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                switch (response.code()) {
                    case 400:
                        showRegisterErrorDialog("数据类型不正确");
                        break;
                    case 404:
                        showEmailExistDialog();
                        break;
                    case 406:
                        showRegisterErrorDialog("邮箱不正确");
                        break;
                    case 200:
                        Intent lIntent = new Intent(ActivityEmailRegister.this, ActivityEmailRegisterPasswordSet.class);
                        lIntent.putExtra("email_address", mEmailRecipient);
                        startActivity(lIntent);
                        finish();
                        break;
                    default:
                        System.out.println("Error register request code: " + response.code());
                        showRegisterErrorDialog();
                        break;
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {

            }
        });

    }

    private void showRegisterErrorDialog() {
        new AlertDialog.Builder(ActivityEmailRegister.this)
                .setTitle("注册错误")
                .setMessage("请求错误，请重试")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    private void showEmailExistDialog() {
        new AlertDialog.Builder(ActivityEmailRegister.this)
                .setTitle("注册错误")
                .setMessage("已存在该邮箱账户")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    private void showRegisterErrorDialog(String str) {
        new AlertDialog.Builder(ActivityEmailRegister.this)
                .setTitle("注册错误")
                .setMessage(str)
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }


}

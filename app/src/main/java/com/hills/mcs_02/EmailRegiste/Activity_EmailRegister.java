package com.hills.mcs_02.EmailRegiste;

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

import com.google.gson.Gson;
import com.hills.mcs_02.R;
import com.hills.mcs_02.dataBeans.User;
import com.hills.mcs_02.networkClasses.interfacesPack.PostRequest_EmailRegiste_AdressCheck;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class Activity_EmailRegister extends AppCompatActivity implements View.OnClickListener {
    private CountDownTimer mCountDownTimer;
    private Button mSend_verifyCode_bt;
    private Button mNext_bt;
    private RegisteCode_Email mRegisteCode_email;
    private String mEmail_recipient;
    private String mVerify_code;
    private final static String REGISTE_ERROR_EMAIL_EXIST = "EXIST_ERROR";                     //已存在邮箱错误代码
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
        mSend_verifyCode_bt = findViewById(R.id.activity_email_registe_verifynumber_send_bt);
        mSend_verifyCode_bt.setOnClickListener(this);
        mNext_bt = findViewById(R.id.activity_email_registe_next_bt);
        mNext_bt.setOnClickListener(this);
        ((EditText) findViewById(R.id.activity_emaile_registe_email_input_et)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //对输入的邮箱地址作正则检查
                if (s.toString().matches("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$")) {
                    mSend_verifyCode_bt.setEnabled(true);
                    mEmail_recipient = s.toString();
                } else {
                    mSend_verifyCode_bt.setEnabled(false);
                    Toast.makeText(Activity_EmailRegister.this, "请输入正确格式的邮箱地址", Toast.LENGTH_LONG).show();
                }
            }
        });
        ((EditText) findViewById(R.id.activity_email_registe_et)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mVerify_code = s.toString();
                mNext_bt.setEnabled(true);
            }
        });
        mCountDownTimer = new CountDownTimer(1000 * 60, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mSend_verifyCode_bt.setText(millisUntilFinished / 1000 + "s后重新发送");
            }

            @Override
            public void onFinish() {
                mSend_verifyCode_bt.setEnabled(true);
                mSend_verifyCode_bt.setText("发送验证码");
            }
        };
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_email_registe_verifynumber_send_bt:
                mSend_verifyCode_bt.setEnabled(false);
                mCountDownTimer.start();
                mRegisteCode_email = new RegisteCode_Email();
                mRegisteCode_email.sendEmail(mEmail_recipient);
                Toast.makeText(Activity_EmailRegister.this, "验证码已发送", Toast.LENGTH_SHORT).show();
                break;
            case R.id.activity_email_registe_next_bt:
                if (mRegisteCode_email.verify(mVerify_code)) {
                    email_registe_request();
                } else
                    Toast.makeText(Activity_EmailRegister.this, "验证码错误，请重新输入或重新发送验证码", Toast.LENGTH_SHORT).show();
                break;
            case R.id.activity_email_register_back:
                finish();
                break;
        }
    }

    private void email_registe_request() {
        //构建User
        String userName_mail = ((EditText)findViewById(R.id.activity_emaile_registe_email_input_et)).getText().toString();
        User lUser = new User();
        lUser.setUserName(userName_mail);
        Gson lGson = new Gson();
        String requestContent = lGson.toJson(lUser);
        RequestBody lRequestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),requestContent);

        Retrofit lRetrofit = new Retrofit.Builder().baseUrl(getString(R.string.base_url)).addConverterFactory(GsonConverterFactory.create()).build();
        //For Test
        //Retrofit lRetrofit = new Retrofit.Builder().baseUrl("http://192.168.43.75:8889/").addConverterFactory(GsonConverterFactory.create()).build();

        PostRequest_EmailRegiste_AdressCheck lPostRequest_emailRegiste_adressCheck = lRetrofit.create(PostRequest_EmailRegiste_AdressCheck.class);
        Call<ResponseBody> call = lPostRequest_emailRegiste_adressCheck.userRegiste(lRequestBody);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                switch (response.code()) {
                    case 400: //bad request，数据类型不正确错误
                        showRegisteErrorDialog("数据类型不正确");
                        break;
                    case 404: //bad request，数据类型不正确错误
                        showEmailExistDialog();
                        break;
                    case 406: //bad request，数据类型不正确错误
                        showRegisteErrorDialog("邮箱不正确");
                        break;
                    case 200:
                        //跳转页面
                        Intent lIntent = new Intent(Activity_EmailRegister.this, Activity_EmailRegister_PasswordSet.class);
                        lIntent.putExtra("email_address", mEmail_recipient);
                        startActivity(lIntent);
                        finish();
                        break;
                    default:
                        System.out.println("Error registe request code: " + response.code());
                        showRegisteErrorDialog();
                        break;
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }

    private void showRegisteErrorDialog() {
        new AlertDialog.Builder(Activity_EmailRegister.this)
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
        new AlertDialog.Builder(Activity_EmailRegister.this)
                .setTitle("注册错误")
                .setMessage("已存在该邮箱账户")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    private void showRegisteErrorDialog(String s) {
        new AlertDialog.Builder(Activity_EmailRegister.this)
                .setTitle("注册错误")
                .setMessage(s)
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }


}

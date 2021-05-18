package com.hills.mcs_02.EmailRegiste;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.hills.mcs_02.R;
import com.hills.mcs_02.account.RegexVerify;
import com.hills.mcs_02.dataBeans.Bean_UserAccount;
import com.hills.mcs_02.dataBeans.User;
import com.hills.mcs_02.networkClasses.interfacesPack.PostRequest_userRegiste;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Activity_EmailRegister_PasswordSet extends AppCompatActivity implements View.OnClickListener {
    private String TAG = "Activity_EmailRegister_PasswordSet";
    private EditText mPwd_set_et;
    private EditText mPwd_nicknmae_et;
    private EditText mPwd_set_confirm_et;
    private ImageView mBack_iv;
    private Button mConfirm_bt;
    private Bean_UserAccount userAccount;
    private RegexVerify regexVerify;
    private TextView mPw_input_error_tv;
    private TextView mPw_input_error_confirm_tv;
    private String mEmail_Address;
    private boolean[] mFinishTag;
    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_reigister_password_set);

        mFinishTag = new boolean[]{false, false, false};
        regexVerify = new RegexVerify();

        //获取注册的邮箱地址
        Intent lIntent = getIntent();
        mEmail_Address = lIntent.getStringExtra("email_address");

        initView();
    }

    private void initView() {
        mPwd_set_et = findViewById(R.id.activity_er_passwordset_pwinput_et);
        mPwd_nicknmae_et = findViewById(R.id.activity_er_passwordset_nickname_et);
        mPwd_set_confirm_et = findViewById(R.id.activity_er_passwordset_pwconfirm_et);
        mPw_input_error_tv = findViewById(R.id.activity_er_passwordset_pwinput_remind_tv);
        mPw_input_error_confirm_tv = findViewById(R.id.activity_er_passwordset_pwconfirm_remind_tv);
        mBack_iv = findViewById(R.id.activity_er_passwordset_back);
        mConfirm_bt = findViewById(R.id.activity_er_passwordset_complete_bt);
        mBack_iv.setOnClickListener(this);
        mConfirm_bt.setOnClickListener(this);

        bindTextWatcher();
    }

    private void bindTextWatcher() {
        //密码输入文本框的监听
        mPwd_set_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!regexVerify.registerUsernameVerfy(s.toString())) {
                    mPw_input_error_tv.setVisibility(View.VISIBLE);
                    mFinishTag[0] = false;
                } else {
                    mPw_input_error_tv.setVisibility(View.INVISIBLE);
                    mFinishTag[0] = true;
                }
                checkEnbleRegiste();
            }
        });

        //密码确认输入文本框的监听
        mPwd_set_confirm_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().equals(mPwd_set_et.getText().toString())) {
                    mPw_input_error_confirm_tv.setVisibility(View.VISIBLE);
                    mFinishTag[1] = false;
                } else {
                    mPw_input_error_confirm_tv.setVisibility(View.INVISIBLE);
                    mFinishTag[1] = true;
                }
                checkEnbleRegiste();
            }
        });

        //昵称输入的文本框监听
        mPwd_nicknmae_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString() != null) mFinishTag[2] = true;
                else mFinishTag[2] = false;
                checkEnbleRegiste();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_er_passwordset_back:
                finish();
                break;
            case R.id.activity_er_passwordset_complete_bt:
                retrofit_UserCreate();
                break;
        }
    }

    //TODO:发送用户创建网络请求到服务器，根据返回结果进行用户信息的本地化存储
    private void retrofit_UserCreate() {
        mUser = new User(null,mEmail_Address,mPwd_set_et.getText().toString(),mPwd_nicknmae_et.getText().toString(),1000);
        Retrofit lRetrofit = new Retrofit.Builder().baseUrl(getString(R.string.base_url)).addConverterFactory(GsonConverterFactory.create()).build();
        //For test
        //Retrofit lRetrofit = new Retrofit.Builder().baseUrl("http://192.168.43.75:8889/").addConverterFactory(GsonConverterFactory.create()).build();

        Gson lGson = new Gson();
        String requestContent = lGson.toJson(mUser);
        RequestBody lRequestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), requestContent);

        PostRequest_userRegiste lPostRequest_userRegiste = lRetrofit.create(PostRequest_userRegiste.class);
        Call<ResponseBody> call = lPostRequest_userRegiste.userRegiste(lRequestBody);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                switch (response.code()) {
                    case 200:
                        registeSuccess();
                        break;
                    case 400:
                        registeErrorAlert("数据类型不正确");
                        break;
                    case 502:
                        registeErrorAlert("数据内容不足");
                        break;
                    default:
                        registeErrorAlert("请求错误");
                        break;
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private void registeErrorAlert(String s) {
        new AlertDialog.Builder(Activity_EmailRegister_PasswordSet.this)
                .setTitle("注册失败")
                .setMessage(s)
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
    }

    private void registeSuccess() {
        //存储用户数据
        //user_Info_store_sp();
        Toast.makeText(Activity_EmailRegister_PasswordSet.this,"注册成功，请登录",Toast.LENGTH_LONG).show();
        finish();
    }

    //TODO：使用SP本地化存储用户信息，排除密码，默认登录
    private void user_Info_store_sp() {
        SharedPreferences sp = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("userID", mUser.getUserId() + "");
        Log.i(TAG, mUser.getUserId() + "");
        editor.putString("userName", mUser.getUserName());
        editor.commit();
        //发送刷新Fragment_mine的广播
        Intent intent = new Intent();
        intent.setAction("action_Fragment_mine_userInfo_login");
        sendBroadcast(intent);
        finish();
    }

    //检测是否能够注册
    public void checkEnbleRegiste() {
        if (mFinishTag[0] && mFinishTag[1] && mFinishTag[2]) {
            mConfirm_bt.setEnabled(true);
        } else mConfirm_bt.setEnabled(false);
    }
}

package com.hills.mcs_02.activities;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.hills.mcs_02.R;

public class ActivityPwdFind extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pwd_find);

        initView();
    }

    private void initView() {
        findViewById(R.id.activity_pwdFind_next_bt).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.activity_pwdFind_next_bt:

                break;
        }
    }
}

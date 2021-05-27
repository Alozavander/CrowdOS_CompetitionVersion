package com.hills.mcs_02;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

import com.hills.mcs_02.languageChange.MultiLanguageUtil;

public class BaseActivity extends AppCompatActivity {
    /** Language switching function */
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(MultiLanguageUtil.attachBaseContext(base));
    }
}

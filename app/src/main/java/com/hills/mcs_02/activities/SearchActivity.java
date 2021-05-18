package com.hills.mcs_02.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.hills.mcs_02.BaseActivity;
import com.hills.mcs_02.MainActivity;
import com.hills.mcs_02.R;

public class SearchActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

    }


    @Override
    protected void onStart() {
        super.onStart();

        ImageView backIv = (ImageView)findViewById(R.id.searchpage_back);
        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SearchActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}

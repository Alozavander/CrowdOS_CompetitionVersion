package com.hills.mcs_02;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class SearchActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

    }


    @Override
    protected void onStart() {
        super.onStart();

        ImageView back_iv = (ImageView)findViewById(R.id.searchpage_back);
        back_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}

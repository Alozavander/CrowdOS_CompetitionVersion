package com.hills.mcs_02.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.hills.mcs_02.BaseActivity;
import com.hills.mcs_02.InterfacePublishedTaskDetail;
import com.hills.mcs_02.R;

/** This class acts as a secondary page to start the Activity that serves as the cornerstone for the Fragment */
public class ActivitySecondPage extends BaseActivity implements InterfacePublishedTaskDetail {

    private FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2rd_page);

        /** Initializes the backout button for the current page */
        ImageView backImage = (ImageView) findViewById(R.id.activity_2rd_backarrow);
        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Intent intent = getIntent();
        String pageTag = intent.getStringExtra("pageTag");
        int position = intent.getIntExtra("position", -1);
        mFragmentManager = getSupportFragmentManager();

        initFragment(pageTag, position);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    private void initFragment(String page, int position) {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();

        switch (page) {
            case "Fragment_publish": {
                switch (position) {
                    case 1:
                        break;
                    default:
                        break;

                }
                break;
            }
            default:
                break;
        }
        transaction.commit();
        mFragmentManager.executePendingTransactions();

    }

    @Override
    public void jumpToTaskDetailPublishedActivity(String toJson) {
        Intent intent = new Intent(ActivitySecondPage.this, ActivityTaskDetailPublished.class);
        intent.putExtra("taskGson",toJson);
        startActivity(intent);
    }
}
